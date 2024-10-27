package com.networking.filetransfer.utils;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TransferExecutorTest {

    @Test
    void testSendFile_Success() throws InterruptedException {
        String result = TransferExecutor.sendFile("/path/to/testfile.txt", "localhost", 12345, 1024);

        assertTrue(result.contains("File sent successfully"));
    }

    @Test
    void testReceiveFile_Success() throws InterruptedException {
        String result = TransferExecutor.receiveFile("/path/to/downloaded_file.txt", 12345);

        assertTrue(result.contains("File received successfully"));
    }
}
