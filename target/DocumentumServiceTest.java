import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

public class YourClassTest {

    // Mock the dependencies
    IDfSysObject document = mock(IDfSysObject.class);
    IDfAttr type = mock(IDfAttr.class);
    Logger logger = mock(Logger.class);  // Assuming you have a logger

    YourClass yourClassInstance = new YourClass();  // Your class under test

    // Parameterized test using CsvSource for different attribute types
    @ParameterizedTest
    @CsvSource({
        "DM_STRING, testAttr, stringValue, true",
        "DM_STRING, testAttr, stringValue, false",
        "DM_INTEGER, testAttr, 123, true",
        "DM_INTEGER, testAttr, 123, false",
        "DM_DOUBLE, testAttr, 123.45, true",
        "DM_DOUBLE, testAttr, 123.45, false",
        "DM_BOOLEAN, testAttr, true, true",
        "DM_BOOLEAN, testAttr, true, false"
    })
    void testSetAttributes(String attrType, String attrName, String value, boolean isAttributeSingle) throws Exception {
        // Arrange
        int dfAttrType;
        switch (attrType) {
            case "DM_STRING":
                dfAttrType = IDfAttr.DM_STRING;
                break;
            case "DM_INTEGER":
                dfAttrType = IDfAttr.DM_INTEGER;
                break;
            case "DM_DOUBLE":
                dfAttrType = IDfAttr.DM_DOUBLE;
                break;
            case "DM_BOOLEAN":
                dfAttrType = IDfAttr.DM_BOOLEAN;
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }

        when(type.getTypeAttrDataType(attrName)).thenReturn(dfAttrType);

        // Act
        yourClassInstance.setAttribute(type, attrName, value, isAttributeSingle, document);

        // Assert
        switch (dfAttrType) {
            case IDfAttr.DM_STRING:
                if (isAttributeSingle) {
                    verify(document).setString(attrName, value);
                } else {
                    verify(document).appendString(attrName, value);
                }
                break;
            case IDfAttr.DM_INTEGER:
                if (isAttributeSingle) {
                    verify(document).setInt(attrName, Integer.parseInt(value));
                } else {
                    verify(document).appendInt(attrName, Integer.parseInt(value));
                }
                break;
            case IDfAttr.DM_DOUBLE:
                if (isAttributeSingle) {
                    verify(document).setDouble(attrName, Double.parseDouble(value));
                } else {
                    verify(document).appendDouble(attrName, Double.parseDouble(value));
                }
                break;
            case IDfAttr.DM_BOOLEAN:
                if (isAttributeSingle) {
                    verify(document).setBoolean(attrName, Boolean.parseBoolean(value));
                } else {
                    verify(document).appendBoolean(attrName, Boolean.parseBoolean(value));
                }
                break;
        }
    }
}
