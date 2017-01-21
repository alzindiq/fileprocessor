package org.fileprocessor;

import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Default Implementation of the org.fileprocessor.LineProcessor interface that calculates several simple stats:
 * word count, line count, average number of letters per word (to one decimal place) and most common letter.
 */
public class DefaultLineProcessor implements LineProcessor {

    private final static Logger logger = Logger.getLogger(DefaultLineProcessor.class.getName());

    /**
     * Key for the total number of number of lines in the file.
     */
    public static final String LINES = "Lines";
    /**
     * Key for the total number of number of words in the file.
     */
    public static final String WORDS = "Words";
    /**
     * Key for average number of letters per word.
     */
    public static final String LETTER_PER_WORD = "Average letters per word";
    /**
     * Key for the most common letter processed so far. If two are equal, returns the firs one by lexicographic order.
     */
    public static final String MOST_COMMON_LETTER = "Most common letter";

    private static final List<String> metricNames = Arrays.asList(LINES,WORDS,LETTER_PER_WORD,MOST_COMMON_LETTER);

    private double lineCount = 0;
    private double wordCount = 0;
    private double totalChar = 0;
    private Map<Character,Integer> charCount = new HashMap<>();
    private Map<String,Object> statMap = new HashMap();

    @Override
    public void process(String line) {
        if(line != null && !line.isEmpty()) {
            lineCount++;
            if(logger.isTraceEnabled()){
                logger.trace("pristine input line: "+line);
            }
            line = line.trim();
            line = line.replaceAll("[^\\p{L}\\p{Nd}\\s]+", ""); // assume "words" are made up of letters and numbers only
            line = line.replaceAll("\\s+", " ");
            processWords(line);
        }
    }

    private void processWords(String line){
        if(logger.isDebugEnabled()){
            logger.debug("sanitised input line: "+line);
        }
        String[] words = line.split(" ");
        for(int i=0; i< words.length; i++){
            if(!words[i].equals(" ")) {
                totalChar += words[i].length();
                wordCount++;
                processChars(words[i].toCharArray());
            }
        }
    }

    private void processChars(char[] word) {
        for(Character c: word){
            if(charCount.get(c) == null){
                charCount.put(c,1);
            }else{
                charCount.put(c,charCount.get(c)+1);
            }
            if(logger.isTraceEnabled()){
                logger.trace("added char to map counter. Char: "+c+". Count: "+charCount.get(c));
            }
        }
    }

    @Override
    public String printProcessedLines() {
        updateStatMap();
        StringBuilder output = new StringBuilder();
        output.append(LINES+": "+statMap.get(LINES)+"\n");
        output.append(WORDS+": "+statMap.get(WORDS)+"\n");
        output.append(LETTER_PER_WORD+": " + new DecimalFormat("0.0").format(statMap.get(LETTER_PER_WORD)) + "\n");
        output.append(MOST_COMMON_LETTER+": "+statMap.get(MOST_COMMON_LETTER)+"\n");
        return output.toString();
    }

    @Override
    public Map<String, Object> getKVForProcessedLines() {
        updateStatMap();
        return new HashMap(statMap);
    }

    private void updateStatMap(){
        statMap.put(LINES,lineCount);
        statMap.put(WORDS,wordCount);

        if(wordCount != 0 ) {
            statMap.put(LETTER_PER_WORD, totalChar / wordCount);
        }else{
            statMap.put(LETTER_PER_WORD, 0.0);
        }

        if(charCount.size() ==0){
            statMap.put(MOST_COMMON_LETTER, "None");
        }else {
            Map.Entry<Character, Integer> mostCommonLetter = charCount.entrySet()
                    .stream() // sort to make output deterministic in case of same number of occurrences of several chars
                    .sorted(( Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) -> o1.getKey().compareTo(o2.getKey()) )
                    .max(
                    Map.Entry.comparingByValue(Integer::compareTo)).get();
            statMap.put(MOST_COMMON_LETTER, mostCommonLetter.getKey() );
        }
    }

    @Override
    public List<String> getKeyNames() {
        return new ArrayList(metricNames);
    }
}