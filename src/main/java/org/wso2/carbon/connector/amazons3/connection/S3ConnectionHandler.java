package org.wso2.carbon.connector.amazons3.connection;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.connector.amazons3.pojo.ConnectionConfiguration;
import org.wso2.carbon.connector.core.connection.Connection;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3ConnectionHandler implements Connection {

    private ConnectionConfiguration connectionConfig;

    public S3ConnectionHandler(ConnectionConfiguration fsConfig) {
            //need this to get region when performing operations
            this.connectionConfig = fsConfig;
    }

    /**
     * @return an instance of S3Client
     */
    public S3Client getS3Client() {
        String region = this.connectionConfig.getRegion();
        String awsAccessKeyId = this.connectionConfig.getAwsAccessKeyId();
        String awsSecretAccessKey = this.connectionConfig.getAwsSecretAccessKey();
        if (StringUtils.isNotEmpty(awsAccessKeyId) && StringUtils.isNotEmpty(awsSecretAccessKey)) {
            AwsCredentialsProvider credentialsProvider =
                    StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey));
            return S3Client.builder()
                    .credentialsProvider(credentialsProvider)
                    .region(Region.of(region))
                    .httpClientBuilder(UrlConnectionHttpClient.builder())
                    .build();
        } else {
            return S3Client.builder()
                    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                    .region(Region.of(region))
                    .httpClientBuilder(UrlConnectionHttpClient.builder())
                    .build();
        }
    }

    public ConnectionConfiguration getConnectionConfig() {
        return connectionConfig;
    }

    public void setConnectionConfig(ConnectionConfiguration connectionConfig) {
        this.connectionConfig = connectionConfig;
    }
}
