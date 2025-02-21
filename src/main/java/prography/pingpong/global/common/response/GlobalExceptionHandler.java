package prography.pingpong.global.common.response;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import prography.pingpong.domain.exception.ApiException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        //e.printStackTrace();
        return ApiResponse.serverError();
    }

    @ExceptionHandler(ApiException.class)
    public ApiResponse handleApiException(ApiException e) {
        //e.printStackTrace();
        return ApiResponse.badRequest();
    }
}
