# Getting an Object with URL Signing
The Amazon S3 connector can be used to signed the URL using two kind of policy such as "Canned" and "Custom" to get the objects from the cloud Front in secure manner. CloudFront integrates with other Amazon Web Services products to give developers and businesses an easy way to accelerate content to end users with no minimum usage commitments.

### Getting started
To get started, go to Configuring Amazon S3 URL  Signing . Once you have completed your configurations, you can perform Get Object with URL signing Operation with the connector.

### Configuring Amazon S3 URL Signing

1. Create the Key pair Id by Signing in to the AWS Management Console using the root credentials for an AWS account.
    1. On the account-name menu, click Security Credentials.
    2. Expand CloudFront Key Pairs.
    3. Confirm that you have no more than one active key pair. You can't create a key pair if you already have two active key pairs.
    4. Click Create New Key Pair.
    5. In the Create Key Pair dialog box, click Download Private Key File.
    6. Record the key pair ID for your key pair. (In the AWS Management Console, this is called the access key ID.)
2. You will get private key in .pem format. So need to change that to .der form. To that use following command.

         $ openssl pkcs8 -topk8 -nocrypt -in origin.pem -inform PEM -out new.der -outform DER
3. Save that file in registry of ESB. For that you need to start the ESB and Go to Registry and click on browse. Expand system-> Governance. Create a collection and Go to that collection and create a Resource. Upload your private key file and give a name and click on Add. You can get the location of the file in registry by clicking on the file and see the value in Location.
4. Create Distribution for the amazon s3 bucket as we need to use it in CloudFront.
    1. Sign in to the AWS Management Console and open the CloudFront console at [https://console.aws.amazon.com/cloudfront/](https://console.aws.amazon.com/cloudfront/).
    2. Choose Create Distribution.
    3. On the first page of the Create Distribution Wizard, in the Web section or RTMP section, choose Get Started.
    4. Specify settings for the distribution. For more information, see Values that You Specify When You Create or Update a Web Distribution.
    5. Choose Create Distribution.
    6. Wait until 15-20 minutes to change the status to progress to deployed.  If you choose default user name, you will get a URL like  [http://d111111abcdef8.cloudfront.net](http://d111111abcdef8.cloudfront.net)
5. Adding Trusted Signers to Your Distribution Using the CloudFront Console.
    1. If you want to use other AWS accounts, get the AWS account ID for each account:
        1. Sign in to the AWS Management Console at [https://console.aws.amazon.com/console/home](https://console.aws.amazon.com/console/home) using an account that you want to use as a trusted signer.
        2. In the upper-right corner of the console, click the name associated with the account, and click My Account.
        3. Under Account Settings, make note of the account ID.
    2. Open the Amazon CloudFront console at [https://console.aws.amazon.com/cloudfront/](https://console.aws.amazon.com/cloudfront/), and sign in using the account that you used to create the distribution that you want to add trusted signers to.
    3. Click the distribution ID.
    4. Change to edit mode:
        1. Web distributions – Click the Behaviors tab, click the behavior that you want to edit, and click Edit.
        2. RTMP distributions – Click Edit.
    5. For Restrict Viewer Access (Use Signed URLs or Signed Cookies), click Yes.
    6. For Trusted Signers, check the applicable check boxes:
        1. Self – Check this check box if you want to use the current account (the account that you used to create the distribution).
        2. Specify Accounts – Check this check box if you want to use other AWS accounts.
    7. If you checked the Specify Accounts check box, enter AWS account IDs in the AWS Account Number field. These are the account IDs that you got in the first step of this procedure. Enter one account ID per line.
    8. Click Yes, Edit.
6. Creating a CloudFront Origin Access Identity.
    1. Got to CloudFront Console and click on the ID of the distribution you have been created.
    2. Change to edit mode:
        1. Web distributions – Click the Origins tab, select the origin that you want to edit, and click Edit. You can only create an origin access identity for origins for which Origin Type is S3 Origin.
        2. RTMP distributions – Click Edit.
    3. For Restrict Bucket Access (Use Signed URLs or Signed Cookies), click Yes.
    4. If you already have an origin access identity that you want to use, click Use an Existing Identity. Then select the identity in the Your Identities list. If you want to create an identity, click Create a New Identity. Then enter a description for the identity in the Comment field.
    5. If you want CloudFront to automatically give the origin access identity permission to read the objects in the Amazon S3 bucket specified in Origin Domain Name, click Yes, Update Bucket Policy. If you want to manually update permissions on your Amazon S3 bucket, click No, I Will Update Permissions. For that:
        1. Go to Amazon S3 management console and right click on the bucket and select properties.
        2. Then go to permissions tab and you can edit bucket policy.
7. Upload new object into the bucket.
8. Download bcprov-ext-jdk15on-155.jar   from [https://downloads.bouncycastle.org/java/bcprov-ext-jdk15on-155.jar[(https://downloads.bouncycastle.org/java/bcprov-ext-jdk15on-155.jar)
9. Download jets3t-0.9.0.zip  from [http://bitbucket.org/jmurty/jets3t/downloads/jets3t-0.9.0.zip](http://bitbucket.org/jmurty/jets3t/downloads/jets3t-0.9.0.zip) and unzip it. After unzip go to ../jets3t-0.9.0/jars and take the  jets3t-0.9.0. jar.
10. Copy these three JAR files to the <ESB_HOME>/repository/components/lib directory.

### Getting an object with URL sign
The getObject operation retrieves objects from Amazon S3. This operation can be done using URL signing also. You need to do above steps to work with Get Objects with URL sign.

When calling init before this operation, the following headers should be removed: xAmzAcl, xAmzGrantRead, xAmzGrantWrite, xAmzGrantReadAcp, xAmzGrantWriteAcp, and xAmzGrantFullControl. In init method we need to attend some values as specifies below.

**getObject**
```
<amazons3.init>
    <methodType>{$ctx:methodType}</methodType>
    <contentType>application/xml</contentType>
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
    <privateKeyFilePath>{$ctx:privateKeyFilePath}</privateKeyFilePath>
    <keyPairId>{$ctx:keyPairId}</keyPairId>
    <policyType>{$ctx:policyType}</policyType>
    <urlSign>{$ctx:urlSign}</urlSign>
    <dateLessThan>{$ctx:dateLessThan}</dateLessThan>
    <dateGreaterThan>{$ctx:dateGreaterThan}</dateGreaterThan>
    <ipAddress>{$ctx:ipAddress}</ipAddress>
    </amazons3.init>
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

* methodType: HTTP method type.
* contentType: Content type of the resource.
* bucketName: Name of the bucket.
* isXAmzDate: Indicates whether the current date and time are considered to calculate the signature. Valid values: true or false.
* expect: This header can be used only if a request is sent not to send the request body until it receives an acknowledgement. Valid value: 100-continue
* contentMD5: Base64-encoded 128-bit MD5 digest of the message according to RFC 1864.
* xAmzSecurityToken: The security token based on whether Amazon DevPay operations or temporary security credentials are used.
* contentLength: Length of the message without the headers according to RFC 2616.
* host: The path-style requests (s3.amazonaws.com) or virtual-style requests (BucketName.s3.amazonaws.com).
* xAmzAcl: Sets the ACL of the bucket using the specified canned ACL.
* xAmzGrantRead: Allows the specified grantee or grantees to list the objects in the bucket.
* xAmzGrantWrite: Allows the specified grantee or grantees to create, overwrite, and delete any object in the bucket.
* xAmzGrantReadAcp: Allows the specified grantee or grantees to read the bucket ACL.
* xAmzGrantWriteAcp: Allows the specified grantee or grantees to write the ACL for the applicable bucket.
* xAmzGrantFullControl: Allows the specified grantee or grantees the READ, WRITE, READ_ACP, and WRITE_ACP permissions on the bucket.
* ipAddress:IP address for creating Policy.
* policyType: Policy for the URL signing. It can be custom or canned policy.
* keyPairId: Key pair Id of AWS cloud Front.
* urlSign: Specify whether to create Signed URL or not. It can be true or false.
* dateLessThan: Can access the object before this specific date only.
* dateGreaterThan: Can access the object before this specific date only.
* privateKeyFilePath:Path of AWS private Key File.
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
    <methodType>GET</methodType>
    <contentType>application/xml</contentType>
    <bucketName>testurlsignaturebucket</bucketName>
     <dateLessThan>2016-11-14T22:20:00.000Z</dateLessThan>
      <dateGreaterThan>2016-10-16T06:31:56.000Z</dateGreaterThan>
       <ipAddress>0.0.0.0</ipAddress>
    <isXAmzDate>true</isXAmzDate>
    <privateKeyFilePath>amazon/new.der</privateKeyFilePath>
    <keyPairId>APKXXXXXXXk</keyPairId>
      <policyType>canned</policyType>
      <urlSign>true</urlSign>
    <xAmzSecurityToken/>
    <contentMD5/>
    <bucketUrl>http://dXXXXXXX.cloudfront.net</bucketUrl>
    <objectName>test.txt</objectName>
    <host>s3.amazonaws.com</host>
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

**Related Amazon S3 Get Object using Signed URL documentation**
[http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/private-content-trusted-signers.html#private-content-rotating-key-pairs](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/private-content-trusted-signers.html#private-content-rotating-key-pairs)

**Additional information**
For general information on using connectors and their operations in your ESB configurations, see Using a Connector . To download the connector, go to [https://store.wso2.com/store/assets/esbconnector/details/amazons3](https://store.wso2.com/store/assets/esbconnector/details/544bc219-1c53-4fa0-b290-0957881b2445) , and click Download Connector. Then you can add and enable the connector in your ESB instance.
