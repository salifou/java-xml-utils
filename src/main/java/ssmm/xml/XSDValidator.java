package ssmm.xml;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
import java.util.List;

public class XSDValidator {

    final static String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;

    /*==========================================================================
     *****************        SAX Based validation             *****************
     =========================================================================*/
    /**
     * Validate the SAX Source against the XSD schema
     * @param source - The SAX Source document to be validated
     * @param xsd    - The XSD schema
     * @throws XSDValidationError if there are validation errors
     * @throws DocumentBuilderException
     */
    public static void validate(
            SAXSource source,
            InputStream xsd
        ) throws XSDValidationError, DocumentBuilderException {

        validate(source, null, xsd, null);
    }

    /**
     * Validate the SAX Source against the XSD schema
     * @param source - The SAX Source document to be validated
     * @param xsd    - The XSD schema
     * @param lsr    - The resource resolver
     * @throws XSDValidationError if there are validation errors
     * @throws DocumentBuilderException
     */
    public static void validate(
            SAXSource source,
            InputStream xsd,
            LSResourceResolver lsr
        ) throws XSDValidationError, DocumentBuilderException {

        validate(source, null, xsd, lsr);
    }

    /**
     * Validate the SAX Source against the XSD schema
     * @param source - The SAX Source document to be validated
     * @param result - The SAX Result
     * @param xsd    - The XSD schema
     * @throws XSDValidationError if there are validation errors
     * @throws DocumentBuilderException
     */
    public static void validate(
            SAXSource source,
            SAXResult result,
            InputStream xsd
        ) throws XSDValidationError, DocumentBuilderException {

        validate(source, result, xsd, null);
    }

    /**
     * Validate the SAX Source against the XSD schema
     * @param source - The SAX Source document to be validated
     * @param result - The SAX Result
     * @param xsd    - The XSD schema
     * @param lsr    - The resource resolver
     * @throws XSDValidationError if there are validation errors
     * @throws DocumentBuilderException
     */
    public static void validate(
            SAXSource source,
            SAXResult result,
            InputStream xsd,
            LSResourceResolver lsr
        ) throws XSDValidationError, DocumentBuilderException {

        genericValidation(source, result, xsd, lsr);
    }

    /*==========================================================================
     *****************        DOM Based validation             *****************
     =========================================================================*/

    /**
     * Validate the DOM Source against the XSD schema
     * @param source - The DOM Source
     * @param xsd    - The XSD schema
     * @throws XSDValidationError if there are validation errors
     * @throws DocumentBuilderException
     */
    public static void validate(
            DOMSource source,
            InputStream  xsd
        ) throws XSDValidationError, DocumentBuilderException {

        validate(source, null, xsd, null);
    }

    /**
     * Validate the DOM Source against the XSD schema
     * @param source - The DOM Source
     * @param xsd    - The XSD schema
     * @param lsr    - The resource resolver
     * @throws XSDValidationError if there are validation errors
     * @throws DocumentBuilderException
     */
    public static void validate(
            DOMSource source,
            InputStream  xsd,
            LSResourceResolver lsr
        ) throws XSDValidationError, DocumentBuilderException {

        validate(source, null, xsd, lsr);
    }

    /**
     * Validate the DOM Source against the XSD schema
     * @param source - The DOM Source
     * @param result - The DOM Result
     * @param xsd    - The XSD schema
     * @throws XSDValidationError if there are validation errors
     * @throws DocumentBuilderException
     */
    public static void validate(
            DOMSource source,
            DOMResult result,
            InputStream  xsd
        ) throws XSDValidationError, DocumentBuilderException {

        validate(source, result, xsd, null);
    }

    /**
     * Validate the DOM Source against the XSD schema
     * @param source - The DOM Source
     * @param result - The DOM Result
     * @param xsd    - The XSD schema
     * @param lsr    - The resource resolver
     * @throws XSDValidationError if there are validation errors
     * @throws DocumentBuilderException
     */
    public static void validate(
            DOMSource source,
            DOMResult result,
            InputStream  xsd,
            LSResourceResolver lsr
        ) throws XSDValidationError, DocumentBuilderException {

        genericValidation(source, result, xsd, lsr);
    }

    /*==========================================================================
     *****************       Stream Based validation           *****************
     =========================================================================*/

    /*
     * Note:
     *
     * Contrary to SAX and DOM Result, the Stream Result does not seems to be
     * augmented with additional information (e.g. default attribute value)
     * defined in the XSD schema.
     */

    /**
     * Validate the DOM Source against the XSD schema
     * @param source - The DOM Source
     * @param xsd    - The XSD schema
     * @throws XSDValidationError if there are validation errors
     * @throws DocumentBuilderException
     */
    public static void validate(
            StreamSource source,
            InputStream  xsd
    ) throws XSDValidationError, DocumentBuilderException {

        genericValidation(source, null, xsd, null);
    }

    /**
     * Validate the DOM Source against the XSD schema
     * @param source - The DOM Source
     * @param xsd    - The XSD schema
     * @param lsr    - The resource resolver
     * @throws XSDValidationError if there are validation errors
     * @throws DocumentBuilderException
     */
    public static void validate(
            StreamSource source,
            InputStream  xsd,
            LSResourceResolver lsr
    ) throws XSDValidationError, DocumentBuilderException {

        genericValidation(source, null, xsd, lsr);
    }

    /**
     * Validates the Document and returns the DOM if no error
     * @param source - The source document to be validated
     * @param result - The resulted document
     * @param xsd    - The XSD schema
     * @param lsr    - The resource resolver
     * @return  The Document or throw an:
     *         - XSDValidationException if the XML is not valid against the XSD
     *         - DocumentBuilderException for all other errors
     * @throws DocumentBuilderException
     * @throws XSDValidationError
     */
    private static Result genericValidation(
            Source source,
            Result result,
            InputStream xsd,
            LSResourceResolver lsr)
            throws DocumentBuilderException, XSDValidationError {

        List<String> errors;
        try {
            Validator validator = createValidator(xsd, lsr);
            validator.validate(source, result);
            errors = ((MyErrorHandler) validator.getErrorHandler()).getErrors();
        } catch (Exception e) {
            throw new DocumentBuilderException(e.getMessage(), e.getCause());
        }
        //Throw an XSDValidationError exception if there are validation errors
        if( errors != null && !errors.isEmpty() )
            throw new XSDValidationError( errors );
        return result;
    }

    /**
     * Creates and returns the schema validator
     * @param xsd - The XSD schema
     * @param lsr - The LSResourceResolver
     * @return The XSD schema validator
     * @throws org.xml.sax.SAXException
     */
    private static Validator createValidator(
            InputStream xsd, LSResourceResolver lsr) throws SAXException {

        SchemaFactory factory = SchemaFactory.newInstance(schemaLanguage);
        // Configure the resource resolver
        if( lsr != null )
            factory.setResourceResolver( lsr );
        Schema schema = factory.newSchema(new StreamSource(xsd));

        Validator validator = schema.newValidator();

        // Set the error handler
        MyErrorHandler errorHandler = new MyErrorHandler();
        validator.setErrorHandler(errorHandler);

        return validator;
    }

}
