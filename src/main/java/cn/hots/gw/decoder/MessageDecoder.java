package cn.hots.gw.decoder;

import cn.hots.gw.domain.Port52001Message;
import cn.hots.gw.domain.Port52002Message;
import cn.hots.gw.domain.Port52003Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
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
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        // 获取当前处理的端口号
        int port = ((InetSocketAddress) ctx.channel().localAddress()).getPort();

        // 根据端口选择解析策略
        switch (port) {
            case 52001:
                out.add(parsePort52001(msg));
                break;
            case 52002:
                out.add(parsePort52002(msg));
                break;
            case 52003:
                out.add(parsePort52003(msg));
                break;
            default:
                throw new IllegalArgumentException("Unknown port");
        }
    }

    private Port52001Message parsePort52001(ByteBuf msg) {
        // 解析52001端口协议（6字节头）
        byte[] header = new byte[6];
        msg.readBytes(header);
        int bodyLen = ByteBuffer.wrap(header, 0, 4).getInt();
        String body = msg.readCharSequence(bodyLen, StandardCharsets.UTF_8).toString();
        return new Port52001Message(header, body);
    }

    private Port52002Message parsePort52002(ByteBuf msg) {
        // 解析52001端口协议（6字节头）
        byte[] header = new byte[6];
        msg.readBytes(header);
        int bodyLen = ByteBuffer.wrap(header, 0, 4).getInt();
        String body = msg.readCharSequence(bodyLen, StandardCharsets.UTF_8).toString();
        return new Port52002Message(header, body);
    }
    private Port52003Message parsePort52003(ByteBuf msg) {
        // 解析52001端口协议（6字节头）
        byte[] header = new byte[6];
        msg.readBytes(header);
        int bodyLen = ByteBuffer.wrap(header, 0, 4).getInt();
        String body = msg.readCharSequence(bodyLen, StandardCharsets.UTF_8).toString();
        return new Port52003Message(header, body);
    }
    // 类似实现其他端口解析方法...
}