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
    private final String outputFileName;

    public SocketMock(String requestFileName, String outputFileName) {
        this.requestFileName = requestFileName;
        this.outputFileName = outputFileName;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return readFile(requestFileName);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        File responseFileName = new File(System.getProperty("user.dir") +
                "/src/test/resources/" + outputFileName);
        responseFileName.delete();
        OutputStream os = new FileOutputStream(responseFileName) {

            @Override
            public void write(int b) throws IOException {
            }
        };

        return os;
    }
}
