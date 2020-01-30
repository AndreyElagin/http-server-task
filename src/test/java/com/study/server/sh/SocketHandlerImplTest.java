package com.study.server.sh;

import com.study.server.SocketHandlerImpl;
import com.study.server.http.HttpRequestParser;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;
import com.study.server.utils.RequestDispatcherMock;
import com.study.server.utils.SocketMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.study.server.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SocketHandlerImplTest {

    @Test
    @DisplayName("Should change the number of dispatch() method calls for each HttpRequest")
    void test1() throws IOException {
        var socketMock = new SocketMock("GET");
        var rd = new RequestDispatcherMock();
        var sh = new SocketHandlerImpl(socketMock, rd);
        Integer expectedNumberDispatchMethodCalls = 1;

        sh.run();

        var in = socketMock.getInputStream();
        var request = HttpRequestParser.parse(in);
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
    void test2() {
        var socketMock = new SocketMock("GET");
        var rd = new RequestDispatcherMock();
        var sh = new SocketHandlerImpl(socketMock, rd);

        var request = HttpRequestParser.parse(readFile("GET"));
        var response = rd.dispatch(request);

        sh.run();

        assertTrue(socketMock.verifyResponse(response));
    }

    @Test
    @DisplayName("Should pass HttpResponse with statusCode 500 to OutputStream if the HttpRequest is incorrect")
    void test3() {
        var socketMock = new SocketMock("bad1");
        var rd = new RequestDispatcherMock();
        var sh = new SocketHandlerImpl(socketMock, rd);
        var builder = new HttpResponse.ResponseBuilder();
        var expectedResponse = builder.setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._400.toString())
                .build();

        sh.run();

        assertTrue(socketMock.verifyResponse(expectedResponse));
    }
}