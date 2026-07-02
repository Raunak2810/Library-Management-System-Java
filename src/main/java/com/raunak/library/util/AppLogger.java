package com.raunak.library.util;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//  Singleton wrapper around java.util.logging that writes application
//  events to logs/app.log as well as the console. Centralizing logger
//  setup here means every class gets consistent formatting without
//  repeating boilerplate configuration.

public final class AppLogger{
    private static final Logger LOGGER = Logger.getLogger("LibraryApp");
    private static boolean initialized = false;
    private AppLogger(){
        // utility class, no instances
    }
    public static synchronized Logger getLogger(){
        if (!initialized){
            try{
                java.io.File logDir = new java.io.File("logs");
                if(!logDir.exists()){
                    logDir.mkdirs();
                }
                FileHandler fileHandler = new FileHandler("logs/app.log", true);
                fileHandler.setFormatter(new SimpleFormatter());
                LOGGER.addHandler(fileHandler);
                LOGGER.setLevel(Level.ALL);
            } catch (IOException e){
                System.err.println("Warning: could not initialize file logging: " + e.getMessage());
            }
            initialized = true;
        }
        return LOGGER;
    }
}
