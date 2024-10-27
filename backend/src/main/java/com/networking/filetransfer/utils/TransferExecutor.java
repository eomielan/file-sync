package com.networking.filetransfer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TransferExecutor {

    public static String sendFile(String filePath, String receiverHostname, int receiverPort, long bytesToTransfer)
            throws InterruptedException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "./tcpOverUdp/sender", receiverHostname, String.valueOf(receiverPort), filePath, String.valueOf(bytesToTransfer));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "File sent successfully:\n" + output;
            } else {
                return "Failed to send file. Error:\n" + output;
            }

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String receiveFile(String filePath, int port) throws InterruptedException {
        try {
            // Ensure the file directory exists
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "./tcpOverUdp/receiver", String.valueOf(port), filePath);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "File received successfully:\n" + output;
            } else {
                return "Failed to receive file. Error:\n" + output;
            }

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
