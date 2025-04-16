package cn.hots.gw.decoder;

import cn.hots.gw.domain.Port52001Message;
import cn.hots.gw.domain.Port52002Message;
import cn.hots.gw.domain.Port52003Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/15 18:20
 */
public class MessageEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) {
        // 将字符串转换为字节
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        out.writeBytes(bytes);
    }
}