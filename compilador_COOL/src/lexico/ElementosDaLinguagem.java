package lexico;

import java.util.Arrays;
import java.util.List;

public class ElementosDaLinguagem {
	
	private static List<Character> delimitadores = 
		Arrays.asList(';', ',', '{', '}', '(', ')', '[', ']', ':', '.', '@');
	
	private static List<String> operadores = 
		Arrays.asList("<-", "+", "-", "*", "/", ">", ">=", "<", "<=", "="); 
	
	private static List<Character> digitos = 
		Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9');
	
	private static  List<Character> caracteresDeIdentificadores =
		Arrays.asList('A', 'a', 'B', 'b', 'C', 'c', 'D', 'd', 'E', 'e', 'F', 'f', 'G', 'g',
			'H', 'h', 'I', 'i', 'J', 'j', 'K', 'k', 'L', 'l', 'M', 'm', 'N', 'n', 'O', 'o',
			'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'T', 't', 'U', 'u', 'V', 'v', 'X', 'x',
			'W', 'w', 'Y', 'y', 'Z', 'z', '_');
	
	private static List<String> palavrasReservadas =
		Arrays.asList("class", "inherits", "Int", "Main", "Object", "IO", "String", "Bool", "true",
			"false", "self", "let", "if", "else", "then", "fi", "while", "loop", "pool", "in",
			"case", "of", "esac", "new", "isvoid", "not", "integer", "string", "out_string", "Object");
	
	private static List<String> tiposDeClasses = 
		Arrays.asList("IO", "Object", "Int", "String", "Bool", "Main");
	
	private ElementosDaLinguagem() {
		
	}
	
	public static boolean ehUmDelimitador(Character conteudo) {
		return delimitadores.contains(conteudo);
	}
	
	public static boolean ehUmDelimitador(String conteudo) {
		if(conteudo.length() > 0){
			return delimitadores.contains(conteudo.charAt(0));
		}
		return false;
	}
	
	public static boolean ehUmOperador(String conteudo) {
		return operadores.contains(conteudo);
	}
	
	public static boolean ehUmDigito(Character conteudo) {
		return digitos.contains(conteudo);
	}
	
	public static boolean ehUmCaractereDeIdentificador(Character conteudo) {
		return caracteresDeIdentificadores.contains(conteudo);
	}
	
	public static boolean ehUmaPalavraReservada(String conteudo) {
		return palavrasReservadas.contains(conteudo);
	}
	
	public static boolean ehUmValorNumericoLiteral(String conteudo) {
		
		int contador = 0;
		
		for(int i = 0; i < conteudo.length(); i++) {
			
			if(ehUmDigito(conteudo.charAt(i))) {
				contador++;
			}
		}
		
		return contador == conteudo.length() && contador > 0;
			
		
	}
	
	public static boolean ehUmIdentificador(String conteudo) {
		int contador = 0;

		for(int i = 0; i < conteudo.length(); i++) {
			if(ehUmCaractereDeIdentificador(conteudo.charAt(i))) {
				contador++;
			}
		}
		
		return contador == conteudo.length() && contador > 0;
		
	}
	
	public static boolean ehUmaStringLiteral(String conteudo){

		for(int i = 0; i < conteudo.length(); i++){
			if(conteudo.charAt(i) == '\u0022'){
				return true;
			}
		}

		return false;
		
	}	

	public static boolean ehUmTipoDeClasse(String conteudo){
		return tiposDeClasses.contains(conteudo);
	}
}
