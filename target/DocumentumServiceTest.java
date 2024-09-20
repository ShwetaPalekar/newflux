import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocumentumServiceTest {

    private IDfSession session;
    private IDfFolder folder;
    private DocumentumService documentumService;

    @BeforeEach
    public void setUp() {
        session = mock(IDfSession.class);
        folder = mock(IDfFolder.class);
        documentumService = new DocumentumService();
    }

    @Test
    public void testEnsureFolderExists_FolderAlreadyExists() throws Exception {
        // Given
        String folderPath = "/existingFolder";
        when(session.getObjectByQualification("dm_folder where any r_folder_path = '" + folderPath + "'"))
            .thenReturn(folder);

        // When
        IDfFolder result = documentumService.ensureFolderExists(session, folderPath);

        // Then
        verify(session, never()).newObject("dm_folder"); // Folder creation should not happen
        verify(folder, never()).save();
        assertNotNull(result);
        System.out.println("Folder exists: " + folderPath);
    }

    @Test
    public void testEnsureFolderExists_FolderDoesNotExist() throws Exception {
        // Given
        String folderPath = "/newFolder/subFolder";
        when(session.getObjectByQualification("dm_folder where any r_folder_path = '" + folderPath + "'"))
            .thenReturn(null); // Folder does not exist
        when(session.newObject("dm_folder")).thenReturn(folder);

        // When
        IDfFolder result = documentumService.ensureFolderExists(session, folderPath);

        // Then
        verify(session).newObject("dm_folder"); // Ensure new folder is created
        verify(folder).setObjectName("subFolder"); // Ensure folder name is set
        verify(folder).link("/newFolder"); // Ensure the folder is linked to the parent
        verify(folder).save(); // Ensure the folder is saved

        assertNotNull(result);
        System.out.println("Created new folder: " + folderPath);
    }

    @Test
    public void testEnsureFolderExists_ThrowsException() throws Exception {
        // Given
        String folderPath = "/errorFolder";
        when(session.getObjectByQualification("dm_folder where any r_folder_path = '" + folderPath + "'"))
            .thenThrow(new DfException("Error fetching folder"));

        // When & Then
        Exception exception = assertThrows(DfException.class, () -> {
            documentumService.ensureFolderExists(session, folderPath);
        });

        assertEquals("Error fetching folder", exception.getMessage());
    }

    @Test
    public void testEnsureFolderExists_FolderCreationThrowsException() throws Exception {
        // Given
        String folderPath = "/newFolder/subFolder";
        when(session.getObjectByQualification("dm_folder where any r_folder_path = '" + folderPath + "'"))
            .thenReturn(null); // Folder does not exist
        when(session.newObject("dm_folder")).thenThrow(new DfException("Error creating folder"));

        // When & Then
        Exception exception = assertThrows(DfException.class, () -> {
            documentumService.ensureFolderExists(session, folderPath);
        });

        assertEquals("Error creating folder", exception.getMessage());
    }
}


            // Ensure the folder exists or create it if necessary
            IDfFolder folder = ensureFolderExists(session, folderPath);

    private IDfFolder ensureFolderExists(IDfSession session, String folderPath) throws Exception {
        String dqlQuery = "dm_folder where any r_folder_path = '" + folderPath + "'";
        IDfFolder folder = (IDfFolder) session.getObjectByQualification(dqlQuery);

        if (folder == null) {
            folder = (IDfFolder) session.newObject("dm_folder");
            folder.setObjectName(folderPath.substring(folderPath.lastIndexOf("/") + 1));
            folder.link(folderPath.substring(0, folderPath.lastIndexOf("/")));
            folder.save();
            System.out.println("Created new folder: " + folderPath);
        } else {
            System.out.println("Folder exists: " + folderPath);
        }

        return folder;
    }
