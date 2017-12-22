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
package org.wso2.carbon.connector.integration.test.amazons3;

import org.apache.axiom.om.OMElement;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AmazonS3ConnectorIntegrationTest extends ConnectorIntegrationTestBase {

    private Map<String, String> esbRequestHeadersMap = new HashMap<>();
    private String multipartProxyUrl;

    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {
        String connectorName = System.getProperty("connector_name") + "-connector-" +
                System.getProperty("connector_version") + ".zip";
        init(connectorName);

        multipartProxyUrl = getProxyServiceURLHttp("multipart");

        String bucketName1 = System.currentTimeMillis() + connectorProperties.getProperty("bucketName_1");
        String bucketName2 = System.currentTimeMillis() + connectorProperties.getProperty("bucketName_2");
        String bucketName3 = System.currentTimeMillis() + connectorProperties.getProperty("bucketName_3");
        connectorProperties.setProperty("bucketName1", bucketName1);
        connectorProperties.setProperty("bucketName2", bucketName2);
        connectorProperties.setProperty("bucketName3", bucketName3);
        connectorProperties.setProperty("bucketName4", bucketName3 + "1");

        connectorProperties.setProperty("bucketUrl1",
                connectorProperties.getProperty("bucketUrl1").replace("<bucketName_1>", bucketName1));
        connectorProperties.setProperty("bucketUrl2",
                connectorProperties.getProperty("bucketUrl2").replace("<bucketName_2>", bucketName2));
        connectorProperties.setProperty("bucketUrl3",
                connectorProperties.getProperty("bucketUrl3").replace("<bucketName_1>", bucketName1));
        connectorProperties.setProperty("bucketUrl4",
                connectorProperties.getProperty("bucketUrl4").replace("<bucketName_2>", bucketName2));
        connectorProperties.setProperty("bucketUrl5",
                connectorProperties.getProperty("bucketUrl5").replace("<bucketName_2>", bucketName2));
        connectorProperties.setProperty("bucketUrl6",
                connectorProperties.getProperty("bucketUrl6").replace("<bucketName_2>", bucketName2));
        connectorProperties.setProperty("bucketUrl7",
                connectorProperties.getProperty("bucketUrl7").replace("<bucketName_3>", bucketName3));
        connectorProperties.setProperty("bucketUrl8",
                connectorProperties.getProperty("bucketUrl7").replace("us-west-2", "us-west-1") + "1");
        connectorProperties.setProperty("host5",
                connectorProperties.getProperty("host5").replace("<bucketName_2>", bucketName2));
        connectorProperties.setProperty("host6",
                connectorProperties.getProperty("host6").replace("<bucketName_2>", bucketName2));
        connectorProperties.setProperty("host7",
                connectorProperties.getProperty("host7").replace("<bucketName_3>", bucketName3));
        connectorProperties.setProperty("host8",
                connectorProperties.getProperty("host7").replace("us-west-2", "us-west-1"));

    }

    @Test(groups = {"wso2.esb"}, description = "AmazonS3 {createBucket} integration test with mandatory parameters.")
    public void testCreateBucketForWebSiteConfig() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucket");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketForWebSiteConfig_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for createBucket method for bucket replication.
     */
    @Test(groups = {"wso2.esb"}, description = "AmazonS3 {createBucket} for bucket replication.")
    public void testCreateBucketForBucketReplication() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucket");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketForBucketReplication_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for createBucket method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"},
            description = "AmazonS3 {createBucket} integration test with mandatory parameters.")
    public void testCreateBucketWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucket");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucket_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for createBucket method with optional parameters.
     */
    @Test(groups = {"wso2.esb"}, description = "AmazonS3 {createBucket} integration test with optional parameters.")
    public void testCreateBucketWithOptionalParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucket");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucket_optional.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for createBucket method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters"},
            description = "AmazonS3 {createBucket} integration test with negative case.")
    public void testCreateBucketWithNegativeCase() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucket");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucket_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }


    /**
     * Positive test case for createBucketPolicy method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketPolicy} integration test with mandatory parameters.")
    public void testCreateBucketPolicyWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucketPolicy");
        esbRequestHeadersMap.put("Content-Type", "application/json");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketPolicy_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for createBucketPolicy method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketPolicy} integration test with negative case.")
    public void testCreateBucketPolicyWithNegativeCase() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucketPolicy");
        esbRequestHeadersMap.put("Content-Type", "application/json");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketPolicy_negative.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
    }

    /**
     * Positive test case for createBucketCors method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketCors} integration test with mandatory parameters.")
    public void testCreateBucketCorsWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucketCors");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketCors_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for createBucketCors method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketCors} integration test with negative case.")
    public void testCreateBucketCorsWithNegativeCase() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucketCors");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketCors_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for createBucketWebsiteConfiguration method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketForWebSiteConfig"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketWebsiteConfiguration} integration test with mandatory parameters.")
    public void testCreateBucketWebsiteConfigurationWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucketWebsiteConfiguration");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketWebsiteConfiguration_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for createBucketWebsiteConfiguration method.
     */
    @Test(dependsOnMethods = {"testCreateBucketForWebSiteConfig"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketWebsiteConfiguration} integration test with negative case.")
    public void testCreateBucketWebsiteConfigurationWithNegativeCase() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucketWebsiteConfiguration");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketWebsiteConfiguration_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for setBucketACL method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters",
            "testGetBucketACLWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {setBucketACL} integration test with mandatory parameters.")
    public void testSetBucketACLWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:setBucketACL");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        Thread.sleep(Long.parseLong(connectorProperties.getProperty("timeOut")));
        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "setBucketACL_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for setBucketACL method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {setBucketACL} integration test with negative case.")
    public void testSetBucketACLWithNegativeCase() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:setBucketACL");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "setBucketACL_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBuckets method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBuckets} integration test with mandatory parameters.")
    public void testGetBucketsWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBuckets");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBuckets_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBuckets method.
     */
    @Test(groups = {"wso2.esb"}, description = "AmazonS3 {getBuckets} integration test with negative case.")
    public void testGetBucketsWithNegativeCase() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getBuckets");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBuckets_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBucketVersioning method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketVersioning} integration test with mandatory parameters.")
    public void testGetBucketVersioningWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getBucketVersioning");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketVersioning_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketVersioning method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketVersioning} integration test with negative case.")
    public void testGetBucketVersioningWithNegativeCase() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getBucketVersioning");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketVersioning_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBucketRequestPayment method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketRequestPayment} integration test with mandatory parameters.")
    public void testGetBucketRequestPaymentWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getBucketRequestPayment");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketRequestPayment_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketRequestPayment method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketRequestPayment} integration test with negative case.")
    public void testGetBucketRequestPaymentWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketRequestPayment");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketRequestPayment_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBucketACL method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketACL} integration test with mandatory parameters.")
    public void testGetBucketACLWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getBucketACL");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketACL_mandatory.xml");
        String response = esbRestResponse.getBody().toString();
        String ownerID = response.substring(response.lastIndexOf("<ID>") + "<ID>".length(),
                response.lastIndexOf("</ID>"));
        String ownerDisplayName = response.substring(response.lastIndexOf("<DisplayName>") + "<DisplayName>".length(),
                response.lastIndexOf("</DisplayName>"));

        connectorProperties.setProperty("ownerId", ownerID);
        connectorProperties.setProperty("ownerdisplayName", ownerDisplayName);
        Assert.assertNotNull(ownerID);
        Assert.assertNotNull(ownerDisplayName);
    }

    /**
     * Negative test case for getBucketACL method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketACL} integration test with negative case.")
    public void testGetBucketACLWithNegativeCase() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getBucketACL");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketACL_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBucketCors method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketCorsWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketCors} integration test with mandatory parameters.")
    public void testGetBucketCorsWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getBucketCors");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketCors_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketCors method.
     */
    @Test(dependsOnMethods = {"testCreateBucketCorsWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketCors} integration test with negative case.")
    public void testGetBucketCorsWithNegativeCase() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:getBucketCors");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketCors_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBucketLifeCycle method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters",
            "testCreateBucketLifecycleWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketLifeCycle} integration test with mandatory parameters.")
    public void testGetBucketLifeCycleWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketLifeCycle");
        esbRequestHeadersMap.put("Content-Type", "application/xml");
        Thread.sleep(Long.parseLong(connectorProperties.getProperty("timeOut")));

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketLifeCycle_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketLifeCycle method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketLifeCycle} integration test with negative case.")
    public void testGetBucketLifeCycleWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketLifeCycle");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketLifeCycle_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBucketLocation method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketLocation} integration test with mandatory parameters.")
    public void testGetBucketLocationWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketLocation");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketLocation_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketLocation method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketLocation} integration test with negative case.")
    public void testGetBucketLocationWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketLocation");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketLocation_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBucketLogging method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketLogging} integration test with mandatory parameters.")
    public void testGetBucketLoggingWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketLogging");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketLogging_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketLogging method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketLogging} integration test with negative case.")
    public void testGetBucketLoggingWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketLogging");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketLogging_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBucketNotification method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketNotification} integration test with mandatory parameters.")
    public void testGetBucketNotificationWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketNotification");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketNotification_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketNotification method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketNotification} integration test with negative case.")
    public void testGetBucketNotificationWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketNotification");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketNotification_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for createBucketVersioning method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketVersioning} integration test with mandatory parameters.")
    public void testCreateBucketVersioningWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:createBucketVersioning");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketVersioning_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for createBucketVersioning method with optional parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketVersioning} integration test with optional parameters.")
    public void testCreateBucketVersioningWithOptionalParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:createBucketVersioning");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketVersioning_optional.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for createBucketVersioning for bucket replication.
     */
    @Test(dependsOnMethods = {"testCreateBucketForBucketReplication"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketVersioning} for bucket replication.")
    public void testCreateBucketVersioningForBucketReplication() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucketVersioning");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketVersioningForReplication_optional.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for createBucketReplication method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketVersioningWithOptionalParameters",
            "testCreateBucketVersioningForBucketReplication"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketReplication} integration test with mandatory parameters.")
    public void testCreateBucketReplicationWithMandatoryParameters() throws Exception {
        esbRequestHeadersMap.put("Action", "urn:createBucketReplication");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketReplication_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for getBucketReplication method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketReplicationWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketReplication} integration test with mandatory parameters.")
    public void testGetBucketReplicationWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketReplication");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketReplication_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketReplication method.
     */
    @Test(dependsOnMethods = {"testCreateBucketReplicationWithMandatoryParameters"},
            groups = {"wso2.esb"}, description = "AmazonS3 {getBucketReplication} integration test with negative case.")
    public void testGetBucketReplicationWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketReplication");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketReplication_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for createBucketTagging method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createBucketTagging} integration test with mandatory parameters.")
    public void testCreateBucketTaggingWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:createBucketTagging");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketTagging_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Positive test case for getBucketTagging method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketTaggingWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketTagging} integration test with mandatory parameters.")
    public void testGetBucketTaggingWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketTagging");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketTagging_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketTagging method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithOptionalParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketTagging} integration test with negative case.")
    public void testGetBucketTaggingWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketTagging");

        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketTagging_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getWebSiteConfiguration method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketWebsiteConfigurationWithMandatoryParameters"},
            groups = {"wso2.esb"}, description = "AmazonS3 {getWebSiteConfiguration} integration test with mandatory parameters.")
    public void testGetWebSiteConfigurationWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getWebSiteConfiguration");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getWebSiteConfiguration_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getWebSiteConfiguration method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWebsiteConfigurationWithMandatoryParameters"},
            groups = {"wso2.esb"}, description = "AmazonS3 {getWebSiteConfiguration} integration test with negative case.")
    public void testGetWebSiteConfigurationWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getWebSiteConfiguration");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getWebSiteConfiguration_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBucketPolicy method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketPolicyWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketPolicy} integration test with mandatory parameters.")
    public void testGetBucketPolicyWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketPolicy");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketPolicy_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketPolicy method.
     */
    @Test(dependsOnMethods = {"testCreateBucketPolicyWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketPolicy} integration test with negative case.")
    public void testGetBucketPolicyWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketPolicy");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketPolicy_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for createObjectACL method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateObjectCopyMandatoryParameters",
            "testGetBucketACLWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {createObjectACL} integration test with mandatory parameters.")
    public void testCreateObjectACLWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:createObjectACL");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createObjectACL_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for createObjectACL method.
     */
    @Test(groups = {"wso2.esb"},
            description = "AmazonS3 {createObjectACL} integration test with negative case.")
    public void testCreateObjectACLWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:createObjectACL");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createObjectACL_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
    }

    /**
     * Positive test case for createObjectCopy method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters"},
            description = "AmazonS3 {createObjectCopy} integration test with mandatory parameters.")
    public void testCreateObjectCopyMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:createObjectCopy");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createObjectCopy_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for createObjectCopy method.
     */
    @Test(groups = {"wso2.esb"},
            description = "AmazonS3 {createObjectCopy} integration test with negative case.")
    public void testCreateObjectCopyWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:createObjectCopy");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createObjectCopy_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getObjectsInBucket method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters"},
            description = "AmazonS3 {getObjectsInBucket} integration test with mandatory parameters.")
    public void testGetObjectsInBucketWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getObjectsInBucket");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getObjectsInBucket_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getObjectsInBucket method.
     */
    @Test(groups = {"wso2.esb"},
            description = "AmazonS3 {getObjectsInBucket} integration test with negative case.")
    public void testGetObjectsInBucketWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getObjectsInBucket");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getObjectsInBucket_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getBucketObjectVersions method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters",
            "testCreateBucketReplicationWithMandatoryParameters", "testCreateObjectCopyMandatoryParameters"},
            description = "AmazonS3 {getBucketObjectVersions} integration test with mandatory parameters.")
    public void testGetBucketObjectVersionsWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketObjectVersions");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketObjectVersions_mandatory.xml");
        String response = esbRestResponse.getBody().toString();

        String versionId = response.substring(response.lastIndexOf("<VersionId>") + "<VersionId>".length(),
                response.lastIndexOf("</VersionId>"));
        connectorProperties.setProperty("versionId", versionId);
        String objectForRestore = response.substring(response.lastIndexOf("<Key>") + "</Key>".length() - 1,
                response.lastIndexOf("</Key>"));
        connectorProperties.setProperty("versionId", versionId);
        connectorProperties.setProperty("objectForRestore", objectForRestore);
        Assert.assertNotNull(versionId);
    }

    /**
     * Positive test case for getBucketObjectVersions method with optional parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters"},
            description = "AmazonS3 {getBucketObjectVersions} integration test with optional parameters.")
    public void testGetBucketObjectVersionsWithOptionalParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketObjectVersions");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketObjectVersions_optional.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getBucketObjectVersions method.
     */
    @Test(groups = {"wso2.esb"},
            description = "AmazonS3 {getBucketObjectVersions} integration test with negative case.")
    public void testGetBucketObjectVersionsWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getBucketObjectVersions");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getBucketObjectVersions_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for getObject method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateObjectCopyMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getObject} integration test with mandatory parameters.")
    public void testGetObjectWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getObject");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getObject_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for getObject method with optional parameters.
     */
    @Test(dependsOnMethods = {"testCreateObjectCopyMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {getObject} integration test with optional parameters.")
    public void testGetObjectWithOptionalParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getObject");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getObject_optional.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getObject method.
     */
    @Test(groups = {"wso2.esb"}, description = "AmazonS3 {getObject} integration test with negative case.")
    public void testGetObjectWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getObject");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getObject_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
    }

    /**
     * Positive test case for getObjectMetaData method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters",
            "testGetObjectWithMandatoryParameters"},
            description = "AmazonS3 {getObjectMetadata} integration test with mandatory parameters.")
    public void testGetObjectMetaDataWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getObjectMetaData");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getObjectMetaData_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for getObjectMetaData method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters"},
            description = "AmazonS3 {getObjectMetaData} integration test with negative case.")
    public void testGetObjectMetaDataWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getObjectMetaData");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getObjectMetaData_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for checkBucketPermission method with mandatory parameters.
     */
    @Test(dependsOnMethods = {"testCreateBucketForWebSiteConfig"}, groups = {"wso2.esb"},
            description = "AmazonS3 {checkBucketPermission} integration test with mandatory parameters.")
    public void testCheckBucketPermissionWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:checkBucketPermission");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "checkBucketPermission_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for checkBucketPermission method.
     */
    @Test(dependsOnMethods = {"testCreateBucketForWebSiteConfig"}, groups = {"wso2.esb"},
            description = "AmazonS3 {checkBucketPermission} integration test with negative case.")
    public void testCheckBucketPermissionWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:checkBucketPermission");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "checkBucketPermission_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 400);
    }

    /**
     * Mandatory parameter test case for createBucketLifecycle method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters"},
            description = "AmazonS3 {createBucketLifecycle} integration test with mandatory parameter.")
    public void testCreateBucketLifecycleWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:createBucketLifecycle");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketLifecycle_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Mandatory parameter test case for getObjectACL method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateObjectACLWithMandatoryParameters",
            "testCompleteMultipartUplaodWithMandatoryParameters"},
            description = "AmazonS3 {getObjectACL} integration test with mandatory parameter.")
    public void testGetObjectACLWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getObjectACL");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getObjectACL_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for initMultipartUpload method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters"},
            description = "AmazonS3 {initMultipartUpload} integration test with mandatory parameters.")
    public void testInitMultipartUploadWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:initMultipartUpload");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "initMultipartUpload_mandatory.xml");
        String response = esbRestResponse.getBody().toString();
        String uploadIdForCopyPart = response.substring(response.lastIndexOf("<UploadId>") + "<UploadId>".length(),
                response.lastIndexOf("</UploadId>"));
        connectorProperties.setProperty("uploadIdForCopyPart", uploadIdForCopyPart);
        Assert.assertNotNull(uploadIdForCopyPart);
    }

    /**
     * Positive test case for initMultipartUpload method with optional parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters"},
            description = "AmazonS3 {initMultipartUpload} integration test with optional parameters.")
    public void testInitMultipartUploadWithOptionalParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:initMultipartUpload");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "initMultipartUpload_optional.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for initMultipartUpload method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithMandatoryParameters"},
            description = "AmazonS3 {initMultipartUpload} integration test with negative case.")
    public void testInitMultipartUploadWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:initMultipartUpload");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "initMultipartUpload_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for abortMultipartUpload method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListMultipartUploadsWithOptionalParameters",
            "testUploadPartCopyWithMandatoryParameters"},
            description = "AmazonS3 {abortMultipartUpload} integration test with mandatory parameters.")
    public void testAbortMultipartUplaodWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:abortMultipartUpload");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "abortMultipartUpload_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Positive test case for abortMultipartUpload method with optional parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListMultipartUploadsWithOptionalParameters",
            "testUploadPartCopyWithMandatoryParameters"},
            description = "AmazonS3 {abortMultipartUpload} integration test with optional parameters.")
    public void testAbortMultipartUplaodWithOptionalParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:abortMultipartUpload");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "abortMultipartUpload_optional.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for abortMultipartUpload method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListMultipartUploadsWithOptionalParameters"},
            description = "AmazonS3 {abortMultipartUpload} integration test with negative case.")
    public void testAbortMultipartUplaodWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:abortMultipartUpload");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "abortMultipartUpload_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for uploadPart method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListMultipartUploadsWithMandatoryParameters"},
            description = "AmazonS3 {uploadPart} integration test with mandatory parameters.")
    public void testUploadPartWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:uploadPart");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "uploadPart_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertNotNull(esbRestResponse.getHeadersMap().get("ETag"));

        String uploadPartETag = esbRestResponse.getHeadersMap().get("ETag").get(0);
        connectorProperties.setProperty("uploadPartETag", uploadPartETag);
    }

    /**
     * Mandatory parameter test case for restoreObject method.
     */
    @Test(enabled = false, groups = {"wso2.esb"}, dependsOnMethods = {"testGetBucketObjectVersionsWithMandatoryParameters"},
            description = "AmazonS3 {restoreObject} integration test with mandatory parameter.")
    public void testRestoreObjectWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:restoreObject");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "restoreObject_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Mandatory parameter test case for headObject method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetBucketObjectVersionsWithMandatoryParameters",
            "testCompleteMultipartUplaodWithMandatoryParameters"},
            description = "AmazonS3 {headObject} integration test with mandatory parameter.")
    public void testHeadObjectWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:headObject");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "headObject_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Optional parameter test case for headObject method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCompleteMultipartUplaodWithMandatoryParameters"},
            description = "AmazonS3 {headObject} integration test with optional parameter.")
    public void testHeadObjectWithOptionalParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:headObject");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "headObject_optional.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Mandatory parameter test case for createBucketACL method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters",
            "testGetBucketACLWithMandatoryParameters"},
            description = "AmazonS3 {createBucketACL} integration test with mandatory parameter.")
    public void testCreateBucketACLWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:createBucketACL");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketACL_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Mandatory parameter test case for createBucketRequestPayment method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters"},
            description = "AmazonS3 {createBucketRequestPayment} integration test with mandatory parameter.")
    public void testCreateBucketRequestPaymentWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:createBucketRequestPayment");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createBucketRequestPayment_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Mandatory parameter test case for headBucket method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters"},
            description = "AmazonS3 {headBucket} integration test with mandatory parameter.")
    public void testHeadBucketWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:headBucket");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "headBucket_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Mandatory parameter test case for getObjectTorrent method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testGetBucketObjectVersionsWithMandatoryParameters",
            "testCompleteMultipartUplaodWithMandatoryParameters"},
            description = "AmazonS3 {getObjectTorrent} integration test with mandatory parameter.")
    public void testGetObjectTorrentWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:getObjectTorrent");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "getObjectTorrent_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Mandatory parameter test case for deleteObject method.
     */
    @Test(dependsOnMethods = {"testCreateObjectCopyMandatoryParameters",
            "testGetObjectWithMandatoryParameters", "testGetObjectMetaDataWithMandatoryParameters",
            "testHeadObjectWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {deleteObject} integration test with mandatory parameter.")
    public void testDeleteObjectWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteObject");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteObject_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for deleteObject method.
     */
    @Test(dependsOnMethods = {"testCreateObjectCopyMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {deleteObject} integration test negative case.")
    public void testDeleteObjectWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteObject");
        esbRequestHeadersMap.put("Content-Type", "application/xml");
        Thread.sleep(Long.parseLong(connectorProperties.getProperty("timeOut")));

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteObject_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Mandatory parameter test case for deleteMultipleObjects method.
     */
    @Test(groups = {"wso2.esb"}, description = "AmazonS3 {deleteMultipleObjects} integration test with mandatory parameter.")
    public void testDeleteMultipleObjectsWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteMultipleObjects");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteMultipleObjects_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for deleteMultipleObjects method.
     */
    @Test(groups = {"wso2.esb"}, description = "AmazonS3 {deleteMultipleObjects} integration test negative case.")
    public void testDeleteMultipleObjectsWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteMultipleObjects");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteMultipleObjects_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Mandatory parameter test case for deleteBucketWebsite method.
     */
    @Test(dependsOnMethods = {"testGetWebSiteConfigurationWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {deleteBucketWebsite} integration test with mandatory parameter.")
    public void testDeleteBucketWebsiteWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteBucketWebsiteConfiguration");
        esbRequestHeadersMap.put("Content-Type", "application/xml");
        Thread.sleep(Long.parseLong(connectorProperties.getProperty("timeOut")));

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteBucketWebsite_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for deleteBucketWebsite method.
     */
    @Test(dependsOnMethods = {"testGetWebSiteConfigurationWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {deleteBucketWebsite} integration test negative case.")
    public void testDeleteBucketWebsiteWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteBucketWebsiteConfiguration");
        esbRequestHeadersMap.put("Content-Type", "application/xml");
        Thread.sleep(Long.parseLong(connectorProperties.getProperty("timeOut")));

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteBucketWebsite_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Mandatory Parameter test case for deleteBucketPolicy method.
     */
    @Test(dependsOnMethods = {"testGetBucketPolicyWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {deleteBucketPolicy} integration test with mandatory parameters.")
    public void testDeleteBucketPolicyWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteBucketPolicy");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteBucketPolicy_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for deleteBucketPolicy method.
     */
    @Test(dependsOnMethods = {"testDeleteMultipleObjectsWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {deleteBucketPolicy} integration test with negative case.")
    public void testDeleteBucketPolicyWitNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteBucketPolicy");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteBucketPolicy_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Mandatory parameter test case for deleteBucketCors method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters",
            "testGetBucketCorsWithMandatoryParameters"},
            description = "AmazonS3 {deleteBucketCors} integration test with mandatory parameter.")
    public void testDeleteBucketCorsWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteBucketCors");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteBucketCors_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Mandatory parameter test case for deleteBucketLifecycle method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters",
            "testGetBucketLifeCycleWithMandatoryParameters"},
            description = "AmazonS3 {deleteBucketLifecycle} integration test with mandatory parameter.")
    public void testDeleteBucketLifecycleWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteBucketLifecycle");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteBucketLifecycle_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Mandatory parameter test case for deleteBucketReplication method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters",
            "testGetBucketReplicationWithMandatoryParameters"},
            description = "AmazonS3 {deleteBucketReplication} integration test with mandatory parameter.")
    public void testDeleteBucketReplicationWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteBucketReplication");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteBucketReplication_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Mandatory parameter test case for deleteBucketTagging method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testCreateBucketWithOptionalParameters",
            "testGetBucketTaggingWithMandatoryParameters"},
            description = "AmazonS3 {deleteBucketTagging} integration test with mandatory parameter.")
    public void testDeleteBucketTaggingWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteBucketTagging");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteBucketTagging_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Mandatory Parameter test case for deleteBucket method.
     */
    @Test(dependsOnMethods = {"testCreateBucketWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {deleteBucket} integration test with mandatory parameters.")
    public void testDeleteBucketWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteBucket");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteBucket_mandatory.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for deleteBucket method.
     */
    @Test(dependsOnMethods = {"testGetBucketsWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {deleteBucket} integration test with negative case.")
    public void testDeleteBucketWitNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:deleteBucket");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteBucket_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for listMultipartUploads method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testInitMultipartUploadWithMandatoryParameters"},
            description = "AmazonS3 {listMultipartUploads} integration test with mandatory parameters.")
    public void testListMultipartUploadsWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:listMultipartUploads");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "listMultipartUploads_mandatory.xml");
        String response = esbRestResponse.getBody().toString();
        String uploadIdMandatory = response.substring(response.lastIndexOf("<UploadId>") + "<UploadId>".length(),
                response.lastIndexOf("</UploadId>"));
        connectorProperties.setProperty("uploadIdMandatory", uploadIdMandatory);
        Assert.assertTrue(response.contains("ListMultipartUploadsResult"));
        Assert.assertTrue(response.contains("<Upload>"));
    }

    /**
     * Positive test case for listMultipartUploads method with optional parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testInitMultipartUploadWithMandatoryParameters"},
            description = "AmazonS3 {listMultipartUploads} integration test with optional parameters.")
    public void testListMultipartUploadsWithOptionalParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:listMultipartUploads");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "listMultipartUploads_optional.xml");

        String response = esbRestResponse.getBody().toString();

        String uploadIdOptional = response.substring(response.indexOf("<UploadId>") + "<UploadId>".length(),
                response.indexOf("</UploadId>"));
        connectorProperties.setProperty("uploadIdOptional", uploadIdOptional);
        Assert.assertTrue(response.contains("ListMultipartUploadsResult"));
        Assert.assertTrue(response.contains("<MaxUploads>1000</MaxUploads>"));
    }

    /**
     * Negative test case for listMultipartUploads method with optional parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListMultipartUploadsWithMandatoryParameters"},
            description = "AmazonS3 {listMultipartUploads} integration test negative case with optional parameters.")
    public void testListMultipartUploadsWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:listMultipartUploads");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "listMultipartUploads_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }

    /**
     * Positive test case for completeMultipartUpload method with mandatory parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testUploadPartWithMandatoryParameters"},
            description = "AmazonS3 {completeMultipartUpload} integration test with mandatory parameters.")
    public void testCompleteMultipartUplaodWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:completeMultipartUpload");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "completeMultipartUpload_mandatory.xml");

        final String response = esbRestResponse.getBody().toString();
        Assert.assertTrue(response.contains("ETag"));
        Assert.assertTrue(response.contains(connectorProperties.getProperty("objectName6")));
    }

    /**
     * Positive test case for completeMultipartUpload method with optional parameters.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListMultipartUploadsWithMandatoryParameters",
            "testUploadPartWithMandatoryParameters"},
            description = "AmazonS3 {completeMultipartUpload} integration test with optional parameters.")
    public void testCompleteMultipartUplaodWithOptionalParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:completeMultipartUpload");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "completeMultipartUpload_optional.xml");

        final String response = esbRestResponse.getBody().toString();

        Assert.assertTrue(response.contains("ETag"));
        Assert.assertTrue(response.contains(connectorProperties.getProperty("objectName6")));
    }

    /**
     * Negative test case for completeMultipartUpload method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testListMultipartUploadsWithMandatoryParameters"},
            description = "AmazonS3 {completeMultipartUpload} integration test with negative case.")
    public void testCompleteMultipartUplaodWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:completeMultipartUpload");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "completeMultipartUpload_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }


    /**
     * Mandatory parameter test case for uploadPartCopy method.
     */
    @Test(groups = {"wso2.esb"}, dependsOnMethods = {"testInitMultipartUploadWithMandatoryParameters"},
            description = "AmazonS3 {uploadPartCopy} integration test with mandatory parameter.")
    public void testUploadPartCopyWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:uploadPartCopy");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "uploadPartCopy_mandatory.xml");
        String response =esbRestResponse.getBody().toString();
        String eTag = response.substring(response.lastIndexOf("<ETag>") + "<ETag>".length(),
                response.lastIndexOf("</ETag>"));
        Assert.assertNotNull(eTag);
    }

    /**
     * Mandatory parameter test case for ListParts method.
     */
    @Test(dependsOnMethods = {"testListMultipartUploadsWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {listParts} integration test with mandatory parameter.")
    public void testListPartsWithMandatoryParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:listParts");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "listParts_mandatory.xml");

        String response = esbRestResponse.getBody().toString();

        Assert.assertTrue(response.contains("ListPartsResult"));
        Assert.assertTrue(response.contains(connectorProperties.getProperty("bucketName2")));
    }

    /**
     * Positive test case for ListParts method with optional parameters.
     */
    @Test(dependsOnMethods = {"testListMultipartUploadsWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {listParts} integration test with optional parameters.")
    public void testListPartsWithOptionalParameters() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:listParts");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "listParts_optional.xml");

        String response = esbRestResponse.getBody().toString();
        Assert.assertTrue(response.contains(connectorProperties.getProperty("encodingType")));
        Assert.assertTrue(response.contains(connectorProperties.getProperty("maxParts")));
    }

    /**
     * Negative test case for listPart method.
     *
     * @throws Exception
     */
    @Test(dependsOnMethods = {"testInitMultipartUploadWithMandatoryParameters"}, groups = {"wso2.esb"},
            description = "AmazonS3 {listParts} integration test with negative case.")
    public void testListPartsWithNegativeCase() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:listParts");
        esbRequestHeadersMap.put("Content-Type", "application/xml");

        RestResponse<OMElement> esbRestResponse =
                sendXmlRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "listParts_negative.xml");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 403);
    }
}
