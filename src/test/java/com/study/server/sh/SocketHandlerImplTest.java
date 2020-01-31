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
    @DisplayName("Should change the number of dispatch() method calls")
    void dispatch1() throws IOException {
        var socketMock = new SocketMock("GET");
        var rd = new RequestDispatcherMock();
        var sh = new SocketHandlerImpl(socketMock, rd);
        Integer expectedDispatchCalls1 = 1;
        Integer expectedDispatchCalls2 = 2;

        sh.run();

        var in = socketMock.getInputStream();
        var request = HttpRequestParser.parse(in);
        Integer dispatchCalls = rd.verifyRequest(request);

        assertEquals(expectedDispatchCalls1, dispatchCalls);

        rd.dispatch(request);
        dispatchCalls = rd.verifyRequest(request);

        assertEquals(expectedDispatchCalls2, dispatchCalls);
    }

    @Test
    @DisplayName("Should change the number of dispatch () method calls for different HttpRequest")
    void dispatch2() throws IOException {
        var socketMockGet = new SocketMock("GET");
        var rd = new RequestDispatcherMock();
        var shGet = new SocketHandlerImpl(socketMockGet, rd);

        var socketMockPut = new SocketMock("PUT");
        var shPut = new SocketHandlerImpl(socketMockPut, rd);

        Integer expectedDispatchCallsGet = 1;
        Integer expectedDispatchCallsPut = 1;

        shGet.run();
        shPut.run();

        var in1 = socketMockGet.getInputStream();
        var requestGet = HttpRequestParser.parse(in1);
        Integer dispatchCalls = rd.verifyRequest(requestGet);

        assertEquals(expectedDispatchCallsGet, dispatchCalls);

        var in2 = socketMockGet.getInputStream();
        var requestPut = HttpRequestParser.parse(in2);
        dispatchCalls = rd.verifyRequest(requestPut);

        assertEquals(expectedDispatchCallsPut, dispatchCalls);
    }

    @Test
    @DisplayName("Should clear mock")
    void dispatch3() throws IOException {
        var socketMock = new SocketMock("GET");
        var rd = new RequestDispatcherMock();
        var sh = new SocketHandlerImpl(socketMock, rd);
        Integer expectedDispatchCalls1 = 1;
        Integer expectedDispatchCalls2 = null;

        sh.run();

        var in = socketMock.getInputStream();
        var request = HttpRequestParser.parse(in);
        Integer dispatchCalls = rd.verifyRequest(request);

        assertEquals(expectedDispatchCalls1, dispatchCalls);

        rd.clearMock();
        dispatchCalls = rd.verifyRequest(request);

        assertEquals(expectedDispatchCalls2, dispatchCalls);
    }

    @Test
    @DisplayName("Should pass expected HttpResponse to OutputStream")
    void output1() {
        var socketMock = new SocketMock("GET");
        var rd = new RequestDispatcherMock();
        var sh = new SocketHandlerImpl(socketMock, rd);

        var request = HttpRequestParser.parse(readFile("GET"));
        var response = rd.dispatch(request);

        sh.run();

        assertTrue(socketMock.verifyResponse(response));
    }

    @Test
    @DisplayName("Should verify request")
    void output2() throws IOException {
        var socketMock = new SocketMock("GET");
        var rd = new RequestDispatcherMock();
        var sh = new SocketHandlerImpl(socketMock, rd);
        Integer expectedGISCalls = 1;

        sh.run();

        Integer GISCalls = socketMock.verifyRequest(readFile("GET"));
        assertEquals(expectedGISCalls, GISCalls);
    }

    @Test
    @DisplayName("Should clear mock")
    void output3() throws IOException {
        var socketMock = new SocketMock("GET");
        var rd = new RequestDispatcherMock();
        var sh = new SocketHandlerImpl(socketMock, rd);
        Integer expectedGISCalls1 = 1;
        Integer expectedGISCalls2 = null;

        sh.run();

        Integer GISCalls1 = socketMock.verifyRequest(readFile("GET"));
        assertEquals(expectedGISCalls1, GISCalls1);

        socketMock.clearMock();

        Integer GISCalls2 = socketMock.verifyRequest(readFile("GET"));
        assertEquals(expectedGISCalls2, GISCalls2);
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