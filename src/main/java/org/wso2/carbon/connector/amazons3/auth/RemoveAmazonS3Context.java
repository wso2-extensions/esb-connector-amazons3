/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.connector.amazons3.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.wso2.carbon.connector.amazons3.constants.AmazonS3Constants;
import org.wso2.carbon.connector.core.AbstractConnector;

/**
 * This class helps to remove the properties from message context.
 */
public class RemoveAmazonS3Context extends AbstractConnector {
    private static Log log = LogFactory.getLog(RemoveAmazonS3Context.class);

    public void connect(MessageContext msgContext) throws SynapseException {
        try {
            removeContext(msgContext);
        } catch (Exception e) {
            throw new SynapseException("Error while removing the properties", e);
        }
    }

    /**
     * Remove the previous amazonsqs method's context
     *
     * @param messageContext message context
     */
    private void removeContext(final MessageContext messageContext) {
        log.debug("Removing the unneeded properties of the already run methods");
        Object[] keys = messageContext.getPropertyKeySet().toArray();
        for (Object key : keys) {
            if ((key).toString().startsWith(AmazonS3Constants.DELIMITER)
                    || (key).toString().startsWith(AmazonS3Constants.ENCODING_TYPE)
                    || (key).toString().startsWith(AmazonS3Constants.KEY_MARKER)
                    || (key).toString().startsWith(AmazonS3Constants.MARKER)
                    || (key).toString().startsWith(AmazonS3Constants.MAX_KEYS)
                    || (key).toString().startsWith(AmazonS3Constants.PREFIX)
                    || (key).toString().startsWith(AmazonS3Constants.VERSION_ID_MARKER)
                    || (key).toString().startsWith(AmazonS3Constants.QUERY_STRING)
                    || (key).toString().startsWith(AmazonS3Constants.VERSION_ID)
                    || (key).toString().startsWith(AmazonS3Constants.UPLOAD_ID_MARKER)
                    || (key).toString().startsWith(AmazonS3Constants.MAX_UPLOADS)
                    || (key).toString().startsWith(AmazonS3Constants.PART_NUMBER)
                    || (key).toString().startsWith(AmazonS3Constants.UPLOAD_ID)
                    || (key).toString().startsWith(AmazonS3Constants.RESPONSE_CONTENT_TYPE)
                    || (key).toString().startsWith(AmazonS3Constants.RESPONSE_EXPIRES)
                    || (key).toString().startsWith(AmazonS3Constants.RESPONSE_CACHE_CONTROL)
                    || (key).toString().startsWith(AmazonS3Constants.RESPONSE_CONTENT_DISPOSITION)
                    || (key).toString().startsWith(AmazonS3Constants.RESPONSE_CONTENT_ENCODING)) {
                messageContext.getPropertyKeySet().remove(key);
            }
        }
    }
}
