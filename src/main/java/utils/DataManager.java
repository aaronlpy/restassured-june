package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class DataManager {
    private static final String FILE_PATH = "src/main/resources/test-data.properties";
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream in = new FileInputStream(FILE_PATH)) {
            properties.load(in);
        }  catch (IOException e) {
            throw new RuntimeException("Error loading test data file", e);
        }
    }

    public static void save(String key, String value) {
        properties.setProperty(key, value);
        try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
            properties.store(out, "Test Data");
        } catch (IOException e) {
            throw new RuntimeException("Error saving test data", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
