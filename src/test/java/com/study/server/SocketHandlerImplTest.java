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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SocketHandlerImplTest {

    @Test
    @DisplayName("The method calling RequestDispatcherMock should be com.study.server.SocketHandlerImpl.run")
    void test1() {
        SocketMock socketMock = new SocketMock();
        Socket clientSocket = socketMock.getClientSocket();
        RequestDispatcherMock rd = new RequestDispatcherMock();
        SocketHandlerImpl sh = new SocketHandlerImpl(clientSocket, rd);
        String expectedCallingMethod = "com.study.server.SocketHandlerImpl.run";

        sh.run();

        assertEquals(expectedCallingMethod, rd.getCallingMethod());
    }

    @Test
    @DisplayName("Passed SocketHandlerImpl to OutputStream HttpResponse should be equal to expected")
    void test2() throws IOException {
        SocketMock socketMock = new SocketMock();
        Socket clientSocket = socketMock.getClientSocket();
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
}