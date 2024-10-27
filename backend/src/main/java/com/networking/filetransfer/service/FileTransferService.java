package com.networking.filetransfer.service;

import com.networking.filetransfer.utils.TransferExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileTransferService {

    public String transferFile(MultipartFile file, String receiverHostname, int receiverPort, long bytesToTransfer)
            throws IOException, InterruptedException {

        // Save the file temporarily
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        // Initiate file transfer via the custom TCP-over-UDP protocol
        return TransferExecutor.sendFile(tempFile.getAbsolutePath(), receiverHostname, receiverPort, bytesToTransfer);
    }

    public String receiveFile(String filename, int port, String fileStorageLocation) throws IOException, InterruptedException {
        Path storagePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        Path filePath = storagePath.resolve(filename).normalize();
        return TransferExecutor.receiveFile(filePath.toString(), port);
    }
}
