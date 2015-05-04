package ssmm.xml;

import org.w3c.dom.Document;
import org.w3c.dom.ls.LSResourceResolver;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import java.io.InputStream;

/**
 * Provides utilities methods for creating and validating a DOM
 *
 * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
 */
public class DocumentBuilder {

    /**
     * Creates and returns the DOM
     * @param xml - The XML as stream
     * @return The DOM or throw a DocumentBuilderException
     * @throws DocumentBuilderException
     */
    public static Document build(InputStream xml) throws DocumentBuilderException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true); // This is very important
            javax.xml.parsers.DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse( xml );
        } catch (Exception e) {
            throw new DocumentBuilderException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Validates the XML and returns the DOM if no error
     * @param xml - The XML as stream
     * @param xsd - The XSD schema
     * @return The DOM or throw an:
     *         - XSDValidationException if the XML is not valid against the XSD
     *         - DocumentBuilderException for all other errors
     * @throws DocumentBuilderException
     * @throws XSDValidationError
     */
    public static Document build(
            InputStream xml,
            InputStream xsd)
            throws DocumentBuilderException, XSDValidationError {
        return build(xml, xsd, null);
    }

    /**
     * Validates the XML and returns the DOM if no error
     * @param xml - The XML as stream
     * @param xsd - The XSD schema
     * @param lsr - The resource resolver
     * @return  The DOM or throw an:
     *         - XSDValidationException if the XML is not valid against the XSD
     *         - DocumentBuilderException for all other errors
     * @throws DocumentBuilderException
     * @throws XSDValidationError
     */
    public static Document build(
            InputStream xml, InputStream xsd,
            LSResourceResolver lsr)
            throws DocumentBuilderException, XSDValidationError {

        DOMSource source = new DOMSource( build(xml) );
        DOMResult result = new DOMResult();
        XSDValidator.validate(source, result, xsd, lsr);

        return (Document) result.getNode();
    }
}
