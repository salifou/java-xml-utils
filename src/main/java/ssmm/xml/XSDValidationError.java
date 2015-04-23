package ssmm.xml;

import java.util.List;

/**
 * Exception raised when XSD schema validation fails.
 *
 * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
 */
public class XSDValidationError extends Exception {

    private List<String> errors;

    public XSDValidationError(List<String> errors) {
        this.errors = errors;
    }

    /**
     * @return The list of errors
     */
    public List<String> getErrors() {
        return errors;
    }
}
