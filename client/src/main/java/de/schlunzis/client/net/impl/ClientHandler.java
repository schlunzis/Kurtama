package de.schlunzis.client.net.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.schlunzis.client.net.ServerMessageDispatcher;
import de.schlunzis.common.messages.ServerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class ClientHandler extends SimpleChannelInboundHandler<String> {

    private final ServerMessageDispatcher dispatcher;
    private final ObjectMapper objectMapper;

    // TODO: Can the eventbus be injected here? The handler should be hidden on the exchangeable network layer. This layer may not write to the eventbus directly.
    public ClientHandler(ServerMessageDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("Message from Server: {}", msg);
        ServerMessage myMessage = objectMapper.readValue(msg, ServerMessage.class);
        dispatcher.dispatch(myMessage);
    }

}