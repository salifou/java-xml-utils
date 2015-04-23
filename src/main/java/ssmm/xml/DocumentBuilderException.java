package ssmm.xml;

/**
 * Exception raised by the document builder when a problem is found
 *
 * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
 */
public class DocumentBuilderException extends Exception {

    DocumentBuilderException(String msg){
        super(msg);
    }

    DocumentBuilderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
