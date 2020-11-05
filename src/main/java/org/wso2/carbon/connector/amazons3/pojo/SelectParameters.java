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

@XmlRootElement(name = "SelectParameters")
public class SelectParameters {
    private InputSerialization inputSerialization;
    private String expressionType;
    private String expression;
    private OutputSerialization outputSerialization;

    @XmlElement(name = "InputSerialization")
    public InputSerialization getInputSerialization() {
        return inputSerialization;
    }

    public void setInputSerialization(InputSerialization inputSerialization) {
        this.inputSerialization = inputSerialization;
    }

    @XmlElement(name = "ExpressionType")
    public String getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(String expressionType) {
        this.expressionType = expressionType;
    }

    @XmlElement(name = "Expression")
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @XmlElement(name = "OutputSerialization")
    public OutputSerialization getOutputSerialization() {
        return outputSerialization;
    }

    public void setOutputSerialization(OutputSerialization outputSerialization) {
        this.outputSerialization = outputSerialization;
    }
}