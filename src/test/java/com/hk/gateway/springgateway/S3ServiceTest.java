@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private S3Object mockS3Object;

    @Mock
    private S3ObjectInputStream mockInputStream;

    @Mock
    private DocumentumService documentumService;

    @Test
    void testProcessCsvAndInsert_success() throws IOException, DfException {
        // Prepare mock S3 data
        String csvContent = "column1,column2,column3\nvalue1,value2,value3\nvalue4,value5,value6";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        when(amazonS3.getObject(anyString(), anyString())).thenReturn(mockS3Object);
        when(mockS3Object.getObjectContent()).thenReturn(mockInputStream);
        when(mockInputStream.readAllBytes()).thenReturn(csvContent.getBytes());

        // Mock DocumentumService behavior
        doNothing().when(documentumService).insertBatchRows(anyList());
        doNothing().when(documentumService).deletePreviousDayRecords();

        // Execute the method
        s3Service.processCsvAndInsert("test-bucket", "test-key");

        // Verify interactions
        verify(amazonS3).getObject("test-bucket", "test-key");
        verify(documentumService, times(1)).insertBatchRows(anyList());
        verify(documentumService, times(1)).deletePreviousDayRecords();
    }

    @Test
    void testProcessCsvAndInsert_handlesIOException() throws IOException {
        // Simulate an IOException when reading the S3 object
        when(amazonS3.getObject(anyString(), anyString())).thenThrow(new IOException("Mocked IOException"));

        // Assert exception handling
        IOException exception = assertThrows(IOException.class, () -> {
            s3Service.processCsvAndInsert("test-bucket", "test-key");
        });
        assertEquals("Mocked IOException", exception.getMessage());
    }
}
