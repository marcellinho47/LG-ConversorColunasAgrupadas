package services;

import java.util.ArrayList;
import java.util.List;

public class ConverterService {

  public String convertContent(
      String originalContent, final String columnSeparator, final String itemSeparator) {

    StringBuilder sb = new StringBuilder();

    List<String> lines = splitContentToLines(originalContent);

    // Walking the lines
    for (String line : lines) {

      List<String> columns = lineToColumns(line, columnSeparator);

      int newLinesNeeded = countNewLinesNeeded(itemSeparator, columns);

      // if no need of new lines
      if (newLinesNeeded == 0) {

        sb.append(columnsToLine(columns));
        sb.append("\r\n");

      } else {

        for (int j = 0; j < newLinesNeeded; j++) {

          // going through the columns
          for (int k = 0; k < columns.size(); k++) {

            // adding the tab except for first item
            if (k != 0) {
              sb.append("\t");
            }

            // adding the item
            if (columns.get(k).contains(itemSeparator)) {

              String[] itens = columns.get(k).split(itemSeparator);

              if (itens.length > j) {
                sb.append(itens[j]);
              }

            } else {
              // adding the column
              sb.append(columns.get(k));
            }
          }

          sb.append("\r\n");
        }
      }
    }

    return sb.toString().replaceAll("\"", "");
  }

  public int countNewLinesNeeded(String itemSeparator, List<String> columns) {

    int contNewLinesNeeded = 0;

    for (String column : columns) {

      if (column.contains(itemSeparator)) {

        int count = column.split(itemSeparator).length;

        if (count > contNewLinesNeeded) {
          contNewLinesNeeded = count;
        }
      }
    }

    return contNewLinesNeeded;
  }

  public List<String> lineToColumns(final String line, String columnSeparator) {

    // Return List
    List<String> list = new ArrayList<>();

    String[] splitBySpace = line.split(columnSeparator);

    // Organize the split
    StringBuilder sb = new StringBuilder();
    int initialQuote = 0;
    for (int i = 0; i < splitBySpace.length; i++) {

      // text without space
      if (splitBySpace[i].chars().filter(ch -> ch == '"').count() > 1) {

        list.add(splitBySpace[i]);

        // open the text
      } else if (splitBySpace[i].contains("\"") && initialQuote == 0) {

        initialQuote = 1;
        sb.append(splitBySpace[i]);
        sb.append(" ");

        // close the text
      } else if (splitBySpace[i].contains("\"") && initialQuote == 1) {

        sb.append(splitBySpace[i]);
        list.add(sb.toString());

        initialQuote = 0;
        sb = new StringBuilder();

        // middle of the text
      } else if (initialQuote == 1) {

        sb.append(splitBySpace[i]);
        sb.append(" ");

        // normal without quotes
      } else {

        list.add(splitBySpace[i]);
      }
    }

    return list;
  }

  public List<String> splitContentToLines(String originalContent) {

    // Return List
    List<String> list = new ArrayList<>();

    originalContent = originalContent.replaceAll("'", "");

    String[] splitByBreakLine;
    if (originalContent.contains("\r\n")) {
      splitByBreakLine = originalContent.split("\r\n");
    } else {
      splitByBreakLine = originalContent.split("\n");
    }

    // Organize the split
    StringBuilder sb = new StringBuilder();
    int initialQuote = 0;
    for (int i = 0; i < splitByBreakLine.length; i++) {

      // text without
      if (splitByBreakLine[i].chars().filter(ch -> ch == '"').count() > 0
          && (splitByBreakLine[i].chars().filter(ch -> ch == '"').count() % 2) == 0) {

        list.add(splitByBreakLine[i]);

        // open the text
      } else if (splitByBreakLine[i].contains("\"") && initialQuote == 0) {

        initialQuote = 1;
        sb.append(splitByBreakLine[i]);
        sb.append(" ");

        // close the text
      } else if (splitByBreakLine[i].contains("\"") && initialQuote == 1) {

        sb.append(splitByBreakLine[i]);
        list.add(sb.toString());

        initialQuote = 0;
        sb = new StringBuilder();

        // middle of the text
      } else if (initialQuote == 1) {

        sb.append(splitByBreakLine[i]);
        sb.append(" ");

        // normal without quotes
      } else {

        list.add(splitByBreakLine[i]);
      }
    }

    return list;
  }

  public String columnsToLine(List<String> columns) {

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < columns.size(); i++) {

      if (i + 1 == columns.size()) {

        sb.append(columns.get(i));
      } else {

        sb.append(columns.get(i));
        sb.append("\t");
      }
    }

    return sb.toString();
  }
}
