package me.jonah.lib.util;

/**
 * @author Jonah Bray
 */
public class Color {
    public static java.awt.Color color(String s) {
        return java.awt.Color.decode(String.valueOf(Integer.parseInt(s.replace("#", ""), 16)));
    }
}
