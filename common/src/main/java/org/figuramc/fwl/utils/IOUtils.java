package org.figuramc.fwl.utils;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOUtils {
    public static byte[] readBytes(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @Nullable String readString(File file) {
        byte[] contents = readBytes(file);
        if (contents != null) {
            return new String(contents, StandardCharsets.UTF_8);
        }
        return null;
    }

    public static boolean writeBytes(File file, byte[] bytes) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean writeString(File file, String string) {
        return writeBytes(file, string.getBytes(StandardCharsets.UTF_8));
    }
}
