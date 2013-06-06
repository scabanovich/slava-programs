package slava.crossword.preference;

import java.io.*;
import java.util.*;

public class CrosswordPreference {
    private static CrosswordPreference instance;
    
    public static String location = "src/CrosswordPreferences.properties";

    public static CrosswordPreference getInstance() {
        if(instance == null) {
            instance = new CrosswordPreference();
            instance.load();
        }
        return instance;
    }

    Properties properties = new Properties();

    public CrosswordPreference() {}

    public void load() {
        File f = getFile();
        System.out.println(f.getAbsolutePath());
        if(f.isFile()) {
            try {
                InputStream is = new FileInputStream(f);
                properties.load(is);
                String[] s = (String[])properties.keySet().toArray(new String[0]);
                is.close();
            } catch (Exception e) {}
        }
    }

    public void save() {
        File f = getFile();
        if(!f.isFile()) try { f.createNewFile(); } catch (Exception e) {}
        try {
            OutputStream os = new FileOutputStream(f);
            properties.save(os, null);;
            os.close();
        } catch (Exception e) {}
    }

    private File getFile() {
//		return new File("CrosswordPreferences.properties");
        return new File(location);
    }

    public String getString(String name, String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

    public void setString(String name, String value) {
        if(value == null) properties.remove(name); else properties.put(name, value);
    }

    public int getInt(String name, int defaultValue) {
        String s = getString(name, null);
        if(s == null) return defaultValue;
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void setInt(String name, int value) {
        setString(name, "" + value);
    }

}