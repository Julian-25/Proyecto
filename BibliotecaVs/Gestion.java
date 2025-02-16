import java.util.ArrayList;
import java.util.Scanner;


class Libro {
    String nombre;
    String autor;
    String categoria;
    boolean disponible;

    public Libro(String nombre, String autor, String categoria, boolean disponible){
        this.nombre = nombre;
        this.autor = autor;
        this.categoria = categoria;
        this.disponible = true;
    }
public void cambiarDisponibilidad(boolean disponible){
    this.disponible = disponible;
}
@Override
    public String toString(){
        return "Nombre: " + nombre + ", Autor: " + autor + ", Categoría: " + categoria + ", Disponible: " + (disponible ? "Sí" : "No");
    }


}

class Usuario {
    String nombre;
    String documento;
    int prestamosPendientes;

    public Usuario(String nombre, String documento, int prestamosPendientes){
        this.nombre = nombre;
        this.documento = documento;
        this.prestamosPendientes = prestamosPendientes;

    }
    

    public void prestarLibro(Libro libro){
        try {
            if (libro == null) {
                    throw new IllegalArgumentException("El libro no existe");

            }else if (libro.disponible) {
                    throw new IllegalStateException("El libro: " +libro.nombre +" ya esta prestado.");
            }
            libro.disponible = false;
            prestamosPendientes++;
            System.out.println("El libro: " +libro.nombre +" ha sido prestado.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }

    }

    public void devolverLibro(Libro libro){
try {
    if (libro == null) {
        throw new IllegalArgumentException("El libro no existe");
        
    }else if(libro.disponible){
        throw new IllegalStateException("El libro: " +libro.nombre +" no esta prestado.");
    }
    libro.disponible = true;
    prestamosPendientes --;
    System.out.println("El libro: " +libro.nombre +" ha sido devuelto");
    
} catch (IllegalArgumentException | IllegalStateException e) {
    System.out.println(e.getMessage());
}
    }




}

public class Gestion {
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

        ArrayList<Usuario> usuarios = new ArrayList<>();
        System.out.println("Bienvenido");
        System.out.println("Opciones:");
        System.out.println("(1) Listado de libros");
        System.out.println("(2) Crear un nuevo usuario");
        System.out.println("(3) Iniciar sesion");
        System.out.println("(4) Salir");


        int opciones = scanner.nextInt();
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

        usuarios.add(new Usuario(nombreUsuario, documentoUsuario, 0));
        System.out.println("Usuario creado existosamente: ");
        System.out.println("Nombre: " +nombreUsuario +", documento: " +documentoUsuario);
        break;

        case 3:
        System.out.println("Ingrese su documento: ");
        String documento = scanner.nextLine();
        usuarioActual = buscarUsuarioPorDocumento(usuarios, documento);
        if (usuarioActual != null) {
            System.out.println("¡Bienvenido, " +usuarioActual.nombre +"!");
            
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
        private static Usuario buscarUsuarioPorDocumento(ArrayList<Usuario> usuarios, String documento) {
            for (Usuario usuario : usuarios) {
                if (usuario.documento.equals(documento)) {
                    return usuario;
                }
            }
            return null;
        }
}