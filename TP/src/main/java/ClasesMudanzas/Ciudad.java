package ClasesMudanzas;

public class Ciudad {
    private int codigoPostal;
    private String nombre, provincia;
    
    public Ciudad(int cod, String nom, String prov){
        codigoPostal = cod;
        nombre = nom;
        provincia = prov;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String toString(){
        return codigoPostal + ", " + nombre + " en " + provincia;
    }
    
    public boolean equals(Ciudad aComparar){
        return codigoPostal == aComparar.codigoPostal;
    }
}
