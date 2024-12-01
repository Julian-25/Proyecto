package com.biblioteca;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
public class App {
    private static Usuario usuarioActual;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList <Libro> libros = new ArrayList<>();
        libros.add(new Libro("100 años de soledad", "Gabriel García Márquez", "Novela", true));
        libros.add(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", "Novela", true));
        libros.add(new Libro("Crimen y castigo", "Fiódor Dostoyevski", "Novela psicológica", true));
        libros.add(new Libro("Orgullo y prejuicio", "Jane Austen", "Romance", true));
        libros.add(new Libro("1984", "George Orwell", "Distopía", true));
        libros.add(new Libro("El señor de los anillos", "J.R.R. Tolkien", "Fantasía", true));
        libros.add(new Libro("Matar a un ruiseñor", "Harper Lee", "Drama", true));
        libros.add(new Libro("Cumbres borrascosas", "Emily Brontë", "Gótico", true));
        libros.add(new Libro("El gran Gatsby", "F. Scott Fitzgerald", "Novela", true));
        libros.add(new Libro("La metamorfosis", "Franz Kafka", "Ficción", true));
        libros.add(new Libro("Harry Potter y la piedra filosofal", "J.K. Rowling", "Fantasía", true));
        boolean usuarioAccedido = false;

        int opciones = 0;

        HashMap<String, Usuario> usuarios = new HashMap<>();
        usuarios.put("192428", new Usuario("Admin", "192428", 0));
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
            System.out.println("Ingrse el nombre del usuario.");
            String nombreUsuario = scanner.nextLine();
    
            System.out.println("Ingrese el documento del usuario.");
            String documentoUsuario = scanner.nextLine();
    
            if (usuarios.containsKey(documentoUsuario)) {
                System.out.println("El usuario ya existe.");
                
            }else{
                usuarios.put(documentoUsuario, new Usuario(nombreUsuario, documentoUsuario, 0));
                System.out.println("Usuario creado exitosamente..");
                System.out.println("Nombre: " +nombreUsuario +" , documento: " +documentoUsuario);

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

        while (opcionAcceso != 4) {
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
                    System.out.println("Libros disponibles para prestamo: ");
                    int index = 1;
                    ArrayList <Libro> librosDisponibles = new ArrayList<>();
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

                    System.out.println("Ingrese el numero del libro que desea sacar");
                    int seleccion = scanner.nextInt();
                    scanner.nextLine();

                    if (seleccion< 1 || seleccion > librosDisponibles.size()) {
                        System.out.println("Seleccion invalida, intente de nuevo.");
                        break;
                        
                    }

                    Libro libroSeleccionado = librosDisponibles.get(seleccion-1);
                    libroSeleccionado.disponible = false;
                    usuarioActual.prestamosPendientes++;

                    System.out.println("Haz sacado el libro: " +libroSeleccionado.nombre);
                    break;
                case 3:
                    System.out.println("Libros en tu posesion: ");
                    int indexDevolver = 1;
                    ArrayList<Libro> librosPrestados = new ArrayList<>();
                    for (Libro libro : libros) {
                        if (!libro.disponible) {
                            librosPrestados.add(libro);
                            System.out.println(indexDevolver +". " +libro.nombre +" - " +libro.autor);
                            indexDevolver++;
                            
                        }

                        if (librosPrestados.isEmpty()) {
                            System.out.println("No tienes libros para devolver.");
                            break;
                            
                        }

                        System.out.println("Ingresa el numero del libro que deseas devolver");
                        int seleccionDevolver = scanner.nextInt();
                        scanner.nextLine();

                        if (seleccionDevolver < 1 || seleccionDevolver > librosPrestados.size()) {
                            System.out.println("Seleccion invalida. Intente de nuevo.");
                            break;
                            
                        }

                        Libro librodevuelto = librosPrestados.get(seleccionDevolver -1);


                        librodevuelto.disponible = true;

                        usuarioActual.prestamosPendientes--;
                        System.out.println("Haz devuelto el libro: " +librodevuelto.nombre);
                        opcionAcceso = 4;
                        break;
                        
                    }
                break;

                case 4:
                System.out.println("Saliendo del sistema.");
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