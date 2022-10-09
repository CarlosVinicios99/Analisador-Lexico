package sintatico;

import java.util.List;
import lexico.ElementosDaLinguagem;
import lexico.Token;

public class Regras {
    
    private static List<Token> listaDeTokens;
    private static int indiceAtual;

    private Regras() {}

    public static void associarTokens(List<Token> tokens){

        listaDeTokens = tokens;
        indiceAtual = 0;

    }

    public static boolean tamanhoDaListaNaoExcedido(){
        return indiceAtual < listaDeTokens.size();
    }

    public static boolean analisarSintaxe(){
        boolean resultado = false;
        boolean condicao = tamanhoDaListaNaoExcedido() &&
            listaDeTokens.get(indiceAtual).getConteudo().equals("class");
        
        //testa se existe pelo menos uma classe definida
        if(!condicao){
            System.out.println("Nao ha classe definida!");
            System.out.println(" Linha: " + listaDeTokens.get(indiceAtual).getLinha());
            return false; 
        }

        //loop de classes, vai verificando cada classe existente
        while(condicao){
            resultado = classe();

            if(resultado == false){
                return false;
            }

            if(tamanhoDaListaNaoExcedido()){
                if(listaDeTokens.get(indiceAtual).getConteudo().equalsIgnoreCase(";")){
                    indiceAtual++;   
                } 
                else{
                    System.out.println("faltou colocar ';' + linha: ");
                    System.out.print(listaDeTokens.get(indiceAtual).getLinha());
                    return false;
                }
            }  

            condicao = tamanhoDaListaNaoExcedido() &&
                listaDeTokens.get(indiceAtual).getConteudo().equals("class"); 
        }
        return true;
    }

    public static boolean classe(){
        indiceAtual++;
        boolean possuiTipo = false;
        boolean possuiInherits = false;
        boolean possuiTipoDeInherits = false;
        boolean possuiAbreChaves = false;
        boolean possuiFeature = false;
        boolean possuiPontoEVirgulaPosFeature = false;
        boolean possuiFechaChaves = false;

        //testa se o tipo da classe esta definido corretamente
        if(tamanhoDaListaNaoExcedido()){
            if(ElementosDaLinguagem.ehUmTipoDeClasse(listaDeTokens.get(indiceAtual).getConteudo())){
                possuiTipo = true;
            }
            else{
                System.out.println("Classe sem definicao correta do tipo! linha: " 
                    + listaDeTokens.get(indiceAtual).getLinha());
                return false;
            }
            indiceAtual++;
        }

        //testa se existe inherits
        if(tamanhoDaListaNaoExcedido()){      
            if(listaDeTokens.get(indiceAtual).getConteudo().equals("inherits")){
                possuiInherits = true;
                indiceAtual++;
            }
        }

        //caso exista inherits, testa se o tipo esta definido corretamente
        if(tamanhoDaListaNaoExcedido() && possuiInherits){
            if(ElementosDaLinguagem.ehUmTipoDeClasse(listaDeTokens.get(indiceAtual).getConteudo())){
                possuiTipoDeInherits = true;
            }
            else{
                System.out.println("inherits sem definicao correta do tipo! linha: "
                    + listaDeTokens.get(indiceAtual).getLinha());
                    return false;
            }
            indiceAtual++;
        }

        //seguir a partir daqui

        //testa se o abre chaves existe
        if(tamanhoDaListaNaoExcedido()){
            if(listaDeTokens.get(indiceAtual).getConteudo().equals("{")){
                possuiAbreChaves = true;       
            }
            else{
                System.out.println("faltou colocar '{' linha: " 
                    + listaDeTokens.get(indiceAtual).getLinha());
            }
            indiceAtual++;
        }

        //testar se tem ID apos abre chaves, caso tenha, criar o loop de features
        if(tamanhoDaListaNaoExcedido() && possuiInherits){
            if(ElementosDaLinguagem.ehUmIdentificador(listaDeTokens.get(indiceAtual).getConteudo())){
                possuiFeature = true;
            }
        }

        //loop de features, vai verificando cada feature existente
        while(possuiFeature){
            boolean resultado = feature();

            if(resultado == false){
                return false;
            }

            //testar se tem ponto e virgula depois da feature
            if(tamanhoDaListaNaoExcedido()){
                if(listaDeTokens.get(indiceAtual).getConteudo().equalsIgnoreCase(";")){
                    possuiPontoEVirgulaPosFeature = true;
                    indiceAtual++;    
                } 
                else{
                    System.out.println("faltou colocar ';' + linha: ");
                    System.out.println(listaDeTokens.get(indiceAtual).getLinha());
                    return false;
                }
            }

            possuiFeature = tamanhoDaListaNaoExcedido() && 
                ElementosDaLinguagem.ehUmIdentificador(listaDeTokens.get(indiceAtual).getConteudo());
        }
        
        //testa se o fecha chaves existe
        if(tamanhoDaListaNaoExcedido()){
            if(listaDeTokens.get(indiceAtual).getConteudo().equals("}")){
                possuiFechaChaves = true;       
            }
            else{
                System.out.println("faltou colocar '}' linha: " 
                    + listaDeTokens.get(indiceAtual).getLinha());
            }
            indiceAtual++;
        }

        boolean classeSimples = possuiTipo && possuiAbreChaves && possuiFechaChaves;

        boolean classeComInheritsESemFeature = classeSimples && possuiInherits && possuiTipoDeInherits;

        boolean classeSemInheritsEComFeature = classeSimples && possuiFeature && possuiPontoEVirgulaPosFeature;

        boolean classeComInheristsEComFeature = classeSimples && possuiInherits && possuiTipoDeInherits &&
            possuiFeature && possuiPontoEVirgulaPosFeature;

        return classeSimples || classeComInheritsESemFeature || classeSemInheritsEComFeature ||
            classeComInheristsEComFeature;

    }

    public static boolean feature(){
        indiceAtual++;
        boolean caminhoComAbreParenteses = false;
        boolean caminhoComDoisPontos = false;

        //testa se apos um ID tem um abre paresentes, dois pontos ou nenhum das opcoes (erro)
        if(tamanhoDaListaNaoExcedido()){
            if(listaDeTokens.get(indiceAtual).getConteudo().equals("(")){
                caminhoComAbreParenteses = true;
                indiceAtual++;
            }
            else if(listaDeTokens.get(indiceAtual).getConteudo().equals(":")){
                caminhoComDoisPontos = true;
                indiceAtual++;
            }
            else{
                System.out.println("Simbolo ilegal:  "
                + "'" + listaDeTokens.get(indiceAtual).getConteudo() + "' linha " 
                + listaDeTokens.get(indiceAtual).getLinha());
                return false;
            }
        }
        
        //segue o caminho que apos o ID tem um abre parenteses
        if(caminhoComAbreParenteses){
            if(tamanhoDaListaNaoExcedido()){

                //testa se existe um formal que eh opcional    
                //se houverter mais de algum formal pode  um, deve se iniciar um loop
                boolean condicao = ElementosDaLinguagem.
                    ehUmIdentificador(listaDeTokens.get(indiceAtual).getConteudo());
                    boolean resultado;

                    if(condicao){
                        resultado = formal();
                        if(resultado == false){
                            return false;
                        }
                    }

                    condicao = tamanhoDaListaNaoExcedido() &&
                        listaDeTokens.get(indiceAtual).getConteudo().equals(",");

                while(condicao){
                    resultado = formal();
                    if(resultado == false){
                        return false;
                    }
                    condicao = tamanhoDaListaNaoExcedido() &&
                        listaDeTokens.get(indiceAtual).getConteudo().equals(",");
                }
                    
                //testa se o proximo token eh um fecha parenteses
                if(listaDeTokens.get(indiceAtual).getConteudo().equals(")")){
                    indiceAtual++;
                }

                else{
                    System.out.println("Faltou colocar ')' Linha: " +
                    listaDeTokens.get(indiceAtual).getLinha());
                    return false;
                }

                condicao = tamanhoDaListaNaoExcedido() && 
                    listaDeTokens.get(indiceAtual).getConteudo().equals(":");

                //testa se o proximo token eh um dois pontos
                if(condicao){
                    indiceAtual++;
                }

                else{
                    System.out.println("Faltou colocar ':' Linha: " +
                    listaDeTokens.get(indiceAtual).getLinha());
                    return false;
                }

                condicao = tamanhoDaListaNaoExcedido() && 
                    ElementosDaLinguagem.ehUmTipoDeClasse(listaDeTokens.get(indiceAtual).getConteudo());

                //testa se o proximo token eh um tipo
                if(condicao){
                    indiceAtual++;
                }

                else{
                    System.out.println("Tipo nao definido Linha: " +
                    listaDeTokens.get(indiceAtual).getLinha());
                    return false;
                }

                condicao = tamanhoDaListaNaoExcedido() && 
                    listaDeTokens.get(indiceAtual).getConteudo().equals("{");

                //testa se o proximo token eh um abre chaves
                if(!condicao){
                    System.out.println("Faltou colocar '{' Linha: " +
                    listaDeTokens.get(indiceAtual).getLinha());
                    return false;
                }

                condicao = tamanhoDaListaNaoExcedido();

                //chamando expresssao(), esse metodo vai gerenciar se tem erro ou nao no proximo token
                if(condicao){
                    resultado = expressao();
                    indiceAtual++;
                    if(resultado == false){
                        return false;
                    }
                }

                condicao = tamanhoDaListaNaoExcedido() && 
                    listaDeTokens.get(indiceAtual).getConteudo().equals("}");

                //testa se o proximo token eh um fecha chaves
                if(condicao){
                    indiceAtual++;
                }

                else{
                    System.out.println("Faltou colocar '}' Linha: " +
                    listaDeTokens.get(indiceAtual).getLinha());
                    return false;
                }
                return true;
            }
        }

        //segue o caminho que apos o ID tem um dois pontos
        if(caminhoComDoisPontos){

            if(tamanhoDaListaNaoExcedido()){

                //testa se o proximo token eh um tipo
                boolean proximoTokenEhUmTipo = 
                    ElementosDaLinguagem.ehUmTipoDeClasse(listaDeTokens.get(indiceAtual).getConteudo());

                if(proximoTokenEhUmTipo){
                    indiceAtual++;
                }

                else{
                    System.out.println("Erro" +
                    listaDeTokens.get(indiceAtual).getLinha());
                    return false;
                }

                //se o proximo token for uma atribuicao, deve ter uma expressao ao lado
                boolean proximoTokenEhUmaAtribuicao = 
                    listaDeTokens.get(indiceAtual).getConteudo().equals("<-");

                if(proximoTokenEhUmaAtribuicao){
                    boolean resultado = expressao();
                    if(resultado == false){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean formal(){
        System.out.println("Entrou em formal");
        indiceAtual++;

        //testa se o proximo token eh um ID
        if(tamanhoDaListaNaoExcedido()){
            boolean proximoTokenEhUmIdentificador = 
                ElementosDaLinguagem.ehUmIdentificador(listaDeTokens.get(indiceAtual).getConteudo());

            if(proximoTokenEhUmIdentificador){
                indiceAtual++;
            }

            else{
                System.out.println("erro");
                return false;
            }

        }

        //testa se o proximo token eh um dois pontos
        if(tamanhoDaListaNaoExcedido()){
            boolean proximoTokenEhUmDoisPontos = 
                listaDeTokens.get(indiceAtual).getConteudo().equals(":");

            if(proximoTokenEhUmDoisPontos){
                indiceAtual++;
            }

            else{
                System.out.println("erro");
                return false;
            }

        }

        //testa se o proximo token eh um tipo
        if(tamanhoDaListaNaoExcedido()){
            boolean proximoTokenEhUmTipo = 
                ElementosDaLinguagem.ehUmTipoDeClasse(listaDeTokens.get(indiceAtual).getConteudo());

            if(proximoTokenEhUmTipo){
                indiceAtual++;
            }

            else{
                System.out.println("erro");
                return false;
            }
        }
        return true;
    }

    public static boolean expressao(){
        indiceAtual++;
        if(tamanhoDaListaNaoExcedido()){

            String tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();

            //testar casos que comecam com ID
            if(ElementosDaLinguagem.ehUmIdentificador(tokenAtual)){
                indiceAtual++;
                if(tamanhoDaListaNaoExcedido()){
                    tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();
                    if(tokenAtual.equals("<-")){
                        boolean resultado = expressao();
                        if(resultado == false){
                            System.out.println("erro");
                            return false;
                        }
                        return true;
                    }

                    else if(tokenAtual.equals("(")){
                        indiceAtual++;
                
                        if(tamanhoDaListaNaoExcedido()){
                            tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();     
                        }

                        boolean condicao = tamanhoDaListaNaoExcedido() && !tokenAtual.equals(")");
                        while(condicao){
                            indiceAtual++;
                            if(tamanhoDaListaNaoExcedido()){
                                tokenAtual = listaDeTokens.get(indiceAtual).getConteudo(); 
                            }
                            condicao = tamanhoDaListaNaoExcedido() && !tokenAtual.equals(")");
                        }

                        return true;
                    }
                    
                    else{
                        System.out.println("erro");
                        return false;
                    }
                }
                return true;
            }

            //testar se eh um if
            else if(tokenAtual.equals("if")){
                boolean resultado = expressao();
                if(resultado == false){
                    System.out.println("Erro");
                    return false;
                }

                if(tamanhoDaListaNaoExcedido()){
                    tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();

                    if(tokenAtual.equals("then")){
                        resultado = expressao();
                        if(resultado == false){
                            System.out.println("Erro");
                            return false;
                        }

                        if(tamanhoDaListaNaoExcedido()){
                            tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();
                            if(tokenAtual.equals("else")){
                                resultado = expressao();
                                if(resultado == false){
                                    System.out.println("Erro");
                                    return false;
                                }

                                if(tamanhoDaListaNaoExcedido()){
                                    tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();
                                    if(tokenAtual.equals("fi")){
                                        return true;
                                    }
                                    else{
                                        System.out.println("erro");
                                        return false;
                                    }
                                }
                            }

                            else{
                                System.out.println("Erro");
                                return false;
                            }
                        }
                    }  

                    else{
                        System.out.println("Erro");
                        return false;
                    }
                }
            }

            //testar se eh um while
            else if(tokenAtual.equals("while")){
                boolean resultado = expressao();
                if(resultado == false){
                    System.out.println("Erro");
                    return false;
                }

                if(tamanhoDaListaNaoExcedido()){
                    tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();

                    if(tokenAtual.equals("loop")){
                        resultado = expressao();
                        if(resultado == false){
                            System.out.println("Erro");
                            return false;
                        }

                        if(tamanhoDaListaNaoExcedido()){
                            tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();
                            if(tokenAtual.equals("pool")){
                               return true;
                            }

                            else{
                                System.out.println("Erro");
                                return false;
                            }
                        }
                    }  

                    else{
                        System.out.println("Erro");
                        return false;
                    }
                }
            }

            else if(tokenAtual.equals("true")){
                return true;
            }

            else if(tokenAtual.equals("false")){
                return true;
            }

            else if(tokenAtual.equals("integer")){
                return true;
            }

            else if(tokenAtual.equals("string")){
                return true;
            }


            else if(tokenAtual.equals("not")){
                boolean resultado = expressao();
                if(resultado == false){
                    System.out.println("Erro");
                    return false;
                }
            }

            else if(tokenAtual.equals("(")){
                boolean resultado = expressao();
                if(resultado == false){
                    System.out.println("Erro");
                    return false;
                }
                
                if(tamanhoDaListaNaoExcedido()){
                    tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();
                    if(tokenAtual.equals(")")){
                        return true;
                    }
                    else{
                        System.out.println("Erro");
                        return false;
                    }
                }
            }
            
            else if(tokenAtual.equals("{")){
                boolean resultado = expressao();

                if(resultado == false){
                    System.out.println("Erro");
                    return false;
                }

                if(tamanhoDaListaNaoExcedido()){
                    tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();

                    if(tokenAtual.equals(";")){
                        indiceAtual++;
                        if(tamanhoDaListaNaoExcedido()){
                            if(tokenAtual.equals("}")){
                                return true;
                            }
                            else{
                                System.out.println("Erro");
                                return false;
                            }
                        }
                    }
                    else{
                        System.out.println("Erro");
                        return false;
                    }
                }
            }

            else if(tokenAtual.equals("isvoid")){
                boolean resultado = expressao();
                if(resultado == false){
                    System.out.println("Erro");
                    return false;
                }
            }

            else if(tokenAtual.equals("new")){
                indiceAtual++;
                if(tamanhoDaListaNaoExcedido()){
                    tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();
                    if(ElementosDaLinguagem.ehUmTipoDeClasse(tokenAtual)){
                        return true;
                    }
                    else{
                        System.out.println("Erro");
                        return false;
                    }
                }
            }

            //testar as expressoes de operacao aritmetica
            else if(ElementosDaLinguagem.ehUmValorNumericoLiteral(tokenAtual)){
                indiceAtual++;
                if(tamanhoDaListaNaoExcedido()){
                    tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();
                    if(ElementosDaLinguagem.ehUmOperador(tokenAtual)){
                        indiceAtual++;
                        if(tamanhoDaListaNaoExcedido()){
                            tokenAtual = listaDeTokens.get(indiceAtual).getConteudo();
                            if(ElementosDaLinguagem.ehUmValorNumericoLiteral(tokenAtual)){
                                return true;
                            }
                            else{
                                System.out.println("Erro");
                            }
                        }
                    }
                    else{
                        System.out.println("Erro");
                        return false;
                    }
                }    
            }
        }

        return true;
    }

}

//apos chamada de expressao tem que incrementar o indice atual