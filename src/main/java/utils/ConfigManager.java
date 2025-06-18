package utils;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ConfigManager {

    private static Map<String, Object> config;
    private static String environment;

    static {
        try{
            loadConfiguration();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void loadConfiguration() throws IOException {
        try(InputStream inputStream = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.yaml")){
            if (inputStream == null)
                throw new RuntimeException("Configuration file not found");

            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);

            environment = System.getProperty("env", "dev");

            if (data.containsKey("environments")){
                Map<String, Object> environments = (Map<String, Object>) data.get("environments");
                if (!environments.containsKey(environment)){
                    throw new RuntimeException("Invalid environment: " + environment);
                }

                config = (Map<String, Object>) environments.get(environment);
            }
        }
    }

    public static String getBaseURL(){
        return config.get("base_url").toString();
    }

    public static int getTimeOut(){
        return (int) config.get("timeout");
    }

    public static Map<String, String> getHeaders(){
        return (Map<String, String>) config.get("headers");
    }

    public static Map<String, String> getParams(){
        return (Map<String, String>) config.get("queryParams");
    }

    public static String getEnvironment(){
        return environment;
    }
}
