package prography.pingpong.domain.service.room;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import prography.pingpong.domain.dto.room.request.GameStart;
import prography.pingpong.domain.dto.room.request.RoomJoin;
import prography.pingpong.domain.dto.room.request.RoomLeave;
import prography.pingpong.domain.dto.room.response.RoomDetail;
import prography.pingpong.domain.dto.room.response.RoomResponse;
import prography.pingpong.domain.entity.room.*;
import prography.pingpong.domain.entity.user.*;
import prography.pingpong.domain.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRoomRepository userRoomRepository;

    @Autowired
    private EntityManager entityManager;

    private User testUser;
    private Room testRoom;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        roomRepository.deleteAll();
        userRoomRepository.deleteAll();

        // ✅ Given - 활성 유저 생성
        testUser = User.createUser(1, "testUser1", "test1@example.com", UserStatus.ACTIVE);
        userRepository.save(testUser);

        // ✅ Given - 방 생성
        testRoom = Room.create("테스트 방", testUser, RoomType.SINGLE);
        roomRepository.save(testRoom);
    }

    @Test
    @DisplayName("방 전체 조회 API 테스트")
    void 방_전체_조회_테스트() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When
        Page<RoomResponse> result = roomService.getAllRooms(0, 10);

        // Then
        assertNotNull(result, "방 목록 응답이 null이 아니어야 합니다.");
        assertTrue(result.getTotalElements() > 0, "방 목록이 비어 있으면 안 됩니다.");
    }

    @Test
    @DisplayName("방 상세 조회 API 테스트")
    void 방_상세_조회_테스트() {
        // Given
        Integer roomId = testRoom.getId();

        // When
        RoomDetail result = roomService.getRoomDetail(roomId);

        // Then
        assertNotNull(result, "방 상세 정보가 null이 아니어야 합니다.");
        assertEquals(testRoom.getTitle(), result.title(), "방 제목이 일치해야 합니다.");
        assertEquals(testRoom.getHost().getId(), result.hostId(), "방 호스트 ID가 일치해야 합니다.");
    }

    @Test
    @DisplayName("방 참가 API 테스트")
    void 방_참가_테스트() {
        // Given
        User newUser = User.createUser(2, "testUser2", "test2@example.com", UserStatus.ACTIVE);
        userRepository.save(newUser);

        RoomJoin request = new RoomJoin(newUser.getId());

        // When
        roomService.joinRoom(testRoom.getId(), request);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertTrue(userRoomRepository.existsByUserIdAndRoomId(newUser.getId(), testRoom.getId()),
                "유저가 정상적으로 방에 참가해야 합니다.");
    }

    @Test
    @DisplayName("방 나가기 API 테스트")
    void 방_나가기_테스트() {
        // Given
        User newUser = User.createUser(2, "testUser2", "test2@example.com", UserStatus.ACTIVE);
        userRepository.save(newUser);

        RoomJoin joinRequest = new RoomJoin(newUser.getId());
        roomService.joinRoom(testRoom.getId(), joinRequest);
        entityManager.flush();
        entityManager.clear();

        RoomLeave leaveRequest = new RoomLeave(newUser.getId());

        // When
        roomService.leaveRoom(testRoom.getId(), leaveRequest);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertFalse(userRoomRepository.existsByUserIdAndRoomId(newUser.getId(), testRoom.getId()),
                "유저가 정상적으로 방에서 나가야 합니다.");
    }

}
