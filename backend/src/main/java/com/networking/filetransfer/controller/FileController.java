package com.networking.filetransfer.controller;

import com.networking.filetransfer.service.FileTransferService;
import com.networking.filetransfer.service.S3FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
@RequestMapping("/file-transfer")
public class FileController {

    @Autowired
    private FileTransferService fileTransferService;

    @Autowired
    private S3FileStorageService s3FileStorageService;

    /**
     * Send a file to a remote host using a custom TCP-over-UDP protocol
     * 
     * @param file MultipartFile object containing the file data
     * @param receiverHostname Hostname of the receiver
     * @param receiverPort Port number of the receiver
     * @param bytesToTransfer Number of bytes to transfer
     * @return ResponseEntity containing the result of the file transfer
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendFile(
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

    /**
     * Upload a file to an S3 bucket
     * 
     * @param file MultipartFile object containing the file data
     * @param bucketName Name of the bucket to upload the file to
     * @param fileName Name of the file to upload
     * @return ResponseEntity containing the result of the file upload
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bucketName") String bucketName,
            @RequestParam("fileName") String fileName) {

        try {
            String eTag = s3FileStorageService.uploadFile(bucketName, fileName, file);
            return ResponseEntity.ok("File uploaded successfully. ETag: " + eTag);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    /**
     * Receive a file from a remote host using a custom TCP-over-UDP protocol
     * 
     * @param filename Name of the file to receive
     * @param port Port number to listen on
     * @param fileStorageLocation Directory to store the received file
     * @return ResponseEntity containing the received file as a Resource
     */
    @GetMapping("/receive")
    public ResponseEntity<Resource> receiveFile(
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

    /**
     * Download a file from an S3 bucket
     * 
     * @param bucketName Name of the bucket to download the file from
     * @param fileName Name of the file to download
     * @return ResponseEntity containing the file as an InputStreamResource
     */
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("fileName") String fileName) {

        return s3FileStorageService.downloadFile(bucketName, fileName);
    }
}
