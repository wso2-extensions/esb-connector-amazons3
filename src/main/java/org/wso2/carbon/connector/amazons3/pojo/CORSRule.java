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

@XmlRootElement(name = "CORSRule")
public class CORSRule {
    private List<String> allowedHeader;
    private List<String> allowedMethod;
    private List<String> allowedOrigin;
    private List<String> exposeHeader;
    private int maxAgeSeconds;

    @XmlElement(name = "AllowedHeader")
    public List<String> getAllowedHeader() {
        return allowedHeader;
    }

    public void setAllowedHeader(List<String> allowedHeader) {
        this.allowedHeader = allowedHeader;
    }

    @XmlElement(name = "AllowedMethod")
    public List<String> getAllowedMethod() {
        return allowedMethod;
    }

    public void setAllowedMethod(List<String> allowedMethod) {
        this.allowedMethod = allowedMethod;
    }

    @XmlElement(name = "AllowedOrigin")
    public List<String> getAllowedOrigin() {
        return allowedOrigin;
    }

    public void setAllowedOrigin(List<String> allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
    }

    @XmlElement(name = "ExposeHeader")
    public List<String> getExposeHeader() {
        return exposeHeader;
    }

    public void setExposeHeader(List<String> exposeHeader) {
        this.exposeHeader = exposeHeader;
    }

    @XmlElement(name = "MaxAgeSeconds")
    public int getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public void setMaxAgeSeconds(int maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
    }
}