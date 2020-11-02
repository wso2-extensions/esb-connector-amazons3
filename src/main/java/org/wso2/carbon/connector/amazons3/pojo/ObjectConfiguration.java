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

@XmlRootElement(name = "ListBucketResult")
public class ObjectConfiguration {
    private boolean isTruncated;
    private String marker;
    private String nextMarker;
    private List<S3Object> contents;
    private String name;
    private String prefix;
    private String delimiter;
    private int maxKeys;
    private List<CommonPrefix> commonPrefixes;
    private String encodingType;

    @XmlElement(name = "Marker")
    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    @XmlElement(name = "NextMarker")
    public String getNextMarker() {
        return nextMarker;
    }

    public void setNextMarker(String nextKeyMarker) {
        this.nextMarker = nextMarker;
    }

    @XmlElement(name = "Contents")
    public List<S3Object> getContents() {
        return contents;
    }

    public void setContents(List<S3Object> contents) {
        this.contents = contents;
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