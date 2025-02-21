package prography.pingpong.domain.dto.room.response;
import org.springframework.data.domain.Page;

import java.util.List;

public record RoomList (
        int totalElements,
        int totalPages,
        List<RoomResponse> roomList
) {
    public static RoomList of(Page<RoomResponse> page) {
        return new RoomList(
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getContent()
        );
    }
}
