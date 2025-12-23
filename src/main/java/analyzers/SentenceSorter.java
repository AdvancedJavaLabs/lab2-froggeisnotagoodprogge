package analyzers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SentenceSorter {
    public List<String> sortByLength(String text) {
        String[] sentences = text.split("(?<=[.!?])\\s+");
        return Arrays.stream(sentences)
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
    }
}