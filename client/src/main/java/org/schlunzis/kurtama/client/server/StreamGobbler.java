package org.schlunzis.kurtama.client.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@RequiredArgsConstructor
public class StreamGobbler implements Runnable {

    private final InputStream is;
    @Getter
    private String[] output;

    @Override
    public void run() {
        output = new BufferedReader(new InputStreamReader(is)).lines()
                .toArray(String[]::new);
    }

}
