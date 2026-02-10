package com.xkb.couple.core.common.exception;
import com.xkb.couple.core.common.constants.ErrorCodeEnum;
import com.xkb.couple.core.common.resp.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author xuwatermelon
 * @date 2026/02/06
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage(), e);
        ErrorCodeEnum errorCodeEnum = e.getErrorCodeEnum();
        return BaseResponse.fail(errorCodeEnum);
    }
    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        //找到最后一个message并切除：.lastIndexOf(":")
            String errorMessage = e.getMessage().substring(e.getMessage().lastIndexOf("message")+9, e.getMessage().length()-3 );
        log.warn("参数校验异常: {}", errorMessage);
        return BaseResponse.fail(errorMessage);
    }
    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse<?> handleSystemException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return BaseResponse.fail(ErrorCodeEnum.FAIL);
    }
}

