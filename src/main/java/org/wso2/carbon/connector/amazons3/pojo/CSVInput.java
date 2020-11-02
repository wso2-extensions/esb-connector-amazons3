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

@XmlRootElement(name = "CSVInput")
public class CSVInput {
    private String fileHeaderInfo;
    private String quoteEscapeCharacter;
    private String recordDelimiter;
    private String fieldDelimiter;
    private String quoteCharacter;
    private String comments;
    private boolean allowQuotedRecordDelimiter;

    @XmlElement(name = "FileHeaderInfo")
    public String getFileHeaderInfo() {
        return fileHeaderInfo;
    }

    public void setFileHeaderInfo(String fileHeaderInfo) {
        this.fileHeaderInfo = fileHeaderInfo;
    }

    @XmlElement(name = "QuoteEscapeCharacter")
    public String getQuoteEscapeCharacter() {
        return quoteEscapeCharacter;
    }

    public void setQuoteEscapeCharacter(String quoteEscapeCharacter) {
        this.quoteEscapeCharacter = quoteEscapeCharacter;
    }

    @XmlElement(name = "RecordDelimiter")
    public String getRecordDelimiter() {
        return recordDelimiter;
    }

    public void setRecordDelimiter(String recordDelimiter) {
        this.recordDelimiter = recordDelimiter;
    }

    @XmlElement(name = "FieldDelimiter")
    public String getFieldDelimiter() {
        return fieldDelimiter;
    }

    public void setFieldDelimiter(String fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    @XmlElement(name = "QuoteCharacter")
    public String getQuoteCharacter() {
        return quoteCharacter;
    }

    public void setQuoteCharacter(String quoteCharacter) {
        this.quoteCharacter = quoteCharacter;
    }

    @XmlElement(name = "Comments")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @XmlElement(name = "AllowQuotedRecordDelimiter")
    public boolean isAllowQuotedRecordDelimiter() {
        return allowQuotedRecordDelimiter;
    }

    public void setAllowQuotedRecordDelimiter(boolean allowQuotedRecordDelimiter) {
        this.allowQuotedRecordDelimiter = allowQuotedRecordDelimiter;
    }

}