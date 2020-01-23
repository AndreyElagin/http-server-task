package com.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerImpl implements HttpServer {
    private int port;
    private int poolSize;
    private boolean isStopped = false;
    private ServerSocket serverSocket = null;
    private ExecutorService executor;
    private SocketHandlerFactory socketHandlerFactory;

    public HttpServerImpl(ServerConfiguration config, SocketHandlerFactory socketHandlerFactory) {
        this.port = config.getPort();
        this.poolSize = config.getPoolSize();
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.socketHandlerFactory = socketHandlerFactory;
    }

    @Override
    public void start() {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.port, e);
        }

        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server stopped");
                    break;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            this.executor.execute(socketHandlerFactory.createSocketHandler(clientSocket));
        }
        this.executor.shutdown();
        System.out.println("Server stopped");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
}