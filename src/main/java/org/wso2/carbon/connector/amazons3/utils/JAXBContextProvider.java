package org.wso2.carbon.connector.amazons3.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class JAXBContextProvider {
    private static final JAXBContextProvider INSTANCE = new JAXBContextProvider();
    private final ConcurrentHashMap<String, JAXBContext> contextMap = new ConcurrentHashMap<>();

    private JAXBContextProvider() {
        // Private constructor for singleton
    }

    public static JAXBContextProvider getInstance() {
        return INSTANCE;
    }

    public JAXBContext getJAXBContext(Class<?>... types) throws JAXBException {
        // Create a unique key based on class names
        String key = Arrays.stream(types)
                .map(Class::getName)
                .sorted()
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        return contextMap.computeIfAbsent(key, k -> {
            try {
                return JAXBContext.newInstance(types);
            } catch (JAXBException e) {
                throw new RuntimeException("Failed to create JAXBContext for: " + key, e);
            }
        });
    }
}
