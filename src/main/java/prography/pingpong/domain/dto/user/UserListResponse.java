package prography.pingpong.domain.dto.user;

import org.springframework.data.domain.Page;

import java.util.List;

public record UserListResponse(
        int totalElements,
        int totalPages,
        List<UserResponse> userList
) {
    public static UserListResponse of(Page<UserResponse> page) {
        return new UserListResponse(
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getContent()
        );
    }
}
