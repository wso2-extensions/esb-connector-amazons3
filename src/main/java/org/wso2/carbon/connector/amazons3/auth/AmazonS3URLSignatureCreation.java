/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.connector.amazons3.auth;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.registry.Registry;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jets3t.service.CloudFrontService;
import org.jets3t.service.CloudFrontServiceException;
import org.jets3t.service.utils.ServiceUtils;
import org.wso2.carbon.connector.amazons3.util.AmazonS3Constants;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.mediation.registry.WSO2Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

import java.io.IOException;
import java.security.Security;
import java.text.ParseException;

/**
 * Class AmazonS3URLSignatreCreation which helps to generate signature for the url for
 * Amazon S3 WSO2 ESB Connector.
 * @since 1.0.4
 */
public class AmazonS3URLSignatureCreation extends AbstractConnector {

    /**
     * Connect method which is generating signature for url for the object access
     *
     * @param messageContext ESB messageContext.
     */
    public final void connect(final MessageContext messageContext) {
        Security.addProvider(new BouncyCastleProvider());
        String distributionDomain = messageContext.getProperty(AmazonS3Constants.BUCKET_URL).toString();
        String privateKeyFilePath = messageContext.getProperty(AmazonS3Constants.PRIVATE_KEY_FILE_PATH).toString();
        String s3ObjectKey = messageContext.getProperty(AmazonS3Constants.OBJECT_NAME).toString();
        String queryParams = messageContext.getProperty(AmazonS3Constants.QUERY_PARAMS).toString();
        String policyResourcePath = distributionDomain + "/" + s3ObjectKey + queryParams;
        String keyPairId = messageContext.getProperty(AmazonS3Constants.KEY_PAIR_ID).toString();
        String policyType = messageContext.getProperty(AmazonS3Constants.POLICY_TYPE).toString();
        try {
            Registry registry = messageContext.getConfiguration().getRegistry();
            // Get the registry path.
            org.wso2.carbon.registry.core.Resource resource = ((WSO2Registry) registry).getResource(privateKeyFilePath);
            // Convert your DER file into a byte array.
            byte[] derPrivateKey = ServiceUtils.readInputStreamToBytes(resource.getContentStream());
            // Generate a "canned" signed URL to allow access to specific distribution and object
            if (policyType.equals(AmazonS3Constants.CANNED_POLICY)) {
                String signedUrlCanned = CloudFrontService.signUrlCanned(
                        policyResourcePath, // Resource URL or Path
                        keyPairId,     // Certificate identifier,
                        // an active trusted signer for the distribution
                        derPrivateKey, // DER Private key data
                        ServiceUtils.parseIso8601Date(messageContext.getProperty(AmazonS3Constants.DATE_LESS_THAN).toString()) // DateLessThan
                );
                messageContext.setProperty(AmazonS3Constants.BUCKET_URL, signedUrlCanned);
            } else if (policyType.equals(AmazonS3Constants.CUSTOM_POLICY)) {
                // Build a policy document to define custom restrictions for a signed URL.
                String policy = CloudFrontService.buildPolicyForSignedUrl(
                        // Resource path (optional, can include '*' and '?' wildcards)
                        policyResourcePath,
                        // DateLessThan
                        ServiceUtils.parseIso8601Date(messageContext.getProperty(AmazonS3Constants.DATE_LESS_THAN).toString()),
                        // CIDR IP address restriction (optional, 0.0.0.0/0 means everyone)
                        messageContext.getProperty(AmazonS3Constants.IP_ADDRESS).toString(),
                        // DateGreaterThan (optional)
                        ServiceUtils.parseIso8601Date(messageContext.getProperty(AmazonS3Constants.DATE_GREATER_THAN).toString())
                );
                // Generate a signed URL using a custom policy document.
                String signedUrl = CloudFrontService.signUrl(
                        // Resource URL or Path
                        policyResourcePath,
                        // Certificate identifier, an active trusted signer for the distribution
                        keyPairId,
                        // DER Private key data
                        derPrivateKey,
                        // Access control policy
                        policy
                );
                messageContext.setProperty(AmazonS3Constants.BUCKET_URL, signedUrl);
            }
        } catch (IOException ioe) {
            storeErrorResponseStatus(messageContext, ioe, AmazonS3Constants.INVALID_KEY_ERROR_CODE);
            handleException("Could not find Private Key File", ioe, messageContext);
        } catch (CloudFrontServiceException cfse) {
            storeErrorResponseStatus(messageContext, cfse, AmazonS3Constants.INVALID_KEY_ERROR_CODE);
            handleException("Exception when Creating Policy", cfse, messageContext);
        } catch (ParseException pe) {
            storeErrorResponseStatus(messageContext, pe, AmazonS3Constants.INVALID_KEY_ERROR_CODE);
            handleException("Could not parse the date in correct format", pe, messageContext);
        } catch (RegistryException re) {
            handleException("No File in Registry", re, messageContext);
        }
    }

    /**
     * Add a Throwable to a message context, the message from the throwable is embedded as the Synapse Constant
     * ERROR_MESSAGE.
     *
     * @param ctxt      message context to which the error tags need to be added
     * @param throwable Throwable that needs to be parsed and added
     * @param errorCode errorCode mapped to the exception
     */
    public static void storeErrorResponseStatus(final MessageContext ctxt, final Throwable throwable, final int errorCode) {
        ctxt.setProperty(SynapseConstants.ERROR_CODE, errorCode);
        ctxt.setProperty(SynapseConstants.ERROR_MESSAGE, throwable.getMessage());
        ctxt.setFaultResponse(true);
    }
}
