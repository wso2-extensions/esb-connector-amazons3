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
import java.util.List;

@XmlRootElement(name = "S3Location")
public class S3Location {
    private String bucketName;
    private String prefix;
    private Encryption encryption;
    private String cannedACL;
    private List<Grant> accessControlList;
    private TagConfiguration tagging;
    private List<MetadataEntry> userMetadata;
    private String storageClass;

    @XmlElement(name = "BucketName")
    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @XmlElement(name = "Prefix")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @XmlElement(name = "Encryption")
    public Encryption getEncryption() {
        return encryption;
    }

    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }

    @XmlElement(name = "CannedACL")
    public String getCannedACL() {
        return cannedACL;
    }

    public void setCannedACL(String cannedACL) {
        this.cannedACL = cannedACL;
    }

    @XmlElement(name = "AccessControlList")
    public List<Grant> getAccessControlList() {
        return accessControlList;
    }

    public void setAccessControlList(List<Grant> accessControlList) {
        this.accessControlList = accessControlList;
    }

    @XmlElement(name = "Tagging")
    public TagConfiguration getTagging() {
        return tagging;
    }

    public void setTagging(TagConfiguration tagging) {
        this.tagging = tagging;
    }

    @XmlElement(name = "MetadataEntry")
    public List<MetadataEntry> getUserMetadata() {
        return userMetadata;
    }

    public void setUserMetadata(List<MetadataEntry> userMetadata) {
        this.userMetadata = userMetadata;
    }

    @XmlElement(name = "StorageClass")
    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }
}