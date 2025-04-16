package cn.hots.gw.protocol.decoder.frame;

import cn.hots.gw.protocol.model.Port52001Message;
import cn.hots.gw.protocol.model.Port52002Message;
import cn.hots.gw.protocol.model.Port52003Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/15 18:20
 */
public class MessageDecoder extends MessageToMessageDecoder<String> {
    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) {
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

    private Port52001Message parsePort52001(String msg) {
        // 读取完整ASCII包头（HeaderBasedFrameDecoder已确保可用）
        String header = msg.substring(0, 4);

        // 读取包体内容
        String body = msg.substring(4);
        return new Port52001Message(header, body);
    }

    private Port52002Message parsePort52002(String msg) {
        // 解析52002端口协议（6字节头）
        String header = msg.substring(0, 6);

        // 读取包体内容
        String body = msg.substring(6);
        return new Port52002Message(header, body);
    }
    private Port52003Message parsePort52003(String msg) {
        // 解析52002端口协议（8字节头）
        String header = msg.substring(0, 8);

        // 读取包体内容
        String body = msg.substring(8);
        return new Port52003Message(header, body);
    }
    // 类似实现其他端口解析方法...
}