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

import org.apache.commons.lang.StringUtils;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.wso2.carbon.connector.amazons3.constants.AmazonS3Constants;
import org.wso2.carbon.connector.core.AbstractConnector;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class AmazonS3AuthConnector which helps to generate authentication header for Amazon S3 WSO2 ESB Connector.
 */
public class AmazonS3AuthConnector extends AbstractConnector {

    /**
     * Connect method which is generating authentication of the connector for each request.
     *
     * @param messageContext ESB messageContext.
     */
    public final void connect(final MessageContext messageContext) {
        final StringBuilder canonicalRequest = new StringBuilder();
        final StringBuilder stringToSign = new StringBuilder();
        final StringBuilder authHeader = new StringBuilder();
        final Map<String, String> parametersMap = getParametersMap(messageContext);
        final Locale defaultLocale = Locale.getDefault();

        final Date date = new Date();
        final TimeZone timeZone = TimeZone.getTimeZone(AmazonS3Constants.TIME_ZONE);
        final SimpleDateFormat dateFormat = new SimpleDateFormat(AmazonS3Constants.CURR_DATE_FORMAT, defaultLocale);
        dateFormat.setTimeZone(timeZone);
        final String currentDate = dateFormat.format(date);

        final DateFormat shortDateFormat = new SimpleDateFormat(AmazonS3Constants.SHORT_DATE_FORMAT);
        shortDateFormat.setTimeZone(timeZone);
        final String shortDate = shortDateFormat.format(date);
        try{
            final String dateTrimmed = currentDate.trim();
            final Map<String, String> amzHeadersMap = new HashMap<>();
            final Map<String, String> queryParamsMap = new HashMap<>();
            final StringBuilder canonicalHeaders = new StringBuilder();
            final StringBuilder canonicalQueryString = new StringBuilder();
            final StringBuilder signedHeader = new StringBuilder();

            canonicalRequest.append(parametersMap.get(AmazonS3Constants.METHOD_TYPE))
                    .append(AmazonS3Constants.NEW_LINE);
            // Setting the canonicalized resource.
            if(StringUtils.isNotEmpty(parametersMap.get(AmazonS3Constants.BUCKET_NAME))) {
                canonicalRequest.append(AmazonS3Constants.FORWARD_SLASH).append(
                        parametersMap.get(AmazonS3Constants.BUCKET_NAME));
            }
            String urlRemainder = (String) messageContext.getProperty(AmazonS3Constants.URI_REMAINDER);
            if (urlRemainder != null && !urlRemainder.isEmpty()) {
                canonicalRequest.append(urlRemainder);
            }

            // Setting canonicalQueryString
            final Map<String, String> queryParametersMap = getQueryParamKeysMap();
            for (Map.Entry<String, String> entry : queryParametersMap.entrySet()) {
                String key = entry.getKey();
                String tempParam = parametersMap.get(key);
                if (!tempParam.isEmpty()) {
                    queryParamsMap.put(queryParametersMap.get(key),
                            tempParam.replaceAll(AmazonS3Constants.REGEX, AmazonS3Constants.EMPTY_STR));
                }
            }

            String queryString = (String) messageContext.getProperty(AmazonS3Constants.QUERY_STRING);
            final SortedSet<String> queryKeys = new TreeSet<>(queryParamsMap.keySet());

            for (String key : queryKeys) {
                if(key.equals(AmazonS3Constants.QUERY_STRING)){
                    canonicalQueryString.append(URLEncoder.encode(queryString, AmazonS3Constants.UTF_8))
                            .append(AmazonS3Constants.EQUAL).append(AmazonS3Constants.EMPTY_STR)
                            .append(AmazonS3Constants.AMP);
                } else {
                    String queryValue = queryParamsMap.get(key);
                    canonicalQueryString.append(URLEncoder.encode(key, AmazonS3Constants.UTF_8))
                            .append(AmazonS3Constants.EQUAL)
                            .append(URLEncoder.encode(queryValue, AmazonS3Constants.UTF_8))
                            .append(AmazonS3Constants.AMP);
                }

            }

            //Remove additionally added ampersand at the end from canonicalQueryString and append to canonicalRequest.
            if(canonicalQueryString.length() > 0) {
                canonicalRequest.append(AmazonS3Constants.NEW_LINE);
                canonicalRequest.append(canonicalQueryString.substring(0, canonicalQueryString.length() - 1));
            } else {
                canonicalRequest.append(AmazonS3Constants.NEW_LINE);
            }

            if (Boolean.parseBoolean(parametersMap.get(AmazonS3Constants.IS_XAMZ_DATE))) {
                canonicalRequest.append(AmazonS3Constants.NEW_LINE);
                amzHeadersMap.put(AmazonS3Constants.HD_XAMZ_DATE, dateTrimmed);
                messageContext.setProperty(AmazonS3Constants.IS_XAMZ_DATE_VAL, dateTrimmed);
            } else {
                canonicalRequest.append(dateTrimmed).append(AmazonS3Constants.NEW_LINE);
            }

            final Map<String, String> amzHeaderKeysMap = getHeaderKeysMap();
            for (Map.Entry<String, String> entry : amzHeaderKeysMap.entrySet()) {
                String key = entry.getKey();
                String tempParam = parametersMap.get(key);
                if (!tempParam.isEmpty()) {
                    amzHeadersMap.put(amzHeaderKeysMap.get(key), tempParam);
                }
            }
    
            final SortedSet<String> keys = new TreeSet<>(amzHeadersMap.keySet());
            for (String key : keys) {
                String headerValues = amzHeadersMap.get(key);
                canonicalHeaders.append(key.toLowerCase(defaultLocale)).append(AmazonS3Constants.COLON)
                        .append(headerValues).append(AmazonS3Constants.NEW_LINE);
                signedHeader.append(key.toLowerCase(defaultLocale));
                signedHeader.append(AmazonS3Constants.SEMI_COLON);
            }
            canonicalRequest.append(canonicalHeaders).append(AmazonS3Constants.NEW_LINE);

            // Remove unwanted semi-colon at the end of the signedHeader string
            String signedHeaders = "";
            if (signedHeader.length() > 0) {
                signedHeaders = signedHeader.substring(0, signedHeader.length() - 1);
            }
            canonicalRequest.append(signedHeaders).append(AmazonS3Constants.NEW_LINE);
            canonicalRequest.append(AmazonS3Constants.UNSIGNED_PAYLOAD);

            System.out.println("#######################\n\n\n");
            System.out.println(canonicalRequest);
            System.out.println("#######################");
            // Create stringToSign
            stringToSign.append(AmazonS3Constants.AWS4_HMAC_SHA_256);
            stringToSign.append(AmazonS3Constants.NEW_LINE);
            stringToSign.append(dateTrimmed);
            stringToSign.append(AmazonS3Constants.NEW_LINE);
    
            stringToSign.append(shortDate);
            stringToSign.append(AmazonS3Constants.FORWARD_SLASH);
            stringToSign.append(messageContext.getProperty(AmazonS3Constants.REGION));
            stringToSign.append(AmazonS3Constants.FORWARD_SLASH);
            stringToSign.append(messageContext.getProperty(AmazonS3Constants.SERVICE));
            stringToSign.append(AmazonS3Constants.FORWARD_SLASH);
            stringToSign.append(messageContext.getProperty(AmazonS3Constants.TERMINATION_STRING));
    
            stringToSign.append(AmazonS3Constants.NEW_LINE);
            stringToSign.append(bytesToHex(hash(messageContext, canonicalRequest.toString())).toLowerCase());

            final byte[] signingKey =
                    getSignatureKey(messageContext,
                            messageContext.getProperty(AmazonS3Constants.SECRET_ACCESS_KEY).toString(),
                            shortDate, messageContext.getProperty(AmazonS3Constants.REGION).toString(),
                            messageContext.getProperty(AmazonS3Constants.SERVICE).toString());
            
            // Construction of authorization header value to be included in API request
            authHeader.append(AmazonS3Constants.AWS4_HMAC_SHA_256);
            authHeader.append(" ");
            authHeader.append(AmazonS3Constants.CREDENTIAL);
            authHeader.append(AmazonS3Constants.EQUAL);
            authHeader.append(messageContext.getProperty(AmazonS3Constants.ACCESS_KEY_ID));
            authHeader.append(AmazonS3Constants.FORWARD_SLASH);
            authHeader.append(shortDate);
            authHeader.append(AmazonS3Constants.FORWARD_SLASH);
            authHeader.append(messageContext.getProperty(AmazonS3Constants.REGION));
            authHeader.append(AmazonS3Constants.FORWARD_SLASH);
            authHeader.append(messageContext.getProperty(AmazonS3Constants.SERVICE));
            authHeader.append(AmazonS3Constants.FORWARD_SLASH);
            authHeader.append(messageContext.getProperty(AmazonS3Constants.TERMINATION_STRING));
            authHeader.append(AmazonS3Constants.COMMA);
            authHeader.append(AmazonS3Constants.SIGNED_HEADERS);
            authHeader.append(AmazonS3Constants.EQUAL);
            authHeader.append(signedHeaders);
            authHeader.append(AmazonS3Constants.COMMA);
            authHeader.append(AmazonS3Constants.API_SIGNATURE);
            authHeader.append(AmazonS3Constants.EQUAL);
            authHeader.append(bytesToHex(hmacSHA256(signingKey, stringToSign.toString())).toLowerCase());

            // Adds authorization header to message context
            messageContext.setProperty(AmazonS3Constants.AUTH_CODE, authHeader.toString());
        } catch (InvalidKeyException exc) {
            storeErrorResponseStatus(messageContext, exc, AmazonS3Constants.INVALID_KEY_ERROR_CODE);
            handleException(AmazonS3Constants.INVALID_KEY_ERROR, exc, messageContext);
        } catch (NoSuchAlgorithmException exc) {
            storeErrorResponseStatus(messageContext, exc, AmazonS3Constants.NO_SUCH_ALGORITHM_ERROR_CODE);
            handleException(AmazonS3Constants.NO_SUCH_ALGORITHM_ERROR, exc, messageContext);
        } catch (IllegalStateException exc) {
            storeErrorResponseStatus(messageContext, exc, AmazonS3Constants.ILLEGAL_STATE_ERROR_CODE);
            handleException(AmazonS3Constants.CONNECTOR_ERROR, exc, messageContext);
        } catch (UnsupportedEncodingException exc) {
            storeErrorResponseStatus(messageContext, exc, AmazonS3Constants.UNSUPPORTED_ENCORDING_ERROR_CODE);
            handleException(AmazonS3Constants.CONNECTOR_ERROR, exc, messageContext);
        }
    }

    /**
     * getKeys method used to return list of predefined parameter keys.
     *
     * @return list of parameter key value.
     */
    private String[] getKeys() {

        return new String[]{AmazonS3Constants.ACCESS_KEY_ID, AmazonS3Constants.SECRET_ACCESS_KEY,
                AmazonS3Constants.METHOD_TYPE, AmazonS3Constants.CONTENT_MD5, AmazonS3Constants.CONTENT_TYPE,
                AmazonS3Constants.BUCKET_NAME, AmazonS3Constants.IS_XAMZ_DATE, AmazonS3Constants.XAMZ_SECURITY_TOKEN,
                AmazonS3Constants.XAMZ_ACL, AmazonS3Constants.XAMZ_GRANT_READ, AmazonS3Constants.XAMZ_GRANT_WRITE,
                AmazonS3Constants.XAMZ_GRANT_READ_ACP, AmazonS3Constants.XAMZ_GRANT_WRITE_ACP,
                AmazonS3Constants.XAMZ_GRANT_FULL_CONTROL, AmazonS3Constants.XAMZ_META,
                AmazonS3Constants.XAMZ_SERVE_ENCRYPTION, AmazonS3Constants.XAMZ_STORAGE_CLASS,
                AmazonS3Constants.XAMZ_WEBSITE_LOCATION, AmazonS3Constants.XAMZ_MFA,
                AmazonS3Constants.XAMZ_COPY_SOURCE, AmazonS3Constants.XAMZ_COPY_SOURCE_RANGE,
                AmazonS3Constants.XAMZ_METADATA_DIRECTIVE, AmazonS3Constants.XAMZ_COPY_SOURCE_IF_MATCH,
                AmazonS3Constants.XAMZ_COPY_SOURCE_IF_NONE_MATCH, AmazonS3Constants.XAMZ_COPY_SOURCE_IF_UNMODIFIED_SINCE,
                AmazonS3Constants.XAMZ_COPY_SOURCE_IF_MODIFIED_SINCE, AmazonS3Constants.HOST,
                AmazonS3Constants.XMAZ_SERVER_SIDE_ENCRYPTION_CUSTOMER_ALGORITHM,
                AmazonS3Constants.XMAZ_SERVER_SIDE_ENCRYPTION_CUSTOMER_KEY,
                AmazonS3Constants.XMAZ_SERVER_SIDE_ENCRYPTION_CUSTOMER_KEY_MD5, AmazonS3Constants.XAMZ_CONTENT_SHA256,
                AmazonS3Constants.DELIMITER, AmazonS3Constants.ENCODING_TYPE, AmazonS3Constants.KEY_MARKER,
                AmazonS3Constants.MAX_KEYS, AmazonS3Constants.PREFIX, AmazonS3Constants.VERSION_ID_MARKER,
                AmazonS3Constants.QUERY_STRING, AmazonS3Constants.VERSION_ID, AmazonS3Constants.MARKER,
                AmazonS3Constants.UPLOAD_ID_MARKER, AmazonS3Constants.MAX_UPLOADS, AmazonS3Constants.PART_NUMBER,
                AmazonS3Constants.UPLOAD_ID, AmazonS3Constants.RESPONSE_CONTENT_TYPE,
                AmazonS3Constants.RESPONSE_CONTENT_LANGUAGE, AmazonS3Constants.RESPONSE_EXPIRES,
                AmazonS3Constants.RESPONSE_CACHE_CONTROL, AmazonS3Constants.RESPONSE_CONTENT_DISPOSITION,
                AmazonS3Constants.RESPONSE_CONTENT_ENCODING, AmazonS3Constants.PART_NUMBER_MARKER,
                AmazonS3Constants.MAX_PARTS};

    }

    /**
     * getParametersMap method used to return list of parameter values passed in via proxy.
     *
     * @param messageContext ESB messageContext.
     * @return assigned parameter values as a HashMap.
     */
    private Map<String, String> getParametersMap(final MessageContext messageContext) {

        String[] keys = getKeys();
        Map<String, String> parametersMap = new HashMap<>();
        for (String key : keys) {
            String paramValue =
                    (messageContext.getProperty(key) != null) ? (String) messageContext
                            .getProperty(key) : AmazonS3Constants.EMPTY_STR;
            parametersMap.put(key, paramValue);
        }
        return parametersMap;
    }

    /**
     * queryParamKeysMap method used to return list of query parameter keys with values.
     *
     * @return list of parameter key value.
     */
    private Map<String, String> getQueryParamKeysMap() {
        Map<String, String> queryParamKeysMap = new HashMap<>();

        queryParamKeysMap.put(AmazonS3Constants.DELIMITER, AmazonS3Constants.API_DELIMITER);
        queryParamKeysMap.put(AmazonS3Constants.ENCODING_TYPE, AmazonS3Constants.API_ENCODING_TYPE);
        queryParamKeysMap.put(AmazonS3Constants.KEY_MARKER, AmazonS3Constants.API_KEY_MARKER);
        queryParamKeysMap.put(AmazonS3Constants.MARKER, AmazonS3Constants.API_MARKER);
        queryParamKeysMap.put(AmazonS3Constants.MAX_KEYS, AmazonS3Constants.API_MAX_KEYS);
        queryParamKeysMap.put(AmazonS3Constants.PREFIX, AmazonS3Constants.API_PREFIX);
        queryParamKeysMap.put(AmazonS3Constants.VERSION_ID_MARKER, AmazonS3Constants.API_VERSION_ID_MARKER);
        queryParamKeysMap.put(AmazonS3Constants.QUERY_STRING, AmazonS3Constants.QUERY_STRING);
        queryParamKeysMap.put(AmazonS3Constants.VERSION_ID, AmazonS3Constants.API_VERSION_ID);
        queryParamKeysMap.put(AmazonS3Constants.UPLOAD_ID_MARKER, AmazonS3Constants.API_UPLOAD_ID_MARKER);
        queryParamKeysMap.put(AmazonS3Constants.MAX_UPLOADS, AmazonS3Constants.API_MAX_UPLOADS);
        queryParamKeysMap.put(AmazonS3Constants.PART_NUMBER, AmazonS3Constants.API_PART_NUMBER);
        queryParamKeysMap.put(AmazonS3Constants.UPLOAD_ID, AmazonS3Constants.API_UPLOAD_ID);
        queryParamKeysMap.put(AmazonS3Constants.RESPONSE_CONTENT_TYPE, AmazonS3Constants.API_RESPONSE_CONTENT_TYPE);
        queryParamKeysMap.put(AmazonS3Constants.RESPONSE_CONTENT_LANGUAGE, AmazonS3Constants.API_RESPONSE_CONTENT_LANGUAGE);
        queryParamKeysMap.put(AmazonS3Constants.RESPONSE_EXPIRES, AmazonS3Constants.API_RESPONSE_EXPIRES);
        queryParamKeysMap.put(AmazonS3Constants.RESPONSE_CACHE_CONTROL, AmazonS3Constants.API_RESPONSE_CACHE_CONTROL);
        queryParamKeysMap.put(AmazonS3Constants.RESPONSE_CONTENT_DISPOSITION, AmazonS3Constants.API_RESPONSE_CONTENT_DISPOSITION);
        queryParamKeysMap.put(AmazonS3Constants.RESPONSE_CONTENT_ENCODING, AmazonS3Constants.API_RESPONSE_CONTENT_ENCODING);
        queryParamKeysMap.put(AmazonS3Constants.PART_NUMBER_MARKER, AmazonS3Constants.API_PART_NUMBER_MARKER);
        queryParamKeysMap.put(AmazonS3Constants.MAX_PARTS, AmazonS3Constants.API_MAX_PARTS);

        return queryParamKeysMap;
    }

    /**
     * getHeaderKeysMap method used to return list of predefined XAMZ keys with values.
     *
     * @return list of Amz header keys and values Map.
     */
    private Map<String, String> getHeaderKeysMap() {

        Map<String, String> headerKeysMap = new HashMap<>();

        headerKeysMap.put(AmazonS3Constants.CONTENT_TYPE, AmazonS3Constants.API_CONTENT_TYPE);
        headerKeysMap.put(AmazonS3Constants.CONTENT_MD5, AmazonS3Constants.API_CONTENT_MD5);
        headerKeysMap.put(AmazonS3Constants.HOST, AmazonS3Constants.API_HOST);
        headerKeysMap.put(AmazonS3Constants.XAMZ_SECURITY_TOKEN, AmazonS3Constants.HD_XAMZ_SECURITY_TOKEN);
        headerKeysMap.put(AmazonS3Constants.XAMZ_ACL, AmazonS3Constants.HD_XAMZ_ACL);
        headerKeysMap.put(AmazonS3Constants.XAMZ_GRANT_READ, AmazonS3Constants.HD_XAMZ_GRANT_READ);
        headerKeysMap.put(AmazonS3Constants.XAMZ_GRANT_WRITE, AmazonS3Constants.HD_XAMZ_GRANT_WRITE);
        headerKeysMap.put(AmazonS3Constants.XAMZ_GRANT_READ_ACP, AmazonS3Constants.HD_XAMZ_GRANT_READ_ACP);
        headerKeysMap.put(AmazonS3Constants.XAMZ_GRANT_WRITE_ACP, AmazonS3Constants.HD_XAMZ_GRANT_WRITE_ACP);
        headerKeysMap.put(AmazonS3Constants.XAMZ_GRANT_FULL_CONTROL, AmazonS3Constants.HD_XAMZ_GRANT_FULL_CONTROL);
        headerKeysMap.put(AmazonS3Constants.XAMZ_META, AmazonS3Constants.HD_XAMZ_META);
        headerKeysMap.put(AmazonS3Constants.XAMZ_SERVE_ENCRYPTION, AmazonS3Constants.HD_XAMZ_SERVE_ENCRYPTION);
        headerKeysMap.put(AmazonS3Constants.XAMZ_STORAGE_CLASS, AmazonS3Constants.HD_XAMZ_STORAGE_CLASS);
        headerKeysMap.put(AmazonS3Constants.XAMZ_WEBSITE_LOCATION, AmazonS3Constants.HD_XAMZ_WEBSITE_LOCATION);
        headerKeysMap.put(AmazonS3Constants.XAMZ_MFA, AmazonS3Constants.HD_XAMZ_MFA);
        headerKeysMap.put(AmazonS3Constants.XAMZ_COPY_SOURCE, AmazonS3Constants.HD_XAMZ_COPY_SOURCE);
        headerKeysMap.put(AmazonS3Constants.XAMZ_COPY_SOURCE_RANGE, AmazonS3Constants.HD_XAMZ_COPY_SOURCE_RANGE);
        headerKeysMap.put(AmazonS3Constants.XAMZ_METADATA_DIRECTIVE, AmazonS3Constants.HD_XAMZ_METADATA_DIRECTIVE);
        headerKeysMap.put(AmazonS3Constants.XAMZ_COPY_SOURCE_IF_MATCH,
                AmazonS3Constants.HD_XAMZ_COPY_SOURCE_IF_MATCH);
        headerKeysMap.put(AmazonS3Constants.XAMZ_COPY_SOURCE_IF_NONE_MATCH,
                AmazonS3Constants.HD_XAMZ_COPY_SOURCE_IF_NONE_MATCH);
        headerKeysMap.put(AmazonS3Constants.XAMZ_COPY_SOURCE_IF_UNMODIFIED_SINCE,
                AmazonS3Constants.HD_XAMZ_COPY_SOURCE_IF_UNMODIFIED_SINCE);
        headerKeysMap.put(AmazonS3Constants.XAMZ_COPY_SOURCE_IF_MODIFIED_SINCE,
                AmazonS3Constants.HD_XAMZ_COPY_SOURCE_IF_MODIFIED_SINCE);
        headerKeysMap.put(AmazonS3Constants.XMAZ_SERVER_SIDE_ENCRYPTION_CUSTOMER_ALGORITHM,
                AmazonS3Constants.HD_XMAZ_SERVER_SIDE_ENCRYPTION_CUSTOMER_ALGORITHM);
        headerKeysMap.put(AmazonS3Constants.XMAZ_SERVER_SIDE_ENCRYPTION_CUSTOMER_KEY,
                AmazonS3Constants.HD_XMAZ_SERVER_SIDE_ENCRYPTION_CUSTOMER_KEY);
        headerKeysMap.put(AmazonS3Constants.XMAZ_SERVER_SIDE_ENCRYPTION_CUSTOMER_KEY_MD5,
                AmazonS3Constants.HD_XMAZ_SERVER_SIDE_ENCRYPTION_CUSTOMER_KEY_MD5);
        headerKeysMap.put(AmazonS3Constants.XAMZ_CONTENT_SHA256, AmazonS3Constants.HD_XAMZ_CONTENT_SHA256);

        return headerKeysMap;
    }

    /**
     * Add a Throwable to a message context, the message from the throwable is embedded as the Synapse.
     * Constant ERROR_MESSAGE.
     *
     * @param ctxt      message context to which the error tags need to be added
     * @param throwable Throwable that needs to be parsed and added
     * @param errorCode errorCode mapped to the exception
     */
    private void storeErrorResponseStatus(final MessageContext ctxt, final Throwable throwable, final int errorCode) {
        ctxt.setProperty(SynapseConstants.ERROR_CODE, errorCode);
        ctxt.setProperty(SynapseConstants.ERROR_MESSAGE, throwable.getMessage());
        ctxt.setFaultResponse(true);
    }

    /**
     * Add a message to message context, the message from the throwable is embedded as the Synapse Constant
     * ERROR_MESSAGE.
     *
     * @param ctxt      message context to which the error tags need to be added
     * @param message   message to be returned to the user
     * @param errorCode errorCode mapped to the exception
     */
    private void storeErrorResponseStatus(final MessageContext ctxt, final String message, final int errorCode) {
        ctxt.setProperty(SynapseConstants.ERROR_CODE, errorCode);
        ctxt.setProperty(SynapseConstants.ERROR_MESSAGE, message);
        ctxt.setFaultResponse(true);
    }

    /**
     * Hashes the string contents (assumed to be UTF-8) using the SHA-256 algorithm.
     *
     * @param messageContext of the connector
     * @param text           text to be hashed
     * @return SHA-256 hashed text
     */
    private byte[] hash(final MessageContext messageContext, final String text) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(AmazonS3Constants.SHA_256);
            messageDigest.update(text.getBytes(AmazonS3Constants.UTF_8));
        } catch (Exception exc) {
            storeErrorResponseStatus(messageContext, exc, AmazonS3Constants.ERROR_CODE_EXCEPTION);
            handleException(AmazonS3Constants.CONNECTOR_ERROR, exc, messageContext);
        }
        if (messageDigest == null) {
            log.error(AmazonS3Constants.CONNECTOR_ERROR);
            storeErrorResponseStatus(messageContext, AmazonS3Constants.CONNECTOR_ERROR,
                    AmazonS3Constants.ERROR_CODE_EXCEPTION);
            handleException(AmazonS3Constants.CONNECTOR_ERROR, messageContext);
        }
        if (messageDigest != null) {
            return messageDigest.digest();
        }
        return null;
    }

    /**
     * bytesToHex method HexEncoded the received byte array.
     *
     * @param bytes bytes to be hex encoded
     * @return hex encoded String of the given byte array
     */
    private static String bytesToHex(final byte[] bytes) {
        final char[] hexArray = AmazonS3Constants.HEX_ARRAY_STRING.toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            final int byteVal = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[byteVal >>> 4];
            hexChars[j * 2 + 1] = hexArray[byteVal & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Provides the HMAC SHA 256 encoded value(using the provided key) of the given data.
     *
     * @param key  to use for encoding
     * @param data to be encoded
     * @return HMAC SHA 256 encoded byte array
     * @throws NoSuchAlgorithmException     No such algorithm Exception
     * @throws InvalidKeyException          Invalid key Exception
     * @throws UnsupportedEncodingException Unsupported Encoding Exception
     * @throws IllegalStateException        Illegal State Exception
     */
    private static byte[] hmacSHA256(final byte[] key, final String data)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
        final String algorithm = AmazonS3Constants.HAMC_SHA_256;
        final Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes(AmazonS3Constants.UTF8));
    }

    /**
     * Returns the encoded signature key to be used for further encodings as per API doc.
     *
     * @param ctx         message context of the connector
     * @param key         key to be used for signing
     * @param dateStamp   current date stamp
     * @param regionName  region name given to the connector
     * @param serviceName Name of the service being addressed
     * @return Signature key
     * @throws UnsupportedEncodingException Unsupported Encoding Exception
     * @throws IllegalStateException        Illegal Argument Exception
     * @throws NoSuchAlgorithmException     No Such Algorithm Exception
     * @throws InvalidKeyException          Invalid Key Exception
     */
    private static byte[] getSignatureKey(final MessageContext ctx, final String key, final String dateStamp,
                                          final String regionName, final String serviceName)
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException {
        final byte[] kSecret = (AmazonS3Constants.AWS4 + key).getBytes(AmazonS3Constants.UTF8);
        final byte[] kDate = hmacSHA256(kSecret, dateStamp);
        final byte[] kRegion = hmacSHA256(kDate, regionName);
        final byte[] kService = hmacSHA256(kRegion, serviceName);
        return hmacSHA256(kService, ctx.getProperty(AmazonS3Constants.TERMINATION_STRING).toString());
    }

}
