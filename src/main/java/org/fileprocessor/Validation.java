package org.fileprocessor;

/**
 * Placeholder to store the result of a file read.
 *
 * It also contains a String (text) representing the message produced by the Processor when creating the validation object.
 */
public class Validation {

    /**
     * Indicates if the processing resulted in:
     * a correct result (OK), an error (ERROR), or the process could be done but there are warnings to take a look at (HINT).
     */
    public enum ValidationType {
        OK, HINT, ERROR;
    }

    private String          text    = null;
    private ValidationType  type    = ValidationType.OK;

    public Validation(String text, ValidationType type) {
        super();
        this.text = text;
        this.type = type;
    }
    public String getText() {
        return text;
    }
    public ValidationType getType() {
        return type;
    }
}