package com.lab.Interfaces;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import com.lab.Models.Curso;

public class CursoTabla extends AbstractTableModel {
    private List<Curso> cursos = new ArrayList<>();
    private final String[] columnNames = {"ID", "Nombre", "Programa", "Activo"};

    @Override
    public int getRowCount() {
        return cursos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Curso curso = cursos.get(rowIndex);
        switch (columnIndex) {
            case 0: return curso.getId();
            case 1: return curso.getNombre();
            case 2: return curso.getPrograma() != null ? curso.getPrograma().getNombre() : "Sin programa";
            case 3: return curso.getActivo() ? "Sí" : "No";
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Double.class;
            case 1: return String.class;
            case 2: return String.class;
            case 3: return String.class;
            default: return Object.class;
        }
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
        fireTableDataChanged();
    }

    public Curso getCursoAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < cursos.size()) {
            return cursos.get(rowIndex);
        }
        return null;
    }
}