/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.connector.amazons3.utils;

import org.apache.synapse.config.AbstractSynapseObserver;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.libraries.model.Library;
import org.wso2.carbon.connector.amazons3.constants.S3Constants;
import org.wso2.carbon.connector.core.connection.ConnectionHandler;

/**
 * Listen for library un deploy events
 * and cleanup connections created by the connector.
 */
public class ConnectorUndeployObserver extends AbstractSynapseObserver {

    private SynapseConfiguration synapseConfiguration;
    private String connectorName = S3Constants.CONNECTOR_NAME;

    public ConnectorUndeployObserver(SynapseConfiguration synapseConfiguration) {
        this.synapseConfiguration = synapseConfiguration;
    }

    @Override
    public void synapseLibraryRemoved(Library library) {
        String libraryPath = library.getFileName();
        // No need to use File.separator here since libraryPath we are getting from synapse is formatted with "/"
        String libraryName = libraryPath.substring(libraryPath.lastIndexOf("/"));
        if (libraryName.contains(S3Constants.CONNECTOR_LIBRARY_NAME)
            && library.getPackage().equals(S3Constants.CONNECTOR_LIBRARY_PACKAGE_TYPE)) {
            if (log.isDebugEnabled()) {
                log.debug(
                    "Amazons3 Connector is being un deployed. Closing all file connections created.");
            }
            ConnectionHandler.getConnectionHandler().
                shutdownConnections(S3Constants.CONNECTOR_NAME);
            synapseConfiguration.unregisterObserver(this);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnectorUndeployObserver that = (ConnectorUndeployObserver) o;
        return connectorName.equals(that.connectorName);
    }

    public int hashCode() {
        return connectorName.hashCode();
    }
}
