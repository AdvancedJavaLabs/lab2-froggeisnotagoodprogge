package analyzers;

public class WordCounter {

    public long count(String text) {
        return text.split("\\s+").length;
    }
}