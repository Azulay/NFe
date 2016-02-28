package file;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

public class ArqCert {
	
	private Path path;
	private String password;
	private PrivateKey privateKey;
	private KeyInfo keyInfo;
	
	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	public void setPath(String path) {		
		this.path.resolve(path) ;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void load() throws Exception{
		
		/*
         * Load Certificates
         */

        KeyStore ks = KeyStore.getInstance("pkcs12");
        String cert = this.getPath().toString();
        String certSenha = this.getPassword();
        try {
            ks.load(new FileInputStream(cert), certSenha.toCharArray());
        } catch (IOException e) {
            throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");
        }

        KeyStore.PrivateKeyEntry pkEntry = null;
        Enumeration<String> aliasesEnum = ks.aliases();
        while (aliasesEnum.hasMoreElements()) {
            String alias = (String)aliasesEnum.nextElement();
            if (ks.isKeyEntry(alias)) {
                pkEntry =
                        (KeyStore.PrivateKeyEntry)ks.getEntry(alias, new KeyStore.PasswordProtection(certSenha.toCharArray()));
                privateKey = pkEntry.getPrivateKey();
                break;
            }
        }

        X509Certificate certificado = (X509Certificate)pkEntry.getCertificate();
        
        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
        
        /*Extraido de signatureFactory */
        ArrayList<Transform> transformList = new ArrayList<Transform>();
        TransformParameterSpec tps = null;
        Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, tps);
        Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315",                                          tps);
        transformList.add(envelopedTransform);
        transformList.add(c14NTransform);
        
        KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
        List<X509Certificate> x509Content = new ArrayList<X509Certificate>();

        x509Content.add(certificado);
        X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
        keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
        
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public KeyInfo getKeyInfo() {
		return keyInfo;
	}

	public void setKeyInfo(KeyInfo keyInfo) {
		this.keyInfo = keyInfo;
	}

}
