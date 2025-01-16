package gm.zona_fit.gui;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.ClienteServicio;
import gm.zona_fit.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class ZonaFitForma extends JFrame{
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField membresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    IClienteServicio clienteServicio;
    private DefaultTableModel tablaModeloCliente;
    private Integer idCliente;

    @Autowired
    public ZonaFitForma(ClienteServicio clienteServicio){
        this.clienteServicio = clienteServicio;
        iniciarForma();
        guardarButton.addActionListener(e -> guardarCliente());
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarClienteSeleccionado();
            }
        });
        eliminarButton.addActionListener(e -> {
            eliminarCliente();
        });


        limpiarButton.addActionListener(e -> {
            limpiarDatos();
        });
    }

    private void iniciarForma(){
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    private void createUIComponents() {
        //this.tablaModeloCliente = new DefaultTableModel(0, 4);

        //Para bloquear la edicion de las celdas
        this.tablaModeloCliente = new DefaultTableModel(0, 4){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        String[] cabecero = {"Id", "Nombre", "Apellido", "Membresia"};
        this.tablaModeloCliente.setColumnIdentifiers(cabecero);
        this.clientesTabla = new JTable(tablaModeloCliente);
        //restringir la doble seleccion
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarClientes();
    }

    private void listarClientes(){
        this.tablaModeloCliente.setRowCount(0);
        var clientes = this.clienteServicio.listarClientes();
        clientes.forEach(cliente -> {
            Object[] renglonCliente = {cliente.getId(), cliente.getNombre(), cliente.getApellido(), cliente.getMembresia()};
            this.tablaModeloCliente.addRow(renglonCliente);
        });

    }

    private void guardarCliente(){
        if(nombreTexto.getText().equals("")){
            mostrarMensaje("Proporciona un nombre");
            nombreTexto.requestFocusInWindow();
            return;
        }
        if(membresiaTexto.getText().equals("")){
            mostrarMensaje("Proporciona el valor de la membresia");
            membresiaTexto.requestFocusInWindow();
            return;
        }

        var nombreUsuario = nombreTexto.getText();
        var apellido = apellidoTexto.getText();
        var membresia = Integer.parseInt(membresiaTexto.getText());

        Cliente cliente = new Cliente(this.idCliente, nombreUsuario, apellido, membresia);
        this.clienteServicio.guardarCliente(cliente);

        if(this.idCliente == null){
            mostrarMensaje("Se agrego el nuevo cliente");
        }
        else{
            mostrarMensaje("Se actualizo el cliente");
        }
        limpiarDatos();
        listarClientes();
    }

    private void mostrarMensaje(String texto){
        JOptionPane.showMessageDialog(this,texto);
    }
    private void limpiarDatos(){
        nombreTexto.setText("");
        apellidoTexto.setText("");
        membresiaTexto.setText("");
        //Limpiar el id cliente
        this.idCliente = null;
        //Deseleccionar el registro de la tabla
        this.clientesTabla.getSelectionModel().clearSelection();
    }

    private void eliminarCliente(){
        if(this.idCliente == null){
            mostrarMensaje("No se ha seleccionado ningun cliente a eliminar. \nPor favor seleccione uno.");
        }
        else {
            Cliente cliente = new Cliente();
            cliente.setId(this.idCliente);
            this.clienteServicio.eliminarCliente(cliente);
            mostrarMensaje("El cliente ha sido eliminado de forma exitosa.");
            limpiarDatos();
            listarClientes();
        }
    }

    private void cargarClienteSeleccionado(){
        var renglon = clientesTabla.getSelectedRow();
        if(renglon != -1){//-1 seignifica que no se ha seleccionado ningun registro
            var id = clientesTabla.getModel().getValueAt(renglon,0).toString();
            this.idCliente = Integer.parseInt(id);
            var nombre = clientesTabla.getModel().getValueAt(renglon, 1).toString();
            this.nombreTexto.setText(nombre);
            var apellido = clientesTabla.getModel().getValueAt(renglon, 2).toString();
            this.apellidoTexto.setText(apellido);
            var membresia = clientesTabla.getModel().getValueAt(renglon, 3).toString();
            this.membresiaTexto.setText(membresia);
        }
    }
}
