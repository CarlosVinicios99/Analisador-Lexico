import java.io.File;
import java.util.Scanner;

public class ManipulaArquivos {
	
	private ManipulaArquivos() {
		
	}
	
	public static String lerArquivo(String nomeArquivo) {
		
		File arquivo = new File(nomeArquivo);
		StringBuilder codigoFonte = new StringBuilder();

		try {

			Scanner entrada = new Scanner(arquivo);
			
			while(entrada.hasNext()) {
				codigoFonte.append(entrada.nextLine());
				codigoFonte.append("\n");
			}
			
			entrada.close();
			return codigoFonte.toString();
		}
		
		catch(Exception erro) {
			System.out.println(erro.getMessage());
			return "";
		}
	}
	
}
