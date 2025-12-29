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

    // Validate RUT using modulus 11 algorithm (Chilean RUT)
    public static boolean validateRut(String rut) {
        if (rut == null) return false;
        String s = rut.replace(".", "").replace("-", "").toLowerCase();
        if (s.length() < 2) return false;
        String dv = s.substring(s.length() - 1);
        String num = s.substring(0, s.length() - 1);
        try {
            int sum = 0;
            int multiplier = 2;
            for (int i = num.length() - 1; i >= 0; i--) {
                int digit = Character.digit(num.charAt(i), 10);
                sum += digit * multiplier;
                multiplier++;
                if (multiplier > 7) multiplier = 2;
            }
            int remainder = 11 - (sum % 11);
            String dvCalc;
            if (remainder == 11) dvCalc = "0";
            else if (remainder == 10) dvCalc = "k";
            else dvCalc = String.valueOf(remainder);
            return dvCalc.equals(dv);
        } catch (Exception e) {
            return false;
        }
    }
}
