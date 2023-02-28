package david.pablo.uv.es;

import java.util.Comparator;

public class Camping {
    private String Nombre;
    private String Categoria;
    private String Provincia;
    private String Municipio;
    private String Correo;

    public String getCategoria() {
        return Categoria;
    }

    public String getProvincia() {
        return Provincia;
    }

    public String getMunicipio() {
        return Municipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getCorreo() {
        return Correo;
    }

    public Camping(String nombre, String categoria, String provincia, String municipio, String correo){
        Nombre = nombre;
        Categoria = categoria;
        Provincia = provincia;
        Municipio = municipio;
        Correo = correo;
    }

    public static Comparator<Camping> comparadorNombreDescendente = new Comparator<Camping>() {
        public int compare(Camping c1, Camping c2) {
            return c2.getNombre().compareTo(c1.getNombre());
        }
    };

    public static Comparator<Camping> comparadorNombreAscendente = new Comparator<Camping>() {
        public int compare(Camping c1, Camping c2) {
            return c1.getNombre().compareTo(c2.getNombre());
        }
    };
}
