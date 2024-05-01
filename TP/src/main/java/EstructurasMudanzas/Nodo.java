package EstructurasMudanzas;

class Nodo {

    private Object elem;
    private Nodo enlace;

    public Nodo(Object elemento, Nodo link) {
        elem = elemento;
        enlace = link;
    }

    public void setElem(Object elemento) {
        elem = elemento;
    }

    public void setEnlace(Nodo link) {
        enlace = link;
    }

    public Nodo getEnlace() {
        return enlace;
    }

    public Object getElem() {
        return elem;
    }
}
