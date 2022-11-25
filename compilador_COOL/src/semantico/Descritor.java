package semantico;

import lexico.Token;

public class Descritor {
    
    private TipoDescritor tipoDescritor;
    private String nome;
    private String tipoDeDado;
    private int linhaDeDeclaracao;
    private Token delimitadorInicialEscopo;
    private Token delimitadorFinalEscopo;

    public Descritor(TipoDescritor tipoDescritor, String nome, String tipoDeDado, int linhaDeDeclaracao){
        this.tipoDescritor = tipoDescritor;
        this.nome = nome;
        this.tipoDeDado = tipoDeDado;
        this.linhaDeDeclaracao = linhaDeDeclaracao;
    }

    public TipoDescritor getTipoDescritor(){
        return tipoDescritor;
    }

    public String getNome(){
        return nome;
    }

    public String getTipoDeDado(){
        return tipoDeDado;
    }

    public int getLinhaDeDeclaracao(){
        return linhaDeDeclaracao;
    }

    public Token getDelimitadorInicialEscopo(){
        return delimitadorInicialEscopo;
    }

    public Token getDelimitadorFinalEscopo(){
        return delimitadorFinalEscopo;
    }

    public void definirEscopo(Token delimitadorInicial, Token delimitadorFinal){
        this.delimitadorInicialEscopo = delimitadorInicial;
        this.delimitadorFinalEscopo = delimitadorFinal;
    }

    @Override
    public String toString (){
        return "Nome: " + nome + " Tipo: " + tipoDescritor.toString() + " Linha: " + linhaDeDeclaracao + " Tipo de dado: " + tipoDeDado;
    }

    public String getEscopo(){
        if(delimitadorInicialEscopo != null && delimitadorFinalEscopo != null){
            return delimitadorInicialEscopo.getConteudo() + " linha: " + delimitadorInicialEscopo.getLinha() 
            + "\n" + delimitadorFinalEscopo.getConteudo() + " linha: " + delimitadorFinalEscopo.getLinha(); 
        }
        return "Nada";
    }

}
