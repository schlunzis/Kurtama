package de.schlunzis.server.net.impl;

import de.schlunzis.common.messages.ServerMessage;
import de.schlunzis.server.net.NetworkServer;
import de.schlunzis.server.net.Session;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class NettyServer implements NetworkServer {

    // Port where chat server will listen for connections.

    private final ServerHandler serverHandler;

    @Value("${chat.server.port}")
    private int port;

    public NettyServer(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    public void sendMessage(ServerMessage serverMessage, Collection<Session> recipients) {
        serverHandler.sendMessage(serverMessage, recipients);
    }

    @Override
    public void sendMessage(ServerMessage serverMessage) {
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
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

