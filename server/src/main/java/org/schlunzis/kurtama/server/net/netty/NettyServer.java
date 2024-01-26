package org.schlunzis.kurtama.server.net.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.net.INetworkServer;
import org.schlunzis.kurtama.server.net.ISession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class NettyServer implements INetworkServer {

    private final ServerHandler serverHandler;

    // Port where chat server will listen for connections.
    @Value("${kurtama.server.netty.port}")
    private int port;

    public NettyServer(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    public void sendMessage(IServerMessage serverMessage, Collection<ISession> recipients) {
        serverHandler.sendMessage(serverMessage, recipients);
    }

    @Override
    public void sendMessage(IServerMessage serverMessage) {
        serverHandler.sendMessage(serverMessage);
    }

    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup) // Set boss & worker groups
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(serverHandler);
                        }
                    });
            // Start the server.
            ChannelFuture f = b.bind(port).sync();
            log.info("Netty Server started on port: {}", port);
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Netty Server interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

