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

package org.wso2.carbon.connector.amazons3.utils;

/**
 * Contains error codes and details
 * related to file connector
 */
public enum Error {

    CONNECTION_ERROR("500", "S3:CONNECTION_ERROR"),
    INVALID_CONFIGURATION("400", "S3:INVALID_CONFIGURATION"),
    BAD_REQUEST("400", "S3:BAD_REQUEST"),
    NOT_FOUND("404", "S3:NOT_FOUND"),
    CONFLICT("409", "S3:CONFLICT");


    private String code;
    private String message;

    /**
     * Create an error code.
     *
     * @param code    error code represented by number
     * @param message error message
     */
    Error(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getErrorCode() {
        return this.code;
    }

    public String getErrorDetail() {
        return this.message;
    }

}
