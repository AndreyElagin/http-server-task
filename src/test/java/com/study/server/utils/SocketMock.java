package com.study.server.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static com.study.server.utils.TestUtils.readFile;

public class SocketMock extends Socket {
    private final String requestFileName;

    public SocketMock(String requestFileName) {
        this.requestFileName = requestFileName;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return readFile(requestFileName);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        File responseFileName = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\output");
        responseFileName.delete();
        OutputStream os = new FileOutputStream(responseFileName) {

            @Override
            public void write(int b) throws IOException {
            }
        };

        return os;
    }
}
