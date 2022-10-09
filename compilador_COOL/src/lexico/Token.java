package lexico;

public class Token {
	
	private String conteudo;
	private int linha;
	private Categoria categoria;
	
	public Token(String conteudo, int linha) {
		
		this.conteudo = conteudo;
		this.linha = linha;
		definirCategoria();
	}

	public String getConteudo(){
		return conteudo;
	}

	public int getLinha(){
		return linha;
	}
	
	private void definirCategoria() {
		if(ElementosDaLinguagem.ehUmDelimitador(conteudo)) {
			categoria = Categoria.DELIMITADOR;
		}
		
		else if(ElementosDaLinguagem.ehUmOperador(conteudo)) {
			categoria = Categoria.OPERADOR;
		}
		
		else if(ElementosDaLinguagem.ehUmaPalavraReservada(conteudo)) {
			categoria = Categoria.PALAVRA_RESERVADA;
		}
		
		else if(ElementosDaLinguagem.ehUmValorNumericoLiteral(conteudo)) {
			categoria = Categoria.VALOR_NUMERICO_LITERAL;
		}
		
		else if(ElementosDaLinguagem.ehUmIdentificador(conteudo)) {
			categoria = Categoria.IDENTIFICADOR;
		}
		
		else if(ElementosDaLinguagem.ehUmaStringLiteral(conteudo)) {
			categoria = Categoria.STRING_LITERAL;
		}
		
		else {
			categoria = Categoria.SIMBOLO_INVALIDO;
		}
	}
	
	@Override
	public String toString() {
		return conteudo + " -> " + categoria.toString() + " Linha: " + linha;
	}
	
}
