package com.study.server;

import java.net.Socket;

public class SocketHandlerFactory {
    RequestDispatcher requestDispatcher;

    public SocketHandlerFactory(RequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }

    public SocketHandlerImpl createSocketHandler(Socket clientSocket) {
        return new SocketHandlerImpl(clientSocket, requestDispatcher);
    }

    ;
}
