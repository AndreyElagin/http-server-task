package com.study.server;

import com.study.server.controller.ControllerImpl;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ConfigurationReaderImpl cr = new ConfigurationReaderImpl();
        ServerConfiguration config = cr.readConfig();
        Map<String, String> mappings = cr.readMappings();

        Set<ControllerImpl> controllers = getControllers(mappings);
        RequestDispatcher requestDispatcher = new RequestDispatcherImpl(controllers);

        HttpServerImpl server = new HttpServerImpl(config);
        server.start();
    }

    private static Set<ControllerImpl> getControllers(Map<String, String> mappings) {
        Set<ControllerImpl> controllers = Collections.emptySet();

        for (Map.Entry<String, String> entry : mappings.entrySet()) {
            String host = entry.getKey();
            String path = entry.getValue();

            ControllerImpl controller = new ControllerImpl(host, path);
            controllers.add(controller);
        }

        return Set.copyOf(controllers);
    }
}
