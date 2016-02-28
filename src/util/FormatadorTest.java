package util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import util.Formatador;

public class FormatadorTest {

	
	
/* Testa a remoção de acentos */
	
	@Test
	public void test_remove_acentos_string() throws IOException {
		
		Formatador formatador = new Formatador();
		
		String input = "Desenvolver é fácil";
		

		String expected = "Desenvolver e facil";
		
		assertEquals("falha - deveria retornar " + expected,expected,formatador.removeAcentos(input));
	}
	
	@Test
	public void test_remove_acentos_string_2() throws IOException {
		
		Formatador formatador = new Formatador();
		
		String input = "Estou caçando bugs";
		

		String expected = "Estou cacando bugs";
		
		assertEquals("falha - deveria retornar " + expected,expected,formatador.removeAcentos(input));
	}
	
	@Test
	public void test_remove_acentos_string_3() throws IOException {
		
		Formatador formatador = new Formatador();
		
		String input = "DESENVOLVER É FÁCIL, MAS PRECISO IR À CAÇA DOS BUGS";
		

		String expected = "DESENVOLVER E FACIL, MAS PRECISO IR A CACA DOS BUGS";
		
		assertEquals("falha - deveria retornar " + expected,expected,formatador.removeAcentos(input));
	}

}
