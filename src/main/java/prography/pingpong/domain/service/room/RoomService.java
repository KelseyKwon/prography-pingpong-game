package prography.pingpong.domain.service.room;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prography.pingpong.domain.dto.room.request.GameStart;
import prography.pingpong.domain.dto.room.request.RoomCreate;
import prography.pingpong.domain.dto.room.request.RoomJoin;
import prography.pingpong.domain.dto.room.request.RoomLeave;
import prography.pingpong.domain.dto.room.response.RoomDetail;
import prography.pingpong.domain.dto.room.response.RoomResponse;
import prography.pingpong.domain.entity.room.*;
import prography.pingpong.domain.entity.user.User;
import prography.pingpong.domain.entity.user.UserStatus;
import prography.pingpong.domain.exception.ApiException;
import prography.pingpong.domain.repository.RoomRepository;
import prography.pingpong.domain.repository.UserRepository;
import prography.pingpong.domain.repository.UserRoomRepository;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

    /**
     * 1. 방 생성 API
     */
    @Transactional
    public void createRoom(RoomCreate request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ApiException("RoomService.createRoom"));

        // 유저가 활성 상태인지 확인
        if (!user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new ApiException("RoomService.createRoom");
        }

        // 유저가 이미 참여 중인 방이 있는지 확인
        if (isJoinedRoom(user)) {
            throw new ApiException("RoomService.createRoom");
        }

        RoomType roomType = RoomType.valueOf(request.roomType().toUpperCase());

        // 방 생성
        Room room = Room.create(request.title(), user, roomType);
        roomRepository.save(room);

        UserRoom userRoom = UserRoom.createUserRoom(request.userId(), room.getId(), Team.RED);
        userRoomRepository.save(userRoom);
    }

    /**
     * 2. 방 전체 조회 API
     */
    @Transactional
    public Page<RoomResponse> getAllRooms(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Room> roomList = roomRepository.findAll(pageable);

        return roomList.map(RoomResponse::from);
    }

    /**
     * 3. 방 상세 조회 API
     */
    @Transactional
    public RoomDetail getRoomDetail(Integer roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("RoomService.getRoomDetail"));
        return RoomDetail.from(room);
    }

    /**
     * 4. 방 참가 API
     */
    public void joinRoom(Integer roomId, RoomJoin request) {
        // 유저 정보 조회
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ApiException("RoomService.joinRoom"));

        // 유저 상태 확인 (ACTIVE 상태만 가능)
        if (!user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new ApiException("RoomService.joinRoom");
        }

        // 유저가 이미 방에 참가했는지 확인
        if (isJoinedRoom(user)) {
            throw new ApiException("RoomService.joinRoom");
        }

        //  방 정보 조회
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("RoomService.joinRoom"));

        // 방 상태 확인 (WAIT 상태만 가능)
        if (!room.getStatus().equals(RoomStatus.WAIT)) {
            throw new ApiException("RoomService.joinRoom");
        }

        // 방이 정원 초과인지 확인
        if (isFull(room)) {
            throw new ApiException("RoomService.joinRoom");
        }

        // 유저 방에 추가
        UserRoom userRoom = UserRoom.createUserRoom(roomId, request.userId(), assignTeam(room));
        userRoomRepository.save(userRoom);
    }

    /** 5. 방 나가기 API */
    public void leaveRoom(Integer roomId, RoomLeave request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ApiException("RoomService.leaveRoom"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("RoomService.leaveRoom"));

        // 유저가 현재 방에 참가한 상태인지 확인
        if (!userRoomRepository.existsByUserIdAndRoomId(user.getId(), roomId)) {
            throw new ApiException("RoomService.leaveRoom: 유저가 이 방에 참가하지 않았습니다.");
        }

        // 방 상태가 PROGRESS 또는 FINISH면 나갈 수 없음
        if (room.getStatus() == RoomStatus.PROGRESS || room.getStatus() == RoomStatus.FINISH) {
            throw new ApiException("RoomService.leaveRoom: 진행 중이거나 완료된 방에서는 나갈 수 없습니다.");
        }

        // 호스트가 나가는 경우: 모든 참가자 제거 + 방 상태 변경
        if (room.getHost().getId().equals(user.getId())) {
            List<UserRoom> participants = userRoomRepository.findAllByRoomId(roomId);
            userRoomRepository.deleteAll(participants);  // 모든 참가자 제거
            room.changeStatus(RoomStatus.FINISH);        // 방 상태 변경
            roomRepository.save(room);
        } else {
            // 일반 참가자가 나가는 경우: 해당 유저만 제거
            userRoomRepository.deleteByUserIdAndRoomId(user.getId(), roomId);
        }
    }


    /**
     * 6. 게임 시작 API
     */
    public void startRoom(Integer roomId, GameStart request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ApiException("RoomService.startRoom"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ApiException("RoomService.startRoom");
        }

        Room room = roomRepository.findByIdAndStatus(roomId, RoomStatus.WAIT)
                .orElseThrow(() -> new ApiException("RoomService.startRoom"));

        if (!isFull(room)) {
            throw new ApiException("RoomService.startRoom");
        }

        if (!roomRepository.existsByIdAndHost(roomId, user)) {
            throw new ApiException("RoomService.startRoom");
        }

        room.startGame();
        roomRepository.save(room);

        scheduleRoomFinish(room);
    }


    /**
     * 🔹 유저가 이미 다른 방에 참여 중인지 확인
     */
    private boolean isJoinedRoom(User user) {
        return roomRepository.existsByHostIdAndStatusNot(user.getId(), RoomStatus.FINISH)
                || userRoomRepository.existsByUserId(user.getId());
    }

    /**
     * 🔹 방이 정원 초과인지 확인
     */
    public boolean isFull(Room room) {
        int currentCount = userRoomRepository.countByRoomId(room.getId());

        return (room.getRoomtype() == RoomType.SINGLE && currentCount >= 2)
                || (room.getRoomtype() == RoomType.DOUBLE && currentCount >= 4);
    }


    /**
     * 팀 배정하기
     */
    public Team assignTeam(Room room) {
        // ✅ 현재 방에서 RED팀과 BLUE팀 인원 수를 조회
        int redCount = userRoomRepository.countByRoomIdAndTeam(room.getId(), Team.RED);
        int blueCount = userRoomRepository.countByRoomIdAndTeam(room.getId(), Team.BLUE);

        // ✅ 인원이 적은 팀에 배정, 동률이면 RED팀 우선
        return (redCount <= blueCount) ? Team.RED : Team.BLUE;
    }

    /**
     * 1분 후 자동으로 FINISH 상태로 변경하는 스케줄러
     */
    private void scheduleRoomFinish(Room room) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                room.finishGame();
                roomRepository.save(room);
            }
        }, 60 * 1000);
    }
}

