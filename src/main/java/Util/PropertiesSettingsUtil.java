package Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesSettingsUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesSettingsUtil(){
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }

    public static int getInt(String key){
        try {
            return Integer.parseInt(PROPERTIES.getProperty(key));
        }catch (NumberFormatException e){
            throw new RuntimeException(e);
        }
    }

    private static void loadProperties(){
        try (InputStream inputStream = PropertiesSettingsUtil.class.getClassLoader().getResourceAsStream("application.properties")){
            PROPERTIES.load(inputStream);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
