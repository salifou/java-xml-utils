package ssmm.xml;

import org.w3c.dom.Document;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

public class XSDValidator {

    final static String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;

    /**
     * Validates the source and returns the unified result if no error
     * @param source - The source to be validated
     * @param xsd    - The XSD schema
     * @return  The resulting document as a string or throw an:
     *         - XSDValidationException if the XML is not valid against the XSD
     *         - DocumentBuilderException for all other errors
     * @throws DocumentBuilderException
     * @throws XSDValidationError
     */
    public static String validate(
            StreamSource source,
            InputStream xsd)
            throws DocumentBuilderException, XSDValidationError {

        return validate(source, xsd, null);
    }

    /**
     * Validates the source and returns the unified result if no error
     * @param source - The source to be validated
     * @param xsd    - The XSD schema
     * @param lsr    - The resource resolver
     * @return  The resulting document as a string or throw an:
     *         - XSDValidationException if the XML is not valid against the XSD
     *         - DocumentBuilderException for all other errors
     * @throws DocumentBuilderException
     * @throws XSDValidationError
     */
    public static String validate(
            StreamSource source,
            InputStream xsd,
            LSResourceResolver lsr)
            throws DocumentBuilderException, XSDValidationError {

        StreamResult result = new StreamResult( new ByteArrayOutputStream());
        //StreamResult result = new StreamResult( new StringWriter() );
        validate(source, result, xsd, lsr);
        return new String( ((ByteArrayOutputStream) result.getOutputStream()).toByteArray() ); //result.getWriter().toString();
    }


    /**
     * Validates the Document and returns the DOM if no error
     * @param doc - The org.w3c.Document to be validated
     * @param xsd - The XSD schema
     * @return  The Document or throw an:
     *         - XSDValidationException if the XML is not valid against the XSD
     *         - DocumentBuilderException for all other errors
     * @throws DocumentBuilderException
     * @throws XSDValidationError
     */
    public static Document validate(Document doc, InputStream xsd)
            throws DocumentBuilderException, XSDValidationError {

        return validate(doc, xsd, null);
    }

    /**
     * Validates the Document and returns the DOM if no error
     * @param doc - The org.w3c.Document to be validated
     * @param xsd - The XSD schema
     * @param lsr - The resource resolver
     * @return  The Document or throw an:
     *         - XSDValidationException if the XML is not valid against the XSD
     *         - DocumentBuilderException for all other errors
     * @throws DocumentBuilderException
     * @throws XSDValidationError
     */
    public static Document validate(
            Document doc,
            InputStream xsd,
            LSResourceResolver lsr)
            throws DocumentBuilderException, XSDValidationError {
        try {
            //FIXME Check what happens if doc is null

            DOMSource source = new DOMSource(doc);
            DOMResult result = new DOMResult();
            validate(source, result, xsd, lsr);

            return (Document) result.getNode();

        } catch (Exception e) {
            throw new DocumentBuilderException(e.getMessage(), e.getCause());
        }
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
    private static Result validate(
            Source source,
            Result result,
            InputStream xsd,
            LSResourceResolver lsr)
            throws DocumentBuilderException, XSDValidationError {
        try {
            Validator validator = createValidator(xsd, lsr);

            //Validate the source and store the resulted document in result
            validator.validate(source, result);

            //Throw an XSDValidationError exception if there are validation errors
            MyErrorHandler eh   = ((MyErrorHandler) validator.getErrorHandler());
            List<String> errors = eh.getErrors();
            if( errors != null && !errors.isEmpty() )
                throw new XSDValidationError( errors );

            return result;
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

        Validator validator = schema.newValidator();

        // Set the error handler
        MyErrorHandler errorHandler = new MyErrorHandler();
        validator.setErrorHandler(errorHandler);

        return validator;
    }

}
