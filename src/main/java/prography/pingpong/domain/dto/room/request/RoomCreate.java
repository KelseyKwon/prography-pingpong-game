package prography.pingpong.domain.dto.room.request;

import jakarta.validation.constraints.NotNull;

public record RoomCreate(
        @NotNull int userId,
        @NotNull
        String roomType,
        @NotNull String title
) {
}
