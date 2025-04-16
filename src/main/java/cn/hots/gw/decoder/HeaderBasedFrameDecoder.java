package cn.hots.gw.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/15 18:20
 */
public class HeaderBasedFrameDecoder extends ByteToMessageDecoder {
    private final int headerLength;

    public HeaderBasedFrameDecoder(int headerLength) {
        this.headerLength = headerLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < headerLength) {
            return; // 等待数据
        }

        // 读取包头（示例：包头包含包体长度信息）
        byte[] headerBytes = new byte[headerLength];
        in.readBytes(headerBytes);

        // 假设包头前4字节为包体长度（具体协议需调整）
        int bodyLength = ByteBuffer.wrap(headerBytes, 0, 4).getInt();

        if (in.readableBytes() < bodyLength) {
            in.resetReaderIndex();
            return;
        }

        // 组合完整消息
        ByteBuf fullMsg = Unpooled.buffer(headerLength + bodyLength);
        fullMsg.writeBytes(headerBytes);
        fullMsg.writeBytes(in.readBytes(bodyLength));

        out.add(fullMsg);
    }
}