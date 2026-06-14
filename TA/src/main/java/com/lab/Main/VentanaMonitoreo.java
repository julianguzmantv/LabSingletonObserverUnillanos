package com.lab.Main;
import javax.swing.*;
import java.awt.*;
import com.lab.Observer.CursoObserver;
import com.lab.Observer.CursoSubject;
import com.lab.Models.Curso;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaMonitoreo extends JFrame implements CursoObserver {
    private JTextArea areaNotificaciones;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    public VentanaMonitoreo() {
        configurarVentana();
        inicializarComponentes();
        registrarObserver();
        mostrarVentana();
    }
    
    private void configurarVentana() {
        setTitle("📊 CONSOLA DE MONITOREO - OBSERVER");
        setSize(700, 400);
        setLocation(400, 200); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void inicializarComponentes() {
        areaNotificaciones = new JTextArea();
        areaNotificaciones.setEditable(false);
        areaNotificaciones.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaNotificaciones.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(areaNotificaciones);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Notificaciones en Tiempo Real"));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void registrarObserver() {
        CursoSubject.getInstancia().registrarObserver(this);
    }
    
    private void mostrarVentana() {
        setVisible(true);
        agregarNotificacion("🟢 MONITOREO ACTIVO - Observer registrado");
        agregarNotificacion("💡 Esperando cambios en cursos...");
        agregarNotificacion("======================================");
    }
    
    private void agregarNotificacion(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaNotificaciones.append(mensaje + "\n");
            areaNotificaciones.setCaretPosition(areaNotificaciones.getDocument().getLength());
        });
    }
    
    @Override
    public void onCursoAgregado(Curso curso) {
        String mensaje = String.format("[%s] 📗 CURSO AGREGADO | ID: %.0f | %s | %s",
                dateFormat.format(new Date()),
                curso.getId(),
                curso.getNombre(),
                curso.getActivo() ? "Activo" : "Inactivo");
        agregarNotificacion(mensaje);
    }
    
    @Override
    public void onCursoEliminado(Curso curso) {
        String mensaje = String.format("[%s] 📕 CURSO ELIMINADO | ID: %.0f | %s",
                dateFormat.format(new Date()),
                curso.getId(),
                curso.getNombre());
        agregarNotificacion(mensaje);
    }
    
    @Override
    public void onCursoActualizado(Curso cursoAntiguo, Curso cursoNuevo) {
        StringBuilder cambios = new StringBuilder();
        
        if (!cursoAntiguo.getNombre().equals(cursoNuevo.getNombre())) {
            cambios.append(String.format("Nombre: '%s'→'%s' ", 
                cursoAntiguo.getNombre(), cursoNuevo.getNombre()));
        }
        
        if (cursoAntiguo.getActivo() != cursoNuevo.getActivo()) {
            if (cambios.length() > 0) cambios.append("| ");
            cambios.append(String.format("Activo: %s→%s", 
                cursoAntiguo.getActivo() ? "Sí" : "No", 
                cursoNuevo.getActivo() ? "Sí" : "No"));
        }

        String mensaje = String.format("[%s] 📘 CURSO ACTUALIZADO | ID: %.0f | %s",
                dateFormat.format(new Date()),
                cursoAntiguo.getId(),
                cambios.length() > 0 ? cambios.toString() : "Sin cambios visibles");
        
        agregarNotificacion(mensaje);
    }
    
    @Override
    public void onInscripcionCreada(String mensaje) {
    }
}