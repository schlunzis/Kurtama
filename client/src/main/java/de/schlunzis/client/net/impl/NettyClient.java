package de.schlunzis.client.net.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.schlunzis.client.net.NetworkClient;
import de.schlunzis.common.messages.ClientMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
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
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
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
        f = b.connect(host, port);
        f.awaitUninterruptibly();

        if (f.isCancelled()) {
            log.info("Connection cancelled by user.");
            close();
        } else if (!f.isSuccess()) {
            log.error("Connection failed!");
            f.cause().printStackTrace();
            close();
        } else {
            log.info("Connected to server.");
        }
    }

    public void close() {
        log.debug("Closing network client");
        f.channel().close();
        group.shutdownGracefully();
    }

    @Override
    public void sendMessage(ClientMessage clientMessage) {
        try {
            String msg = objectMapper.writeValueAsString(clientMessage);
            log.info("Sending message {}", msg);
            f.sync().channel().writeAndFlush(msg);
        } catch (JsonProcessingException e) {
            log.error("Failed to send message. Could not create JSON from object.", e);
        } catch (InterruptedException e) {
            log.error("Failed to send message", e);
            Thread.currentThread().interrupt();
        }
    }
}
