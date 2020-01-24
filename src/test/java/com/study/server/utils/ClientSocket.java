package com.study.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static com.study.server.utils.TestUtils.readFile;

public class ClientSocket extends Socket {
    public InputStream getInputStream() {
        return readFile("GET");
    }

    public OutputStream getOutputStream() {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                System.out.println("Out write");
            }
        };
    }
}
