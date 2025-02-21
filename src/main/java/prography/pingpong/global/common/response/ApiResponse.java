package prography.pingpong.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private Integer code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 성공 응답 ; 데이터 포함
    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(200, "API 요청이 성공했습니다.", result);
    }

    //성공 응답 ; 데이터 없음
    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> badRequest(T result) {
        return new ApiResponse<>(201, "불가능한 요청입니다.", null);
    }

    public static ApiResponse<Void> badRequest() {
        return badRequest(null);
    }

    public static <T> ApiResponse<T> serverError(T result) {
        return new ApiResponse<>(500, "에러가 발생했습니다.", null);
    }

    public static ApiResponse<Void> serverError() {
        return serverError(null);
    }
}
