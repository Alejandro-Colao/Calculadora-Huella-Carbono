package com.iesazarquiel.huella.model;

public class HuellaCarbonoService {
    
    private static final double FACTOR_COCHE = 0.21;
    private static final double FACTOR_AUTOBUS = 0.10;
    private static final double FACTOR_TREN = 0.04;
    private static final double FACTOR_BICI = 0.00;
    private static final double FACTOR_PIE = 0.00;
    
    public double calcularSemanal(String transporte, double kmDiarios, int diasSemanales) {
        double factor = obtenerFactor(transporte);
        return kmDiarios * diasSemanales * factor;
    }
    
    public String clasificarImpacto(double kgSem) {
        if (kgSem <= 5) {
            return "Baja";
        } else if (kgSem <= 15) {
            return "Media";
        } else {
            return "Alta";
        }
    }
    
    public String proponerCompensacion(double kgSem) {
        int arboles = (int) Math.ceil(kgSem / 0.40);
        double kmBici = kgSem / 0.21;
        return String.format("~%d árboles (equivalente anual) o %.1f km en bici", arboles, kmBici);
    }
    
    private double obtenerFactor(String transporte) {
        switch (transporte) {
            case "COCHE": return FACTOR_COCHE;
            case "AUTOBUS": return FACTOR_AUTOBUS;
            case "TREN": return FACTOR_TREN;
            case "BICI": return FACTOR_BICI;
            case "PIE": return FACTOR_PIE;
            default: return 0.0;
        }
    }
}