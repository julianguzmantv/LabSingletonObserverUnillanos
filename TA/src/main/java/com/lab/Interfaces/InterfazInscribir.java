package com.lab.Interfaces;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.lab.DataAccessObj.CursosInscritosDAO;
import com.lab.DataTransferObj.InscripcionDTO;
import com.lab.Models.Inscripcion;
import java.awt.*;
import java.util.List;

public class InterfazInscribir extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CursosInscritosDAO inscripcionDAO;
    private JLabel lblContador;

    public InterfazInscribir(CursosInscritosDAO inscripcionDAO) {
        this.inscripcionDAO = inscripcionDAO;
        this.setLayout(new BorderLayout());
        configurarTabla();
        cargarDatosEnTabla();
    }

    private void configurarTabla() {
        String[] columnas = {"ID Inscripción", "ID Curso", "Nombre Curso", "ID Estudiante", "Nombre Estudiante", "Año", "Semestre"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblContador = new JLabel("Total de inscripciones: 0");
        panelInferior.add(lblContador);
        
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarDatosEnTabla());
        panelInferior.add(btnRefrescar);
        
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(panelInferior, BorderLayout.SOUTH);
    }

    public void cargarDatosEnTabla() {
        try {
            modeloTabla.setRowCount(0); // Limpiar tabla
            
            List<InscripcionDTO> lista = inscripcionDAO.cargarTodos();
            
            for (InscripcionDTO inscripcion : lista) {
                Object[] fila = {
                    inscripcion.getIdInscripcion(),
                    inscripcion.getIdCurso(),
                    inscripcion.getNombreCurso(),
                    inscripcion.getIdEstudiante(),
                    inscripcion.getNombreEstudiante() + " " + inscripcion.getApellidoEstudiante(),
                    inscripcion.getAno(),
                    inscripcion.getSemestre()
                };
                modeloTabla.addRow(fila);
            }
            
            lblContador.setText("Total de inscripciones: " + lista.size());
            
            System.out.println("✅ Tabla cargada con " + lista.size() + " inscripciones");
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar datos en tabla: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al cargar inscripciones: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void agregarFila(Inscripcion inscripcion) {
        try {
            Object[] nuevaFila = {
                inscripcion.getId(),
                inscripcion.getCurso().getId(),
                inscripcion.getCurso().getNombre(),
                inscripcion.getEstudiante().getId(),
                inscripcion.getEstudiante().getNombre() + " " + inscripcion.getEstudiante().getApellidos(),
                inscripcion.getAno(),
                inscripcion.getSemestre()
            };
            modeloTabla.addRow(nuevaFila);
            
            int nuevoTotal = modeloTabla.getRowCount();
            lblContador.setText("Total de inscripciones: " + nuevoTotal);
            
            System.out.println("✅ Nueva inscripción agregada a la tabla: ID " + inscripcion.getId());
            
        } catch (Exception e) {
            System.err.println("❌ Error al agregar fila a tabla: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getNumeroFilas() {
        return modeloTabla.getRowCount();
    }
}