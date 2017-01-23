package org.fileprocessor;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultFileProcessorITest {
    private DefaultFileProcessor defaultFileProcessor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setup(){
        defaultFileProcessor = new DefaultFileProcessor();
    }

    @Test
    public void testNullProcessorShouldFail(){
        LineProcessor lineProcessor = null;
        URL url = DefaultFileProcessorITest.class.getClassLoader().getResource("testFileUTF8_NO_BOM.txt");
        Validation validation= defaultFileProcessor.processFile(url.getFile().substring(1),lineProcessor, StandardCharsets.UTF_8);
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(DefaultFileProcessor.EMPTY_LIST_OF_PROCESSORS_ERROR));
    }

    @Test
    public void testIncompatibleCharset(){
        URL url = DefaultFileProcessorITest.class.getClassLoader().getResource("testFileUTF8_NO_BOM.txt");
        Validation validation= defaultFileProcessor.processFile(url.getFile().substring(1), Charset.forName("UTF-32"));
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(DefaultFileProcessor.WRONG_ENCODING_ERROR));
    }

    @Test
    public void testUTF8(){
        URL url = DefaultFileProcessorITest.class.getClassLoader().getResource("testFileUTF8_NO_BOM.txt");
        LineProcessor lineProcessor = new DefaultLineProcessor();
        Validation validation= defaultFileProcessor.processFile(url.getFile().substring(1), lineProcessor, StandardCharsets.UTF_8);
        assertEquals("OK ", Validation.ValidationType.OK,validation.getType());

        Double expectedLines = 3.0;
        Double expectedWords = 33.0;
        Double avgLettersPerWord = 4.363636363636363;
        Character mostCommonLetter = 'e';

        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testISO8859_1(){
        URL url = DefaultFileProcessorITest.class.getClassLoader().getResource("testFileISO8859-1.txt");
        LineProcessor lineProcessor = new DefaultLineProcessor();
        Validation validation= defaultFileProcessor.processFile(url.getFile().substring(1), lineProcessor, StandardCharsets.ISO_8859_1);
        assertEquals("OK ", Validation.ValidationType.OK,validation.getType());

        Double expectedLines = 2.0;
        Double expectedWords = 12.0;
        Double avgLettersPerWord = 4.833333333333333;
        Character mostCommonLetter = 'a';

        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testEmptyListOfProcessors()  {
        URL url = DefaultFileProcessorITest.class.getClassLoader().getResource("testFileUTF8_NO_BOM.txt");
        Validation validation= defaultFileProcessor.processFile(url.getFile().substring(1), new ArrayList(), StandardCharsets.UTF_8);
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(DefaultFileProcessor.EMPTY_LIST_OF_PROCESSORS_ERROR));
    }

    @Test
    public void testListOfProcessorsAllNull()  {
        URL url = DefaultFileProcessorITest.class.getClassLoader().getResource("testFileUTF8_NO_BOM.txt");
        Validation validation= defaultFileProcessor.processFile(url.getFile().substring(1), Arrays.asList(null, null), StandardCharsets.UTF_8);
        assertEquals("Error ", Validation.ValidationType.ERROR,validation.getType());
        assertTrue("Error message should contain ", validation.getText().contains(DefaultFileProcessor.EMPTY_LIST_OF_PROCESSORS_ERROR));
    }

    @Test
    public void testOneNullProcessor()  {
        URL url = DefaultFileProcessorITest.class.getClassLoader().getResource("testFileUTF8_NO_BOM.txt");
        LineProcessor lineProcessor = new DefaultLineProcessor();
        Validation validation= defaultFileProcessor.processFile(url.getFile().substring(1), Arrays.asList(null, lineProcessor), StandardCharsets.UTF_8);
        assertEquals("OK ", Validation.ValidationType.OK,validation.getType());

        Double expectedLines = 3.0;
        Double expectedWords = 33.0;
        Double avgLettersPerWord = 4.363636363636363;
        Character mostCommonLetter = 'e';

        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testTwoNonNullProcessor()  {
        URL url = DefaultFileProcessorITest.class.getClassLoader().getResource("testFileUTF8_NO_BOM.txt");
        LineProcessor defaultLineProcessor = new DefaultLineProcessor();
        LineProcessor testLineProcessor = new DoNothingLineProcessor();
        Validation validation= defaultFileProcessor.processFile(url.getFile().substring(1), Arrays.asList(testLineProcessor, defaultLineProcessor),
                StandardCharsets.UTF_8);
        assertEquals("OK ", Validation.ValidationType.OK,validation.getType());

        Double expectedLines = 3.0;
        Double expectedWords = 33.0;
        Double avgLettersPerWord = 4.363636363636363;
        Character mostCommonLetter = 'e';

        Map<String,Object> statMap = defaultLineProcessor.getKVForProcessedLines();
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));

        Map<String,Object> statMapDoNothing = testLineProcessor.getKVForProcessedLines();
        assertEquals("Get expected lines ",DoNothingLineProcessor.DO_NOTHING,statMapDoNothing.get(DoNothingLineProcessor.DO_NOTHING));
    }

    @Test
    public void testWrongArgumentsToMain()  {
        System.setOut(new PrintStream(outContent));
        String[] args = null;
        String expected = DefaultFileProcessor.help+"\r\n";
        DefaultFileProcessor.main(args);
        String out = outContent.toString();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        assertEquals(expected,out);
    }

    @Test
    public void testMadeUpEncoding()  {
        System.setOut(new PrintStream(outContent));
        String[] args = {"testFileISO8859-1.txt","MyEncoding"};
        String expected = "Wrong encoding provided for file: testFileISO8859-1.txt. Make sure the provided encoding (MyEncoding) is correct. \r\n";
        DefaultFileProcessor.main(args);
        String out = outContent.toString();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        assertEquals(expected,out);
    }

    @Test
    public void testRightSingleArgumentToMain()  {
        System.setOut(new PrintStream(outContent));
        URL url = DefaultFileProcessorITest.class.getClassLoader().getResource("testFileUTF8_NO_BOM.txt");
        String[] args = {url.getFile().substring(1)};
        DefaultFileProcessor.main(args);
        String out = outContent.toString();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        assertTrue(out.contains("DefaultLineProcessor"));
        assertTrue(out.contains("Lines: 3.0"));
        assertTrue(out.contains("Most common letter: e"));
    }

    @Test
    public void testRightTwoArgumentsToMain()  {
        System.setOut(new PrintStream(outContent));
        URL url = DefaultFileProcessorITest.class.getClassLoader().getResource("testFileUTF8_NO_BOM.txt");
        String[] args = {url.getFile().substring(1), "UTF-8"};
        DefaultFileProcessor.main(args);
        String out = outContent.toString();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        assertTrue(out.contains("DefaultLineProcessor"));
        assertTrue(out.contains("Lines: 3.0"));
        assertTrue(out.contains("Most common letter: e"));
    }
}