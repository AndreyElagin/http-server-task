package com.study.server;

import com.study.server.utils.RequestDispatcherMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static com.study.server.utils.TestUtils.readFile;

class SocketHandlerImplTest {

    @Test
    @DisplayName("xxx")
    void test() throws IOException {
//        тебе надо как то проверить:
//        а) то что вызов был передан в реквест диспатчер
//        б) то что респонс был записан в аутпут стрим

        Socket clientSocket = new Socket() {
            @Override
            public InputStream getInputStream() throws IOException {
                return readFile("GET");
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                File file = new File("/src/test/resources/output");
                OutputStream os = new FileOutputStream(file) {

                    @Override
                    public void write(int b) throws IOException {
                    }
                };

                return os;
            }
        };

        RequestDispatcherMock rd = new RequestDispatcherMock();
        SocketHandlerImpl sh = new SocketHandlerImpl(clientSocket, rd);

        sh.run();

//        File file = new File("output");
//        var in = new FileInputStream(file);
//        var br = new BufferedReader(new InputStreamReader(in));
//        var curLine = br.readLine();
//        while (!curLine.equals("")) {
//            System.out.println(curLine);
//        }
//        System.out.println(file);
    }
}