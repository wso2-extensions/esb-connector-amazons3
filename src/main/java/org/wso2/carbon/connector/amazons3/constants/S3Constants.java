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
package org.wso2.carbon.connector.amazons3.constants;

/**
 * This class contains required constants.
 */
public class S3Constants {

    public static final String CONNECTOR_NAME = "amazons3";
    /**
     * Constant for Region.
     */
    public static final String REGION = "region";

    /**
     * Constant for AWS_ACCESS_KEY_ID.
     */
    public static final String AWS_ACCESS_KEY_ID = "awsAccessKeyId";

    /**
     * Constant for AWS_SECRET_ACCESS_KEY.
     */
    public static final String AWS_SECRET_ACCESS_KEY = "awsSecretAccessKey";

    public static final String HOST = "host";

    public static final String ROLE_ARN = "roleArn";

    public static final String ROLE_SESSION_NAME = "roleSessionName";

    /**
     * Constant for connection name.
     */
    public static final String CONNECTION_NAME = "name";

    public static final String PROPERTY_ERROR_CODE = "ERROR_CODE";
    public static final String PROPERTY_ERROR_MESSAGE = "ERROR_MESSAGE";
    public static final String STATUS_CODE = "HTTP_SC";

    public static final String OPERATION_NAME = "operationName";

    /**
     * Bucket operations.
     */
    public static final String OPERATION_CREATE_BUCKET = "createBucket";
    public static final String OPERATION_DELETE_BUCKET = "deleteBucket";
    public static final String OPERATION_DELETE_BUCKET_CORS = "deleteBucketCORS";
    public static final String OPERATION_DELETE_BUCKET_LIFECYCLE = "deleteBucketLifecycle";
    public static final String OPERATION_DELETE_BUCKET_POLICY = "deleteBucketPolicy";
    public static final String OPERATION_DELETE_BUCKET_REPLICATION = "deleteBucketReplication";
    public static final String OPERATION_DELETE_BUCKET_TAGGING = "deleteBucketTagging";
    public static final String OPERATION_DELETE_BUCKET_WEBSITE = "deleteBucketWebsite";
    public static final String OPERATION_GET_BUCKET_ACL = "getBucketACL";
    public static final String OPERATION_GET_BUCKET_CORS = "getBucketCORS";
    public static final String OPERATION_GET_BUCKET_LIFECYCLE_CONFIGURATION = "getBucketLifecycleConfiguration";
    public static final String OPERATION_GET_BUCKET_LOCATION = "getBucketLocation";
    public static final String OPERATION_GET_BUCKET_LOGGING = "getBucketLogging";
    public static final String OPERATION_GET_BUCKET_NOTIFICATION_CONFIGURATION = "getBucketNotificationConfiguration";
    public static final String OPERATION_GET_BUCKET_POLICY = "getBucketPolicy";
    public static final String OPERATION_GET_BUCKET_REPLICATION = "getBucketReplication";
    public static final String OPERATION_GET_BUCKET_REQUEST_PAYMENT = "getBucketRequestPayment";
    public static final String OPERATION_GET_BUCKET_TAGGING = "getBucketTagging";
    public static final String OPERATION_GET_BUCKET_VERSIONING = "getBucketVersioning";
    public static final String OPERATION_GET_BUCKET_WEBSITE = "getBucketWebsite";
    public static final String OPERATION_HEAD_BUCKET = "headBucket";
    public static final String OPERATION_LIST_BUCKETS = "listBuckets";
    public static final String OPERATION_LIST_MULTIPART_UPLOADS = "listMultipartUploads";
    public static final String OPERATION_LIST_OBJECTS = "listObjects";
    public static final String OPERATION_LIST_OBJECT_VERSIONS = "listObjectVersions";
    public static final String OPERATION_PUT_BUCKET_ACL = "putBucketACL";
    public static final String OPERATION_PUT_BUCKET_CORS = "putBucketCORS";
    public static final String OPERATION_PUT_BUCKET_LIFECYCLE_CONFIGURATION = "putBucketLifecycleConfiguration";
    public static final String OPERATION_PUT_BUCKET_POLICY = "putBucketPolicy";
    public static final String OPERATION_PUT_BUCKET_REPLICATION = "putBucketReplication";
    public static final String OPERATION_PUT_BUCKET_REQUEST_PAYMENT = "putBucketRequestPayment";
    public static final String OPERATION_PUT_BUCKET_TAGGING = "putBucketTagging";
    public static final String OPERATION_PUT_BUCKET_VERSIONING = "putBucketVersioning";
    public static final String OPERATION_PUT_BUCKET_WEBSITE = "putBucketWebsite";

    /**
     * Object operations.
     */
    public static final String OPERATION_ABORT_MULTIPART_UPLOAD = "abortMultipartUpload";
    public static final String OPERATION_COPY_BUCKET_OBJECT = "copyBucketObject";
    public static final String OPERATION_COMPLETE_MULTIPART_UPLOAD = "completeMultipartUpload";
    public static final String OPERATION_CREATE_MULTIPART_UPLOAD = "createMultipartUpload";
    public static final String OPERATION_DELETE_OBJECT = "deleteObject";
    public static final String OPERATION_DELETE_OBJECTS = "deleteObjects";
    public static final String OPERATION_GET_OBJECT = "getObject";
    public static final String OPERATION_GET_OBJECT_ACL = "getObjectACL";
    public static final String OPERATION_GET_OBJECT_TORRENT = "getObjectTorrent";
    public static final String OPERATION_HEAD_OBJECT = "headObject";
    public static final String OPERATION_GET_OBJECT_TAGGING = "getObjectTagging";
    public static final String OPERATION_LIST_PARTS = "listParts";
    public static final String OPERATION_MULTIPART_UPLOAD = "multipartUpload";
    public static final String OPERATION_PUT_OBJECT = "putObject";
    public static final String OPERATION_PUT_OBJECT_ACL = "putObjectAcl";
    public static final String OPERATION_RESTORE_OBJECT = "restoreObject";
    public static final String OPERATION_UPLOAD_PART = "uploadPart";
    public static final String OPERATION_UPLOAD_PART_COPY = "uploadPartCopy";

    public static final String OPERATION_GENERATE_PUT_OBJECT_PRESIGNED_URL = "generatePutObjectPresignedUrl";
    public static final String OPERATION_GENERATE_GET_OBJECT_PRESIGNED_URL = "generateGetObjectPresignedUrl";


    public static final String UTF_8 = "UTF-8";
}
