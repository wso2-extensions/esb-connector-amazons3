package org.wso2.carbon.connector.amazons3.utils;

import org.apache.http.entity.ContentType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.StringWriter;

public class XmlUtil {
    public String convertToXml(Object source, Class... type) {
        String result = "";
        StringWriter sw = new StringWriter();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");

            JAXBContext context = JAXBContext.newInstance(type);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.marshal(source, sw);
            result = sw.toString();
        } catch (JAXBException | TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    /**
     * is the contentType an XML-type ?!
     *
     * @param contentType the contentType to check
     * @return true when its a family of an XML type
     */
    public static boolean isXmlContent(ContentType contentType) {
        return contentType.equals(ContentType.TEXT_XML) ||
                contentType.equals(ContentType.APPLICATION_ATOM_XML) ||
                contentType.equals(ContentType.APPLICATION_SVG_XML) ||
                contentType.equals(ContentType.APPLICATION_XHTML_XML) ||
                contentType.equals(ContentType.APPLICATION_XML);
    }

    /**
     * Remove the XML Prolog if the contentType is of
     *
     * @param content the content to strip the prolog from
     * @return the content without XML-prolog
     */
    public static String removeProlog(String content) {
        // Test if there is a prolog.
        if (content.startsWith("<?")) { // Yes prolog!
            // strip the prolog, where does it end?
            String prologEndStr = "?>";
            int endProlog = content.indexOf(prologEndStr);
            if (endProlog > 0) {
                // strip it off
                return content.substring(endProlog + prologEndStr.length());
            }
        }

        return content;
    }
}
