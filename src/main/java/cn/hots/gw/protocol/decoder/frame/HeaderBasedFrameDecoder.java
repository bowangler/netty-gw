package cn.hots.gw.protocol.decoder.frame;

import cn.hots.gw.protocol.decoder.parser.ProtocolParser;
import cn.hots.gw.protocol.decoder.parser.ProtocolParserFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/15 18:20
 */
@Slf4j
public class HeaderBasedFrameDecoder extends ByteToMessageDecoder {
    private final ProtocolParser parser;

    // 通过端口号初始化对应解析器
    public HeaderBasedFrameDecoder(int port) {
        this.parser = ProtocolParserFactory.getParser(port);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {
            String msg = parser.parse(in);
            out.add(msg);
        } catch (Exception e) {
//            log.error("Protocol parse failed", e);
//            ctx.close();
            // 记录错误日志（包含远程IP信息）
            String clientIp = ((InetSocketAddress)ctx.channel().remoteAddress())
                    .getAddress().getHostAddress();
            log.error("Protocol violation from {}: {}", clientIp, e.getMessage());

            // 发送错误响应并关闭连接
            ByteBuf errorResp = Unpooled.copiedBuffer(
                    "PROTOCOL_ERROR", StandardCharsets.UTF_8);
            ctx.writeAndFlush(errorResp)
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }
}