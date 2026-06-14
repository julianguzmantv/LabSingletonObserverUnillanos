package com.lab.Interfaces;

import java.sql.Timestamp;
import javax.swing.*;
import com.lab.DataAccessObj.CursoDAO;
import com.lab.DataAccessObj.CursosInscritosDAO;
import com.lab.DataAccessObj.EstudianteDAO;
import com.lab.Factory.DatesDB;
import com.lab.Factory.FactoryDAO;
import com.lab.Models.Curso;
import com.lab.Models.Estudiante;
import com.lab.connections.DBConnect;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class InterfazMain extends JFrame {
    private CursoDAO cursoDAO;
    private EstudianteDAO estudianteDAO;
    private CursosInscritosDAO inscripcionDAO;
    private JComboBox<Curso> comboCursos;
    private JComboBox<Estudiante> comboEstudiantes;
    private JTextField txtAno;
    private JTextField txtSemestre;
    private JButton btnInscribir;
    private InterfazInscribir tablaPanel; 
    private List<Curso> listaCursos;
    private List<Estudiante> listaEstudiantes;
    private InterfazConsola menuConsola;

    public InterfazMain(FactoryDAO factory, DBConnect conexion) {
        super("Gestionar las Inscripciones y Cursos");

        this.cursoDAO = factory.createCursoDAO();
        this.estudianteDAO = factory.createEstudianteDAO();
        this.inscripcionDAO = factory.createInscripcionesDAO();
        this.menuConsola = new InterfazConsola(conexion, estudianteDAO, cursoDAO);
        this.menuConsola.start();
        
        setLayout(new BorderLayout(10, 10));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Estudiantes", new EstudiantePanel(estudianteDAO));

        JPanel inscripcionesPanel = new JPanel(new BorderLayout(10, 10));
        
        this.tablaPanel = new InterfazInscribir(inscripcionDAO);
        
        inscripcionesPanel.add(configurarFormulario(), BorderLayout.NORTH);
        inscripcionesPanel.add(tablaPanel, BorderLayout.CENTER); 
        
        try {
            Timestamp fechaBD = DatesDB.getCurrentDateTime(
                    conexion.getConexion().getConexion()
            );

            JLabel lblFecha = new JLabel("Fecha BD: " + fechaBD);
            lblFecha.setHorizontalAlignment(SwingConstants.CENTER);
            lblFecha.setFont(new Font("Arial", Font.BOLD, 12));
            inscripcionesPanel.add(lblFecha, BorderLayout.SOUTH);

            System.out.println("Fecha de la base de datos: " + fechaBD);

        } catch (Exception e) {
            System.err.println("Error al obtener fecha de BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        tabs.addTab("Inscripciones", inscripcionesPanel);
        tabs.addTab("Cursos", new CursoPanel(cursoDAO));
        tabs.addTab("profesores", new JPanel());
        tabs.addTab("Reportes", new JPanel());

        add(tabs, BorderLayout.CENTER);

        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        if (tablaPanel != null) {
            tablaPanel.cargarDatosEnTabla();
            System.out.println("📊 Datos iniciales cargados en la tabla de inscripciones");
        } else {
            System.err.println("❌ tablaPanel es null - no se pueden cargar datos iniciales");
        }
    }

    private JPanel configurarFormulario() {
        JPanel formularioPanel = new JPanel(new GridLayout(5, 2, 10, 5)); // Cambiado a 5x2

        listaCursos = cursoDAO.obtenerTodos();
        listaEstudiantes = estudianteDAO.obtenerTodos();

        comboCursos = new JComboBox<>(listaCursos.toArray(new Curso[0]));
        comboEstudiantes = new JComboBox<>(listaEstudiantes.toArray(new Estudiante[0]));
        txtAno = new JTextField(10);
        txtSemestre = new JTextField(10);
        btnInscribir = new JButton("Inscribir Curso");

        comboCursos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Curso) {
                    setText(((Curso) value).getNombre());
                }
                return this;
            }
        });

        comboEstudiantes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Estudiante) {
                    setText(((Estudiante) value).getNombre() + " " + ((Estudiante) value).getApellidos());
                }
                return this;
            }
        });

        formularioPanel.add(new JLabel("Curso:"));
        formularioPanel.add(comboCursos);
        formularioPanel.add(new JLabel("Estudiante:"));
        formularioPanel.add(comboEstudiantes);
        formularioPanel.add(new JLabel("Año:"));
        formularioPanel.add(txtAno);
        formularioPanel.add(new JLabel("Semestre:"));
        formularioPanel.add(txtSemestre);
        formularioPanel.add(new JLabel("")); 
        formularioPanel.add(btnInscribir);

        return formularioPanel;
    }

    public void addInscribirListener(ActionListener listener) {
        btnInscribir.addActionListener(listener);
    }

    public Curso getCursoSeleccionado() {
        return (Curso) comboCursos.getSelectedItem();
    }

    public Estudiante getEstudianteSeleccionado() {
        return (Estudiante) comboEstudiantes.getSelectedItem();
    }

    public int getAno() {
        try {
            return Integer.parseInt(txtAno.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getSemestre() {
        try {
            return Integer.parseInt(txtSemestre.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void refrescarTabla() {
        if (tablaPanel != null) {
            tablaPanel.cargarDatosEnTabla();
            System.out.println("🔄 Tabla de inscripciones actualizada");
        } else {
            System.err.println(" No se puede refrescar - tablaPanel es null");
        }
    }

    public void limpiarCampos() {
        txtAno.setText("");
        txtSemestre.setText("");
        if (comboCursos.getItemCount() > 0) comboCursos.setSelectedIndex(0);
        if (comboEstudiantes.getItemCount() > 0) comboEstudiantes.setSelectedIndex(0);
    }

    public InterfazInscribir getTablaPanel() {
        return tablaPanel;
    }

    
}