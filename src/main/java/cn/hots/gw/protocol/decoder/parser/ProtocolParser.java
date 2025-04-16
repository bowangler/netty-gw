package cn.hots.gw.protocol.decoder.parser;

import io.netty.buffer.ByteBuf;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/16 16:10
 */
public interface ProtocolParser {
    /**
     * 从ByteBuf解析出完整消息
     * @param in 输入缓冲区
     * @return 解析后的消息对象（包含包头和包体）
     */
    String parse(ByteBuf in);

    class ParsedMessage {
        private final String header;
        private final String body;

        public ParsedMessage(String header, String body) {
            this.header = header;
            this.body = body;
        }
        public String getHeader() { return header; }
        public String getBody() { return body; }
    }
}
