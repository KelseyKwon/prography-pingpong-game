package prography.pingpong.domain.service.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prography.pingpong.domain.dto.room.request.TeamChange;
import prography.pingpong.domain.entity.room.*;
import prography.pingpong.domain.exception.ApiException;
import prography.pingpong.domain.repository.RoomRepository;
import prography.pingpong.domain.repository.UserRoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;

    /** * 팀 변경 API */
    public void changeTeam(Integer roomId, TeamChange request) {
        // 1. 방 존재 여부 확인
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("TeamService.changeTeam"));

        // 2. 요청한 유저가 해당 방에 참가 중인지 확인
        List<UserRoom> userRooms = userRoomRepository.findAllByRoomId(roomId);
        UserRoom userRoom = userRooms.stream()
                .filter(ur -> ur.getUserId().equals(request.userId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("TeamService.changeTeam"));

        // 3. 현재 방의 상태가 WAIT인지 확인
        if (!room.getStatus().equals(RoomStatus.WAIT)) {
            throw new ApiException("TeamService.changeTeam"); // 201 응답
        }

        // 4. 현재 속한 팀 확인
        Team currentTeam = userRoom.getTeam();
        Team newTeam = (currentTeam == Team.RED) ? Team.BLUE : Team.RED;

        // 5. 변경될 팀의 인원이 정원의 절반과 같은지 확인
        long newTeamSize = userRooms.stream()
                .filter(ur -> ur.getTeam() == newTeam)
                .count();

        int maxTeamSize = (room.getRoomtype() == RoomType.SINGLE) ? 1 : 2;
        if (newTeamSize >= maxTeamSize) {
            throw new ApiException("TeamService.changeTeam"); // 201 응답
        }

        // 6. 팀 변경 후 저장
        userRoom.changeTeam();
        userRoomRepository.save(userRoom);

    }
}
