package file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.security.KeyStore;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


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
		
		Document document;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(this.getContent().getBytes("UTF-8")));
            
            XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
            ArrayList<Transform> transformList;
            
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
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }   
		
	}
	
	

}
