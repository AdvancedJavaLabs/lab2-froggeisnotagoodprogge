package utils;

import java.util.ArrayList;
import java.util.List;

public class TextSplitter {

    private final int chunkSize;

    public TextSplitter(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public List<String> split(String text) {
        List<String> chunks = new ArrayList<>();

        for (int i = 0; i < text.length(); i += chunkSize) {
            chunks.add(text.substring(i, Math.min(text.length(), i + chunkSize)));
        }

        return chunks;
    }
}