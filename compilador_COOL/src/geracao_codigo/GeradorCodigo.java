package geracao_codigo;

import java.util.List;

import lexico.Categoria;
import lexico.Token;

import java.util.ArrayList;


public class GeradorCodigo {

    private static List<Token> tokens = new ArrayList<>();
    private static Codigo codigo = new Codigo();
    

    private GeradorCodigo(){

    }

    public static void associarTokens(List<Token> listaDeTokens){
        tokens = listaDeTokens;
    }

    private static boolean tamanhoDaListaNaoExcedido(int indice){
        return indice < tokens.size();
    }

    public static void gerarCodigo() {

        for(int i = 0; i < tokens.size(); i++){

            if(tokens.get(i).getConteudo().equals("inherits")){
                codigo.adicionarCodigo("extends");
            }

            else if(tokens.get(i).getConteudo().equals("Main")){
                codigo.adicionarCodigo("Principal");
            }

            else if(tokens.get(i).getConteudo().equals("Class")){
                codigo.adicionarCodigo("class");
            }

            else if(tokens.get(i).getConteudo().equals("Int")){
                codigo.adicionarCodigo("int");
            }

            else if(tokens.get(i).getConteudo().equals("self")){
                codigo.adicionarCodigo("return this");
            }

            else if(tokens.get(i).getConteudo().equals("self")){
                codigo.adicionarCodigo("return this");
            } 
            
            else if(tokens.get(i).getConteudo().equals("let")){
                codigo.adicionarCodigo("");
            }

            else if(tokens.get(i).getConteudo().equals("fi")){
                codigo.adicionarCodigo("");
            }

            else if(tokens.get(i).getConteudo().equals("then")){
                codigo.adicionarCodigo("");
            }

            else if(tokens.get(i).getConteudo().equals("loop")){
                codigo.adicionarCodigo("for");
            }

            else if(tokens.get(i).getConteudo().equals("pool")){
                codigo.adicionarCodigo("");
            }

            else if(tokens.get(i).getConteudo().equals("in")){
                codigo.adicionarCodigo("");
            }

            else if(tokens.get(i).getConteudo().equals("case")){
                codigo.adicionarCodigo("");
            }

            else if(tokens.get(i).getConteudo().equals("of")){
                codigo.adicionarCodigo("");
            }

            else if(tokens.get(i).getConteudo().equals("esac")){
                codigo.adicionarCodigo("");
            }

            else if(tokens.get(i).getConteudo().equals("new")){
                codigo.adicionarCodigo("");
            }

            else if(tokens.get(i).getConteudo().equals("isvoid")){
                codigo.adicionarCodigo("");
            }

            else if(tokens.get(i).getConteudo().equals("not")){
                codigo.adicionarCodigo("!");
            }

            else if(tokens.get(i).getConteudo().equals("out_string")){
                codigo.adicionarCodigo("System.out.println");
            }

            else if(tokens.get(i).getConteudo().equals("main")){
                codigo.adicionarCodigo("public static void main(String[] args)");
                i = i + 4;
            }

            else if(tokens.get(i).getCategoria() == Categoria.IDENTIFICADOR && 
                tamanhoDaListaNaoExcedido(i + 2) &&
                tokens.get(i + 1).getConteudo().equals(":")){
    
                    if(tokens.get(i + 2).getCategoria() == Categoria.IDENTIFICADOR){
                        codigo.adicionarCodigo(tokens.get(i + 2).getConteudo()); 
                        codigo.adicionarCodigo(tokens.get(i).getConteudo());
                        i = i + 2;   
                    }

                    else if(tokens.get(i + 2).getConteudo().equals("String") ||
                        tokens.get(i + 2).getConteudo().equals("Int")){
                        codigo.adicionarCodigo(tokens.get(i + 2).getConteudo()); 
                        codigo.adicionarCodigo(tokens.get(i).getConteudo());
                        i = i + 2;  
                    }
            }

            else if(tokens.get(i).getCategoria() == Categoria.IDENTIFICADOR &&
                tamanhoDaListaNaoExcedido(i + 3)
                && tokens.get(i + 3).getConteudo().equals(":")){

                    if(tokens.get(i + 2).getConteudo().equals(")")){
                        if(tamanhoDaListaNaoExcedido(i + 4)){
                            codigo.adicionarCodigo(tokens.get(i + 4).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i + 1).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i + 2).getConteudo());
                            i = i + 4;
                        }
                    }

                    else if(tamanhoDaListaNaoExcedido(i + 11) &&
                        tokens.get(i + 10).getConteudo().equals(":")){
                            codigo.adicionarCodigo(tokens.get(i + 11).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i + 1).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i + 4).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i + 2).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i + 5).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i + 8).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i + 6).getConteudo());
                            codigo.adicionarCodigo(tokens.get(i + 9).getConteudo());
                            i = i + 11;
                    }
            }

            else if(tokens.get(i).getConteudo().equals("<-")){
                codigo.adicionarCodigo("=");
            }

            else if(tokens.get(i).getConteudo().equals("=")){
                codigo.adicionarCodigo("==");
            }
    
            else{
                codigo.adicionarCodigo(tokens.get(i).getConteudo());
            }

        }

    }

    public static void exibirCodigo(){
        System.out.println(codigo);
    }

}
