import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lexico.*;
import sintatico.Regras;


public class Principal {

	public static void main(String[] args) {
		
		Scanner entrada = new Scanner(System.in);
		String nomeArquivo = entrada.next();
		entrada.close();
		
		String codigoFonte = ManipulaArquivos.lerArquivo(nomeArquivo);
		
		List<Token> tokens = new ArrayList<Token>();
		StringBuilder cadeiaAtual = new StringBuilder();
		
		int linhaAtual = 1;
	
		for(int i = 0; i < codigoFonte.length(); i++) {
			if(codigoFonte.charAt(i) == '\n'){
				linhaAtual++;
			}
			
			else if(ElementosDaLinguagem.ehUmDelimitador(codigoFonte.charAt(i))){
				if((i + 1) < codigoFonte.length()){
					if(codigoFonte.charAt(i) == '(' && codigoFonte.charAt(i + 1) == '*'){

						boolean asteriscoEncontrado = false;
						boolean fechaParentesesEncontrado = false;
	
						if((i + 2) < codigoFonte.length()){

							for(i = i + 2 ; i < codigoFonte.length() - 1; i++){
								if('*' == codigoFonte.charAt(i)){
									asteriscoEncontrado = true;
								
									if(')' == codigoFonte.charAt(i + 1)){
										fechaParentesesEncontrado = true;
									}
					
								}

								if(asteriscoEncontrado && fechaParentesesEncontrado){
									i = i + 2;	
									break;
								}
	
							}

							if(!(asteriscoEncontrado && fechaParentesesEncontrado)){
								i = codigoFonte.length();
							}


						}
					}

					else{
						if(cadeiaAtual.length() > 0) {	
							tokens.add(new Token(cadeiaAtual.toString(), linhaAtual));
							cadeiaAtual.setLength(0);
							tokens.add(new Token(Character.toString(codigoFonte.charAt(i)), linhaAtual));
						}
		
						else{
							tokens.add(new Token(Character.toString(codigoFonte.charAt(i)), linhaAtual));
						}
					}
				}

				else if(cadeiaAtual.length() > 0) {	
					tokens.add(new Token(cadeiaAtual.toString(), linhaAtual));
					cadeiaAtual.setLength(0);
					tokens.add(new Token(Character.toString(codigoFonte.charAt(i)), linhaAtual));
				}

				else{
					tokens.add(new Token(Character.toString(codigoFonte.charAt(i)), linhaAtual));
				}
			}
			
			else if(ElementosDaLinguagem.ehUmOperador(Character.toString(codigoFonte.charAt(i)))){
				
				if(cadeiaAtual.length() > 0) {
					String possivelOperador = 
						Character.toString(cadeiaAtual.length() - 1) +
						codigoFonte.charAt(i);
					
					if(ElementosDaLinguagem.ehUmOperador(possivelOperador)) {
						tokens.add(new Token(possivelOperador, linhaAtual));
						cadeiaAtual.setLength(0);
					}
					
					else {
						tokens.add(new Token(cadeiaAtual.toString(), linhaAtual));
						cadeiaAtual.setLength(0);
						cadeiaAtual.append(codigoFonte.charAt(i));
					}
				}
				
				else {
					cadeiaAtual.append(codigoFonte.charAt(i));
				}	
			}
			
			else {
				Character simboloAtual = codigoFonte.charAt(i);

				if('\u0022' == simboloAtual){
					tokens.add(new Token(cadeiaAtual.toString(), linhaAtual));
					cadeiaAtual.setLength(0);
					cadeiaAtual.append(simboloAtual);
					i++;
					char possivelFechaAspas = codigoFonte.charAt(i);

					while((possivelFechaAspas != '\u0022') && (i < codigoFonte.length())){
						cadeiaAtual.append(codigoFonte.charAt(i));
						i++;
						
						if(i < codigoFonte.length()){
							possivelFechaAspas = codigoFonte.charAt(i);
						}

					}

					if(i < codigoFonte.length()){
						cadeiaAtual.append(codigoFonte.charAt(i));
					}
			
					tokens.add(new Token(cadeiaAtual.toString(), linhaAtual));
					cadeiaAtual.setLength(0);

				}
				
				else if(ElementosDaLinguagem.ehUmDigito(simboloAtual) || 
					ElementosDaLinguagem.ehUmCaractereDeIdentificador(simboloAtual)) {						
					cadeiaAtual.append(simboloAtual);			
				}
							
				else {
					tokens.add(new Token(cadeiaAtual.toString(), linhaAtual));
					cadeiaAtual.setLength(0);
					
					if(simboloAtual != ' ' && simboloAtual != '\t') {
						tokens.add(new Token(simboloAtual.toString(), linhaAtual));
					}				
				}
			}			
		}

		tokens.removeIf(token -> token.getConteudo().equals(""));

		//for(Token token: tokens){
			//System.out.println(token);
		//}

		Regras.associarTokens(tokens);
		boolean resultado = Regras.analisarSintaxe();

		if(resultado){
			System.out.println("Regras sintaticas OK");	
		}	
	}
}
