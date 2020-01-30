package com.study.server.sh;

import com.study.server.RequestDispatcher;
import com.study.server.SocketHandlerImpl;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpRequestParser;
import com.study.server.http.HttpResponse;
import com.study.server.utils.HttpResponseParser;
import com.study.server.utils.RequestDispatcherMock;
import com.study.server.utils.SocketMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static com.study.server.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SocketHandlerImplTest {

    @Test
    @DisplayName("Should change the number of dispatch() method calls for each HttpRequest")
    void test1() throws IOException {
        Socket clientSocket = new SocketMock("GET", "output-sh-test-1");
        RequestDispatcherMock rd = new RequestDispatcherMock();
        SocketHandlerImpl sh = new SocketHandlerImpl(clientSocket, rd);
        Integer expectedNumberDispatchMethodCalls = 1;

        sh.run();

        InputStream in = clientSocket.getInputStream();
        HttpRequest request = HttpRequestParser.parse(in);
        Integer numberDispatchMethodCalls = rd.verifyRequest(request);

        assertEquals(expectedNumberDispatchMethodCalls, numberDispatchMethodCalls);

        expectedNumberDispatchMethodCalls = 2;
        rd.dispatch(request);
        numberDispatchMethodCalls = rd.verifyRequest(request);

        assertEquals(expectedNumberDispatchMethodCalls, numberDispatchMethodCalls);

        rd.clearMock();
        expectedNumberDispatchMethodCalls = 1;
        rd.dispatch(request);
        numberDispatchMethodCalls = rd.verifyRequest(request);

        assertEquals(expectedNumberDispatchMethodCalls, numberDispatchMethodCalls);
    }

    @Test
    @DisplayName("Should pass expected HttpResponse to OutputStream")
    void test2() throws Exception {
        Socket clientSocket = new SocketMock("GET", "output-sh-test-2");
        RequestDispatcher rd = new RequestDispatcherMock();
        SocketHandlerImpl sh = new SocketHandlerImpl(clientSocket, rd);

        HttpRequest requestMock = HttpRequestParser.parse(readFile("GET"));
        HttpResponse expectedResponse = rd.dispatch(requestMock);

        sh.run();

        var in = readFile("output-sh-test-2");
        HttpResponse response = HttpResponseParser.parse(in);

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should pass HttpResponse with statusCode 500 to OutputStream if the HttpRequest is incorrect")
    void test3() throws Exception {
        Socket clientSocket = new SocketMock("bad1", "output-sh-test-3");
        RequestDispatcher rd = new RequestDispatcherMock();
        SocketHandlerImpl sh = new SocketHandlerImpl(clientSocket, rd);
        var expectedResponseLine = "HTTP/1.1 500 Unable to parse request\r\n\r\n";

        sh.run();

        String responseLine = getResponseLineFromFile();

        assertEquals(expectedResponseLine, responseLine);
    }

    private String getResponseLineFromFile() throws IOException {
        var in = readFile("output-sh-test-3");
        var br = new BufferedReader(new InputStreamReader(in));
        var sb = new StringBuilder();
        var curLine = br.readLine();

        while (curLine != null) {
            sb.append(curLine).append("\r\n");
            curLine = br.readLine();
        }

        return sb.toString();
    }
}