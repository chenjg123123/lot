package com.example.model.common;

import lombok.Data;

/**
 * 通用返回对象
 */
@Data
public class R<T> {
    private int code;
    private String message;
    private T data;

    public R() {}

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功返回（带数据）
    public static <T> R<T> success(String message,T data) {
        return new R<>(200, message, data);
    }

    // 成功返回（无数据）
    public static <T> R<T> success() {
        return new R<>(200, "成功", null);
    }

    // 失败返回
    public static <T> R<T> error(int code, String message) {
        return new R<>(code, message, null);
    }

    // 失败返回（默认错误码）
    public static <T> R<T> error(String message) {
        return new R<>(500, message, null);
    }

    // 自定义返回
    public static <T> R<T> build(int code, String message, T data) {
        return new R<>(code, message, data);
    }

    public static R<?> fail(int code, String message) {
        return R.error(code, message);
    }


}
