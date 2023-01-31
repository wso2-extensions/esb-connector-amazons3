package org.wso2.carbon.connector.amazons3.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.XMLConstants;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.marshaller.DataWriter;

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

    public String convertToXml(Object source, String encoding, Class... type) {
        String result = "";
        StringWriter sw = new StringWriter();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");

            JAXBContext context = JAXBContext.newInstance(type);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            // The below code will take care of avoiding the conversion of < to &lt; and > to &gt; etc
            PrintWriter printWriter = new PrintWriter(sw);
            DataWriter dataWriter = new DataWriter(printWriter, encoding, new JaxbCharacterEscapeHandler());

            marshaller.marshal(source, dataWriter);
            result = sw.toString();
        } catch (JAXBException | TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    static class JaxbCharacterEscapeHandler implements CharacterEscapeHandler {
        @Override
        public void escape(char[] buf, int start, int len, boolean isAttValue, Writer out) throws IOException {
            out.write(buf,start,len);

        }
    }
}
