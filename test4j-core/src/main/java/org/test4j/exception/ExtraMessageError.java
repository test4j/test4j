package org.test4j.exception;

import lombok.Getter;

/**
 * 携带额外消息的断言异常
 *
 * @author darui.wu
 * @create 2020/4/14 4:51 下午
 */
@Getter
public class ExtraMessageError extends Error {
    private String extra;

    public ExtraMessageError(String message, Throwable cause) {
        super(message + cause.getMessage(), cause);
        this.extra = message;
    }
}