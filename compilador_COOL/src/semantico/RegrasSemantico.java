package semantico;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import lexico.ElementosDaLinguagem;
import lexico.Token;

public class RegrasSemantico {
    
    private static List<Token> tokens;
    private static List<Descritor> descritores;

    private RegrasSemantico(){

    }

    private static boolean tamanhoDaListaNaoExcedido(int indice){
        return indice < tokens.size();
    }

    public static void associarTokens(List<Token> listaTokens){
        tokens = listaTokens;
    }

    public static void associarDescritores(List<Descritor> listaDescritores){
        descritores = listaDescritores;
    }

    public static List<Descritor> obterDescritores(){
        ArrayList<Descritor> descritores = new ArrayList<>();
      
        //obtendo variaveis e objetos declarados
        for(int i = 0; i < tokens.size(); i++){
            
            //obtendo variaveis e objetos declarados
            if(tokens.get(i).getConteudo().equals("let")){
                if(tamanhoDaListaNaoExcedido(i + 3)){
                    descritores.add(new Descritor(TipoDescritor.OBJETO, tokens.get(i + 1).getConteudo(), tokens.get(i + 3).getConteudo(), tokens.get(i + 1).getLinha()));
                }
            }

            //obtendo funcoes declaradas
            if(ElementosDaLinguagem.ehUmIdentificador(tokens.get(i).getConteudo()) && !ElementosDaLinguagem.ehUmaPalavraReservada(tokens.get(i).getConteudo())){
                if(tamanhoDaListaNaoExcedido(i + 4)){
                    if(tokens.get(i + 1).getConteudo().equals("(") && tokens.get(i + 2).getConteudo().equals(")")){
                        if(tokens.get(i + 3).getConteudo().equals(":")){
                            descritores.add(new Descritor(TipoDescritor.FUNCAO, tokens.get(i).getConteudo(), tokens.get(i + 4).getConteudo(), tokens.get(i).getLinha()));
                        }
                    }
                }
            }
        }
        return descritores;
    }


    public static boolean analisarTipos(){

        if(descritores != null){
            for(Descritor descritor: descritores){
                if(descritor.getTipoDescritor() == TipoDescritor.OBJETO){            
                    for(int i = 0; i < tokens.size(); i++){

                        if(tokens.get(i).getConteudo().equals(descritor.getNome())){

                            if(tamanhoDaListaNaoExcedido(i + 4)){
                                boolean teveAtribuicao = tokens.get(i + 1).getConteudo().equals(":") ||
                                    tokens.get(i + 1).getConteudo().equals("<-");

                                if(teveAtribuicao){

                                    if(temOMesmoTipo(descritor, tokens.get(i + 2))){
                                       
                                    }

                                    else if(temOMesmoTipo(descritor, tokens.get(i + 4))){
                                        
                                    }

                                    else{
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }

                else{
                    for(int i = 0; i < tokens.size(); i++){
                        if(tokens.get(i).getConteudo().equals(descritor.getNome())){
                            
                            for(int j = i; j < tokens.size(); j++){
                                if(tokens.get(j).getConteudo().equals("return")){
                                    if(tamanhoDaListaNaoExcedido(j + 1)){
                                        if(!temOMesmoTipo(descritor, tokens.get(j + 1))){
                                            return false;
                                        }
                                        
                                    }
                                }
                            }

                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static boolean analisarEscopos(){

        for(Descritor descritor: descritores){
            
            for(int i = 0; i < tokens.size(); i++){

                if(tokens.get(i).getConteudo().equals(descritor.getNome())){

                    if(tokens.get(i).getLinha() < descritor.getLinhaDeDeclaracao()){
                        return false;
                    }

                    else{
                        definirEscopo(descritor, i);
                        boolean dentroDoEscopo = 
                            tokens.get(i).getLinha() >= descritor.getDelimitadorInicialEscopo().getLinha()
                            && tokens.get(i).getLinha() <= descritor.getDelimitadorFinalEscopo().getLinha();
                        
                        if(!dentroDoEscopo){
                            return false;
                        }
                    }
                }
            }

        }          
        return true;
    }

    private static void definirEscopo(Descritor descritor, int indiceEscopo){
        Deque<String> pilhaEscopo = new ArrayDeque<>();
        Token delimitadorInicial = null;
        Token delimitadorFinal = null;
        

        //percorre a lista de tokens buscando o inicio do escopo do descritor, o primeiro abre chaves antes de sua declaracao
        for(int i = indiceEscopo; i >= 0; i--){
            if(tokens.get(i).getConteudo().equals("{")){
                indiceEscopo = i + 1;
                delimitadorInicial = new Token(tokens.get(i).getConteudo(), tokens.get(i).getLinha());
                pilhaEscopo.push("X");
                break;
            }
        }


        //percorre a lista de tokens buscando o final do escopo do descritor, o fecha chaves correspondente ao abre chaves do inicio
        while(pilhaEscopo.size() != 0  && indiceEscopo != tokens.size()){

            if(tokens.get(indiceEscopo).getConteudo().equalsIgnoreCase("{")){
                pilhaEscopo.push("X");
            }

            else if(tokens.get(indiceEscopo).getConteudo().equalsIgnoreCase("}")){
                pilhaEscopo.pop();
                delimitadorFinal = new Token(tokens.get(indiceEscopo).getConteudo(), tokens.get(indiceEscopo).getLinha());
            }

            indiceEscopo++;
        }

        descritor.definirEscopo(delimitadorInicial, delimitadorFinal);

    }

    private static boolean temOMesmoTipo(Descritor descritor, Token token){
        
        boolean tipoString = descritor.getTipoDeDado().equals("string") &&
            ElementosDaLinguagem.ehUmaStringLiteral(token.getConteudo());
        
        boolean tipoInteger = descritor.getTipoDeDado().equals("integer") &&
            ElementosDaLinguagem.ehUmValorNumericoLiteral(token.getConteudo());

        boolean tipoClasse = false;

        if(ElementosDaLinguagem.ehUmTipoDeClasse(token.getConteudo())){
            tipoClasse = descritor.getTipoDeDado().equals(token.getConteudo());
        }

        if(descritor.getTipoDescritor() == TipoDescritor.FUNCAO){
            Descritor descritorTeste;
            for(Descritor d: descritores){
                if(d.getTipoDeDado().equals(descritor.getTipoDeDado())){
                    descritorTeste = d;
                    tipoString = tipoString || descritorTeste.getTipoDeDado().equals("string");
                    tipoInteger = tipoInteger || descritorTeste.getTipoDeDado().equals("integer");
                }
            }
        }

        return tipoString || tipoInteger || tipoClasse;
    }
}
