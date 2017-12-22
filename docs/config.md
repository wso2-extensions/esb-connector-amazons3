# Configuring Amazon S3 Operations
[[Obtaining user credentials]](#obtaining-user-credentials) [[Initializing the Connector]](#initializing-the-Connector)

> NOTE: To work with the Amazon S3 connector,Amazon S3 account. If you don't already have one, Go to [https://aws.amazon.com/s3/](https://aws.amazon.com/s3/) and choose Get started with Amazon S3 and follow the on-screen instructions. AWS will notify you by email when your account is active and available for you to use.

## Obtaining user credentials

You can access the Amazon S3 service using the root user credentials. But these credentials allow full access to all resources in the account. Because you can't restrict permissions for root user credentials. You can create [IAM(Identity and Access Management) users](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html) to securely control access to AWS services and resources for users in your AWS account.

* **Follow the steps below to create a new secret access key for your AWS root account:**
  1. Go to the AWS Management Console.
  2. Hover over your company name in the right top menu and click "My Security Credentials".
  3. Scroll to the "Access Keys" section.
  4. Click on "Create New Access Key".
  5. Copy both the Access Key ID (YOUR_AMAZON_S3_KEY) and Secret Access Key (YOUR_AMAZON_S3_SECRET).

* **Follow the steps below to create a new secret access key for an IAM user account:**

  1. Sign in to the AWS Management Console and open the IAM console.
  2. In the navigation pane, choose Users.
  3. Add a checkmark next to the name of the desired user, and then choose User Actions from the top. Please note that the selected user must have read and write access to the AWS S3 bucket for the migration.
  4. Click on Manage Access Keys.
  5. Click on Create Access Key.
  6. Click on Show User Security Credentials. Copy and paste the Access Key ID and Secret Access Key values, or click on Download Credentials to download the credentials in a CSV (file).

## Initializing the Connector

To use the Amazon S3 connector, add the <amazons3.init> element in your configuration before carrying out any Amazon S3 operations. This Amazon S3 configuration authenticates with Amazon S3 by specifying the AWS access key ID and secret access key ID, which are used for every operation. The signature is used with every request and thus differs based on the request the user makes.

**init**
```xml
<amazons3.init>
    <accessKeyId>{$ctx:accessKeyId}</accessKeyId>
    <secretAccessKey>{$ctx:secretAccessKey}</secretAccessKey>
    <methodType>{$ctx:methodType}</methodType>
    <region>{$ctx:region}</region>
    <contentType>{$ctx:contentType}</contentType>
    <addCharset>{$ctx:addCharset}</addCharset>
    <bucketName>{$ctx:bucketName}</bucketName>
    <isXAmzDate>{$ctx:isXAmzDate}</isXAmzDate>
    <expect>{$ctx:expect}</expect>
    <contentMD5>{$ctx:contentMD5}</contentMD5>
    <xAmzSecurityToken>{$ctx:xAmzSecurityToken}</xAmzSecurityToken>
    <contentLength>{$ctx:contentLength}</contentLength>
    <host>{$ctx:host}</host>
    <xAmzAcl>{$ctx:xAmzAcl}</xAmzAcl>
    <xAmzGrantRead>{$ctx:xAmzGrantRead}</xAmzGrantRead>
    <xAmzGrantWrite>{$ctx:xAmzGrantWrite}</xAmzGrantWrite>
    <xAmzGrantReadAcp>{$ctx:xAmzGrantReadAcp}</xAmzGrantReadAcp>
    <xAmzGrantWriteAcp>{$ctx:xAmzGrantWriteAcp}</xAmzGrantWriteAcp>
    <xAmzGrantFullControl>{$ctx:xAmzGrantFullControl}</xAmzGrantFullControl>
    <uriRemainder>{$ctx:uriRemainder}</uriRemainder>
    <xAmzCopySource>{$ctx:xAmzCopySource}</xAmzCopySource>
    <xAmzCopySourceRange>{$ctx:xAmzCopySourceRange}</xAmzCopySourceRange>
    <xAmzCopySourceIfMatch>{$ctx:xAmzCopySourceIfMatch}</xAmzCopySourceIfMatch>
    <xAmzCopySourceIfNoneMatch>{$ctx:xAmzCopySourceIfNoneMatch}</xAmzCopySourceIfNoneMatch>
    <xAmzCopySourceIfUnmodifiedSince>{$ctx:xAmzCopySourceIfUnmodifiedSince}</xAmzCopySourceIfUnmodifiedSince>
    <xAmzCopySourceIfModifiedSince>{$ctx:xAmzCopySourceIfModifiedSince}</xAmzCopySourceIfModifiedSince>
    <cacheControl>{$ctx:cacheControl}</cacheControl>
    <contentEncoding>{$ctx:contentEncoding}</contentEncoding>
    <expires>{$ctx:expires}</expires>
    <xAmzMeta>{$ctx:xAmzMeta}</xAmzMeta>
    <xAmzServeEncryption>{$ctx:xAmzServeEncryption}</xAmzServeEncryption>
    <xAmzStorageClass>{$ctx:xAmzStorageClass}</xAmzStorageClass>
    <xAmzWebsiteLocation>{$ctx:xAmzWebsiteLocation}</xAmzWebsiteLocation>
 </amazons3.init>
```

**Properties**
* accessKeyId: AWS access key ID.
* secretAccessKey: AWS secret access key.
* region: Region which is used select a regional endpoint to make requests.
* methodType: Type of the HTTP method.
* contentLength: Length of the message without the headers according to RFC 2616.
* contentType: The content type of the resource in case the request content in the body.
* addCharset: To add the char set to ContentType header. Set to true to add the charset in the ContentType header of POST and HEAD methods when you are using the connector with ESB 4.9.0.
* host: For path-style requests, the value is s3.amazonaws.com. For virtual-style requests, the value is BucketName.s3.amazonaws.com.
* isXAmzDate: The current date and time according to the requester.
* bucketName: Name of the bucket require.
* blocking: The blocking parameter is helping connector performs the blocking invocations to Amazon S3
* privateKeyFilePath: Path of AWS private Key File.
* keyPairId: Key pair Id of AWS cloud Front.
* policyType: Policy for the URL signing. It can be custom or canned policy.
* urlSign: Specify whether to create Signed URL or not. It can be true or false.
* dateLessThan: Can access the object before this specific date only.
* dateGreaterThan: Can access the object before this specific date only.
* ipAddress: IP address for creating Policy.
* contentMD5: Base64 encoded 128-bit MD5 digest of the message without the headers according to RFC 1864.
* expect: This header can be used only if a body is sent to not to send the request body until it recieves an acknowledgment.
* isXAmzDate: The current date and time according to the requester.
* xAmzSecurityToken: The security token based on whether using Amazon DevPay operations or temporary security credentials.
* xAmzAcl: Sets the ACL of the bucket using the specified canned ACL.
* xAmzGrantRead: Allows the specified grantee or grantees to list the objects in the bucket.
* xAmzGrantWrite: Allows the specified grantee or grantees to create, overwrite, and delete any object in the bucket.
* xAmzGrantReadAcp: Allows the specified grantee or grantees to read the bucket ACL.
* xAmzGrantWriteAcp: Allows the specified grantee or grantees to write the ACL for the applicable bucket.
* xAmzGrantFullControl: Allows the specified grantee or grantees the READ, WRITE, READ_ACP, and WRITE_ACP permissions on the bucket.
* xAmzMeta: Field names prefixed with x-amz-meta- contain user-specified metadata.
* xAmzServeEncryption: Specifies server-side encryption algorithm to use when Amazon S3 creates an object.
* xAmzStorageClass: Storage class to use for storing the object.
* xAmzWebsiteLocation: Amazon S3 stores the value of this header in the object metadata.
* xAmzMfa: The value is the concatenation of the authentication device's serial number, a space, and the value that is displayed on your authentication device.
* xAmzCopySource: The name of the source bucket and key name of the source object, separated by a slash.
* xAmzCopySourceRange: The range of bytes to copy from the source object.
* xAmzMetadataDirective: Specifies whether the metadata is copied from the source object or replaced with metadata provided in the request.
* xAmzCopySourceIfMatch: Copies the object if its entity tag (ETag) matches the specified tag.
* xAmzCopySourceIfNoneMatch: Copies the object if its entity tag (ETag) is different than the specified ETag.
* xAmzCopySourceIfUnmodifiedSince: Copies the object if it hasn't been modified since the specified time.
* xAmzCopySourceIfModifiedSince: Copies the object if it has been modified since the specified time.
* xAmzServerSideEncryption: Specifies the server-side encryption algorithm to use when Amazon S3 creates the target object.

> Note: You need to pass the bucketName within init configuration only if you use the bucketURL in path-style(Eg: BucketName.s3.amazonaws.com). For the virtual-style bucketUrl(Eg: s3.amazonaws.com) you should not pass the bucketName.

Now that you have connected to Amazon S3, use the information in the following topics to perform various operations with the connector.

* [Working with Buckets in Amazon S3](buckets.md)
* [Working with Objects in Amazon S3](objects.md)
* [Getting an Object with URL Signing](urlSigned.md)