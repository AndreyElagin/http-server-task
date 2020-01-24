package com.study.server.utils;

import com.study.server.RequestDispatcher;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;

public class RequestDispatcherTest implements RequestDispatcher {
    @Override
    public HttpResponse dispatch(HttpRequest request) {
        return null;
    }
}
