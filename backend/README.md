# File Sync Backend

This project provides backend APIs for transferring files using a custom TCP-over-UDP protocol and S3 for file storage. It includes endpoints for sending, receiving, uploading, and downloading files.

## Related Repository

This project relies on the tcp-over-udp implementation for file transfer. You can find the full implementation and details of the TCP-over-UDP protocol in the [tcp-over-udp repository](https://github.com/eomielan/tcp-over-udp).

## Base URL

All endpoints are prefixed with `/file-transfer`.

Example:

```plaintext
http://localhost:8080/file-transfer
```

## Environment Configuration

To use Amazon S3 for file storage, set up a `.env` file in the root of your project directory with the following environment variables:

```plaintext
# .env
AWS_ACCESS_KEY_ID=your-access-key-id
AWS_SECRET_ACCESS_KEY=your-secret-access-key
AWS_REGION=your-aws-region
```

These environment variables are necessary for the application to authenticate with AWS and interact with your S3 bucket.

> **Note:** Ensure that the `.env` file is included in `.gitignore` to keep your credentials secure.

## Endpoints

### 1. Upload File to S3

Uploads a file to an Amazon S3 bucket for storage.

- **URL**: `/file-transfer/upload`
- **Method**: `POST`
- **Description**: Uploads a file to a specified S3 bucket.

#### Request Parameters

- **file**: (multipart/form-data) The file to be uploaded.
- **bucketName**: (string) The name of the S3 bucket where the file will be stored.
- **fileName**: (string) The name to save the file as in S3.

#### Example `curl` Request

```bash
curl -X POST -F "file=@/path/to/your/file.txt" \
     -F "bucketName=your-s3-bucket-name" \
     -F "fileName=uploaded-file.txt" \
     http://localhost:8080/file-transfer/upload
```

#### Response

- **Success**: Returns a message with the S3 ETag confirming the file was uploaded successfully.
- **Error**: Returns a `500 Internal Server Error` if the upload fails.

---

### 2. Download File from S3

Downloads a file from S3 and serves it as a downloadable resource.

- **URL**: `/file-transfer/download`
- **Method**: `GET`
- **Description**: Fetches a file from S3 based on the provided bucket and file name.

#### Request Parameters

- **bucketName**: (query parameter) The name of the S3 bucket where the file is stored.
- **fileName**: (query parameter) The name of the file in S3.

#### Example `curl` Request

```bash
curl -X GET "http://localhost:8080/file-transfer/download?bucketName=your-s3-bucket-name&fileName=your-file-name" -o downloaded-file.txt
```

#### Response

- **Success**: Returns the file as a downloadable resource.
- **Error**: Returns a `404 Not Found` if the file does not exist or a `500 Internal Server Error` if the download fails.

---

### 3. Send File via TCP-over-UDP

Uploads and sends a file to a specified receiver over a custom TCP-over-UDP protocol.

- **URL**: `/file-transfer/send`
- **Method**: `POST`
- **Description**: Uploads a file and sends it to a designated receiver.

#### Request Parameters

- **file**: (multipart/form-data) The file to be sent.
- **receiverHostname**: (string) The hostname or IP address of the receiver.
- **receiverPort**: (integer) The port on which the receiver is listening.
- **bytesToTransfer**: (integer) The number of bytes to transfer.

#### Example `curl` Request

```bash
curl -X POST -F "file=@/path/to/your/file.txt" \
     -F "receiverHostname=localhost" \
     -F "receiverPort=12345" \
     -F "bytesToTransfer=1024" \
     http://localhost:8080/file-transfer/send
```

#### Response

- **Success**: Returns a message confirming that the file was sent successfully.
- **Error**: Returns a `500 Internal Server Error` with details if the transfer fails.

---

### 4. Receive File via TCP-over-UDP

Initiates the receiver process to download a file over TCP-over-UDP, then serves it as a downloadable resource.

- **URL**: `/file-transfer/receive`
- **Method**: `GET`
- **Description**: Starts the receiver to download the file over the TCP-over-UDP protocol.

#### Request Parameters

- **filename**: (query parameter) The name of the file to save the received data as.
- **port**: (query parameter) The port number for the `receiver` to listen on.

#### Request Headers

- **fileStorageLocation**: (string, required) The directory path where the received file should be stored temporarily.

#### Example `curl` Request

```bash
curl -X GET "http://localhost:8080/file-transfer/receive?filename=received_file.txt&port=12345" \
     -H "fileStorageLocation:/custom/downloads"
```

#### Response

- **Success**: Serves the file as a downloadable resource.
- **Error**: Returns a `404 Not Found` if the file cannot be found or `500 Internal Server Error` if the reception fails.

---

## How to Run the Backend Locally

To run the backend locally, follow these steps:

### Prerequisites

- **Java 17** (or compatible version for your Spring Boot setup)
- **Gradle** (or use the included Gradle Wrapper, `./gradlew`)
- **GCC** (for compiling the TCP-over-UDP binaries)

### Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/eomielan/file-sync.git
   cd file-sync/backend
   ```

2. **Compile the TCP-over-UDP Binaries**

   ```bash
   cd tcpOverUdp
   make
   ```

3. **Configure the .env File**

   Create a `.env` file in the project root with your AWS credentials and region:

   ```plaintext
   AWS_ACCESS_KEY_ID=your-access-key-id
   AWS_SECRET_ACCESS_KEY=your-secret-access-key
   AWS_REGION=your-aws-region
   ```

4. **Run the Backend**

   ```bash
   cd ..
   ./gradlew bootRun
   ```

5. **Test the Endpoints**

   You can now test the `/file-transfer/send`, `/file-transfer/receive`, `/file-transfer/upload`, and `/file-transfer/download` endpoints using `curl` or Postman.

---

## How to Run Tests

1. **Navigate to the Backend Directory**

   ```bash
   cd file-sync/backend
   ```

2. **Run All Tests**

   ```bash
   ./gradlew test
   ```

3. **View Test Results**

   Test results are available in `build/reports/tests/test/index.html`.

4. **Run Specific Tests**

   ```bash
   ./gradlew test --tests "com.networking.filetransfer.controller.FileControllerTest"
   ```
