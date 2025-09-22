package com.incede.nbfc.core.monolith.customer.utils;


import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.io.StringWriter;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.InputSource;

public final class XmlUtils {
    private XmlUtils(){}

    public static String toXml(Object obj) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(obj.getClass());
        Marshaller m = ctx.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
        StringWriter sw = new StringWriter();
        m.marshal(obj, sw);
        return sw.toString();
    }

    public static Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        return dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    public static String toString(Document doc) throws Exception {
        java.io.StringWriter sw = new java.io.StringWriter();
        jakarta.xml.bind.JAXBContext.newInstance(); // just ensure JAXB present
        javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer t = tf.newTransformer();
        t.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "no");
        t.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
        t.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "no");
        t.transform(new javax.xml.transform.dom.DOMSource(doc), new javax.xml.transform.stream.StreamResult(sw));
        return sw.toString();
    }
}

