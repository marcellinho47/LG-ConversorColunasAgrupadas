package services;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FilesService {

  public List<File> readFilesOfCurrentFolder() throws IOException {

    File folder = new File(localDirectory());
    File[] files = folder.listFiles();
    return Arrays.stream(files).filter(File::isFile).toList();
  }

  public File createConvertedFile(File file) throws IOException {

    File convertedFile =
        new File(localDirectory() + File.separator + "CONVERTED_" + file.getName());

    if (convertedFile.exists()) {
      convertedFile.delete();
    }

    convertedFile.createNewFile();

    return convertedFile;
  }

  public void saveContentOnFile(File originalFile, String convertedContent) throws IOException {

    File convertedFile = createConvertedFile(originalFile);

    FileOutputStream fos = new FileOutputStream(convertedFile, Boolean.TRUE);
    DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
    outStream.write(convertedContent.getBytes(StandardCharsets.UTF_8));
    outStream.close();
  }

  public String readContentOfFile(File originalFile) throws IOException {
    return Files.readString(originalFile.toPath());
  }

  private String localDirectory() throws IOException {
    return new File(".").getCanonicalPath();
  }

  public static String getFileExtension(String fileName) {
    if (Objects.isNull(fileName) || fileName.isBlank()) {
      return null;
    }

    int len = fileName.lastIndexOf(".");

    if (len == -1) {
      return null;
    }

    if (len + 1 >= fileName.length()) {
      return null;
    }

    return fileName.substring(len + 1);
  }
}
