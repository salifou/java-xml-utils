package ssmm.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DocumentBuilderTest {

    @Test
    public void testDocumentShallBeValid() {
        Document d = createTestDocument();
        assertNotNull(d);
    }

    @Test
    public void defaultValuesShallBeResolved() {
        Document d = createTestDocument();
        assertNotNull(d);

        NodeList list = d.getElementsByTagName("Person");
        assertNotNull(list);
        assertEquals(2, list.getLength());

        Element johnDoe = (Element) list.item(0);
        assertEquals("User", johnDoe.getAttribute("Role"));
    }

    private Document createTestDocument() {
        try {
            InputStream xsd = getClass().getResourceAsStream("/xsd/Person.xsd");
            InputStream xml = getClass().getResourceAsStream("/xsd/People.xml");
            LSResourceResolver lsr = new LocalLSResourceResolver("/xsd");
            return DocumentBuilder.build(xml, xsd, lsr);
        } catch (Exception e) {
            fail(e.getMessage());
            return null;
        }
    }
}
