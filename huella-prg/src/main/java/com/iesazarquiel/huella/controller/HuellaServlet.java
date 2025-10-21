package com.iesazarquiel.huella.controller;

import com.iesazarquiel.huella.model.HuellaCarbonoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/huella")
public class HuellaServlet extends HttpServlet {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HuellaCarbonoService service = new HuellaCarbonoService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String transporte = request.getParameter("transporte");
        String kmStr = request.getParameter("km");
        String diasStr = request.getParameter("dias");
        String op = request.getParameter("op");
        
        // Validación
        if (!validarTransporte(transporte) || !validarKm(kmStr) || !validarDias(diasStr)) {
            String error = "Error: Parámetros inválidos. Verifique transporte, km y días.";
            response.sendRedirect(request.getContextPath() + "/huella?error=" + 
                java.net.URLEncoder.encode(error, "UTF-8"));
            return;
        }
        
        double km = Double.parseDouble(kmStr);
        int dias = Integer.parseInt(diasStr);
        double kg = service.calcularSemanal(transporte, km, dias);
        
        // Redirección según operación
        String redirectUrl = request.getContextPath() + "/huella?op=" + op + "&kg=" + kg;
        
        switch (op) {
            case "CLASIFICAR_IMPACTO":
                String impacto = service.clasificarImpacto(kg);
                redirectUrl += "&impacto=" + impacto;
                break;
            case "PROPONER_COMPENSACION":
                String comp = service.proponerCompensacion(kg);
                redirectUrl += "&comp=" + java.net.URLEncoder.encode(comp, "UTF-8");
                break;
        }
        
        response.sendRedirect(redirectUrl);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Copiar parámetros a atributos para el JSP
        if (request.getParameter("op") != null) {
            request.setAttribute("op", request.getParameter("op"));
            request.setAttribute("kg", Double.parseDouble(request.getParameter("kg")));
            
            if (request.getParameter("impacto") != null) {
                request.setAttribute("impacto", request.getParameter("impacto"));
            }
            if (request.getParameter("comp") != null) {
                request.setAttribute("comp", request.getParameter("comp"));
            }
        }
        if (request.getParameter("error") != null) {
            request.setAttribute("error", request.getParameter("error"));
        }
        
        request.getRequestDispatcher("/WEB-INF/views/huella.jsp").forward(request, response);
    }
    
    private boolean validarTransporte(String transporte) {
        return transporte != null && 
            (transporte.equals("COCHE") || transporte.equals("AUTOBUS") || 
             transporte.equals("TREN") || transporte.equals("BICI") || transporte.equals("PIE"));
    }
    
    private boolean validarKm(String kmStr) {
        if (kmStr == null || kmStr.trim().isEmpty()) return false;
        try {
            double km = Double.parseDouble(kmStr);
            return km > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean validarDias(String diasStr) {
        if (diasStr == null || diasStr.trim().isEmpty()) return false;
        try {
            int dias = Integer.parseInt(diasStr);
            return dias >= 1 && dias <= 7;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}