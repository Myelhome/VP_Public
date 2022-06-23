package Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class PropertiesHolidaysUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesHolidaysUtil(){
    }

    public static Set<String> get(){return PROPERTIES.keySet().stream().map(Object::toString).collect(toSet());}

    public static String getType(String key){return PROPERTIES.getProperty(key);}

    private static void loadProperties(){
        try (InputStream inputStream = PropertiesSettingsUtil.class.getClassLoader().getResourceAsStream("holidays.properties")){
            PROPERTIES.load(inputStream);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
