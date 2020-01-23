package com.study.server;

import com.study.server.controller.ControllerImpl;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;

import java.util.Collections;
import java.util.Set;

public class RequestDispatcherImpl implements RequestDispatcher {
    Set<ControllerImpl> controllers = Collections.emptySet();

    public RequestDispatcherImpl(Set<ControllerImpl> controllers) {
        this.controllers = controllers;
    }

    @Override
    public HttpResponse dispatch(HttpRequest request) {
        HttpResponse response;

        return new HttpResponse();
    }
}