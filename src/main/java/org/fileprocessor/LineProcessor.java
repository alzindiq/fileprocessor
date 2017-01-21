package org.fileprocessor;

import java.util.List;
import java.util.Map;

/**
 * Interface to be implemented by classes wanting to process a file and extract some metrics/stats.
 * The metrics are supposed to be named and a Key (name) - value map is created to store them.
 *
 * Before you write your own org.fileprocessor.LineProcessor, you may want to revisit some basics of text encoding and unicode in Java:
 * @see <a href="http://illegalargumentexception.blogspot.co.uk/2009/05/java-rough-guide-to-character-encoding.html">http://illegalargumentexception.blogspot.co.uk/2009/05/java-rough-guide-to-character-encoding.html</a>
 */
public interface LineProcessor {
    /**
     * Calculates statistics from the provided line and accumulates results in internal data structures.
     * This operation is not idempotent for it will accummulate
     * @param line to be processed as read from the file
     */
    void process(String line);

    /**
     * Pretty prints the desired statistics
     * @return String representing the stats calculated from all lines in a File.
     */
    String printProcessedLines();

    /**
     * Returns a list of "tags" (keys in a Map) that can be used by the developer of a class implementing this interface
     * to give a name to whatever stats/values the Processor is calculating.
     * @return keys of the metrics calculated by this line processor
     */
    List<String> getKeyNames();

    /**
     * Return a map where the key is the stat name and the value is the actual value of the calculated stat over the processed lines.
     * @return Name-value KV map of all the stats
     */
    Map<String,Object> getKVForProcessedLines();
}