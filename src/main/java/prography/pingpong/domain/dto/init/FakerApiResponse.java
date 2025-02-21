package prography.pingpong.domain.dto.init;

import prography.pingpong.domain.entity.user.User;
import prography.pingpong.domain.entity.user.UserStatus;

import java.util.List;
import java.util.Optional;

public record FakerApiResponse(
        String status,
        Integer code,
        Integer total,
        List<FakerUserDto> data
) {
    public record FakerUserDto(
            Integer id,
            String username,
            String email
    ) {
        public User createUser() {
            return User.createUser(id, username, email, UserStatus.determineStatus(id));
        }
    }

    public List<User> toUserList() {
        return Optional.ofNullable(data)
                .orElse(List.of())
                .stream()
                .map(FakerUserDto::createUser)
                .toList();
    }
}