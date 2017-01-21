import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DefaultLineProcessorTest {
    private LineProcessor lineProcessor;

    @Before
    public void setup(){
        lineProcessor = new DefaultLineProcessor();
    }

    @Test
    public void nullLine(){
        Double expectedLines = 0.0;
        Double expectedWords = 0.0;
        Double avgLettersPerWord = 0.0;
        String mostCommonLetter = "None";

        String expectedOutput = "Lines: "+expectedLines+"\n" +
                "Words: "+expectedWords+"\n" +
                "Average letters per word: "+new DecimalFormat("0.0").format(avgLettersPerWord)+"\n" +
                "Most common letter: "+mostCommonLetter+"\n";
        lineProcessor.process(null);
        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Get empty output ",expectedOutput,lineProcessor.printProcessedLines());
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void emptyLine(){
        Double expectedLines = 0.0;
        Double expectedWords = 0.0;
        Double avgLettersPerWord = 0.0;
        String mostCommonLetter = "None";

        String expectedOutput = "Lines: "+expectedLines+"\n" +
                "Words: "+expectedWords+"\n" +
                "Average letters per word: "+new DecimalFormat("0.0").format(avgLettersPerWord)+"\n" +
                "Most common letter: "+mostCommonLetter+"\n";
        lineProcessor.process("");
        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Empty output ",expectedOutput,lineProcessor.printProcessedLines());
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testOneLine(){
        Double expectedLines = 1.0;
        Double expectedWords = 5.0;
        Double avgLettersPerWord = 3.0;
        Character mostCommonLetter = 'i';

        String expectedOutput = "Lines: "+expectedLines+"\n" +
                "Words: "+expectedWords+"\n" +
                "Average letters per word: "+new DecimalFormat("0.0").format(avgLettersPerWord)+"\n" +
                "Most common letter: "+mostCommonLetter+"\n";
        lineProcessor.process("This is a test line");
        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Simple one line stats output ",expectedOutput,lineProcessor.printProcessedLines());
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testOneWordLine(){
        Double expectedLines = 1.0;
        Double expectedWords = 1.0;
        Double avgLettersPerWord = 57.0;
        Character mostCommonLetter = 's';

        String expectedOutput = "Lines: "+expectedLines+"\n" +
                "Words: "+expectedWords+"\n" +
                "Average letters per word: "+new DecimalFormat("0.0").format(avgLettersPerWord)+"\n" +
                "Most common letter: "+mostCommonLetter+"\n";
        lineProcessor.process("apdjpsafbasjdfbssssssadkjfhisadfosaidfh[sadfj[sadjf[sapodfjj");
        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Simple one line stats output ",expectedOutput,lineProcessor.printProcessedLines());
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testExtraBlankSpaces(){
        Double expectedLines = 1.0;
        Double expectedWords = 4.0;
        Double avgLettersPerWord = 2.75;
        Character mostCommonLetter = 's';

        String expectedOutput = "Lines: "+expectedLines+"\n" +
                "Words: "+expectedWords+"\n" +
                "Average letters per word: "+new DecimalFormat("0.0").format(avgLettersPerWord)+"\n" +
                "Most common letter: "+mostCommonLetter+"\n";
        lineProcessor.process(" this is  a test  ");
        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Get extra blank spaces out ",expectedOutput,lineProcessor.printProcessedLines());
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testPunctuation(){
        Double expectedLines = 1.0;
        Double expectedWords = 5.0;
        Double avgLettersPerWord = 3.0;
        Character mostCommonLetter = 's';

        String expectedOutput = "Lines: "+expectedLines+"\n" +
                "Words: "+expectedWords+"\n" +
                "Average letters per word: "+new DecimalFormat("0.0").format(avgLettersPerWord)+"\n" +
                "Most common letter: "+mostCommonLetter+"\n";
        lineProcessor.process(" this is)  a test . DOne !!!??? ");
        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Get punctuation marks out ",expectedOutput,lineProcessor.printProcessedLines());
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testUnicode(){
        String unicode = "Ǖ ǖ Ꞁ ¤ Ð ¢ ℥ Ω ℧ K ℶ ℷ ℸ ⅇ ⅊";
        Double expectedLines = 1.0;
        Double expectedWords = 10.0;
        Double avgLettersPerWord = 1.0;
        Character mostCommonLetter = 'K';

        String expectedOutput = "Lines: "+expectedLines+"\n" +
                "Words: "+expectedWords+"\n" +
                "Average letters per word: "+new DecimalFormat("0.0").format(avgLettersPerWord)+"\n" +
                "Most common letter: "+mostCommonLetter+"\n";
        lineProcessor.process(unicode);
        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Get unicode ",expectedOutput,lineProcessor.printProcessedLines());
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testIdeograms(){
        String ideograms = "\uF93D\uF936\uF949\uF942\uF942"; // Chinese ideograms
        Double expectedLines = 1.0;
        Double expectedWords = 1.0;
        Double avgLettersPerWord = 5.0;
        Character mostCommonLetter = '\uF942';

        String expectedOutput = "Lines: "+expectedLines+"\n" +
                "Words: "+expectedWords+"\n" +
                "Average letters per word: "+new DecimalFormat("0.0").format(avgLettersPerWord)+"\n" +
                "Most common letter: "+mostCommonLetter+"\n";
        lineProcessor.process(ideograms);
        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Get ideograms ",expectedOutput,lineProcessor.printProcessedLines());
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testNonUTF8(){
        String nonUtf = "he\uFEFFlo" ;
        Double expectedLines = 1.0;
        Double expectedWords = 1.0;
        Double avgLettersPerWord = 4.0;
        Character mostCommonLetter = 'e';

        String expectedOutput = "Lines: "+expectedLines+"\n" +
                "Words: "+expectedWords+"\n" +
                "Average letters per word: "+new DecimalFormat("0.0").format(avgLettersPerWord)+"\n" +
                "Most common letter: "+mostCommonLetter+"\n";
        lineProcessor.process(nonUtf);
        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Get non UTF8 ",expectedOutput,lineProcessor.printProcessedLines());
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testForbidden(){
        String nonUtf = "\uFFFF" ; //linebreaks that cause disagreement when used in JSON literals
        Double expectedLines = 1.0;
        Double expectedWords = 1.0;
        Double avgLettersPerWord = 0.0;
        String mostCommonLetter = "None";

        String expectedOutput = "Lines: "+expectedLines+"\n" +
                "Words: "+expectedWords+"\n" +
                "Average letters per word: "+new DecimalFormat("0.0").format(avgLettersPerWord)+"\n" +
                "Most common letter: "+mostCommonLetter+"\n";
        lineProcessor.process(nonUtf);
        Map<String,Object> statMap = lineProcessor.getKVForProcessedLines();
        assertEquals("Get forbidden ",expectedOutput,lineProcessor.printProcessedLines());
        assertEquals("Get expected lines ",expectedLines,statMap.get(DefaultLineProcessor.LINES));
        assertEquals("Get expected words ",expectedWords,statMap.get(DefaultLineProcessor.WORDS));
        assertEquals("Get expected avgLettersPerWord ",avgLettersPerWord,statMap.get(DefaultLineProcessor.LETTER_PER_WORD));
        assertEquals("Get expected mostCommonLetter ",mostCommonLetter,statMap.get(DefaultLineProcessor.MOST_COMMON_LETTER));
    }

    @Test
    public void testKeyNames(){
        assertEquals(lineProcessor.getKeyNames(), Arrays.asList(DefaultLineProcessor.LINES,DefaultLineProcessor.WORDS,
                DefaultLineProcessor.LETTER_PER_WORD,DefaultLineProcessor.MOST_COMMON_LETTER));
    }

}
