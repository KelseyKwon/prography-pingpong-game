package prography.pingpong.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import prography.pingpong.domain.dto.room.request.GameStart;
import prography.pingpong.domain.dto.room.request.RoomCreate;
import prography.pingpong.domain.dto.room.request.RoomJoin;
import prography.pingpong.domain.dto.room.request.RoomLeave;
import prography.pingpong.domain.dto.room.response.RoomDetail;
import prography.pingpong.domain.dto.room.response.RoomResponse;
import prography.pingpong.domain.service.room.RoomService;
import prography.pingpong.global.common.response.ApiResponse;

@Tag(name = "방 API", description = "방 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;
    /** 1. 방 생성 API */
    @Operation(summary = "방 생성")
    @PostMapping
    public ApiResponse<Void> createRoom(@RequestBody @Validated RoomCreate request) {
        roomService.createRoom(request);
        return ApiResponse.success();
    }

    /** 2. 방 전체 조회 API */
    @Operation(summary = "모든 방 조회")
    @GetMapping
    public ApiResponse room(@RequestParam Integer page, @RequestParam Integer size) {
        Page<RoomResponse> rooms = roomService.getAllRooms(page, size);
        return ApiResponse.success(rooms);
    }

    /** 3. 방 상세 조회 API */
    @Operation(summary = "방 상세 조회")
    @GetMapping("/{roomId}")
    public ApiResponse roomDetail(@PathVariable int roomId) {
        RoomDetail roomDetail = roomService.getRoomDetail(roomId);
        return ApiResponse.success(roomDetail);
    }

    /** 4. 방 참가 API */
    @Operation(summary = "방 참가")
    @PostMapping("/attention/{roomId}")
    public ApiResponse<Void> joinRoom(@PathVariable int roomId,
                                      @RequestBody @Validated RoomJoin request) {
        roomService.joinRoom(roomId, request);
        return ApiResponse.success();
    }

    /** 5. 방 나가기 API */
    @Operation(summary = "방 나가기")
    @PostMapping("/out/{roomId}")
    public ApiResponse<Void> outRoom(@PathVariable int roomId,
                               @RequestBody @Validated RoomLeave request) {
        roomService.leaveRoom(roomId, request);
        return ApiResponse.success();
    }

    /** 6. 게임 시작 API */
    @Operation(summary = "게임 시작")
    @PutMapping("/start/{roomId}")
    public ApiResponse<Void> startRoom(@PathVariable("roomId") int roomId,
                                 @RequestBody @Validated GameStart request) {
        roomService.startRoom(roomId, request);
        return ApiResponse.success();
    }
}
