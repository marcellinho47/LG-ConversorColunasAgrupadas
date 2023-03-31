import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import services.ConverterService;
import services.FilesService;

public class ColumnsConverterApp {

  static final FilesService filesService = new FilesService();
  static final ConverterService converterService = new ConverterService();

  public static void main(String[] args) {
    System.out.print("\n\nBEGIN - ");
    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

    try {
      validateArgs(args);

      List<File> files = filesService.readFilesOfCurrentFolder();

      for (File originalFile : files) {

        if (originalFile.getName().contains("git") || originalFile.getName().contains("pom")) {
          continue;
        }

        final String originalContent = filesService.readContentOfFile(originalFile);
        final String convertedContent =
            converterService.convertContent(originalContent, args[0], args[1]);
        filesService.saveContentOnFile(originalFile, convertedContent);
      }

    } catch (Exception e) {
      System.out.println("\n\n-----------------------------------------------\n\n");
      System.out.println("Erro no programa, segue o log:\n\n");
      e.printStackTrace();
    }

    System.out.print("\n\nEND - ");
    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
  }

  private static void validateArgs(String[] args) {

    if (args.length != 2) {
      throw new RuntimeException("Parâmetros não informados.");
    }
  }
}
