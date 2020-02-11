package com.study.server;

import com.study.server.utils.HttpPatternsUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationReaderImpl implements ConfigurationReader {
    private final String sourceDir;
    private static final Logger LOGGER = Logger.getLogger(ConfigurationReaderImpl.class.getName());

    public ConfigurationReaderImpl() {
        sourceDir = System.getenv().get("CONF_DIR");
        if (sourceDir == null) {
            LOGGER.log(Level.SEVERE, "Missing environment variable CONF_DIR");
            throw new IllegalArgumentException("Missing environment variable CONF_DIR");
        }
    }

    @Override
    public ServerConfiguration readConfig() {
        int port;
        int poolSize;

        Properties properties = new Properties();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(sourceDir, "server-config.properties"))) {
            properties.load(reader);
            port = Integer.parseInt(properties.getProperty("server.port"));
            poolSize = Integer.parseInt(properties.getProperty("server.pool-size"));

            LOGGER.log(
                    Level.INFO,
                    "Parameters obtained from configuration file:\r\n"
                            + "port = " + port + "\r\n"
                            + "poolSize = " + poolSize
            );
            return new ServerConfiguration(port, poolSize);
        } catch (IOException e) {
            LOGGER.log(
                    Level.INFO,
                    "Configuration file cannot be read, port and poolSize accepted by default"
            );
            return new ServerConfiguration();
        }
    }

    @Override
    public Map<String, String> readMappings() {
        Map<String, String> mappings = new HashMap<>();
        File[] dirs = new File(sourceDir).listFiles(File::isDirectory);

        if (dirs == null) {
            LOGGER.log(Level.SEVERE, "The configuration directory does not contain directories");
            throw new NoSuchElementException("The configuration directory does not contain directories");
        } else {
            for (File dir : dirs) {
                String host;
                var hostMatcher = HttpPatternsUtils.PATH_HOST_PATTERN.matcher(dir.getPath());
                if (hostMatcher.find()) {
                    host = hostMatcher.group("pathHost");
                    mappings.put(host, dir.getPath());
                }
            }
        }

        if (mappings.isEmpty()) {
            LOGGER.log(Level.SEVERE, "The configuration directory does not contain valid site directories");
            throw new NoSuchElementException("The configuration directory does not contain valid site directories");
        } else {
            LOGGER.log(Level.INFO, "found " + mappings.size() + " directories with sites");
        }

        return Map.copyOf(mappings);
    }
}
