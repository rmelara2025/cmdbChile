package com.telefonicatech.cmdbChile.helper;

public class RutUtils {

    // Remove dots, ensure lower-case verifier and has hyphen
    public static String formatRut(String raw) {
        if (raw == null) return null;
        String s = raw.replace(".", "").trim();
        s = s.toLowerCase();
        if (!s.contains("-") && s.length() > 1) {
            s = s.substring(0, s.length() - 1) + "-" + s.substring(s.length() - 1);
        }
        return s;
    }

    // Validate RUT using modulus 11 algorithm
    public static boolean validateRut(String rut) {
        if (rut == null) return false;
        String s = rut.replace(".", "").replace("-", "").toLowerCase();
        if (s.length() < 2) return false;
        String dv = s.substring(s.length() - 1);
        String num = s.substring(0, s.length() - 1);
        try {
            int m = 0, sumb = 1;
            for (int i = num.length() - 1; i >= 0; i--) {
                int digit = Character.digit(num.charAt(i), 10);
                sumb = (sumb + digit * (9 - (m++ % 6))) % 11;
            }
            int res = sumb == 0 ? 0 : 11 - sumb;
            String dvCalc = res == 10 ? "k" : String.valueOf(res);
            return dvCalc.equals(dv);
        } catch (Exception e) {
            return false;
        }
    }
}

