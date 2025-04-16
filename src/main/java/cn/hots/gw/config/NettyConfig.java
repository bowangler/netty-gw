package cn.hots.gw.config;

import cn.hots.gw.decoder.HeaderBasedFrameDecoder;
import cn.hots.gw.decoder.MessageDecoder;
import cn.hots.gw.handler.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/15 18:17
 */
@Configuration
public class NettyConfig {

    @Bean
    public CommandLineRunner runNettyServers() {
        return args -> {
            // 端口配置（可扩展为从配置文件读取）
            Map<Integer, Integer> portConfig = new HashMap<>();
            portConfig.put(52001, 6);  // 端口:包头长度
            portConfig.put(52002, 8);
            portConfig.put(52003, 10);

            // 为每个端口启动独立Netty服务
            for (Map.Entry<Integer, Integer> entry : portConfig.entrySet()) {
                startServer(entry.getKey(), entry.getValue());
            }
        };
    }

    private void startServer(int port, int headerLength) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 根据包头长度动态配置Pipeline
                            ch.pipeline().addLast(
                                    new HeaderBasedFrameDecoder(headerLength),
                                    new MessageDecoder(),
                                    new MessageHandler(port)
                            );
                        }
                    });

            b.bind(port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}