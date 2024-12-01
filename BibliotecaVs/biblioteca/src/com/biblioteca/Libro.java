package com.biblioteca;
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
