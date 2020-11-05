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

@XmlRootElement(name = "LifecycleRule")
public class LifecycleRule {
    private LifecycleExpiration expiration;
    private String id;
    private String prefix;
    private LifecycleRuleFilter filter;
    private String status;
    private List<Transition> transitions;
    private List<NoncurrentVersionTransition> noncurrentVersionTransitions;
    private NoncurrentVersionExpiration noncurrentVersionExpiration;
    private AbortIncompleteMultipartUpload abortIncompleteMultipartUpload;

    @XmlElement(name = "Expiration")
    public LifecycleExpiration getExpiration() {
        return expiration;
    }

    public void setExpiration(LifecycleExpiration expiration) {
        this.expiration = expiration;
    }

    @XmlElement(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "Prefix")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @XmlElement(name = "Filter")
    public LifecycleRuleFilter getFilter() {
        return filter;
    }

    public void setFilter(LifecycleRuleFilter filter) {
        this.filter = filter;
    }

    @XmlElement(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlElement(name = "Transitions")
    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    @XmlElement(name = "NoncurrentVersionTransitions")
    public List<NoncurrentVersionTransition> getNoncurrentVersionTransitions() {
        return noncurrentVersionTransitions;
    }

    public void setNoncurrentVersionTransitions(List<NoncurrentVersionTransition> noncurrentVersionTransitions) {
        this.noncurrentVersionTransitions = noncurrentVersionTransitions;
    }

    @XmlElement(name = "NoncurrentVersionExpiration")
    public NoncurrentVersionExpiration getNoncurrentVersionExpiration() {
        return noncurrentVersionExpiration;
    }

    public void setNoncurrentVersionExpiration(NoncurrentVersionExpiration noncurrentVersionExpiration) {
        this.noncurrentVersionExpiration = noncurrentVersionExpiration;
    }

    @XmlElement(name = "AbortIncompleteMultipartUpload")
    public AbortIncompleteMultipartUpload getAbortIncompleteMultipartUpload() {
        return abortIncompleteMultipartUpload;
    }

    public void setAbortIncompleteMultipartUpload(AbortIncompleteMultipartUpload abortIncompleteMultipartUpload) {
        this.abortIncompleteMultipartUpload = abortIncompleteMultipartUpload;
    }
}