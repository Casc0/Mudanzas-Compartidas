
package ClasesMudanzas;

import java.util.Objects;

public class ClaveSolicitud {
    //private String clave;
    private int origen ,destino;
    
    public ClaveSolicitud(int origen, int destino){
        //clave = origen + " - "+ destino;
        this.origen = origen;
        this.destino = destino;
    }
    
    
    public int getOrigen(){
        return origen;
    }
    
    public int getDestino(){
        return destino;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.origen;
        hash = 97 * hash + this.destino;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClaveSolicitud other = (ClaveSolicitud) obj;
        if (this.origen != other.origen) {
            return false;
        }
        return this.destino == other.destino;
    }
  
    
    
    @Override
    public String toString(){
        return origen + " - " + destino ;
    }
    
}
