package prography.pingpong.domain.service.init;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import prography.pingpong.domain.dto.init.FakerApiResponse;
import prography.pingpong.domain.entity.user.User;
import prography.pingpong.domain.exception.ApiException;
import prography.pingpong.domain.repository.RoomRepository;
import prography.pingpong.domain.repository.UserRepository;
import prography.pingpong.domain.repository.UserRoomRepository;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final WebClient webClient;

    // ==== 비즈니스 로직 === //
    @Transactional
    public void initializeData(int seed, int quantity) {
        // 기존 데이터 삭제
        userRepository.deleteAll();
        roomRepository.deleteAll();
        userRoomRepository.deleteAll();

        // 외부 API 호출
        FakerApiResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/users")
                        .queryParam("_seed", seed)
                        .queryParam("_quantity", quantity)
                        .queryParam("_locale", "ko_KR")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ApiException("API 호출 실패: " + errorBody)))
                )
                .bodyToMono(FakerApiResponse.class)
                .block();

        if (response == null || response.code() != 200 || response.data() == null) {
            throw new ApiException("InitService.initializeData");
        }

        // 중복 제거 후 저장
        List<User> users = response.toUserList();
        users.stream()
                .filter(user -> !userRepository.existsByFakerId(user.getFakerId()))
                .forEach(userRepository::save);
    }

}
