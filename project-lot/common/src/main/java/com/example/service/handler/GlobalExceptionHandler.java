package com.example.service.handler;


import com.example.model.common.R;


import com.example.service.exception.JsonAnalyseException;
import com.example.service.exception.RSABusinessException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RSABusinessException.class)
    public R<?> handleBusinessException(RSABusinessException ex) {
        return R.fail(ex.getCode(), "账号密码异常");
    }

    @ExceptionHandler(Exception.class)
    public R<?> handleUnknownException(Exception ex) {
        log.warn("系统未知异常", ex);
        return R.fail(500, "系统异常");
    }

    @ExceptionHandler(JsonAnalyseException.class)
    public R<?> handleJsonAnalyseException(JsonAnalyseException ex) {
        log.warn("数据异常", ex);
        return R.fail(500, "数据异常");
    }
}

