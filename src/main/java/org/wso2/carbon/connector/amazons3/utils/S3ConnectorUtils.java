package org.wso2.carbon.connector.amazons3.utils;

import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.wso2.carbon.connector.amazons3.constants.S3Constants;
import org.wso2.carbon.connector.amazons3.exception.InvalidConfigurationException;
import org.wso2.carbon.connector.amazons3.pojo.S3OperationResult;
import software.amazon.awssdk.http.SdkHttpResponse;

public class S3ConnectorUtils {
    private static Log log = LogFactory.getLog(S3ConnectorUtils.class);

    /**
     * Sets the error code and error detail in message
     *
     * @param messageContext Message Context
     * @param error          Error to be set
     */
    public static void setErrorPropertiesToMessage(MessageContext messageContext, Error error) {

        messageContext.setProperty(S3Constants.PROPERTY_ERROR_CODE, error.getErrorCode());
        messageContext.setProperty(S3Constants.PROPERTY_ERROR_MESSAGE, error.getErrorDetail());
        Axis2MessageContext axis2smc = (Axis2MessageContext) messageContext;
        org.apache.axis2.context.MessageContext axis2MessageCtx = axis2smc.getAxis2MessageContext();
        axis2MessageCtx.setProperty(S3Constants.STATUS_CODE, error.getErrorCode());
    }

    /**
     * Retrieves connection name from message context if configured as configKey attribute
     * or from the template parameter
     *
     * @param messageContext Message Context from which the parameters should be extracted from
     * @return connection name
     */
    public static String getConnectionName(MessageContext messageContext) throws InvalidConfigurationException {

        String connectionName = (String) messageContext.getProperty(S3Constants.CONNECTION_NAME);
        if (connectionName == null) {
            throw new InvalidConfigurationException("Mandatory parameter 'connectionName' is not set.");
        }
        return connectionName;
    }

    public static JsonObject generateOperationResult(MessageContext msgContext, S3OperationResult result) {
        //Create a new JSON result object

        JsonObject resultJson = new JsonObject();
        resultJson.addProperty("success", result.isSuccessful());

        if(result.getWrittenBytes() != 0) {
            resultJson.addProperty("writtenBytes", result.getWrittenBytes());
        }

        if (result.getResultElement() != null) {
            resultJson.add("result", result.getResultElement());
        }

        if (result.getError() != null) {
            setErrorPropertiesToMessage(msgContext, result.getError());
            //set error code and detail to the message
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("code", result.getError().getErrorCode());
            errorJson.addProperty("message", result.getError().getErrorDetail());
            resultJson.add("error", errorJson);
            
            //set error detail
            if(StringUtils.isNotEmpty(result.getErrorMessage())) {
                resultJson.addProperty("detail", result.getErrorMessage());
            }
        }

        return resultJson;
    }

    public static S3OperationResult getSuccessResult(SdkHttpResponse sdkHttpResponse, String operationName) {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("statusCode", sdkHttpResponse.statusCode());
        String statusText = sdkHttpResponse.statusText().orElse("");
        responseJson.addProperty("statusText", statusText);
        responseJson.addProperty("response", sdkHttpResponse.statusCode() + ":" + statusText);
        
        return new S3OperationResult(
                operationName,
                true, responseJson);
    }

    public static S3OperationResult getFailureResult(String cause, String operationName, Error error) {
        return new S3OperationResult(
                operationName,
                false,
                error,
                "Operation failed with: " + cause);
    }

    /**
     * Creates a JSON response for operations that return complex response objects
     * @param operationName The name of the operation
     * @param responseData The response data object to be converted to JSON
     * @return S3OperationResult with JSON response
     */
    public static S3OperationResult createJsonOperationResult(String operationName, Object responseData) {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("operation", operationName);
        
        if (responseData != null) {
            responseJson.addProperty("data", responseData.toString());
        }
        
        return new S3OperationResult(operationName, true, responseJson);
    }

    /**
     * Creates a simple JSON response with key-value pairs
     * @param operationName The operation name
     * @param properties The key-value pairs to include in the response
     * @return S3OperationResult with JSON response
     */
    public static S3OperationResult createSimpleJsonResult(String operationName, String... properties) {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("operation", operationName);
        
        for (int i = 0; i < properties.length; i += 2) {
            if (i + 1 < properties.length) {
                responseJson.addProperty(properties[i], properties[i + 1]);
            }
        }
        
        return new S3OperationResult(operationName, true, responseJson);
    }
}
