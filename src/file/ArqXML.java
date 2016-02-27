package file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;

import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ArqXML{
	
	private Path path;
	private String content;
	private ArqCert arqCert;
	
	public ArqCert getArqCert() {
		return arqCert;
	}

	public void setArqCert(ArqCert arqCert) {
		this.arqCert = arqCert;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	public void setPath(String path) {		
		this.path.resolve(path) ;
	}

	public String getContent() throws IOException {
		
		if(this.getContent().equals("")){
			String linha = "";
	        StringBuilder xml = new StringBuilder();
	
	        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(this.getPath().toString()), "UTF-8"));
	        while ((linha = in.readLine()) != null) {
	            xml.append(linha);
	        }
	        in.close();
	        
	        /* Remove Acentos */
	        CharSequence cs = new StringBuilder(xml.toString() == null ? "" : xml.toString());
	        this.content = Normalizer.normalize(cs,Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	        	         
		}

        
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Document getDocument(){
		
		Document document = null;
		ArrayList<Transform> transformList = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(this.getContent().getBytes("UTF-8")));
            
            XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
           
            
            /*
             * Signature Factory
             */
            TransformParameterSpec tps = null;
            Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, tps);
            Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315",tps);

            transformList.add(envelopedTransform);
            transformList.add(c14NTransform);
            
            
            
            NodeList tagSignature = document.getDocumentElement().getElementsByTagName("Signature");
            
            if(tagSignature.getLength() > 0)
                return document;
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }   
		
	}
	
	public boolean hasSignature(){
		
		NodeList tagSignature = this.getDocument().getDocumentElement().getElementsByTagName("Signature");
        
        if(tagSignature.getLength() > 0)
            return true;
        else
            return false;
	}
	
	@Override
	public String toString(){
		
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        TransformerFactory tf = TransformerFactory.newInstance();
        
        String xml = null;
        try{
        	javax.xml.transform.Transformer trans = tf.newTransformer();
        	trans.transform(new javax.xml.transform.dom.DOMSource(this.getDocument()), new javax.xml.transform.stream.StreamResult(os));
	        xml = os.toString();
	        if ((xml != null) && (!"".equals(xml))) {
	            xml = xml.replaceAll("\\r\\n", "");
	            xml = xml.replaceAll(" standalone=\"no\"", "");
	        }
        }catch(Exception e){
        	e.printStackTrace();        	
        }
        
        return xml;
		
	}
	
	

}
