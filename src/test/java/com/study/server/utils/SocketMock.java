package com.study.server.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static com.study.server.utils.TestUtils.readFile;

public class SocketMock extends Socket {
    private final String fileName;

    public SocketMock(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return readFile(fileName);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        File file = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\output");
        file.delete();
        OutputStream os = new FileOutputStream(file) {

            @Override
            public void write(int b) throws IOException {
            }
        };

        return os;
    }
}
