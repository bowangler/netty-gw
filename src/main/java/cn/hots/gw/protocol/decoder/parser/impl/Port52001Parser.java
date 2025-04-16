package cn.hots.gw.protocol.decoder.parser.impl;

import cn.hots.gw.protocol.decoder.parser.ProtocolParser;
import cn.hots.gw.protocol.exception.ProtocolParseException;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/16 16:15
 */
public class Port52001Parser implements ProtocolParser {
    @Override
    public String parse (ByteBuf in) throws ProtocolParseException {
        try {
            String header = in.readCharSequence(4, StandardCharsets.UTF_8).toString();
            int bodyLength = Integer.parseInt(header.trim());
            if (bodyLength <=0) {
                throw ProtocolParseException.forInvalidHeader(header);
            }
            String body = in.readCharSequence(bodyLength, StandardCharsets.UTF_8).toString();
            return header + body;
        } catch (NumberFormatException e) {
            throw new ProtocolParseException("Header must be numeric", e);
        }
    }
}
