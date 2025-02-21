package prography.pingpong.domain.dto.room.response;

import prography.pingpong.domain.entity.room.Room;
import prography.pingpong.domain.entity.room.RoomStatus;
import prography.pingpong.domain.entity.room.RoomType;

import java.time.LocalDateTime;

public record RoomDetail (
        Integer id,
        String title,
        Integer hostId,
        RoomType room_type,
        RoomStatus status,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
    public static RoomDetail from(Room room) {
        return new RoomDetail(
                room.getId(),
                room.getTitle(),
                room.getHost().getId(),
                room.getRoomtype(),
                room.getStatus(),
                room.getBaseTime().getCreatedat(),
                room.getBaseTime().getUpdatedat()
        );
    }
}
