package com.plushome.mc.homeplugin.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class LibraryLoader {

    private final File dataFolder;

    public LibraryLoader(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public void load() {
        File libsFolder = new File(dataFolder, "libs");
        if (!libsFolder.exists()) {
            libsFolder.mkdirs();
        }

        File sqliteJar = new File(libsFolder, "sqlite-jdbc.jar");

        if (!sqliteJar.exists()) {
            System.out.println("Downloading SQLite JDBC driver...");
            try {
                URL url = new URL("https://repo.maven.apache.org/maven2/org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar");
                try (InputStream in = url.openStream();
                     ReadableByteChannel rbc = Channels.newChannel(in);
                     FileOutputStream fos = new FileOutputStream(sqliteJar)) {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }
                System.out.println("SQLite JDBC driver downloaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try {
            URLClassLoader classLoader = (URLClassLoader) getClass().getClassLoader();
            java.lang.reflect.Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, sqliteJar.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}