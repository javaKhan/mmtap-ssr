package com.mmtap.common;

/**
 * @author mmtap.com
 * @date 2019/1/9
 **/
public class ResultGenerator {

    private static final Result<?> NO_CONTENT = Result.of(Result.Status.NO_CONTENT, null, null);

    private static final Result<?> FAIL = Result.of(Result.Status.FAILURE, null, null);

    private static final Result<?> OK = ok(null);

    public static <T> Result<T> ok() {
        return (Result<T>)OK;
    }

    public static <T> Result<T> noContent() {
        return (Result<T>)NO_CONTENT;
    }

    public static <T> Result<T> created(T content) {
        return Result.of(Result.Status.CREATED, content, null);
    }

    public static <T> Result<T> ok(T content) {
        return Result.of(Result.Status.SUCCESS, content, null);
    }

    public static <T> Result<T> fail() {
        return (Result<T>)FAIL;
    }

    public static <T> Result<T> fail(String msg) {
        return Result.of(Result.Status.OTHER_FAILURE,msg,null,null);
    }
}
