package ssmm.xml;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSResourceResolver;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class XSDValidatorTest {

    InputStream xsd = getClass().getResourceAsStream("/xsd/Person.xsd");
    InputStream xml = getClass().getResourceAsStream("/xsd/People.xml");
    LSResourceResolver lsr = new LocalLSResourceResolver("/xsd");

    /*@Before
    public void init() {
        xsd = getClass().getResourceAsStream("/xsd/Person.xsd");
        xml = getClass().getResourceAsStream("/xsd/People.xml");
        lsr = new LocalLSResourceResolver("/xsd");
    }*/

    @Test
    public void streamBasedTest() {
        StreamSource source = new StreamSource(xml);

        try {
            String result = XSDValidator.validate(source, xsd, lsr);
            assertNotNull(result);

            System.out.println("==============================================");

            System.out.println(result);
        } catch (DocumentBuilderException e) {
            fail(e.getMessage());
        } catch (XSDValidationError xsdValidationError) {
            fail(xsdValidationError.getMessage());
        }
    }

}
