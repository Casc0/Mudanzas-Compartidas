
package ClasesMudanzas;

public class Cliente {

    private String clave, nombre, apellido, telefono;
    
    public Cliente(String tipoD, int numD, String nom, String ap, String tel){
        clave = tipoD + numD;
        nombre = nom;
        apellido = ap;
        telefono = tel;
    }
    
    public Cliente(String laClave, String nom, String ap, String tel){
        clave = laClave;
        nombre = nom;
        apellido = ap;
        telefono = tel;
    }

    public String getClave() {
        return clave;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }


    public String getTelefono() {
        return telefono;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    @Override
    public String toString(){
        return "Cliente " + clave + ", " + nombre + " "+ apellido + ", con telefono: " +  telefono;
    }
    
    public boolean equals(Cliente aComparar){
        return clave.equals(aComparar.clave);
    }
    
    

}
