package prography.pingpong.domain.dto.room.response;

import prography.pingpong.domain.entity.room.Room;
import prography.pingpong.domain.entity.room.RoomType;

public record RoomResponse (
        Integer id,
        String title,
        Integer hostId,
        RoomType room_type,
        String status
) {
    public static RoomResponse from(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getTitle(),
                room.getHost().getId(),
                room.getRoomtype(),
                room.getStatus().name()
        );
    }
}
