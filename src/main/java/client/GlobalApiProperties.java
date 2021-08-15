package client;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class GlobalApiProperties {

    private static GlobalApiProperties instance;
    private static CompositeConfiguration configuration;

    private GlobalApiProperties() {
        configuration = new CompositeConfiguration();
        try {
            configuration.addConfiguration(new PropertiesConfiguration("testing.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static GlobalApiProperties getInstance() {
        if (instance == null) {
            synchronized (GlobalApiProperties.class) {
                if (instance == null) {
                    instance = new GlobalApiProperties();
                }
            }
        }

        return instance;
    }

    public String get(String key) {
        return configuration.getString(key);
    }
}