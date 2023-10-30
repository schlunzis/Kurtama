package de.schlunzis.client.net.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.schlunzis.client.net.ServerMessageDispatcher;
import de.schlunzis.common.messages.IServerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
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