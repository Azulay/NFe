package file;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Test;

public class NFeTest {

	@Test
	public void test_set_content_from_file() throws IOException {
		
		NFe nfe = new NFe();
		
		nfe.setContentFromFile("D:\\java\\workspace\\NFe\\src\\file\\teste_01.xml");		
		
		assertNotNull("failed - content must contain something", nfe.getContent());
		
	}

	
	@Test
	public void test_set_null_content_from_file() throws IOException {
		
		NFe nfe = new NFe();
		
		nfe.setContentFromFile("D:\\java\\workspace\\NFe\\src\\file\\teste_02.xml");		
		
		assertNull("failed - content must contain nothing", nfe.getContent());
		
	}
}
