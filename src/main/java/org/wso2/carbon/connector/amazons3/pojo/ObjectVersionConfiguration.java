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

@XmlRootElement(name = "ListVersionsResult")
public class ObjectVersionConfiguration {
    private String keyMarker;
    private String versionIdMarker;
    private String nextKeyMarker;
    private String nextVersionIdMarker;
    private List<ObjectVersion> versions;
    private int maxKeys;
    private boolean isTruncated;
    private List<DeleteMarkerEntry> deleteMarkers;
    private String name;
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

    @XmlElement(name = "VersionIdMarker")
    public String getVersionIdMarker() {
        return versionIdMarker;
    }

    public void setVersionIdMarker(String versionIdMarker) {
        this.versionIdMarker = versionIdMarker;
    }

    @XmlElement(name = "NextKeyMarker")
    public String getNextKeyMarker() {
        return nextKeyMarker;
    }

    public void setNextKeyMarker(String nextKeyMarker) {
        this.nextKeyMarker = nextKeyMarker;
    }

    @XmlElement(name = "NextVersionIdMarker")
    public String getNextVersionIdMarker() {
        return nextVersionIdMarker;
    }

    public void setNextVersionIdMarker(String nextVersionIdMarker) {
        this.nextVersionIdMarker = nextVersionIdMarker;
    }

    @XmlElement(name = "Version")
    public List<ObjectVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<ObjectVersion> versions) {
        this.versions = versions;
    }

    @XmlElement(name = "MaxKeys")
    public int getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }

    @XmlElement(name = "Truncated")
    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean truncated) {
        isTruncated = truncated;
    }

    @XmlElement(name = "DeleteMarker")
    public List<DeleteMarkerEntry> getDeleteMarkers() {
        return deleteMarkers;
    }

    public void setDeleteMarkers(List<DeleteMarkerEntry> deleteMarkers) {
        this.deleteMarkers = deleteMarkers;
    }

    @XmlElement(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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