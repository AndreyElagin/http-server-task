package com.study.server;

public class ServerConfiguration {
    private final int port;
    private final int poolSize;

    public ServerConfiguration() {
        port = 80;
        poolSize = 20;
    }

    public ServerConfiguration(int port, int poolSize) {
        this.port = port;
        this.poolSize = poolSize;
    }

    public int getPort() {
        return port;
    }

    public int getPoolSize() {
        return poolSize;
    }
}