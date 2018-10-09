# Working with Objects in Amazon S3
[[  Overview ]](#overview)  [[ Operation details ]](#operation-details)  [[  Sample configuration  ]](#sample-configuration)
### Overview
The following operations allow you to work with objects in Amazon S3. Click an operation name to see details on how to use it.

For a sample proxy service that illustrates how to work with objects, see [Sample configuration](#sample-configuration).

| Operation        | Description |
| ------------- |-------------|
| [deleteObject](#deleting-an-object)    |   Removes the null version (if there is one) of an object, and inserts a delete marker, which becomes the latest version of the object.   |
| [deleteMultipleObjects](#deleting-multiple-objects) | Deletes multiple objects from a bucket using a single HTTP request.   |
| [getObject](#getting-an-object) | Returns objects from Amazon S3.   |
| [createObject](#creating-an-object)  | Uploads an object to a bucket via an HTML form.   |
| [createObjectACL](#creating-an-object-acl)   | Sets the access control list (ACL) permissions for an object that already exists in a bucket. |
| [createObjectCopy](#creating-an-object-copy)  | Creates a copy of an object that is already stored in Amazon S3.  |
| [getObjectMetaData](#getting-object-metadata) | Retrieves metadata from an object without returning the object itself.    |
| [uploadPart](#uploading-a-part)    | Uploads a part for a current multipart upload.    |
| [completeMultipartUpload](#completing-a-multipart-upload)   | Completes a currently active multipart upload.
| [abortMultipartUpload](#aborting-multipart-upload)  | Aborts a currently active multipart upload.
| [listParts](#listing-parts) | Retrieves list of uploaded parts
| [initMultipartUpload](#initiating-multipart-upload)   | Initiates a multipart upload for Part uploads.    |
| [getObjectACL](#getting-the-access-control-list-of-an-object)  | Returns the access control list (ACL) of an object|
| [getObjectTorrent](#getting-torrent-files-from-a-bucket)  | Returns torrent files from a bucket.  |
| [restoreObject](#restoring-specific-object-version) | Restores a temporary copy of an archived object.  |
| [uploadPartCopy](#uploading-a-part-by-copying-data-from-an-existing-object)   | Uploads a part by copying data from an existing object as data source.    |
| [headObject](#retrieving-metadata-from-an-object-without-returning-the-object)    | Retrieves metadata from an object without returning the object itself.    |

## Operation details
This section provides details on each of the operations.

### Deleting an object
The deleteObject operation removes the null version (if there is one) of an object and inserts a delete marker, which becomes the latest version of the object. If there is no null version, Amazon S3 does not remove any objects.

If the object you want to delete is in a bucket where the bucket versioning configuration is MFA Delete enabled, you must include the xAmzMfa header in the request. Requests that include xAmzMfa must use HTTPS. For more information about MFA Delete, see Using MFA Delete .

Following is the proxy configuration for init and deleteObject. The init section has additional parameters and parameters that need to be removed apart from those mentioned in the Connecting to Amazon S3 section.

**deleteObject**
```
<amazons3.init>
    <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
    <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
    <methodType>{$ctx:methodType}</methodType>
    <contentType>{$ctx:contentType}</contentType>
    <bucketName>{$ctx:bucketName}</bucketName>
    <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
    <contentMD5>{$ctx:contentMD5}</contentMD5>
    <xAmzSecurityToken>{$ctx:xAmzSecurityToken}</xAmzSecurityToken>
    <host>{$ctx:host}</host>
    <region>{$ctx:region}</region>
    <expect>{$ctx:expect}</expect>
    <contentLength>{$ctx:contentLength}</contentLength>
    <xAmzMfa>{$ctx:xAmzMfa}</xAmzMfa>
</amazons3.init>

<amazons3.deleteObject>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <objectName>{$ctx:objectName}</objectName>
    <versionID>{$ctx:versionId}</versionID>
</amazons3.deleteObject>
```

**Properties**
* accessKeyId: AWS access key ID.
* secretAccessKey: AWS secret access key.
* methodType: HTTP method type.
* contentType: Content type of the resource.
* bucketName: Name of the bucket.
* isXAmzDate: Indicates whether the current date and time are considered to calculate the signature. Valid values: true or false
* contentMD5: Base64 encoded 128-bit MD5 digest of the message according to RFC 1864.
* xAmzSecurityToken: The security token based on whether Amazon DevPay operations or temporary security credentials are used.
* host: The path-style requests (s3.amazonaws.com) or virtual-style requests (BucketName.s3.amazonaws.com).
* region: Region which is used select a regional endpoint to make requests.
* expect: When this property is set to 100-continue, the request does not send the request body until it receives an acknowledgment. If the message is rejected based on the headers, the body of the message is not sent.
* contentLength: Length of the message without the headers according to RFC 2616.
* xAmzMfa: Required to permanently delete a versioned object if versioning is configured with MFA Delete enabled. The value is the concatenation of the authentication device's serial number, a space, and the value displayed on your authentication device.
* bucketUrl: The URL of the bucket.
* objectName: The name of the object.
* versionID: The version ID of the object.

> Note: To remove a specific version, the user must be the bucket owner and must use the versionId sub-resource, which permanently deletes the version.

**Sample request**
Following is a sample REST request that can be handled by the deleteObject operation.

```
<deleteObject>
    <accessKeyId>AKIAIGURKJSAZM7GJ7TRO6KQWER</accessKeyId>
    <secretAccessKey>asAXsdfsdf8CJoDKzeOd0Ve5dMCFk4STUFDRHkGX6m0CcYdsf</secretAccessKey>
    <methodType>DELETE</methodType>
    <contentLength></contentLength>
    <contentType>application/xml</contentType>
    <contentMD5></contentMD5>
    <expect>100-continue</expect>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <isXAmzDate>true</isXAmzDate>
    <xAmzMfa></xAmzMfa>
    <xAmzSecurityToken></xAmzSecurityToken>
    <objectName>testObject1</objectName>
    <versionId>FHbrL3xf2TK54hLNWWArYI79woSElvHf</versionId>
</deleteObject>
```

**Related Amazon S3 documentation**

[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETE.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETE.html)

### Deleting multiple objects
The deleteMultipleObjects operation deletes multiple objects from a bucket using a single HTTP request. If object keys that need to be deleted are known, this operation provides a suitable alternative to sending individual delete requests (deleteObject). The deleteMultipleObjects request contains a list of up to 1000 keys that the user wants to delete. In the XML, you provide the object key names, and optionally provide version IDs if you want to delete a specific version of the object from a versioning-enabled bucket. For each key, Amazon S3 performs a delete operation and returns the result of that deletion, success or failure, in the response. Note that if the object specified in the request is not found, Amazon S3 returns the result as deleted.

The deleteMultipleObjects operation supports two modes for the response: verbose and quiet. By default, the operation uses the verbose mode in which the response includes the result of deletion of each key in your request. In the quiet mode, the response includes only keys where the delete operation encountered an error. For a successful deletion, the operation does not return any information about the deletion in the response body.

When using the deleteMultipleObjects operation that attempts to delete a versioned object on an MFA Delete enabled bucket, you must include an MFA token. If you do not provide one, even if there are non-versioned objects you are attempting to delete. Additionally, f you provide an invalid token, the entire request will fail, regardless of whether there are versioned keys in the request. For more information about MFA Delete, see MFA Delete.

Following is the proxy configuration for init and deleteMultipleObjects. The init section has additional parameters and parameters that need to be removed apart from those mentioned in the Connecting to Amazon S3 section.

**deleteMultipleObjects**
```
<amazons3.init>
    <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
    <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
    <methodType>{$ctx:methodType}</methodType>
    <contentType>{$ctx:contentType}</contentType>
    <bucketName>{$ctx:bucketName}</bucketName>
    <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
    <xAmzSecurityToken>{$ctx:xAmzSecurityToken}</xAmzSecurityToken>
    <host>{$ctx:host}</host>
    <region>{$ctx:region}</region>
    <expect>{$ctx:expect}</expect>
    <contentLength>{$ctx:contentLength}</contentLength>
    <xAmzMfa>{$ctx:xAmzMfa}</xAmzMfa>
</amazons3.init>

<amazons3.deleteMultipleObjects>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <quiet>{$ctx:quiet}</quiet>
    <deleteConfig>{$ctx:deleteConfig}</deleteConfig>
</amazons3.deleteMultipleObjects>
```

**Properties**
* accessKeyId: AWS access key ID.
* secretAccessKey: AWS secret access key.
* methodType: HTTP method type.
* contentType: Content type of the resource.
* bucketName: Name of the bucket.
* isXAmzDate: Indicates whether the current date and time are considered to calculate the signature. Valid values: true or false
* xAmzSecurityToken: The security token based on whether Amazon DevPay operations or temporary security credentials are used.
* host: The path-style requests (s3.amazonaws.com) or virtual-style requests (BucketName.s3.amazonaws.com).
* region: Region which is used select a regional endpoint to make requests.
* expect: When this property is set to 100-continue, the request does not send the request body until it receives an acknowledgment. If the message is rejected based on the headers, the body of the message is not sent.
* contentLength: Length of the message without the headers according to RFC 2616.
* xAmzMfa: Required to permanently delete a versioned object if versioning is configured with MFA Delete enabled. The value is the concatenation of the authentication device's serial number, a space, and the value displayed on your authentication device.
* bucketUrl: The URL of the bucket.
* deleteConfig: The configuration for deleting the objects. It contains the following properties:
    * Delete: Container for the request.
        * Quiet: Enable quiet mode for the request. When you add this element, you must set its value to true. Default is false.
        * Object: Container element that describes the delete request for each object.
            * Key: Key name of the object to delete.
            * VersionId: Version ID for the specific version of the object to delete.

**Sample request**
Following is a sample REST request that can be handled by the deleteMultipleObjects operation.

```
<deleteMultipleObjects>
    <accessKeyId>SDAKIAIGURZM7GDFGJ7TRO6KQ</accessKeyId>
    <secretAccessKey>asAX8CJoDdfEFKzeOd0Ve5dMCFk4STUFDRHkGX6m0CcY</secretAccessKey>
    <methodType>POST</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken/>
    <expect/>
    <xAmzMfa/>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <quiet>true</quiet>
    <deleteConfig>
        <Objects>
            <Object>
                <Key>testobject33</Key>
                <VersionId>M46OVgxl4lHBNCeZwBpEZvGhj0k5vvjK</VersionId>
            </Object>
            <Object>
                <Key>testObject1</Key>
                <VersionId>PwbvPU.yn3YcHOCF8bntKeTdzfKQC6jN</VersionId>
            </Object>
        </Objects>
    </deleteConfig>
</deleteMultipleObjects>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html](http://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html)

### Getting an object
The getObject operation retrieves objects from Amazon S3. To use this operation, the user must have READ access to the object. If the user grants READ access to the anonymous user, the object can be returned without using an authorization header. By default, this operation returns the latest version of the object.

An Amazon S3 bucket has no directory hierarchy such as in a typical computer file system. However, a logical hierarchy can be created by using object key names that imply a folder structure. For example, instead of naming an object sample.jpg, it could be named photos/2006/February/sample.jpg. To retrieve an object from such a logical hierarchy, the full key name for the object should be specified.

For a virtual hosted-style request example, if you have the object photos/2006/February/sample.jpg, specify the resource as /photos/2006/February/sample.jpg. For a path-style request example, if you have the object photos/2006/February/sample.jpg in the bucket named examplebucket, specify the resource as /examplebucket/photos/2006/February/sample.jpg. If the object to be retrieved is a GLACIER storage class object, the object is archived in Amazon Glacier, and you must first restore a copy using the POST Object restore API before retrieving the object. Otherwise, this operation returns the "InvalidObjectStateError" error.

When calling init before this operation, the following headers should be removed: xAmzAcl, xAmzGrantRead, xAmzGrantWrite, xAmzGrantReadAcp, xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getObject**
```
<amazons3.getObject>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <objectName>{$ctx:objectName}</objectName>
    <responseContentType>{$ctx:responseContentType}</responseContentType>
    <responseContentLanguage>{$ctx:responseContentLanguage}</responseContentLanguage>
    <responseExpires>{$ctx:responseExpires}</responseExpires>
    <responseCacheControl>{$ctx:responseCacheControl}</responseCacheControl>
    <responseContentDisposition>{$ctx:responseContentDisposition}</responseContentDisposition>
    <responseContentEncoding>{$ctx:responseContentEncoding}</responseContentEncoding>
    <range>{$ctx:range}</range>
    <ifModifiedSince>{$ctx:ifModifiedSince}</ifModifiedSince>
    <ifUnmodifiedSince>{$ctx:ifUnmodifiedSince}</ifUnmodifiedSince>
    <ifMatch>{$ctx:ifMatch}</ifMatch>
    <ifNoneMatch>{$ctx:ifNoneMatch}</ifNoneMatch>
</amazons3.getObject>
```

**Properties**
* bucketUrl: The URL of the bucket.
* objectName: The name of the object.
* responseContentType: Content-Type header of the response.
* responseContentLanguage: Content-Language header of the response.
* responseExpires: The Expires header of the response.
* responseCacheControl: The Cache-Control header of the response.
* responseContentDisposition: The Content-Disposition header of the response.
* responseContentEncoding: The Content-Encoding header of the response.
* range: Downloads the specified range bytes of an object.
* ifModifiedSince: Returns the object only if it has been modified since the specified time.
* ifUnmodifiedSince: Returns the object only if it has not been modified since the specified time.
* ifMatch: Returns the object only if its ETag is the same as the one specified.
* ifNoneMatch: Returns the object only if its ETag is not the same as the one specified.


**Sample request**
Following is a sample REST request that can be handled by the getObject operation.

```
<getObject>
    <accessKeyId>AFSKIAIGURZM7GJDFG7TRO6KQ</accessKeyId>
    <secretAccessKey>asAXsdf8CJoDKzeOd0Ve5dMCFk4STUFDRHkGdfdfX6m0CcY</secretAccessKey>
    <methodType>GET</methodType>
    <contentType>application/xml</contentType>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken/>
    <contentMD5/>
    <objectName>Tree2.png</objectName>
    <rangeBytes/>
    <responseContentType/>
    <responseContentLanguage/>
    <responseExpires/>
    <responseCacheControl/>
    <responseContentDisposition/>
    <range/>
    <ifModifiedSince/>
    <ifUnmodifiedSince/>
    <ifMatch/>
    <ifNoneMatch/>
</getObject>
```

**Related Amazon S3 documentation**

[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)

### Creating an object
The createObject operation adds an object to a bucket. You must have WRITE permissions on a bucket to add an object to it. Amazon S3 does not add partial objects, so if a success response is received, the entire object is added to the bucket. Because Amazon S3 is a distributed system, if it receives multiple write requests for the same object simultaneously, it overwrites all but the last object written.

To ensure that data is not corrupted traversing the network, use the Content-MD5 header. When it is used, Amazon S3 checks the object against the provided MD5 value and, if they do not match, it returns an error. Additionally, you can calculate the MD5 value while putting an object to Amazon S3 and compare the returned ETag with the calculated MD5 value.

When uploading an object, you can specify the accounts or groups that should be granted specific permissions on the object. There are two ways to grant the appropriate permissions using the request headers: either specify a canned (predefined) ACL using the "x-amz-acl" request header, or specify access permissions explicitly using the "x-amz-grant-read", "x-amz-grant-read-acp", "x-amz-grant-write-acp", and "x-amz-grant-full-control" headers. These headers map to the set of permissions Amazon S3 supports in an ACL. Use only one approach, not both.

See multipart processing for more information.

**createObject**
```
<amazons3.createObject>
   <bucketUrl>{$url:bucketUrl}</bucketUrl>
   <objectName>{$url:objectName}</objectName>
</amazons3.createObject>
```

**Properties**
* bucketUrl: The URL of the bucket.
* objectName: The name to give for the newly created object.

**Sample request**
Following is a sample REST request that can be handled by the createObject operation.

Sample request for createObject
Set the file content in body when making the request.
<!-- Use below url when testing createObject with attachments -->
http://localhost:8280/services/amazons3_createObject?objectName=testFile1.txt&bucketUrl=http://1484039949818testconbkt2.s3-us-west-2.amazonaws.com&accessKeyId=AKIAXXXXXXXXXISQ&secretAccessKey=c7UqXXXhsI/5e&contentType=text/plain&isXAmzDate=true&methodType=PUT&bucketName=1484039949818testconbkt2&addCharset=true

**Related Amazon S3 documentation**

[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)

### Creating an object ACL
The createObjectACL operation sets the access control list (ACL) permissions for an object that already exists in a bucket. You can specify the ACL in the request body or specify permissions using request headers, depending on the application needs.  For example, if there is an existing application that updates an object ACL using the request body, you can continue to use that approach.

The ACL of an object is set at the object version level. By default, createObjectACL sets the ACL of the latest version of an object. To set the ACL of a different version, use the versionId property.

**createObjectACL**
```
<amazons3.createObjectACL>
    <objectName>{$ctx:objectName}</objectName>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <ownerId>{$ctx:ownerId}</ownerId>
    <ownerDisplayName>{$ctx:ownerDisplayName}</ownerDisplayName>
    <accessControlList>{$ctx:accessControlList}</accessControlList>
    <versionId>{$ctx:versionId}</versionId>
</amazons3.createObjectACL>
```

**Properties**
* objectName: Name of the object.
* bucketUrl: The URL of the bucket.
* ownerID: ID of the bucket owner.
* ownerdisplayName: Screen name of the bucket owner.
* accessControlList: Container for ACL information, which includes the following:
    * Grant: Container for the grantee and permissions.
        * Grantee: The subject whose permissions are being set.
            * ID: ID of the grantee.
            * DisplayName: Screen name of the grantee.
        * Permission: Specifies the permission to give to the grantee.
* versionId: ID of the object version.

**Sample request**
Following is a sample REST request that can be handled by the createObjectACL operation.

```
<createObjectACL>
  <accessKeyId>AKIAIGURZMDFG7TRO6KQ</accessKeyId>
  <secretAccessKey>asAX8CJoDKzdfg0Ve5dMCFk4STUFDRHkGX6m0CcY</secretAccessKey>
  <methodType>PUT</methodType>
  <contentLength>256</contentLength>
  <contentType>application/xml</contentType>
  <contentMD5></contentMD5>
  <expect></expect>
  <region>us-east-2</region>
  <host>s3.us-east-2.amazonaws.com</host>
  <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
  <bucketName>signv4test</bucketName>
  <isXAmzDate>true</isXAmzDate>
  <xAmzSecurityToken></xAmzSecurityToken>
  <objectName>testObject2</objectName>
  <versionId>FHbrL3xf2TK54hLNWWArYI79woSElvHf</versionId>
  <xAmzAcl></xAmzAcl>
  <xAmzGrantRead></xAmzGrantRead>
  <xAmzGrantWrite></xAmzGrantWrite>
  <xAmzGrantReadAcp></xAmzGrantReadAcp>
  <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
  <xAmzGrantFullControl></xAmzGrantFullControl>
  <ownerId>f422baefcd6a519ea3c43bec8874b6c3f71c83f72549f4fb4c0e23044efd2531</ownerId>
  <ownerdisplayName>rhettige@yahoo.com</ownerdisplayName>
  <accessControlList>
    <Grant>
      <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="CanonicalUser">
        <ID>c6567b8c9274b78d6af4a3080c5e43e700f560f3517b7d9acc87251412044c35</ID>
        <DisplayName>pe.chanaka.ck@gmail.com</DisplayName>
      </Grantee>
      <Permission>WRITE_ACP</Permission>
    </Grant>
    <Grant>
      <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="CanonicalUser">
        <ID>c6567b8c9274b78d6af4a3080c5e43e700f560f3517b7d9acc87251412044c35</ID>
        <DisplayName>pe.chanaka.ck@gmail.com</DisplayName>
      </Grantee>
      <Permission>READ</Permission>
    </Grant>
  </accessControlList>
</createObjectACL>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html)

### Creating an object copy
The createObjectCopy operation creates a copy of an object that is already stored in Amazon S3. This operation is the same as performing a GET and then a PUT. Adding the request header "x-amz-copy-source" enables the PUT operation to copy the source object into the destination bucket.

When copying an object, most of the metadata (default) can be preserved, or new metadata can be specified. However, the ACL is not preserved and is set to "private" for the user making the request. All copy requests must be authenticated and cannot contain a message body. Additionally, the user must have the READ access to the source object and WRITE access to the destination bucket. To copy an object only under certain conditions, such as whether the ETag matches or whether the object was modified before or after a specified date, the request headers such as "x-amz-copy-source-if-match", "x-amz-copy-source-if-none-match", "x-amz-copy-source-if-unmodified-since", or "x-amz-copy-source-if-modified-since" must be used (all headers prefixed with "x-amz-" must be signed, including "x-amz-copy-source").

There are two instances when the copy request could return an error. One is when Amazon S3 receives the copy request, and the other can occur while Amazon S3 is copying the files. If the error occurs before the copy operation starts, you receive a standard Amazon S3 error. If the error occurs during the copy operation, the error response is embedded in the 200 OK response. This means that a 200 OK response can contain either a success or an error. If the request is an HTTP 1.1 request, the response is chunk encoded. Otherwise, it will not contain the content-length, and you will need to read the entire body.

When copying an object, the accounts or groups that should be granted specific permissions on the object can be specified. There are two ways to grant the appropriate permissions using the request headers: one is to specify a canned (predefined) ACL using the "x-amz-acl" request header, and the other is to s pecify access permissions explicitly using the "x-amz-grant-read", "x-amz-grant-read-acp", "x-amz-grant-write-acp", and "x-amz-grant-full-control" headers. These headers map to the set of permissions Amazon S3 supports in an ACL. Use one approach, not both .

Following is the proxy configuration for init and createObjectCopy. The init section has additional parameters and parameters that need to be removed apart from those mentioned in the Connecting to Amazon S3 section.

**createObjectCopy**
```
<amazons3.init>
    <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
    <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
    <methodType>{$ctx:methodType}</methodType>
    <contentLength>{$ctx:contentLength}</contentLength>
    <contentType>{$ctx:contentType}</contentType>
    <contentMD5>{$ctx:contentMD5}</contentMD5>
    <expect>{$ctx:expect}</expect>
    <host>{$ctx:host}</host>
    <region>{$ctx:region}</region>
    <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
    <xAmzSecurityToken>{$ctx:xAmzSecurityToken}</xAmzSecurityToken>
    <bucketName>{$ctx:bucketName}</bucketName>
    <xAmzAcl>{$ctx:xAmzAcl}</xAmzAcl>
    <xAmzGrantRead>{$ctx:xAmzGrantRead}</xAmzGrantRead>
    <xAmzGrantWrite>{$ctx:xAmzGrantWrite}</xAmzGrantWrite>
    <xAmzGrantReadAcp>{$ctx:xAmzGrantReadAcp}</xAmzGrantReadAcp>
    <xAmzGrantWriteAcp>{$ctx:xAmzGrantWriteAcp}</xAmzGrantWriteAcp>
    <xAmzGrantFullControl>{$ctx:xAmzGrantFullControl}</xAmzGrantFullControl>
    <xAmzCopySource>{$ctx:xAmzCopySource}</xAmzCopySource>
    <xAmzMetadataDirective>{$ctx:xAmzMetadataDirective}</xAmzMetadataDirective>
    <xAmzCopySourceIfMatch>{$ctx:xAmzCopySourceIfMatch}</xAmzCopySourceIfMatch>
    <xAmzCopySourceIfNoneMatch>{$ctx:xAmzCopySourceIfNoneMatch}</xAmzCopySourceIfNoneMatch>
    <xAmzCopySourceIfUnmodifiedSince>{$ctx:xAmzCopySourceIfUnmodifiedSince}</xAmzCopySourceIfUnmodifiedSince>
    <xAmzCopySourceIfModifiedSince>{$ctx:xAmzCopySourceIfModifiedSince}</xAmzCopySourceIfModifiedSince>
    <xAmzServeEncryption>{$ctx:xAmzServeEncryption}</xAmzServeEncryption>
    <xAmzStorageClass>{$ctx:xAmzStorageClass}</xAmzStorageClass>
    <xAmzWebsiteLocation>{$ctx:xAmzWebsiteLocation}</xAmzWebsiteLocation>
</amazons3.init>

<amazons3.createObjectCopy>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <destinationObject>{$ctx:destinationObject}</destinationObject>
</amazons3.createObjectCopy>
```

**Properties**
* accessKeyId: AWS access key ID.
* secretAccessKey: AWS secret access key.
* methodType: HTTP method type.
* contentLength: Length of the message without the headers according to RFC 2616.
* contentType: Content type of the resource.
* contentMD5: Base64 encoded 128-bit MD5 digest of the message according to RFC 1864.
* expect: When this property is set to 100-continue, the request does not send the request body until it receives an acknowledgment. If the message is rejected based on the headers, the body of the message is not sent.
* host: The path-style requests (s3.amazonaws.com) or virtual-style requests (BucketName.s3.amazonaws.com).
* region: Region which is used select a regional endpoint to make requests.
* isXAmzDate: Specifies whether the current date and time are considered to calculate the signature. Valid values: true or false
* xAmzSecurityToken: The security token based on whether Amazon DevPay operations or temporary security credentials are used.
* bucketName: Name of the bucket.
* xAmzAcl: The canned ACL to apply to the object.
* xAmzGrantRead: Allows the specified grantee or grantees to list the objects in the bucket.
* xAmzGrantWrite: Allows the specified grantee or grantees to create, overwrite, and delete any object in the bucket.
* xAmzGrantReadAcp: Allows the specified grantee or grantees to read the bucket ACL.
* xAmzGrantWriteAcp: Allows the specified grantee or grantees to write the ACL for the applicable bucket.
* xAmzGrantFullControl: Allows the specified grantee or grantees the READ, WRITE, READ_ACP, and WRITE_ACP permissions on the bucket.
* xAmzCopySource: Required - The name of the source bucket and key name of the source object, separated by a slash (/).
* xAmzMetadataDirective: Optional - Specifies whether the metadata is copied from the source object or replaced with metadata provided in the request.
* xAmzCopySourceIfMatch: Optional - Copies the object if its entity tag (ETag) matches the specified tag. Otherwise, the request returns a 412 HTTP status code error (failed precondition).
* xAmzCopySourceIfNoneMatch: Optional - Copies the object if its entity tag (ETag) is different from the specified ETag. Otherwise, the request returns a 412 HTTP status code error (failed precondition).
* xAmzCopySourceIfUnmodifiedSince: Optional - Copies the object if it has not been modified since the specified time. Oherwise, the request returns a 412 HTTP status code error (failed precondition).
* xAmzCopySourceIfModifiedSince: Optional - Copies the object if it has been modified since the specified time. Otherwise, the request returns a 412 HTTP status code error (failed condition).
* xAmzServeEncryption: Optional - Specifies the server-side encryption algorithm to use when Amazon S3 creates the target object.
* xAmzStorageClass: Optional - RRS enables customers to reduce their costs by storing non-critical, reproducible data at lower levels of redundancy than Amazon S3's standard storage.
* xAmzWebsiteLocation: Optional - If the bucket is configured as a website, redirects requests for this object to another object in the same bucket or to an external URL. Amazon S3 stores the value of this header in the object metadata.
* bucketUrl: The URL of the bucket.
* destinationObject: The destination where the source will be copied.

Even if the same operation is executed several times, it will not give an error, and a new copy will not be duplicated.

**Sample request**
Following is a sample REST request that can be handled by the createObjectCopy operation.

```
 <createObjectCopy>
    <accessKeyId>AKISFSAIGURZM7GJ7TRODSDF6KQ</accessKeyId>
    <secretAccessKey>asAX8CJoDKzeOd0Ve5dMsdfWERCFk4STUFDRHkGX6m0CcY</secretAccessKey>
    <methodType>PUT</methodType>
    <contentLength></contentLength>
    <contentType>application/xml</contentType>
    <contentMD5></contentMD5>
    <expect></expect>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <destinationObject>testObject5</destinationObject>
    <xAmzAcl></xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
    <xAmzCopySource>/imagesBucket5/testObject37</xAmzCopySource>
    <xAmzMetadataDirective></xAmzMetadataDirective>
    <xAmzCopySourceIfMatch></xAmzCopySourceIfMatch>
    <xAmzCopySourceIfNoneMatch></xAmzCopySourceIfNoneMatch>
    <xAmzCopySourceIfUnmodifiedSince></xAmzCopySourceIfUnmodifiedSince>
    <xAmzCopySourceIfModifiedSince></xAmzCopySourceIfModifiedSince>
    <xAmzServeEncryption></xAmzServeEncryption>
    <xAmzStorageClass></xAmzStorageClass>
    <xAmzWebsiteLocation></xAmzWebsiteLocation>
</createObjectCopy>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)

### Getting object metadata
The getObjectMetaData operation retrieves metadata from an object without returning the object itself. This operation is useful if you are interested only in an object's metadata. To use this operation, you must have READ access to the object. The response is identical to the GET response except that there is no response body.

When calling init before this operation, the following headers should be removed: xAmzAcl, xAmzGrantRead, xAmzGrantWrite, xAmzGrantReadAcp, xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getObjectMetaData**
```
<amazons3.getObjectMetaData>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <objectName>{$ctx:objectName}</objectName>
    <range>{$ctx:range}</range>
    <ifModifiedSince>{$ctx:ifModifiedSince}</ifModifiedSince>
    <ifUnmodifiedSince>{$ctx:ifUnmodifiedSince}</ifUnmodifiedSince>
    <ifMatch>{$ctx:ifMatch}</ifMatch>
    <ifNoneMatch>{$ctx:ifNoneMatch}</ifNoneMatch>
</amazons3.getObjectMetaData>
```

**Properties**
* bucketUrl: The URL of the bucket.
* objectName: The name of the object.
* range: Downloads the specified range bytes of an object.
* ifModifiedSince: Returns the object only if it has been modified since the specified time. Otherwise, returns 304.
* ifUnmodifiedSince: Returns the object only if it has not been modified since the specified time. Otherwise, returns 412.
* ifMatch: Returns the object only if its entity tag (ETag) is the same as the one specified. Otherwise, returns 412.
* ifNoneMatch: Returns the object only if its entity tag (ETag) is different from the one specified. Otherwise, returns 304.

**Sample request**
Following is a sample REST request that can be handled by the getObjectMetaData operation.

```
<getObjectMetaData>
    <accessKeyId>AKIAIGURSDFSDFDZM7GJ7TRO6KQSFGFD</accessKeyId>
    <secretAccessKey>asAX8CJoDKzeOd0Ve5dMCFk4STUFDRHkGX6m0CcY</secretAccessKey>
    <methodType>HEAD</methodType>
    <contentLength></contentLength>
    <contentType>application/xml</contentType>
    <contentMD5></contentMD5>
    <expect></expect>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <objectName>testObject2</objectName>
    <range></range>
    <ifModifiedSince></ifModifiedSince>
    <ifUnmodifiedSince></ifUnmodifiedSince>
    <ifMatch></ifMatch>
    <ifNoneMatch></ifNoneMatch>
</getObjectMetaData>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html)

### Uploading a part
The uploadPart operation uploads a part in a multipart upload. In this operation, you provide part data in your request. However, you have an option to specify your existing Amazon S3 object as the data source for the part being uploaded. You must initiate a multipart upload (see initMultipartUpload) before you can upload any part. In response to your initiate request, Amazon S3 returns an upload ID, which is the unique identifier that must be included in the upload part request.

Part numbers can be any number from 1 to 10,000 (inclusive). A part number uniquely identifies a part and also defines its position within the object being created. If a new part is uploaded using the same part number that was used with a previous part, the previously uploaded part is overwritten. Each part must be at least 5 MB in size, except the last part. There is no size limit on the last part of your multipart upload.

To ensure that data is not corrupted when traversing the network, specify the Content-MD5 header in the upload part request. Amazon S3 checks the part data against the provided MD5 value. If they do not match, Amazon S3 returns an error. After the multipart upload is initiated and one or more parts are uploaded, you must either complete or abort multipart upload in order to stop getting charged for storage of the uploaded parts. Only after you either complete or abort multipart upload will Amazon S3 free up the parts storage and stop charging you for the parts storage.

**uploadPart**
<amazons3.uploadPart>
    <bucketUrl>{$url:bucketUrl}</bucketUrl>
    <objectName>{$url:objectName}</objectName>
    <uploadId>{$url:uploadId}</uploadId>
    <partNumber>{$url:partNumber}</partNumber>
</amazons3.uploadPart>

**Properties**
* bucketUrl: The URL of the bucket.
* objectName: Name of the object.
* uploadId: Upload ID.
* partNumber: Part number that identifies the part.

**Sample request**
Following is a sample REST request that can be handled by the uploadPart operation.

```
<amazons3.init>
    <accessKeyId>AKIAIGUASDRZM7GJ7TRO6KQAD</accessKeyId>
    <secretAccessKey>asAX8CJoDKsdfzeOd0Ve5dMCFk4STUFDRHkGX6m0CSLKcY</secretAccessKey>
    <isXAmzDate>true</isXAmzDate>
    <contentType>text/plain</contentType>
    <methodType>PUT</methodType>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
</amazons3.init>

<!-- Use the following for the uploading part method -->

http://localhost:8889/services/multipart?objectName=testFile1.txt&uploadId=VSMdi3EgFYBq_DpBv6G0LWXydidqO9WIw90UIp81EripQrJNuxOo.jf3tkA.23aURwTOZPBD4iCfcogwtMc8_A--&partNumber=1&bucketUrl=http://sinhala.com.s3-us-west-2.amazonaws.com&accessKeyId=AKIAIGUASDRZM7GJ7TRO6KQAD&secretAccessKey=asAX8CJoDKsdfzeOd0Ve5dMCFk4STUFDRHkGX6m0CSLKcY&bucketName=sinhala.com&isXAmzDate=true&methodType=PUT
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html)

### Completing a multipart upload
The completeMultipartUpload operation completes a multipart upload by assembling previously uploaded parts. You should first initiate the multipart upload using initMultipartUpload, and then upload all parts using uploadParts. After you successfully upload all relevant parts of an upload, call completeMultipartUpload to complete the upload.
When you call completeMultipartUpload, Amazon S3 concatenates all the parts in ascending order by part number to create a new object. In the completeMultipartUpload request, you must provide the complete parts list (see listParts). For each part in the list, the part number and the ETag header value must be provided. When the part is uploaded the part number and the ETag header value should be returned.

Processing of a completeMultipartUpload request can take several minutes. After Amazon S3 begins processing the request, it sends an HTTP response header that specifies a 200 OK response. While processing is in progress, Amazon S3 periodically sends whitespace characters to keep the connection from timing out. Because a request could fail after the initial 200 OK response has been sent, it is important that you check the response body to determine whether the request succeeded. If completeMultipartUpload fails, applications should be prepared to retry the failed requests.

When calling init before this operation, the following headers should be removed: xAmzAcl, xAmzGrantRead, xAmzGrantWrite, xAmzGrantReadAcp, xAmzGrantWriteAcp, and xAmzGrantFullControl.

**completeMultipartUpload**
```
<amazons3.completeMultipartUpload>
    <partDetails>{$ctx:partDetails}</partDetails>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <objectName>{$ctx:objectName}</objectName>
    <uploadId>{$ctx:uploadId}</uploadId>
</amazons3.completeMultipartUpload>
```

**Properties**
* partDetails: The container that holds the part details. The part details are as follows:
    * part: The container for elements related to a previously uploaded part.
        *  partNumber: The part number that identifies the part.
        *  ETag: The entity tag returned when the part is uploaded.
*  bucketUrl: The URL of the bucket.
*  objectName: The name of the object.
*  uploadId: The ID of the upload.

**Sample request**
Following is a sample REST request that can be handled by the completeMultipartUpload operation.

```
<completeMultipartUpload>
    <accessKeyId>AKIAJ5H4DUFASFDF3O2VJMLA</accessKeyId>
    <secretAccessKey>oz22F2SDmwtR+JGCGaykdfdQmKHZj56zhUiiEbdfd48Fynwt</secretAccessKey>
    <methodType>POST</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <contentMD5></contentMD5>
    <objectName>myimage.png</objectName>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <uploadId>VONszTPldyDo80ARdEMI2kVxEBLQYY1tncD7PpB54WDtLTACJIn.jWRIGo7iL_EkJYn9Z2BT3MM.kEqju9CgLyUveDtl6MgXzRYqjb8R4L.ZVpUhv25d56P2Tk1XnD0C</uploadId>
    <partDetails>
        <Part>
            <PartNumber>1</PartNumber>
            <ETag>LKJLINTLNM9879NL7jNLk</ETag>
        </Part>
    </partDetails>
    <xAmzSecurityToken></xAmzSecurityToken>
    <expect></expect>
    <contentLength></contentLength>
    <expires></expires>
</completeMultipartUpload>
```

Related Amazon S3 documentation
[http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html)

**Sample Scenario for Completing a multipart upload**
Following is sample scenario that describes how to upload a file to Amazon Simple Storage Service.

* Create a bucket via the AWS Management Console or by using the [createBucket](https://docs.wso2.com/display/ESBCONNECTORS/Working+with+Buckets+in+Amazon+S3#WorkingwithBucketsinAmazonS3-createBucket) method.
* Invoke the initMultipartUpload method to initiate the file upload. To invoke the initMultipartUpload method, you need to provide the bucketName, bucketUrl and the objectName that you retrieved via the [createBucket](https://docs.wso2.com/display/ESBCONNECTORS/Working+with+Buckets+in+Amazon+S3#WorkingwithBucketsinAmazonS3-createBucket) method.
* Get the uploadId from the response.
* Invoke the uploadPart method by providing the bucketName, bucketUrl, and uploadId that you retrieved from the response of initMultipartUpload. You also need to specify values for the other required fields.
* Get the ETag from the header of the response. Here, you need to pass the file content as the payload.
* Finally, invoke the completeMultipartUpload method with the value of ETag, uploadId, bucketUrl, bucketName and objectName to complete the file upload.

The following proxy service invokes the [initMultipartUpload](https://docs.wso2.com/display/ESBCONNECTORS/Working+with+Objects+in+Amazon+S3#WorkingwithObjectsinAmazonS3-initMultipartUpload), [uploadPart](https://docs.wso2.com/display/ESBCONNECTORS/Working+with+Objects+in+Amazon+S3#WorkingwithObjectsinAmazonS3-uploadPart) and [completeMultipartUpload](https://docs.wso2.com/display/ESBCONNECTORS/Working+with+Objects+in+Amazon+S3#WorkingwithObjectsinAmazonS3-completeMultipartUpload) methods of amazon S3 connector to complete the multipart upload.

```
<proxy xmlns="http://ws.apache.org/ns/synapse"
       name="completeMultipartUploadScenario"
       transports="https http"
       startOnLoad="true">
   <description/>
   <target>
      <inSequence>
         <property name="accessKeyId" expression="//accessKeyId/text()"/>
         <property name="secretAccessKey" expression="//secretAccessKey/text()"/>
         <property name="contentType" expression="//contentType/text()"/>
         <property name="addCharset" expression="//addCharset/text()"/>
         <property name="bucketName" expression="//bucketName/text()"/>
         <property name="isXAmzDate" expression="//isXAmzDate/text()"/>
         <property name="bucketUrl" expression="//bucketUrl/text()"/>
         <property name="objectName" expression="//objectName/text()"/>
         <property name="host" expression="//host/text()"/>
         <property name="region" expression="//region/text()"/>
         <property name="xAmzServeEncryption" expression="//xAmzServeEncryption/text()"/>
         <property name="xAmzMeta" expression="//xAmzMeta/text()"/>
         <property name="xAmzStorageClass" expression="//xAmzStorageClass/text()"/>
         <property name="cacheControl" expression="//cacheControl/text()"/>
         <property name="contentDisposition" expression="//contentDisposition/text()"/>
         <property name="objectName" expression="//objectName/text()"/>
         <property name="expires" expression="//expires/text()"/>
         <property name="filePath" expression="//filePath/text()"/>
         <property name="partNumber" value="1"/>
         <property name="blocking" expression="//blocking/text()"/>
         <property name="fileContent" expression="//fileContent"/>
         <property name="methodType" value="POST"/>
          <log level = "custom">
            <property name = "initMultipartUpload" value = "!!!!!!!!!!!!!!!! Before initMultipartUpload !!!!!!!!!!!!!!!!"/>
         </log>
         <amazons3.init>
            <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
            <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
            <methodType>{$ctx:methodType}</methodType>
            <blocking>{$ctx:blocking}</blocking>
            <contentType>{$ctx:contentType}</contentType>
            <addCharset>{$ctx:addCharset}</addCharset>
            <bucketName>{$ctx:bucketName}</bucketName>
            <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
            <host>{$ctx:host}</host>
            <region>{$ctx:region}</region>
            <xAmzMeta>{$ctx:xAmzMeta}</xAmzMeta>
            <xAmzServeEncryption>{$ctx:xAmzServeEncryption}</xAmzServeEncryption>
            <xAmzStorageClass>{$ctx:xAmzStorageClass}</xAmzStorageClass>
         </amazons3.init>
         <amazons3.initMultipartUpload>
            <cacheControl>{$ctx:cacheControl}</cacheControl>
            <contentDisposition>{$ctx:contentDisposition}</contentDisposition>
            <contentEncoding>{$ctx:contentEncoding}</contentEncoding>
            <expires>{$ctx:expires}</expires>
            <objectName>{$ctx:objectName}</objectName>
            <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
         </amazons3.initMultipartUpload>
         <log level = "custom">
            <property name = "initMultipartUpload" value = "!!!!!!!!!!!!!!!! After initMultipartUpload !!!!!!!!!!!!!!!!"/>
         </log>
         <property name="ContentType" value="application/xml" scope="axis2"/>
         <property xmlns:ns="http://s3.amazonaws.com/doc/2006-03-01/"
                   name="uploadId"
                   expression="//ns:UploadId"
                   scope="default"
                   type="STRING"/>
         <log level = "custom">
            <property name = "uploadPart" value = "!!!!!!!!!!!!!!!! Before uploadPart !!!!!!!!!!!!!!!!"/>
         </log>
         <property name="methodType" value="PUT"/>
         <amazons3.init>
            <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
            <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
            <bucketName>{$ctx:bucketName}</bucketName>
            <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
            <contentType>{$ctx:contentType}</contentType>
            <addCharset>{$ctx:addCharset}</addCharset>
            <methodType>{$ctx:methodType}</methodType>
            <host>{$ctx:host}</host>
            <region>{$ctx:region}</region>
         </amazons3.init>
         <payloadFactory media-type="xml">
            <format>
               <content xmlns="">$1</content>
            </format>
            <args>
               <arg evaluator="xml" expression="$ctx:fileContent"/>
            </args>
         </payloadFactory>
         <enrich>
            <source clone="true" xpath="$body//content//fileContent/*"/>
            <target type="body"/>
         </enrich>
         <amazons3.uploadPart>
            <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
            <objectName>{$ctx:objectName}</objectName>
            <uploadId>{$ctx:uploadId}</uploadId>
            <partNumber>{$ctx:partNumber}</partNumber>
         </amazons3.uploadPart>
          <log level = "custom">
            <property name = "uploadPart" value = "!!!!!!!!!!!!!!!! After uploadPart !!!!!!!!!!!!!!!!"/>
         </log>
         <property name="eTag" expression="$trp:ETag"/>
         <payloadFactory media-type="xml">
            <format>
               <partDetails xmlns="">
                  <Part>
                     <PartNumber>1</PartNumber>
                     <ETag>$1</ETag>
                  </Part>
               </partDetails>
            </format>
            <args>
               <arg evaluator="xml" expression="$ctx:eTag"/>
            </args>
         </payloadFactory>
          <log level = "custom">
            <property name = "completeMultipartUpload" value = "!!!!!!!!!!!!!!!! Before completeMultipartUpload !!!!!!!!!!!!!!!!"/>
         </log>
         <property name="methodType" value="POST"/>
         <amazons3.init>
            <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
            <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
            <bucketName>{$ctx:bucketName}</bucketName>
            <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
            <contentType>{$ctx:contentType}</contentType>
            <addCharset>{$ctx:addCharset}</addCharset>
            <methodType>{$ctx:methodType}</methodType>
            <host>{$ctx:host}</host>
            <region>{$ctx:region}</region>
         </amazons3.init>
         <amazons3.completeMultipartUpload>
            <partDetails>{//partDetails/*}</partDetails>
            <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
            <objectName>{$ctx:objectName}</objectName>
            <uploadId>{$ctx:uploadId}</uploadId>
         </amazons3.completeMultipartUpload>
          <log level = "custom">
            <property name = "completeMultipartUpload" value = "!!!!!!!!!!!!!!!! After completeMultipartUpload !!!!!!!!!!!!!!!!"/>
         </log>
         <respond/>
      </inSequence>
   </target>
</proxy>
```

**Sample request**
Following is a sample REST request that can be used for the above scenario:

```
<completeMultipartUpload>
   <accessKeyId>AKIAXXXXXXXXX</accessKeyId>
    <secretAccessKey>qHZBBzX3kmXXXXXXXXXXX</secretAccessKey>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <objectName>sampleTest.xml</objectName>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <fileContent><a>Sample Test</a></fileContent>
</completeMultipartUpload>
```


### Aborting multipart upload
The abortMultipartUpload operation aborts a multipart upload. After a multipart upload is aborted, no additional parts can be uploaded using that upload ID. The storage consumed by any previously uploaded parts will be freed. However, if any part uploads are currently in progress, those part uploads might or might not succeed. As a result, it might be necessary to abort a given multipart upload multiple times in order to completely free all storage consumed by all parts. To verify that all parts have been removed so that you do not get charged for the part storage, call the listParts operation and ensure the parts list is empty.

Following is the proxy configuration for init and abortMultipartUpload. The init section has additional parameters and parameters that need to be removed apart from those mentioned in the Connecting to Amazon S3 section.

**abortMultipartUpload**
```
<amazons3.init>
    <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
    <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
    <methodType>{$ctx:methodType}</methodType>
    <contentLength>{$ctx:contentLength}</contentLength>
    <contentType>{$ctx:contentType}</contentType>
    <contentMD5>{$ctx:contentMD5}</contentMD5>
    <expect>{$ctx:expect}</expect>
    <host>{$ctx:host}</host>
    <region>{$ctx:region}</region>
    <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
    <xAmzSecurityToken>{$ctx:xAmzSecurityToken}</xAmzSecurityToken>
    <bucketName>{$ctx:bucketName}</bucketName>
    <xAmzAcl>{$ctx:xAmzAcl}</xAmzAcl>
    <xAmzGrantRead>{$ctx:xAmzGrantRead}</xAmzGrantRead>
    <xAmzGrantWrite>{$ctx:xAmzGrantWrite}</xAmzGrantWrite>
    <xAmzGrantReadAcp>{$ctx:xAmzGrantReadAcp}</xAmzGrantReadAcp>
    <xAmzGrantWriteAcp>{$ctx:xAmzGrantWriteAcp}</xAmzGrantWriteAcp>
    <xAmzGrantFullControl>{$ctx:xAmzGrantFullControl}</xAmzGrantFullControl>
    <xAmzMeta>{$ctx:xAmzMeta}</xAmzMeta>
    <xAmzServeEncryption>{$ctx:xAmzServeEncryption}</xAmzServeEncryption>
    <xAmzStorageClass>{$ctx:xAmzStorageClass}</xAmzStorageClass>
    <xAmzWebsiteLocation>{$ctx:xAmzWebsiteLocation}</xAmzWebsiteLocation>
</amazons3.init>

<amazons3.abortMultipartUpload>
    <cacheControl>{$ctx:cacheControl}</cacheControl>
    <contentDisposition>{$ctx:contentDisposition}</contentDisposition>
    <contentEncoding>{$ctx:contentEncoding}</contentEncoding>
    <expires>{$ctx:expires}</expires>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <objectName>{$ctx:objectName}</objectName>
    <uploadId>{$ctx:uploadId}</uploadId>
</amazons3.abortMultipartUpload>
```

**Properties**
* accessKeyId: AWS access key ID.
* secretAccessKey: AWS secret access key.
* methodType: HTTP method type.
* contentLength: Length of the message without the headers according to RFC 2616.
* contentType: Content type of the resource.
* contentMD5: Base64 encoded 128-bit MD5 digest of the message according to RFC 1864.
* expect: When this property is set to 100-continue, the request does not send the request body until it receives an acknowledgment. If the message is rejected based on the headers, the body of the message is not sent.
* host: The path-style requests (s3.amazonaws.com) or virtual-style requests (BucketName.s3.amazonaws.com).
* region: Region which is used select a regional endpoint to make requests.
* isXAmzDate: Specifies whether the current date and time are considered to calculate the signature. Valid values: true or false
* xAmzSecurityToken: The security token based on whether Amazon DevPay operations or temporary security credentials are used.
* bucketName: Name of the bucket.
* xAmzAcl: The canned ACL to apply to the object.
* xAmzGrantRead: Allows the specified grantee or grantees to list the objects in the bucket.
* xAmzGrantWrite: Allows the specified grantee or grantees to create, overwrite, and delete any object in the bucket.
* xAmzGrantReadAcp: Allows the specified grantee or grantees to read the bucket ACL.
* xAmzGrantWriteAcp: Allows the specified grantee or grantees to write the ACL for the applicable bucket.
* xAmzGrantFullControl: Allows the specified grantee or grantees the READ, WRITE, READ_ACP, and WRITE_ACP permissions on the bucket.
* xAmzMeta: Optional - Any header starting with this prefix is considered user metadata. It will be stored with the object and returned when you retrieve the object.
* xAmzServeEncryption: Optional - Specifies the server-side encryption algorithm to use when Amazon S3 creates the target object.
* xAmzStorageClass: Optional - RRS enables customers to reduce their costs by storing non-critical, reproducible data at lower levels of redundancy than Amazon S3's standard storage.
* xAmzWebsiteLocation: Optional - If the bucket is configured as a website, redirects requests for this object to another object in the same bucket or to an external URL. Amazon S3 stores the value of this header in the object metadata.
* cacheControl: The Cache-Control header of the request.
* contentDisposition: The Content-Disposition header of the request.
* contentEncoding: The Content-Encoding header of the request.
* expires: The Expires header of the response.
* bucketUrl: The URL of the bucket.
* objectName: Name of the object.
* uploadId: The upload ID.

**Sample request**
Following is a sample REST request that can be handled by the abortMultipartUpload operation.

```
<abortMultipartUpload>
    <accessKeyId>AKIAJ5H4DFDDUFA3O2VJMLASFD</accessKeyId>
    <secretAccessKey>oz22F2mwtRsdfdsJGCGaykQmKHSDZj56zhUiiEb48Fynwt</secretAccessKey>
    <methodType>DELETE</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <contentMD5></contentMD5>
    <objectName>myimage.png</objectName>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <uploadId>VONszTPldyDo80ARdEMI2kVxEBLQYY1tncD7PpB54WDtLTACJIn.jWRIGo7iL_EkJYn9Z2BT3MM.kEqju9CgLyUveDtl6MgXzRYqjb8R4L.ZVpUhv25d56P2Tk1XnD0C</uploadId>
    <expect></expect>
    <xAmzAcl></xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
    <contentDisposition></contentDisposition>
    <contentEncoding></contentEncoding>
    <contentLength></contentLength>
    <expires></expires>
    <xAmzMeta>Content-Language:enus</xAmzMeta>
    <xAmzStorageClass>STANDARD</xAmzStorageClass>
    <xAmzWebsiteLocation></xAmzWebsiteLocation>
</abortMultipartUpload>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadAbort.html](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadAbort.html)

### Listing parts
The listParts operation lists the parts that have been uploaded for a specific multipart upload.

This operation must include the upload ID, which can be obtained using the initMultipartUpload operation. The listParts operation returns a maximum of 1,000 uploaded parts. The default number of parts returned is 1,000 parts, but you can restrict the number of parts using the maxParts property. If the multipart upload consists of more than 1,000 parts, the response returns an IsTruncated field with the value of true and a NextPartNumberMarker element. In subsequent listParts requests, you can include the partNumberMarker query string parameter and set its value to the NextPartNumberMarker field value from the previous response.

Following is the proxy configuration for init and listParts. The init section has additional parameters and parameters that need to be removed apart from those mentioned in the Connecting to Amazon S3 section.

**listParts**
```
<amazons3.init>
    <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
    <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
    <methodType>{$ctx:methodType}</methodType>
    <contentType>{$ctx:contentType}</contentType>
    <bucketName>{$ctx:bucketName}</bucketName>
    <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
    <expect>{$ctx:expect}</expect>
    <contentMD5>{$ctx:contentMD5}</contentMD5>
    <xAmzSecurityToken>{$ctx:xAmzSecurityToken}</xAmzSecurityToken>
    <host>{$ctx:host}</host>
    <region>{$ctx:region}</region>
    <uriRemainder>{$ctx:uriRemainder}</uriRemainder>
    <contentLength>{$ctx:contentLength}</contentLength>
</amazons3.init>

<amazons3.listParts>
    <maxParts>{$ctx:maxParts}</maxParts>
    <partNumberMarker>{$ctx:partNumberMarker}</partNumberMarker>
    <contentEncoding>{$ctx:contentEncoding}</contentEncoding>
    <encodingType>{$ctx:encodingType}</encodingType>
    <uploadId>{$ctx:uploadId}</uploadId>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <objectName>{$ctx:objectName}</objectName>
</amazons3.listParts>
```

**Properties**
* accessKeyId: AWS access key ID.
* secretAccessKey: AWS secret access key.
* methodType: HTTP method type.
* contentType: Content type of the resource.
* bucketName: Name of the bucket.
* isXAmzDate: Specifies whether the current date and time are considered to calculate the signature. Valid values: true or false
* expect: When this property is set to 100-continue, the request does not send the request body until it receives an acknowledgment. If the message is rejected based on the headers, the body of the message is not sent.
* contentMD5: Base64 encoded 128-bit MD5 digest of the message according to RFC 1864.
* xAmzSecurityToken: The security token based on whether Amazon DevPay operations or temporary security credentials are used.
* host: The path-style requests (s3.amazonaws.com) or virtual-style requests (BucketName.s3.amazonaws.com).
* region: Region which is used select a regional endpoint to make requests.
* uriRemainder: The URI syntax consists of a sequence of components separated by reserved characters, with the first component defining the semantics for the remainder of the URI string.
* contentLength: Length of the message without the headers according to RFC 2616.
* maxParts: Maximum number of parts allowed in the response.
* partNumberMarker: Specifies the part after which listing should begin. Only parts with higher part numbers will be listed.
* contentEncoding: The Content-Encoding header of the request.
* encodingType: Requests Amazon S3 to encode the response and specifies the encoding method to use.
* uploadId: Upload ID identifying the multipart upload whose parts are being listed.
* buckertUrl: The URL of the bucket.
* objectName: The name of the object.

**Sample request**
Following is a sample REST request that can be handled by the listParts operation.

```
<listParts>
    <accessKeyId>AFDKIAJ5H4DUFAADS3O2VJMLASDS</accessKeyId>
    <secretAccessKey>oSDz22F2mwtR+JGCGaykQmKHZjsdff56zhUiiEb48Fynwt</secretAccessKey>
    <methodType>GET</methodType>
    <contentLength></contentLength>
    <contentEncoding></contentEncoding>
    <encodingType>url</encodingType>
    <contentType>application/xml</contentType>
    <contentMD5></contentMD5>
    <uploadId>KyxZ7yjpSSZM9f0bdRectMF5dPg2h08BqTsmWf.8OEIq2Z4YvYg01LmJL0kVDqVcz2utci2CDE2Cn7k647j_84GhExGAN9uer65jljH_oapI758RA_AmcyW4N2usGHH0</uploadId>
    <expect></expect>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <objectName>myimage.png</objectName>
    <isXAmzDate>true</isXAmzDate>
    <maxParts>100</maxParts>
    <partNumberMarker>8</partNumberMarker>
    <xAmzSecurityToken></xAmzSecurityToken>
</listParts>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListParts.html](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListParts.html)

### Initiating multipart upload
The initMultipartUpload operation initiates a multipart upload and returns an upload ID. This upload ID is used to associate all the parts in the specific multipart upload. You specify this upload ID in each of your subsequent uploadPart requests. You also include this upload ID in the final request to either complete or abort the multipart upload request.

For request signing, multipart upload is just a series of regular requests: you initiate multipart upload, send one or more requests to upload parts (uploadPart), and finally complete multipart upload (completeMultipartUpload). You sign each request individually. After you initiate multipart upload and upload one or more parts, you must either complete or abort multipart upload in order to stop getting charged for storage of the uploaded parts. Only after you either complete or abort multipart upload will Amazon S3 free up the parts storage and stop charging you for the parts storage.

Following is the proxy configuration for init and initMultipartUpload. The init section has additional parameters and parameters that need to be removed apart from those mentioned in the Connecting to Amazon S3 section.

**initMultipartUpload**
```
<amazons3.init>
    <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
    <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
    <methodType>{$ctx:methodType}</methodType>
    <contentType>{$ctx:contentType}</contentType>
    <bucketName>{$ctx:bucketName}</bucketName>
    <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
    <expect>{$ctx:expect}</expect>
    <contentMD5>{$ctx:contentMD5}</contentMD5>
    <xAmzSecurityToken>{$ctx:xAmzSecurityToken}</xAmzSecurityToken>
    <host>{$ctx:host}</host>
    <region>{$ctx:region}</region>
    <uriRemainder>{$ctx:uriRemainder}</uriRemainder>
    <contentLength>{$ctx:contentLength}</contentLength>
    <xAmzAcl>{$ctx:xAmzAcl}</xAmzAcl>
    <xAmzGrantRead>{$ctx:xAmzGrantRead}</xAmzGrantRead>
    <xAmzGrantWrite>{$ctx:xAmzGrantWrite}</xAmzGrantWrite>
    <xAmzGrantReadAcp>{$ctx:xAmzGrantReadAcp}</xAmzGrantReadAcp>
    <xAmzGrantWriteAcp>{$ctx:xAmzGrantWriteAcp}</xAmzGrantWriteAcp>
    <xAmzGrantFullControl>{$ctx:xAmzGrantFullControl}</xAmzGrantFullControl>
    <xAmzMeta>{$ctx:xAmzMeta}</xAmzMeta>
    <xAmzServeEncryption>{$ctx:xAmzServeEncryption}</xAmzServeEncryption>
    <xAmzStorageClass>{$ctx:xAmzStorageClass}</xAmzStorageClass>
    <xAmzWebsiteLocation>{$ctx:xAmzWebsiteLocation}</xAmzWebsiteLocation>
</amazons3.init>

<amazons3.initMultipartUpload>
    <cacheControl>{$ctx:cacheControl}</cacheControl>
    <contentDisposition>{$ctx:contentDisposition}</contentDisposition>
    <contentEncoding>{$ctx:contentEncoding}</contentEncoding>
    <expires>{$ctx:expires}</expires>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <objectName>{$ctx:objectName}</objectName>
</amazons3.initMultipartUpload>
```

**Properties**
* accessKeyId: AWS access key ID.
* secretAccessKey: AWS secret access key.
* methodType: HTTP method type.
* contentType: Content type (MIME) of the file to be sent.
* bucketName: Name of the bucket.
* isXAmzDate: Specifies whether the current date and time are considered to calculate the signature. Valid values: true or false
* expect: When this property is set to 100-continue, the request does not send the request body until it receives an acknowledgment. If the message is rejected based on the headers, the body of the message is not sent.
* contentMD5: Base64 encoded 128-bit MD5 digest of the message according to RFC 1864.
* xAmzSecurityToken: The security token based on whether Amazon DevPay operations or temporary security credentials are used.
* host: The path-style requests (s3.amazonaws.com) or virtual-style requests (BucketName.s3.amazonaws.com).
* region: Region which is used select a regional endpoint to make requests.
* uriRemainder: The URI syntax consists of a sequence of components separated by reserved characters, with the first component defining the semantics for the remainder of the URI string.
* contentLength: Length of the message without the headers according to RFC 2616.
* xAmzAcl: The canned ACL to apply to the object.
* xAmzGrantRead: Allows the specified grantee or grantees to list the objects in the bucket.
* xAmzGrantWrite: Allows the specified grantee or grantees to create, overwrite, and delete any object in the bucket.
* xAmzGrantReadAcp: Allows the specified grantee or grantees to read the bucket ACL.
* xAmzGrantWriteAcp: Allows the specified grantee or grantees to write the ACL for the applicable bucket.
* xAmzGrantFullControl: Allows the specified grantee or grantees the READ, WRITE, READ_ACP, and WRITE_ACP permissions on the bucket.
* xAmzMeta: Optional - Any header starting with this prefix is considered user metadata. It will be stored with the object and returned when you retrieve the object.
* xAmzServeEncryption: Optional - Specifies the server-side encryption algorithm to use when Amazon S3 creates the target object.
* xAmzStorageClass: Optional - RRS enables customers to reduce their costs by storing non-critical, reproducible data at lower levels of redundancy than Amazon S3's standard storage.
* xAmzWebsiteLocation: Optional - If the bucket is configured as a website, redirects requests for this object to another object in the same bucket or to an external URL. Amazon S3 stores the value of this header in the object metadata.
* cacheControl: A request header that can be used to specify caching behavior along the request/reply chain.
* contentDisposition: A request header that specifies presentational information for the object.
* contentEncoding: A request header that specifies what content encodings have been applied to the object and thus what decoding mechanisms must be applied to obtain the media type referenced by the Content-Type header field.
* expires: The date and time at which the object is no longer cacheable.
* bucketUrl: The URL of the bucket.
* objectName: The name of the object.

**Sample request**
Following is a sample REST request that can be handled by the initMultipartUpload operation.

```
<initMultipartUpload>
    <accessKeyId>ASDKIAJ5H4DUFAASDF3O2VJMLDFDA</accessKeyId>
    <secretAccessKey>oz2sdf2F2mwtR+JGCGaykQmKHZjdfg56zhUiiEb48Fynwt</secretAccessKey>
    <methodType>POST</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <contentMD5></contentMD5>
    <objectName>myImage.png</objectName>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <expect></expect>
    <xAmzAcl></xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
    <contentDisposition></contentDisposition>
    <contentEncoding></contentEncoding>
    <contentLength></contentLength>
    <expires></expires>
    <xAmzMeta>Content-Language:enus</xAmzMeta>
    <xAmzServeEncryption>AES256</xAmzServeEncryption>
    <xAmzStorageClass>STANDARD</xAmzStorageClass>
    <xAmzWebsiteLocation></xAmzWebsiteLocation>
</initMultipartUpload>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)

### Getting the access control list of an object
The getObjectACL operation uses the ACL subresource to return the access control list (ACL) of an object. To use this operation, you must have READ_ACP access to the object.

Following is the proxy configuration for getObjectACL.

**getObjectACL**
```
<amazons3.getObjectACL>
    <objectName>{$ctx:objectName}</objectName>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getObjectACL>
```

**Properties**
* bucketUrl: The URL of the bucket.
* objectName: The name of the object.

**Sample request**
Following is a sample REST request that can be handled by the getObjectACL operation.

```
<getObjectACL>
    <accessKeyId>ASDKIAJ5H4DUFAASDF3O2VJMLDFDA</accessKeyId>
    <secretAccessKey>oz2sdf2F2mwtR+JGCGaykQmKHZjdfg56zhUiiEb48Fynwt</secretAccessKey>
    <methodType>GET</methodType>
    <contentType>application/xml; charset=UTF-8</contentType>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <contentMD5></contentMD5>
    <expect>100-continue</expect>
    <contentLength></contentLength>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead>GrantRead</xAmzGrantRead>
    <xAmzGrantWrite>Grantwrite</xAmzGrantWrite>
    <xAmzGrantReadAcp>GrantReadAcp</xAmzGrantReadAcp>
    <xAmzGrantWriteAcp>GrantWriteAcp</xAmzGrantWriteAcp>
    <xAmzGrantFullControl>GrantFullControl</xAmzGrantFullControl>
    <objectName>testFile.txt</objectName>
</getObjectACL>
```

Related Amazon S3 documentation
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETacl.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETacl.html)

### Getting torrent files from a bucket
The getObjectTorrent operation uses the torrent subresource to return torrent files from a bucket. BitTorrent can save you bandwidth when you're distributing large files.

You can get torrent only for objects that are less than 5 GB in size and that are not encrypted using server-side encryption with customer-provided encryption key.

To use this operation, you must have READ access to the object.

Following is the proxy configuration for getObjectTorrent.

**getObjectTorrent**
```
<amazons3.getObjectTorrent>
    <objectName>{$ctx:objectName}</objectName>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getObjectTorrent>
```

**Properties**
* bucketUrl: The URL of the bucket.
* objectName: The name of the object.

**Sample request**
Following is a sample REST request that can be handled by the getObjectTorrent operation.

```
<getObjectTorrent>
    <accessKeyId>ASDKIAJ5H4DUFAASDF3O2VJMLDFDA</accessKeyId>
    <secretAccessKey>oz2sdf2F2mwtR+JGCGaykQmKHZjdfg56zhUiiEb48Fynwt</secretAccessKey>
    <methodType>GET</methodType>
    <contentType>application/xml; charset=UTF-8</contentType>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <contentMD5></contentMD5>
    <expect>100-continue</expect>
    <contentLength></contentLength>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead>GrantRead</xAmzGrantRead>
    <xAmzGrantWrite>Grantwrite</xAmzGrantWrite>
    <xAmzGrantReadAcp>GrantReadAcp</xAmzGrantReadAcp>
    <xAmzGrantWriteAcp>GrantWriteAcp</xAmzGrantWriteAcp>
    <xAmzGrantFullControl>GrantFullControl</xAmzGrantFullControl>
    <objectName>testFile.txt</objectName>
</getObjectTorrent>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtorrent.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtorrent.html)

### Restoring specific object version
The restoreObject operation restores a temporary copy of an archived object. You can optionally provide version ID to restore specific object version. If version ID is not provided, it will restore the current version. The number of days that you want the restored copy will be determined by numberOfDays. After the specified period, Amazon S3 deletes the temporary copy. Note that the object remains archived; Amazon S3 deletes only the restored copy.

An object in the Glacier storage class is an archived object. To access the object, you must first initiate a restore request, which restores a copy of the archived object. Restore jobs typically complete in three to five hours.

Following is the proxy configuration for restoreObject.

**restoreObject**
```
<amazons3.restoreObject>
    <objectName>{$ctx:objectName}</objectName>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <numberOfDays>{$ctx:numberOfDays}</numberOfDays>
    <versionId>{$ctx:versionId}</versionId>
</amazons3.restoreObject>
```

**Properties**
* bucketUrl: The URL of the bucket.
* objectName: The name of the object.
* numberOfDays: Lifetime of the restored (active) copy.
* versionId: Specifies the object version.

**Sample request**
Following is a sample REST request that can be handled by the restoreObject operation.

```
<restoreObject>
    <accessKeyId>ASDKIAJ5H4DUFAASDF3O2VJMLDFDA</accessKeyId>
    <secretAccessKey>oz2sdf2F2mwtR+JGCGaykQmKHZjdfg56zhUiiEb48Fynwt</secretAccessKey>
    <methodType>POST</methodType>
    <contentType>application/xml; charset=UTF-8</contentType>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <contentMD5></contentMD5>
    <expect>100-continue</expect>
    <contentLength></contentLength>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead>GrantRead</xAmzGrantRead>
    <xAmzGrantWrite>Grantwrite</xAmzGrantWrite>
    <xAmzGrantReadAcp>GrantReadAcp</xAmzGrantReadAcp>
    <xAmzGrantWriteAcp>GrantWriteAcp</xAmzGrantWriteAcp>
    <xAmzGrantFullControl>GrantFullControl</xAmzGrantFullControl>
    <objectName>testFile.txt</objectName>
    <numberOfDays>7</numberOfDays>
    <versionId></versionId>
</restoreObject>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)

### Uploading a part by copying data from an existing object
The uploadPartCopy operation uploads a part by copying data from an existing object as data source. You specify the data source by adding the x-amz-copy-source in your request and a byte range by adding the x-amz-copy-source-range in your request. The minimum allowable part size for a multipart upload is 5 MB.

**uploadPartCopy**
```
<amazons3.uploadPartCopy>
    <objectName>{$ctx:objectName}</objectName>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <uploadId>{$ctx:uploadId}</uploadId>
    <partNumber>{$ctx:partNumber}</partNumber>
</amazons3.uploadPartCopy>
```

**Properties**
* bucketUrl: The URL of the bucket.
* objectName: Name of the object.
* uploadId: Upload ID.
* partNumber: Part number that identifies the part.

**Sample request**
Following is a sample REST request that can be handled by the uploadPartCopy operation.

```
<uploadPartCopy>
    <accessKeyId>ASDKIAJ5H4DUFAASDF3O2VJMLDFDA</accessKeyId>
    <secretAccessKey>oz2sdf2F2mwtR+JGCGaykQmKHZjdfg56zhUiiEb48Fynwt</secretAccessKey>
    <methodType>PUT</methodType>
    <contentType>application/xml; charset=UTF-8</contentType>
    <contentLength>256</contentLength>
    <contentMD5></contentMD5>
    <objectName>testFile1.txt</objectName>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <expect></expect>
    <uploadId>SsNUDqUklMaoV_IfePCpGAZHjaxJx.cGXEcX6TVW4I6WzOQFnAKomYevz5qi5LtkfTvlpwjY9M6QDGsIIvdGEQzBURo3MMU2Yh.ZEQDsk_lsnx3Z8m9jsglW6FIfKGQ_</uploadId>
    <partNumber>2</partNumber>
    <uriRemainder>/testFile1.txt?partNumber=2&amp;uploadId=SsNUDqUklMaoV_IfePCpGAZHjaxJx.cGXEcX6TVW4I6WzOQFnAKomYevz5qi5LtkfTvlpwjY9M6QDGsIIvdGEQzBURo3MMU2Yh.ZEQDsk_lsnx3Z8m9jsglW6FIfKGQ_</uriRemainder>
    <xAmzCopySource>/testBucket1/testFile.jpg</xAmzCopySource>
    <xAmzCopySourceRange>bytes=0-9</xAmzCopySourceRange>
    <xAmzCopySourceIfMatch></xAmzCopySourceIfMatch>
    <xAmzCopySourceIfNoneMatch></xAmzCopySourceIfNoneMatch>
    <xAmzCopySourceIfUnmodifiedSince></xAmzCopySourceIfUnmodifiedSince>
    <xAmzCopySourceIfModifiedSince></xAmzCopySourceIfModifiedSince>
</uploadPartCopy>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html)

### Retrieving metadata from an object without returning the object.
The headObject operation retrieves metadata from an object without returning the object itself. This operation is useful if you are interested only in an object's metadata. To use this operation, you must have READ access to that object. A HEAD request has the same options as a GET operation on an object. The response is identical to the GET response except that there is no response body.

**headObject**
```
<amazons3.headObject>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <objectName>{$ctx:objectName}</objectName>
    <range>{$ctx:range}</range>
    <ifModifiedSince>{$ctx:ifModifiedSince}</ifModifiedSince>
    <ifUnmodifiedSince>{$ctx:ifUnmodifiedSince}</ifUnmodifiedSince>
    <ifMatch>{$ctx:ifMatch}</ifMatch>
    <ifNoneMatch>{$ctx:ifNoneMatch}</ifNoneMatch>
</amazons3.headObject>
```

**Properties**
* objectName: Name of the object.
* bucketUrl: The URL of the bucket.
* range: Optional - The specified range bytes of an object to download.
* ifModifiedSince: Optional - Return the object only if it has been modified since the specified time.
* ifUnmodifiedSince: Optional - Return the object only if it has not been modified since the specified time.
* ifMatch: Optional - Return the object only if its entity tag (ETag) is the same as the one specified.
* ifNoneMatch: Optional - Return the object only if its entity tag (ETag) is different from the one specified.

**Sample request**
Following is a sample REST request that can be handled by the headObject operation.

```
<headObject>
  <accessKeyId>AKIAIGURZMDFG7TRO6KQ</accessKeyId>
  <secretAccessKey>asAX8CJoDKzdfg0Ve5dMCFk4STUFDRHkGX6m0CcY</secretAccessKey>
  <methodType>PUT</methodType>
  <contentLength>256</contentLength>
  <contentType>application/xml</contentType>
  <contentMD5></contentMD5>
  <expect></expect>
  <region>us-east-2</region>
  <host>s3.us-east-2.amazonaws.com</host>
  <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
  <bucketName>signv4test</bucketName>
  <isXAmzDate>true</isXAmzDate>
  <xAmzSecurityToken></xAmzSecurityToken>
  <objectName>testObject2</objectName>
  <xAmzAcl></xAmzAcl>
  <xAmzGrantRead></xAmzGrantRead>
  <xAmzGrantWrite></xAmzGrantWrite>
  <xAmzGrantReadAcp></xAmzGrantReadAcp>
  <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
  <xAmzGrantFullControl></xAmzGrantFullControl>
  <range></range>
  <ifModifiedSince></ifModifiedSince>
  <ifMatch></ifMatch>
  <ifNoneMatch></ifNoneMatch>
  <ifUnmodifiedSince></ifUnmodifiedSince>
</headObject>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html)

## Sample configuration
Following is a sample proxy service that illustrates how to connect to Amazon S3 with the init operation and use the deleteObject operation. The sample request for this proxy can be found in deleteObject sample request. You can use this sample as a template for using other operations in this category.

**Sample Proxy**
```
<proxy xmlns="http://ws.apache.org/ns/synapse"
       name="amazons3_deleteObject"
       transports="https,http"
       statistics="disable"
       trace="disable"
       startOnLoad="true">
   <target>
      <inSequence onError="faultHandlerSeq">
         <property name="accessKeyId" expression="//accessKeyId/text()"/>
         <property name="secretAccessKey" expression="//secretAccessKey/text()"/>
         <property name="methodType" expression="//methodType/text()"/>
         <property name="contentType" expression="//contentType/text()"/>
         <property name="bucketName" expression="//bucketName/text()"/>
         <property name="isXAmzDate" expression="//isXAmzDate/text()"/>
         <property name="contentMD5" expression="//contentMD5/text()"/>
         <property name="xAmzSecurityToken" expression="//xAmzSecurityToken/text()"/>
         <property name="host" expression="//host/text()"/>
         <property name="region" expression="//region/text()"/>
         <property name="expect" expression="//expect/text()"/>
         <property name="contentLength" expression="//contentLength/text()"/>
         <property name="xAmzMfa" expression="//xAmzMfa/text()"/>
         <property name="bucketUrl" expression="//bucketUrl/text()"/>
         <property name="objectName" expression="//objectName/text()"/>
         <property name="versionId" expression="//versionId/text()"/>
         <amazons3.init>
            <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
            <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
            <methodType>{$ctx:methodType}</methodType>
            <contentType>{$ctx:contentType}</contentType>
            <bucketName>{$ctx:bucketName}</bucketName>
            <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
            <contentMD5>{$ctx:contentMD5}</contentMD5>
            <xAmzSecurityToken>{$ctx:xAmzSecurityToken}</xAmzSecurityToken>
            <host>{$ctx:host}</host>
            <region>{$ctx:region}</region>
            <expect>{$ctx:expect}</expect>
            <contentLength>{$ctx:contentLength}</contentLength>
            <xAmzMfa>{$ctx:xAmzMfa}</xAmzMfa>
         </amazons3.init>
         <amazons3.deleteObject>
            <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
            <objectName>{$ctx:objectName}</objectName>
            <versionId>{$ctx:versionId}</versionId>
         </amazons3.deleteObject>
         <respond/>
      </inSequence>
      <outSequence>
        <respond/>
      </outSequence>
   </target>
   <description/>
</proxy>
```