/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.connector.amazons3.pojo;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.connector.amazons3.exception.InvalidConfigurationException;

/**
 * Configuration parameters used to
 * establish a connection to the file server
 */
public class ConnectionConfiguration {

    private String connectionName;
    private String region;
    private String awsAccessKeyId;
    private String awsSecretAccessKey;

    private String host;

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) throws InvalidConfigurationException {
        if (StringUtils.isNotEmpty(connectionName)) {
            this.connectionName = connectionName;
        } else {
            throw new InvalidConfigurationException("Mandatory parameter 'connectionName' is not set.");
        }
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) throws InvalidConfigurationException {
        if (StringUtils.isNotEmpty(region)) {
            this.region = region;
        } else {
            throw new InvalidConfigurationException("Mandatory parameter 'region' is not set.");
        }
    }

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.awsAccessKeyId = awsAccessKeyId;
    }

    public String getAwsSecretAccessKey() {
        return awsSecretAccessKey;
    }

    public void setAwsSecretAccessKey(String awsSecretAccessKey) {
        this.awsSecretAccessKey = awsSecretAccessKey;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
