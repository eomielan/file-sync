# File Sync Frontend

This project provides a React frontend interface for a file transfer application using a custom TCP-over-UDP protocol. The application allows users to send and receive files through a backend API.

## Getting Started

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

### Prerequisites

- **Node.js** and **npm**: Make sure you have Node.js and npm installed. You can download them from [https://nodejs.org](https://nodejs.org).

## Available Scripts

In the project directory, you can run the following scripts:

### `npm start`

Runs the app in development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will automatically reload if you make changes to the source files. You may also see lint errors in the console.

### `npm test`

Launches the test runner in interactive watch mode. See [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder. It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified, and filenames include hashes. The app is ready to be deployed!

### `npm run eject`

**Note: This is a one-way operation. Once you `eject`, you can't go back!**

If you need to customize the build configuration, you can `eject` the app to gain full control over the configuration files.

## Proxying Backend Requests

The frontend application uses a proxy defined in `package.json` to forward API requests to the backend server. This avoids hardcoding URLs and makes it easy to work with the backend during development.

To set up the proxy:

1. Open `package.json`.
2. Add the following line:

   ```json
   "proxy": "http://localhost:8080"
   ```

This allows you to make API requests directly to `/file-transfer` without specifying the full backend URL.

## Using the App

### 1. Send File

Navigate to the "Sender" page to select a file, specify the receiver's hostname, port, and byte limit, then initiate the transfer.

### 2. Receive File

On the "Receiver" page, enter the port, filename to save the file as, and file storage location. Click **Start Receiver** to initiate the download process.
