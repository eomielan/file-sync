package com.networking.filetransfer.controller;

import com.networking.filetransfer.service.FileTransferService;
import com.networking.filetransfer.service.S3FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FileControllerTest {

    @Mock
    private FileTransferService fileTransferService;

    @Mock
    private S3FileStorageService s3FileStorageService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendFile_Success() throws IOException, InterruptedException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        when(fileTransferService.transferFile(any(), anyString(), anyInt(), anyLong())).thenReturn("File transfer complete");

        ResponseEntity<String> response = fileController.sendFile(mockFile, "localhost", 8080, 1024);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File transfer complete", response.getBody());
    }

    @Test
    void testSendFile_Failure() throws IOException, InterruptedException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        when(fileTransferService.transferFile(any(), anyString(), anyInt(), anyLong())).thenThrow(new IOException("Transfer error"));

        ResponseEntity<String> response = fileController.sendFile(mockFile, "localhost", 8080, 1024);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("File transfer failed: Transfer error", response.getBody());
    }

    @Test
    void testUploadFile_Success() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        when(s3FileStorageService.uploadFile(anyString(), anyString(), any())).thenReturn("eTag123");

        ResponseEntity<String> response = fileController.uploadFile(mockFile, "test-bucket", "test-file");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File uploaded successfully. ETag: eTag123", response.getBody());
    }

    @Test
    void testUploadFile_Failure() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        when(s3FileStorageService.uploadFile(anyString(), anyString(), any())).thenThrow(new RuntimeException("Upload error"));

        ResponseEntity<String> response = fileController.uploadFile(mockFile, "test-bucket", "test-file");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("File upload failed: Upload error", response.getBody());
    }

    @Test
    void testReceiveFile_Success() throws IOException, InterruptedException {
        String filePath = "/path/to/file.txt";
        when(fileTransferService.receiveFile(anyString(), anyInt(), anyString())).thenReturn(filePath);
        when(fileTransferService.receiveFile(anyString(), anyInt(), anyString())).thenReturn(filePath);

        ResponseEntity<Resource> response = fileController.receiveFile("file.txt", 8080, "/storage/location");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("attachment; filename=\"" + filePath + "\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }

    @Test
    void testReceiveFile_NotFound() throws IOException, InterruptedException {
        when(fileTransferService.receiveFile(anyString(), anyInt(), anyString())).thenReturn(null);

        ResponseEntity<Resource> response = fileController.receiveFile("file.txt", 8080, "/storage/location");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testReceiveFile_Failure() throws IOException, InterruptedException {
        when(fileTransferService.receiveFile(anyString(), anyInt(), anyString())).thenThrow(new IOException("Receive error"));

        ResponseEntity<Resource> response = fileController.receiveFile("file.txt", 8080, "/storage/location");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testDownloadFile_Success() {
        InputStreamResource mockResource = mock(InputStreamResource.class);
        ResponseEntity<InputStreamResource> mockResponse = new ResponseEntity<>(mockResource, HttpStatus.OK);

        when(s3FileStorageService.downloadFile(anyString(), anyString())).thenReturn(mockResponse);

        ResponseEntity<InputStreamResource> response = fileController.downloadFile("test-bucket", "test-file");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
