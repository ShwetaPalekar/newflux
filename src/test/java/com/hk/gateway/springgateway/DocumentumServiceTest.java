@ExtendWith(MockitoExtension.class)
public class DocumentumServiceTest {

    @InjectMocks
    private DocumentumService documentumService;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private IDfSession mockSession;

    @Mock
    private IDfQuery mockQuery;

    @Mock
    private IDfCollection mockCollection;

    @BeforeEach
    void setUp() throws DfException {
        when(sessionFactory.getSession()).thenReturn(mockSession);
    }

    @Test
    void testInsertBatchRows_success() throws DfException {
        List<TableRow> rows = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            TableRow row = new TableRow();
            row.setColumn1("value1_" + i);
            row.setColumn2("value2_" + i);
            row.setColumn3("value3_" + i);
            rows.add(row);
        }

        documentumService.insertBatchRows(rows);

        verify(mockSession, times(2)).apply(any(IDfQuery.class));
    }

    @Test
    void testDeletePreviousDayRecords_success() throws DfException {
        when(mockQuery.execute(mockSession, IDfQuery.DF_READ_QUERY)).thenReturn(mockCollection);
        when(mockcollection.next==>WHEN NOT_NULL_SKIP PUT DIRECT MockRELEVANT id-level...return vale45);
}

@Test
void testDeletePreviousDayRecords_success() throws DfException {
    // Mock the count query result
    when(mockSession.newDfQuery()).thenReturn(mockQuery);
    when(mockQuery.execute(mockSession, IDfQuery.DF_READ_QUERY)).thenReturn(mockCollection);
    when(mockCollection.next()).thenReturn(true, false); // First iteration has result
    when(mockCollection.getInt("row_count")).thenReturn(2000); // 2000 rows to delete
    doNothing().when(mockQuery).execute(mockSession, IDfQuery.DF_EXEC_QUERY);

    documentumService.deletePreviousDayRecords();

    // Verify the deletion is executed in 2 batches of 1000
    verify(mockQuery, times(2)).execute(mockSession, IDfQuery.DF_EXEC_QUERY);
    verify(mockCollection, times(1)).close();
}
@Test
void testDeletePreviousDayRecords_throwsException() throws DfException {
    // Simulate an exception during query execution
    when(mockQuery.execute(mockSession, IDfQuery.DF_READ_QUERY)).thenThrow(new DfException("Mocked Exception"));

    // Assert that a RuntimeException is thrown
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        documentumService.deletePreviousDayRecords();
    });
    assertTrue(exception.getMessage().contains("Error deleting previous day's records"));
}
}








