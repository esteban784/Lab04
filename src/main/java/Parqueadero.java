import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class Parqueadero extends JFrame {
    private JTable tablaVehiculos;
    private DefaultTableModel modeloTabla;
    private List<Vehiculo> listaVehiculos;
    private int numeroVehiculo = 1;

    public Parqueadero() {
        setTitle("Parqueadero Público");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        listaVehiculos = new LinkedList<>();

        String[] columnas = {"N°", "Placa", "Tipo", "Hora de Ingreso", "Valor por Minuto"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaVehiculos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaVehiculos);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar Vehículo");
        JButton btnVerDosRuedas = new JButton("Ver Vehículos 2 Ruedas");
        JButton btnVerCuatroRuedas = new JButton("Ver Vehículos 4 Ruedas");
        JButton btnEliminar = new JButton("Eliminar Vehículo");
        JButton btnCantidad = new JButton("Cantidad y Total a Pagar");
        JButton btnSalir = new JButton("Salir");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnVerDosRuedas);
        panelBotones.add(btnVerCuatroRuedas);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCantidad);
        panelBotones.add(btnSalir);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarVehiculo();
            }
        });

        btnVerDosRuedas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verVehiculosDosRuedas();
            }
        });

        btnVerCuatroRuedas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verVehiculosCuatroRuedas();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarVehiculo();
            }
        });

        btnCantidad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarCantidadYTotal();
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void agregarVehiculo() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
        String[] tipos = {"Bicicleta", "Ciclomotor", "Motocicleta", "Carro"};
        String tipo = (String) JOptionPane.showInputDialog(null, "Seleccione el tipo de vehículo:",
                "Tipo de Vehículo", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);

        if (placa != null && tipo != null) {
            Vehiculo vehiculo = new Vehiculo(numeroVehiculo++, placa, tipo, LocalDateTime.now());
            listaVehiculos.add(vehiculo);
            actualizarTabla();
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        for (Vehiculo vehiculo : listaVehiculos) {
            Object[] fila = {vehiculo.getNumero(), vehiculo.getPlaca(), vehiculo.getTipo(), vehiculo.getHoraIngreso(), vehiculo.getTarifaPorMinuto()};
            modeloTabla.addRow(fila);
        }
    }

    private void verVehiculosDosRuedas() {
        StringBuilder sb = new StringBuilder("Vehículos de 2 ruedas:\n");
        double total = 0;
        for (Vehiculo vehiculo : listaVehiculos) {
            if (vehiculo.esDosRuedas()) {
                long minutos = vehiculo.getMinutosEstacionado();
                double valor = minutos * vehiculo.getTarifaPorMinuto();
                sb.append(vehiculo.getPlaca()).append(" - Minutos: ").append(minutos).append(" - Total: ").append(valor).append("\n");
                total += valor;
            }
        }
        sb.append("Total a pagar por vehículos de 2 ruedas: ").append(total).append(" COP");
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void verVehiculosCuatroRuedas() {
        StringBuilder sb = new StringBuilder("Vehículos de 4 ruedas:\n");
        double total = 0;
        for (Vehiculo vehiculo : listaVehiculos) {
            if (!vehiculo.esDosRuedas()) {
                long minutos = vehiculo.getMinutosEstacionado();
                double valor = minutos * vehiculo.getTarifaPorMinuto();
                sb.append(vehiculo.getPlaca()).append(" - Minutos: ").append(minutos).append(" - Total: ").append(valor).append("\n");
                total += valor;
            }
        }
        sb.append("Total a pagar por vehículos de 4 ruedas: ").append(total).append(" COP");
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void eliminarVehiculo() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo a eliminar:");
        Vehiculo vehiculoAEliminar = null;
        for (Vehiculo vehiculo : listaVehiculos) {
            if (vehiculo.getPlaca().equals(placa)) {
                vehiculoAEliminar = vehiculo;
                break;
            }
        }
        if (vehiculoAEliminar != null) {
            listaVehiculos.remove(vehiculoAEliminar);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Vehículo eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Vehículo no encontrado.");
        }
    }

    private void mostrarCantidadYTotal() {
        int cantidad = listaVehiculos.size();
        double total = 0;
        for (Vehiculo vehiculo : listaVehiculos) {
            long minutos = vehiculo.getMinutosEstacionado();
            total += minutos * vehiculo.getTarifaPorMinuto();
        }
        JOptionPane.showMessageDialog(this, "Cantidad de vehículos: " + cantidad + "\nTotal a pagar: " + total + " COP");
    }

    class Vehiculo {
        private int numero;
        private String placa;
        private String tipo;
        private LocalDateTime horaIngreso;

        public Vehiculo(int numero, String placa, String tipo, LocalDateTime horaIngreso) {
            this.numero = numero;
            this.placa = placa;
            this.tipo = tipo;
            this.horaIngreso = horaIngreso;
        }

        public int getNumero() {
            return numero;
        }

        public String getPlaca() {
            return placa;
        }

        public String getTipo() {
            return tipo;
        }

        public LocalDateTime getHoraIngreso() {
            return horaIngreso;
        }

        public double getTarifaPorMinuto() {
            switch (tipo) {
                case "Bicicleta":
                case "Ciclomotor":
                    return 20.0;
                case "Motocicleta":
                    return 30.0;
                case "Carro":
                    return 60.0;
                default:
                    return 0.0;
            }
        }

        public long getMinutosEstacionado() {
            return ChronoUnit.MINUTES.between(horaIngreso, LocalDateTime.now());
        }

        public boolean esDosRuedas() {
            return tipo.equals("Bicicleta") || tipo.equals("Ciclomotor") || tipo.equals("Motocicleta");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Parqueadero().setVisible(true);
            }
        });
    }
}
