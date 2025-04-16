package cn.hots.gw.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
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
    private final int headerLength;

    public HeaderBasedFrameDecoder(int headerLength) {
        this.headerLength = headerLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 第一阶段：检查包头长度
        if (in.readableBytes() < headerLength) {
            return;
        }

        // 标记当前读指针位置
        in.markReaderIndex();
        try {
            // 第二阶段：读取并解析包头
            String header = in.readCharSequence(headerLength, StandardCharsets.UTF_8).toString();
            int bodyLength = Integer.parseInt(header);


            // 第四阶段：验证数据完整性
            if (in.readableBytes() < bodyLength) {
                return;
            }

            // 第五阶段：提取完整数据帧
            String body = in.readCharSequence(bodyLength, StandardCharsets.UTF_8).toString();
            out.add(header + body);

        } catch (NumberFormatException e) {
            // 包头解析失败时关闭连接
            in.resetReaderIndex();
            log.error("Header parse error", e);
            ctx.close();
        }
    }
}