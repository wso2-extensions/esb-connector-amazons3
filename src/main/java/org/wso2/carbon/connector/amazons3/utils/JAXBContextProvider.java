/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
