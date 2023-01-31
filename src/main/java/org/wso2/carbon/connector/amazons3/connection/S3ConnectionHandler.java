package org.wso2.carbon.connector.amazons3.connection;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.connector.amazons3.pojo.ConnectionConfiguration;
import org.wso2.carbon.connector.core.connection.Connection;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

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
        String host = this.connectionConfig.getHost();
        S3ClientBuilder s3ClientBuilder = S3Client.builder();
        if (StringUtils.isNotEmpty(awsAccessKeyId) && StringUtils.isNotEmpty(awsSecretAccessKey)) {
            AwsCredentialsProvider credentialsProvider =
                    StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey));
            s3ClientBuilder
                    .credentialsProvider(credentialsProvider);
        }
        if (StringUtils.isNotEmpty(host)) {
            s3ClientBuilder.endpointOverride(URI.create(host));
        }

        return s3ClientBuilder.region(Region.of(region))
                .httpClientBuilder(UrlConnectionHttpClient.builder()).build();
    }

    public ConnectionConfiguration getConnectionConfig() {
        return connectionConfig;
    }

    public void setConnectionConfig(ConnectionConfiguration connectionConfig) {
        this.connectionConfig = connectionConfig;
    }
}
