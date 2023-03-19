import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import utils.Utilitario;

public class ConverteColunas {

  public static void main(String[] args) {

    final String separadorColunas = "\t";
    final String separadorItens = ";";

    try {
      System.out.print("\n\nInício - ");
      System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

      // Preparando arquivo de retorno
      criarArquivoDestino();
      StringBuilder sb = new StringBuilder();

      // Lendo arquivo de origem

      File fileArqCompleto = new File(Utilitario.diretorioLocal() + File.separator + "origem.txt");
      String strArqCompleto = Files.readString(fileArqCompleto.toPath());

      // Split por linhas
      String[] arrLinhas = strArqCompleto.split("\r\n");

      // Percorrendo as linhas
      for (int i = 0; i < arrLinhas.length; i++) {

        // Cabecalho
        if (i == 0) {

          String[] columns = Utilitario.textToColumns(arrLinhas[i], separadorColunas);
          sb.append(Utilitario.arrToStr(columns));

        } else { // Linhas

          // Variaveis
          int qtdItens = 0;

          // Split por colunas
          String[] colunas = Utilitario.textToColumns(arrLinhas[i], separadorColunas);

          // Conta quantas novas linhas ter�o
          for (int j = 0; j < colunas.length; j++) {

            if (colunas[j].contains(separadorItens)) {

              int aux = 0;

              // Contando os separadores
              String[] itens = colunas[j].split(separadorItens);
              aux = itens.length;

              // Pegando qtd da coluna que possuir mais pipe
              if (aux > qtdItens) {
                qtdItens = aux;
              }
            }
          }

          // Caso não exista itens
          if (qtdItens == 0) {

            sb.append("\r\n");
            String[] columns = Utilitario.textToColumns(arrLinhas[i], separadorColunas);
            sb.append(Utilitario.arrToStr(columns));

          } else {

            // Duplicando as linhas, na quantidade de vezes necessárias - de acordo com os
            // pipes
            for (int j = 0; j < qtdItens; j++) {

              sb.append("\r\n");

              // Percorrendo as colunas
              for (int k = 0; k < colunas.length; k++) {

                // Adicionando tabulação entre os campos, exceto para o 1 coluna
                if (k != 0) {
                  sb.append("\t");
                }

                // Adicionando a coluna ou o item
                if (colunas[k].contains(separadorItens)) {

                  String[] itens = colunas[k].split(separadorItens);

                  if (itens.length > j) {
                    sb.append(itens[j]);
                  }

                } else {

                  sb.append(colunas[k]);
                }
              }
            }
          }

          // Salvar no arquivo de destino - salvando aos poucos, linha a linha por causa
          // de memória
          salvarArquivoDestino(sb.toString().replaceAll("\"", ""));
          sb = new StringBuilder();
        }
      }

      System.out.print("Concluído - ");
      System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

    } catch (Exception e) {
      System.out.println("\n\n-----------------------------------------------\n\n");
      System.out.println("Erro no programa, segue o log:\n\n");
      e.printStackTrace();
    }
  }

  private static void criarArquivoDestino() throws IOException {

    // Define nome de destino
    File arquivo = new File(Utilitario.diretorioLocal() + File.separator + "destino.txt");

    // deletando caso já exista
    if (arquivo.exists()) {
      arquivo.delete();
    }

    arquivo.createNewFile();
  }

  private static void salvarArquivoDestino(String str) throws IOException {

    // write merged files
    File arquivo = new File(Utilitario.diretorioLocal() + File.separator + "destino.txt");

    FileOutputStream fos = new FileOutputStream(arquivo, Boolean.TRUE);
    DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
    outStream.write(str.getBytes(StandardCharsets.UTF_8));
    outStream.close();
  }
}
