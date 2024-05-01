package ClasesMudanzas;

import java.util.Objects;

public class Solicitud {
    
    private ClaveSolicitud clave;
    private int cantMetros, cantBultos;
    private boolean estaPago;
    private String claveCliente, fecha, domRetiro, domEntrega;

    //creador que llegan los codigos postales separados
    public Solicitud(int origen, int destino, int cantMetros, int cantBultos, String claveCliente, String fecha, String domRetiro, String domEntrega, boolean estaPago) {
        this.clave = new ClaveSolicitud(origen, destino);
        this.cantMetros = cantMetros;
        this.cantBultos = cantBultos;
        this.claveCliente = claveCliente;
        this.estaPago = estaPago;
        this.fecha = fecha;
        this.domRetiro = domRetiro;
        this.domEntrega = domEntrega;
    }
    
    //creador que llega la claveSolicitud ya creada
    public Solicitud(ClaveSolicitud clave, int cantMetros, int cantBultos, String claveCliente, String fecha, String domRetiro, String domEntrega, boolean estaPago) {
        this.clave = clave;
        this.cantMetros = cantMetros;
        this.cantBultos = cantBultos;
        this.claveCliente = claveCliente;
        this.estaPago = estaPago;
        this.fecha = fecha;
        this.domRetiro = domRetiro;
        this.domEntrega = domEntrega;
    }
    
    //creador con solo la clave
    public Solicitud(ClaveSolicitud clave, String claveCliente) {
        this.clave = clave;
        this.cantMetros = 0;
        this.cantBultos = 0;
        this.claveCliente = claveCliente;
        this.estaPago = false;
        this.fecha = "";
        this.domRetiro = "";
        this.domEntrega = "";
    }
    
    public ClaveSolicitud getClave(){
        return clave;
    }
    

    public int getCantMetros() {
        return cantMetros;
    }

    public int getCantBultos() {
        return cantBultos;
    }

    public String getClaveCliente() {
        return claveCliente;
    }

    public boolean getEstaPago() {
        return estaPago;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDomRetiro() {
        return domRetiro;
    }

    public String getDomEntrega() {
        return domEntrega;
    }

    public void setCantMetros(int cantMetros) {
        this.cantMetros = cantMetros;
    }

    public void setCantBultos(int cantBultos) {
        this.cantBultos = cantBultos;
    }

    public void setEstaPago(boolean estaPago) {
        this.estaPago = estaPago;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setDomRetiro(String domRetiro) {
        this.domRetiro = domRetiro;
    }

    public void setDomEntrega(String domEntrega) {
        this.domEntrega = domEntrega;
    }
    
    
    
    @Override
    public String toString(){
        return "Solicitud de cliente " + claveCliente + ", con origen - destino en " + clave.toString() + " , domicilio de retiro " + domRetiro + ", domicilio de entrega " + domEntrega + ". El pedido tiene " + cantMetros + " metros y " + cantBultos + " bultos. Pago: " + estaPago;
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
        final Solicitud other = (Solicitud) obj;
        return Objects.equals(this.clave, other.clave);
    }
    
    
    
}
