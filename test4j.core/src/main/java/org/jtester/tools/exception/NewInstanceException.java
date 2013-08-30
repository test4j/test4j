package org.jtester.tools.exception;

/**
 * 创建newInstance()实例异常
 * 
 * @author darui.wudr 2013-1-11 上午12:17:29
 */
public class NewInstanceException extends RuntimeException {
    private static final long serialVersionUID = -3181479465495833383L;

    public NewInstanceException() {
        super();
    }

    public NewInstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewInstanceException(String message) {
        super(message);
    }

    public NewInstanceException(Throwable cause) {
        super(cause);
    }
}
