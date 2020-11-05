package org.wso2.carbon.connector.amazons3.utils;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.soap.SOAPBody;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.transport.passthru.PassThroughConstants;
import org.wso2.carbon.connector.amazons3.constants.S3Constants;
import org.wso2.carbon.connector.amazons3.exception.InvalidConfigurationException;
import org.wso2.carbon.connector.amazons3.pojo.S3OperationResult;
import software.amazon.awssdk.http.SdkHttpResponse;

import javax.xml.stream.XMLStreamException;

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

    public static void setResultAsPayload(MessageContext msgContext, S3OperationResult result) {

        OMElement resultElement = generateOperationResult(msgContext,result);
        if(result.getResultEle() != null) {
            resultElement.addChild(result.getResultEle());
        }
        SOAPBody soapBody = msgContext.getEnvelope().getBody();
        JsonUtil.removeJsonPayload(((Axis2MessageContext)msgContext).getAxis2MessageContext());
        ((Axis2MessageContext)msgContext).getAxis2MessageContext().
                removeProperty(PassThroughConstants.NO_ENTITY_BODY);
        soapBody.addChild(resultElement);
    }

    private static OMElement generateOperationResult(MessageContext msgContext, S3OperationResult result) {
        //Create a new payload body and add to context

        String resultElementName = result.getOperation() + "Result";
        OMElement resultElement = createOMElement(resultElementName, null);

        OMElement statusCodeElement = createOMElement("success",
                String.valueOf(result.isSuccessful()));
        resultElement.addChild(statusCodeElement);

        if(result.getWrittenBytes() != 0) {
            OMElement writtenBytesEle = createOMElement("writtenBytes",
                    String.valueOf(result.getWrittenBytes()));
            resultElement.addChild(writtenBytesEle);
        }

        if(result.getError() != null) {
            setErrorPropertiesToMessage(msgContext, result.getError());
            //set error code and detail to the message
            OMElement errorEle = createOMElement("error", result.getError().getErrorCode());
            OMElement errorCodeEle = createOMElement("code", result.getError().getErrorCode());
            OMElement errorMessageEle = createOMElement("message", result.getError().getErrorDetail());
            errorEle.addChild(errorCodeEle);
            errorEle.addChild(errorMessageEle);
            resultElement.addChild(errorCodeEle);
            //set error detail
            if(StringUtils.isNotEmpty(result.getErrorMessage())) {
                OMElement errorDetailEle = createOMElement("detail", result.getErrorMessage());
                resultElement.addChild(errorDetailEle);
            }
        }

        return resultElement;
    }

    public static OMElement createOMElement(String elementName, Object value) {
        OMElement resultElement = null;
        try {
            if (value != null) {
                resultElement = AXIOMUtil.
                        stringToOM("<" + elementName + ">" + value
                                + "</" + elementName + ">");
            } else {
                resultElement = AXIOMUtil.
                        stringToOM("<" + elementName
                                + "></" + elementName + ">");
            }
        } catch (XMLStreamException | OMException e) {
            log.error("Error while generating OMElement from element name" + elementName, e);
        }
        return resultElement;
    }

    public static S3OperationResult getSuccessResult(SdkHttpResponse sdkHttpResponse, String operationName) {
        OMElement responseElement = S3ConnectorUtils.
                createOMElement("Response", Integer.toString(sdkHttpResponse.statusCode())
                        + ":" + sdkHttpResponse.statusText());
        return new S3OperationResult(
                operationName,
                true, responseElement);
    }

    public static S3OperationResult getFailureResult(String cause, String operationName, Error error) {
        return new S3OperationResult(
                operationName,
                false,
                error,
                "Operation failed with: " + cause);
    }
}
