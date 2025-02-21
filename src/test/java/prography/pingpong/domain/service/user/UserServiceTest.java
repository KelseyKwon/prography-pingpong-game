package prography.pingpong.domain.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import prography.pingpong.domain.dto.user.UserResponse;
import prography.pingpong.domain.entity.user.User;
import prography.pingpong.domain.entity.user.UserStatus;
import prography.pingpong.domain.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저_전체_조회_테스트")
    public void 유저_전체_조회_테스트() throws Exception {
        //given
        User user1 = User.createUser(1, "유저1", "user1@example.com", UserStatus.ACTIVE);
        userRepository.save(user1);

        User user2 = User.createUser(2, "유저2", "user2@example.com", UserStatus.NON_ACTIVE);
        userRepository.save(user2);

        //when
        Page<UserResponse> result = userService.getAllUsers(0, 10);

        //then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }
}