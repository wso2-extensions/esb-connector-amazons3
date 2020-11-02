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

@XmlRootElement(name = "RestoreRequest")
public class RestoreRequest {
    private int days;
    private GlacierJobParameters glacierJobParameters;
    private String type;
    private String tier;
    private String description;
    private SelectParameters selectParameters;
    private OutputLocation outputLocation;

    @XmlElement(name = "Days")
    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @XmlElement(name = "GlacierJobParameters")
    public GlacierJobParameters getGlacierJobParameters() {
        return glacierJobParameters;
    }

    public void setGlacierJobParameters(GlacierJobParameters glacierJobParameters) {
        this.glacierJobParameters = glacierJobParameters;
    }

    @XmlElement(name = "Type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement(name = "Tier")
    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    @XmlElement(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "SelectParameters")
    public SelectParameters getSelectParameters() {
        return selectParameters;
    }

    public void setSelectParameters(SelectParameters selectParameters) {
        this.selectParameters = selectParameters;
    }

    @XmlElement(name = "OutputLocation")
    public OutputLocation getOutputLocation() {
        return outputLocation;
    }

    public void setOutputLocation(OutputLocation outputLocation) {
        this.outputLocation = outputLocation;
    }
}