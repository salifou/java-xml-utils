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
        super(errorsAsString(errors));
        this.errors = errors;
    }

    /**
     * @return The list of errors
     */
    public List<String> getErrors() {
        return errors;
    }

    private static String errorsAsString(List<String> errors) {
        if( errors == null ) return "";

        StringBuilder sb = new StringBuilder("XSD Schema validation errors: ");
        for( String s: errors)
            sb = sb.append(s).append("\n");
        return sb.toString();
    }

}
