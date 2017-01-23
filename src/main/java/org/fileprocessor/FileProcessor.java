package org.fileprocessor;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Basic scaffolding that simply breaks text in lines and invokes @see org.fileprocessor.LineProcessor#process().
 * It relies on user-provided @link org.fileprocessor.LineProcessor for implementing the desired line analysis
 */
public interface FileProcessor {

    /**
     * This method is ultimately used by all other overloaded methods in this interface.
     * @param fileName URL of the file to be read.
     * @param processors List of line processors to be applied to each line in the file. @link org.fileprocessor.DefaultLineProcessor is loaded of null.
     * @param charset Encoding of the file. Default OS encoding is loaded if null.
     * @return org.fileprocessor.Validation object containing result of processing and descriptive message.
     */
    Validation processFile(String fileName, List<LineProcessor> processors , Charset charset);

    /**
     * Invokes @see FileProcessor#processFile above by populating processors List with the org.fileprocessor.DefaultLineProcessor
     * @param fileName URL of the file to be read.
     * @param charsetString String representing a valid JVM encoding for the file. Default OS encoding is loaded if null.
     * @return org.fileprocessor.Validation object containing result of processing and descriptive message.
     */
    Validation processFile(String fileName, String charsetString);

    /**
     * Invokes @see FileProcessor#processFile above by populating processors List with the org.fileprocessor.DefaultLineProcessor
     * @param fileName URL of the file to be read.
     * @param charset Encoding of the file. Default OS encoding is loaded if null.
     * @return org.fileprocessor.Validation object containing result of processing and descriptive message.
     */
    Validation processFile(String fileName, Charset charset);

    /**
     * Invokes @see FileProcessor#processFile above by populating processors List with the org.fileprocessor.DefaultLineProcessor.
     * It also assumes the local default (OS) encoding to handle the file.
     * @param fileName URL of the file to be read.
     * @return org.fileprocessor.Validation object containing result of processing and descriptive message.
     */
    Validation processFile(String fileName);

    /**
     * Invokes @see FileProcessor#processFile above by populating processors List with the provided @link org.fileprocessor.LineProcessor.
     * @param fileName URL of the file to be read.
     * @param processor LineProcessor to be applied to each line in the file.
     * @param charset Encoding of the file. Default OS encoding is loaded if null.
     * @return org.fileprocessor.Validation object containing result of processing and descriptive message.
     */
    Validation processFile(String fileName, LineProcessor processor, Charset charset);
}