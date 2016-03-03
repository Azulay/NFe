package signature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import file.Certificado;
import file.NFe;

public class AssinadorTest {

	NFe nfe;
	Assinador assinador;
	Certificado certificadoCliente;
	
	@Before
	public void setUp() throws IOException{
		nfe = new NFe();
		nfe.setContentFromFile("D:\\dev\\resources\\xml\\NFE_LCA_001.xml");
		
		certificadoCliente = new Certificado();
		certificadoCliente.setPath("D:\\dev\\resources\\cert\\lca_alim.pfx");
		certificadoCliente.setPassword("LCA889412");
		
		assinador = new Assinador();
	}
	
	@Test
	public void test_assina_returns_true(){		
		assertEquals("failed - assina method must return true",true,assinador.assina(nfe,certificadoCliente));
	}
	
	@Test
	public void test_nfe_contains_signature_tag(){
		assertTrue("failed - nfe object must contains signature tag",nfe.getContent().contains("</Signature>"));
	}

}
