package com.networking.filetransfer.controller;

import com.networking.filetransfer.service.FileTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;

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

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam("filename") String filename,
            @RequestParam("port") int port,
            @RequestHeader("fileStorageLocation") String fileStorageLocation) {

        try {
            String filePath = fileTransferService.receiveFile(filename, port, fileStorageLocation);
            Resource resource = new UrlResource(Paths.get(filePath).toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (IOException | InterruptedException ex) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
