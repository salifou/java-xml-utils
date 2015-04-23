package ssmm.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom Error Handler
 *
 * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
 */
public class MyErrorHandler implements ErrorHandler {

    private List<String> errors = new ArrayList<String>();

    /**
     * @return The list of errors detected.
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        //Ignore warnings
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        errors.add(asString(e));
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        errors.add(asString(e));
    }

    private String asString(SAXParseException e) {
        int l = e.getLineNumber();
        int c = e.getColumnNumber();
        return String.format("[%d, %d] %s", l, c, e.getMessage());
    }
}
