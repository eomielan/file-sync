# File Sync

## Project Structure

```Markdown
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
│   │   │   │           └── TransferExecutor.java      # Executes sender binary
│   │   │   └── resources/
│   │   │       └── application.properties             # Spring Boot configurations
│   ├── tcpOverUdp/                    # Directory for C binaries
│   │   ├── sender.c                   # Sender implementation
│   │   ├── receiver.c                 # Receiver implementation
│   │   └── Makefile                   # Compiles sender and receiver
│   └── pom.xml                        # Maven dependencies and build configuration for Spring Boot
├── frontend/                          # React frontend
│   ├── public/
│   ├── src/
│   │   ├── App.js                     # Main React component
│   │   └── components/                # Additional UI components
│   └── package.json                   # React project dependencies and scripts
├── .gitignore                         # Root .gitignore
└── README.md                          # Project overview and setup instructions
```
