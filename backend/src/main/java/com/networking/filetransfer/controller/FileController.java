package com.networking.filetransfer.controller;

import com.networking.filetransfer.service.FileTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileTransferService fileTransferService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("receiverHostname") String receiverHostname,
            @RequestParam("receiverPort") int receiverPort,
            @RequestParam("bytesToTransfer") long bytesToTransfer) {

        try {
            String result = fileTransferService.transferFile(file, receiverHostname, receiverPort, bytesToTransfer);
            return ResponseEntity.ok(result);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File transfer failed: " + e.getMessage());
        }
    }
}
