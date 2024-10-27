package com.networking.filetransfer.service;

import com.example.filetransfer.utils.TransferExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
