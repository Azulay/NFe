package signature;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import file.ArqXML;
import file.NFe;

public class AssinadorTest {

	NFe nfe;
	Assinador assinador;
	
	@Before
	public void setUp() throws IOException{
		nfe = new NFe();
		nfe.setContentFromFile("D:\\java\\workspace\\NFe\\src\\file\\teste_01.xml");
		
		assinador = new Assinador();
	}
	
	@Test
	public void test_assina_returns_true(){		
		assertEquals("failed - assina method must return true",true,assinador.assina(nfe));
	}
	
	@Test
	public void test_nfe_contains_signature_tag(){
		assertTrue("failed - nfe object must contains signature tag",nfe.getContent().contains("<signature>"));
	}

}
