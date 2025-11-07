package com.tpopdsunomas.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase utilitaria para cargar configuración desde config.properties
 */
public class ConfigLoader {
    private static Properties properties = new Properties();
    
    static {
        try {
            // Intentar cargar desde el archivo en resources
            InputStream input = ConfigLoader.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            
            if (input != null) {
                properties.load(input);
                System.out.println("✓ Configuración cargada desde config.properties");
            } else {
                System.err.println("⚠ No se encontró config.properties, usando configuración por defecto");
                // Valores por defecto si no se encuentra el archivo
                properties.setProperty("email.username", "test@example.com");
                properties.setProperty("email.password", "test");
            }
        } catch (IOException e) {
            System.err.println("✗ Error cargando configuración: " + e.getMessage());
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
