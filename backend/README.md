# File Sync Backend

This project provides backend APIs for transferring files using a custom TCP-over-UDP protocol. It includes endpoints for sending and receiving files, allowing you to specify the file storage location dynamically in each request.

## Related Repository

This project relies on the tcp-over-udp implementation for file transfer. You can find the full implementation and details of the TCP-over-UDP protocol in the [tcp-over-udp repository](https://github.com/eomielan/tcp-over-udp).

## Base URL

All endpoints are prefixed with `/file-transfer`.

Example:

```plaintext
http://localhost:8080/file-transfer
```

## Endpoints

### 1. Upload and Send File

Upload a file and send it to a specified receiver.

- **URL**: `/file-transfer/send`
- **Method**: `POST`
- **Description**: Uploads a file to the backend and sends it to the specified receiver over a custom TCP-over-UDP protocol.

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

### 2. Receive and Download File

Trigger the receiver to download a file over TCP-over-UDP, then serve it as a downloadable resource.

- **URL**: `/file-transfer/receive`
- **Method**: `GET`
- **Description**: Initiates the file reception via the custom TCP-over-UDP protocol and provides the file for download.

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

   Clone the repository to your local machine:

   ```bash
   git clone https://github.com/eomielan/file-sync.git
   cd file-sync/backend
   ```

2. **Compile the TCP-over-UDP Binaries**

   The backend requires the `sender` and `receiver` binaries from the [tcp-over-udp repository](https://github.com/eomielan/tcp-over-udp). Compile these binaries by running `make` in the `src/tcpOverUdp` directory:

   ```bash
   cd tcpOverUdp
   make
   ```

   - This will generate the `sender` and `receiver` executables required for file transfer.
   - Ensure both binaries are executable (if not, use `chmod +x sender receiver`).

3. **Configure Application Properties**

   Open `src/main/resources/application.properties` to configure any necessary properties, such as server port (optional):

   ```properties
   # Default server port (optional)
   server.port=8080
   ```

4. **Run the Backend**

   Start the Spring Boot backend using the Gradle Wrapper:

   ```bash
   cd ..  # Navigate back to the project root
   ./gradlew bootRun
   ```

   Alternatively, if you have Gradle installed, you can also use:

   ```bash
   gradle bootRun
   ```

   This command will start the backend server on `http://localhost:8080` (or on the port specified in `application.properties`).

5. **Test the Endpoints**

   You can now test the `/file-transfer/send` and `/file-transfer/receive` endpoints using `curl` or Postman. Ensure that the receiver is set up and listening on the specified port when testing the `send` endpoint.

---

## How to Run Tests

The backend includes unit and integration tests for verifying file transfer functionality, error handling, and endpoint responses. These tests are implemented using **JUnit 5** and **Mockito**.

### Running Tests with Gradle

1. **Navigate to the Backend Directory**

   Make sure you’re in the `backend` directory:

   ```bash
   cd file-sync/backend
   ```

2. **Run All Tests**

   To run all tests, use the following Gradle command:

   ```bash
   ./gradlew test
   ```

   Alternatively, if Gradle is installed, you can use:

   ```bash
   gradle test
   ```

3. **View Test Results**

   Test results will be available in the `build/reports/tests/test/index.html` file. Open this file in a browser to view a detailed test report.

### Running Tests for a Specific Class

If you want to run tests for a specific class, use the following command:

```bash
./gradlew test --tests "com.networking.filetransfer.controller.FileControllerTest"
```

Replace `FileControllerTest` with the specific test class name.
