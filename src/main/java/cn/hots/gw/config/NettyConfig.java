package cn.hots.gw.config;

import cn.hots.gw.protocol.decoder.frame.HeaderBasedFrameDecoder;
import cn.hots.gw.protocol.decoder.frame.MessageDecoder;
import cn.hots.gw.protocol.encoder.MessageEncoder;
import cn.hots.gw.handler.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/15 18:17
 */
@Slf4j
@Configuration
public class NettyConfig {
    // 在类中添加线程池控制
    private static final Executor SERVER_EXECUTOR =
            Executors.newFixedThreadPool(3, r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);  // 设置为守护线程
                return t;
            });

    @Bean
    public CommandLineRunner runNettyServers() {
        return args -> {
            // 端口配置（可扩展为从配置文件读取）
            Map<Integer, Integer> portConfig = new HashMap<>();
            portConfig.put(52001, 4);  // 端口:包头长度
            portConfig.put(52002, 8);
            portConfig.put(52003, 10);
            log.info("Netty server starting... portSize: {}", portConfig.size());

            // 为每个端口启动独立Netty服务
//            for (Map.Entry<Integer, Integer> entry : portConfig.entrySet()) {
//                startServer(entry.getKey(), entry.getValue());
//                log.info("Netty server started on port {}", entry.getKey());
//            }
            portConfig.forEach((port, len) -> {
                SERVER_EXECUTOR.execute(() -> startServer(port, len));
            });
        };
    }

    private void startServer(int port, int headerLength) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 控制线程数
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        new Thread(() -> {
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ch.pipeline()
                                        .addLast(new HeaderBasedFrameDecoder(port)) // 拆包
                                        .addLast(new MessageDecoder())                      // 字节转字符串
                                        .addLast(new MessageEncoder())                      // 新增编码器
                                        .addLast(new MessageHandler(port));
                            }
                        });

                ChannelFuture f = b.bind(port).sync();
                log.info("✅ 端口 {} 持续监听中...", port);

                // 保持线程存活的正确方式
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.info("端口{}服务正常终止", port);
            } catch (Exception e) {
                log.error("端口{}异常: {}", port, e.getMessage());
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                log.debug("端口{}资源释放完成", port);
            }
        }, "Netty-Port-" + port).start();
    }
}