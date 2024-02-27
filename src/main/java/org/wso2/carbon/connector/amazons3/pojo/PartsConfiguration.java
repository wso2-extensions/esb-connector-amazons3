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
import java.time.Instant;
import java.util.List;

@XmlRootElement(name = "PartsConfiguration")
public class PartsConfiguration {
    private List<Part> part;
    private Instant abortDate;
    private String abortRuleId;
    private String bucket;
    private String key;
    private String uploadId;
    private Integer partNumberMarker;
    private Integer nextPartNumberMarker;
    private Integer maxParts;
    private boolean isTruncated;
    private Initiator initiator;
    private Owner owner;
    private String storageClass;
    private String requestCharged;

    @XmlElement(name = "Part")
    public List<Part> getParts() {
        return part;
    }

    public void setParts(List<Part> part) {
        this.part = part;
    }


    @XmlElement(name = "AbortDate")
    public String getAbortDate() {
        return (abortDate != null) ? abortDate.toString() : "";
    }

    public void setAbortDate(Instant abortDate) {
        this.abortDate = abortDate;
    }

    @XmlElement(name = "AbortRuleId")
    public String getAbortRuleId() {
        return abortRuleId;
    }

    public void setAbortRuleId(String abortRuleId) {
        this.abortRuleId = abortRuleId;
    }

    @XmlElement(name = "Bucket")
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @XmlElement(name = "Key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @XmlElement(name = "UploadId")
    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    @XmlElement(name = "PartNumberMarker")
    public Integer getPartNumberMarker() {
        return partNumberMarker;
    }

    public void setPartNumberMarker(Integer partNumberMarker) {
        this.partNumberMarker = partNumberMarker;
    }

    @XmlElement(name = "NextPartNumberMarker")
    public Integer getNextPartNumberMarker() {
        return nextPartNumberMarker;
    }

    public void setNextPartNumberMarker(Integer nextPartNumberMarker) {
        this.nextPartNumberMarker = nextPartNumberMarker;
    }

    @XmlElement(name = "MaxParts")
    public Integer getMaxParts() {
        return maxParts;
    }

    public void setMaxParts(Integer maxParts) {
        this.maxParts = maxParts;
    }

    @XmlElement(name = "Truncated")
    public boolean getTruncated() {
        return isTruncated;
    }

    public void setTruncated(Boolean truncated) {
        isTruncated = truncated;
    }

    @XmlElement(name = "Initiator")
    public Initiator getInitiator() {
        return initiator;
    }

    public void setInitiator(Initiator initiator) {
        this.initiator = initiator;
    }

    @XmlElement(name = "Owner")
    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @XmlElement(name = "StorageClass")
    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    @XmlElement(name = "RequestCharged")
    public String getRequestCharged() {
        return requestCharged;
    }

    public void setRequestCharged(String requestCharged) {
        this.requestCharged = requestCharged;
    }
}