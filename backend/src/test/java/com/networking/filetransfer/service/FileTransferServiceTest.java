package com.networking.filetransfer.service;

import com.networking.filetransfer.utils.TransferExecutor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class FileTransferServiceTest {

    @InjectMocks
    private FileTransferService fileTransferService;

    @Test
    void testTransferFile_Success() throws IOException, InterruptedException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Mock file content".getBytes());

        Mockito.when(TransferExecutor.sendFile(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn("File sent successfully");

        String result = fileTransferService.transferFile(file, "localhost", 12345, 1024);
        assertEquals("File sent successfully", result);
    }

    @Test
    void testReceiveFile_Success() throws IOException, InterruptedException {
        Mockito.when(TransferExecutor.receiveFile(anyString(), anyInt()))
                .thenReturn("File received successfully");

        String result = fileTransferService.receiveFile("test.txt", 12345, "/mock/path");
        assertEquals("File received successfully", result);
    }
}
