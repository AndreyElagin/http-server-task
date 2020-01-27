package com.study.server.utils;

import com.study.server.RequestDispatcher;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

import java.util.Map;

public class RequestDispatcherMock implements RequestDispatcher {
    public RequestDispatcherMock() {
    }

    @Override
    public HttpResponse dispatch(HttpRequest request) {
        final Map<String, String> headers = Map.of(
                "Server", "Apache",
                "Content-Language", "ru",
                "Content-Type", "text/html; charset=utf-8"
        );
        final String body = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/bar.css\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/foo.css\">\n" +
                "    <title>Food</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Food</p>\n" +
                "<script src=\"js/script.js\"></script>\n" +
                "</body>\n" +
                "</html>";
        HttpResponse.ResponseBuilder builder = new HttpResponse.ResponseBuilder();
        builder.setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._200)
                .setHeaders(headers)
                .setBody(body);
        return new HttpResponse(builder);
    }
}
