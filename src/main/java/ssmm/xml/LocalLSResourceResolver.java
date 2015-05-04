package ssmm.xml;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

/**
 * Resolve included/imported XSD schemas.
 *
 * Notes:
 *   1) The schemas are expected to be in the specified folder in the class path
 *   2) The XSD schemas schemas SHALL be in the same folder
 *
 * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
 */
public class LocalLSResourceResolver implements LSResourceResolver {

    private String folder = null;

    LocalLSResourceResolver(String folder) {
        this.folder = folder + File.separator;
    }

    @Override
    public LSInput resolveResource(
            String type,
            String namespaceURI,
            String publicId,
            String systemId,
            String baseURI) {

        LSInput input  = new MyLSInput();
        InputStream is = this.getClass().getResourceAsStream(folder + systemId);
        input.setByteStream(is);
        return input;
    }

    static class MyLSInput implements LSInput {

        private InputStream is = null;

        @Override
        public Reader getCharacterStream() { return null; }

        @Override
        public void setCharacterStream(Reader reader) { }

        @Override
        public InputStream getByteStream() { return is; }

        @Override
        public void setByteStream(InputStream is) { this.is = is ;}

        @Override
        public String getStringData() { return null; }

        @Override
        public void setStringData(String s) { }

        @Override
        public String getSystemId() { return null; }

        @Override
        public void setSystemId(String s) { }

        @Override
        public String getPublicId() { return null; }

        @Override
        public void setPublicId(String s) { }

        @Override
        public String getBaseURI() { return null; }

        @Override
        public void setBaseURI(String s) { }

        @Override
        public String getEncoding() { return null; }

        @Override
        public void setEncoding(String s) { }

        @Override
        public boolean getCertifiedText() { return false; }

        @Override
        public void setCertifiedText(boolean b) {}
    }
}
