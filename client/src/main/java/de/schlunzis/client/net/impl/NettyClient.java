package de.schlunzis.client.net.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.schlunzis.client.net.NetworkClient;
import de.schlunzis.common.messages.ClientMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class NettyClient implements NetworkClient {

    private final EventLoopGroup group;
    private final Bootstrap b;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${chat.server.port}")
    private int port;
    @Value("${chat.server.host}")
    private String host;
    private ChannelFuture f;

    public NettyClient(ClientHandler clientHandler) {
        group = new NioEventLoopGroup();

        b = new Bootstrap();
        b.group(group) // Set EventLoopGroup to handle all events for client.
                .channel(NioSocketChannel.class)// Use NIO to accept new connections.
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new StringDecoder());
                        p.addLast(new StringEncoder());
                        // This is our custom client handler which will have logic for chat.
                        p.addLast(clientHandler);
                    }
                });
    }

    public void start() {

        // TODO reorganize this

        try {
            // Start the client.
            f = b.connect(host, port).sync();

            try {
                // Wait until the connection is closed.
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                group.shutdownGracefully();
            }
        } catch (InterruptedException e) {
            log.error("Failed to start client", e);
            Thread.currentThread().interrupt();
        }
    }

    public void close() {
        log.debug("Closing network client");
        f.channel().close();
    }

    @Override
    public void sendMessage(ClientMessage clientMessage) {
        try {
            String msg = objectMapper.writeValueAsString(clientMessage);
            log.info("Sending message {}", msg);
            f.sync().channel().writeAndFlush(msg);
        } catch (JsonProcessingException | InterruptedException e) {
            log.error("Failed to send message", e);
            Thread.currentThread().interrupt();
        }
    }
}
