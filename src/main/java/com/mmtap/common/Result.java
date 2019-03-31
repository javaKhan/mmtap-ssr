package com.mmtap.common;

/**
 * @author mmtap.com
 * @date 2019/1/9
 **/

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * @author mmtap.com
 * @date 2019/01/09
 */
@Slf4j
public class Result<T> extends ResponseEntity<Result.ResultBody> {

    private Result(ResultBody body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    static <T> Result<T> of(Status status, T content, MultiValueMap<String, String> headers) {
        return of(status,status.getMsg(),content,headers);
    }

    static <T> Result<T> of(Status status,String msg, T content, MultiValueMap<String, String> headers) {
        ResultBody body = new ResultBody(status.getCode(), msg, content);
        log.debug("[HttpStatus={}] resultBody :=> {}", status.getHttpStatus(), JSON.toJSONString(body));
        return new Result<>(body, headers, status.getHttpStatus());
    }

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ResultBody {
        private Integer status;

        private String msg;

        private Object content;
    }

    @Getter
    @AllArgsConstructor
    enum Status {
        /**
         * 操作成功
         */
        SUCCESS(0, HttpStatus.OK, "操作成功"),

        /**
         * 创建成功
         */
        CREATED(2100, HttpStatus.CREATED, "创建成功"),

        /**
         * 无内容
         */
        NO_CONTENT(2200, HttpStatus.NO_CONTENT, "无内容"),

        /**
         * 操作失败
         */
        FAILURE(-1, HttpStatus.NOT_FOUND, "操作失败"),

        /**
         * 自定义错误
         */
        OTHER_FAILURE(-2, HttpStatus.NOT_FOUND, "");

        Integer code;

        HttpStatus httpStatus;

        String msg;
    }

}
