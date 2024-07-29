package com.example.lambdas3documentum.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.IDfSysObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentumServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private IDfSessionManager sessionManager;

    @Mock
    private IDfSession session;

    @Mock
    private IDfSysObject sysObject;

    @InjectMocks
    private DocumentumService documentumService;

    @BeforeEach
    void setUp() throws Exception {
        when(sessionManager.getSession(anyString())).thenReturn(session);
        when(session.newObject(anyString())).thenReturn(sysObject);
    }

    @Test
    void testStreamFilesToDocumentum() throws Exception {
        List<String> keys = Arrays.asList("file1", "file2");
        String bucketName = "test-bucket";

        documentumService.streamFilesToDocumentum(keys, bucketName);

        verify(sessionManager, times(1)).getSession(anyString());
        verify(sessionManager, times(2)).release(session); // Ensure session is released for each file
        verify(amazonS3, times(keys.size())).getObject(anyString(), anyString());
    }

    @Test
    void testStreamFilesToDocumentumWithError() throws Exception {
        List<String> keys = Arrays.asList("file1", "file2");
        String bucketName = "test-bucket";
        when(sessionManager.getSession(anyString())).thenThrow(new RuntimeException("Session error"));

        assertThrows(RuntimeException.class, () -> documentumService.streamFilesToDocumentum(keys, bucketName));

        verify(sessionManager, times(1)).getSession(anyString());
    }

    @Test
    void testProcessFile() {
        String bucketName = "test-bucket";
        String key = "file1";
        S3Object s3Object = mock(S3Object.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        when(amazonS3.getObject(bucketName, key)).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(inputStream);

        Mono<Void> result = documentumService.processFile(session, bucketName, key);

        verify(amazonS3, times(1)).getObject(bucketName, key);
        verify(sessionManager, times(1)).release(session);
        assertEquals(Mono.empty(), result);
    }

    @Test
    void testProcessFileWithError() {
        String bucketName = "test-bucket";
        String key = "file1";
        when(amazonS3.getObject(bucketName, key)).thenThrow(new RuntimeException("S3 error"));

        assertThrows(RuntimeException.class, () -> documentumService.processFile(session, bucketName, key).block());

        verify(amazonS3, times(1)).getObject(bucketName, key);
        verify(sessionManager, times(1)).release(session);
    }

    @Test
    void testSaveFileToDocumentum() throws Exception {
        String key = "file1";
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});

        documentumService.saveFileToDocumentum(session, inputStream, key);

        ArgumentCaptor<byte[]> contentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(sysObject).setObjectName(key);
        verify(sysObject).setContentType("application/octet-stream");
        verify(sysObject).setContent(contentCaptor.capture());
        verify(sysObject).save();

        byte[] savedContent = contentCaptor.getValue();
        assertEquals(4, savedContent.length);
        assertEquals(1, savedContent[0]);
        assertEquals(2, savedContent[1]);
        assertEquals(3, savedContent[2]);
        assertEquals(4, savedContent[3]);
    }

    @Test
    void testSaveFileToDocumentumWithError() throws Exception {
        String key = "file1";
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        doThrow(new IOException("IO error")).when(sysObject).save();

        documentumService.saveFileToDocumentum(session, inputStream, key);

        ArgumentCaptor<byte[]> contentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(sysObject).setObjectName(key);
        verify(sysObject).setContentType("application/octet-stream");
        verify(sysObject).setContent(contentCaptor.capture());
        verify(sysObject).save(); // This will throw IOException

        byte[] savedContent = contentCaptor.getValue();
        assertEquals(4, savedContent.length);
        assertEquals(1, savedContent[0]);
        assertEquals(2, savedContent[1]);
        assertEquals(3, savedContent[2]);
        assertEquals(4, savedContent[3]);
    }

    @Test
    void testSaveFileToDocumentumWithReadError() throws Exception {
        String key = "file1";
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException("Read error"));

        documentumService.saveFileToDocumentum(session, inputStream, key);

        verify(sysObject, never()).save();
    }
}
