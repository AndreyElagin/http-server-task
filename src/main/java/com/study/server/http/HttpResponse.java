package com.study.server.http;

import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private final String protocol;
    private final StatusCode statusCode;
    private final Map<String, String> headers;
    private final String body;

    public HttpResponse(ResponseBuilder builder) {
        protocol = builder.protocol;
        statusCode = builder.statusCode;
        headers = builder.headers;
        body = builder.body;
    }

    public String getProtocol() {
        return protocol;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return Map.copyOf(headers);
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpResponse that = (HttpResponse) o;
        return Objects.equals(protocol, that.protocol) &&
                statusCode == that.statusCode &&
                Objects.equals(headers, that.headers) &&
                Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, statusCode, headers, body);
    }


    public static class ResponseBuilder {
        private String protocol;
        private StatusCode statusCode;
        private Map<String, String> headers;
        private String body;

        public ResponseBuilder() {
        }

        public ResponseBuilder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public ResponseBuilder setStatusCode(StatusCode statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ResponseBuilder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public ResponseBuilder setBody(String body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }
}