package org.fileprocessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a class used for testing the org.fileprocessor.FileProcessor with multiple LineProcessors.
 * It does nto really do any processing.
 */
public class DoNothingLineProcessor implements LineProcessor {
    /**
     * Only kew for the "metric" calculated by this main.org.fileprocessor.LineProcessor
     */
    public static final String DO_NOTHING = "NOTHING";

    private final static Map<String,Object> statMap = new HashMap();

    public DoNothingLineProcessor(){
        statMap.put(DO_NOTHING,DO_NOTHING);
    }

    @Override
    public void process(String line) {
        return;
    }

    @Override
    public String printProcessedLines() {
        return DO_NOTHING;
    }

    @Override
    public List<String> getKeyNames() {
        return Arrays.asList(DO_NOTHING);
    }

    @Override
    public Map<String, Object> getKVForProcessedLines() {
        return statMap;
    }
}
