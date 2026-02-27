package software.ulpgc.bigData.InvertedIndex.CleanBooks;

import java.util.List;

public interface BookCleaner {
    String cleanText(String content);

    List<String> readWords(String path);
}
