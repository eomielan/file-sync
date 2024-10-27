package com.networking.filetransfer.controller;

import com.networking.filetransfer.service.FileTransferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(FileController.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileTransferService fileTransferService;

    @Test
    void testUploadFile_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "File content".getBytes());

        Mockito.when(fileTransferService.transferFile(any(), anyString(), anyInt(), anyLong()))
                .thenReturn("File sent successfully");

        mockMvc.perform(multipart("/file-transfer/upload")
                        .file(file)
                        .param("receiverHostname", "localhost")
                        .param("receiverPort", "12345")
                        .param("bytesToTransfer", "1024"))
                .andExpect(status().isOk())
                .andExpect(content().string("File sent successfully"));
    }

    @Test
    void testDownloadFile_Success() throws Exception {
        Mockito.when(fileTransferService.receiveFile(anyString(), anyInt(), anyString()))
                .thenReturn("/path/to/received_file.txt");

        mockMvc.perform(get("/file-transfer/download")
                        .param("filename", "received_file.txt")
                        .param("port", "12345")
                        .header("fileStorageLocation", "/downloads"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"received_file.txt\""));
    }
}
