### AmazonS3 ESB Connector

The AmazonS3 [Connector](https://docs.wso2.com/display/EI650/Working+with+Connectors) allows you to access the REST API of [Amazon Simple Storage Service (Amazon S3)](http://docs.aws.amazon.com/AmazonS3/latest/API/Welcome.html), which provides a simple web services interface that can be used to store and retrieve any amount of data, at any time, from anywhere on the web. It gives any developer access to the same highly scalable, reliable, secure, fast, inexpensive infrastructure that Amazon S3 uses to run its own global network of websites. The service aims to maximize benefits of scale and to pass on those benefits to developers.

## Compatibility

| Connector version | Supported Amazon S3 API version | Supported WSO2 ESB/EI version |
| ------------- | ---------------                     |------------- |
|  [1.0.9](https://github.com/wso2-extensions/esb-connector-amazons3/tree/org.wso2.carbon.connector.amazons3-1.0.9)        |   2006-03-01                        | EI 6.5.0 |
|  [1.0.8](https://github.com/wso2-extensions/esb-connector-amazons3/tree/org.wso2.carbon.connector.amazons3-1.0.8)        |   2006-03-01                        | EI 6.4.0, EI 6.1.1, ESB 5.0.0 |
|  [1.0.7](https://github.com/wso2-extensions/esb-connector-amazons3/tree/org.wso2.carbon.connector.amazons3-1.0.7)        |   2006-03-01                        | EI 6.1.1, ESB 5.0.0 |
|  [1.0.6](https://github.com/wso2-extensions/esb-connector-amazons3/tree/org.wso2.carbon.connector.amazons3-1.0.6)        |   2006-03-01                        | EI 6.1.1, ESB 5.0.0, ESB 4.9.0 |
|  [1.0.5](https://github.com/wso2-extensions/esb-connector-amazons3/tree/org.wso2.carbon.connector.amazons3-1.0.5)        |   2006-03-01                        | EI 6.4.0, EI 6.1.1, ESB 5.0.0 |
|  [1.0.4](https://github.com/wso2-extensions/esb-connector-amazons3/tree/org.wso2.carbon.connector.amazons3-1.0.4)        |   2006-03-01                        | ESB 4.9.0, ESB 5.0.0 |

#### Download and install the connector

1. Download the connector from the [WSO2 Store](https://store.wso2.com/store/assets/esbconnector/details/3fcaf309-1a69-4edf-870a-882bb76fdaa1) by clicking the **Download Connector** button.
2. You can then follow this [documentation](https://docs.wso2.com/display/EI650/Working+with+Connectors+via+the+Management+Console) to add the connector to your WSO2 EI instance and to enable it (via the management console).
3. For more information on using connectors and their operations in your WSO2 EI configurations, see [Using a Connector](https://docs.wso2.com/display/EI650/Using+a+Connector).
4. If you want to work with connectors via WSO2 EI Tooling, see [Working with Connectors via Tooling](https://docs.wso2.com/display/EI650/Working+with+Connectors+via+Tooling).

#### Configuring the connector operations
To get started with the **Amazon S3** connector and their operations, see [Configuring Amazon S3 Operations](docs/config.md).

## Building from the source

Follow the steps given below to build the BigQuery connector from the source code.

1. Get a clone or download the source from [Github](https://github.com/wso2-extensions/esb-connector-amazons3).
2. Run the following Maven command from the `esb-connector-amazons3` directory: `mvn clean install`.
3. The ZIP file with the AmazonS3 connector is created in the `esb-connector-amazons3/target` directory.

## How you can contribute

As an open source project, WSO2 extensions welcome contributions from the community.
Check the [issue tracker](https://github.com/wso2-extensions/esb-connector-amazons3/issues) for open issues that interest you. We look forward to receiving your contributions.
