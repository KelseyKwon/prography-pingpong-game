package prography.pingpong.global.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prography.pingpong.global.common.response.ApiResponse;

@RestController
@RequestMapping("/health")
public class HealthCheckController {
    @GetMapping
    public ApiResponse healthCheck() {
        return ApiResponse.success();
    }
}