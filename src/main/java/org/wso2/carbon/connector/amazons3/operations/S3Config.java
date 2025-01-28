package org.wso2.carbon.connector.amazons3.operations;

import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.wso2.carbon.connector.amazons3.connection.S3ConnectionHandler;
import org.wso2.carbon.connector.amazons3.constants.S3Constants;
import org.wso2.carbon.connector.amazons3.exception.InvalidConfigurationException;
import org.wso2.carbon.connector.amazons3.pojo.ConnectionConfiguration;
import org.wso2.carbon.connector.amazons3.utils.Error;
import org.wso2.carbon.connector.amazons3.utils.S3ConnectorUtils;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.connector.core.connection.ConnectionHandler;
import org.wso2.carbon.connector.core.util.ConnectorUtils;

public class S3Config extends AbstractConnector implements ManagedLifecycle {

    public void init(SynapseEnvironment synapseEnvironment) {
        //do nothing on deployment - configs unknown by that time
    }

    public void destroy() {
        ConnectionHandler.getConnectionHandler().
                shutdownConnections(S3Constants.CONNECTOR_NAME);
    }

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        String connectorName = S3Constants.CONNECTOR_NAME;
        String connectionName = "";
        try {
            ConnectionConfiguration configuration = getConnectionConfigFromContext(messageContext);
            connectionName = configuration.getConnectionName();

            ConnectionHandler handler = ConnectionHandler.getConnectionHandler();
            if (!handler.checkIfConnectionExists(connectorName, connectionName)) {
                S3ConnectionHandler s3ConnectionHandler = new S3ConnectionHandler(configuration);
                handler.createConnection(S3Constants.CONNECTOR_NAME, connectionName, s3ConnectionHandler);
            } else {
                S3ConnectionHandler connectionHandler = (S3ConnectionHandler) handler
                        .getConnection(connectorName, connectionName);
                if (connectionHandler.getConnectionConfig() != configuration) {
                    connectionHandler.setConnectionConfig(configuration);
                }
            }
        } catch (InvalidConfigurationException e) {
            S3ConnectorUtils.setErrorPropertiesToMessage(messageContext, Error.INVALID_CONFIGURATION);
            handleException("[" + connectionName + "] Failed to initiate s3 connector configuration.", e,
                    messageContext);
        }
    }

    private ConnectionConfiguration getConnectionConfigFromContext(MessageContext msgContext)
            throws InvalidConfigurationException {

        String connectionName = (String) ConnectorUtils.
                lookupTemplateParamater(msgContext, S3Constants.CONNECTION_NAME);
        String region = (String) ConnectorUtils.
                lookupTemplateParamater(msgContext, S3Constants.REGION);
        String awsAccessKeyId = (String) ConnectorUtils.
                lookupTemplateParamater(msgContext, S3Constants.AWS_ACCESS_KEY_ID);
        String awsSecretAccessKey = (String) ConnectorUtils.
                lookupTemplateParamater(msgContext, S3Constants.AWS_SECRET_ACCESS_KEY);
        String host = (String) ConnectorUtils.lookupTemplateParamater(msgContext, S3Constants.HOST);
        String roleArn = (String) ConnectorUtils.lookupTemplateParamater(msgContext, S3Constants.ROLE_ARN);
        String roleSessionName = (String) ConnectorUtils.lookupTemplateParamater(msgContext, S3Constants.ROLE_SESSION_NAME);

        ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
        connectionConfig.setConnectionName(connectionName);
        connectionConfig.setRegion(region);
        connectionConfig.setAwsAccessKeyId(awsAccessKeyId);
        connectionConfig.setAwsSecretAccessKey(awsSecretAccessKey);
        connectionConfig.setHost(host);
        connectionConfig.setRoleArn(roleArn);
        connectionConfig.setRoleSessionName(roleSessionName);
        return connectionConfig;
    }
}
