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

@XmlRootElement(name = "MultipartUploads")
public class MultipartUploads {
    private String keyMarker;
    private String uploadIdMarker;
    private String nextKeyMarker;
    private String nextUploadIdMarker;
    private String bucket;
    private int maxUploads;
    private boolean isTruncated;
    private List<MultipartUpload> uploads;
    private String prefix;
    private String delimiter;
    private List<CommonPrefix> commonPrefixes;
    private String encodingType;

    @XmlElement(name = "KeyMarker")
    public String getKeyMarker() {
        return keyMarker;
    }

    public void setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
    }

    @XmlElement(name = "UploadIdMarker")
    public String getUploadIdMarker() {
        return uploadIdMarker;
    }

    public void setUploadIdMarker(String uploadIdMarker) {
        this.uploadIdMarker = uploadIdMarker;
    }

    @XmlElement(name = "NextKeyMarker")
    public String getNextKeyMarker() {
        return nextKeyMarker;
    }

    public void setNextKeyMarker(String nextKeyMarker) {
        this.nextKeyMarker = nextKeyMarker;
    }

    @XmlElement(name = "NextUploadIdMarker")
    public String getNextUploadIdMarker() {
        return nextUploadIdMarker;
    }

    public void setNextUploadIdMarker(String nextUploadIdMarker) {
        this.nextUploadIdMarker = nextUploadIdMarker;
    }

    @XmlElement(name = "Upload")
    public List<MultipartUpload> getMultipartUploads() {
        return uploads;
    }

    public void setMultipartUploads(List<MultipartUpload> uploads) {
        this.uploads = uploads;
    }

    @XmlElement(name = "MaxUploads")
    public int getMaxUploads() {
        return maxUploads;
    }

    public void setMaxUploads(int maxUploads) {
        this.maxUploads = maxUploads;
    }

    @XmlElement(name = "Truncated")
    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean truncated) {
        isTruncated = truncated;
    }

    @XmlElement(name = "Bucket")
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String deleteMarkers) {
        this.bucket = bucket;
    }

    @XmlElement(name = "Prefix")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @XmlElement(name = "Delimiter")
    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @XmlElement(name = "CommonPrefix")
    public List<CommonPrefix> getCommonPrefixes() {
        return commonPrefixes;
    }

    public void setCommonPrefixes(List<CommonPrefix> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
    }

    @XmlElement(name = "EncodingType")
    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

}