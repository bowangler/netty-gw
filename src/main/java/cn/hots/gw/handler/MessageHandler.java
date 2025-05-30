package cn.hots.gw.handler;

import cn.hots.gw.protocol.model.Port52001Message;
import cn.hots.gw.protocol.model.Port52002Message;
import cn.hots.gw.protocol.model.Port52003Message;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;


/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/15 18:22
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<Object> {
    private final int port;

    public MessageHandler(int port) {
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        switch (port) {
            case 52001:
                handlePort52001(ctx, (Port52001Message) msg);
                break;
            case 52002:
                handlePort52002(ctx, (Port52002Message) msg);
                break;
            case 52003:
                handlePort52003(ctx, (Port52003Message) msg);
                break;
        }
    }

    private void handlePort52001(ChannelHandlerContext ctx, Port52001Message msg) {
        // 处理52001端口的业务逻辑
        // 示例msg内容：0004test

        log.info("端口[{}]收到请求 | 包头:{} | 内容:{}", port, msg.getHeader(), msg.getBody());

        // 构造响应（0007success）
        String response = String.format("%04d%s", "success".length(), "success");
        ctx.writeAndFlush(response);
    }

    private void handlePort52002(ChannelHandlerContext ctx, Port52002Message msg) {
        // 处理52001端口的业务逻辑
        String response = "PORT52001_ACK:" + msg.getBody().toUpperCase();
        ctx.writeAndFlush(Unpooled.copiedBuffer(response, StandardCharsets.UTF_8));
    }
    private void handlePort52003(ChannelHandlerContext ctx, Port52003Message msg) {
        // 处理52001端口的业务逻辑
        String response = "PORT52001_ACK:" + msg.getBody().toUpperCase();
        ctx.writeAndFlush(Unpooled.copiedBuffer(response, StandardCharsets.UTF_8));
    }
    // 其他端口处理方法...
}