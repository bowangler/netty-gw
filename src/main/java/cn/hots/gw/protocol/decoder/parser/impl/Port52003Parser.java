package cn.hots.gw.protocol.decoder.parser.impl;

import cn.hots.gw.protocol.decoder.parser.ProtocolParser;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/16 16:15
 */
public class Port52003Parser implements ProtocolParser {
    @Override
    public String parse(ByteBuf in) {
        String header = in.readCharSequence(6, StandardCharsets.UTF_8).toString();
        int bodyLength = Integer.parseInt(header.substring(2).trim());
        String body = in.readCharSequence(bodyLength, StandardCharsets.UTF_8).toString();
        return header + body;
    }

}