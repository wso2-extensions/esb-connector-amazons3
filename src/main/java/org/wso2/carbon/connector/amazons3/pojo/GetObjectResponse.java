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
import java.util.Map;

@XmlRootElement(name = "GetObjectResponse")
public class GetObjectResponse {
    private boolean deleteMarker;
    private String acceptRanges;
    private String expiration;
    private String restore;
    private Instant lastModified;
    private long contentLength;
    private String eTag;
    private int missingMeta;
    private String versionId;
    private String cacheControl;
    private String contentDisposition;
    private String contentEncoding;
    private String contentLanguage;
    private String contentRange;
    private String contentType;
    private Instant expires;
    private String websiteRedirectLocation;
    private String serverSideEncryption;
    private Map<String, String> metadata;
    private String sseCustomerAlgorithm;
    private String sseCustomerKeyMD5;
    private String ssekmsKeyId;
    private String storageClass;
    private String requestCharged;
    private String replicationStatus;
    private int partsCount;
    private int tagCount;
    private String objectLockMode;
    private Instant objectLockRetainUntilDate;
    private String objectLockLegalHoldStatus;
    private String content;

    @XmlElement(name = "DeleteMarker")
    public boolean getDeleteMarker() {
        return deleteMarker;
    }

    public void setDeleteMarker(boolean deleteMarker) {
        this.deleteMarker = deleteMarker;
    }

    @XmlElement(name = "AcceptRanges")
    public String getAcceptRanges() {
        return acceptRanges;
    }

    public void setAcceptRanges(String acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

    @XmlElement(name = "Expiration")
    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    @XmlElement(name = "Restore")
    public String getRestore() {
        return restore;
    }

    public void setRestore(String restore) {
        this.restore = restore;
    }

    @XmlElement(name = "LastModified")
    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    @XmlElement(name = "ContentLength")
    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    @XmlElement(name = "ETag")
    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    @XmlElement(name = "MissingMeta")
    public int getMissingMeta() {
        return missingMeta;
    }

    public void setMissingMeta(int missingMeta) {
        this.missingMeta = missingMeta;
    }

    @XmlElement(name = "VersionId")
    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @XmlElement(name = "CacheControl")
    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    @XmlElement(name = "ContentDisposition")
    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    @XmlElement(name = "ContentEncoding")
    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    @XmlElement(name = "ContentLanguage")
    public String getContentLanguage() {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    @XmlElement(name = "ContentRange")
    public String getContentRange() {
        return contentRange;
    }

    public void setContentRange(String contentRange) {
        this.contentRange = contentRange;
    }

    @XmlElement(name = "ContentType")
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @XmlElement(name = "Expires")
    public Instant getExpires() {
        return expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    @XmlElement(name = "WebsiteRedirectLocation")
    public String getWebsiteRedirectLocation() {
        return websiteRedirectLocation;
    }

    public void setWebsiteRedirectLocation(String websiteRedirectLocation) {
        this.websiteRedirectLocation = websiteRedirectLocation;
    }

    @XmlElement(name = "ServerSideEncryption")
    public String getServerSideEncryption() {
        return serverSideEncryption;
    }

    public void setServerSideEncryption(String serverSideEncryption) {
        this.serverSideEncryption = serverSideEncryption;
    }

    @XmlElement(name = "Metadata")
    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
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

    @XmlElement(name = "ReplicationStatus")
    public String getReplicationStatus() {
        return replicationStatus;
    }

    public void setReplicationStatus(String replicationStatus) {
        this.replicationStatus = replicationStatus;
    }

    @XmlElement(name = "PartsCount")
    public int getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(int partsCount) {
        this.partsCount = partsCount;
    }

    @XmlElement(name = "TagCount")
    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    @XmlElement(name = "ObjectLockMode")
    public String getObjectLockMode() {
        return objectLockMode;
    }

    public void setObjectLockMode(String objectLockMode) {
        this.objectLockMode = objectLockMode;
    }

    @XmlElement(name = "ObjectLockRetainUntilDate")
    public Instant getObjectLockRetainUntilDate() {
        return objectLockRetainUntilDate;
    }

    public void setObjectLockRetainUntilDate(Instant objectLockRetainUntilDate) {
        this.objectLockRetainUntilDate = objectLockRetainUntilDate;
    }

    @XmlElement(name = "ObjectLockLegalHoldStatus")
    public String getObjectLockLegalHoldStatus() {
        return objectLockLegalHoldStatus;
    }

    public void setObjectLockLegalHoldStatus(String objectLockLegalHoldStatus) {
        this.objectLockLegalHoldStatus = objectLockLegalHoldStatus;
    }

    @XmlElement(name = "Content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
