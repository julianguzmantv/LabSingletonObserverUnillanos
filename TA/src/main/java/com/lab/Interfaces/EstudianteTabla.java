package com.lab.Interfaces;
import javax.swing.table.AbstractTableModel;
import com.lab.Models.Estudiante;
import java.util.ArrayList;
import java.util.List;

public class EstudianteTabla extends AbstractTableModel {
    private List<Estudiante> estudiantes = new ArrayList<>();
    private final String[] columnas = { "ID", "Nombre", "Apellidos", "Código", "Promedio" };

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
        fireTableDataChanged(); 
    }

    public Estudiante getEstudianteAt(int rowIndex) {
        return estudiantes.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return estudiantes.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Estudiante estudiante = estudiantes.get(rowIndex);
        switch (columnIndex) {
            case 0: return estudiante.getId();
            case 1: return estudiante.getNombre();
            case 2: return estudiante.getApellidos();
            case 3: return estudiante.getCodigo();
            case 4: return estudiante.getPromedio();
            default: return null;
        }
    }
}