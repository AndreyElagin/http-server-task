package com.study.server;

import com.study.server.exceptions.BadRequestException;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpRequestParser;
import com.study.server.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class SocketHandlerImpl implements SocketHandler, Runnable {
    private InputStream in;
    private OutputStream out;
    private RequestDispatcher requestDispatcher;

    public SocketHandlerImpl(Socket clientSocket, RequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
        try {
            this.in = clientSocket.getInputStream();
            this.out = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            HttpRequest request = HttpRequestParser.parse(in);
            HttpResponse response = requestDispatcher.dispatch(request);
            sendResponse(response);

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

    private void sendResponse(HttpResponse response) {
        String protocol = response.getProtocol();
        String statusCode = response.getStatusCode();
        Map<String, String> headers = response.getHeaders();
        String body = response.getBody();
        try {
            out.write((protocol + " " + statusCode + "\r\n").getBytes());

            for (String headerName : headers.keySet()) {
                out.write((headerName + ": " + headers.get(headerName) + "\r\n").getBytes());
            }

            out.write("\r\n".getBytes());

            if (body != null) {
                out.write(body.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
