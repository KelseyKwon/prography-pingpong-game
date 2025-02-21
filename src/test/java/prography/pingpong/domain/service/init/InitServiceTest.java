package prography.pingpong.domain.service.init;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import prography.pingpong.domain.dto.init.InitRequest;
import prography.pingpong.domain.repository.RoomRepository;
import prography.pingpong.domain.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 기존의 모든 데이터가 삭제되어야 한다.
 * body로 전달받은 정보들을 회원 정보로 저장해야 한다.
 * fakerapi의 응답 결과로 나온 데이터를 세팅해야 한다.
 */
@SpringBootTest
@Transactional
class InitServiceTest {
    @Autowired
    private InitService initService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @DisplayName("데이터_초기화_테스트")
    public void 데이터초기화_성공() throws Exception {
        // Given
        InitRequest request = new InitRequest();
        request.setSeed(123);
        request.setQuantity(10);

        // When
        initService.initializeData(request.getSeed(), request.getQuantity());

        // Then
        assertFalse(userRepository.findAll().isEmpty(), "유저 데이터가 저장되어야 합니다.");
    }

    @Test
    @DisplayName("기존_데이터_삭제_확인")
    public void 기존데이터_삭제확인() throws Exception {
        initService.initializeData(123, 10);
        assertEquals(10, userRepository.count(), "초기 데이터가 있어야 합니다.");

        initService.initializeData(456, 5);
        assertEquals(5, userRepository.findAll().size(), "새로운 데이터만 남아야 합니다.");
    }
}