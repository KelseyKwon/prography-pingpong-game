package prography.pingpong.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import prography.pingpong.domain.dto.room.request.TeamChange;
import prography.pingpong.domain.service.room.TeamService;
import prography.pingpong.global.common.response.ApiResponse;

@Tag(name = "팀 API", description = "팀 관련 API")
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 변경")
    @PutMapping("/{roomId}")
    public ApiResponse<Void> changeTeam(@PathVariable Integer roomId, @RequestBody @Validated TeamChange request) {
        teamService.changeTeam(roomId, request);
        return ApiResponse.success();
    }
}
