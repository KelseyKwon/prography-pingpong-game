package prography.pingpong.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prography.pingpong.domain.dto.init.InitRequest;
import prography.pingpong.domain.service.init.InitService;
import prography.pingpong.global.common.response.ApiResponse;

@Tag(name = "초기화 API", description = "초기화 관련 API")
@RestController
@RequestMapping("/init")
public class InitController {
    private final InitService initService;

    public InitController(InitService initService) {
        this.initService = initService;
    }

    @Operation(summary = "초기화")
    @PostMapping
    public ApiResponse<Void> initializeData(@RequestBody InitRequest request) throws Exception{
        initService.initializeData(request.getSeed(), request.getQuantity());
        return ApiResponse.success();
    }
}