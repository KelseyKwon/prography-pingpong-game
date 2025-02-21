package prography.pingpong.domain.service.room;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import prography.pingpong.domain.dto.room.request.TeamChange;
import prography.pingpong.domain.entity.room.*;
import prography.pingpong.domain.entity.user.User;
import prography.pingpong.domain.entity.user.UserStatus;
import prography.pingpong.domain.exception.ApiException;
import prography.pingpong.domain.repository.RoomRepository;
import prography.pingpong.domain.repository.UserRepository;
import prography.pingpong.domain.repository.UserRoomRepository;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRoomRepository userRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private Room testRoom;
    private UserRoom userRoom;
    private User testUser;

    @BeforeEach
    void setUp() {
        // DB 초기화
        userRoomRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();

        // ✅ Given: 테스트 유저 생성
        testUser = User.createUser(1, "testUser", "test@example.com", UserStatus.ACTIVE);
        userRepository.save(testUser);

        // ✅ Given: 방 생성 후 저장
        testRoom = Room.create("테스트 방", testUser, RoomType.DOUBLE);
        testRoom.changeStatus(RoomStatus.WAIT);
        roomRepository.save(testRoom);

        // ✅ Given: 유저 방 참여 후 저장
        userRoom = UserRoom.createUserRoom(testUser.getId(), testRoom.getId(), Team.RED);
        userRoomRepository.save(userRoom);

        // 데이터베이스 반영
        entityManager.flush();
    }

    @Test
    @DisplayName("팀 변경 성공")
    void 팀_변경_성공() {
        // Given
        TeamChange request = new TeamChange(testUser.getId());

        // When
        teamService.changeTeam(testRoom.getId(), request);

        // Then
        UserRoom updatedUserRoom = userRoomRepository.findById(userRoom.getId()).orElseThrow();
        assertEquals(Team.BLUE, updatedUserRoom.getTeam(), "팀이 BLUE로 변경되어야 합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 방 - 예외 발생")
    void 팀_변경_실패_방_없음() {
        // When & Then
        assertThrows(ApiException.class, () -> teamService.changeTeam(999, new TeamChange(testUser.getId())));
    }
}
