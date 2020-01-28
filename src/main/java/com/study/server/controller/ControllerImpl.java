package com.study.server.controller;

import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

import java.util.Map;

public class ControllerImpl implements Controller {
    String host;
    String path;

    public ControllerImpl(String host, String path) {
        this.host = host;
        this.path = path;
    }

    @Override
    public boolean match(HttpRequest request) {
        String requestHost = request.getHost();

        return host.equals(requestHost);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        HttpResponse.ResponseBuilder builder = new HttpResponse.ResponseBuilder();
        builder.setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._404.toString())
                .setHeaders(Map.of())
                .setBody("body");
        HttpResponse response = new HttpResponse(builder);
//        StringBuffer buf = new StringBuffer();
//
//        try {
//            FileInputStream file = new FileInputStream(path);
//
//            int c;
//            while ((c = file.read()) != -1) {
//                buf.append((char) c);
//            }
//
//            response.setResponseCode(StatusCode._200);
//            response.addHeader("Content-Type", "text/html");
//            response.addBody(buf.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return response;
    }
}