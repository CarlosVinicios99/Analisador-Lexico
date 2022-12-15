package geracao_codigo;


public class Codigo {
    
    private StringBuilder codigoFonte;
    private String novoCodigo;

    public Codigo(){
        codigoFonte = new StringBuilder();
    }

    public void adicionarCodigo(String codigo){

        codigoFonte.append(codigo);

        if(codigo.equals("}") || codigo.equals(";") || codigo.equals("{")){
            codigoFonte.append("" +  '\n');
        }
        else{
            codigoFonte.append(" ");
        }
    }

    public void formatarCodigo(){
        novoCodigo = codigoFonte.toString().replace(". System", "\nSystem");
        novoCodigo = novoCodigo.replace("};", "}");
        novoCodigo = novoCodigo.replace("}\n;", "}");
        novoCodigo = novoCodigo.replace("} ;", "}");
        String codigoFormatado = "";

        String identacao = "    ";
        int qtdIdentacao = 0;

        for(int i = 0; i < novoCodigo.length(); i++){
            codigoFormatado = codigoFormatado.concat(Character.toString(novoCodigo.charAt(i)));

            if(novoCodigo.charAt(i) == '{'){
                qtdIdentacao++;
            }

            if(novoCodigo.charAt(i) == '}'){
                qtdIdentacao--;
            }

            if(novoCodigo.charAt(i) == '\n'){
                for(int j = 0; j < qtdIdentacao; j++){
                    codigoFormatado = codigoFormatado.concat(identacao);
                }
            }
        }
        novoCodigo = codigoFormatado;
    }

    //gerarArquivoDeSaida()

    @Override
    public String toString(){
        formatarCodigo();
        return novoCodigo;
    }

}
