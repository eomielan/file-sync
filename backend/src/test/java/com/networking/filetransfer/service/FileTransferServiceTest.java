package com.networking.filetransfer.service;

import com.networking.filetransfer.utils.TransferExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FileTransferServiceTest {

    @InjectMocks
    private FileTransferService fileTransferService;

    @Mock
    private TransferExecutor transferExecutor;

    @BeforeEach
    void setup() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        tempDir.mkdirs();
    }

    @Test
    void testTransferFile_Success() throws IOException, InterruptedException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "File content".getBytes());

        Mockito.when(TransferExecutor.sendFile(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn("File sent successfully");

        String result = fileTransferService.transferFile(file, "localhost", 12345, 1024);
        assertEquals("File sent successfully", result);
    }

    @Test
    void testReceiveFile_Success() throws IOException, InterruptedException {
        Mockito.when(TransferExecutor.receiveFile(anyString(), anyInt()))
                .thenReturn("File received successfully");

        String result = fileTransferService.receiveFile("test.txt", 12345, "/downloads");
        assertEquals("File received successfully", result);
    }
}
