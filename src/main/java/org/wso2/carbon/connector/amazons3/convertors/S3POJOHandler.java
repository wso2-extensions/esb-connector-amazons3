/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.connector.amazons3.convertors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.ContentType;
import org.wso2.carbon.connector.amazons3.exception.InvalidConfigurationException;
import org.wso2.carbon.connector.amazons3.pojo.GetObjectPresignedUrlResponse;
import org.wso2.carbon.connector.amazons3.utils.XmlUtil;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.AccessControlPolicy;
import software.amazon.awssdk.services.s3.model.AccessControlTranslation;
import software.amazon.awssdk.services.s3.model.AbortIncompleteMultipartUpload;
import software.amazon.awssdk.services.s3.model.GetBucketNotificationConfigurationResponse;
import software.amazon.awssdk.services.s3.model.GetBucketVersioningResponse;
import software.amazon.awssdk.services.s3.model.GetBucketWebsiteResponse;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.Condition;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.CopyObjectResult;
import software.amazon.awssdk.services.s3.model.CopyPartResult;
import software.amazon.awssdk.services.s3.model.CORSRule;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CSVInput;
import software.amazon.awssdk.services.s3.model.CSVOutput;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeletedObject;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.Destination;
import software.amazon.awssdk.services.s3.model.DeleteMarkerEntry;
import software.amazon.awssdk.services.s3.model.DeleteMarkerReplication;
import software.amazon.awssdk.services.s3.model.Encryption;
import software.amazon.awssdk.services.s3.model.ErrorDocument;
import software.amazon.awssdk.services.s3.model.ExpirationStatus;
import software.amazon.awssdk.services.s3.model.ExistingObjectReplication;
import software.amazon.awssdk.services.s3.model.EncryptionConfiguration;
import software.amazon.awssdk.services.s3.model.FilterRule;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GlacierJobParameters;
import software.amazon.awssdk.services.s3.model.Grant;
import software.amazon.awssdk.services.s3.model.Grantee;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.IndexDocument;
import software.amazon.awssdk.services.s3.model.Initiator;
import software.amazon.awssdk.services.s3.model.InputSerialization;
import software.amazon.awssdk.services.s3.model.JSONInput;
import software.amazon.awssdk.services.s3.model.JSONOutput;
import software.amazon.awssdk.services.s3.model.LambdaFunctionConfiguration;
import software.amazon.awssdk.services.s3.model.LifecycleRule;
import software.amazon.awssdk.services.s3.model.LifecycleExpiration;
import software.amazon.awssdk.services.s3.model.LifecycleRuleAndOperator;
import software.amazon.awssdk.services.s3.model.LifecycleRuleFilter;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListMultipartUploadsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsResponse;
import software.amazon.awssdk.services.s3.model.ListPartsResponse;
import software.amazon.awssdk.services.s3.model.LoggingEnabled;
import software.amazon.awssdk.services.s3.model.MetadataEntry;
import software.amazon.awssdk.services.s3.model.Metrics;
import software.amazon.awssdk.services.s3.model.MultipartUpload;
import software.amazon.awssdk.services.s3.model.NoncurrentVersionExpiration;
import software.amazon.awssdk.services.s3.model.NoncurrentVersionTransition;
import software.amazon.awssdk.services.s3.model.NotificationConfigurationFilter;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.ObjectVersion;
import software.amazon.awssdk.services.s3.model.OutputLocation;
import software.amazon.awssdk.services.s3.model.OutputSerialization;
import software.amazon.awssdk.services.s3.model.Owner;
import software.amazon.awssdk.services.s3.model.ParquetInput;
import software.amazon.awssdk.services.s3.model.Part;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.QueueConfiguration;
import software.amazon.awssdk.services.s3.model.Redirect;
import software.amazon.awssdk.services.s3.model.RedirectAllRequestsTo;
import software.amazon.awssdk.services.s3.model.ReplicationRuleAndOperator;
import software.amazon.awssdk.services.s3.model.ReplicationTime;
import software.amazon.awssdk.services.s3.model.ReplicationTimeValue;
import software.amazon.awssdk.services.s3.model.ReplicationRule;
import software.amazon.awssdk.services.s3.model.ReplicationRuleFilter;
import software.amazon.awssdk.services.s3.model.ReplicationConfiguration;
import software.amazon.awssdk.services.s3.model.RestoreRequest;
import software.amazon.awssdk.services.s3.model.RoutingRule;
import software.amazon.awssdk.services.s3.model.SelectParameters;
import software.amazon.awssdk.services.s3.model.SourceSelectionCriteria;
import software.amazon.awssdk.services.s3.model.SseKmsEncryptedObjects;
import software.amazon.awssdk.services.s3.model.S3Error;
import software.amazon.awssdk.services.s3.model.S3KeyFilter;
import software.amazon.awssdk.services.s3.model.S3Location;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.Tagging;
import software.amazon.awssdk.services.s3.model.TargetGrant;
import software.amazon.awssdk.services.s3.model.TopicConfiguration;
import software.amazon.awssdk.services.s3.model.Transition;
import software.amazon.awssdk.services.s3.model.UploadPartCopyResponse;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;
import software.amazon.awssdk.services.s3.model.WebsiteConfiguration;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class S3POJOHandler {
    private static Log log = LogFactory.getLog(S3POJOHandler.class);

    public String getObjectAsXml(Object configuration, Class pojoClass) {
        XmlUtil xmlUtil = new XmlUtil();
        String xml = xmlUtil.convertToXml(configuration, pojoClass);
        return xml;
    }

    public <T> T xmlToObject(String xmlObj, Class<T> pojoClass) throws InvalidConfigurationException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(pojoClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (T) jaxbUnmarshaller.unmarshal(new StringReader(xmlObj));
        } catch (JAXBException e) {
            throw new InvalidConfigurationException("Unable to process the XML string: " + e.getMessage());
        }
    }

    public org.wso2.carbon.connector.amazons3.pojo.MultipartUploads castS3MultipartUploads(
            ListMultipartUploadsResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.MultipartUploads obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.MultipartUploads();
        obj1.setBucket(obj.bucket());
        obj1.setCommonPrefixes(obj.commonPrefixes() != null ? castS3CommonPrefixes(obj.commonPrefixes()) : null);
        obj1.setDelimiter(obj.delimiter());
        obj1.setEncodingType(obj.encodingTypeAsString());
        obj1.setKeyMarker(obj.keyMarker());
        if (obj.maxUploads() != null) {
            obj1.setMaxUploads(obj.maxUploads());
        }
        obj1.setMultipartUploads(obj.uploads() != null ? castS3Uploads(obj.uploads()) : null);
        obj1.setNextKeyMarker(obj.nextKeyMarker());
        obj1.setNextUploadIdMarker(obj.nextUploadIdMarker());
        obj1.setPrefix(obj.prefix());
        if (obj.isTruncated() != null) {
            obj1.setTruncated(obj.isTruncated());
        }
        obj1.setUploadIdMarker(obj.uploadIdMarker());
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.MultipartUpload> castS3Uploads(List<MultipartUpload> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.MultipartUpload> uploads = new ArrayList<>();
        for (MultipartUpload upload : obj) {
            if (upload != null) {
                uploads.add(castS3Upload(upload));
            }
        }
        return uploads;
    }

    public org.wso2.carbon.connector.amazons3.pojo.MultipartUpload castS3Upload(MultipartUpload obj) {
        org.wso2.carbon.connector.amazons3.pojo.MultipartUpload obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.MultipartUpload();
        obj1.setInitiated(obj.initiated());
        obj1.setKey(obj.key());
        obj1.setOwner(obj.owner() != null ? castS3Owner(obj.owner()) : null);
        obj1.setStorageClass(obj.storageClassAsString());
        obj1.setUploadId(obj.uploadId());
        obj1.setKey(obj.key());
        return obj1;
    }

    public WebsiteConfiguration castWebsiteConfiguration(
            org.wso2.carbon.connector.amazons3.pojo.WebsiteConfiguration obj) {
        return WebsiteConfiguration.builder()
                .errorDocument(obj.getErrorDocument() != null ? castErrorDocument(obj.getErrorDocument()) : null)
                .indexDocument(obj.getIndexDocument() != null ? castIndexDocument(obj.getIndexDocument()) : null)
                .redirectAllRequestsTo(obj.getRedirectAllRequestsTo() != null ?
                        castRedirectAllRequestsTo(obj.getRedirectAllRequestsTo()) : null)
                .routingRules(obj.getRoutingRules() != null ? castRoutingRules(obj.getRoutingRules()) : null)
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.WebsiteConfiguration castS3WebsiteConfiguration(
            GetBucketWebsiteResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.WebsiteConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.WebsiteConfiguration();
        obj1.setErrorDocument(obj.errorDocument() != null ? castS3ErrorDocument(obj.errorDocument()) : null);
        obj1.setIndexDocument(obj.indexDocument() != null ? castS3IndexDocument(obj.indexDocument()) : null);
        obj1.setRedirectAllRequestsTo(obj.redirectAllRequestsTo() != null ?
                castS3RedirectAllRequestsTo(obj.redirectAllRequestsTo()) : null);
        obj1.setRoutingRules(obj.routingRules() != null ? castS3RoutingRules(obj.routingRules()) : null);
        return obj1;
    }

    public ErrorDocument castErrorDocument(org.wso2.carbon.connector.amazons3.pojo.ErrorDocument obj) {
        return ErrorDocument.builder()
                .key(obj.getKey())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.ErrorDocument castS3ErrorDocument(ErrorDocument obj) {
        org.wso2.carbon.connector.amazons3.pojo.ErrorDocument obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ErrorDocument();
        obj1.setKey(obj.key());
        return obj1;
    }

    public IndexDocument castIndexDocument(org.wso2.carbon.connector.amazons3.pojo.IndexDocument obj) {
        return IndexDocument.builder()
                .suffix(obj.getSuffix())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.IndexDocument castS3IndexDocument(IndexDocument obj) {
        org.wso2.carbon.connector.amazons3.pojo.IndexDocument obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.IndexDocument();
        obj1.setSuffix(obj.suffix());
        return obj1;
    }

    public RedirectAllRequestsTo castRedirectAllRequestsTo(
            org.wso2.carbon.connector.amazons3.pojo.RedirectAllRequestsTo obj) {
        return RedirectAllRequestsTo.builder()
                .hostName(obj.getHostName())
                .protocol(obj.getProtocol())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.RedirectAllRequestsTo castS3RedirectAllRequestsTo(
            RedirectAllRequestsTo obj) {
        org.wso2.carbon.connector.amazons3.pojo.RedirectAllRequestsTo obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.RedirectAllRequestsTo();
        obj1.setHostName(obj.hostName());
        obj1.setProtocol(obj.protocolAsString());
        return obj1;
    }

    public List<RoutingRule> castRoutingRules(List<org.wso2.carbon.connector.amazons3.pojo.RoutingRule> obj) {
        List<RoutingRule> rules = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.RoutingRule rule : obj) {
            if (rule != null) {
                rules.add(castRoutingRule(rule));
            }
        }
        return rules;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.RoutingRule> castS3RoutingRules(List<RoutingRule> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.RoutingRule> rules = new ArrayList<>();
        for (RoutingRule rule : obj) {
            if (rule != null) {
                rules.add(castS3RoutingRule(rule));
            }
        }
        return rules;
    }

    public RoutingRule castRoutingRule(org.wso2.carbon.connector.amazons3.pojo.RoutingRule obj) {
        return RoutingRule.builder()
                .condition(castCondition(obj.getCondition()))
                .redirect(castRedirect(obj.getRedirect()))
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.RoutingRule castS3RoutingRule(RoutingRule obj) {
        org.wso2.carbon.connector.amazons3.pojo.RoutingRule obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.RoutingRule();
        obj1.setCondition(obj.condition() != null ? castS3Condition(obj.condition()) : null);
        obj1.setRedirect(obj.redirect() != null ? castS3Redirect(obj.redirect()) : null);
        return obj1;
    }

    public Condition castCondition(org.wso2.carbon.connector.amazons3.pojo.Condition obj) {
        return Condition.builder()
                .httpErrorCodeReturnedEquals(obj.getHttpErrorCodeReturnedEquals())
                .keyPrefixEquals(obj.getKeyPrefixEquals())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.Condition castS3Condition(Condition obj) {
        org.wso2.carbon.connector.amazons3.pojo.Condition obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.Condition();
        obj1.setHttpErrorCodeReturnedEquals(obj.httpErrorCodeReturnedEquals());
        obj1.setKeyPrefixEquals(obj.keyPrefixEquals());
        return obj1;
    }

    public Redirect castRedirect(org.wso2.carbon.connector.amazons3.pojo.Redirect obj) {
        return Redirect.builder()
                .hostName(obj.getHostName())
                .httpRedirectCode(obj.getHttpRedirectCode())
                .protocol(obj.getProtocol())
                .replaceKeyPrefixWith(obj.getReplaceKeyPrefixWith())
                .replaceKeyWith(obj.getReplaceKeyWith())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.Redirect castS3Redirect(Redirect obj) {
        org.wso2.carbon.connector.amazons3.pojo.Redirect obj1 = new org.wso2.carbon.connector.amazons3.pojo.Redirect();
        obj1.setHostName(obj.hostName());
        obj1.setProtocol(obj.protocolAsString());
        obj1.setHttpRedirectCode(obj.httpRedirectCode());
        obj1.setReplaceKeyPrefixWith(obj.replaceKeyPrefixWith());
        obj1.setReplaceKeyWith(obj.replaceKeyWith());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.ObjectConfiguration castS3Objects(ListObjectsResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.ObjectConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ObjectConfiguration();
        obj1.setCommonPrefixes(obj.commonPrefixes() != null ? castS3CommonPrefixes(obj.commonPrefixes()) : null);
        obj1.setContents(obj.contents() != null ? castS3ObjectContents(obj.contents()) : null);
        obj1.setDelimiter(obj.delimiter());
        obj1.setEncodingType(obj.encodingTypeAsString());
        obj1.setMarker(obj.marker());
        if (obj.maxKeys() != null) {
            obj1.setMaxKeys(obj.maxKeys());
        }
        obj1.setName(obj.name());
        obj1.setNextMarker(obj.nextMarker());
        obj1.setPrefix(obj.prefix());
        if (obj.isTruncated() != null) {
            obj1.setTruncated(obj.isTruncated());
        }
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.S3Object> castS3ObjectContents(List<S3Object> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.S3Object> s3Objects = new ArrayList<>();
        for (S3Object object : obj) {
            if (object != null) {
                s3Objects.add(castS3ObjectContent(object));
            }
        }
        return s3Objects;
    }

    public org.wso2.carbon.connector.amazons3.pojo.S3Object castS3ObjectContent(S3Object obj) {
        org.wso2.carbon.connector.amazons3.pojo.S3Object obj1 = new org.wso2.carbon.connector.amazons3.pojo.S3Object();
        obj1.setETag(obj.eTag());
        obj1.setKey(obj.key());
        obj1.setLastModified(obj.lastModified());
        obj1.setOwner(obj.owner() != null ? castS3Owner(obj.owner()) : null);
        obj1.setSize(obj.size());
        obj1.setStorageClass(obj.storageClassAsString());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.BucketVersioningConfiguration castS3BucketVersioningConfiguration(
            GetBucketVersioningResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.BucketVersioningConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.BucketVersioningConfiguration();
        obj1.setMfaDelete(obj.mfaDeleteAsString());
        obj1.setStatus(obj.statusAsString());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.BucketsConfiguration castS3BucketsConfiguration(
            ListBucketsResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.BucketsConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.BucketsConfiguration();
        obj1.setBuckets(obj.buckets() != null ? castS3Buckets(obj.buckets()) : null);
        obj1.setOwner(obj.owner() != null ? castS3Owner(obj.owner()) : null);
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.Bucket> castS3Buckets(List<Bucket> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.Bucket> s3Buckets = new ArrayList<>();
        for (Bucket bucket : obj) {
            if (bucket != null) {
                s3Buckets.add(castS3Bucket(bucket));
            }
        }
        return s3Buckets;
    }

    public org.wso2.carbon.connector.amazons3.pojo.Bucket castS3Bucket(Bucket obj) {
        org.wso2.carbon.connector.amazons3.pojo.Bucket obj1 = new org.wso2.carbon.connector.amazons3.pojo.Bucket();
        obj1.setCreationDate(obj.creationDate());
        obj1.setName(obj.name());
        return obj1;
    }

    public List<CORSRule> castCORSRules(List<org.wso2.carbon.connector.amazons3.pojo.CORSRule> obj) {
        List<CORSRule> s3CORSRules = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.CORSRule rule : obj) {
            if (rule != null) {
                s3CORSRules.add(castCORSRule(rule));
            }
        }
        return s3CORSRules;
    }

    public CORSRule castCORSRule(org.wso2.carbon.connector.amazons3.pojo.CORSRule obj) {
        return CORSRule.builder()
                .allowedHeaders(obj.getAllowedHeader())
                .allowedMethods(obj.getAllowedMethod())
                .allowedOrigins(obj.getAllowedOrigin())
                .exposeHeaders(obj.getExposeHeader())
                .maxAgeSeconds(obj.getMaxAgeSeconds()).build();
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.CORSRule> castS3CORSRules(List<CORSRule> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.CORSRule> s3CORSRules = new ArrayList<>();
        for (CORSRule rule : obj) {
            if (rule != null) {
                s3CORSRules.add(castS3CORSRule(rule));
            }
        }
        return s3CORSRules;
    }

    public org.wso2.carbon.connector.amazons3.pojo.CORSRule castS3CORSRule(CORSRule obj) {
        org.wso2.carbon.connector.amazons3.pojo.CORSRule rule = new org.wso2.carbon.connector.amazons3.pojo.CORSRule();
        rule.setAllowedHeader(obj.allowedHeaders());
        rule.setAllowedMethod(obj.allowedMethods());
        rule.setAllowedOrigin(obj.allowedOrigins());
        rule.setExposeHeader(obj.exposeHeaders());
        if (obj.maxAgeSeconds() != null) {
            rule.setMaxAgeSeconds(obj.maxAgeSeconds());
        }
        return rule;
    }

    public List<LifecycleRule> castLifecycleRules(List<org.wso2.carbon.connector.amazons3.pojo.LifecycleRule> obj) {
        List<LifecycleRule> s3LifecycleRules = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.LifecycleRule rule : obj) {
            if (rule != null) {
                s3LifecycleRules.add(castLifecycleRule(rule));
            }
        }
        return s3LifecycleRules;
    }

    public LifecycleRule castLifecycleRule(org.wso2.carbon.connector.amazons3.pojo.LifecycleRule obj) {
        return LifecycleRule.builder()
                .abortIncompleteMultipartUpload(obj.getAbortIncompleteMultipartUpload() != null ?
                        castAbortIncompleteMultipartUpload(obj.getAbortIncompleteMultipartUpload()) : null)
                .expiration(obj.getExpiration() != null ? castLifecycleExpiration(obj.getExpiration()) : null)
                .filter(obj.getFilter() != null ? castLifecycleRuleFilter(obj.getFilter()) : null)
                .id(obj.getId())
                .noncurrentVersionExpiration(obj.getNoncurrentVersionExpiration() != null ?
                        castNoncurrentVersionExpiration(obj.getNoncurrentVersionExpiration()) : null)
                .noncurrentVersionTransitions(obj.getNoncurrentVersionTransitions() != null ?
                        castNoncurrentVersionTransitions(obj.getNoncurrentVersionTransitions()) : null)
                .prefix(obj.getPrefix())
                .status(ExpirationStatus.fromValue(obj.getStatus()))
                .transitions(obj.getTransitions() != null ? castTransitions(obj.getTransitions()) : null).build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.LifecycleRule castS3LifecycleRule(LifecycleRule obj) {
        org.wso2.carbon.connector.amazons3.pojo.LifecycleRule rule =
                new org.wso2.carbon.connector.amazons3.pojo.LifecycleRule();
        rule.setAbortIncompleteMultipartUpload(obj.abortIncompleteMultipartUpload() != null ?
                castS3AbortIncompleteMultipartUpload(obj.abortIncompleteMultipartUpload()) : null);
        rule.setExpiration(obj.expiration() != null ? castS3LifecycleExpiration(obj.expiration()) : null);
        rule.setFilter(obj.filter() != null ? castS3LifecycleRuleFilter(obj.filter()) : null);
        rule.setId(obj.id());
        rule.setNoncurrentVersionExpiration(obj.noncurrentVersionExpiration() != null ?
                castS3NoncurrentVersionExpiration(obj.noncurrentVersionExpiration()) : null);
        rule.setNoncurrentVersionTransitions(obj.noncurrentVersionTransitions() != null ?
                castS3NoncurrentVersionTransitions(obj.noncurrentVersionTransitions()) : null);
        rule.setPrefix(obj.prefix());
        rule.setStatus(obj.status().toString());
        rule.setTransitions(obj.transitions() != null ? castS3Transitions(obj.transitions()) : null);
        return rule;
    }

    public AbortIncompleteMultipartUpload castAbortIncompleteMultipartUpload(
            org.wso2.carbon.connector.amazons3.pojo.AbortIncompleteMultipartUpload obj) {
        return AbortIncompleteMultipartUpload.builder()
                .daysAfterInitiation(obj.getDaysAfterInitiation())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.AbortIncompleteMultipartUpload castS3AbortIncompleteMultipartUpload(
            AbortIncompleteMultipartUpload obj) {
        org.wso2.carbon.connector.amazons3.pojo.AbortIncompleteMultipartUpload obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.AbortIncompleteMultipartUpload();
        if (obj.daysAfterInitiation() != null) {
            obj1.setDaysAfterInitiation(obj.daysAfterInitiation());
        }
        return obj1;
    }

    public LifecycleExpiration castLifecycleExpiration(
            org.wso2.carbon.connector.amazons3.pojo.LifecycleExpiration obj) {
        return LifecycleExpiration.builder()
                .date(obj.getDate())
                .days(obj.getDays())
                .expiredObjectDeleteMarker(obj.getExpiredObjectDeleteMarker())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.LifecycleExpiration castS3LifecycleExpiration(
            LifecycleExpiration obj) {
        org.wso2.carbon.connector.amazons3.pojo.LifecycleExpiration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.LifecycleExpiration();
        obj1.setDate(obj.date());
        if (obj.days() != null) {
            obj1.setDays(obj.days());
        }
        if (obj.expiredObjectDeleteMarker() != null) {
            obj1.setExpiredObjectDeleteMarker(obj.expiredObjectDeleteMarker());
        }
        return obj1;
    }

    public LifecycleRuleFilter castLifecycleRuleFilter(
            org.wso2.carbon.connector.amazons3.pojo.LifecycleRuleFilter obj) {
        return LifecycleRuleFilter.builder()
                .and(obj.getAnd() != null ? castAnd(obj.getAnd()) : null)
                .prefix(obj.getPrefix())
                .tag(obj.getTag() != null ? castTag(obj.getTag()) : null)
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.LifecycleRuleFilter castS3LifecycleRuleFilter(
            LifecycleRuleFilter obj) {
        org.wso2.carbon.connector.amazons3.pojo.LifecycleRuleFilter obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.LifecycleRuleFilter();
        obj1.setAnd(obj.and() != null ? castS3And(obj.and()) : null);
        obj1.setPrefix(obj.prefix());
        obj1.setTag(obj.tag() != null ? castS3Tag(obj.tag()) : null);
        return obj1;
    }

    public NoncurrentVersionExpiration castNoncurrentVersionExpiration(
            org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionExpiration obj) {
        return NoncurrentVersionExpiration.builder()
                .noncurrentDays(obj.getNoncurrentDays())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionExpiration castS3NoncurrentVersionExpiration(
            NoncurrentVersionExpiration obj) {
        org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionExpiration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionExpiration();
        if (obj.noncurrentDays() != null) {
            obj1.setNoncurrentDays(obj.noncurrentDays());
        }
        return obj1;
    }

    public List<NoncurrentVersionTransition> castNoncurrentVersionTransitions(
            List<org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionTransition> obj) {
        List<NoncurrentVersionTransition> s3NoncurrentVersionTransitions = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionTransition transition : obj) {
            if (transition != null) {
                s3NoncurrentVersionTransitions.add(castNoncurrentVersionTransition(transition));
            }
        }
        return s3NoncurrentVersionTransitions;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionTransition> castS3NoncurrentVersionTransitions(
            List<NoncurrentVersionTransition> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionTransition> noncurrentVersionTransitions =
                new ArrayList<>();
        for (NoncurrentVersionTransition transition : obj) {
            if (transition != null) {
                noncurrentVersionTransitions.add(castS3NoncurrentVersionTransition(transition));
            }
        }
        return noncurrentVersionTransitions;
    }

    public NoncurrentVersionTransition castNoncurrentVersionTransition(
            org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionTransition obj) {
        return NoncurrentVersionTransition.builder()
                .noncurrentDays(obj.getNoncurrentDays())
                .storageClass(obj.getStorageClass())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionTransition castS3NoncurrentVersionTransition(
            NoncurrentVersionTransition obj) {
        org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionTransition obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.NoncurrentVersionTransition();
        if (obj.noncurrentDays() != null) {
            obj1.setNoncurrentDays(obj.noncurrentDays());
        }
        obj1.setStorageClass(obj.storageClassAsString());
        return obj1;
    }

    public List<Transition> castTransitions(List<org.wso2.carbon.connector.amazons3.pojo.Transition> obj) {
        List<Transition> s3Transitions = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.Transition transition : obj) {
            if (transition != null) {
                s3Transitions.add(castTransition(transition));
            }
        }
        return s3Transitions;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.Transition> castS3Transitions(List<Transition> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.Transition> s3Transitions = new ArrayList<>();
        for (Transition transition : obj) {
            if (transition != null) {
                s3Transitions.add(castS3Transition(transition));
            }
        }
        return s3Transitions;
    }

    public Transition castTransition(org.wso2.carbon.connector.amazons3.pojo.Transition obj) {
        return Transition.builder()
                .date(obj.getDate())
                .days(obj.getDays())
                .storageClass(obj.getStorageClass())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.Transition castS3Transition(Transition obj) {
        org.wso2.carbon.connector.amazons3.pojo.Transition obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.Transition();
        obj1.setDate(obj.date());
        if (obj.days() != null) {
            obj1.setDays(obj.days());
        }
        obj1.setStorageClass(obj.storageClassAsString());
        return obj1;
    }

    public LifecycleRuleAndOperator castAnd(org.wso2.carbon.connector.amazons3.pojo.LifecycleRuleAndOperator obj) {
        return LifecycleRuleAndOperator.builder()
                .prefix(obj.getPrefix())
                .tags(obj.getTags() != null ? castTags(obj.getTags()) : null)
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.LifecycleRuleAndOperator castS3And(LifecycleRuleAndOperator obj) {
        org.wso2.carbon.connector.amazons3.pojo.LifecycleRuleAndOperator obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.LifecycleRuleAndOperator();
        obj1.setPrefix(obj.prefix());
        obj1.setTags(obj.tags() != null ? castS3Tags(obj.tags()) : null);
        return obj1;
    }

    public List<Tag> castTags(List<org.wso2.carbon.connector.amazons3.pojo.Tag> obj) {
        List<Tag> s3Tags = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.Tag tag : obj) {
            if (tag != null) {
                s3Tags.add(castTag(tag));
            }
        }
        return s3Tags;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.Tag> castS3Tags(List<Tag> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.Tag> s3Tags = new ArrayList<>();
        for (Tag tag : obj) {
            if (tag != null) {
                s3Tags.add(castS3Tag(tag));
            }
        }
        return s3Tags;
    }

    public Tag castTag(org.wso2.carbon.connector.amazons3.pojo.Tag obj) {
        return Tag.builder()
                .key(obj.getKey())
                .value(obj.getValue())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.Tag castS3Tag(Tag obj) {
        org.wso2.carbon.connector.amazons3.pojo.Tag obj1 = new org.wso2.carbon.connector.amazons3.pojo.Tag();
        obj1.setKey(obj.key());
        obj1.setValue(obj.value());
        return obj1;
    }

    public ReplicationConfiguration castReplicationConfiguration(
            org.wso2.carbon.connector.amazons3.pojo.ReplicationConfiguration obj) {
        return ReplicationConfiguration.builder()
                .role(obj.getRole())
                .rules(castReplicationRules(obj.getReplicationRules()))
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.ReplicationConfiguration castS3ReplicationConfiguration(
            ReplicationConfiguration obj) {
        org.wso2.carbon.connector.amazons3.pojo.ReplicationConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ReplicationConfiguration();
        obj1.setReplicationRules(obj.rules() != null ? castS3ReplicationRules(obj.rules()) : null);
        obj1.setRole(obj.role());
        return obj1;
    }

    public List<ReplicationRule> castReplicationRules(
            List<org.wso2.carbon.connector.amazons3.pojo.ReplicationRule> obj) {
        List<ReplicationRule> s3ReplicationRules = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.ReplicationRule rule : obj) {
            if (rule != null) {
                s3ReplicationRules.add(castReplicationRule(rule));
            }
        }
        return s3ReplicationRules;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.ReplicationRule> castS3ReplicationRules(
            List<ReplicationRule> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.ReplicationRule> s3ReplicationRules = new ArrayList<>();
        for (ReplicationRule rule : obj) {
            if (rule != null) {
                s3ReplicationRules.add(castS3ReplicationRule(rule));
            }
        }
        return s3ReplicationRules;
    }

    public ReplicationRule castReplicationRule(org.wso2.carbon.connector.amazons3.pojo.ReplicationRule obj) {
        return ReplicationRule.builder()
                .id(obj.getId())
                .prefix(obj.getPrefix())
                .status(obj.getStatus())
                .priority(obj.getPriority())
                .filter(obj.getFilter() != null ? castReplicationRuleFilter(obj.getFilter()) : null)
                .deleteMarkerReplication(obj.getDeleteMarkerReplication() != null ?
                        castDeleteMarkerReplication(obj.getDeleteMarkerReplication()) : null)
                .destination(obj.getDestination() != null ? castDestination(obj.getDestination()) : null)
                .existingObjectReplication(obj.getExistingObjectReplication() != null ?
                        castExistingObjectReplication(obj.getExistingObjectReplication()) : null)
                .sourceSelectionCriteria(obj.getSourceSelectionCriteria() != null ?
                        castSourceSelectionCriteria(obj.getSourceSelectionCriteria()) : null).build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.ReplicationRule castS3ReplicationRule(ReplicationRule obj) {
        org.wso2.carbon.connector.amazons3.pojo.ReplicationRule obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ReplicationRule();
        obj1.setDeleteMarkerReplication(obj.deleteMarkerReplication() != null ?
                castS3DeleteMarkerReplication(obj.deleteMarkerReplication()) : null);
        obj1.setDestination(obj.destination() != null ? castS3Destination(obj.destination()) : null);
        obj1.setExistingObjectReplication(obj.existingObjectReplication() != null ?
                castS3ExistingObjectReplication(obj.existingObjectReplication()) : null);
        obj1.setFilter(obj.filter() != null ? castS3ReplicationRuleFilter(obj.filter()) : null);
        obj1.setId(obj.id());
        if (obj.prefix() != null) {
            obj1.setPrefix(obj.prefix());
        }
        if (obj.priority() != null) {
            obj1.setPriority(obj.priority());
        }
        obj1.setSourceSelectionCriteria(obj.sourceSelectionCriteria() != null ?
                castS3SourceSelectionCriteria(obj.sourceSelectionCriteria()) : null);
        obj1.setStatus(obj.statusAsString());
        return obj1;
    }

    public ReplicationRuleFilter castReplicationRuleFilter(
            org.wso2.carbon.connector.amazons3.pojo.ReplicationRuleFilter obj) {
        return ReplicationRuleFilter.builder()
                .and(obj.getAnd() != null ? castReplicationRuleAndOperator(obj.getAnd()) : null)
                .prefix(obj.getPrefix())
                .tag(obj.getTag() != null ? castTag(obj.getTag()) : null)
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.ReplicationRuleFilter castS3ReplicationRuleFilter(
            ReplicationRuleFilter obj) {
        org.wso2.carbon.connector.amazons3.pojo.ReplicationRuleFilter obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ReplicationRuleFilter();
        obj1.setAnd(obj.and() != null ? castS3ReplicationRuleAndOperator(obj.and()) : null);
        obj1.setPrefix(obj.prefix());
        obj1.setTag(obj.tag() != null ? castS3Tag(obj.tag()) : null);
        return obj1;
    }

    public ReplicationRuleAndOperator castReplicationRuleAndOperator(
            org.wso2.carbon.connector.amazons3.pojo.ReplicationRuleAndOperator obj) {
        return ReplicationRuleAndOperator.builder()
                .prefix(obj.getPrefix())
                .tags(obj.getTags() != null ? castTags(obj.getTags()) : null)
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.ReplicationRuleAndOperator castS3ReplicationRuleAndOperator(
            ReplicationRuleAndOperator obj) {
        org.wso2.carbon.connector.amazons3.pojo.ReplicationRuleAndOperator obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ReplicationRuleAndOperator();
        obj1.setPrefix(obj.prefix());
        obj1.setTags(obj.tags() != null ? castS3Tags(obj.tags()) : null);
        return obj1;
    }

    public DeleteMarkerReplication castDeleteMarkerReplication(
            org.wso2.carbon.connector.amazons3.pojo.DeleteMarkerReplication obj) {
        return DeleteMarkerReplication.builder()
                .status(obj.getStatus())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.DeleteMarkerReplication castS3DeleteMarkerReplication(
            DeleteMarkerReplication obj) {
        org.wso2.carbon.connector.amazons3.pojo.DeleteMarkerReplication obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.DeleteMarkerReplication();
        obj1.setStatus(obj.statusAsString());
        return obj1;
    }

    public Destination castDestination(org.wso2.carbon.connector.amazons3.pojo.Destination obj) {
        return Destination.builder()
                .accessControlTranslation(obj.getAccessControlTranslation() != null ?
                        castAccessControlTranslation(obj.getAccessControlTranslation()) : null)
                .account(obj.getAccount())
                .bucket(obj.getBucket())
                .encryptionConfiguration(obj.getEncryptionConfiguration() != null ?
                        castEncryptionConfiguration(obj.getEncryptionConfiguration()) : null)
                .metrics(obj.getMetrics() != null ? castMetrics(obj.getMetrics()) : null)
                .replicationTime(obj.getReplicationTime() != null ?
                        castReplicationTime(obj.getReplicationTime()) : null)
                .storageClass(obj.getStorageClass())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.Destination castS3Destination(Destination obj) {
        org.wso2.carbon.connector.amazons3.pojo.Destination obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.Destination();
        obj1.setAccessControlTranslation(obj.accessControlTranslation() != null ?
                castS3AccessControlTranslation(obj.accessControlTranslation()) : null);
        obj1.setAccount(obj.account());
        obj1.setBucket(obj.bucket());
        obj1.setEncryptionConfiguration(obj.encryptionConfiguration() != null ?
                castS3EncryptionConfiguration(obj.encryptionConfiguration()) : null);
        obj1.setMetrics(obj.metrics() != null ? castS3Metrics(obj.metrics()) : null);
        obj1.setReplicationTime(obj.replicationTime() != null ? castS3ReplicationTime(obj.replicationTime()) : null);
        obj1.setStorageClass(obj.storageClassAsString());
        return obj1;
    }

    public ExistingObjectReplication castExistingObjectReplication(
            org.wso2.carbon.connector.amazons3.pojo.ExistingObjectReplication obj) {
        return ExistingObjectReplication.builder()
                .status(obj.getStatus())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.ExistingObjectReplication castS3ExistingObjectReplication(
            ExistingObjectReplication obj) {
        org.wso2.carbon.connector.amazons3.pojo.ExistingObjectReplication obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ExistingObjectReplication();
        obj1.setStatus(obj.statusAsString());
        return obj1;
    }

    public SourceSelectionCriteria castSourceSelectionCriteria(
            org.wso2.carbon.connector.amazons3.pojo.SourceSelectionCriteria obj) {
        return SourceSelectionCriteria.builder()
                .sseKmsEncryptedObjects(obj.getSseKmsEncryptedObjects() != null ?
                        castSseKmsEncryptedObjects(obj.getSseKmsEncryptedObjects()) : null)
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.SourceSelectionCriteria castS3SourceSelectionCriteria(
            SourceSelectionCriteria obj) {
        org.wso2.carbon.connector.amazons3.pojo.SourceSelectionCriteria obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.SourceSelectionCriteria();
        obj1.setSseKmsEncryptedObjects(obj.sseKmsEncryptedObjects() != null ?
                castS3SseKmsEncryptedObjects(obj.sseKmsEncryptedObjects()) : null);
        return obj1;
    }

    public AccessControlPolicy castAccessControlPolicy(
            org.wso2.carbon.connector.amazons3.pojo.AccessControlPolicy obj) {
        return AccessControlPolicy.builder()
                .grants(obj.getGrant() != null ? castGrants(obj.getGrant()) : null)
                .owner(obj.getOwner() != null ? castOwner(obj.getOwner()) : null)
                .build();
    }

    public AccessControlTranslation castAccessControlTranslation(
            org.wso2.carbon.connector.amazons3.pojo.AccessControlTranslation obj) {
        return AccessControlTranslation.builder()
                .owner(obj.getOwner())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.AccessControlTranslation castS3AccessControlTranslation(
            AccessControlTranslation obj) {
        org.wso2.carbon.connector.amazons3.pojo.AccessControlTranslation obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.AccessControlTranslation();
        obj1.setOwner(obj.ownerAsString());
        return obj1;
    }

    public EncryptionConfiguration castEncryptionConfiguration(
            org.wso2.carbon.connector.amazons3.pojo.EncryptionConfiguration obj) {
        return EncryptionConfiguration.builder()
                .replicaKmsKeyID(obj.getReplicaKmsKeyID())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.EncryptionConfiguration castS3EncryptionConfiguration(
            EncryptionConfiguration obj) {
        org.wso2.carbon.connector.amazons3.pojo.EncryptionConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.EncryptionConfiguration();
        obj1.setReplicaKmsKeyID(obj.replicaKmsKeyID());
        return obj1;
    }

    public Metrics castMetrics(org.wso2.carbon.connector.amazons3.pojo.Metrics obj) {
        return Metrics.builder()
                .status(obj.getStatus())
                .eventThreshold(obj.getEventThreshold() != null ?
                        castReplicationTimeValue(obj.getEventThreshold()) : null)
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.Metrics castS3Metrics(Metrics obj) {
        org.wso2.carbon.connector.amazons3.pojo.Metrics obj1 = new org.wso2.carbon.connector.amazons3.pojo.Metrics();
        obj1.setEventThreshold(obj.eventThreshold() != null ? castS3ReplicationTimeValue(obj.eventThreshold()) : null);
        obj1.setStatus(obj.statusAsString());
        return obj1;
    }

    public ReplicationTime castReplicationTime(org.wso2.carbon.connector.amazons3.pojo.ReplicationTime obj) {
        return ReplicationTime.builder()
                .status(obj.getStatus())
                .time(obj.getTime() != null ? castReplicationTimeValue(obj.getTime()) : null)
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.ReplicationTime castS3ReplicationTime(ReplicationTime obj) {
        org.wso2.carbon.connector.amazons3.pojo.ReplicationTime obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ReplicationTime();
        obj1.setStatus(obj.statusAsString());
        obj1.setTime(obj.time() != null ? castS3ReplicationTimeValue(obj.time()) : null);
        return obj1;
    }

    public SseKmsEncryptedObjects castSseKmsEncryptedObjects(
            org.wso2.carbon.connector.amazons3.pojo.SseKmsEncryptedObjects obj) {
        return SseKmsEncryptedObjects.builder()
                .status(obj.getStatus() != null ? obj.getStatus() : null)
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.SseKmsEncryptedObjects castS3SseKmsEncryptedObjects(
            SseKmsEncryptedObjects obj) {
        org.wso2.carbon.connector.amazons3.pojo.SseKmsEncryptedObjects obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.SseKmsEncryptedObjects();
        obj1.setStatus(obj.statusAsString());
        return obj1;
    }

    public ReplicationTimeValue castReplicationTimeValue(
            org.wso2.carbon.connector.amazons3.pojo.ReplicationTimeValue obj) {
        return ReplicationTimeValue.builder()
                .minutes(obj.getMinutes())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.ReplicationTimeValue castS3ReplicationTimeValue(
            ReplicationTimeValue obj) {
        org.wso2.carbon.connector.amazons3.pojo.ReplicationTimeValue obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ReplicationTimeValue();
        if (obj.minutes() != null) {
            obj1.setMinutes(obj.minutes());
        }
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.LoggingEnabled castS3LoggingEnabled(LoggingEnabled obj) {
        org.wso2.carbon.connector.amazons3.pojo.LoggingEnabled obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.LoggingEnabled();
        obj1.setTargetBucket(obj.targetBucket());
        obj1.setTargetPrefix(obj.targetPrefix().toString());
        obj1.setTargetGrants(obj.targetGrants() != null ? castS3TargetGrants(obj.targetGrants()) : null);
        return obj1;
    }

    public List<Grant> castGrants(List<org.wso2.carbon.connector.amazons3.pojo.Grant> obj) {
        List<Grant> grants = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.Grant grant : obj) {
            if (grant != null) {
                grants.add(castGrant(grant));
            }
        }
        return grants;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.TargetGrant> castS3TargetGrants(List<TargetGrant> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.TargetGrant> s3TargetGrants = new ArrayList<>();
        for (TargetGrant grant : obj) {
            if (grant != null) {
                s3TargetGrants.add(castS3TargetGrant(grant));
            }
        }
        return s3TargetGrants;
    }

    public Grant castGrant(org.wso2.carbon.connector.amazons3.pojo.Grant obj) {
        return Grant.builder()
                .grantee(obj.getGrantee() != null ? castGrantee(obj.getGrantee()) : null)
                .permission(obj.getPermission())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.Grant castS3Grant(Grant obj) {
        org.wso2.carbon.connector.amazons3.pojo.Grant obj1 = new org.wso2.carbon.connector.amazons3.pojo.Grant();
        obj1.setPermission(obj.permissionAsString());
        obj1.setGrantee(castS3Grantee(obj.grantee()));
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.TargetGrant castS3TargetGrant(TargetGrant obj) {
        org.wso2.carbon.connector.amazons3.pojo.TargetGrant obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.TargetGrant();
        obj1.setPermission(obj.permissionAsString());
        obj1.setGrantee(castS3Grantee(obj.grantee()));
        return obj1;
    }

    public Grantee castGrantee(org.wso2.carbon.connector.amazons3.pojo.Grantee obj) {
        return Grantee.builder()
                .emailAddress(obj.getEmailAddress())
                .displayName(obj.getDisplayName())
                .id(obj.getId())
                .type(obj.getType())
                .uri(obj.getUri())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.Grantee castS3Grantee(Grantee obj) {
        org.wso2.carbon.connector.amazons3.pojo.Grantee obj1 = new org.wso2.carbon.connector.amazons3.pojo.Grantee();
        obj1.setDisplayName(obj.displayName());
        obj1.setEmailAddress(obj.emailAddress());
        obj1.setId(obj.id());
        obj1.setType(obj.typeAsString());
        obj1.setUri(obj.uri());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.NotificationConfiguration castS3NotificationConfiguration(
            GetBucketNotificationConfigurationResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.NotificationConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.NotificationConfiguration();
        obj1.setLambdaFunctionConfigurations(obj.lambdaFunctionConfigurations() != null ?
                castS3LambdaFunctionConfigurations(obj.lambdaFunctionConfigurations()) : null);
        obj1.setQueueConfigurations(obj.queueConfigurations() != null ?
                castS3QueueConfigurations(obj.queueConfigurations()) : null);
        obj1.setTopicConfigurations(obj.topicConfigurations() != null ?
                castS3TopicConfigurations(obj.topicConfigurations()) : null);
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.LambdaFunctionConfiguration> castS3LambdaFunctionConfigurations(
            List<LambdaFunctionConfiguration> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.LambdaFunctionConfiguration> s3Configuration = new ArrayList<>();
        for (LambdaFunctionConfiguration config : obj) {
            if (config != null) {
                s3Configuration.add(castS3LambdaFunctionConfiguration(config));
            }
        }
        return s3Configuration;
    }

    public org.wso2.carbon.connector.amazons3.pojo.LambdaFunctionConfiguration castS3LambdaFunctionConfiguration(
            LambdaFunctionConfiguration obj) {
        org.wso2.carbon.connector.amazons3.pojo.LambdaFunctionConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.LambdaFunctionConfiguration();
        obj1.setEvents(obj.eventsAsStrings());
        obj1.setId(obj.id());
        obj1.setLambdaFunctionArn(obj.lambdaFunctionArn());
        obj1.setNotificationConfigurationFilter(obj.filter() != null ?
                castS3NotificationConfigurationFilter(obj.filter()) : null);
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.NotificationConfigurationFilter castS3NotificationConfigurationFilter(
            NotificationConfigurationFilter obj) {
        org.wso2.carbon.connector.amazons3.pojo.NotificationConfigurationFilter obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.NotificationConfigurationFilter();
        obj1.setKey(obj.key() != null ? castS3KeyFilter(obj.key()) : null);
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.S3KeyFilter castS3KeyFilter(S3KeyFilter obj) {
        org.wso2.carbon.connector.amazons3.pojo.S3KeyFilter obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.S3KeyFilter();
        obj1.setFilterRules(castS3FilterRules(obj.filterRules()));
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.FilterRule> castS3FilterRules(List<FilterRule> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.FilterRule> s3Configuration = new ArrayList<>();
        for (FilterRule config : obj) {
            if (config != null) {
                s3Configuration.add(castS3FilterRule(config));
            }
        }
        return s3Configuration;
    }

    public org.wso2.carbon.connector.amazons3.pojo.FilterRule castS3FilterRule(FilterRule obj) {
        org.wso2.carbon.connector.amazons3.pojo.FilterRule obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.FilterRule();
        obj1.setName(obj.nameAsString());
        obj1.setValue(obj.value());
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.QueueConfiguration> castS3QueueConfigurations(
            List<QueueConfiguration> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.QueueConfiguration> s3Configuration = new ArrayList<>();
        for (QueueConfiguration config : obj) {
            if (config != null) {
                s3Configuration.add(castS3QueueConfiguration(config));
            }
        }
        return s3Configuration;
    }

    public org.wso2.carbon.connector.amazons3.pojo.QueueConfiguration castS3QueueConfiguration(QueueConfiguration obj) {
        org.wso2.carbon.connector.amazons3.pojo.QueueConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.QueueConfiguration();
        obj1.setEvents(obj.eventsAsStrings());
        obj1.setId(obj.id());
        obj1.setNotificationConfigurationFilter(obj.filter() != null ?
                castS3NotificationConfigurationFilter(obj.filter()) : null);
        obj1.setQueueArn(obj.queueArn());
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.TopicConfiguration> castS3TopicConfigurations(
            List<TopicConfiguration> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.TopicConfiguration> s3Configuration = new ArrayList<>();
        for (TopicConfiguration config : obj) {
            if (config != null) {
                s3Configuration.add(castS3TopicConfiguration(config));
            }
        }
        return s3Configuration;
    }

    public org.wso2.carbon.connector.amazons3.pojo.TopicConfiguration castS3TopicConfiguration(TopicConfiguration obj) {
        org.wso2.carbon.connector.amazons3.pojo.TopicConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.TopicConfiguration();
        obj1.setEvents(obj.eventsAsStrings());
        obj1.setId(obj.id());
        obj1.setNotificationConfigurationFilter(obj.filter() != null ?
                castS3NotificationConfigurationFilter(obj.filter()) : null);
        obj1.setTopicArn(obj.topicArn());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.ObjectVersionConfiguration castS3ObjectVersions(
            ListObjectVersionsResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.ObjectVersionConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ObjectVersionConfiguration();
        obj1.setCommonPrefixes(obj.commonPrefixes() != null ? castS3CommonPrefixes(obj.commonPrefixes()) : null);
        obj1.setDeleteMarkers(obj.deleteMarkers() != null ? castS3DeleteMarkers(obj.deleteMarkers()) : null);
        obj1.setDelimiter(obj.delimiter());
        obj1.setEncodingType(obj.encodingTypeAsString());
        obj1.setKeyMarker(obj.keyMarker());
        if (obj.maxKeys() != null) {
            obj1.setMaxKeys(obj.maxKeys());
        }
        obj1.setName(obj.name());
        obj1.setNextKeyMarker(obj.nextKeyMarker());
        obj1.setNextVersionIdMarker(obj.nextVersionIdMarker());
        obj1.setPrefix(obj.prefix());
        if (obj.isTruncated() != null) {
            obj1.setTruncated(obj.isTruncated());
        }
        obj1.setVersionIdMarker(obj.versionIdMarker());
        obj1.setVersions(obj.versions() != null ? castS3Versions(obj.versions()) : null);
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.CommonPrefix> castS3CommonPrefixes(List<CommonPrefix> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.CommonPrefix> s3CommonPrefix = new ArrayList<>();
        for (CommonPrefix prefix : obj) {
            if (prefix != null) {
                s3CommonPrefix.add(castS3CommonPrefix(prefix));
            }
        }
        return s3CommonPrefix;
    }

    public org.wso2.carbon.connector.amazons3.pojo.CommonPrefix castS3CommonPrefix(CommonPrefix obj) {
        org.wso2.carbon.connector.amazons3.pojo.CommonPrefix obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.CommonPrefix();
        obj1.setPrefix(obj.prefix());
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.DeleteMarkerEntry> castS3DeleteMarkers(
            List<DeleteMarkerEntry> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.DeleteMarkerEntry> s3DeleteMarkerEntry = new ArrayList<>();
        for (DeleteMarkerEntry entry : obj) {
            if (entry != null) {
                s3DeleteMarkerEntry.add(castS3DeleteMarker(entry));
            }
        }
        return s3DeleteMarkerEntry;
    }

    public org.wso2.carbon.connector.amazons3.pojo.DeleteMarkerEntry castS3DeleteMarker(DeleteMarkerEntry obj) {
        org.wso2.carbon.connector.amazons3.pojo.DeleteMarkerEntry obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.DeleteMarkerEntry();
        if (obj.isLatest() != null) {
            obj1.setIsLatest(obj.isLatest());
        }
        obj1.setKey(obj.key());
        obj1.setLastModified(obj.lastModified());
        obj1.setOwner(obj.owner() != null ? castS3Owner(obj.owner()) : null);
        obj1.setVersionId(obj.versionId());
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.ObjectVersion> castS3Versions(List<ObjectVersion> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.ObjectVersion> s3Versions = new ArrayList<>();
        for (ObjectVersion version : obj) {
            if (version != null) {
                s3Versions.add(castS3Version(version));
            }
        }
        return s3Versions;
    }

    public org.wso2.carbon.connector.amazons3.pojo.ObjectVersion castS3Version(ObjectVersion obj) {
        org.wso2.carbon.connector.amazons3.pojo.ObjectVersion obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.ObjectVersion();
        obj1.setETag(obj.eTag());
        obj1.setKey(obj.key());
        obj1.setLastModified(obj.lastModified());
        if (obj.isLatest() != null) {
            obj1.setLatest(obj.isLatest());
        }
        obj1.setSize(obj.size());
        obj1.setStorageClass(obj.storageClassAsString());
        obj1.setVersionId(obj.versionId());
        obj1.setOwner(obj.owner() != null ? castS3Owner(obj.owner()) : null);
        return obj1;
    }

    public Owner castOwner(org.wso2.carbon.connector.amazons3.pojo.Owner obj) {
        return Owner.builder()
                .displayName(obj.getDisplayName())
                .id(obj.getId())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.Owner castS3Owner(Owner obj) {
        org.wso2.carbon.connector.amazons3.pojo.Owner obj1 = new org.wso2.carbon.connector.amazons3.pojo.Owner();
        obj1.setDisplayName(obj.displayName());
        obj1.setId(obj.id());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.PartsConfiguration castS3PartsConfiguration(ListPartsResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.PartsConfiguration obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.PartsConfiguration();
        obj1.setAbortDate(obj.abortDate());
        obj1.setAbortRuleId(obj.abortRuleId());
        obj1.setBucket(obj.bucket());
        obj1.setInitiator(obj.initiator() != null ? castS3Initiator(obj.initiator()) : null);
        obj1.setKey(obj.key());
        obj1.setMaxParts(obj.maxParts());
        obj1.setNextPartNumberMarker(obj.nextPartNumberMarker());
        obj1.setOwner(obj.owner() != null ? castS3Owner(obj.owner()) : null);
        obj1.setParts(obj.parts() != null ? castS3Parts(obj.parts()) : null);
        obj1.setPartNumberMarker(obj.partNumberMarker());
        obj1.setRequestCharged(obj.requestChargedAsString());
        obj1.setStorageClass(obj.storageClassAsString());
        obj1.setTruncated(obj.isTruncated());
        obj1.setUploadId(obj.uploadId());
        return obj1;
    }

    public List<Part> castParts(List<org.wso2.carbon.connector.amazons3.pojo.Part> obj) {
        List<Part> parts = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.Part part : obj) {
            if (part != null) {
                parts.add(castPart(part));
            }
        }
        return parts;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.Part> castS3Parts(List<Part> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.Part> s3Parts = new ArrayList<>();
        for (Part part : obj) {
            if (part != null) {
                s3Parts.add(castS3Part(part));
            }
        }
        return s3Parts;
    }

    public List<CompletedPart> castCompletedParts(List<org.wso2.carbon.connector.amazons3.pojo.CompletedPart> obj) {
        List<CompletedPart> completedParts = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.CompletedPart completedPart : obj) {
            if (completedPart != null) {
                completedParts.add(castCompletedPart(completedPart));
            }
        }
        return completedParts;
    }

    public CompletedPart castCompletedPart(org.wso2.carbon.connector.amazons3.pojo.CompletedPart obj) {
        return CompletedPart.builder()
                .eTag(obj.getETag())
                .partNumber(obj.getPartNumber())
                .build();
    }

    public Part castPart(org.wso2.carbon.connector.amazons3.pojo.Part obj) {
        return Part.builder()
                .eTag(obj.getETag())
                .lastModified(obj.getLastModified())
                .partNumber(obj.getPartNumber())
                .size(obj.getSize())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.Part castS3Part(Part obj) {
        org.wso2.carbon.connector.amazons3.pojo.Part obj1 = new org.wso2.carbon.connector.amazons3.pojo.Part();
        obj1.setETag(obj.eTag());
        obj1.setLastModified(obj.lastModified());
        if (obj.partNumber() != null) {
            obj1.setPartNumber(obj.partNumber());
        }
        obj1.setSize(obj.size());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.Initiator castS3Initiator(Initiator obj) {
        org.wso2.carbon.connector.amazons3.pojo.Initiator obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.Initiator();
        obj1.setDisplayName(obj.displayName());
        obj1.setId(obj.id());
        return obj1;
    }

    public List<ObjectIdentifier> castObjectIdentifiers(
            List<org.wso2.carbon.connector.amazons3.pojo.ObjectIdentifier> obj) {
        List<ObjectIdentifier> identifiers = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.ObjectIdentifier identifier : obj) {
            if (identifier != null) {
                identifiers.add(castObjectIdentifier(identifier));
            }
        }
        return identifiers;
    }

    public ObjectIdentifier castObjectIdentifier(org.wso2.carbon.connector.amazons3.pojo.ObjectIdentifier obj) {
        return ObjectIdentifier.builder()
                .key(obj.getKey())
                .versionId(obj.getVersionId())
                .build();
    }
    public org.wso2.carbon.connector.amazons3.pojo.GetObjectResponse castS3GetObjectResponseWithContent(
            ResponseBytes<GetObjectResponse> responseBytes, String isContentAsBase64) {

        org.wso2.carbon.connector.amazons3.pojo.GetObjectResponse obj1 =
                castS3GetObjectResponse(responseBytes.response());
        if ("true".equalsIgnoreCase(isContentAsBase64)) {
            byte[] objectBytes = responseBytes.asByteArray();

            // Convert PDF bytes to base64
            String base64String = Base64.getEncoder().encodeToString(objectBytes);
            obj1.setContent(base64String);
        } else {
            ContentType contentType = ContentType.parse(responseBytes.response().contentType());
            Charset charset = contentType.getCharset();
            if (charset == null) {
                charset = StandardCharsets.UTF_8;
            }
            obj1.setContent(responseBytes.asString(charset));
        }
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.GetObjectResponse castS3GetObjectResponse(GetObjectResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.GetObjectResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.GetObjectResponse();
        obj1.setAcceptRanges(obj.acceptRanges());
        obj1.setCacheControl(obj.cacheControl());
        obj1.setContentDisposition(obj.contentDisposition());
        obj1.setContentEncoding(obj.contentEncoding());
        obj1.setContentLanguage(obj.contentLanguage());
        obj1.setContentLength(obj.contentLength());
        obj1.setContentRange(obj.contentRange());
        obj1.setContentType(obj.contentType());
        if (obj.deleteMarker() != null) {
            obj1.setDeleteMarker(obj.deleteMarker());
        }
        obj1.setETag(obj.eTag());
        obj1.setExpiration(obj.expiration());
        obj1.setExpires(obj.expires());
        obj1.setLastModified(obj.lastModified());
        obj1.setMetadata(obj.metadata());
        if (obj.missingMeta() != null) {
            obj1.setMissingMeta(obj.missingMeta());
        }
        obj1.setObjectLockLegalHoldStatus(obj.objectLockLegalHoldStatusAsString());
        obj1.setObjectLockMode(obj.objectLockModeAsString());
        obj1.setObjectLockRetainUntilDate(obj.objectLockRetainUntilDate());
        if (obj.partsCount() != null) {
            obj1.setPartsCount(obj.partsCount());
        }
        obj1.setReplicationStatus(obj.replicationStatusAsString());
        obj1.setRequestCharged(obj.requestChargedAsString());
        obj1.setRestore(obj.restore());
        obj1.setServerSideEncryption(obj.serverSideEncryptionAsString());
        obj1.setSseCustomerAlgorithm(obj.sseCustomerAlgorithm());
        obj1.setSseCustomerKeyMD5(obj.sseCustomerKeyMD5());
        obj1.setSsekmsKeyId(obj.ssekmsKeyId());
        obj1.setStorageClass(obj.storageClassAsString());
        if (obj.tagCount() != null) {
            obj1.setTagCount(obj.tagCount());
        }
        obj1.setVersionId(obj.versionId());
        obj1.setWebsiteRedirectLocation(obj.websiteRedirectLocation());
        return obj1;
    }

    public RestoreRequest castRestoreRequest(org.wso2.carbon.connector.amazons3.pojo.RestoreRequest obj) {
        return RestoreRequest.builder()
                .outputLocation(obj.getOutputLocation() != null ? castOutputLocation(obj.getOutputLocation()) : null)
                .days(obj.getDays())
                .description(obj.getDescription())
                .glacierJobParameters(obj.getGlacierJobParameters() != null ? castGlacierJobParameters(obj.getGlacierJobParameters()) : null)
                .selectParameters(obj.getSelectParameters() != null ? castSelectParameters(obj.getSelectParameters()) : null)
                .tier(obj.getTier())
                .type(obj.getType())
                .build();
    }

    public OutputLocation castOutputLocation(org.wso2.carbon.connector.amazons3.pojo.OutputLocation obj) {
        return OutputLocation.builder()
                .s3(obj.getS3Location() != null ? castS3Location(obj.getS3Location()) : null)
                .build();
    }

    public S3Location castS3Location(org.wso2.carbon.connector.amazons3.pojo.S3Location obj) {
        return S3Location.builder()
                .accessControlList(obj.getAccessControlList() != null ? castGrants(obj.getAccessControlList()) : null)
                .bucketName(obj.getBucketName())
                .cannedACL(obj.getCannedACL())
                .encryption(obj.getEncryption() != null ? castEncryption(obj.getEncryption()) : null)
                .prefix(obj.getPrefix())
                .storageClass(obj.getStorageClass())
                .tagging(obj.getTagging() != null ? castTagging(obj.getTagging()) : null)
                .userMetadata(obj.getUserMetadata() != null ? castUserMetadataEntries(obj.getUserMetadata()) : null)
                .build();
    }

    public Encryption castEncryption(org.wso2.carbon.connector.amazons3.pojo.Encryption obj) {
        return Encryption.builder()
                .encryptionType(obj.getEncryptionType())
                .kmsContext(obj.getKmsContext())
                .kmsKeyId(obj.getKmsKeyId())
                .build();
    }

    public Tagging castTagging(org.wso2.carbon.connector.amazons3.pojo.TagConfiguration obj) {
        List<Tag> tags = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.Tag tag : obj.getTags()) {
            if (tag != null) {
                tags.add(castTag(tag));
            }
        }
        return Tagging.builder().tagSet(tags).build();
    }

    public List<MetadataEntry> castUserMetadataEntries(
            List<org.wso2.carbon.connector.amazons3.pojo.MetadataEntry> obj) {
        List<MetadataEntry> entries = new ArrayList<>();
        for (org.wso2.carbon.connector.amazons3.pojo.MetadataEntry entry : obj) {
            if (entry != null) {
                entries.add(castUserMetadata(entry));
            }
        }
        return entries;
    }

    public MetadataEntry castUserMetadata(org.wso2.carbon.connector.amazons3.pojo.MetadataEntry obj) {
        return MetadataEntry.builder()
                .name(obj.getName())
                .value(obj.getValue())
                .build();
    }

    public SelectParameters castSelectParameters(org.wso2.carbon.connector.amazons3.pojo.SelectParameters obj) {
        return SelectParameters.builder()
                .expression(obj.getExpression())
                .expressionType(obj.getExpressionType())
                .inputSerialization(obj.getInputSerialization() != null ?
                        castInputSerialization(obj.getInputSerialization()) : null)
                .outputSerialization(obj.getOutputSerialization() != null ?
                        castOutputSerialization(obj.getOutputSerialization()) : null)
                .build();
    }

    public InputSerialization castInputSerialization(org.wso2.carbon.connector.amazons3.pojo.InputSerialization obj) {
        return InputSerialization.builder()
                .compressionType(obj.getCompressionType())
                .csv(obj.getCSVInput() != null ? castCSVInput(obj.getCSVInput()) : null)
                .json(obj.getJSONInput() != null ? castJSONInput(obj.getJSONInput()) : null)
                .parquet(ParquetInput.builder().build())
                .build();
    }

    public OutputSerialization castOutputSerialization(
            org.wso2.carbon.connector.amazons3.pojo.OutputSerialization obj) {
        return OutputSerialization.builder()
                .csv(obj.getCSVOutput() != null ? castCSVOutput(obj.getCSVOutput()) : null)
                .json(obj.getJSONOutput() != null ? castJSONOutput(obj.getJSONOutput()) : null)
                .build();
    }

    public GlacierJobParameters castGlacierJobParameters(
            org.wso2.carbon.connector.amazons3.pojo.GlacierJobParameters obj) {
        return GlacierJobParameters.builder()
                .tier(obj.getTier())
                .build();
    }

    public CSVInput castCSVInput(org.wso2.carbon.connector.amazons3.pojo.CSVInput obj) {
        return CSVInput.builder()
                .allowQuotedRecordDelimiter(obj.isAllowQuotedRecordDelimiter())
                .comments(obj.getComments())
                .fieldDelimiter(obj.getFieldDelimiter())
                .fileHeaderInfo(obj.getFileHeaderInfo())
                .quoteCharacter(obj.getQuoteCharacter())
                .quoteEscapeCharacter(obj.getQuoteEscapeCharacter())
                .recordDelimiter(obj.getRecordDelimiter())
                .build();
    }

    public JSONInput castJSONInput(org.wso2.carbon.connector.amazons3.pojo.JSONInput obj) {
        return JSONInput.builder()
                .type(obj.getType())
                .build();
    }

    public CSVOutput castCSVOutput(org.wso2.carbon.connector.amazons3.pojo.CSVOutput obj) {
        return CSVOutput.builder()
                .fieldDelimiter(obj.getFieldDelimiter())
                .quoteCharacter(obj.getQuoteCharacter())
                .quoteEscapeCharacter(obj.getQuoteEscapeCharacter())
                .quoteFields(obj.getQuoteFields())
                .recordDelimiter(obj.getRecordDelimiter())
                .build();
    }

    public JSONOutput castJSONOutput(org.wso2.carbon.connector.amazons3.pojo.JSONOutput obj) {
        return JSONOutput.builder()
                .recordDelimiter(obj.getRecordDelimiter())
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.HeadObjectResponse castS3HeadObjectResponse(HeadObjectResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.HeadObjectResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.HeadObjectResponse();
        obj1.setAcceptRanges(obj.acceptRanges());
        obj1.setCacheControl(obj.cacheControl());
        obj1.setContentDisposition(obj.contentDisposition());
        obj1.setContentEncoding(obj.contentEncoding());
        obj1.setContentLanguage(obj.contentLanguage());
        obj1.setContentLength(obj.contentLength());
        obj1.setContentType(obj.contentType());
        if (obj.deleteMarker() != null) {
            obj1.setDeleteMarker(obj.deleteMarker());
        }
        obj1.setETag(obj.eTag());
        obj1.setExpiration(obj.expiration());
        obj1.setExpires(obj.expires());
        obj1.setLastModified(obj.lastModified());
        obj1.setMetadata(obj.metadata());
        if (obj.missingMeta() != null) {
            obj1.setMissingMeta(obj.missingMeta());
        }
        obj1.setObjectLockLegalHoldStatus(obj.objectLockLegalHoldStatusAsString());
        obj1.setObjectLockMode(obj.objectLockModeAsString());
        obj1.setObjectLockRetainUntilDate(obj.objectLockRetainUntilDate());
        if (obj.partsCount() != null) {
            obj1.setPartsCount(obj.partsCount());
        }
        obj1.setReplicationStatus(obj.replicationStatusAsString());
        obj1.setRequestCharged(obj.requestChargedAsString());
        obj1.setRestore(obj.restore());
        obj1.setServerSideEncryption(obj.serverSideEncryptionAsString());
        obj1.setSseCustomerAlgorithm(obj.sseCustomerAlgorithm());
        obj1.setSseCustomerKeyMD5(obj.sseCustomerKeyMD5());
        obj1.setSsekmsKeyId(obj.ssekmsKeyId());
        obj1.setStorageClass(obj.storageClassAsString());
        obj1.setVersionId(obj.versionId());
        obj1.setWebsiteRedirectLocation(obj.websiteRedirectLocation());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.CompleteMultipartUploadResponse castS3CompleteMultipartUploadResponse(
            CompleteMultipartUploadResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.CompleteMultipartUploadResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.CompleteMultipartUploadResponse();
        obj1.setBucket(obj.bucket());
        obj1.setKey(obj.key());
        obj1.setLocation(obj.location());
        obj1.setETag(obj.eTag());
        obj1.setExpiration(obj.expiration());
        obj1.setRequestCharged(obj.requestChargedAsString());
        obj1.setServerSideEncryption(obj.serverSideEncryptionAsString());
        obj1.setSsekmsKeyId(obj.ssekmsKeyId());
        obj1.setVersionId(obj.versionId());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.UploadPartResponse castS3UploadPartResponse(UploadPartResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.UploadPartResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.UploadPartResponse();
        obj1.setETag(obj.eTag());
        obj1.setRequestCharged(obj.requestChargedAsString());
        obj1.setServerSideEncryption(obj.serverSideEncryptionAsString());
        obj1.setSsekmsKeyId(obj.ssekmsKeyId());
        obj1.setSseCustomerAlgorithm(obj.sseCustomerAlgorithm());
        obj1.setSseCustomerKeyMD5(obj.sseCustomerKeyMD5());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.UploadPartCopyResponse castS3UploadPartCopyResponse(
            UploadPartCopyResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.UploadPartCopyResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.UploadPartCopyResponse();
        obj1.setCopyPartResult(obj.copyPartResult() != null ? castS3CopyPartResult(obj.copyPartResult()) : null);
        obj1.setCopySourceVersionId(obj.copySourceVersionId());
        obj1.setRequestCharged(obj.requestChargedAsString());
        obj1.setServerSideEncryption(obj.serverSideEncryptionAsString());
        obj1.setSsekmsKeyId(obj.ssekmsKeyId());
        obj1.setSseCustomerAlgorithm(obj.sseCustomerAlgorithm());
        obj1.setSseCustomerKeyMD5(obj.sseCustomerKeyMD5());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.CopyPartResult castS3CopyPartResult(CopyPartResult obj) {
        org.wso2.carbon.connector.amazons3.pojo.CopyPartResult obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.CopyPartResult();
        obj1.setETag(obj.eTag());
        obj1.setLastModified(obj.lastModified());
        return obj1;
    }

    public Delete castDeleteConfig(org.wso2.carbon.connector.amazons3.pojo.Delete obj) {
        return Delete.builder()
                .quiet(obj.isQuiet())
                .objects(obj.getObjectIdentifiers() != null ? castObjectIdentifiers(obj.getObjectIdentifiers()) : null)
                .build();
    }

    public org.wso2.carbon.connector.amazons3.pojo.CreateMultipartUploadResponse castS3CreateMultipartUploadResponse(
            CreateMultipartUploadResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.CreateMultipartUploadResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.CreateMultipartUploadResponse();
        obj1.setAbortDate(obj.abortDate());
        obj1.setAbortRuleId(obj.abortRuleId());
        obj1.setBucket(obj.bucket());
        obj1.setKey(obj.key());
        obj1.setUploadId(obj.uploadId());
        obj1.setSsekmsEncryptionContext(obj.ssekmsEncryptionContext());
        obj1.setRequestCharged(obj.requestChargedAsString());
        obj1.setServerSideEncryption(obj.serverSideEncryptionAsString());
        obj1.setSsekmsKeyId(obj.ssekmsKeyId());
        obj1.setSseCustomerAlgorithm(obj.sseCustomerAlgorithm());
        obj1.setSseCustomerKeyMD5(obj.sseCustomerKeyMD5());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.DeleteObjectsResponse castS3DeleteObjectsResponse(
            DeleteObjectsResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.DeleteObjectsResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.DeleteObjectsResponse();
        obj1.setDeleted(obj.deleted() != null ? castS3DeletedObjects(obj.deleted()) : null);
        obj1.setErrors(obj.errors() != null ? castS3Errors(obj.errors()) : null);
        obj1.setRequestCharged(obj.requestChargedAsString());
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.DeletedObject> castS3DeletedObjects(List<DeletedObject> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.DeletedObject> objects = new ArrayList<>();
        for (DeletedObject object : obj) {
            if (object != null) {
                objects.add(castS3DeletedObject(object));
            }
        }
        return objects;
    }

    public org.wso2.carbon.connector.amazons3.pojo.DeletedObject castS3DeletedObject(DeletedObject obj) {
        org.wso2.carbon.connector.amazons3.pojo.DeletedObject obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.DeletedObject();
        if (obj.deleteMarker() != null) {
            obj1.setDeleteMarker(obj.deleteMarker());
        }
        obj1.setDeleteMarkerVersionId(obj.deleteMarkerVersionId());
        obj1.setKey(obj.key());
        obj1.setVersionId(obj.versionId());
        return obj1;
    }

    public List<org.wso2.carbon.connector.amazons3.pojo.S3Error> castS3Errors(List<S3Error> obj) {
        List<org.wso2.carbon.connector.amazons3.pojo.S3Error> objects = new ArrayList<>();
        for (S3Error object : obj) {
            if (object != null) {
                objects.add(castS3Error(object));
            }
        }
        return objects;
    }

    public org.wso2.carbon.connector.amazons3.pojo.S3Error castS3Error(S3Error obj) {
        org.wso2.carbon.connector.amazons3.pojo.S3Error obj1 = new org.wso2.carbon.connector.amazons3.pojo.S3Error();
        obj1.setCode(obj.code());
        obj1.setMessage(obj.message());
        obj1.setKey(obj.key());
        obj1.setVersionId(obj.versionId());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.DeleteObjectResponse castS3DeleteObjectResponse(
            DeleteObjectResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.DeleteObjectResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.DeleteObjectResponse();
        if (obj.deleteMarker() != null) {
            obj1.setDeleteMarker(obj.deleteMarker());
        }
        obj1.setRequestCharged(obj.requestChargedAsString());
        obj1.setVersionId(obj.versionId());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.PutObjectResponse castS3PutObjectResponse(PutObjectResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.PutObjectResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.PutObjectResponse();
        obj1.setETag(obj.eTag());
        obj1.setExpiration(obj.expiration());
        obj1.setRequestCharged(obj.requestChargedAsString());
        obj1.setServerSideEncryption(obj.serverSideEncryptionAsString());
        obj1.setSseCustomerAlgorithm(obj.sseCustomerAlgorithm());
        obj1.setSseCustomerKeyMD5(obj.sseCustomerKeyMD5());
        obj1.setSsekmsKeyId(obj.ssekmsKeyId());
        obj1.setSsekmsEncryptionContext(obj.ssekmsEncryptionContext());
        obj1.setVersionId(obj.versionId());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.CopyObjectResponse castS3CopyObjectResponse(CopyObjectResponse obj) {
        org.wso2.carbon.connector.amazons3.pojo.CopyObjectResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.CopyObjectResponse();
        obj1.setCopyObjectResult(obj.copyObjectResult() != null ?
                castS3CopyObjectResult(obj.copyObjectResult()) : null);
        obj1.setExpiration(obj.expiration());
        obj1.setCopySourceVersionId(obj.copySourceVersionId());
        obj1.setVersionId(obj.versionId());
        obj1.setServerSideEncryption(obj.serverSideEncryptionAsString());
        obj1.setSseCustomerAlgorithm(obj.sseCustomerAlgorithm());
        obj1.setSseCustomerKeyMD5(obj.sseCustomerKeyMD5());
        obj1.setSsekmsKeyId(obj.ssekmsKeyId());
        obj1.setSsekmsEncryptionContext(obj.ssekmsEncryptionContext());
        obj1.setRequestCharged(obj.requestChargedAsString());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.CopyObjectResult castS3CopyObjectResult(CopyObjectResult obj) {
        org.wso2.carbon.connector.amazons3.pojo.CopyObjectResult obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.CopyObjectResult();
        obj1.setETag(obj.eTag());
        obj1.setLastModified(obj.lastModified());
        return obj1;
    }

    public org.wso2.carbon.connector.amazons3.pojo.PutObjectPresignedUrlResponse castS3PutObjectPresignedUrlRequest(PresignedPutObjectRequest obj) {
        org.wso2.carbon.connector.amazons3.pojo.PutObjectPresignedUrlResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.PutObjectPresignedUrlResponse();
        obj1.setUrl(obj.url().toString());
        obj1.setExpiration(obj.expiration().toString());
        return obj1;
    }

    public GetObjectPresignedUrlResponse castS3GetObjectPresignedUrlRequest(PresignedGetObjectRequest obj) {
        org.wso2.carbon.connector.amazons3.pojo.GetObjectPresignedUrlResponse obj1 =
                new org.wso2.carbon.connector.amazons3.pojo.GetObjectPresignedUrlResponse();
        obj1.setUrl(obj.url().toString());
        obj1.setExpiration(obj.expiration().toString());
        return obj1;
    }
}
