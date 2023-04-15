# webapp

## To be updated

## AWS CLI command to import SSL Certificate to AWS Certificate Manager

```bash
aws acm import-certificate --certificate fileb://Certificate.pem --certificate-chain fileb://CertificateChain.pem --private-key fileb://PrivateKey.pem
```

## Requirements

For building and running the application you need:

- JDK 17
- Maven 3

## Steps to Run


**Build and run the app using maven**

```bash
mvn package
java java -jar target/webapp-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

**Test app using maven**

```bash
mvn test
```


## Explore Rest APIs

The app defines following CRUD APIs.


    GET /healthz
    
    POST /v1/user
    
    GET /v1/user/{userId}
    
    PUT v1/user/{userId}

