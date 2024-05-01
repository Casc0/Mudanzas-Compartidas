package EstructurasMudanzas;

class NodoDic {
    private Comparable clave;
    private Object elem;
    private NodoDic izquierdo, derecho;
    private int altura;
    

    public NodoDic(Comparable cla, Object el, NodoDic izq, NodoDic der) {
        clave = cla;
        elem = el;
        izquierdo = izq;
        derecho = der;
        this.recalcularAltura();
    }

    public Comparable getClave() {
        return clave;
    }

    public Object getElem() {
        return elem;
    }

    public NodoDic getIzquierdo() {
        return izquierdo;
    }

    public NodoDic getDerecho() {
        return derecho;
    }

    public int getAltura() {
        return altura;
    }
    

    public void setClave(Comparable clave) {
        this.clave = clave;
    }

    public void setElem(Object elem) {
        this.elem = elem;
    }

    public void setIzquierdo(NodoDic izquierdo) {
        this.izquierdo = izquierdo;
        this.recalcularAltura();
    }

    public void setDerecho(NodoDic derecho) {
        this.derecho = derecho;
        this.recalcularAltura();
    }
    
    public void recalcularAltura(){
        if(izquierdo != null && derecho != null){
            altura = 1 + Math.max(izquierdo.altura, derecho.altura);
        }else if( izquierdo != null){
            altura = 1 + izquierdo.altura;
        }else if(derecho != null){
            altura = 1 + derecho.altura;
        }else{
            altura = 0;
        }
        
    }
    
    
}
