import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileProcessorTest {
    private FileProcessor fileProcessor;

    @Before
    public void setup(){
        fileProcessor = new FileProcessor();
    }

    @Test
    public void testNullFileName(){
        Validation validation = fileProcessor.processFile(null);
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(FileProcessor.FILE_LOCATION_ERROR));
    }

    @Test
    public void testEmptyFileName(){
        Validation validation = fileProcessor.processFile("");
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(FileProcessor.FILE_LOCATION_ERROR));
    }


    @Test
    public void testNonExistentFileName(){
        Validation validation = fileProcessor.processFile("madeUpName.txt");
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(FileProcessor.FILE_LOCATION_ERROR));
    }

    @Test
    public void testDirectoryFileName() {
        Validation validation = fileProcessor.processFile(".");
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(FileProcessor.FILE_LOCATION_ERROR));
    }

}
