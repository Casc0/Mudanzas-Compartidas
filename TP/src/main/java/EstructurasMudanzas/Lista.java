package EstructurasMudanzas;

public class Lista {

    private Nodo cabecera;
    private int longitud;

    public Lista() {
        cabecera = null;
        longitud = 0;
    }

    public boolean insertar(Object obj, int pos) {
        boolean exito = true;
        if (pos < 1 || this.longitud() + 1 < pos) {
            exito = false;
        } else {
            longitud += 1;
            if (pos == 1) { //crea un nuevo nodo y enlaza la cabecera
                cabecera = new Nodo(obj, cabecera);
            } else { //avanza hasta el nodo en pos - 1
                Nodo aux = avanzar(pos);
                //crea el nodo y lo enlaza
                Nodo nuevo = new Nodo(obj, aux.getEnlace());
                aux.setEnlace(nuevo);

            }
        }

        return exito;
    }

    private Nodo avanzar(int pos) {
        Nodo aux = cabecera;
        int i;
        for (i = 1; i < pos - 1; i++) {
            aux = aux.getEnlace();
        }
        return aux;
    }

    public boolean eliminar(int pos) {
        boolean exito = true;
        if (pos < 1 || this.longitud() < pos) {
            exito = false;
        } else {
            longitud -= 1;
            if (pos == 1) { //crea un nuevo nodo y enlaza la cabecera
                cabecera = cabecera.getEnlace();
            } else { //avanza hasta el nodo en pos - 1
                Nodo aux = avanzar(pos);
                aux.setEnlace(aux.getEnlace().getEnlace());
            }
        }

        return exito;
    }

    public Object recuperar(int pos) {
        //es precondicion que la posicion sea valida
        int i = 1;
        Nodo iterador = cabecera;
        boolean encontro = false;
        Object obj = null;
        if (cabecera != null) {
            //mientras este dentro de la lista y no se haya encontrado, se itera nodo por nodo.
            while (iterador != null && !encontro) {

                if (pos == i) {
                    encontro = true;
                    obj = iterador.getElem();
                } else {
                    iterador = iterador.getEnlace();
                    i++;
                }

            }
        }

        return obj;
    }

    public int localizar(Object elem) {
        int pos = 1;
        Nodo iterador = cabecera;
        boolean encontro = false;
        if (cabecera != null) {

            //mientras este dentro de la lista y no se haya encontrado, se itera nodo por nodo.
            while (iterador != null && !encontro) {
                if (iterador.getElem().equals(elem)) {
                    encontro = true;
                } else {
                    iterador = iterador.getEnlace();
                    pos++;
                }

            }
        }
        if (!encontro) {
            pos = -1;
        }

        return pos;

    }

    public int longitud() {
        return longitud;
    }

    public void vaciar() {
        cabecera = null;
        longitud = 0;
    }

    public boolean esVacia() {
        return (cabecera == null);
    }

    @Override
    public Lista clone() {
        Lista listita = new Lista();
        if (cabecera != null) {
            listita.clonar(cabecera, listita);
            listita.longitud = this.longitud;
        }
        return listita;
    }

    private void clonar(Nodo auxOrig, Lista listita) {

        Nodo auxiliar;
        if (auxOrig.getEnlace() == null) {
            auxiliar = new Nodo(auxOrig.getElem(), null);
            listita.cabecera = auxiliar;

        } else {
            listita.clonar(auxOrig.getEnlace(), listita);
            auxiliar = new Nodo(auxOrig.getElem(), listita.cabecera);
            listita.cabecera = auxiliar;

        }

    }

    //toString original
    @Override
    public String toString() {
        String s;

        if (cabecera == null) {
            s = "[Lista vacia";
        } else {
            Nodo aux = cabecera;
            s = "[";

            while (aux != null) {
                s = s + aux.getElem();
                aux = aux.getEnlace();

                if (aux != null) {
                    s = s + ", ";
                }
            }
        }
        s = s + "]";
        return s;
    }
     /* toString con \n entre elementos
    @Override
    public String toString() {
        String s = "";

        if (cabecera == null) {
            s = "[Lista vacia]";
        } else {
            Nodo aux = cabecera;

            while (aux != null) {
                s = s + "[ " + aux.getElem() + " ]\n";
                aux = aux.getEnlace();

            }
        }
        return s;
    }
    */
    /*
        public Lista obtenerMultiplos(int num){
            Lista multiplos = new Lista();
            int i = 1;
            Nodo actual = cabecera, multiplosActual = new Nodo(null,  null), nuevo;
            
            while(actual != null){
                System.out.println("El actual es " + actual.getElem());
                if(i%num == 0){
                    nuevo= new Nodo(actual.getElem(), null);
                    if(multiplos.cabecera == null){
                        multiplosActual = nuevo;
                        multiplos.cabecera = multiplosActual;
                    }else{
                        multiplosActual.setEnlace(nuevo);
                        multiplosActual = multiplosActual.getEnlace();
                        
                    }
                }
                i++;
                actual = actual.getEnlace();
            }
            return multiplos;
        }
     */
}
