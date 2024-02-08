package org.schlunzis.kurtama.server.net.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.net.ClientMessageDispatcher;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.net.SessionType;
import org.schlunzis.kurtama.server.net.util.ChannelStore;
import org.schlunzis.kurtama.server.net.util.MessageConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private final ChannelStore<Channel> channelStore = new ChannelStore<>(SessionType.NETTY);

    private final MessageConverter messageConverter;
    private final ClientMessageDispatcher clientMessageDispatcher;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        log.info("Client joined - " + ctx);
        ISession session = channelStore.create(ctx.channel());
        clientMessageDispatcher.newClient(session);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        channelStore.get(ctx.channel()).ifPresentOrElse(
                clientMessageDispatcher::clientDisconnected,
                () -> log.error("No session found for channel " + ctx.channel()));
        log.info("Client left - " + ctx);
        channelStore.remove(ctx.channel());
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        try {
            log.info("Message received: " + msg);
            IClientMessage myMessage = messageConverter.toClientMessage(msg);
            log.info("converted to {}", myMessage);
            channelStore.get(ctx.channel()).ifPresentOrElse(
                    session -> clientMessageDispatcher.dispatch(myMessage, session),
                    () -> log.error("No session found for channel " + ctx.channel())
            );
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


    public void sendMessage(IServerMessage serverMessage) {
        sendMessageHelper(serverMessage, channelStore.getAll());
    }

    /**
     * Sends a message to all recipients. If recipients is empty, the message is sent to all clients.
     *
     * @param serverMessage
     * @param recipients
     */
    public void sendMessage(IServerMessage serverMessage, Collection<ISession> recipients) {
        if (recipients.isEmpty())
            sendMessage(serverMessage);
        else
            sendMessageHelper(serverMessage, channelStore.get(recipients));
    }

    private void sendMessageHelper(IServerMessage serverMessage, Collection<Channel> channels) {
        channels.forEach(channel -> {
            try {
                String msg = messageConverter.toJson(serverMessage);
                channel.writeAndFlush(msg);
            } catch (JsonProcessingException e) {
                log.error("Error during message conversion occurred:", e);
            }
        });
    }

}