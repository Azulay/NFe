package signature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.sound.midi.SysexMessage;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import file.ArqCert;
import file.ArqXML;
import file.Certificado;
import file.NFe;

public class Assinador {
	
	
	private NFe nfe;
	
	
	
	public boolean assina(NFe nfe){
		
		this.nfe = nfe;
		
		Certificado certificadoCliente = new Certificado();
		certificadoCliente.setPath("D:\\java\\bssv\\cert\\lca_alim.pfx");
		certificadoCliente.setPassword("LCA889412");
		
		//Certificado certificadoCacerts = new Certificado();
		
		String caminhoDoCertificadoDoCliente = certificadoCliente.getPath();  
        String senhaDoCertificado = certificadoCliente.getPassword();  
        
        //String arquivoCacertsGeradoTodosOsEstados = certificadoCacerts.getPath();  

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        Reference ref = null;
        SignedInfo si = null;
        KeyStore.PrivateKeyEntry keyEntry = null;
        KeyInfo ki = null;
        try {
				ref = fac.newReference
					 ("", fac.newDigestMethod(DigestMethod.SHA1, null),
					  Collections.singletonList
					   (fac.newTransform
					    (Transform.ENVELOPED, (TransformParameterSpec) null)),
					     null, null);
			
				si = fac.newSignedInfo
	        		 (fac.newCanonicalizationMethod
	        		  (CanonicalizationMethod.INCLUSIVE,
	        		   (C14NMethodParameterSpec) null),
	        		    fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
	        		     Collections.singletonList(ref));
				
				KeyStore ks = KeyStore.getInstance("pkcs12");
				ks.load(new FileInputStream(caminhoDoCertificadoDoCliente), senhaDoCertificado.toCharArray());
				
				/*keyEntry =
				    (KeyStore.PrivateKeyEntry) ks.getEntry
				        ("mykey", new KeyStore.PasswordProtection(senhaDoCertificado.toCharArray()));
				*/
				//KeyStore.PrivateKeyEntry keyEntry = null;  
		        Enumeration<String> aliasesEnum = ks.aliases();  
		        while (aliasesEnum.hasMoreElements()) {  
		            String alias = (String) aliasesEnum.nextElement();  
		            if (ks.isKeyEntry(alias)) {  
		            	keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,  
		                        new KeyStore.PasswordProtection(senhaDoCertificado.toCharArray()));  
		                //privateKey = pkEntry.getPrivateKey();  
		                break;  
		            }  
		        } 
				
				X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
				
				KeyInfoFactory kif = fac.getKeyInfoFactory();
				List x509Content = new ArrayList();
				x509Content.add(cert.getSubjectX500Principal().getName());
				x509Content.add(cert);
				X509Data xd = kif.newX509Data(x509Content);
				ki = kif.newKeyInfo(Collections.singletonList(xd));

		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
     // Instantiate the document to be signed.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = null;
		try {
			doc = dbf.newDocumentBuilder().parse
			    (new FileInputStream(nfe.getPath()));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}

        // Create a DOMSignContext and specify the RSA PrivateKey and
        // location of the resulting XMLSignature's parent element.
        DOMSignContext dsc = new DOMSignContext
            (keyEntry.getPrivateKey(), doc.getDocumentElement());

        // Create the XMLSignature, but don't sign it yet.
        XMLSignature signature = fac.newXMLSignature(si, ki);

        // Marshal, generate, and sign the enveloped signature.
        try {
			signature.sign(dsc);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (XMLSignatureException e) {
			e.printStackTrace();
		}
        
        
     // Output the resulting document.
        OutputStream os;
		try {
			os = new FileOutputStream("result.xml");
			TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer trans = tf.newTransformer();
		    trans.transform(new DOMSource(doc), new StreamResult(os));
		    
		    System.out.println(doc.getTextContent());
		    
		    nfe.setContent(doc.getTextContent());
		    
		} catch (FileNotFoundException | TransformerException e) {
			e.printStackTrace();
		}
       

        
		
		return true;
	}
	
	
	
	

}
