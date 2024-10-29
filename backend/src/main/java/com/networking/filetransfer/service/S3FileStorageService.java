package com.networking.filetransfer.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;

@Service
public class S3FileStorageService {

    private final S3Client s3Client;

    public S3FileStorageService() {
        Dotenv dotenv = Dotenv.load();
        String region = dotenv.get("AWS_REGION");
        this.s3Client = S3Client.builder()
                .region(region != null ? Region.of(region) : Region.US_EAST_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    /**
     * Upload a file to an S3 bucket
     * 
     * @param bucketName Name of the bucket to upload the file to
     * @param fileName Name of the file to upload
     * @param file MultipartFile object containing the file data
     * @return ETag of the uploaded file
     */
    public String uploadFile(String bucketName, String fileName, MultipartFile file) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Upload file data to S3
            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            return response.eTag(); // Return ETag as confirmation

        } catch (IOException | S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    /**
     * Download a file from an S3 bucket
     * 
     * @param bucketName Name of the bucket to download the file from
     * @param fileName Name of the file to download
     * @return ResponseEntity containing the file as an InputStreamResource
     */
    public ResponseEntity<InputStreamResource> downloadFile(String bucketName, String fileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Retrieve file as InputStream
            InputStream fileStream = s3Client.getObject(getObjectRequest);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(fileStream));

        } catch (S3Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
