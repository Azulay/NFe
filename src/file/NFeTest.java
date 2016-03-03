package file;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

public class NFeTest {

	NFe nfeLCA = null;
	NFe nfeNull = null;
	
	@Before
	public void setUp() throws IOException{
		this.nfeLCA = new NFe();
		
		this.nfeLCA.setContentFromFile("D:\\dev\\resources\\xml\\NFE_LCA_001.xml");
		
		this.nfeNull = new NFe();
		
		this.nfeNull.setContentFromFile("D:\\dev\\resources\\xml\\xml_nulo.xml");	
	}
	
	@Test
	public void test_set_content_from_file() throws IOException {		
		assertNotNull("failed - content must contain something", nfeLCA.getContent());		
	}

	
	@Test
	public void test_set_null_content_from_file() throws IOException {
		assertNull("failed - content must contain nothing", this.nfeNull.getContent());
	}
}
