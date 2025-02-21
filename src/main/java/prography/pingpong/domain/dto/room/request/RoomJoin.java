package prography.pingpong.domain.dto.room.request;

import jakarta.validation.constraints.NotNull;

public record RoomJoin(
        @NotNull
        Integer userId) {
}
