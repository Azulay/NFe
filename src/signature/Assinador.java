package signature;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import file.ArqCert;
import file.ArqXML;

public class Assinador {
	
	private static final String INFINUT = "infInut";
    private static final String INFCANC = "infCanc";
    private static final String NFE = "NFe";
	
    private XMLSignatureFactory signatureFactory;
	private ArrayList<Transform> transformList;
	
	private ArqXML arqXml;
	private ArqXML arqXmlResult;
	public ArqXML getArqXml() {
		return arqXml;
	}


	public void setArqXml(ArqXML arqXml) {
		this.arqXml = arqXml;
	}


	public ArqXML getArqXmlResult() {
		return arqXmlResult;
	}


	public void setArqXmlResult(ArqXML arqXmlResult) {
		this.arqXmlResult = arqXmlResult;
	}


	public ArqCert getArqCert() {
		return arqCert;
	}


	public void setArqCert(ArqCert arqCert) {
		this.arqCert = arqCert;
	}

	private ArqCert arqCert;
	
	public boolean assina(ArqXML arqXml, ArqCert arqCert) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

	        
		 this.arqXmlResult = new ArqXML();
         this.arqXmlResult.setPath(arqXml.getPath());
         
         this.arqXml = arqXml;
         this.arqCert = arqCert;
         
         if (!arqXml.hasSignature()){
        	 this.getArqXmlResult().setContent(assina(this.getArqXml()));
         }
		
         
         return true;        		            

	 	                       
	        
	}
	 
	 
	 	public String assina(ArqXML arqXml) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{
	 	     
	 		ArqXML arqXmlParaAssinar = arqXml;
	 		
	 		for (int i = 0;
	 	             i < arqXmlParaAssinar.getDocument().getDocumentElement().getElementsByTagName(NFE).getLength();
	 	             i++) {
	 	            
		 		
		 		 NodeList elements = arqXmlParaAssinar.getDocument().getElementsByTagName("infNFe");
		 		
		         org.w3c.dom.Element el = (org.w3c.dom.Element)elements.item(i);
	
		         String id = el.getAttribute("Id");
		         el.setIdAttribute("Id", true);
	
		         XMLSignatureFactory fac = this.getXMLSignatureFactory();
		         ArrayList<Transform> transformList = this.getTransformList();
		         
		         Reference ref = 
		        		 fac.newReference("#" + id, fac.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
	
		         SignedInfo si = 
		        		 fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
		                                                             (C14NMethodParameterSpec)null),
		                               fac.newSignatureMethod(SignatureMethod.RSA_SHA1,
		                                                      null),
		                               Collections.singletonList(ref));
	
		         XMLSignature signature = fac.newXMLSignature(si,this.getArqCert().getKeyInfo() );
	
		         DOMSignContext dsc = new DOMSignContext(this.getArqCert().getPrivateKey(),arqXmlParaAssinar.getDocument().getDocumentElement().getElementsByTagName(NFE).item(i));
	
		         
		         Node no = arqXmlParaAssinar.getDocument().getDocumentElement().getElementsByTagName(NFE).item(i).getParentNode();
		 		
		         try {
		             signature.sign(dsc);
		         } catch (Exception ex) {
		             ex.printStackTrace();
		         }
	         
	 		}	
	 		

	        return arqXmlParaAssinar.toString();	 		
	 		
	 	}
	 	
	 	
	 	XMLSignatureFactory getXMLSignatureFactory() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{
	 		
	 		if(this.signatureFactory == null){
	 			
	 			XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
	 			ArrayList<Transform> transformList = new ArrayList<Transform>();
	 			
	            TransformParameterSpec tps = null;
	            Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, tps);
	            Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315",tps);

	            transformList.add(envelopedTransform);
	            transformList.add(c14NTransform);
	            
	            this.transformList = transformList;
	 		}
	 		
	 		return this.signatureFactory;
	 		
	 	}
	 		
	 	ArrayList<Transform> getTransformList(){
	 		
	 		return this.transformList;
	 		
	 	}
	 	

}
