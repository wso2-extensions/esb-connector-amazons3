package org.wso2.carbon.connector.amazons3.connection;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.connector.amazons3.pojo.ConnectionConfiguration;
import org.wso2.carbon.connector.core.connection.Connection;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;

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
        String roleArn = this.connectionConfig.getRoleArn();
        String roleSessionName = this.connectionConfig.getRoleSessionName();
        S3ClientBuilder s3ClientBuilder = S3Client.builder();
        // This is used to access the buckets with a cross account role
        if (StringUtils.isNotBlank(roleArn) && StringUtils.isNotBlank(roleSessionName)) {
		StsClient stsClient = StsClient.create();
            AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                    .roleArn(roleArn)
                    .roleSessionName(roleSessionName)
                    .build();
            AssumeRoleResponse response = stsClient.assumeRole(assumeRoleRequest);
            AwsSessionCredentials temporaryCredentials = AwsSessionCredentials.create(
                    response.credentials().accessKeyId(),
                    response.credentials().secretAccessKey(),
                    response.credentials().sessionToken());
            return s3ClientBuilder.region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(temporaryCredentials))
                    .build();
        } else {
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
    }

    public ConnectionConfiguration getConnectionConfig() {
        return connectionConfig;
    }

    public void setConnectionConfig(ConnectionConfiguration connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    /**
     * Get an instance of the S3Presigner
     *
     * @return an instance of S3Presigner
     */
    public S3Presigner getS3Presigner() {
        String region = this.connectionConfig.getRegion();
        String awsAccessKeyId = this.connectionConfig.getAwsAccessKeyId();
        String awsSecretAccessKey = this.connectionConfig.getAwsSecretAccessKey();
        String host = this.connectionConfig.getHost();
        S3Presigner.Builder s3PresignerBuilder = S3Presigner.builder();
        if (StringUtils.isNotEmpty(awsAccessKeyId) && StringUtils.isNotEmpty(awsSecretAccessKey)) {
            AwsCredentialsProvider credentialsProvider =
                    StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey));
            s3PresignerBuilder.credentialsProvider(credentialsProvider);
        }
        if (StringUtils.isNotEmpty(host)) {
            s3PresignerBuilder.endpointOverride(URI.create(host));
        }
        return s3PresignerBuilder.region(Region.of(region)).build();
    }
}
