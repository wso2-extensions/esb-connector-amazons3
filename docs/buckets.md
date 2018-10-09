# Working with Buckets in Amazon S3
[[  Overview ]](#overview)  [[ Operation details ]](#operation-details)  [[  Sample configuration  ]](#sample-configuration)

### Overview
The following operations allow you to work with buckets in Amazon S3. Click an operation name to see details on how to use it.
For a sample proxy service that illustrates how to work with buckets, see [Sample configuration](#sample-configuration).

| Operation        | Description |
| ------------- |-------------|
| [getBuckets](#getting-all-buckets-owned-by-the-authenticated-sender)    | Returns a list of all buckets owned by the authenticated sender of the request. |
| [createBucket](#creating-a-bucket)      | Creates a new bucket.  |
| [createBucketWebsiteConfiguration](#creating-a-bucket-website-configuration)      | Sets the website configuration for a bucket.  |
| [createBucketPolicy](#creating-the-bucket-policy)     | Adds or replaces a policy on a bucket.  |
| [createBucketACL](#setting-the-permissions-on-an-existing-bucket)     | Set the permissions on an existing bucket using access control lists.  |
| [createBucketLifecycle](#setting-the-lifecycle-for-an-existing-bucket)     | Creates a new lifecycle configuration for the bucket or replaces an existing lifecycle configuration.  |
| [createBucketReplication](#creating-a-new-replication-configuration)     | Creates a new replication configuration.  |
| [createBucketTagging](#adding-a-set-of-tags-to-an-existing-bucket)     | Adds a set of tags to an existing bucket.  |
| [createBucketRequestPayment](#setting-the-request-payment-configuration-of-a-bucket)     | Set the request configuration of a bucket.  |
| [createBucketVersioning](#setting-the-versioning-state-of-an-existing-bucket  )     | Set the versioning state of an existing bucket.  |
| [createBucketCors](#setting-the-cors-configuration-for-the-bucket)     | Sets the cors configuration for your bucket.  |
| [deleteBucket](#deleting-a-bucket)     | Deletes a specified bucket.  |
| [deleteBucketPolicy](#deleting-the-bucket-policy)     | Deletes the policy on a specified bucket.  |
| [deleteBucketCors](#deleting-the-cors-configuration-of-an-existing-bucket)     | Deletes the corsconfiguration information set for the bucket.  |
| [deleteBucketLifecycle](#deleting-the-lifecycle-configuration-from-a-bucket)     | Deletes the lifecycle configuration from the specified bucket.  |
| [deleteBucketReplication](#deleting-the-replication-subresource-associated-with-a-bucket)     | Deletes the replication sub-resource associated with the specified bucket.  |
| [deleteBucketTagging](#deleting-the-tag-set-from-a-bucket)     | Removes a tag set from the specified bucket.  |
| [deleteBucketWebsiteConfiguration](#deleting-the-bucket-website)     | Removes the website configuration for a bucket.  |
| [getObjectsInBucket](#getting-objects-in-a-bucket)     | Returns some or all (up to 1000) of the objects in a bucket.  |
| [getBucketLifeCycle](#getting-the-bucket-lifecycle)     | Returns the lifecycle configuration information set on the bucket.  |
| [getBucketPolicy](#getting-the-bucket-policy)     | Returns the policy of a specified bucket.  |
| [getBucketObjectVersions](#getting-bucket-object-versions)     | Lists metadata about all of the versions of objects in a bucket.  |
| [getBucketCors](#getting-the-bucket-cors)     | Returns the corsconfiguration information set for the bucket.  |
| [getBucketLocation](#getting-the-bucket-location)     | Returns a bucket's region.  |
| [getBucketLogging](#getting-the-bucket-logging)     | Returns the logging status of a bucket.  |
| [getBucketNotification](#getting-the-bucket-notification)     | Returns the notification configuration of a bucket.  |
| [getBucketReplication](#getting-the-bucket-replication)     | Returns replication configuration information set on the bucket.  |
| [getBucketTagging](#getting-the-bucket-tagging)     | Returns the tag set associated with the bucket.  |
| [getBucketRequestPayment](#getting-the-request-payment-configuration-of-a-bucket)     | Returns the request payment configuration of a bucket.  |
| [getBucketVersioning](#getting-bucket-versioning)     | Returns the versioning state of a bucket.  |
| [getWebSiteConfiguration](#getting-the-bucket-website)     | Returns the website configuration associated with a bucket.  |
| [getBucketACL](#getting-the-bucket-access-control-list(ACL))  |	Returns the access control list (ACL) of a bucket.  |
| [checkBucketPermission](#checking-bucket-permissions)     | Determines whether a bucket exists and you have permission to access it.  |
| [setBucketACL](#setting-a-bucket-ACL)     | Sets the permissions on an existing bucket using access control lists (ACL).  |
| [headBucket](#determining-the-existence-of-a-bucket-and-permission-to-access-it)     | To determine if a bucket exists and you have permission to access it.  |
| [listMultipartUploads](#listing-multipart-uploads)     | Lists in-progress multipart uploads.  |

## Operation details
This section provides details on each of the operations.

### Getting all buckets owned by the authenticated sender
The getBuckets implementation of the GET operation returns a list of all buckets owned by the authenticated sender of the request. To authenticate a request, use a valid AWS Access Key ID that is registered with Amazon S3. Anonymous requests cannot list buckets, and a user cannot list buckets that were not created by that particular user.
When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBuckets**
```xml
<amazons3.getBuckets>
  <apiUrl>{$ctx:apiUrl}</apiUrl>
  <region>{$ctx:region}</region>
<amazons3.getBuckets>
```

**Properties**
* apiUrl: Amazon S3 API URL, e.g., http://s3.amazonaws.com

**Sample request**

Following is a sample REST request that can be handled by the getBuckets operation.
```xml
<getBuckets>
    <accessKeyId>AKIAXXXXXXXXXXQM7G5EA</accessKeyId>
    <secretAccessKey>qHZBBzXXXXXXXXXXDYQc4oMQMnAOj+34XXXXXXXXXXO2s</secretAccessKey>
    <methodType>GET</methodType>
    <contentLength></contentLength>
    <contentType>application/xml</contentType>
    <contentMD5></contentMD5>
    <expect>100-continue</expect>
    <host>s3.amazonaws.com</host>
    <region>us-east-1</region>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <apiUrl>https://s3.amazonaws.com</apiUrl>
</getBuckets>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTServiceGET.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTServiceGET.html)

### Creating a bucket

The createBucket implementation of the PUT operation creates a new bucket. To create a bucket, the user should be registered with Amazon S3 and have a valid AWS Access Key ID to authenticate requests. Anonymous requests are never allowed to create buckets. By creating the bucket, the user becomes the owner of the bucket. Not every string is an acceptable bucket name. For information on bucket naming restrictions, see [Working with Amazon S3 Buckets](http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html).
By default, the bucket is created in the US Standard region. The user can optionally specify a region in the request body. For example, if the user resides in Europe, the user will probably find it advantageous to create buckets in the EU (Ireland) region. For more information, see [How to Select a Region for Your Buckets](http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html#access-bucket-intro).

**createBucket**
```
<amazons3.createBucket>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
  <bucketRegion>{$ctx:bucketRegion}</bucketRegion>
<amazons3.createBucket>
```

**Properties**
* bucketUrl: The URL of the bucket.
* bucketRegion: The region for the created bucket.

**Notes**
* When creating a bucket using the createBucket operation, the user can optionally specify the accounts or groups that should be granted specific permissions on the bucket. There are two ways to grant the appropriate permissions using the request headers: specify a canned ACL using the "x-amz-acl" request header or specify access permissions explicitly using the "x-amz-grant-read", "x-amz-grant-write", "x-amz-grant-read-acp", "x-amz-grant-write-acp", and "x-amz-grant-full-control" headers. These headers map to the set of permissions Amazon S3 supports in an ACL. Use only one of these approaches.

**Sample request**
Following is a sample REST request that can be handled by the createBucket operation.

```
<createBucket>
    <accessKeyId>AJPVXXXXXXXXXX7G5EA</accessKeyId>
    <secretAccessKey>qHZBXXXXXXXXYQc4oMQMnAOj+3XXXXXXXX2s</secretAccessKey>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <addCharset>false</addCharset>
    <isXAmzDate>true</isXAmzDate>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <bucketName>signv4test</bucketName>
    <region>us-east-2</region>
    <host>s3.us-east-2.amazonaws.com</host>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <bucketRegion>us-east-2</bucketRegion>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
</createBucket>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html)

### Creating a bucket website configuration

The createBucketWebsiteConfiguration operation sets the configuration of the website that is specified in the websiteConfig property. A bucket could be configured as a website by adding this sub-resource on the bucket with website configuration information such as the file name of the index document and any redirect rules.
This operation requires the S3:PutBucketWebsite permission. By default, only the bucket owner can configure the website attached to a bucket. However, the owners can allow other users to set the website configuration by writing a bucket policy that grants them the S3:PutBucketWebsite permission.
When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**createBucketWebsiteConfiguration**
```
<amazons3.createBucketWebsiteConfiguration>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <websiteConfig>{$ctx:websiteConfig}</websiteConfig>
</amazons3.createBucketWebsiteConfiguration>
```

**Properties**
* bucketUrl: The URL of the bucket.
* websiteConfig: Required - Website configuration information. For information on the elements you use in the request to specify the website configuration, see [http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTwebsite.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTwebsite.html).

**Sample request**
Following is a sample REST request that can be handled by the createWebsiteConfiguration operation.
```
<createBucketWebsiteConfiguration>
    <accessKeyId>AKIXXXXXXXXXXA</accessKeyId>
    <secretAccessKey>qHZXXXXXXQc4oMQMnAOj+340XXxO2s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>PUT</methodType>
    <contentLength>256</contentLength>
    <contentType>application/xml</contentType>
    <contentMD5></contentMD5>
    <expect></expect>
    <host>s3.us-east-2.amazonaws.com</host>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <bucketName>signv4test</bucketName>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <websiteConfig>
        <IndexDocument>
                <Suffix>index2.html</Suffix>
        </IndexDocument>
        <ErrorDocument>
                <Key>Error2.html</Key>
        </ErrorDocument>
        <RoutingRules>
                <RoutingRule>
                    <Condition>
                        <KeyPrefixEquals>docs/</KeyPrefixEquals>
                    </Condition>
                    <Redirect>
                        <ReplaceKeyPrefixWith>documents/</ReplaceKeyPrefixWith>
                    </Redirect>
                </RoutingRule>
                <RoutingRule>
                    <Condition>
                        <KeyPrefixEquals>images/</KeyPrefixEquals>
                    </Condition>
                    <Redirect>
                        <ReplaceKeyPrefixWith>documents/</ReplaceKeyPrefixWith>
                    </Redirect>
                </RoutingRule>
        </RoutingRules>
    </websiteConfig>
</createBucketWebsiteConfiguration>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTwebsite.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTwebsite.html)

### Creating the bucket policy
The createBucketPolicy implementation of the PUT operation adds or replaces a policy on a bucket. If the bucket already has a policy, the one in this request completely replaces it. To perform this operation, you must be the bucket owner.

If you are not the bucket owner but have PutBucketPolicy permissions on the bucket, Amazon S3 returns a 405 Method Not Allowed. In all other cases, for a PUT bucket policy request that is not from the bucket owner, Amazon S3 returns 403 Access Denied. There are restrictions about who can create bucket policies and which objects in a bucket they can apply to.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp and  xAmzGrantFullControl.

**createBucketPolicy**
```
<amazons3.createBucketPolicy>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
  <bucketPolicy>{$ctx:bucketPolicy}</bucketPolicy>
</amazons3.createBucketPolicy>
```

**Properties**
* bucketUrl: The URL of the bucket.
* bucketPolicy: Policy of the bucket.

**Sample request**
```

{
    "accessKeyId": "AKXXXXXXXXX5EAS",
    "secretAccessKey": "qHXXXXXXNMDYadDdsQMnAOj+3XXXXPs",
    "region":"us-east-2",
    "methodType": "PUT",
    "contentType": "application/json",
    "bucketName": "signv4test",
    "isXAmzDate": "true",
    "bucketUrl": "http://s3.us-east-2.amazonaws.com/signv4test",
    "contentMD5":"",
    "xAmzSecurityToken":"",
    "host":"s3.us-east-2.amazonaws.com",
    "expect":"",
    "contentLength":"",
    "bucketPolicy": {
                    "Version":"2012-10-17",
                    "Statement":[{
                        "Sid":"AddPerm",
                            "Effect":"Allow",
                        "Principal": {
                                "AWS": "*"
                            },
                         "Action":["s3:GetObject"],
                        "Resource":["arn:aws:s3:::signv4test/*"]
                        }]
                }
}
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTpolicy.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTpolicy.html)

### Setting the permissions on an existing bucket

The createBucketACL operation uses the ACL sub-resource to set the permissions on an existing bucket using access control lists (ACL).

**createBucketACL**
```
<amazons3.createBucketACL>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
  <ownerId>{$ctx:ownerId}</ownerId>
  <ownerDisplayName>{$ctx:ownerDisplayName}</ownerDisplayName>
  <accessControlList>{$ctx:accessControlList}</accessControlList>
</amazons3.createBucketACL>
```

**Properties**
* bucketUrl:The URL of the bucket.
* ownerId: The ID of the bucket owner.
* ownerDisplayName: The screen name of the bucket owner.
* accessControlList: Container for ACL information, which includes the following:
    * Grant: Container for the grantee and permissions.
        * Grantee: The subject whose permissions are being set.
            * ID: ID of the grantee.
            * DisplayName: Screen name of the grantee.
        * Permission: Specifies the permission to give to the grantee.

**Sample request**
Following is a sample REST request that can be handled by the createBucketACL operation.

```
<createBucketACL>
    <accessKeyId>AKIXXXXXXXXXG5EA</accessKeyId>
    <secretAccessKey>qHZXXXXXXXDYQc4oMQXXXOj+340pXXX23s</secretAccessKey>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <addCharset>false</addCharset>
    <contentLength></contentLength>
    <contentMD5></contentMD5>
    <bucketName>signv4test</bucketName>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <host>s3.us-east-2.amazonaws.com</host>
    <region>us-east-2</region>
    <expect></expect>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <ownerId>9a48e6b16816cc75df306d35bb5d0bd0778b61fbf49b8ef4892143197c84a867</ownerId>
    <ownerDisplayName>admin+aws+connectors+secondary</ownerDisplayName>
    <accessControlList>
        <Grants>
            <Grant>
                <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="CanonicalUser">
                    <ID>9a48e6b16816cc75df306d35bb5d0bd0778b61fbf49b8ef4892143197c84a867</ID>
                    <DisplayName>admin+aws+connectors+secondary</DisplayName>
                </Grantee>
                <Permission>FULL_CONTROL</Permission>
            </Grant>
            <Grant>
            <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="Group">
                <URI xmlns="">http://acs.amazonaws.com/groups/global/AllUsers</URI>
            </Grantee>
                <Permission xmlns="">READ</Permission>
            </Grant>
        </Grants>
    </accessControlList>
</createBucketACL>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html)

### Setting the lifecycle for an existing bucket
The createBucketLifecycle  operation uses the acl subresource to set the permissions on an existing bucket using access control lists (ACL).

**createBucketLifecycle**
```
<amazons3.createBucketLifecycle>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
  <lifecycleConfiguration>{$ctx:lifecycleConfiguration}</lifecycleConfiguration>
</amazons3.createBucketLifecycle>
```

**Properties**
* bucketUrl:The URL of the bucket.
* lifecycleConfiguration: Container for lifecycle rules, which includes the following:
    * Rule: Container for a lifecycle rule.
        * ID: Unique identifier for the rule. The value cannot be longer than 255 characters.
        * Prefix: Object key prefix identifying one or more objects to which the rule applies.
        * Status: If Enabled, Amazon S3 executes the rule as scheduled. If Disabled, Amazon S3 ignores the rule.
        * Transition: This action specifies a period in the objects' lifetime when Amazon S3 should transition them to the STANDARD_IA or the GLACIER storage class.
            * Days: Specifies the number of days after object creation when the specific rule action takes effect.
            * StorageClass: Specifies the Amazon S3 storage class to which you want the object to transition.
        * Expiration: This action specifies a period in an object's lifetime when Amazon S3 should take the appropriate expiration action.
            * Days: Specifies the number of days after object creation when the specific rule action takes effect.

**Sample request**
Following is a sample REST request that can be handled by the createBucketLifecycle operation.
```
<createBucketLifecycle>
    <accessKeyId>AKXXXXXXXXXXX5EA</accessKeyId>
    <secretAccessKey>qHXXXXXXXXXXXqQc4oMQMnAOj+33XXXXXDPO2s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
    <lifecycleConfiguration>
        <Rule>
            <ID>id1</ID>
            <Prefix>documents/</Prefix>
            <Status>Enabled</Status>
            <Transition>
                <Days>30</Days>
                <StorageClass>GLACIER</StorageClass>
            </Transition>
        </Rule>
        <Rule>
            <ID>id2</ID>
            <Prefix>logs/</Prefix>
            <Status>Enabled</Status>
            <Expiration>
              <Days>365</Days>
            </Expiration>
        </Rule>
    </lifecycleConfiguration>
</createBucketLifecycle>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlifecycle.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlifecycle.html)

### Creating a new replication configuration
The createBucketReplication operation uses the acl subresource to set the permissions on an existing bucket using access control lists (ACL).

**createBucketReplication**
```
<amazons3.createBucketReplication>
  <role>{$ctx:role}</role>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
  <rules>{$ctx:rules}</rules>
</amazons3.createBucketReplication>
```

**Properties**
* bucketUrl:The URL of the bucket.
* rules: Container for replication rules, which includes the following:
    * Rule: Container for information about a particular replication rule.
        * ID: Unique identifier for the rule. The value cannot be longer than 255 characters.
        * Prefix: Object keyname prefix identifying one or more objects to which the rule applies.
        * Status: The rule is ignored if status is not Enabled.
        * Destination: Container for destination information.
            * Bucket:Amazon resource name (ARN) of the bucket where you want Amazon S3 to store replicas of the object identified by the rule.

**Sample request**
Following is a sample REST request that can be handled by the createBucketReplication operation.

Sample request for createBucketReplication
```
<createBucketReplication>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
    <role>arn:aws:iam::35667example:role/CrossRegionReplicationRoleForS3</role>
    <rules>
        <Rule>
            <ID>id1</ID>
            <Prefix>documents/</Prefix>
            <Status>Enabled</Status>
            <Destination>
                <Bucket>arn:aws:s3:::signv4testq23aa1</Bucket>
            </Destination>
        </Rule>
    </rules>
</createBucketReplication>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTreplication.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTreplication.html)

### Adding a set of tags to an existing bucket
The createBucketTagging operation uses the tagging subresource to add a set of tags to an existing bucket.

Use tags to organize your AWS bill to reflect your own cost structure. To do this, sign up to get your AWS account bill with tag key values included. Then, to see the cost of combined resources, organize your billing information according to resources with the same tag key values. For example, you can tag several resources with a specific application name, and then organize your billing information to see the total cost of that application across several services.

**createBucketTagging**
```
<amazons3.createBucketTagging>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
  <tagSet>{$ctx:tagSet}</tagSet>
</amazons3.createBucketTagging>
```

**Properties**
* bucketUrl:The URL of the bucket.
* tagSet: Container for a set of tags, which includes the following:
    * Tag: Container for tag information.
        * Key: Name of the tag.
        * Value: Value of the tag.

**Sample request**
Following is a sample REST request that can be handled by the createBucketTagging operation.

```
<createBucketTagging>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
    <tagSet>
        <Tag>
            <Key>Project</Key>
            <Value>Project One</Value>
        </Tag>
        <Tag>
            <Key>User</Key>
            <Value>jsmith</Value>
        </Tag>
    </tagSet>
</createBucketTagging>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTtagging.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTtagging.html)

### Setting the request payment configuration of a bucket.
The createBucketRequestPayment operation uses the requestPayment subresource to set the request payment configuration of a bucket. By default, the bucket owner pays for downloads from the bucket. This configuration parameter enables the bucket owner (only) to specify that the person requesting the download will be charged for the download.

**createBucketRequestPayment**
```
<amazons3.createBucketRequestPayment>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
  <payer>{$ctx:payer}</payer>
</amazons3.createBucketRequestPayment>
```

**Properties**
* bucketUrl:The URL of the bucket.
* payer: Specifies who pays for the download and request fees.

**Sample request**
Following is a sample REST request that can be handled by the createBucketRequestPayment operation.

```
<createBucketRequestPayment>
    <accessKeyId>AKXXXXXXXXXXX5EA</accessKeyId>
    <secretAccessKey>qHXXXXXXXXXXXqQc4oMQMnAOj+33XXXXXDPO2s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
    <payer>Requester</payer>
</createBucketRequestPayment>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentPUT.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentPUT.html)

### Setting the versioning state of an existing bucket.
The createBucketVersioning operation uses the requestPayment subresource to set the request payment configuration of a bucket. By default, the bucket owner pays for downloads from the bucket. This configuration parameter enables the bucket owner (only) to specify that the person requesting the download will be charged for the download.

**createBucketVersioning**
```
<amazons3.createBucketVersioning>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
  <status>{$ctx:status}</status>
  <mfaDelete>{$ctx:mfaDelete}</mfaDelete>
</amazons3.createBucketVersioning>
```

**Properties**
* bucketUrl:The URL of the bucket.
* status: Sets the versioning state of the bucket.
* mfaDelete: Specifies whether MFA Delete is enabled in the bucket versioning configuration.

**Sample request**
Following is a sample REST request that can be handled by the createBucketVersioning operation.

```
<createBucketVersioning>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
    <status>Enabled</status>
</createBucketVersioning>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTVersioningStatus.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTVersioningStatus.html)

### Deleting a bucket
The deleteBucket implementation of the DELETE operation deletes the bucket named in the URI. All objects (including all object versions and Delete Markers) in the bucket must be deleted before the bucket itself can be deleted.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**deleteBucket**
```
<amazons3.deleteBucket>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
<amazons3.deleteBucket>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the deleteBucket operation.

```
<deleteBucket>
    <accessKeyId>AKIAIGURZM7SDFGJ7TRO6KSFSQ</accessKeyId>
    <secretAccessKey>asAX8CJoDKzeOgfdgd0Ve5dMCFk4STUFDdfgdgRHkGX6m0CcY</secretAccessKey>
    <methodType>DELETE</methodType>
    <region>us-east-2</region>
    <contentLength></contentLength>
    <contentType>application/xml</contentType>
    <contentMD5></contentMD5>
    <expect></expect>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</deleteBucket>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETE.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETE.html)

### Deleting the bucket policy
The deleteBucketPolicy implementation of the DELETE operation deletes the policy on a specified bucket. To use the operation, you must have DeletePolicy permissions on the specified bucket and be the bucket owner.

If there are no DeletePolicy permissions, Amazon S3 returns a 403 Access Denied error. If there is the correct permission, but you are not the bucket owner, Amazon S3 returns a 405 Method Not Allowed error. If the bucket does not have a policy, Amazon S3 returns a 204 No Content error. There are restrictions about who can create bucket policies and which objects in a bucket they can apply to.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**deleteBucketPolicy**
```
<amazons3.deleteBucketPolicy>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.deleteBucketPolicy>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the deleteBucketPolicy operation.

```
<deleteBucketPolicy>
    <accessKeyId>AKIAQEIGURZSDFDM7GJ7TRO6KQ</accessKeyId>
    <secretAccessKey>asAX8CJoDvcvKzeOd0Ve5dMjkjCFk4STUFDRHkGX6m0CcY</secretAccessKey>
    <methodType>DELETE</methodType>
    <contentType>application/xml</contentType>
    <contentLength>256</contentLength>
    <contentMD5></contentMD5>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <expect></expect>
    <region>us-east-2</region>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</deleteBucketPolicy>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEpolicy.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEpolicy.html)

### Deleting the cors configuration of an existing bucket
The deleteBucketCors operation deletes the cors configuration information set for the bucket.

To use this operation, you must have permission to perform the s3:PutCORSConfiguration action. The bucket owner has this permission by default and can grant this permission to others.

**deleteBucketCors**
```
<amazons3.deleteBucketCors>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.deleteBucketCors>
```

**Properties**
* bucketUrl:The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the deleteBucketCors operation.

```
<deleteBucketCors>
    <accessKeyId>AKIAIGURZMSDFD7GJ7TRO6KQDFD</accessKeyId>
    <secretAccessKey>asAX8CJoDKzeOd0Ve5dfgdgdfMCFk4STUFDRHSFSDkGX6m0CcY</secretAccessKey>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <region>us-east-2</region>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
</deleteBucketCors>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEcors.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEcors.html)

### Deleting the lifecycle configuration from a bucket
The deleteBucketLifecycle operation deletes the lifecycle configuration from the specified bucket. Amazon S3 removes all the lifecycle configuration rules in the lifecycle subresource associated with the bucket. Your objects never expire, and Amazon S3 no longer automatically deletes any objects on the basis of rules contained in the deleted lifecycle configuration.

To use this operation, you must have permission to perform the s3:PutLifecycleConfiguration action. By default, the bucket owner has this permission and the bucket owner can grant this permission to others.

**deleteBucketLifecycle**
```
<amazons3.deleteBucketLifecycle>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.deleteBucketLifecycle>
```

**Properties**
* bucketUrl:The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the deleteBucketLifecycle operation.

```
<deleteBucketLifecycle>
    <accessKeyId>AKIAIGURZMSDFD7GJ7TRO6KQDFD</accessKeyId>
    <secretAccessKey>asAX8CJoDKzeOd0Ve5dfgdgdfMCFk4STUFDRHSFSDkGX6m0CcY</secretAccessKey>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <region>us-east-2</region>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
</deleteBucketLifecycle>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETElifecycle.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETElifecycle.html)

### Deleting the replication subresource associated with a bucket
The deleteBucketReplication operation deletes the replication sub-resource associated with the specified bucket.

This operation requires permission for the s3:DeleteReplicationConfiguration action.

**deleteBucketReplication**
```
<amazons3.deleteBucketReplication>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.deleteBucketReplication>
```

**Properties**
* bucketUrl:The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the deleteBucketReplication operation.

```
<deleteBucketReplication>
    <accessKeyId>AKIAIGURZMSDFD7GJ7TRO6KQDFD</accessKeyId>
    <secretAccessKey>asAX8CJoDKzeOd0Ve5dfgdgdfMCFk4STUFDRHSFSDkGX6m0CcY</secretAccessKey>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <region>us-east-2</region>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
</deleteBucketReplication>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEreplication.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEreplication.html)

### Deleting the tag set from a bucket
The deleteBucketTagging operation uses the tagging sub-resource to remove a tag set from the specified bucket.

To use this operation, you must have permission to perform the s3:PutBucketTagging action. By default, the bucket owner has this permission and can grant this permission to others.

**deleteBucketTagging**
```
<amazons3.deleteBucketTagging>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.deleteBucketTagging>
```

**Properties**
* bucketUrl:The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the deleteBucketTagging operation.

```
<deleteBucketTagging>
    <accessKeyId>AKIAIGURZMSDFD7GJ7TRO6KQDFD</accessKeyId>
    <secretAccessKey>asAX8CJoDKzeOd0Ve5dfgdgdfMCFk4STUFDRHSFSDkGX6m0CcY</secretAccessKey>
    <methodType>PUT</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <region>us-east-2</region>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
</deleteBucketTagging>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEtagging.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEtagging.html)

### Deleting the bucket website
The deleteBucketWebsiteConfiguration operation removes the website configuration for a bucket. Amazon S3 returns a 207 OK response upon successfully deleting a website configuration on the specified bucket. It will give a 200 response if the website configuration you are trying to delete does not exist on the bucket, and a 404 response if the bucket itself does not exist.

This DELETE operation requires the S3: DeleteBucketWebsite permission. By default, only the bucket owner can delete the website configuration attached to a bucket. However, bucket owners can grant other users permission to delete the website configuration by writing a bucket policy granting them the S3: DeleteBucketWebsite permission.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**deleteBucketWebsiteConfiguration**
```
<amazons3.deleteBucketWebsiteConfiguration>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.deleteBucketWebsiteConfiguration>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the deleteBucketWebsiteConfiguration operation.

```
<deleteBucketWebsiteConfiguration>
    <accessKeyId>AKIAIGURZM7GDFDJ7TRO6KQDFD</accessKeyId>
    <secretAccessKey>asAdfsX8CJoDKzeOd0Ve5dMCdfsdFk4STUFDRHkdsfGX6m0CcY</secretAccessKey>
    <methodType>DELETE</methodType>
    <contentType>application/xml</contentType>
    <contentLength></contentLength>
    <region>us-east-2</region>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <contentMD5></contentMD5>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <expect></expect>
</deleteBucketWebsiteConfiguration>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEwebsite.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEwebsite.html)

### Getting objects in a bucket
The getObjectsInBucket implementation of the GET operation returns some or all (up to 1000) of the objects in a bucket. The request parameters act as selection criteria to return a subset of the objects in a bucket. To use this implementation of the operation, the user must have READ access to the bucket.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getObjectsInBucket**
```
<amazons3.getObjectsInBucket>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <delimiter>{$ctx:delimiter}</delimiter>
    <encodingType>{$ctx:encodingType}</encodingType>
    <marker>{$ctx:marker}</marker>
    <maxKeys>{$ctx:maxKeys}</maxKeys>
    <prefix>{$ctx:prefix}</prefix>
</amazons3.getObjectsInBucket>
```

**Properties**
* bucketUrl: The URL of the bucket.
* delimiter: Optional - A delimiter is a character used to group keys. All keys that contain the same string between the prefix, if specified, and the first occurrence of the delimiter after the prefixes are grouped under a single result element CommonPrefixes. If the prefix parameter is not specified, the substring starts at the beginning of the key. The keys that are grouped under the CommonPrefixesresult element are not returned elsewhere in the response.
* encodingType: Optional - Requests Amazon S3 to encode the response and specifies the encoding method to use. An object key can contain any Unicode character. However, XML 1.0 parser cannot parse some characters such as characters with an ASCII value from 0 to 10. For characters that are not supported in XML 1.0, this parameter can be added to request Amazon S3 to encode the keys in the response.
* marker: Optional - Specifies the key to start with when listing objects in a bucket. Amazon S3 lists objects in alphabetical order.
* maxKeys: Optional - Sets the maximum number of keys returned in the response body. The response might contain fewer keys but will never contain more.
* prefix:  Optional - Limits the response to keys that begin with the specified prefix.

**Sample request**
Following is a sample REST request that can be handled by the getObjectsInBucket operation.

```
<getObjectsInBucket>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
     <prefix>t</prefix>
     <marker>obj</marker>
     <expect/>
     <maxKeys>3</maxKeys>
     <encodingType>url</encodingType>
     <delimiter>images</delimiter>
</getObjectsInBucket>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)

### Getting the bucket lifecycle
The getBucketLifeCycle operation returns the lifecycle configuration information set on the bucket.

To use this operation, permissions should be given to perform the s3:GetLifecycleConfiguration action. The bucket owner has this permission by default and can grant this permission to others. There is usually some time lag before lifecycle configuration deletion is fully propagated to all the Amazon S3 systems.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketLifeCycle**
```
<amazons3.getBucketLifeCycle>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketLifeCycle>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketLifeCycle operation.

```
<getBucketLifeCycle>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketLifeCycle>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlifecycle.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlifecycle.html)

### Setting the cors configuration for the bucket
The createBucketCors operation returns the  cors  configuration information set for the bucket.

To use this operation, you must have permission to perform the s3:CreateBucketCORS action. By default, the bucket owner has this permission and can grant it to others.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**createBucketCors**
```
<amazons3.createBucketCors>
     <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
     <corsConfiguration>{$ctx:corsConfiguration}</corsConfiguration>
</amazons3.createBucketCors>
```

**Properties**
* bucketUrl: The URL of the bucket.
* corsConfiguration: Container for up to 100 CORSRules elements.

**Sample request**
Following is a sample REST request that can be handled by the createBucketCors operation.

```
<createBucketCors>
    <accessKeyId>AKXXXXXXXXXXX5EA</accessKeyId>
    <secretAccessKey>qHXXXXXXXXXXXqQc4oMQMnAOj+33XXXXXDPO2s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>PUT</methodType>
    <contentLength>256</contentLength>
    <contentType>application/xml</contentType>
    <expect></expect>
    <host>s3.us-east-2.amazonaws.com</host>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <bucketName>signv4test</bucketName>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <corsConfiguration>
        <CORSRule>
           <AllowedOrigin>*</AllowedOrigin>
           <AllowedMethod>GET</AllowedMethod>
           <AllowedHeader>*</AllowedHeader>
           <MaxAgeSeconds>3000</MaxAgeSeconds>
        </CORSRule>
    </corsConfiguration>
</createBucketCors>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTcors.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTcors.html)

### Getting the bucket cors
The getBucketCors operation returns the  cors  configuration information set for the bucket.

To use this operation, you must have permission to perform the s3:GetBucketCORS action. By default, the bucket owner has this permission and can grant it to others.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketCors**
```
<amazons3.getBucketCors>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketCors>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketCors operation.

```
<getBucketCors>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>GET</methodType>
    <contentType>application/xml</contentType>
    <contentLength>256</contentLength>
    <contentMD5></contentMD5>
    <bucketName>signv4test</bucketName>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <host>s3.us-east-2.amazonaws.com</host>
    <expect></expect>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketCors>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETcors.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETcors.html)

### Getting the bucket location
The getBucketLocation operation returns the lifecycle configuration information set on the bucket.

To use this operation, you must be the bucket owner.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketLocation**
```
<amazons3.getBucketLocation>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketLocation>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketLifeCycle operation.

```
<getBucketLocation>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketLocation>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlocation.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlocation.html)

### Getting the bucket logging
The getBucketLogging operation returns the logging status of a bucket and the permissions users have to view and modify that status.

To use this operation, you must be the bucket owner.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketLogging**
```
<amazons3.getBucketLogging>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketLogging>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketLogging operation.

```
<getBucketLogging>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketLogging>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlogging.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlogging.html)

### Getting the bucket notification
The getBucketNotification operation returns the lifecycle configuration information set on the bucket.

To use this operation, you must be the bucket owner to read the notification configuration of a bucket. However, the bucket owner can use a bucket policy to grant permission to other users to read this configuration with the  s3:GetBucketNotification  permission.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketNotification**
```
<amazons3.getBucketNotification>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketNotification>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketNotification operation.

```
<getBucketNotification>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketNotification>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETnotification.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETnotification.html)

### Getting the bucket tagging
The getBucketTagging operation returns the lifecycle configuration information set on the bucket.

To use this operation, you must have permission to perform the s3:GetBucketTagging action. By default, the bucket owner has this permission and can grant this permission to others.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketTagging**
```
<amazons3.getBucketTagging>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketTagging>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketTagging operation.

```
<getBucketTagging>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketTagging>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETtagging.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETtagging.html)

### Getting the bucket replication
The getBucketReplication operation returns the lifecycle configuration information set on the bucket.

To use this operation, you must have permission to perform the s3:GetReplicationConfiguration action. For more information about permissions, go to  Using Bucket Policies and User Policies  in the  Amazon Simple Storage Service Developer Guide .

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketReplication**
```
<amazons3.getBucketReplication>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketReplication>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketReplication operation.

```
<getBucketReplication>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketReplication>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETreplication.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETreplication.html)

### Getting the bucket policy
The getBucketPolicy implementation of the GET operation returns the policy of a specified bucket. To use this operation, the user must have GetPolicy permissions on the specified bucket, and the user must be the bucket owner.

If the user does not have GetPolicy permissions, Amazon S3 returns a 403 Access Denied error. If the user has correct permissions, but the user is not the bucket owner, Amazon S3 returns a 405 Method Not Allowed error. If the bucket does not have a policy, Amazon S3 returns a 404 Policy Not found error. There are restrictions about who can create bucket policies and which objects in a bucket they can apply to.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketPolicy**
```
<amazons3.getBucketPolicy>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketPolicy>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketPolicy operation. The body is a JSON string containing the policy contents with the policy statements.

```
<getBucketPolicy>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketPolicy>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETpolicy.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETpolicy.html)

### Getting bucket object versions
The getBucketObjectVersions operation lists metadata about all of the versions of objects in a bucket. Request parameters can be used as selection criteria to return metadata about a subset of all the object versions. To use this operation, the user must have READ access to the bucket.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketObjectVersions**
```
<amazons3.getBucketObjectVersions>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <delimiter>{$ctx:delimiter}</delimiter>
    <encodingType>{$ctx:encodingType}</encodingType>
    <keyMarker>{$ctx:keyMarker}</keyMarker>
    <maxKeys>{$ctx:maxKeys}</maxKeys>
    <prefix>{$ctx:prefix}</prefix>
    <versionIdMarker>{$ctx:versionIdMarker}</versionIdMarker>
</amazons3.getBucketObjectVersions>
```

**Properties**
* bucketUrl: The URL of the bucket.
* delimiter: Optional - A character that is used to group keys.
* encodingType: Optional - Requests Amazon S3 to encode the response and specifies the encoding method to use.
* keyMarker: Optional - Specifies the key in the bucket that you want to start listing from. See also versionIdMarker below.
* maxKeys: Optional - Sets the maximum number of keys returned in the response body.
* prefix: Optional - Selects only those keys that begin with the specified prefix.
* versionIdMarker: Optional - Specifies the object version you want to start listing from.

**Sample request**
Following is a sample REST request that can be handled by the getBucketObjectVersions operation.

```
<getBucketObjectVersions>
	<accessKeyId>AKXXXXXS3KJA</accessKeyId>
    <secretAccessKey>ieXXHXXTVh/12hL2VxxJJS</secretAccessKey>
    <methodType>GET</methodType>
    <contentType>application/xml</contentType>
	<contentLength>256</contentLength>
	<contentMD5></contentMD5>
    <bucketName>testkeerthu1234</bucketName>
    <isXAmzDate>true</isXAmzDate>
	<xAmzSecurityToken></xAmzSecurityToken>
	<host></host>
	<expect></expect>
    <bucketUrl>http://s3.amazonaws.com/testkeerthu1234</bucketUrl>
    <delimiter>/</delimiter>
    <encodingType></encodingType>
    <keyMarker></keyMarker>
    <maxKeys>3</maxKeys>
    <prefix>images</prefix>
    <versionIdMarker></versionIdMarker>
</getBucketObjectVersions>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETVersion.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETVersion.html)

### Getting the request payment configuration of a bucket
The getBucketRequestPayment implementation of the GET operation returns the request payment configuration of a bucket. To use this operation, the user must be the bucket owner.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketRequestPayment**
```
<amazons3.getBucketRequestPayment>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketRequestPayment>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketRequestPayment operation.

```
<getBucketRequestPayment>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketRequestPayment>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentGET.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentGET.html)

### Getting bucket versioning
The getBucketVersioning implementation of the GET operation returns the versioning state of a bucket. To retrieve the versioning state of a bucket, the user must be the bucket owner.

This implementation also returns the MFA Delete status of the versioning state. If the MFA Delete status is enabled, the bucket owner must use an authentication device to change the versioning state of the bucket.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketVersioning**
```
<amazons3.getBucketVersioning>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketVersioning>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketVersioning operation.

```
<getBucketVersioning>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketVersioning>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETversioningStatus.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETversioningStatus.html)

### Getting the bucket website
The getWebSiteConfiguration implementation of the GET operation returns the website configuration associated with a bucket. To host the website on Amazon S3, a bucket can be configured as a website by adding a website configuration.

This GET operation requires the S3:GetBucketWebsite permission. By default, only the bucket owner can read the bucket website configuration. However, bucket owners can allow other users to read the website configuration by writing a bucket policy granting them the S3:GetBucketWebsite permission.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getWebSiteConfiguration**
```
<amazons3.getWebSiteConfiguration>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getWebSiteConfiguration>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getWebsiteConfiguration operation.

```
<getWebSiteConfiguration>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
     <methodType>GET</methodType>
     <contentType>application/xml</contentType>
     <contentLength></contentLength>
     <contentMD5></contentMD5>
     <bucketName>signv4test</bucketName>
     <isXAmzDate>true</isXAmzDate>
     <xAmzSecurityToken></xAmzSecurityToken>
     <host>s3.us-east-2.amazonaws.com</host>
     <expect></expect>
     <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getWebSiteConfiguration>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETwebsite.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETwebsite.html)

### Getting the bucket access control list (ACL)
The getBucketACL implementation of the GET operation returns the access control list (ACL) of a bucket. To use GET to return the ACL of the bucket, the user must have READ_ACP access to the bucket. If READ_ACP permission is granted to the anonymous user, you can return the ACL of the bucket without using an authorization header.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**getBucketACL**
```
<amazons3.getBucketACL>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.getBucketACL>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the getBucketACL operation.

```
<getBucketACL>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>GET</methodType>
    <contentType>application/xml</contentType>
    <contentLength>256</contentLength>
    <contentMD5></contentMD5>
    <bucketName>signv4test</bucketName>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <host>s3.us-east-2.amazonaws.com</host>
    <expect></expect>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</getBucketACL>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETacl.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETacl.html)

### Checking bucket permissions
The checkBucketPermission operation determines whether a bucket exists and you have permission to access it. The operation returns a 200 OK if the bucket exists and you have permission to access it. Otherwise, the operation might return responses such as 404 Not Found and 403 Forbidden.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**checkBucketPermission**
```
<amazons3.checkBucketPermission>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.checkBucketPermission>
```

**Properties**
* bucketUrl: The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the checkBucketPermission operation.

```
<checkBucketPermission>
    <accessKeyId>AKXXXXXXXXXXX5EA</accessKeyId>
    <secretAccessKey>qHXXXXXXXXXXXqQc4oMQMnAOj+33XXXXXDPO2s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>HEAD</methodType>
    <contentType>application/xml</contentType>
    <contentLength></contentLength>
    <contentMD5></contentMD5>
    <expect></expect>
    <host>s3.us-east-2.amazonaws.com</host>
    <xAmzSecurityToken></xAmzSecurityToken>
    <bucketName>signv4test</bucketName>
    <isXAmzDate>true</isXAmzDate>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
</checkBucketPermission>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/user-guide/bucket-permissions-check.html](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/bucket-permissions-check.html)

### Setting a bucket ACL
The setBucketACL implementation of the PUT operation sets the permissions on an existing bucket using access control lists (ACL). You set the permissions by specifying the ACL in the request body.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**setBucketACL**
```
<amazons3.setBucketACL>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <accessControlPolicy>{$ctx:accessControlPolicy}</accessControlPolicy>
</amazons3.setBucketACL>
```

**Properties**
* bucketUrl: The URL of the bucket.
* accessControlPolicy: Contains the following elements that set the ACL permissions for an object per grantee:
    * Owner: Container for the bucket owner's ID and display name.
        * ID: ID of the bucket owner, or the ID of the grantee.
        * DisplayName: Screen name of the bucket owner.
    * AccessControlList: Container for the grants.
        * Grant: Container for the grantee and the permissions of this grant.
            * Grantee: The subject whose permissions are being set. For more information, see Grantee Values.
                * URI: Granting permission to a predefined Amazon S3 group.
            * Permission: Specifies the permission given to the grantee.

**Sample request**
Following is a sample REST request that can be handled by the setBucketACL operation.

```
<setBucketACL>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>PUT</methodType>
    <contentLength>2000</contentLength>
    <contentType>application/xml</contentType>
    <contentMD5></contentMD5>
    <expect></expect>
    <host>s3.us-east-2.amazonaws.com</host>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <bucketName>signv4test</bucketName>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <accessControlPolicy>
        <Owner>
            <ID>9a48e6b16816cc75df306d35bb5d0bd0778b61fbf49b8ef4892143197c84a867</ID>
            <DisplayName>admin+aws+connectors+secondary</DisplayName>
        </Owner>
        <AccessControlList>
                <Grant>
                        <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="CanonicalUser">
                                <ID>9a48e6b16816cc75df306d35bb5d0bd0778b61fbf49b8ef4892143197c84a867</ID>
                            <DisplayName>admin+aws+connectors+secondary</DisplayName>
                        </Grantee>
                    <Permission>FULL_CONTROL</Permission>
                </Grant>
            <Grant>
                    <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="Group">
                            <URI xmlns="">http://acs.amazonaws.com/groups/global/AllUsers</URI>
                        </Grantee>
                    <Permission xmlns="">READ</Permission>
                </Grant>
            <Grant>
                    <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="Group">
                            <URI xmlns="">http://acs.amazonaws.com/groups/s3/LogDelivery</URI>
                        </Grantee>
                    <Permission xmlns="">WRITE</Permission>
                </Grant>
        </AccessControlList>
    </accessControlPolicy>
</setBucketACL>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html)

### Determining the existence of a bucket and permission to access it.
The headBucket operation is useful to determine if a bucket exists and you have permission to access it. The operation returns a 200 OK if the bucket exists and you have permission to access it. Otherwise, the operation might return responses such as 404 Not Found and 403 Forbidden.

**headBucket**
```
<amazons3.headBucket>
  <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
</amazons3.headBucket>
```

**Properties**
bucketUrl:The URL of the bucket.

**Sample request**
Following is a sample REST request that can be handled by the headBucket operation.

```
<headBucket>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-west-2</region>
    <methodType>HEAD</methodType>
    <contentType>application/xml</contentType>
    <addCharset>false</addCharset>
    <contentLength></contentLength>
    <contentMD5></contentMD5>
    <bucketName>1513162931643testconbkt2</bucketName>
    <isXAmzDate>true</isXAmzDate>
    <xAmzSecurityToken></xAmzSecurityToken>
    <host>s3-us-west-2.amazonaws.com</host>
    <expect></expect>
    <bucketUrl>http://s3-us-west-2.amazonaws.com/1513162931643testconbkt2</bucketUrl>
</headBucket>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketHEAD.html](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketHEAD.html)

### Listing multipart uploads
The listMultipartUploads operation lists in-progress multipart uploads. A multipart upload is in progress when it has been initiated using the Initiate Multipart Upload request but has not yet been completed or aborted. It returns a default value of 1000 multipart uploads in the response. The number of uploads can be further limited in a response by specifying the maxUploads property. If additional multipart uploads satisfy the list criteria, the response will contain an "IsTruncated" element with the value "true". To list the additional multipart uploads, use the keyMarker and uploadIdMarker request parameters.

In the response, the uploads are sorted by key. If the application has initiated more than one multipart upload using the same object key, uploads in the response are first sorted by key. Additionally, uploads are sorted in ascending order within each key by the upload initiation time.

When calling init before this operation, the following headers should be removed: xAmzAcl, x AmzGrantRead,  xAmzGrantWrite,  xAmzGrantReadAcp,  xAmzGrantWriteAcp, and xAmzGrantFullControl.

**listMultipartUploads**
```
<amazons3.listMultipartUploads>
    <bucketUrl>{$ctx:bucketUrl}</bucketUrl>
    <delimiter>{$ctx:delimiter}</delimiter>
    <encodingType>{$ctx:encodingType}</encodingType>
    <maxUploads>{$ctx:maxUploads}</maxUploads>
    <keyMarker>{$ctx:keyMarker}</keyMarker>
    <prefix>{$ctx:prefix}</prefix>
    <uploadIdMarker>{$ctx:uploadIdMarker}</uploadIdMarker>
</amazons3.listMultipartUploads>
```

**Properties**
* bucketUrl: The URL of the bucket.
* delimiter: A delimiter is a character you use to group keys. All keys that contain the same string between the prefix, if specified, and the first occurrence of the delimiter after the prefix are grouped under a single result element CommonPrefixes. If you do not specify the prefix parameter, the substring starts at the beginning of the key. The keys that are grouped under the CommonPrefixesresult element are not returned elsewhere in the response.
* encodingType: Requests Amazon S3 to encode the response and specifies the encoding method to use. An object key can contain any Unicode character. However, XML 1.0 parser cannot parse some characters such as characters with an ASCII value from 0 to 10. For characters that are not supported in XML 1.0, you can add this parameter to request Amazon S3 to encode the keys in the response.
* maxUploads: Sets the maximum number of multipart uploads, from 1 to 1,000, to return in the response body. 1,000 is the maximum number of uploads that can be returned in a response.
* keyMarker: Specifies the key to start with when listing objects in a bucket. Amazon S3 lists objects in alphabetical order.
* prefix: Limits the response to keys that begin with the specified prefix.
* uploadIdMarker: Together with keyMarker, specifies the multipart upload after which listing should begin. If keyMarker is not specified, the uploadIdMarker parameter is ignored. Otherwise, any multipart uploads for a key equal to the keyMarker might be included in the list only if they have an upload ID lexicographically greater than the specified uploadIdMarker.

**Sample request**
Following is a sample REST request that can be handled by the listMultipartUploads operation.

```
<listMultipartUploads>
    <accessKeyId>AKIXXXXXHXQXXG5XX</accessKeyId>
    <secretAccessKey>qHXXBXXXXASYQc4oMCEOj+343HD82s</secretAccessKey>
    <region>us-east-2</region>
    <methodType>GET</methodType>
    <contentType>application/xml</contentType>
    <isXAmzDate>true</isXAmzDate>
    <bucketUrl>http://s3.us-east-2.amazonaws.com/signv4test</bucketUrl>
    <contentLength>0</contentLength>
    <contentMD5></contentMD5>
    <bucketName>signv4test</bucketName>
    <host>s3.us-east-2.amazonaws.com</host>
    <expect></expect>
    <xAmzSecurityToken></xAmzSecurityToken>
    <xAmzAcl>public-read</xAmzAcl>
    <xAmzGrantRead></xAmzGrantRead>
    <xAmzGrantWrite></xAmzGrantWrite>
    <xAmzGrantReadAcp></xAmzGrantReadAcp>
    <xAmzGrantWriteAcp></xAmzGrantWriteAcp>
    <xAmzGrantFullControl></xAmzGrantFullControl>
</listMultipartUploads>
```

**Related Amazon S3 documentation**
[http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListMPUpload.html](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListMPUpload.html)

## Sample configuration
Following is a sample proxy service that illustrates how to connect to Amazon S3 with the init operation and use the getBuckets operation. The sample request for this proxy can be found in getBuckets sample request. You can use this sample as a template for using other operations in this category.

**Sample Proxy**
```xml
<proxy xmlns="http://ws.apache.org/ns/synapse"
       name="amazons3_getBuckets"
       transports="https,http"
       statistics="disable"
       trace="disable"
       startOnLoad="true">
   <target>
      <inSequence onError="faultHandlerSeq">
         <property name="accessKeyId" expression="//accessKeyId/text()"/>
         <property name="secretAccessKey" expression="//secretAccessKey/text()"/>
         <property name="methodType" expression="//methodType/text()"/>
         <property name="contentLength" expression="//contentLength/text()"/>
         <property name="contentType" expression="//contentType/text()"/>
         <property name="contentMD5" expression="//contentMD5/text()"/>
         <property name="expect" expression="//expect/text()"/>
         <property name="host" expression="//host/text()"/>
         <property name="region" expression="//region/text()"/>
         <property name="isXAmzDate" expression="//isXAmzDate/text()"/>
         <property name="xAmzSecurityToken" expression="//xAmzSecurityToken/text()"/>
         <property name="apiUrl" expression="//apiUrl/text()"/>
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
         </amazons3.init>
         <amazons3.getBuckets>
            <apiUrl>{$ctx:apiUrl}</apiUrl>
         </amazons3.getBuckets>
         <respond/>
      </inSequence>
      <outSequence>
        <send/>
      </outSequence>
   </target>
   <description/>
</proxy>
```