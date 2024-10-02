import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.documentum.fc.common.DfException;

public class CIDProcessorUtilTest {

    @Mock
    private IDfSysObject mockDocument;

    @InjectMocks
    private CIDProcessorUtil cidProcessorUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for setDocumentIdHex with "cba_ppow" type
    @Test
    public void testSetDocumentIdHex_cbaPpowType() throws DfException {
        when(mockDocument.getTypeName()).thenReturn("cba_ppow");
        when(mockDocument.getObjectId()).thenReturn(mockObjectId("0900000000000001"));

        IDfSysObject result = cidProcessorUtil.setDocumentIdHex(mockDocument);

        verify(mockDocument).setString("c_documentid", "1450001");
        assertEquals(mockDocument, result);
    }

    // Test for setDocumentIdHex with "cba_paf" and "VENDOR" bizchannel
    @Test
    public void testSetDocumentIdHex_cbaPafType_VendorBizChannel() throws DfException {
        when(mockDocument.getTypeName()).thenReturn("cba_paf");
        when(mockDocument.getObjectId()).thenReturn(mockObjectId("0900000000000002"));
        when(mockDocument.getString("c_paf_bizchannel")).thenReturn("VENDOR");

        IDfSysObject result = cidProcessorUtil.setDocumentIdHex(mockDocument);

        verify(mockDocument).setString("c_documentid", "1450003");
        assertEquals(mockDocument, result);
    }

    // Test for setDocumentIdHex with "cba_paf" and non-"VENDOR" bizchannel
    @Test
    public void testSetDocumentIdHex_cbaPafType_NonVendorBizChannel() throws DfException {
        when(mockDocument.getTypeName()).thenReturn("cba_paf");
        when(mockDocument.getObjectId()).thenReturn(mockObjectId("0900000000000003"));
        when(mockDocument.getString("c_paf_bizchannel")).thenReturn("NON_VENDOR");

        IDfSysObject result = cidProcessorUtil.setDocumentIdHex(mockDocument);

        verify(mockDocument).setString("c_documentid", "1450002");
        assertEquals(mockDocument, result);
    }

    // Test for setDocumentIdHex when bizchannel is null
    @Test
    public void testSetDocumentIdHex_NullBizChannel() throws DfException {
        when(mockDocument.getTypeName()).thenReturn("cba_paf");
        when(mockDocument.getObjectId()).thenReturn(mockObjectId("0900000000000004"));
        when(mockDocument.getString("c_paf_bizchannel")).thenReturn(null);

        IDfSysObject result = cidProcessorUtil.setDocumentIdHex(mockDocument);

        verify(mockDocument).setString("c_documentid", "1450002");
        assertEquals(mockDocument, result);
    }

    // Test for setDocumentIdHex when object type is not recognized (default case)
    @Test
    public void testSetDocumentIdHex_DefaultType() throws DfException {
        when(mockDocument.getTypeName()).thenReturn("unknown_type");
        when(mockDocument.getObjectId()).thenReturn(mockObjectId("0900000000000005"));

        IDfSysObject result = cidProcessorUtil.setDocumentIdHex(mockDocument);

        verify(mockDocument).setString("c_documentid", "1450005");
        assertEquals(mockDocument, result);
    }

    // Test for setDocumentIdInt with setObjectName = true
    @Test
    public void testSetDocumentIdInt_SetObjectNameTrue() throws DfException {
        when(mockDocument.getObjectId()).thenReturn(mockObjectId("0900000000000006"));

        IDfSysObject result = cidProcessorUtil.setDocumentIdInt(mockDocument, true);

        verify(mockDocument).setString("object_name", "24" + "1450006.pdf");
        verify(mockDocument).setString("c_documentid", "1450006");
        assertEquals(mockDocument, result);
    }

    // Test for setDocumentIdInt with setObjectName = false
    @Test
    public void testSetDocumentIdInt_SetObjectNameFalse() throws DfException {
        when(mockDocument.getObjectId()).thenReturn(mockObjectId("0900000000000007"));

        IDfSysObject result = cidProcessorUtil.setDocumentIdInt(mockDocument, false);

        verify(mockDocument).setString("c_documentid", "1450007");
        assertEquals(mockDocument, result);
    }

    // Test for setDocumentIdInt when object ID is at its minimum value
    @Test
    public void testSetDocumentIdInt_MinObjectId() throws DfException {
        when(mockDocument.getObjectId()).thenReturn(mockObjectId("0900000000000000"));

        IDfSysObject result = cidProcessorUtil.setDocumentIdInt(mockDocument, false);

        verify(mockDocument).setString("c_documentid", "1450000");
        assertEquals(mockDocument, result);
    }

    // Test for setDocumentIdInt when object ID is at its maximum value
    @Test
    public void testSetDocumentIdInt_MaxObjectId() throws DfException {
        when(mockDocument.getObjectId()).thenReturn(mockObjectId("090000000FFFFFFFFF"));

        IDfSysObject result = cidProcessorUtil.setDocumentIdInt(mockDocument, false);

        verify(mockDocument).setString("c_documentid", "145FFFFFFFF");
        assertEquals(mockDocument, result);
    }

    // Test for setDocumentIdInt when getObjectId throws an exception
    @Test(expected = DfException.class)
    public void testSetDocumentIdInt_GetObjectIdException() throws DfException {
        when(mockDocument.getObjectId()).thenThrow(new DfException("Object ID retrieval error"));

        cidProcessorUtil.setDocumentIdInt(mockDocument, false);
    }

    // Test for setDocumentIdHex when getString throws an exception
    @Test(expected = DfException.class)
    public void testSetDocumentIdHex_GetStringException() throws DfException {
        when(mockDocument.getTypeName()).thenReturn("cba_paf");
        when(mockDocument.getObjectId()).thenReturn(mockObjectId("0900000000000008"));
        when(mockDocument.getString("c_paf_bizchannel")).thenThrow(new DfException("Attribute retrieval error"));

        cidProcessorUtil.setDocumentIdHex(mockDocument);
    }

    // Helper method to mock ObjectId for IDfSysObject
    private IDfId mockObjectId(String idValue) {
        IDfId mockObjectId = mock(IDfId.class);
        when(mockObjectId.getId()).thenReturn(idValue);
        return mockObjectId;
    }
}
