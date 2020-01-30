package com.study.server;

import com.study.server.exceptions.BadRequestException;
import com.study.server.http.HttpRequestParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketHandlerImpl implements SocketHandler, Runnable {
    private final InputStream in;
    private final OutputStream out;
    private final RequestDispatcher requestDispatcher;

    public SocketHandlerImpl(Socket clientSocket, RequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
        try {
            this.in = clientSocket.getInputStream();
            this.out = clientSocket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException("Can't read clientSocket");
        }
    }

    @Override
    public void run() {
        try {
            var request = HttpRequestParser.parse(in);
            var response = requestDispatcher.dispatch(request);
            var preparedResponse = response.getPreparedResponse();
            out.write(preparedResponse);

            out.close();
            in.close();
        } catch (BadRequestException e) {
            try {
                respond(500, "Unable to parse request", out);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void respond(int statusCode, String msg, OutputStream out) throws IOException {
        String responseLine = "HTTP/1.1 " + statusCode + " " + msg + "\r\n\r\n";
        out.write(responseLine.getBytes());
    }
}
