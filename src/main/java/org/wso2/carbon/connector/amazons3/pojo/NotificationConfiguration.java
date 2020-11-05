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

@XmlRootElement(name = "NotificationConfiguration")
public class NotificationConfiguration {
    private List<TopicConfiguration> topicConfiguration;
    private List<QueueConfiguration> queueConfiguration;
    private List<LambdaFunctionConfiguration> lambdaFunctionConfiguration;

    @XmlElement(name = "TopicConfiguration")
    public List<TopicConfiguration> getTopicConfigurations() {
        return topicConfiguration;
    }

    public void setTopicConfigurations(List<TopicConfiguration> topicConfigurations) {
        this.topicConfiguration = topicConfigurations;
    }

    @XmlElement(name = "QueueConfiguration")
    public List<QueueConfiguration> getQueueConfigurations() {
        return queueConfiguration;
    }

    public void setQueueConfigurations(List<QueueConfiguration> queueConfigurations) {
        this.queueConfiguration = queueConfigurations;
    }

    @XmlElement(name = "LambdaFunctionConfiguration")
    public List<LambdaFunctionConfiguration> getLambdaFunctionConfigurations() {
        return lambdaFunctionConfiguration;
    }

    public void setLambdaFunctionConfigurations(List<LambdaFunctionConfiguration> lambdaFunctionConfiguration) {
        this.lambdaFunctionConfiguration = lambdaFunctionConfiguration;
    }
}