package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static byte[] toJsonBytes(Object obj) {
        try {
            return MAPPER.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJsonBytes(byte[] data, Class<T> clazz) {
        try {
            return MAPPER.readValue(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void toFile(Object obj, String path) {
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File(path), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}