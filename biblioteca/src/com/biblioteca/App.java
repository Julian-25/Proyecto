package com.biblioteca;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class App {
	private static final String controlador = "com.mysql.cj.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/bibliotecabase";
	private static final String usuario = "root";
	private static final String contraseña = "Julian1025";
	
	public Connection conectar() {
		Connection conexion = null;
    	try {
			Class.forName(controlador);
			conexion = DriverManager.getConnection(url, usuario, contraseña);
			System.out.println("Conexion exitosa......");
		} catch (ClassNotFoundException e) {
			System.out.println("Error: al cargar el controlador");
			e.printStackTrace();
		}catch(SQLException e) {
			System.out.println("Ocurrio un error con la base de datos");
			e.printStackTrace();
			
		}
    	return conexion;
	}
    private static Usuario usuarioActual;
    
    private int generarNuevoIdUsuario(String nombre, String documento) {
        String sql = "INSERT INTO usuarios (nombre, documento) VALUES (?, ?)";
        try (Connection conexion = this.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
             
            pstmt.setString(1, nombre);
            pstmt.setString(2, documento);
            pstmt.executeUpdate();
            
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al generar un nuevo ID: " + e.getMessage());
        }
        return -1; 
    }



    public void registrarUsuario(String nombre, String documento){
        String sql = "INSERT INTO usuarios (nombre, documento) VALUES(?, ?)";
        try(Connection conexion = this.conectar();
            PreparedStatement pstmt = conexion.prepareStatement(sql)){
                pstmt.setString(1, nombre);
                pstmt.setString(2, documento);
                pstmt.executeUpdate();
                System.out.println("Usuario registrado exitosamente.");
            
        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " +e.getMessage());
        }
    }

    public void listarLibros(){
        String sql = "SELECT nombre, autor, categoria, disponible FROM libros";
        try(Connection conexion = this.conectar(); 
        java.sql.Statement stmt = conexion.createStatement(); 
        ResultSet rs = stmt.executeQuery(sql) ) {
            while (rs.next()) {
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Autor: " + rs.getString("autor"));
                System.out.println("Categoría: " + rs.getString("categoria"));
                System.out.println("Disponible: " + (rs.getBoolean("disponible") ? "Sí" : "No"));
                System.out.println("----------------------------");
                
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar libros: " +e.getMessage());
        }

    }

    public void prestarLibro(int idLibro) {
        String sqlActualizar = "UPDATE libros SET disponible = FALSE WHERE id = ? AND disponible = TRUE";
        String sqlRegistrarPrestamo = "INSERT INTO prestamos (id_usuario, id_libro) VALUES (?, ?)";
        
        try (Connection conexion = this.conectar();
             PreparedStatement pstmtActualizar = conexion.prepareStatement(sqlActualizar);
             PreparedStatement pstmtPrestamo = conexion.prepareStatement(sqlRegistrarPrestamo)) {
            
            
            pstmtActualizar.setInt(1, idLibro);
            int filasActualizadas = pstmtActualizar.executeUpdate();
            
            if (filasActualizadas > 0) {
                System.out.println("El libro con ID " + idLibro + " ha sido prestado.");
                
               
                pstmtPrestamo.setInt(1, usuarioActual.id); 
                pstmtPrestamo.setInt(2, idLibro); 
                pstmtPrestamo.executeUpdate();
                
                System.out.println("El préstamo ha sido registrado.");
            } else {
                System.out.println("El libro no está disponible o no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al prestar el libro: " + e.getMessage());
        }
    }

    
    

    public void devolverLibro(int idLibro) {
        String sqlActualizar = "UPDATE libros SET disponible = TRUE WHERE id = ? AND disponible = FALSE";
        String sqlRegistrarDevolucion = "INSERT INTO devoluciones (id_usuario, id_libro) VALUES (?, ?)";
        
        try (Connection conexion = this.conectar();
             PreparedStatement pstmtActualizar = conexion.prepareStatement(sqlActualizar);
             PreparedStatement pstmtDevolucion = conexion.prepareStatement(sqlRegistrarDevolucion)) {
            
            
            pstmtActualizar.setInt(1, idLibro);
            int filasActualizadas = pstmtActualizar.executeUpdate();
            
            if (filasActualizadas > 0) {
                System.out.println("El libro con ID " + idLibro + " ha sido devuelto.");
                
                
                pstmtDevolucion.setInt(1, usuarioActual.id);
                pstmtDevolucion.setInt(2, idLibro); 
                pstmtDevolucion.executeUpdate();
                
                System.out.println("La devolución ha sido registrada.");
            } else {
                System.out.println("El libro no estaba prestado o no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al devolver el libro: " + e.getMessage());
        }
    }


    public void registrarPrestamo(int idUsuario, String nombreLibro) {
        String sqlPrestamo = "INSERT INTO prestamos (id_usuario, nombre_libro, fecha_prestamo) VALUES (?, ?, NOW())";
        try (Connection conexion = this.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sqlPrestamo)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setString(2, nombreLibro);
            pstmt.executeUpdate();
            System.out.println("El préstamo ha sido registrado.");
        } catch (SQLException e) {
            System.out.println("Error al registrar el préstamo: " + e.getMessage());
        }
    }
    public void listarPrestamos(int idUsuario) {
        String sql = "SELECT nombre_libro, fecha_prestamo FROM prestamos WHERE id_usuario = ?";
        try (Connection conexion = this.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Libro: " + rs.getString("nombre_libro"));
                System.out.println("Fecha de préstamo: " + rs.getDate("fecha_prestamo"));
                System.out.println("----------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los préstamos: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
    	App app = new App();
    	Connection conexion = app.conectar();
    	if (conexion != null) {
    	    System.out.println("La conexión al servidor SQL fue exitosa.");
    	} else {
    	    System.out.println("No se pudo establecer conexión con el servidor SQL.");
    	}
        Scanner scanner = new Scanner(System.in);
        ArrayList <Libro> libros = new ArrayList<>();
        libros.add(new Libro(1,"100 años de soledad", "Gabriel García Márquez", "Novela", true));
        libros.add(new Libro(2,"Don Quijote de la Mancha", "Miguel de Cervantes", "Novela", true));
        libros.add(new Libro(3,"Crimen y castigo", "Fiódor Dostoyevski", "Novela psicológica", true));
        libros.add(new Libro(4,"Orgullo y prejuicio", "Jane Austen", "Romance", true));
        libros.add(new Libro(5,"1984", "George Orwell", "Distopía", true));
        libros.add(new Libro(6,"El señor de los anillos", "J.R.R. Tolkien", "Fantasía", true));
        libros.add(new Libro(7,"Matar a un ruiseñor", "Harper Lee", "Drama", true));
        libros.add(new Libro(8,"Cumbres borrascosas", "Emily Brontë", "Gótico", true));
        libros.add(new Libro(9,"El gran Gatsby", "F. Scott Fitzgerald", "Novela", true));
        libros.add(new Libro(10,"La metamorfosis", "Franz Kafka", "Ficción", true));
        libros.add(new Libro(11,"Harry Potter y la piedra filosofal", "J.K. Rowling", "Fantasía", true));
        boolean usuarioAccedido = false;

        int opciones = 0;

        HashMap<String, Usuario> usuarios = new HashMap<>();
        usuarios.put("192428", new Usuario("Admin", "192428", 0,1));
        while (opciones != 4 && !usuarioAccedido ) {
            System.out.println("Bienvenido");
            System.out.println("Opciones:");
            System.out.println("(1) Listado de libros");
            System.out.println("(2) Crear un nuevo usuario");
            System.out.println("(3) Iniciar sesion");
            System.out.println("(4) Salir");
    
    
            opciones = scanner.nextInt();
            scanner.nextLine();
    
            switch (opciones) {
                case 1:
                System.out.println("Libros disponibles: ");
                        for (Libro libro : libros) {
                System.out.println("╔════════════════════════════════════════════════════╗");
                System.out.printf("║ %-50s ║\n", "Nombre: " + libro.nombre);
                System.out.printf("║ %-50s ║\n", "Autor: " + libro.autor);
                System.out.printf("║ %-50s ║\n", "Categoría: " + libro.categoria);
                System.out.printf("║ %-50s ║\n", "Disponible: " + (libro.disponible ? "Sí" : "No"));
                System.out.println("╚════════════════════════════════════════════════════╝");
                System.out.println("<-------------------------------------------------------->");
                app.listarLibros();
            }
            break;
    
            case 2:
            System.out.println("Ingrse el nombre del usuario.");
            String nombreUsuario = scanner.nextLine();
    
            System.out.println("Ingrese el documento del usuario.");
            String documentoUsuario = scanner.nextLine();
    
            if (usuarios.containsKey(documentoUsuario)) {
                System.out.println("El usuario ya existe.");
                
            }else{
                int nuevoId = app.generarNuevoIdUsuario(nombreUsuario, documentoUsuario);
            	usuarios.put(documentoUsuario, new Usuario(nombreUsuario, documentoUsuario, 0, nuevoId ));
                System.out.println("Usuario creado exitosamente..");
                System.out.println("Nombre: " +nombreUsuario +" , documento: " +documentoUsuario);
                app.registrarUsuario(nombreUsuario, documentoUsuario);

            }

            break;
    
            case 3:
            System.out.println("Ingrese su documento: ");
            String documento = scanner.nextLine();
            if (usuarios.containsKey(documento)) {
                usuarioActual = buscarUsuarioPorDocumento(usuarios, documento);
                System.out.println("¡Bienvenido, " +usuarioActual.nombre +"!");
                usuarioAccedido = true;
                }else{
                    System.out.println("Error: usuario no encontrado");
                }
    
            break;
    
            case 4:
            System.out.println("Saliendo del sistema...");
            
            break;
    
                default:
                try {
                    throw new IllegalArgumentException("Opcion no valida, por favor elija una opcion valida.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                    break;
            }
            
        }
        int opcionAcceso = 0;

        while (opcionAcceso != 4 && usuarioAccedido == true) {
            System.out.println("Bienvenido" );
            System.out.println("Opciones:");
            System.out.println("(1) Listado de libros.");
            System.out.println("(2) Sacar libro.");
            System.out.println("(3) Devolver libro.");
            System.out.println("(4) Salir");

            int opcionUsuario = scanner.nextInt();

            switch (opcionUsuario) {
                case 1:
                System.out.println("Libros disponibles: ");
                for (Libro libro : libros) {
        System.out.println("╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.printf("║ %-50s ║\n", "Nombre: " + libro.nombre);
        System.out.printf("║ %-50s ║\n", "Autor: " + libro.autor);
        System.out.printf("║ %-50s ║\n", "Categoría: " + libro.categoria);
        System.out.printf("║ %-50s ║\n", "Disponible: " + (libro.disponible ? "Sí" : "No"));
        System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
        System.out.println("<--------------------------------------------------------------------------->");
                }
                    
                    break;

                case 2:
                    System.out.println("Libros disponibles para préstamo: ");
                    int index = 1;
                    ArrayList<Libro> librosDisponibles = new ArrayList<>();
                    for (Libro libro : libros) {
                        if (libro.disponible) {
                            librosDisponibles.add(libro);
                            System.out.println(index + ". " + libro.nombre + " - " + libro.autor);
                            index++;
                        }
                    }
                    
                    if (librosDisponibles.isEmpty()) {
                        System.out.println("No hay libros disponibles en este momento.");
                        break;
                    }

                    System.out.println("Ingrese el número del libro que desea sacar");
                    int seleccion = scanner.nextInt();
                    scanner.nextLine();

                    if (seleccion < 1 || seleccion > librosDisponibles.size()) {
                        System.out.println("Selección inválida, intente de nuevo.");
                        break;
                    }

                    Libro libroSeleccionado = librosDisponibles.get(seleccion - 1);
                    app.prestarLibro(libroSeleccionado.idlibro);
                    libroSeleccionado.cambiarDisponibilidad(false);
                    break;

                case 3:
                    System.out.println("Libros en tu posesión: ");
                    int indexDevolver = 1;
                    ArrayList<Libro> librosPrestados = new ArrayList<>();
                    for (Libro libro : libros) {
                        if (!libro.disponible) {
                            librosPrestados.add(libro);
                            System.out.println(indexDevolver + ". " + libro.nombre + " - " + libro.autor);
                            indexDevolver++;
                        }
                    }

                    if (librosPrestados.isEmpty()) {
                        System.out.println("No tienes libros para devolver.");
                        break;  
                    }

                    System.out.println("Ingresa el número del libro que deseas devolver");
                    int seleccionDevolver = scanner.nextInt();
                    scanner.nextLine();

                    if (seleccionDevolver < 1 || seleccionDevolver > librosPrestados.size()) {
                        System.out.println("Selección inválida. Intente de nuevo.");
                        break;
                    }

                    Libro libroDevuelto = librosPrestados.get(seleccionDevolver - 1);
                    app.devolverLibro(libroDevuelto.idlibro);  
                    break;
                case 4:
                System.out.println("Saliendo del sistema....");
                opcionAcceso = 4;
                break;
            
                default:
                    break;
            }
            
        }


scanner.close();

    }
        private static Usuario buscarUsuarioPorDocumento(HashMap<String, Usuario> usuarios, String documento) {
            
            return usuarios.get(documento);
        }
}