package org.wso2.carbon.connector.amazons3.operations;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.amazons3.connection.S3ConnectionHandler;
import org.wso2.carbon.connector.amazons3.constants.S3Constants;
import org.wso2.carbon.connector.amazons3.convertors.S3POJOHandler;
import org.wso2.carbon.connector.amazons3.exception.InvalidConfigurationException;
import org.wso2.carbon.connector.amazons3.pojo.S3OperationResult;
import org.wso2.carbon.connector.amazons3.pojo.TagConfiguration;
import org.wso2.carbon.connector.amazons3.utils.Error;
import org.wso2.carbon.connector.amazons3.utils.S3ConnectorUtils;
import org.wso2.integration.connector.core.AbstractConnectorOperation;
import org.wso2.integration.connector.core.ConnectException;
import org.wso2.integration.connector.core.connection.ConnectionHandler;
import org.wso2.integration.connector.core.util.ConnectorUtils;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.AccessControlPolicy;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.GetObjectTorrentRequest;
import software.amazon.awssdk.services.s3.model.GetObjectTorrentResponse;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingResponse;
import software.amazon.awssdk.services.s3.model.GetObjectAclRequest;
import software.amazon.awssdk.services.s3.model.GetObjectAclResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListPartsRequest;
import software.amazon.awssdk.services.s3.model.ListPartsResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.Part;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectAclResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.RestoreObjectRequest;
import software.amazon.awssdk.services.s3.model.RestoreObjectResponse;
import software.amazon.awssdk.services.s3.model.RestoreRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.UploadPartCopyRequest;
import software.amazon.awssdk.services.s3.model.UploadPartCopyResponse;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implements object related operations
 */
public class ObjectOperations extends AbstractConnectorOperation {
    private static Log log = LogFactory.getLog(ObjectOperations.class);
    S3POJOHandler s3POJOHandler = new S3POJOHandler();

    @Override
    public void execute(MessageContext messageContext, String responseVariable, Boolean overwriteBody)
            throws ConnectException {
        String operationName = (String) messageContext.getProperty(S3Constants.OPERATION_NAME);
        String errorMessage = "";
        String connectorName = S3Constants.CONNECTOR_NAME;
        ConnectionHandler handler = ConnectionHandler.getConnectionHandler();

        String bucketName, objectKey, uploadId, torrentFilePath, versionId, filePath, destinationBucket, range,
                ifModifiedSince, ifUnmodifiedSince, ifMatch, ifNoneMatch, responseCacheControl, responseContentType,
                responseContentLanguage, responseContentDisposition, responseContentEncoding, responseExpires,
                copySource, copySourceSSECustomerAlgorithm, copySourceSSECustomerKey, copySourceSSECustomerKeyMD5,
                sseCustomerAlgorithm, sseCustomerKey, sseCustomerKeyMD5, mfa, requestPayer, copySourceRange, acl,
                cacheControl, contentDisposition, contentEncoding, contentLanguage, contentType, contentMD5,
                grantFullControl, grantRead, grantReadACP, grantWrite, grantWriteACP, serverSideEncryption,
                storageClass, websiteRedirectLocation, ssekmsKeyId, ssekmsEncryptionContext, tagging, objectLockMode,
                objectLockLegalHoldStatus, copySourceIfMatch, copySourceIfNoneMatch, metadataDirective,
                taggingDirective, destinationKey, expires, copySourceIfModifiedSince, copySourceIfUnmodifiedSince,
                objectLockRetainUntilDate, destinationFilePath, fileContent, isFileContentEncoded,
                signatureDurationInMins, isContentAsBase64;
        Map<String, String> metadata;
        int maxParts, partNumberMarker;
        Integer partNumber = null;
        RequestBody s3RequestBody = null;
        List<Part> s3PartDetails = new ArrayList<>();
        List<CompletedPart> s3CompletedParts = new ArrayList<>();
        AccessControlPolicy s3AccessControlPolicy = AccessControlPolicy.builder().build();
        RestoreRequest s3RestoreRequest = RestoreRequest.builder().build();
        Delete s3DeleteConfig = Delete.builder().build();

        try {
            String connectionName = S3ConnectorUtils.getConnectionName(messageContext);

            //get s3 client
            S3ConnectionHandler s3ConnectionHandler = (S3ConnectionHandler) handler
                    .getConnection(connectorName, connectionName);
            S3Client s3Client = s3ConnectionHandler.getS3Client();

            // get s3 presigner
            S3Presigner s3Presigner = s3ConnectionHandler.getS3Presigner();

            //read inputs
            bucketName = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "bucketName");
            objectKey = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "objectKey");
            uploadId = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "uploadId");
            String accessControlPolicyStr = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "accessControlList");
            if (StringUtils.isNotEmpty(accessControlPolicyStr)) {
                org.wso2.carbon.connector.amazons3.pojo.AccessControlPolicy configuration =
                        s3POJOHandler.xmlToObject(accessControlPolicyStr,
                        org.wso2.carbon.connector.amazons3.pojo.AccessControlPolicy.class);
                s3AccessControlPolicy = s3POJOHandler.castAccessControlPolicy(configuration);
            }
            torrentFilePath = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "torrentFilePath");
            versionId = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "versionId");
            destinationBucket = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "destinationBucket");
            acl = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "acl");
            cacheControl = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "cacheControl");
            contentDisposition = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "contentDisposition");
            contentEncoding = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "contentEncoding");
            contentLanguage = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "contentLanguage");
            contentType = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "contentType");
            contentMD5 = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "contentMD5");
            expires = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "expires");
            grantFullControl = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "grantFullControl");
            grantRead = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "grantRead");
            grantReadACP = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "grantReadACP");
            grantWrite = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "grantWrite");
            grantWriteACP = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "grantWriteACP");
            serverSideEncryption = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "objeserverSideEncryptionctPath");
            storageClass = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "storageClass");
            websiteRedirectLocation = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "websiteRedirectLocation");
            sseCustomerAlgorithm = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "sseCustomerAlgorithm");
            sseCustomerKey = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "sseCustomerKey");
            sseCustomerKeyMD5 = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "sseCustomerKeyMD5");
            ssekmsKeyId = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "ssekmsKeyId");
            ssekmsEncryptionContext = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "ssekmsEncryptionContext");
            requestPayer = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "requestPayer");
            tagging = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "tagging");
            objectLockMode = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "objectLockMode");
            objectLockRetainUntilDate = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "objectLockRetainUntilDate");
            objectLockLegalHoldStatus = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "objectLockLegalHoldStatus");
            Object maxPartsObj = ConnectorUtils.
                    lookupTemplateParamater(messageContext, "maxParts");
            maxParts = maxPartsObj != null ? Integer.valueOf((String) maxPartsObj) : 1000;
            Object partNumberMarkerObj = ConnectorUtils.
                    lookupTemplateParamater(messageContext, "partNumberMarker");
            partNumberMarker = partNumberMarkerObj != null ? Integer.valueOf((String) partNumberMarkerObj) : 1;
            String restoreRequestStr = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "restoreRequest");
            if (StringUtils.isNotEmpty(restoreRequestStr)) {
                org.wso2.carbon.connector.amazons3.pojo.RestoreRequest configuration =
                        s3POJOHandler.xmlToObject(restoreRequestStr,
                        org.wso2.carbon.connector.amazons3.pojo.RestoreRequest.class);
                s3RestoreRequest = s3POJOHandler.castRestoreRequest(configuration);
            }
            String partNumberObj = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "partNumber");
            if (StringUtils.isNotBlank(partNumberObj)) {
                partNumber = Integer.valueOf(partNumberObj);
            }
            filePath = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "filePath");
            fileContent = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "fileContent");
            isFileContentEncoded = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "isContentBase64Encoded");
            if (StringUtils.isNotEmpty(fileContent)) {
                if (Boolean.parseBoolean(isFileContentEncoded)) {
                    s3RequestBody = RequestBody.fromBytes(Base64.getDecoder().decode(fileContent));
                } else {
                    s3RequestBody = RequestBody.fromString(fileContent);
                }
            }
            if (StringUtils.isNotEmpty(filePath)) {
                s3RequestBody = RequestBody.fromFile(Paths.get(filePath));
            }
            destinationFilePath = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "destinationFilePath");
            range = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "range");
            ifModifiedSince = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "ifModifiedSince");
            ifUnmodifiedSince = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "ifUnmodifiedSince");
            ifMatch = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "ifMatch");
            ifNoneMatch = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "ifNoneMatch");
            copySource = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "copySource");
            copySourceRange = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "copySourceRange");
            copySourceSSECustomerAlgorithm = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "copySourceSSECustomerAlgorithm");
            copySourceSSECustomerKey = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "copySourceSSECustomerKey");
            copySourceSSECustomerKeyMD5 = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "copySourceSSECustomerKeyMD5");
            String partDetailsStr = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "partDetails");
            if (StringUtils.isNotEmpty(partDetailsStr)) {
                List<org.wso2.carbon.connector.amazons3.pojo.Part> parts =
                        s3POJOHandler.xmlToObject(partDetailsStr,
                                org.wso2.carbon.connector.amazons3.pojo.PartDetails.class).getParts();
                s3PartDetails = s3POJOHandler.castParts(parts);
            }
            String completedPartDetailsStr = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "completedPartDetails");
            if (StringUtils.isNotEmpty(completedPartDetailsStr)) {
                List<org.wso2.carbon.connector.amazons3.pojo.CompletedPart> completedParts =
                        s3POJOHandler.xmlToObject(completedPartDetailsStr,
                                org.wso2.carbon.connector.amazons3.pojo.CompletedPartDetails.class).getCompletedParts();
                s3CompletedParts = s3POJOHandler.castCompletedParts(completedParts);
            }
            String deleteConfigStr = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "deleteConfig");
            if (StringUtils.isNotEmpty(deleteConfigStr)) {
                org.wso2.carbon.connector.amazons3.pojo.Delete configuration =
                        s3POJOHandler.xmlToObject(deleteConfigStr,
                                org.wso2.carbon.connector.amazons3.pojo.Delete.class);
                s3DeleteConfig = s3POJOHandler.castDeleteConfig(configuration);
            }

            responseCacheControl = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "responseCacheControl");
            responseContentType = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "responseContentType");
            responseContentLanguage = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "responseContentLanguage");
            responseContentDisposition = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "responseContentDisposition");
            responseContentEncoding = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "responseContentEncoding");
            responseExpires = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "responseExpires");
            mfa = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "mfa");
            String bypassGovernanceRetentionObj = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "bypassGovernanceRetention");
            copySourceIfMatch = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "copySourceIfMatch");
            copySourceIfNoneMatch = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "copySourceIfNoneMatch");
            metadataDirective = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "metadataDirective");
            String metadataStr = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "metadata");
            metadata = StringUtils.isNotEmpty(metadataStr) ? Arrays.stream(metadataStr.split(","))
                    .map(s -> s.split(":"))
                    .collect(Collectors.toMap(s -> s[0], s -> s[1])) : null;
            taggingDirective = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "taggingDirective");
            destinationKey = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "destinationKey");

            copySourceIfModifiedSince = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "copySourceIfModifiedSince");
            copySourceIfUnmodifiedSince = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "copySourceIfUnmodifiedSince");
            signatureDurationInMins = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "signatureDurationInMins");
            isContentAsBase64 = (String) ConnectorUtils.
                    lookupTemplateParamater(messageContext, "getContentAsBase64");

            //call the operations
            switch (operationName) {
                case S3Constants.OPERATION_ABORT_MULTIPART_UPLOAD:
                    errorMessage = "Error while aborting the multipart upload";
                    abortMultipartUpload(operationName, s3Client, bucketName, objectKey, uploadId, requestPayer,
                            messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_COMPLETE_MULTIPART_UPLOAD:
                    errorMessage = "Error while completing the multipart upload";
                    CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                            .parts(s3CompletedParts)
                            .build();
                    completeMultipartUpload(operationName, s3Client, bucketName, objectKey, uploadId,
                            completedMultipartUpload, requestPayer, messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_COPY_BUCKET_OBJECT:
                    errorMessage = "Error while copying the object";
                    copyBucketObject(operationName, s3Client, acl, cacheControl, contentDisposition,
                            contentEncoding, contentLanguage, contentType, copySource, copySourceIfMatch,
                            copySourceIfModifiedSince, copySourceIfNoneMatch, copySourceIfUnmodifiedSince, expires,
                            grantFullControl, grantRead, grantReadACP, grantWriteACP, metadataDirective, metadata,
                            taggingDirective, serverSideEncryption, storageClass, websiteRedirectLocation,
                            sseCustomerAlgorithm, sseCustomerKey, sseCustomerKeyMD5, ssekmsKeyId,
                            ssekmsEncryptionContext, copySourceSSECustomerAlgorithm, copySourceSSECustomerKey,
                            copySourceSSECustomerKeyMD5, requestPayer, tagging, objectLockMode,
                            objectLockRetainUntilDate, objectLockLegalHoldStatus, destinationBucket, destinationKey,
                            messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_CREATE_MULTIPART_UPLOAD:
                    errorMessage = "Error while performing multipart upload creation";
                    createMultipartUpload(operationName, s3Client, acl, bucketName, cacheControl, contentDisposition,
                            contentEncoding, contentLanguage, contentType, expires, grantFullControl, grantRead,
                            grantReadACP, grantWriteACP, objectKey, metadata, serverSideEncryption, storageClass,
                            websiteRedirectLocation, sseCustomerAlgorithm, sseCustomerKey, sseCustomerKeyMD5,
                            ssekmsKeyId, ssekmsEncryptionContext, requestPayer, tagging, objectLockMode,
                            objectLockRetainUntilDate, objectLockLegalHoldStatus, messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_DELETE_OBJECT:
                    errorMessage = "Error while deleting the objects";
                    deleteObject(operationName, s3Client, bucketName, objectKey, mfa, versionId, requestPayer,
                            bypassGovernanceRetentionObj, messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_DELETE_OBJECTS:
                    errorMessage = "Error while deleting multiple objects";
                    deleteObjects(operationName, s3Client, bucketName, s3DeleteConfig, bypassGovernanceRetentionObj,
                            mfa, requestPayer, messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_GET_OBJECT:
                    errorMessage = "Error while retrieving the object";
                    getObject(operationName, s3Client, bucketName, objectKey, range, ifModifiedSince, ifUnmodifiedSince,
                            ifMatch, ifNoneMatch, responseCacheControl, responseContentType, responseContentLanguage,
                            responseContentDisposition, responseContentEncoding, responseExpires, versionId,
                            sseCustomerAlgorithm, sseCustomerKey, sseCustomerKeyMD5, requestPayer, partNumber,
                            destinationFilePath, isContentAsBase64, messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_GET_OBJECT_ACL:
                    errorMessage = "Error while retrieving the object ACL";
                    getObjectACL(operationName, s3Client, bucketName, objectKey, versionId, requestPayer,
                            messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_GET_OBJECT_TORRENT:
                    errorMessage = "Error while retrieving the object torrent info";
                    if (torrentFilePath == null) {
                        throw new IllegalArgumentException("Object path should be given to save the torrent file: "
                                + operationName);
                    }
                    getObjectTorrent(operationName, s3Client, bucketName, objectKey, Paths.get(torrentFilePath),
                            requestPayer, messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_HEAD_OBJECT:
                    errorMessage = "Error while retrieving the user access permission of the object";
                    headObject(operationName, s3Client, bucketName, objectKey, range, ifModifiedSince,
                            ifUnmodifiedSince, ifMatch, ifNoneMatch, versionId, sseCustomerAlgorithm, sseCustomerKey,
                            sseCustomerKeyMD5, requestPayer, partNumber, messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_GET_OBJECT_TAGGING:
                    errorMessage = "Error while retrieving the object tags";
                    getObjectTagging(operationName, s3Client, bucketName, objectKey, versionId, responseVariable, overwriteBody, messageContext);
                    break;
                case S3Constants.OPERATION_LIST_PARTS:
                    errorMessage = "Error while listing the object parts";
                    listParts(operationName, s3Client, bucketName, objectKey, uploadId, maxParts, partNumberMarker,
                            requestPayer, messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_MULTIPART_UPLOAD:
                    errorMessage = "Error while performing multipart upload";
                    multipartUpload(operationName, s3Client, bucketName, objectKey, s3PartDetails, s3RequestBody,
                            requestPayer, messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_PUT_OBJECT:
                    errorMessage = "Error while creating the object";
                    putObject(operationName, s3Client, acl, bucketName, cacheControl, contentDisposition,
                            contentEncoding, contentLanguage, contentType, contentMD5, expires, grantFullControl,
                            grantRead, grantReadACP, grantWriteACP, objectKey, metadata, serverSideEncryption,
                            storageClass, websiteRedirectLocation, sseCustomerAlgorithm, sseCustomerKey,
                            sseCustomerKeyMD5, ssekmsKeyId, ssekmsEncryptionContext, requestPayer, tagging,
                            objectLockMode, objectLockRetainUntilDate, objectLockLegalHoldStatus, s3RequestBody,
                            messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_PUT_OBJECT_ACL:
                    errorMessage = "Error while creating the object ACL";
                    putObjectAcl(operationName, s3Client, acl, s3AccessControlPolicy, bucketName, grantFullControl,
                            grantRead, grantReadACP, grantWrite, grantWriteACP, objectKey, requestPayer, versionId,
                            messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_RESTORE_OBJECT:
                    errorMessage = "Error while restoring the object";
                    restoreObject(operationName, s3Client, bucketName, objectKey, versionId, s3RestoreRequest,
                            requestPayer, messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_UPLOAD_PART:
                    errorMessage = "Error while uploading the object part";
                    if (partNumber == null) {
                        throw new IllegalArgumentException("partNumber is required for the operation " + operationName);
                    }
                    uploadPart(operationName, s3Client, bucketName, objectKey, contentMD5, uploadId, partNumber,
                            s3RequestBody, sseCustomerAlgorithm, sseCustomerKey, sseCustomerKeyMD5, requestPayer,
                            messageContext, responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_UPLOAD_PART_COPY:
                    errorMessage = "Error while uploading the part copy";
                    if (partNumber == null) {
                        throw new IllegalArgumentException("partNumber is required for the operation " + operationName);
                    }
                    uploadPartCopy(operationName, s3Client, bucketName, objectKey, uploadId, partNumber,
                            copySourceRange, ifModifiedSince, ifUnmodifiedSince, ifMatch, ifNoneMatch, copySource,
                            copySourceSSECustomerAlgorithm, copySourceSSECustomerKey, copySourceSSECustomerKeyMD5,
                            sseCustomerAlgorithm, sseCustomerKey, sseCustomerKeyMD5, requestPayer, messageContext,
                            responseVariable, overwriteBody);
                    break;
                case S3Constants.OPERATION_GENERATE_PUT_OBJECT_PRESIGNED_URL:
                    errorMessage = "Error while generating the presigned URL to upload an object";
                    if (signatureDurationInMins == null) {
                        throw new IllegalArgumentException("signatureDurationInMins is required for the operation "
                                + operationName);
                    }
                    generatePutObjectPresignedUrl(operationName, s3Presigner, bucketName, objectKey, contentType,
                            Long.parseLong(signatureDurationInMins), metadata, messageContext);
                    break;
                case S3Constants.OPERATION_GENERATE_GET_OBJECT_PRESIGNED_URL:
                    errorMessage = "Error while generating the presigned URL to download an object";
                    if (signatureDurationInMins == null) {
                        throw new IllegalArgumentException("signatureDurationInMins is required for the operation "
                                + operationName);
                    }
                    generateGetObjectPresignedUrl(operationName, s3Presigner, bucketName, objectKey,
                            Long.parseLong(signatureDurationInMins), messageContext);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation: " + operationName);
            }
        } catch (InvalidConfigurationException e) {
            S3OperationResult result = new S3OperationResult(
                    operationName,
                    false,
                    Error.INVALID_CONFIGURATION,
                    errorMessage);

            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException(errorMessage, e, messageContext);
        }
    }

    public void abortMultipartUpload(String operationName, S3Client s3Client, String bucketName, String objectKey,
                                     String uploadId, String requestPayer, MessageContext messageContext, 
                                     String responseVariable, Boolean overwriteBody) {
        S3OperationResult result;
        AbortMultipartUploadRequest request = AbortMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .uploadId(uploadId)
                .requestPayer(requestPayer)
                .build();
        try {
            AbortMultipartUploadResponse response = s3Client.abortMultipartUpload(request);
            SdkHttpResponse sdkHttpResponse = response.sdkHttpResponse();
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("operation", "AbortMultipartUploadResponse");
            responseJson.addProperty("status", Integer.toString(sdkHttpResponse.statusCode())
                    + ":" + sdkHttpResponse.statusText().orElse(""));
            responseJson.addProperty("requestCharged", response.requestChargedAsString());
            result = new S3OperationResult(
                    operationName,
                    true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName,
                    Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(
                    operationName,
                    false,
                    Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    private void completeMultipartUpload(String operationName, S3Client s3Client, String bucketName, String objectKey,
                                         String uploadId, CompletedMultipartUpload completedMultipartUpload,
                                         String requestPayer, MessageContext messageContext, 
                                         String responseVariable, Boolean overwriteBody) {
        S3OperationResult result;
        CompleteMultipartUploadRequest request = CompleteMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .uploadId(uploadId)
                .multipartUpload(completedMultipartUpload)
                .requestPayer(requestPayer)
                .build();
        try {
            CompleteMultipartUploadResponse response = s3Client.completeMultipartUpload(request);
            org.wso2.carbon.connector.amazons3.pojo.CompleteMultipartUploadResponse uploadResponse =
                    s3POJOHandler.castS3CompleteMultipartUploadResponse(response);
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(uploadResponse).getAsJsonObject();
            result = new S3OperationResult(
                    operationName,
                    true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName,
                    Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(
                    operationName,
                    false,
                    Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public void copyBucketObject(String operationName, S3Client s3Client, String acl, String cacheControl,
                                 String contentDisposition, String contentEncoding, String contentLanguage,
                                 String contentType, String copySource, String copySourceIfMatch,
                                 String copySourceIfModifiedSince, String copySourceIfNoneMatch,
                                 String copySourceIfUnmodifiedSince, String expires, String grantFullControl,
                                 String grantRead, String grantReadACP, String grantWriteACP, String metadataDirective,
                                 Map<String, String> metadata, String taggingDirective, String serverSideEncryption,
                                 String storageClass, String websiteRedirectLocation, String sseCustomerAlgorithm,
                                 String sseCustomerKey, String sseCustomerKeyMD5, String ssekmsKeyId,
                                 String ssekmsEncryptionContext, String copySourceSSECustomerAlgorithm,
                                 String copySourceSSECustomerKey, String copySourceSSECustomerKeyMD5,
                                 String requestPayer, String tagging, String objectLockMode,
                                 String objectLockRetainUntilDate, String objectLockLegalHoldStatus,
                                 String destinationBucket, String destinationKey, MessageContext messageContext,
                                 String responseVariable, Boolean overwriteBody) {
        S3OperationResult result;
        CopyObjectRequest request = CopyObjectRequest.builder()
                .acl(acl)
                .cacheControl(cacheControl)
                .contentDisposition(contentDisposition)
                .contentEncoding(contentEncoding)
                .contentLanguage(contentLanguage)
                .contentType(contentType)
                .copySource(copySource)
                .copySourceIfMatch(copySourceIfMatch)
                .copySourceIfModifiedSince(copySourceIfModifiedSince != null ?
                        Instant.parse(copySourceIfModifiedSince) : null)
                .copySourceIfNoneMatch(copySourceIfNoneMatch)
                .copySourceIfUnmodifiedSince(copySourceIfUnmodifiedSince != null ?
                        Instant.parse(copySourceIfUnmodifiedSince) : null)
                .destinationBucket(destinationBucket)
                .destinationKey(destinationKey)
                .expires(expires != null ? Instant.parse(expires) : null)
                .grantFullControl(grantFullControl)
                .grantRead(grantRead)
                .grantReadACP(grantReadACP)
                .grantWriteACP(grantWriteACP)
                .metadataDirective(metadataDirective)
                .taggingDirective(taggingDirective)
                .serverSideEncryption(serverSideEncryption)
                .storageClass(storageClass)
                .websiteRedirectLocation(websiteRedirectLocation)
                .sseCustomerAlgorithm(sseCustomerAlgorithm)
                .sseCustomerKey(sseCustomerKey)
                .sseCustomerKeyMD5(sseCustomerKeyMD5)
                .ssekmsKeyId(ssekmsKeyId)
                .ssekmsEncryptionContext(ssekmsEncryptionContext)
                .copySourceSSECustomerAlgorithm(copySourceSSECustomerAlgorithm)
                .copySourceSSECustomerKey(copySourceSSECustomerKey)
                .copySourceSSECustomerKeyMD5(copySourceSSECustomerKeyMD5)
                .requestPayer(requestPayer)
                .metadata(metadata)
                .objectLockMode(objectLockMode)
                .objectLockRetainUntilDate(objectLockRetainUntilDate != null ?
                        Instant.parse(objectLockRetainUntilDate) : null)
                .objectLockLegalHoldStatus(objectLockLegalHoldStatus)
                .tagging(tagging)
                .build();
        try {
            CopyObjectResponse response = s3Client.copyObject(request);
            org.wso2.carbon.connector.amazons3.pojo.CopyObjectResponse objectResponse =
                    s3POJOHandler.castS3CopyObjectResponse(response);
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(objectResponse).getAsJsonObject();
            result = new S3OperationResult(
                    operationName,
                    true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName,
                    Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(
                    operationName,
                    false,
                    Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    private String createMultipartUpload(String operationName, S3Client s3Client, String acl, String bucketName,
                                         String cacheControl, String contentDisposition, String contentEncoding,
                                         String contentLanguage, String contentType, String expires,
                                         String grantFullControl, String grantRead, String grantReadACP,
                                         String grantWriteACP, String objectKey, Map<String, String> metadata,
                                         String serverSideEncryption, String storageClass,
                                         String websiteRedirectLocation, String sseCustomerAlgorithm,
                                         String sseCustomerKey, String sseCustomerKeyMD5, String ssekmsKeyId,
                                         String ssekmsEncryptionContext, String requestPayer, String tagging,
                                         String objectLockMode, String objectLockRetainUntilDate,
                                         String objectLockLegalHoldStatus, MessageContext messageContext,
                                         String responseVariable, Boolean overwriteBody) {
        String uploadId = "";
        S3OperationResult result;
        CreateMultipartUploadRequest request = CreateMultipartUploadRequest.builder()
                .acl(acl)
                .bucket(bucketName)
                .cacheControl(cacheControl)
                .contentDisposition(contentDisposition)
                .contentEncoding(contentEncoding)
                .contentLanguage(contentLanguage)
                .contentType(contentType)
                .expires(expires != null ? Instant.parse(expires) : null)
                .grantFullControl(grantFullControl)
                .grantRead(grantRead)
                .grantReadACP(grantReadACP)
                .grantWriteACP(grantWriteACP)
                .key(objectKey)
                .metadata(metadata)
                .serverSideEncryption(serverSideEncryption)
                .storageClass(storageClass)
                .websiteRedirectLocation(websiteRedirectLocation)
                .sseCustomerAlgorithm(sseCustomerAlgorithm)
                .sseCustomerKey(sseCustomerKey)
                .sseCustomerKeyMD5(sseCustomerKeyMD5)
                .ssekmsKeyId(ssekmsKeyId)
                .ssekmsEncryptionContext(ssekmsEncryptionContext)
                .requestPayer(requestPayer)
                .tagging(tagging)
                .objectLockMode(objectLockMode)
                .objectLockRetainUntilDate(objectLockRetainUntilDate != null ?
                        Instant.parse(objectLockRetainUntilDate) : null)
                .objectLockLegalHoldStatus(objectLockLegalHoldStatus)
                .build();
        try {
            CreateMultipartUploadResponse response = s3Client.createMultipartUpload(request);
            uploadId = response.uploadId();
            org.wso2.carbon.connector.amazons3.pojo.CreateMultipartUploadResponse uploadResponse =
                    s3POJOHandler.castS3CreateMultipartUploadResponse(response);
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(uploadResponse).getAsJsonObject();
            result = new S3OperationResult(
                    operationName,
                    true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName,
                    Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(
                    operationName,
                    false,
                    Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
        return uploadId;
    }

    public void deleteObject(String operationName, S3Client s3Client, String bucketName, String objectName,
                             String mfa, String versionId, String requestPayer, String bypassGovernanceRetentionObj,
                             MessageContext messageContext, String responseVariable, Boolean overwriteBody) {
        S3OperationResult result;
        ArrayList<ObjectIdentifier> toDelete = new ArrayList<>();
        toDelete.add(ObjectIdentifier.builder().key(objectName).build());
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .mfa(mfa)
                .versionId(versionId)
                .bypassGovernanceRetention(bypassGovernanceRetentionObj != null ?
                        Boolean.valueOf(bypassGovernanceRetentionObj) : null)
                .requestPayer(requestPayer)
                .build();
        try {
            DeleteObjectResponse response = s3Client.deleteObject(request);
            org.wso2.carbon.connector.amazons3.pojo.DeleteObjectResponse objectResponse =
                    s3POJOHandler.castS3DeleteObjectResponse(response);
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(objectResponse).getAsJsonObject();
            result = new S3OperationResult(
                    operationName,
                    true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName,
                    Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(
                    operationName,
                    false,
                    Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public void deleteObjects(String operationName, S3Client s3Client, String bucketName, Delete s3DeleteConfig,
                              String bypassGovernanceRetentionObj, String mfa, String requestPayer,
                              MessageContext messageContext, String responseVariable, Boolean overwriteBody) {
        S3OperationResult result;
        DeleteObjectsRequest request = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(s3DeleteConfig)
                .mfa(mfa)
                .bypassGovernanceRetention(bypassGovernanceRetentionObj != null ?
                        Boolean.valueOf(bypassGovernanceRetentionObj) : null)
                .requestPayer(requestPayer)
                .build();
        try {
            DeleteObjectsResponse response = s3Client.deleteObjects(request);
            org.wso2.carbon.connector.amazons3.pojo.DeleteObjectsResponse objectResponse =
                    s3POJOHandler.castS3DeleteObjectsResponse(response);
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(objectResponse).getAsJsonObject();
            result = new S3OperationResult(
                    operationName,
                    true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName,
                    Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(
                    operationName,
                    false,
                    Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public void getObject(String operationName, S3Client s3Client, String bucketName, String objectKey, String range,
                          String ifModifiedSince, String ifUnmodifiedSince, String ifMatch, String ifNoneMatch,
                          String responseCacheControl, String responseContentType, String responseContentLanguage,
                          String responseContentDisposition, String responseContentEncoding, String responseExpires,
                          String versionId, String sseCustomerAlgorithm, String sseCustomerKey,
                          String sseCustomerKeyMD5, String requestPayer, Integer partNumber, String destinationFilePath,
                          String isContentAsBase64, MessageContext messageContext, String responseVariable, Boolean overwriteBody) {
        S3OperationResult result;
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .range(range)
                .ifMatch(ifMatch)
                .ifNoneMatch(ifNoneMatch)
                .ifModifiedSince(ifModifiedSince != null ? Instant.parse(ifModifiedSince) : null)
                .responseCacheControl(responseCacheControl)
                .responseContentType(responseContentType)
                .responseContentLanguage(responseContentLanguage)
                .responseContentDisposition(responseContentDisposition)
                .responseContentEncoding(responseContentEncoding)
                .responseExpires(responseExpires != null ? Instant.parse(responseExpires) : null)
                .ifUnmodifiedSince(ifUnmodifiedSince != null ? Instant.parse(ifUnmodifiedSince) : null)
                .versionId(versionId)
                .sseCustomerAlgorithm(sseCustomerAlgorithm)
                .sseCustomerKey(sseCustomerKey)
                .sseCustomerKeyMD5(sseCustomerKeyMD5)
                .requestPayer(requestPayer)
                .partNumber(partNumber)
                .build();
        try {
            org.wso2.carbon.connector.amazons3.pojo.GetObjectResponse objectResponse;
            if (StringUtils.isNotBlank(destinationFilePath)) {
                GetObjectResponse response = s3Client.getObject(request, Paths.get(destinationFilePath));
                objectResponse = s3POJOHandler.castS3GetObjectResponse(response);
            } else {
                ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(request);
                objectResponse = s3POJOHandler.castS3GetObjectResponseWithContent(responseBytes, isContentAsBase64);
            }
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(objectResponse).getAsJsonObject();
            result = new S3OperationResult(
                    operationName,
                    true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName,
                    Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (SdkException e) {
            result = S3ConnectorUtils.getFailureResult(e.getMessage(), operationName, Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        }
    }

    public void getObjectACL(String operationName, S3Client s3Client, String bucketName, String objectKey,
                             String versionId, String requestPayer, MessageContext messageContext, String responseVariable, Boolean overwriteBody) {
        S3OperationResult result;
        GetObjectAclRequest request = GetObjectAclRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .versionId(versionId)
                .requestPayer(requestPayer)
                .build();
        try {
            GetObjectAclResponse response = s3Client.getObjectAcl(request);
            // Convert the AWS SDK response directly to JSON
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(response).getAsJsonObject();
            result = new S3OperationResult(
                    operationName,
                    true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName,
                    Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(
                    operationName,
                    false,
                    Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public void getObjectTagging(String operationName, S3Client s3Client, String bucketName, String objectKey,
                                 String versionId, String responseVariable, boolean overwriteBody, MessageContext messageContext) {
        S3OperationResult result;
        GetObjectTaggingRequest.Builder requestBuilder = GetObjectTaggingRequest
                .builder()
                .key(objectKey)
                .bucket(bucketName);
        
        if (StringUtils.isNotBlank(versionId)) {
            requestBuilder.versionId(versionId);
        }
        
        GetObjectTaggingRequest request = requestBuilder.build();
        
        try {
            GetObjectTaggingResponse response = s3Client.getObjectTagging(request);
            
            // Create TagConfiguration POJO
            TagConfiguration tagConfig = new TagConfiguration();
            List<org.wso2.carbon.connector.amazons3.pojo.Tag> tags = new ArrayList<>();
            
            for (Tag s3Tag : response.tagSet()) {
                org.wso2.carbon.connector.amazons3.pojo.Tag tag = s3POJOHandler.castS3Tag(s3Tag);
                tags.add(tag);
            }
            tagConfig.setTags(tags);
            
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(tagConfig).getAsJsonObject();
            
            result = new S3OperationResult(operationName, true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName, Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(operationName, false, Error.CONNECTION_ERROR, 
                "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public void getObjectTorrent(String operationName, S3Client s3Client, String bucketName, String objectKey,
                                 Path torrentFilePath, String requestPayer, MessageContext messageContext, 
                                 String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        GetObjectTorrentRequest request = GetObjectTorrentRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .requestPayer(requestPayer)
                .build();
        try {
            GetObjectTorrentResponse response = s3Client.getObjectTorrent(request, torrentFilePath);
            SdkHttpResponse sdkHttpResponse = response.sdkHttpResponse();
            
            // Create response JSON
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("Status", 
                    Integer.toString(sdkHttpResponse.statusCode()) + ":" + sdkHttpResponse.statusText());
            responseJson.addProperty("RequestCharged", response.requestChargedAsString());
            
            result = new S3OperationResult(operationName, true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName, Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(operationName, false, Error.CONNECTION_ERROR, 
                "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public void headObject(String operationName, S3Client s3Client, String bucketName, String objectKey, String range,
                           String ifModifiedSince, String ifUnmodifiedSince, String ifMatch, String ifNoneMatch,
                           String versionId, String sseCustomerAlgorithm, String sseCustomerKey,
                           String sseCustomerKeyMD5, String requestPayer, Integer partNumber,
                           MessageContext messageContext, String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        HeadObjectRequest request = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .range(range)
                .ifModifiedSince(ifModifiedSince != null ? Instant.parse(ifModifiedSince) : null)
                .ifUnmodifiedSince(ifUnmodifiedSince != null ? Instant.parse(ifUnmodifiedSince) : null)
                .ifMatch(ifMatch)
                .ifNoneMatch(ifNoneMatch)
                .versionId(versionId)
                .sseCustomerAlgorithm(sseCustomerAlgorithm)
                .sseCustomerKey(sseCustomerKey)
                .sseCustomerKeyMD5(sseCustomerKeyMD5)
                .requestPayer(requestPayer)
                .partNumber(partNumber)
                .build();
        try {
            HeadObjectResponse response = s3Client.headObject(request);
            
            // Convert to POJO and then to JSON
            org.wso2.carbon.connector.amazons3.pojo.HeadObjectResponse objectResponse =
                    s3POJOHandler.castS3HeadObjectResponse(response);
            
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(objectResponse).getAsJsonObject();
            
            result = new S3OperationResult(operationName, true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName, Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(operationName, false, Error.CONNECTION_ERROR, 
                "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public void listParts(String operationName, S3Client s3Client, String bucketName, String objectKey, String uploadId,
                          int maxParts, int partNumberMarker, String requestPayer, MessageContext messageContext,
                          String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        ListPartsRequest request = ListPartsRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .uploadId(uploadId)
                .maxParts(maxParts)
                .partNumberMarker(partNumberMarker)
                .requestPayer(requestPayer)
                .build();
        try {
            ListPartsResponse response = s3Client.listParts(request);
            
            // Convert to POJO and then to JSON
            org.wso2.carbon.connector.amazons3.pojo.PartsConfiguration configuration =
                    s3POJOHandler.castS3PartsConfiguration(response);
            
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(configuration).getAsJsonObject();
            
            result = new S3OperationResult(operationName, true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName, Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(operationName, false, Error.CONNECTION_ERROR, 
                "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    private void multipartUpload(String operationName, S3Client s3Client, String bucketName, String objectKey,
                                 List<Part> partDetails, RequestBody s3RequestBody, String requestPayer,
                                 MessageContext messageContext, String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        try {
            String uploadId = createMultipartUpload(operationName, s3Client, null, bucketName, null, null, null, null,
                    null, null, null, null, null, null, objectKey, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, messageContext, responseVariable, overwriteBody);
            List<CompletedPart> completedParts = new ArrayList<>();
            for (Part part : partDetails) {
                CompletedPart completedPart = uploadPart(operationName, s3Client, bucketName, objectKey, null,
                        uploadId, part.partNumber(), s3RequestBody, null, null, null, null, messageContext, responseVariable, overwriteBody);
                if (completedPart.partNumber() != null && completedPart.eTag() != null) {
                    completedParts.add(completedPart);
                }
            }
            if (completedParts.size() != 0) {
                CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                        .parts(completedParts)
                        .build();
                completeMultipartUpload(operationName, s3Client, bucketName, objectKey, uploadId,
                        completedMultipartUpload, requestPayer, messageContext, responseVariable, overwriteBody);
            }
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName,
                    Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(
                    operationName,
                    false,
                    Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public void putObject(String operationName, S3Client s3Client, String acl, String bucketName,
                          String cacheControl, String contentDisposition, String contentEncoding,
                          String contentLanguage, String contentType, String contentMD5, String expires,
                          String grantFullControl, String grantRead, String grantReadACP, String grantWriteACP,
                          String objectKey, Map<String, String> metadata, String serverSideEncryption,
                          String storageClass, String websiteRedirectLocation, String sseCustomerAlgorithm,
                          String sseCustomerKey, String sseCustomerKeyMD5, String ssekmsKeyId,
                          String ssekmsEncryptionContext, String requestPayer, String tagging,
                          String objectLockMode, String objectLockRetainUntilDate,
                          String objectLockLegalHoldStatus, RequestBody requestBody, MessageContext messageContext,
                          String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        PutObjectRequest request = PutObjectRequest.builder()
                .acl(acl)
                .bucket(bucketName)
                .cacheControl(cacheControl)
                .contentDisposition(contentDisposition)
                .contentEncoding(contentEncoding)
                .contentLanguage(contentLanguage)
                .contentMD5(contentMD5)
                .contentType(contentType)
                .expires(expires != null ? Instant.parse(expires) : null)
                .grantFullControl(grantFullControl)
                .grantRead(grantRead)
                .grantReadACP(grantReadACP)
                .grantWriteACP(grantWriteACP)
                .key(objectKey)
                .metadata(metadata)
                .serverSideEncryption(serverSideEncryption)
                .storageClass(storageClass)
                .websiteRedirectLocation(websiteRedirectLocation)
                .sseCustomerAlgorithm(sseCustomerAlgorithm)
                .sseCustomerKey(sseCustomerKey)
                .sseCustomerKeyMD5(sseCustomerKeyMD5)
                .ssekmsKeyId(ssekmsKeyId)
                .ssekmsEncryptionContext(ssekmsEncryptionContext)
                .requestPayer(requestPayer)
                .tagging(tagging)
                .objectLockMode(objectLockMode)
                .objectLockRetainUntilDate(objectLockRetainUntilDate != null ?
                        Instant.parse(objectLockRetainUntilDate) : null)
                .objectLockLegalHoldStatus(objectLockLegalHoldStatus)
                .build();
        try {
            PutObjectResponse response = s3Client.putObject(request, requestBody);
            
            // Convert to POJO and then to JSON
            org.wso2.carbon.connector.amazons3.pojo.PutObjectResponse uploadResponse =
                    s3POJOHandler.castS3PutObjectResponse(response);
            
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(uploadResponse).getAsJsonObject();
            
            result = new S3OperationResult(operationName, true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, Boolean.valueOf(overwriteBody), resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName,
                    Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, Boolean.valueOf(overwriteBody), resultJSON, null, null);
        }
    }

    public void putObjectAcl(String operationName, S3Client s3Client, String acl,
                             AccessControlPolicy accessControlPolicy, String bucketName, String grantFullControl,
                             String grantRead, String grantReadACP, String grantWrite, String grantWriteACP,
                             String objectKey, String requestPayer, String versionId, MessageContext messageContext,
                             String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        PutObjectAclRequest request = PutObjectAclRequest.builder()
                .acl(acl)
                .accessControlPolicy(accessControlPolicy)
                .bucket(bucketName)
                .grantFullControl(grantFullControl)
                .grantRead(grantRead)
                .grantReadACP(grantReadACP)
                .grantWrite(grantWrite)
                .grantWriteACP(grantWriteACP)
                .key(objectKey)
                .requestPayer(requestPayer)
                .versionId(versionId)
                .build();
        try {
            PutObjectAclResponse response = s3Client.putObjectAcl(request);
            SdkHttpResponse sdkHttpResponse = response.sdkHttpResponse();
            
            // Create JSON response using Gson
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("Status", sdkHttpResponse.statusCode() + ":" + sdkHttpResponse.statusText());
            jsonResponse.addProperty("RequestCharged", response.requestChargedAsString());
            
            result = new S3OperationResult(operationName, true, jsonResponse);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName, Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(operationName, false, Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public void restoreObject(String operationName, S3Client s3Client, String bucketName, String objectKey,
                              String versionId, RestoreRequest restoreRequest,
                              String requestPayer, MessageContext messageContext, String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        RestoreObjectRequest request = RestoreObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .versionId(versionId)
                .restoreRequest(restoreRequest)
                .requestPayer(requestPayer)
                .build();
        try {
            RestoreObjectResponse response = s3Client.restoreObject(request);
            SdkHttpResponse sdkHttpResponse = response.sdkHttpResponse();
            
            // Create JSON response using Gson
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("Status", sdkHttpResponse.statusCode() + ":" + sdkHttpResponse.statusText());
            jsonResponse.addProperty("RestoreOutputPath", response.restoreOutputPath());
            
            result = new S3OperationResult(operationName, true, jsonResponse);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName, Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(operationName, false, Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public CompletedPart uploadPart(String operationName, S3Client s3Client, String bucketName, String objectKey,
                                    String contentMD5, String uploadId, Integer partNumber, RequestBody requestBody,
                                    String sseCustomerAlgorithm, String sseCustomerKey, String sseCustomerKeyMD5,
                                    String requestPayer, MessageContext messageContext, String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        CompletedPart completedPart = CompletedPart.builder().build();
        UploadPartRequest request = UploadPartRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentMD5(contentMD5)
                .uploadId(uploadId)
                .partNumber(partNumber)
                .sseCustomerAlgorithm(sseCustomerAlgorithm)
                .sseCustomerKey(sseCustomerKey)
                .sseCustomerKeyMD5(sseCustomerKeyMD5)
                .requestPayer(requestPayer)
                .build();
        try {
            UploadPartResponse response = s3Client.uploadPart(request, requestBody);
            String etag = response.eTag();
            completedPart = CompletedPart.builder()
                    .partNumber(partNumber)
                    .eTag(etag)
                    .build();
            
            // Convert to JSON using POJO and Gson
            org.wso2.carbon.connector.amazons3.pojo.UploadPartResponse uploadResponse =
                    s3POJOHandler.castS3UploadPartResponse(response);
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(uploadResponse).getAsJsonObject();
            
            result = new S3OperationResult(operationName, true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName, Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(operationName, false, Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
        return completedPart;
    }

    public void uploadPartCopy(String operationName, S3Client s3Client, String bucketName, String objectKey,
                               String uploadId, Integer partNumber, String copySourceRange, String ifModifiedSince,
                               String ifUnmodifiedSince, String ifMatch, String ifNoneMatch, String copySource,
                               String copySourceSSECustomerAlgorithm, String copySourceSSECustomerKey,
                               String copySourceSSECustomerKeyMD5, String sseCustomerAlgorithm, String sseCustomerKey,
                               String sseCustomerKeyMD5, String requestPayer, MessageContext messageContext, String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        UploadPartCopyRequest request = UploadPartCopyRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .uploadId(uploadId)
                .partNumber(partNumber)
                .copySource(copySource)
                .copySourceRange(copySourceRange)
                .copySourceIfUnmodifiedSince(ifUnmodifiedSince != null ? Instant.parse(ifUnmodifiedSince) : null)
                .copySourceIfModifiedSince(ifModifiedSince != null ? Instant.parse(ifModifiedSince) : null)
                .copySourceIfNoneMatch(ifNoneMatch)
                .copySourceIfMatch(ifMatch)
                .copySourceSSECustomerAlgorithm(copySourceSSECustomerAlgorithm)
                .copySourceSSECustomerKey(copySourceSSECustomerKey)
                .copySourceSSECustomerKeyMD5(copySourceSSECustomerKeyMD5)
                .sseCustomerAlgorithm(sseCustomerAlgorithm)
                .sseCustomerKey(sseCustomerKey)
                .sseCustomerKeyMD5(sseCustomerKeyMD5)
                .requestPayer(requestPayer)
                .build();
        try {
            UploadPartCopyResponse response = s3Client.uploadPartCopy(request);
            JsonObject responseJson;
            org.wso2.carbon.connector.amazons3.pojo.UploadPartCopyResponse uploadResponse =
                    s3POJOHandler.castS3UploadPartCopyResponse(response);
            Gson gson = new Gson();
            String jsonString = gson.toJson(uploadResponse);
            responseJson = com.google.gson.JsonParser.parseString(jsonString).getAsJsonObject();
            result = new S3OperationResult(operationName, true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (S3Exception e) {
            result = S3ConnectorUtils.getFailureResult(e.awsErrorDetails().errorMessage(), operationName, Error.BAD_REQUEST);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (AwsServiceException | SdkClientException e) {
            result = new S3OperationResult(operationName, false, Error.CONNECTION_ERROR,
                    "Error occurred while accessing the AWS SDK service: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while accessing the AWS SDK service", e, messageContext);
        }
    }

    public void generatePutObjectPresignedUrl(String operationName, S3Presigner s3Presigner, String bucketName,
                                              String objectKey, String contentType, long signatureDurationInMins,
                                              Map<String, String> metadata, MessageContext messageContext) {
        generatePutObjectPresignedUrl(operationName, s3Presigner, bucketName, objectKey, contentType, 
                                      signatureDurationInMins, metadata, messageContext, null, false);
    }

    public void generatePutObjectPresignedUrl(String operationName, S3Presigner s3Presigner, String bucketName,
                                              String objectKey, String contentType, long signatureDurationInMins,
                                              Map<String, String> metadata, MessageContext messageContext,
                                              String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        PutObjectRequest.Builder putObjectRequestBuilder = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey);
        if (StringUtils.isNotEmpty(contentType)) {
            putObjectRequestBuilder.contentType(contentType);
        }
        if (metadata != null) {
            putObjectRequestBuilder.metadata(metadata);
        }
        PutObjectRequest request = putObjectRequestBuilder.build();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(signatureDurationInMins))
                .putObjectRequest(request)
                .build();
        try {
            PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(presignRequest);
            
            // Convert to POJO and then to JSON
            org.wso2.carbon.connector.amazons3.pojo.PutObjectPresignedUrlResponse response =
                    s3POJOHandler.castS3PutObjectPresignedUrlRequest(presignedPutObjectRequest);
            
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(response).getAsJsonObject();
            
            result = new S3OperationResult(operationName, true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (Exception e) {
            result = new S3OperationResult(operationName, false, Error.CONNECTION_ERROR,
                    "Error occurred while generating presigned URL: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while generating presigned URL", e, messageContext);
        }
    }

    public void generateGetObjectPresignedUrl(String operationName, S3Presigner s3Presigner, String bucketName,
                                              String objectKey, long signatureDurationInMins, MessageContext messageContext) {
        generateGetObjectPresignedUrl(operationName, s3Presigner, bucketName, objectKey, 
                                      signatureDurationInMins, messageContext, null, false);
    }

    public void generateGetObjectPresignedUrl(String operationName, S3Presigner s3Presigner, String bucketName,
                                              String objectKey, long signatureDurationInMins, MessageContext messageContext,
                                              String responseVariable, boolean overwriteBody) {
        S3OperationResult result;
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(signatureDurationInMins))
                .getObjectRequest(getObjectRequest)
                .build();
        try {
            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
            
            // Convert to POJO and then to JSON
            org.wso2.carbon.connector.amazons3.pojo.GetObjectPresignedUrlResponse response =
                    s3POJOHandler.castS3GetObjectPresignedUrlRequest(presignedGetObjectRequest);
            
            Gson gson = new Gson();
            JsonObject responseJson = gson.toJsonTree(response).getAsJsonObject();
            
            result = new S3OperationResult(operationName, true, responseJson);
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
        } catch (Exception e) {
            result = new S3OperationResult(operationName, false, Error.CONNECTION_ERROR,
                    "Error occurred while generating presigned URL: " + e.getMessage());
            JsonObject resultJSON = S3ConnectorUtils.generateOperationResult(messageContext, result);
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, resultJSON, null, null);
            handleException("Error occurred while generating presigned URL", e, messageContext);
        }
    }

    public byte[] getObjectFile(String path) throws IOException {

        byte[] bFile = readBytesFromFile(path);
        return bFile;
    }

    private byte[] readBytesFromFile(String filePath) throws IOException {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("Error while closing the file input stream", e);
                }
            }
        }
        return bytesArray;
    }
}
