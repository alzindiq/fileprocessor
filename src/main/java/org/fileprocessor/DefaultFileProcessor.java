package org.fileprocessor;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation for files that do not require interactive access to intermediate results of the processing.
 * It implements a well defined interface for basic file processing @Link FileProcessor.
 */

public class DefaultFileProcessor implements FileProcessor{

    private final static Logger logger = Logger.getLogger(DefaultFileProcessor.class.getName());

    private List<LineProcessor> processors = null;

    /**
     * Error message for file processing errors.
     */
    public final static String FILE_PROCESSING_ERROR = "Couldn't process file";
    /**
     * Error message for non-found files.
     */
    public final static String FILE_LOCATION_ERROR = "Couldn't find file";
    /**
     * Error message for undefined lineprocessors.
     */
    public final static String EMPTY_LIST_OF_PROCESSORS_ERROR = "Need to specify a non-null line processor or a non-empty " +
            "list of Line processors with at least one non-null processor";

    /**
     * Help message to be displayed on wrong arguments to main
     */
    public final static String help ="FileName is mandatory. \n*******************\nUsage: java main.org.fileprocessor.DefaultFileProcessor <filename> \n" +
            "You can also provide a valid encoding as an argument. Usage: java org.fileprocessor.DefaultFileProcessor <filename> <charset> \n" +
            "For valid encodings, visit: https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html";

    /**
     * Error message for wrong encoding.
     */
    public final static String WRONG_ENCODING_ERROR = "Wrong encoding provided for file";

    @Override
    public Validation processFile(String fileName, String charsetString){
        if(charsetString == null || charsetString.isEmpty()){
            System.out.println(WRONG_ENCODING_ERROR+": "+fileName+". Make sure you provide a valid encoding." );
            logger.info(WRONG_ENCODING_ERROR+": "+fileName+". because of null/empty encoding.");
            return new Validation(WRONG_ENCODING_ERROR+": "+fileName+" because of null/empty encoding \n",
                    Validation.ValidationType.ERROR);
        }

        try{
            return processFile(fileName, new DefaultLineProcessor(), Charset.forName(charsetString));
        } catch (java.nio.charset.UnsupportedCharsetException uce){
            System.out.println(WRONG_ENCODING_ERROR+": "+fileName+". Make sure the provided encoding ("+charsetString+") is correct. " );
            logger.info(WRONG_ENCODING_ERROR+": "+fileName+" because of exception: \n "+uce.getMessage());
            return new Validation(WRONG_ENCODING_ERROR+": "+fileName+" because of exception: \n "+uce.getMessage(),
                    Validation.ValidationType.ERROR);
        }
    }

    @Override
    public Validation processFile(String fileName, Charset charset){
        return processFile(fileName, new DefaultLineProcessor(), charset);
    }

    @Override
    public Validation processFile(String fileName){
        return processFile(fileName, new DefaultLineProcessor(), null);
    }

    @Override
    public Validation processFile(String fileName, LineProcessor processor , Charset charset){
        return processFile(fileName, Arrays.asList(processor), charset);
    }

    @Override
    public Validation processFile(String fileName, List<LineProcessor> processors , Charset charset){
        if(fileName == null){
            System.out.println(FILE_LOCATION_ERROR+": "+fileName+".\n Empty file names are not accepted. Please make sure it exists in specified path.");
            logger.info(FILE_LOCATION_ERROR+": "+fileName+".\n Empty file names are not accepted. Please make sure it exists in specified path.");
            return new Validation(FILE_LOCATION_ERROR+": "+fileName+".\n Empty file names are not accepted. Please make sure it exists in specified path.",
                    Validation.ValidationType.ERROR);
        }

        if(fileName.isEmpty()){
            System.out.println(FILE_LOCATION_ERROR+": <empty name>.\n Empty file names are not accepted. Please make sure it exists in specified path.");
            logger.info(FILE_LOCATION_ERROR+": <empty name>.\n Empty file names are not accepted. Please make sure it exists in specified path.");
            return new Validation(FILE_LOCATION_ERROR+": <empty name>.\n Empty file names are not accepted. Please make sure it exists in specified path.",
                    Validation.ValidationType.ERROR);
        }

        if(processors==null || processors.isEmpty() || !processors.stream().anyMatch(p -> p!= null)) {
            System.out.println(EMPTY_LIST_OF_PROCESSORS_ERROR + ".");
            logger.info(EMPTY_LIST_OF_PROCESSORS_ERROR + ".");
            return new Validation(EMPTY_LIST_OF_PROCESSORS_ERROR + ".",
                    Validation.ValidationType.ERROR);
        }

        File file = new File(fileName);
        if(!file.exists() || file.isDirectory()) {
            System.out.println(FILE_LOCATION_ERROR+": "+fileName+".");
            logger.info(FILE_LOCATION_ERROR+": "+fileName+". \n Please make sure it exists in specified path and it is not a directory.");
            return new Validation(FILE_LOCATION_ERROR+": "+fileName+". \n Please make sure it exists in specified path and it is not a directory.", Validation.ValidationType.ERROR);
        }

        this.processors = processors;

        try (Stream<String> lines = Files.lines(Paths.get(fileName), charset == null? Charset.defaultCharset() : charset)) {
            lines.forEachOrdered(line -> {
                processors.stream().filter(processor -> processor != null).forEach(processor -> processor.process(line));
            });
        } catch (UncheckedIOException wrongEncoding){
            System.out.println(WRONG_ENCODING_ERROR+": "+fileName+". Make sure the provided encoding ("+charset.name()+") is correct. " );
            logger.info(WRONG_ENCODING_ERROR+": "+fileName+" because of exception: \n "+wrongEncoding.getMessage());
            return new Validation(WRONG_ENCODING_ERROR+": "+fileName+" because of exception: \n "+wrongEncoding.getMessage(),
                    Validation.ValidationType.ERROR);
        } catch (IOException e) {
            System.out.println(FILE_PROCESSING_ERROR+": "+fileName+".");
            logger.info(FILE_PROCESSING_ERROR+": "+fileName+" because of exception: \n "+e.getMessage());
            return new Validation(FILE_PROCESSING_ERROR+": "+fileName+" because of exception: \n "+e.getMessage(),
                    Validation.ValidationType.ERROR);
        }
        // at least one of them needs to be non-null
        processors.stream().filter(processor -> processor != null).forEach(processor -> {
            System.out.println("**********************");
            System.out.println("Output for processor: " + processor.getClass().getName());
            System.out.println("**********************");
            logger.info("Output for processor: " + processor.getClass().getName());
            System.out.println(processor.printProcessedLines());
            logger.info(processor.printProcessedLines());
        });
        return new Validation("OK", Validation.ValidationType.OK);
    }

    @Override
    public List<String> getKeyNames() {
        return this.processors == null ? new ArrayList<>(0) : this.processors.stream().
                filter(p -> p != null).map(p -> p.getKeyNames()).flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getKVForProcessedLines() {
        return this.processors == null ? new HashMap() : this.processors.stream().
                filter(p -> p != null).map(p -> p.getKVForProcessedLines()).flatMap (map -> map.entrySet().stream()).
                collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public static void main(String[] args) {
        if(args == null || args.length == 0 || args.length > 2) {
            System.out.println(help);
        }else {
            DefaultFileProcessor defaultFileProcessor = new DefaultFileProcessor();
            if (args.length == 1) {
                defaultFileProcessor.processFile(args[0]);
            } else {
                if (args.length == 2) {
                    defaultFileProcessor.processFile(args[0], args[1]);
                }
            }
        }
        return;
    }
}