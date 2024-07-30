import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentumServiceTest {

    @InjectMocks
    private DocumentumService documentumService;

    @Mock
    private S3Service s3Service;

    @Mock
    private IDfSessionFactory sessionFactory;

    @Mock
    private IDfSession session;

    @BeforeEach
    public void setUp() {
        when(sessionFactory.getSession(anyString())).thenReturn(session);
    }

    @Test
    public void testStreamFilesToDocumentum_Success() {
        String token = "test-token";
        List<String> keys = Arrays.asList("file1", "file2");
        String bucketName = "test-bucket";

        S3Object s3Object = mock(S3Object.class);
        InputStream inputStream = new ByteArrayInputStream("test-content".getBytes());

        when(s3Service.getObject(bucketName, "file1")).thenReturn(s3Object);
        when(s3Service.getObject(bucketName, "file2")).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(inputStream);

        Boolean result = documentumService.streamFilesToDocumentum(token, keys, bucketName);

        assertTrue(result);
        verify(s3Service, times(1)).getObject(bucketName, "file1");
        verify(s3Service, times(1)).getObject(bucketName, "file2");
        verify(sessionFactory, times(1)).closeSession(session);
    }

    @Test
    public void testStreamFilesToDocumentum_Exception() {
        String token = "test-token";
        List<String> keys = Arrays.asList("file1");
        String bucketName = "test-bucket";

        when(s3Service.getObject(bucketName, "file1")).thenThrow(new RuntimeException("S3 error"));

        Boolean result = documentumService.streamFilesToDocumentum(token, keys, bucketName);

        assertFalse(result);
        verify(s3Service, times(1)).getObject(bucketName, "file1");
        verify(sessionFactory, times(1)).closeSession(session);
    }

    @Test
    public void testSaveFileToDocumentum_Success() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test-content".getBytes());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        IDfDocument newDoc = mock(IDfDocument.class);
        when(session.newObject("dm_document")).thenReturn(newDoc);

        documentumService.saveFileToDocumentum(session, inputStream, "file1");

        verify(newDoc, times(1)).setObjectName("file1");
        verify(newDoc, times(1)).setContent(buffer);
        verify(newDoc, times(1)).save();
    }

    @Test
    public void testSaveFileToDocumentum_Exception() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test-content".getBytes());

        doThrow(new RuntimeException("Save error")).when(session).newObject("dm_document");

        documentumService.saveFileToDocumentum(session, inputStream, "file1");

        verify(session, times(1)).newObject("dm_document");
    }
}
