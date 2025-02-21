package prography.pingpong.domain.dto.user;

import prography.pingpong.domain.entity.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record UserResponse(
        int id,
        int fakerId,
        String name,
        String email,
        String status,
        String created_at,
        String updated_at
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFakerId(),
                user.getName(),
                user.getEmail(),
                user.getStatus().name(),
                formatDateTime(user.getBaseTime().getCreatedat()),
                formatDateTime(user.getBaseTime().getUpdatedat())
        );
    }

    private static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
}