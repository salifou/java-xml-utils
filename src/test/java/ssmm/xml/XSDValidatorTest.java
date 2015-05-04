package ssmm.xml;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

import static org.junit.Assert.fail;

public class XSDValidatorTest {

    InputStream xsd = null;
    InputStream xml = null;
    InputStream invalidXML = null;
    LSResourceResolver lsr = new LocalLSResourceResolver("/xsd");

    @Before
    public void init() {
        xsd = getClass().getResourceAsStream("/xsd/Person.xsd");
        xml = getClass().getResourceAsStream("/xsd/People.xml");
        invalidXML = getClass().getResourceAsStream("/xsd/PeopleInvalid.xml");
    }

    @Test
    public void saxValidTest() {
        try {
            SAXSource source  = new SAXSource( new InputSource(xml) );
            SAXResult result  = new SAXResult( new DefaultHandler() );
            XSDValidator.validate(source, result, xsd, lsr);
        } catch (DocumentBuilderException|XSDValidationError e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void domValidTest() {
        try {
            Document doc = createDocument(xml);
            DOMSource source  = new DOMSource(doc);
            DOMResult result  = new DOMResult();
            XSDValidator.validate(source, result, xsd, lsr);
        } catch (DocumentBuilderException|XSDValidationError e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void streamValidTest() {
        try {
            StreamSource source = new StreamSource(xml);
            XSDValidator.validate(source, xsd, lsr);
        } catch (DocumentBuilderException|XSDValidationError e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = XSDValidationError.class)
    public void saxInvalidTest()
            throws XSDValidationError, DocumentBuilderException {

        SAXSource source  = new SAXSource( new InputSource(invalidXML) );
        SAXResult result  = new SAXResult( new DefaultHandler() );
        XSDValidator.validate(source, result, xsd, lsr);
    }

    @Test(expected = XSDValidationError.class)
    public void domInvalidTest()
            throws XSDValidationError, DocumentBuilderException {

        Document doc = createDocument(invalidXML);
        DOMSource source  = new DOMSource(doc);
        DOMResult result  = new DOMResult();
        XSDValidator.validate(source, result, xsd, lsr);
    }

    @Test(expected = XSDValidationError.class)
    public void streamInvalidTest()
            throws XSDValidationError, DocumentBuilderException {

        StreamSource source = new StreamSource(invalidXML);
        XSDValidator.validate(source, xsd, lsr);
    }

    @Test(expected = DocumentBuilderException.class)
    public void saxDocumentBuilderExceptionTest()
            throws XSDValidationError, DocumentBuilderException {

        SAXSource source  = new SAXSource( new InputSource(xml) );
        SAXResult result  = new SAXResult( new DefaultHandler() );
        XSDValidator.validate(source, result, xsd);
    }

    @Test(expected = DocumentBuilderException.class)
    public void domDocumentBuilderExceptionTest()
            throws XSDValidationError, DocumentBuilderException {

        Document doc = createDocument(xml);
        DOMSource source  = new DOMSource(doc);
        DOMResult result  = new DOMResult();
        XSDValidator.validate(source, result, xsd);
    }

    @Test(expected = DocumentBuilderException.class)
    public void streamDocumentBuilderExceptionTest()
            throws XSDValidationError, DocumentBuilderException {

        XSDValidator.validate(new StreamSource(xml), xsd);
    }

    private Document createDocument(InputStream xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            javax.xml.parsers.DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(xml);
        } catch (Exception e) {
            fail( e.getMessage() );
            return null;
        }
    }

}
