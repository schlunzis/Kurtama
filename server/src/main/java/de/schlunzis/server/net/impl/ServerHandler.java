package de.schlunzis.server.net.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.schlunzis.common.messages.ClientMessage;
import de.schlunzis.common.messages.ServerMessage;
import de.schlunzis.server.net.ClientMessageDispatcher;
import de.schlunzis.server.net.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@ChannelHandler.Sharable
@Component
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private final ObjectMapper objectMapper;

    private final IChannelStore channelStore;

    private final ClientMessageDispatcher clientMessageDispatcher;


    public ServerHandler(IChannelStore channelStore, ClientMessageDispatcher clientMessageDispatcher) {
        this.channelStore = channelStore;
        this.clientMessageDispatcher = clientMessageDispatcher;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws JsonProcessingException {
        log.info("Client joined - " + ctx);
        channelStore.create(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        // TODO: Log out the user of the corresponding session (see Issue "Allow Logout of user #8")
        log.info("Client left - " + ctx);
        channelStore.remove(ctx.channel());
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        try {
            log.info("Message received: " + msg);
            ClientMessage myMessage = objectMapper.readValue(msg, ClientMessage.class);
            log.info("converted to {}", myMessage);
            channelStore.get(ctx.channel()).ifPresentOrElse(
                    session -> clientMessageDispatcher.dispatch(myMessage, session),
                    () -> log.error("No session found for channel " + ctx.channel()));


        } catch (Throwable t) {
            log.error("Error during message conversion occurred:", t);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("Closing connection for client - " + ctx);
        channelStore.remove(ctx.channel());
        ctx.close();
    }


    public void sendMessage(ServerMessage serverMessage) {
        sendMessageHelper(serverMessage, channelStore.getAll());
    }

    /**
     * Sends a message to all recipients. If recipients is empty, the message is sent to all clients.
     *
     * @param serverMessage
     * @param recipients
     */
    public void sendMessage(ServerMessage serverMessage, Collection<Session> recipients) {
        if (recipients.isEmpty())
            sendMessage(serverMessage);
        else
            sendMessageHelper(serverMessage, channelStore.get(recipients));
    }

    private void sendMessageHelper(ServerMessage serverMessage, Collection<Channel> channels) {
        channels.forEach(channel -> {
            try {
                String msg = new ObjectMapper().writeValueAsString(serverMessage);
                channel.writeAndFlush(msg);
            } catch (JsonProcessingException e) {
                log.error("Error during message conversion occurred:", e);
            }
        });
    }

}