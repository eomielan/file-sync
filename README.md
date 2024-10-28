# File Sync

![Frontend Build & Test Status](https://github.com/eomielan/file-sync/actions/workflows/frontend.yml/badge.svg)
![Backend Build & Test Status](https://github.com/eomielan/file-sync/actions/workflows/backend.yml/badge.svg)
![TCP over UDP Build & Test Status](https://github.com/eomielan/file-sync/actions/workflows/tcp-over-udp.yml/badge.svg)

File Sync is a full-stack application that enables file transfer between two endpoints using a custom **TCP-like protocol over UDP**. The application consists of a React frontend and a Spring Boot backend, with C binaries for handling low-level TCP-like file transfer over UDP.

### Related Repository

This project relies on the **TCP-like protocol over UDP** provided by the [tcp-over-udp repository](https://github.com/eomielan/tcp-over-udp). This repository contains the core C code for the sender and receiver binaries used in file transfers.

## Project Structure

```plaintext
file-sync/
├── backend/                           # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/networking/filetransfer/
│   │   │   │       ├── FileTransferApplication.java   # Main Spring Boot application class
│   │   │   │       ├── controller/
│   │   │   │       │   └── FileController.java        # REST controller
│   │   │   │       ├── service/
│   │   │   │       │   └── FileTransferService.java   # Service for transfer logic
│   │   │   │       └── utils/
│   │   │   │           └── TransferExecutor.java      # Executes sender and receiver binaries
│   │   │   └── resources/
│   │   │       └── application.properties             # Spring Boot configurations
│   ├── tcpOverUdp/                    # Directory for C binaries
│   │   ├── sender.c                   # Sender implementation
│   │   ├── receiver.c                 # Receiver implementation
│   │   └── Makefile                   # Compiles sender and receiver
│   └── build.gradle                   # Gradle dependencies and build configuration
├── frontend/                          # React frontend
│   ├── public/
│   ├── src/
│   │   ├── App.js                     # Main React component
│   │   └── components/                # UI components for specific pages and functionalities
│   │       ├── SenderPage.js          # Component for the sender page
│   │       └── ReceiverPage.js        # Component for the receiver page
│   └── package.json                   # React project dependencies and scripts
├── .gitignore                         # Root .gitignore
└── README.md                          # Project overview and setup instructions
```

## Sender and Receiver Pages

| ![Sender Page](https://github.com/eomielan/file-sync/blob/main/images/sender.png?raw=true) | ![Receiver Page](https://github.com/eomielan/file-sync/blob/main/images/receiver.png?raw=true) |
| :----------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------: |

## Getting Started

This project requires **Node.js** for the frontend, **Java 17** with **Gradle** for the backend, and **GCC** to compile the C binaries for TCP-like functionality over UDP.

### 1. Set Up the Backend

1. **Navigate to the backend directory**:

   ```bash
   cd backend
   ```

2. **Compile the TCP-like Binaries**:

   - Go to the `tcpOverUdp` directory:

     ```bash
     cd tcpOverUdp
     ```

   - Run `make` to compile the `sender` and `receiver` binaries:

     ```bash
     make
     ```

   - Ensure the binaries are executable:

     ```bash
     chmod +x sender receiver
     ```

3. **Configure and Run the Spring Boot Application**:

   - Go back to the `backend` root directory:

     ```bash
     cd ..
     ```

   - Run the backend:

     ```bash
     ./gradlew bootRun
     ```

   - The backend should start on `http://localhost:8080`.

For detailed backend setup and usage, see the [backend README](backend/README.md).

### 2. Set Up the Frontend

1. **Navigate to the frontend directory**:

   ```bash
   cd ../frontend
   ```

2. **Install Dependencies**:

   ```bash
   npm install
   ```

3. **Run the React Application**:

   ```bash
   npm start
   ```

   - Open [http://localhost:3000](http://localhost:3000) to view the frontend in your browser.

For detailed frontend setup and usage, see the [frontend README](frontend/README.md).

## Features

- **File Sending**: Upload and send files to a specified receiver over a custom TCP-like protocol using UDP.
- **File Receiving**: Initiate a file transfer from the receiver endpoint, save the file locally, and download it via the frontend.

## Usage

1. **Sender**: Access the "Sender" page in the React app, where you can specify the file, receiver hostname, port, and byte transfer limit.
2. **Receiver**: Access the "Receiver" page to enter the port, desired filename, and file storage location, then start the receiver process.

## Additional Resources

- [Backend README](backend/README.md)
- [Frontend README](frontend/README.md)
- [TCP-over-UDP Repository](https://github.com/eomielan/tcp-over-udp)
