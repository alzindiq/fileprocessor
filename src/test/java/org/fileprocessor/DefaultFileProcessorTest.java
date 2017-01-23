package org.fileprocessor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultFileProcessorTest {
    private DefaultFileProcessor defaultFileProcessor;

    @Before
    public void setup(){
        defaultFileProcessor = new DefaultFileProcessor();
    }

    @Test
    public void testNullFileName(){
        Validation validation = defaultFileProcessor.processFile(null);
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(DefaultFileProcessor.FILE_LOCATION_ERROR));
    }

    @Test
    public void testEmptyFileName(){
        Validation validation = defaultFileProcessor.processFile("");
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(DefaultFileProcessor.FILE_LOCATION_ERROR));
    }


    @Test
    public void testNonExistentFileName(){
        Validation validation = defaultFileProcessor.processFile("madeUpName.txt");
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(DefaultFileProcessor.FILE_LOCATION_ERROR));
    }

    @Test
    public void testDirectoryFileName() {
        Validation validation = defaultFileProcessor.processFile(".");
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(DefaultFileProcessor.FILE_LOCATION_ERROR));
    }

    @Test
    public void testGetKeyNamesNullProcessors(){
        Validation validation = defaultFileProcessor.processFile(null);
        assertTrue(defaultFileProcessor.getKeyNames().size()==0);
    }

    @Test
    public void testGetKVMpNullProcessors(){
        Validation validation = defaultFileProcessor.processFile(null);
        assertTrue(defaultFileProcessor.getKVForProcessedLines().size()==0);
    }
}