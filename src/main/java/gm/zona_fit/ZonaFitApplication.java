package gm.zona_fit;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;


//@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {

	String nl = System.lineSeparator();

	@Autowired
	private IClienteServicio clienteServicio;

	private static final Logger logger = LoggerFactory.getLogger(ZonaFitApplication.class);
	public static void main(String[] args) {
		logger.info("Iniciando la aplicacion");

		SpringApplication.run(ZonaFitApplication.class, args);

		logger.info("Aplicacion finalizada\n");
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info(nl+"*** Aplicacion Zona fit (GYM) ***"+nl);
		zonaFitApp();

	}


	public void zonaFitApp(){
		Scanner escaner = new Scanner(System.in);
		boolean salir = false;
		while (!salir){
			var opcion = mostrarMenu(escaner, logger);
			salir = ejecutarOpcion(escaner, opcion, logger);
		}
	}

	private Integer mostrarMenu(Scanner escaner, Logger logger){
		logger.info("""
				\n1. Listar los clientes
				2. Buscar el cliente por el id
				3. Guardar cliente
				4. Modificar cliente
				5. Eliminar Cliente
				6. salir
				""");
		Integer opcion = escaner.nextInt();
		return opcion;
	}
	private boolean ejecutarOpcion(Scanner escaner, Integer opcion, Logger logger){
		var salir = false;
		switch (opcion){
			case 1 -> {
				logger.info(nl+"--- Listado de clientes ---"+nl);
				List<Cliente> clientes =clienteServicio.listarClientes();
				clientes.forEach(cliente -> logger.info(cliente.toString()+nl));
			}
			case 2 -> {
				logger.info(nl+"--- Buscar cliente por id ---");
				logger.info(nl+"Ingrese el id del cliente que quiere buscar: ");
				var idCliente = escaner.nextInt();
				var cliente = clienteServicio.buscarClientePorId(idCliente);
				if (cliente != null){
					logger.info(nl+cliente.toString());
				}else logger.info("No existe el cliente con el id: "+idCliente);
			}
			case 3 -> {
				logger.info("--- Guardar Cliente ---");
				logger.info("Introduzca los datos del nuevo cliente"+nl+"Nombre: ");
				escaner.nextLine();
				var nombre = escaner.nextLine();

				logger.info(nl+"Apellido: ");
				var apellido = escaner.nextLine();
				logger.info(nl+"Membresia: ");
				Integer membresia = escaner.nextInt();
				Cliente cliente = new Cliente();
				cliente.setNombre(nombre);
				cliente.setApellido(apellido);
				cliente.setMembresia(membresia);
				clienteServicio.guardarCliente(cliente);
				logger.info("Cliente agregado"+ cliente + nl);
			}
			case 4 ->{
				logger.info("--- Modificar cliente ---"+nl);
				logger.info("id cliente: ");
				var idCliente = escaner.nextInt();
				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
				if (cliente!= null){
					logger.info("Nombre: ");
					escaner.nextLine();
					var nombre = escaner.nextLine();
					logger.info("Apellido: ");
					var apellido = escaner.nextLine();
					logger.info("Membresia: ");
					Integer membresia = escaner.nextInt();
					cliente.setNombre(nombre);
					cliente.setApellido(apellido);
					cliente.setMembresia(membresia);
					clienteServicio.guardarCliente(cliente);
					logger.info("Cliente modificado: "+ cliente + nl);
				}else
					logger.info("El cliente a modificar no existe, id: "+ idCliente);
			}
			case 5 ->{
				logger.info("Ingrese el id del cliente a eliminar: ");
				Integer idCliente = escaner.nextInt();
				escaner.nextLine();
				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
				if (cliente != null){
					clienteServicio.eliminarCliente(cliente);
					Cliente clienteEliminado = clienteServicio.buscarClientePorId(idCliente);
					if (clienteEliminado == null)logger.info("El cliente se ha eliminado de forma exitosa");
					else logger.info("Error El cliente aun no se ha eliminado");
				}
				else {
					logger.info("El cliente con id "+ idCliente+" no existe");
				}
			}
			case 6 ->{
				logger.info("Hasta pronto.."+nl+nl);
				return true;
			}
			default -> logger.info("Opcion no reconocida");
		}
		return salir;
	}


}
