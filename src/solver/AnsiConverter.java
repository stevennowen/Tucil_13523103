package solver;

import java.awt.Color;

public class AnsiConverter {
    public static Color colorFromANSI(String ansiCode) {
        // Pastikan ANSI code mengandung "38;2;" untuk true color
        String marker = "38;2;";
        int index = ansiCode.indexOf(marker);
        if (index == -1) {
            // Jika format gak sesuai
            return Color.BLACK;
        }

        // Ambil substring setelah "38;2;"
        String rgbPart = ansiCode.substring(index + marker.length());
        // Hapus karakter 'm' di akhir (jika ada)
        if (rgbPart.endsWith("m")) {
            rgbPart = rgbPart.substring(0, rgbPart.length() - 1);
        }

        String[] parts = rgbPart.split(";");
        if (parts.length != 3) {
            return Color.BLACK;
        }

        try {
            int r = Integer.parseInt(parts[0]);
            int g = Integer.parseInt(parts[1]);
            int b = Integer.parseInt(parts[2]);
            return new Color(r, g, b);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Color.BLACK;
        }
    }
}
