
package EstructurasMudanzas;


class NodoAdy {
    private NodoAdy sigAdyacente;
    private NodoVert vertice;
    private Object etiqueta;

    public NodoAdy(NodoAdy sigAdyacente, NodoVert vertice, Object etiqueta) {
        this.sigAdyacente = sigAdyacente;
        this.vertice = vertice;
        this.etiqueta = etiqueta;
    }

    
    public NodoAdy getSigAdyacente() {
        return sigAdyacente;
    }

    public NodoVert getVertice() {
        return vertice;
    }

    public Object getEtiqueta() {
        return etiqueta;
    }

    public void setSigAdyacente(NodoAdy sigAdyacente) {
        this.sigAdyacente = sigAdyacente;
    }

    public void setVertice(NodoVert vertice) {
        this.vertice = vertice;
    }

    public void setEtiqueta(Object etiqueta) {
        this.etiqueta = etiqueta;
    }
    
}
