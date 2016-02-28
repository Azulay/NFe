package util;

import java.text.Normalizer;

public class Formatador {
	
	
	
	public String removeAcentos(String str){
		
		/* Remove Acentos */
        CharSequence cs = new StringBuilder(str.toString() == null ? "" : str.toString());
        
        str = Normalizer.normalize(cs,Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        
        return str;
	}

}
