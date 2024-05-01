
package EstructurasMudanzas;

class NodoVert {
    private Object elem;
    private NodoVert sigVertice;
    private NodoAdy primerAdy;

    public NodoVert(Object elem, NodoVert sigVertice, NodoAdy primerAdy) {
        this.elem = elem;
        this.sigVertice = sigVertice;
        this.primerAdy = primerAdy;
    }

    public Object getElem() {
        return elem;
    }

    public NodoVert getSigVertice() {
        return sigVertice;
    }

    public NodoAdy getPrimerAdy() {
        return primerAdy;
    }

    public void setElem(Object elem) {
        this.elem = elem;
    }

    public void setSigVertice(NodoVert sigVertice) {
        this.sigVertice = sigVertice;
    }

    public void setPrimerAdy(NodoAdy primerAdy) {
        this.primerAdy = primerAdy;
    }
    
    
}
