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

@XmlRootElement(name = "Rule")
public class ReplicationRule {
    private String id;
    private int priority;
    private String prefix;
    private ReplicationRuleFilter filter;
    private String status;
    private SourceSelectionCriteria sourceSelectionCriteria;
    private ExistingObjectReplication existingObjectReplication;
    private Destination destination;
    private DeleteMarkerReplication deleteMarkerReplication;

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
    public ReplicationRuleFilter getFilter() {
        return filter;
    }

    public void setFilter(ReplicationRuleFilter filter) {
        this.filter = filter;
    }

    @XmlElement(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlElement(name = "SourceSelectionCriteria")
    public SourceSelectionCriteria getSourceSelectionCriteria() {
        return sourceSelectionCriteria;
    }

    public void setSourceSelectionCriteria(SourceSelectionCriteria sourceSelectionCriteria) {
        this.sourceSelectionCriteria = sourceSelectionCriteria;
    }

    @XmlElement(name = "ExistingObjectReplication")
    public ExistingObjectReplication getExistingObjectReplication() {
        return existingObjectReplication;
    }

    public void setExistingObjectReplication(ExistingObjectReplication existingObjectReplication) {
        this.existingObjectReplication = existingObjectReplication;
    }

    @XmlElement(name = "Priority")
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @XmlElement(name = "Destination")
    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    @XmlElement(name = "DeleteMarkerReplication")
    public DeleteMarkerReplication getDeleteMarkerReplication() {
        return deleteMarkerReplication;
    }

    public void setDeleteMarkerReplication(DeleteMarkerReplication deleteMarkerReplication) {
        this.deleteMarkerReplication = deleteMarkerReplication;
    }
}