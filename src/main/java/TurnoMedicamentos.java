import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.PriorityQueue;
import java.util.Comparator;

public class TurnoMedicamentos extends JFrame {
    private JTextField txtNombre, txtEdad;
    private JComboBox<String> cmbAfiliacion, cmbCondicion;
    private JTextArea txtAreaTurnos;
    private PriorityQueue<Paciente> colaPacientes;
    private Paciente pacienteEnCurso;
    
    // Constructor
    public TurnoMedicamentos() {
        setTitle("Asignación de Turnos EPS");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Cola para los turnos de pacientes con prioridad
        colaPacientes = new PriorityQueue<>(new PacienteComparator());

        // Panel para la entrada de datos del paciente
        JPanel panelEntrada = new JPanel(new GridLayout(5, 2));
        
        panelEntrada.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelEntrada.add(txtNombre);
        
        panelEntrada.add(new JLabel("Edad:"));
        txtEdad = new JTextField();
        panelEntrada.add(txtEdad);
        
        panelEntrada.add(new JLabel("Afiliación:"));
        cmbAfiliacion = new JComboBox<>(new String[]{"POS", "PC"});
        panelEntrada.add(cmbAfiliacion);
        
        panelEntrada.add(new JLabel("Condición Especial:"));
        cmbCondicion = new JComboBox<>(new String[]{"Ninguna", "Tercera Edad", "Menor de 12", "Embarazo", "Limitación Motriz"});
        panelEntrada.add(cmbCondicion);
        
        JButton btnAgregar = new JButton("Agregar Paciente");
        panelEntrada.add(btnAgregar);

        // Área para mostrar los turnos
        txtAreaTurnos = new JTextArea();
        txtAreaTurnos.setEditable(false);
        
        JScrollPane scrollTurnos = new JScrollPane(txtAreaTurnos);
        add(panelEntrada, BorderLayout.NORTH);
        add(scrollTurnos, BorderLayout.CENTER);

        // Acción del botón Agregar Paciente
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarPaciente();
                actualizarTurnos();
            }
        });

        // Timer para simular la atención de pacientes cada 5 segundos
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarTurno();
                actualizarTurnos();
            }
        });
        timer.start();
    }

    // Método para agregar paciente a la cola con prioridad
    private void agregarPaciente() {
        String nombre = txtNombre.getText();
        int edad = Integer.parseInt(txtEdad.getText());
        String afiliacion = (String) cmbAfiliacion.getSelectedItem();
        String condicion = (String) cmbCondicion.getSelectedItem();

        Paciente nuevoPaciente = new Paciente(nombre, edad, afiliacion, condicion);
        colaPacientes.offer(nuevoPaciente);

        txtNombre.setText("");
        txtEdad.setText("");
    }

    // Método para procesar el turno actual
    private void procesarTurno() {
        if (!colaPacientes.isEmpty()) {
            pacienteEnCurso = colaPacientes.poll();
            txtAreaTurnos.append("Turno de: " + pacienteEnCurso.getNombre() + " - Condición: " + pacienteEnCurso.getCondicion() + "\n");
        }
    }

    // Método para actualizar el área de turnos
    private void actualizarTurnos() {
        txtAreaTurnos.setText("Pacientes en espera:\n");
        for (Paciente p : colaPacientes) {
            txtAreaTurnos.append(p.getNombre() + " - Condición: " + p.getCondicion() + "\n");
        }
    }

    // Clase Paciente con prioridad
    class Paciente {
        private String nombre;
        private int edad;
        private String afiliacion;
        private String condicion;

        public Paciente(String nombre, int edad, String afiliacion, String condicion) {
            this.nombre = nombre;
            this.edad = edad;
            this.afiliacion = afiliacion;
            this.condicion = condicion;
        }

        public String getNombre() {
            return nombre;
        }

        public String getCondicion() {
            return condicion;
        }

        public int getPrioridad() {
            // Asignar prioridades: menor número, mayor prioridad
            switch (condicion) {
                case "Tercera Edad":
                case "Menor de 12":
                    return 1;  // Mayor prioridad
                case "Embarazo":
                case "Limitación Motriz":
                    return 2;
                default:
                    return 3;
            }
        }
    }

    // Comparator para manejar la prioridad de los pacientes en la cola
    class PacienteComparator implements Comparator<Paciente> {
        @Override
        public int compare(Paciente p1, Paciente p2) {
            return Integer.compare(p1.getPrioridad(), p2.getPrioridad());
        }
    }

    // Método principal para iniciar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TurnoMedicamentos().setVisible(true);
            }
        });
    }
}
