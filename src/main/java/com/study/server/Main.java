package com.study.server;

import com.study.server.controller.ControllerImpl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ConfigurationReaderImpl cr = new ConfigurationReaderImpl();
        ServerConfiguration config = cr.readConfig();
        Map<String, String> mappings = cr.readMappings();
        Set<ControllerImpl> controllers = getControllers(mappings);

        RequestDispatcher requestDispatcher = new RequestDispatcherImpl(controllers);
        SocketHandlerFactoryImpl socketHandlerFactory = new SocketHandlerFactoryImpl(requestDispatcher);


        HttpServerImpl server = new HttpServerImpl(config, socketHandlerFactory);
        server.start();
    }

    private static Set<ControllerImpl> getControllers(Map<String, String> mappings) {
        Set<ControllerImpl> controllers = new HashSet<>();

        mappings.forEach((k, v) -> controllers.add(new ControllerImpl(k, v)));

        return Set.copyOf(controllers);
    }
}
