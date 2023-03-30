package david.pablo.uv.es;

import java.io.Serializable;
import java.util.Comparator;

public class Camping implements Serializable {
    private int Id;
    private String CP;
    private String Periodo;
    private String Plazas;
    private String Direcion;
    private String Web;
    private String Nombre;
    private String Categoria;
    private String Provincia;
    private String Municipio;
    private String Correo;

     public Camping(String web, String s, String municipio, String periodo, String categoria, String provincia, String nombre, String direcion, String categoria1, String direcion1, String correo){

     }

    public Camping(int id, String cp, String periodo, String plazas, String direcion, String web, String nombre, String categoria, String provincia, String municipio, String correo) {
        Id = id;
        CP = cp;
        Periodo = periodo;
        Plazas = plazas;
        Direcion = direcion;
        Web = web;
        Nombre = nombre;
        Categoria = categoria;
        Provincia = provincia;
        Municipio = municipio;
        Correo = correo;
    }

    public int getId() {
        return Id;
    }

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
    
    public String getPeriodo() {
        return Periodo;
    }
    public String getPlazas() {
        return Plazas;
    }
    public String getDirecion() {
        return Direcion;
    }
    public String getWeb() {
        return Web;
    }
    public String getCP() {
        return CP;
    }
    public Camping(String nombre, String categoria, String provincia, String municipio, String correo, String web, String periodo, String plazas, String direccion, String cp){
        Nombre = nombre;
        Categoria = categoria;
        Provincia = provincia;
        Municipio = municipio;
        Correo = correo;
        Web = web;
        Periodo = periodo;
        Plazas = plazas;
        Direcion = direccion;
        CP = cp;
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
