package ssmm.xml;

import org.w3c.dom.Document;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
import java.util.List;

/**
 * Provides utilities methods for creating and validating a DOM
 *
 * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
 */
public class DocumentBuilder {

    final static String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;

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
        try {
            Validator validator = createValidator(xsd, lsr);

            // Set the error handler
            MyErrorHandler errorHandler = new MyErrorHandler();
            validator.setErrorHandler(errorHandler);

            // Create the DOM
            DOMSource source = new DOMSource( build(xml) );
            DOMResult result = new DOMResult();
            validator.validate(source, result);

            // Return the unified DOM or the list of errors
            List<String> errors = errorHandler.getErrors();
            if( errors == null || errors.isEmpty() )
                return (Document) result.getNode();
            else
                throw new XSDValidationError( errors );
        } catch (Exception e) {
            throw new DocumentBuilderException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Creates and returns the schema validator
     * @param xsd - The XSD schema
     * @param lsr - The LSResourceResolver
     * @return The schema validator
     * @throws org.xml.sax.SAXException
     */
    private static Validator createValidator(
            InputStream xsd, LSResourceResolver lsr) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(schemaLanguage);
        // Configure the resource resolver
        if( lsr != null )
           factory.setResourceResolver( lsr );
        Schema schema = factory.newSchema(new StreamSource(xsd));
        return schema.newValidator();
    }
}
