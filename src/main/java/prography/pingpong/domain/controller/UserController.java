package prography.pingpong.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prography.pingpong.domain.dto.user.UserListResponse;
import prography.pingpong.domain.dto.user.UserResponse;
import prography.pingpong.domain.service.user.UserService;
import prography.pingpong.global.common.response.ApiResponse;

@Tag(name = "유저 API", description = "유저 관련 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 전체 조회")
    @GetMapping
    public ApiResponse<UserListResponse> getAllUsers(@RequestParam int page, @RequestParam int size) {
        Page<UserResponse> users = userService.getAllUsers(page, size);
        return ApiResponse.success(UserListResponse.of(users));
    }

}
