package com.study.server;

import com.study.server.controller.ControllerImpl;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class RequestDispatcherImpl implements RequestDispatcher {
    Set<ControllerImpl> controllers = Collections.emptySet();

    public RequestDispatcherImpl(Set<ControllerImpl> controllers) {
        this.controllers = controllers;
    }

    @Override
    public HttpResponse dispatch(HttpRequest request) {
        HttpResponse.ResponseBuilder builder = new HttpResponse.ResponseBuilder();
        builder.setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._404.toString())
                .setHeaders(Map.of())
                .setBody("body");
        return new HttpResponse(builder);
    }
}