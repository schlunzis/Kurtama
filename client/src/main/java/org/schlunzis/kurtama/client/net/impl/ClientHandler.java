package org.schlunzis.kurtama.client.net.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.net.ServerMessageDispatcher;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private final ServerMessageDispatcher dispatcher;
    private final ObjectMapper objectMapper;

    public ClientHandler(ServerMessageDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("Message from Server: {}", msg);
        IServerMessage myMessage = objectMapper.readValue(msg, IServerMessage.class);
        dispatcher.dispatch(myMessage);
    }

}