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

@XmlRootElement(name = "WebsiteConfiguration")
public class WebsiteConfiguration {
    private List<RoutingRule> routingRule;
    private ErrorDocument errorDocument;
    private IndexDocument indexDocument;
    private RedirectAllRequestsTo redirectAllRequestsTo;

    @XmlElement(name = "RoutingRule")
    public List<RoutingRule> getRoutingRules() {
        return routingRule;
    }

    public void setRoutingRules(List<RoutingRule> routingRule) {
        this.routingRule = routingRule;
    }

    @XmlElement(name = "ErrorDocument")
    public ErrorDocument getErrorDocument() {
        return errorDocument;
    }

    public void setErrorDocument(ErrorDocument errorDocument) {
        this.errorDocument = errorDocument;
    }

    @XmlElement(name = "IndexDocument")
    public IndexDocument getIndexDocument() {
        return indexDocument;
    }

    public void setIndexDocument(IndexDocument indexDocument) {
        this.indexDocument = indexDocument;
    }

    @XmlElement(name = "RedirectAllRequestsTo")
    public RedirectAllRequestsTo getRedirectAllRequestsTo() {
        return redirectAllRequestsTo;
    }

    public void setRedirectAllRequestsTo(RedirectAllRequestsTo redirectAllRequestsTo) {
        this.redirectAllRequestsTo = redirectAllRequestsTo;
    }
}