package com.lab.Interfaces;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.lab.DataAccessObj.CursoDAO;
import com.lab.Models.Curso;
import com.lab.Models.Programa;

public class CursoPanel extends JPanel {
    private JTextField idField, nombreField;
    private JCheckBox activoCheckBox;
    private JButton guardarButton, leerButton, actualizarButton, eliminarButton;
    private JTable cursoTable;
    private CursoTabla tableModel;
    private final CursoDAO cursoDAO;

    public CursoPanel(CursoDAO cursoDAO) {
        this.cursoDAO = cursoDAO;
        setLayout(new BorderLayout(10, 10));
        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        this.tableModel = new CursoTabla();
        cursoTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(cursoTable);
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        addEventosBotones();
        cargarCursos();
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        inputPanel.add(nombreField);
        inputPanel.add(new JLabel("Activo:"));
        activoCheckBox = new JCheckBox();
        inputPanel.add(activoCheckBox);
        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        guardarButton = new JButton("Guardar");
        leerButton = new JButton("Leer");
        actualizarButton = new JButton("Actualizar");
        eliminarButton = new JButton("Eliminar");
        buttonPanel.add(guardarButton);
        buttonPanel.add(leerButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        return buttonPanel;
    }

    private void addEventosBotones() {
        guardarButton.addActionListener(e -> guardarCurso());
        leerButton.addActionListener(e -> cargarCursos());
        actualizarButton.addActionListener(e -> actualizarCurso());
        eliminarButton.addActionListener(e -> eliminarCurso());

        cursoTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = cursoTable.getSelectedRow();
                if (selectedRow != -1) {
                    Curso curso = tableModel.getCursoAt(selectedRow);
                    idField.setText(String.valueOf(curso.getId()));
                    nombreField.setText(curso.getNombre());
                    activoCheckBox.setSelected(curso.getActivo());
                }
            }
        });
    }

    private void guardarCurso() {
        try {
            double id = Double.parseDouble(idField.getText());
            String nombre = nombreField.getText();
            boolean activo = activoCheckBox.isSelected();
            Curso nuevoCurso = new Curso(id, nombre, null, activo);
            cursoDAO.insertar(nuevoCurso);

            cargarCursos();
            JOptionPane.showMessageDialog(this, "Curso guardado con éxito.");
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar curso: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void cargarCursos() {
        try {
            List<Curso> cursos = cursoDAO.obtenerTodos();
            tableModel.setCursos(cursos);
            System.out.println("✅ Cursos cargados: " + cursos.size());
        } catch (Exception e) {
            System.err.println("❌ Error al cargar cursos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void actualizarCurso() {
        try {
            double id = Double.parseDouble(idField.getText());
            Curso cursoParaActualizar = cursoDAO.buscarPorId(id);
            if (cursoParaActualizar != null) {
                cursoParaActualizar.setNombre(nombreField.getText());
                cursoParaActualizar.setActivo(activoCheckBox.isSelected());
                Curso cursoAntiguo = cursoDAO.buscarPorId(id); // O mantener una copia
                cursoDAO.actualizar(cursoAntiguo, cursoParaActualizar);
                JOptionPane.showMessageDialog(this, "Curso actualizado con éxito.");
                cargarCursos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Curso no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido para actualizar.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar curso: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarCurso() {
        try {
            double id = Double.parseDouble(idField.getText());
            Curso curso = cursoDAO.buscarPorId(id);
            
            if (curso != null) {
                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar el curso: " + curso.getNombre() + "?",
                        "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    cursoDAO.eliminar(curso);
                    JOptionPane.showMessageDialog(this, "Curso eliminado.");
                    cargarCursos();
                    limpiarCampos();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Curso no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido para eliminar.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar curso: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        idField.setText("");
        nombreField.setText("");
        activoCheckBox.setSelected(false);
    }
}