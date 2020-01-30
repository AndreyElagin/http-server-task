package com.study.server.utils;

import com.study.server.http.HttpResponse;

import java.io.*;
import java.net.Socket;

import static com.study.server.utils.TestUtils.readFile;

public class SocketMock extends Socket {
    private final String requestFileName;
    private ByteArrayOutputStream os;

    public SocketMock(String requestFileName) {
        this.requestFileName = requestFileName;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return readFile(requestFileName);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new ByteArrayOutputStream();
    }

    public boolean verifyResponse(HttpResponse expectedResponse) {
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpResponse response = HttpResponseParser.parse(is);

        return expectedResponse.equals(response);
    }

//    public Integer verifyRequest(HttpRequest request) {
//        return counts.get(request);
//    }
//
//    public void clearMock() {
//        counts.clear();
//    }
}
