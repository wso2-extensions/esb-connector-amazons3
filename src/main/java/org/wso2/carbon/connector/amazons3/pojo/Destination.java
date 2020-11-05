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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Destination")
public class Destination {
    private String bucket;
    private String account;
    private String storageClass;
    private AccessControlTranslation accessControlTranslation;
    private EncryptionConfiguration encryptionConfiguration;
    private ReplicationTime replicationTime;
    private Metrics metrics;

    @XmlElement(name = "Bucket")
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @XmlElement(name = "Account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @XmlElement(name = "StorageClass")
    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    @XmlElement(name = "AccessControlTranslation")
    public AccessControlTranslation getAccessControlTranslation() {
        return accessControlTranslation;
    }

    public void setAccessControlTranslation(AccessControlTranslation accessControlTranslation) {
        this.accessControlTranslation = accessControlTranslation;
    }

    @XmlElement(name = "EncryptionConfiguration")
    public EncryptionConfiguration getEncryptionConfiguration() {
        return encryptionConfiguration;
    }

    public void setEncryptionConfiguration(EncryptionConfiguration encryptionConfiguration) {
        this.encryptionConfiguration = encryptionConfiguration;
    }

    @XmlElement(name = "ReplicationTime")
    public ReplicationTime getReplicationTime() {
        return replicationTime;
    }

    public void setReplicationTime(ReplicationTime replicationTime) {
        this.replicationTime = replicationTime;
    }

    @XmlElement(name = "Metrics")
    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

}