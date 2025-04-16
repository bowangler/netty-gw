package cn.hots.gw.protocol.exception;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/16 17:21
 */
public class ProtocolParseException extends Exception {
    // 基础构造器
    public ProtocolParseException(String message) {
        super(message);
    }

    // 带原因的构造器
    public ProtocolParseException(String message, Throwable cause) {
        super(message, cause);
    }

    // 静态工厂方法（推荐）
    public static ProtocolParseException forInvalidHeader(String header) {
        return new ProtocolParseException("Invalid protocol header: " + header);
    }
}
