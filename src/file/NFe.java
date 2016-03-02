package file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.Normalizer;

import org.w3c.dom.Document;

public class NFe{
	
	private String path;
	private String fileName;
	private String content;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent(){		
		return this.content;
	}
	
	public String setContentFromFile(String filePath) throws IOException {
		
		if(this.getContent() == null){
			
			String linha = "";
	        StringBuilder xml = new StringBuilder();
	
	        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
	        while ((linha = in.readLine()) != null) {
	            xml.append(linha);
	        }
	        in.close(); 
	        
	        if(!xml.toString().equals(""))
	        	this.setContent(xml.toString());
	        
	        this.path = filePath;
	        
		}
		
		
        
		return content;
	}

	

}
