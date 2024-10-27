# File Transfer API

This project provides a backend API for transferring files using a custom TCP-over-UDP protocol. It includes endpoints for sending and receiving files, allowing you to specify the file storage location dynamically in each request.

## Related Repository
This project relies on the tcp-over-udp implementation for file transfer. You can find the full implementation and details of the TCP-over-UDP protocol in the [tcp-over-udp repository](https://github.com/eomielan/tcp-over-udp).

## Base URL

All endpoints are prefixed with `/file-transfer`.

Example:
```
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
