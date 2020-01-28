package com.study.server;

import com.study.server.http.HttpRequest;
import com.study.server.http.HttpRequestParser;
import com.study.server.http.HttpResponse;
import com.study.server.utils.HttpResponseParser;
import com.study.server.utils.RequestDispatcherMock;
import com.study.server.utils.SocketMock;
import com.study.server.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SocketHandlerImplTest {

    @Test
    @DisplayName("The method calling RequestDispatcherMock should be com.study.server.SocketHandlerImpl.run")
    void test1() {
        Socket clientSocket = new SocketMock("GET");
        RequestDispatcherMock rd = new RequestDispatcherMock();
        SocketHandlerImpl sh = new SocketHandlerImpl(clientSocket, rd);
        String expectedCallingMethod = "com.study.server.SocketHandlerImpl.run";

        sh.run();

        assertEquals(expectedCallingMethod, rd.getCallingMethod());
    }

    @Test
    @DisplayName("Should pass expected HttpResponse to OutputStream")
    void test2() throws Exception {
        Socket clientSocket = new SocketMock("GET");
        RequestDispatcher rd = new RequestDispatcherMock();
        SocketHandlerImpl sh = new SocketHandlerImpl(clientSocket, rd);

        HttpRequest requestMock = HttpRequestParser.parse(TestUtils.readFile("GET"));
        HttpResponse expectedResponse = rd.dispatch(requestMock);

        sh.run();

        File file = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\output");
        var in = new FileInputStream(file);
        HttpResponse response = HttpResponseParser.parse(in);

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should pass HttpResponse with statusCode 500 to OutputStream if the HttpRequest is incorrect")
    void test3() throws Exception {
        Socket clientSocket = new SocketMock("bad1");
        RequestDispatcher rd = new RequestDispatcherMock();
        SocketHandlerImpl sh = new SocketHandlerImpl(clientSocket, rd);
        var expectedResponseLine = "HTTP/1.1 500 Unable to parse request\r\n\r\n";

        sh.run();

        File file = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\output");
        var in = new FileInputStream(file);
        var br = new BufferedReader(new InputStreamReader(in));
        var sb = new StringBuilder();
        var curLine = br.readLine();

        while (curLine != null) {
            sb.append(curLine).append("\r\n");
            curLine = br.readLine();
        }

        String responseLine = sb.toString();

        assertEquals(expectedResponseLine, responseLine);
    }
}