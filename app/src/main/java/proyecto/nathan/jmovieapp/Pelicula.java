package proyecto.nathan.jmovieapp;

import java.io.Serializable;
import java.util.Objects;

public class Pelicula implements Serializable {
    String titulo;
    String anio;
    String duracion;
    String lanzamiento;
    String lenguaje;
    String pais;
    String imagen;
    String descripcion;
    String guion;
    String director;
    String genero;

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Pelicula(String titulo, String anio, String duracion, String lanzamiento, String lenguaje, String pais, String imagen, String descripcion, String guion, String director, String genero) {
        this.titulo = titulo;
        this.anio = anio;
        this.duracion = duracion;
        this.lanzamiento = lanzamiento;
        this.lenguaje = lenguaje;
        this.pais = pais;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.genero = genero;
        this.guion = guion;
        this.director = director;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getLanzamiento() {
        return lanzamiento;
    }

    public void setLanzamiento(String lanzamiento) {
        this.lanzamiento = lanzamiento;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getGuion() {
        return guion;
    }

    public void setGuion(String guion) {
        this.guion = guion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return Objects.equals(titulo, pelicula.titulo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(titulo);
    }
}


