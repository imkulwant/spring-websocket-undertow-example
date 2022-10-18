package com.kulsin.wesocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
@AllArgsConstructor
@EnableScheduling
public class WebSocketConnector {

    private StandardWebSocketClient standardWebSocketClient;

    @Scheduled(fixedDelayString = "PT30S")
    public void connect() {

        String webSocketUrl = "wss://some-url";

        WebSocketClientHandler webSocketHandler = new WebSocketClientHandler();

        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();

        URI uri = UriComponentsBuilder.fromUriString(webSocketUrl).encode().build().toUri();

        ListenableFuture<WebSocketSession> wsSessionListenableFuture = standardWebSocketClient.doHandshake(
                webSocketHandler,
                webSocketHttpHeaders,
                uri
        );

        wsSessionListenableFuture.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(WebSocketSession session) {
                log.info("Successfully websocket client handshake [session={}]", session);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Failed on websocket client handshake [error={}]", ex.getMessage(), ex);
            }

        });
    }
}
