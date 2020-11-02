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

@XmlRootElement(name = "PutObjectResponse")
public class PutObjectResponse {
    private String expiration;
    private String eTag;
    private String versionId;
    private String serverSideEncryption;
    private String sseCustomerAlgorithm;
    private String sseCustomerKeyMD5;
    private String ssekmsKeyId;
    private String requestCharged;
    private String ssekmsEncryptionContext;

    @XmlElement(name = "Expiration")
    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    @XmlElement(name = "ETag")
    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    @XmlElement(name = "VersionId")
    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @XmlElement(name = "ServerSideEncryption")
    public String getServerSideEncryption() {
        return serverSideEncryption;
    }

    public void setServerSideEncryption(String serverSideEncryption) {
        this.serverSideEncryption = serverSideEncryption;
    }

    @XmlElement(name = "SseCustomerAlgorithm")
    public String getSseCustomerAlgorithm() {
        return sseCustomerAlgorithm;
    }

    public void setSseCustomerAlgorithm(String sseCustomerAlgorithm) {
        this.sseCustomerAlgorithm = sseCustomerAlgorithm;
    }

    @XmlElement(name = "SseCustomerKeyMD5")
    public String getSseCustomerKeyMD5() {
        return sseCustomerKeyMD5;
    }

    public void setSseCustomerKeyMD5(String sseCustomerKeyMD5) {
        this.sseCustomerKeyMD5 = sseCustomerKeyMD5;
    }

    @XmlElement(name = "SsekmsKeyId")
    public String getSsekmsKeyId() {
        return ssekmsKeyId;
    }

    public void setSsekmsKeyId(String ssekmsKeyId) {
        this.ssekmsKeyId = ssekmsKeyId;
    }

    @XmlElement(name = "RequestCharged")
    public String getRequestCharged() {
        return requestCharged;
    }

    public void setRequestCharged(String requestCharged) {
        this.requestCharged = requestCharged;
    }

    @XmlElement(name = "SsekmsEncryptionContext")
    public String getSsekmsEncryptionContext() {
        return ssekmsEncryptionContext;
    }

    public void setSsekmsEncryptionContext(String ssekmsEncryptionContext) {
        this.ssekmsEncryptionContext = ssekmsEncryptionContext;
    }
}