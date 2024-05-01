package EstructurasMudanzas;

public class Diccionario {

    private NodoDic raiz;

    public Diccionario() {
        raiz = null;
    }

    public boolean existeClave(Comparable codigo) {
        boolean esta = false;
        if (raiz != null) {
            esta = buscarClave(raiz, codigo);
        }
        return esta;
    }

    private boolean buscarClave(NodoDic n, Comparable codigo) {
        boolean esta = false;
        if (n != null) {
            int comparacion = codigo.compareTo(n.getClave());
            if (comparacion == 0) {
                esta = true;
            } else if (comparacion > 0) {
                esta = buscarClave(n.getDerecho(), codigo);
            } else {
                esta = buscarClave(n.getIzquierdo(), codigo);
            }
        }

        return esta;
    }

    public Object obtenerDato(Comparable codigo) {
        Object dato = null;
        if (raiz != null) {
            dato = buscarElem(raiz, codigo);
        }
        return dato;
    }

    private Object buscarElem(NodoDic n, Comparable codigo) {
        Object dato = null;
        if (n != null) {
            int comparacion = codigo.compareTo(n.getClave());
            if (comparacion == 0) {
                dato = n.getElem();
            } else if (comparacion > 0) {
                dato = buscarElem(n.getDerecho(), codigo);
            } else {
                dato = buscarElem(n.getIzquierdo(), codigo);
            }
        }

        return dato;
    }

    public boolean insertar(Comparable codigo, Object elemento) {
        boolean pudo;
        if (raiz == null) {
            raiz = new NodoDic(codigo, elemento, null, null);
            pudo = true;
        } else {
            pudo = insertarAux(raiz, codigo, elemento);
            if (pudo) {
                raiz.recalcularAltura();
                rotarRaiz(raiz);
            }
        }
        return pudo;
    }

    private boolean insertarAux(NodoDic n, Comparable codigo, Object elemento) {
        boolean exito = false;
        if (n != null) {
            int comparacion = codigo.compareTo(n.getClave());

            if (comparacion > 0) {
                NodoDic hijo = n.getDerecho();
                if (hijo != null) {
                    exito = insertarAux(hijo, codigo, elemento);
                } else {
                    n.setDerecho(new NodoDic(codigo, elemento, null, null));
                    exito = true;
                }
            } else if (comparacion < 0) {
                NodoDic hijo = n.getIzquierdo();
                if (hijo != null) {
                    exito = insertarAux(hijo, codigo, elemento);
                } else {
                    n.setIzquierdo(new NodoDic(codigo, elemento, null, null));
                    exito = true;
                }
            }
            if (exito) {
                n.recalcularAltura();
                rotar(n);
            }
        }
        return exito;
    }

    //le llega un nodo, y pregunta si sus hijos estan bien balanceados
    private void rotar(NodoDic padre) {

        NodoDic hijoIzq = padre.getIzquierdo(), hijoDer = padre.getDerecho();

        if (hijoIzq != null) {
            int balanceIzq = calcularBalance(hijoIzq);

            if (balanceIzq >= 2) {
                VerificarDobleDerecha(padre, hijoIzq);
            } else if (balanceIzq <= -2) {
                VerificarDobleIzquierda(padre, hijoIzq);
            }
        }

        if (hijoDer != null) {
            int balanceDer = calcularBalance(hijoDer);
            if (balanceDer >= 2) {
                VerificarDobleDerecha(padre, hijoDer);
            } else if (balanceDer <= -2) {
                VerificarDobleIzquierda(padre, hijoDer);
            }
        }

    }

    private void rotarRaiz(NodoDic n) {
        int balance = calcularBalance(n);

        if (balance >= 2) {
            VerificarDobleDerecha(null, n);
        } else if (balance <= -2) {
            VerificarDobleIzquierda(null, n);
        }

    }

    //devuelve el balance del nodo
    private int calcularBalance(NodoDic n) {
        int balance = 0;
        NodoDic hijoIz = n.getIzquierdo(), hijoDer = n.getDerecho();

        if (hijoIz != null && hijoDer != null) {
            balance = hijoIz.getAltura() - hijoDer.getAltura();
        } else if (hijoDer != null) {
            balance = -1 - hijoDer.getAltura();
        } else if (hijoIz != null) {
            balance = hijoIz.getAltura() + 1;
        }
        return balance;
    }

    private void VerificarDobleDerecha(NodoDic padre, NodoDic pivote) {
        //guarda el que va a ser la nueva raiz;
        NodoDic nuevaRaiz = pivote.getIzquierdo();

        //si el balance de nuevaRaiz es del otro signo, hay que hacer doble rotacion
        if (calcularBalance(nuevaRaiz) < 0) {
            rotacionIzq(pivote, nuevaRaiz);
        }
        rotacionDer(padre, pivote);
    }

    private void rotacionDer(NodoDic padre, NodoDic pivote) {
        //guarda el que va a ser la nueva raiz;
        NodoDic nuevaRaiz = pivote.getIzquierdo();
        NodoDic aux = nuevaRaiz.getDerecho();

        //si padre es nulo es el caso rotacion con raiz;
        if (padre != null) {
            if (pivote == padre.getDerecho()) {
                padre.setDerecho(nuevaRaiz);
            } else {
                padre.setIzquierdo(nuevaRaiz);
            }
        } else {
            raiz = nuevaRaiz;
        }
        pivote.setIzquierdo(aux);
        nuevaRaiz.setDerecho(pivote);
    }

    private void VerificarDobleIzquierda(NodoDic padre, NodoDic pivote) {
        //guarda el que va a ser la nueva raiz;
        NodoDic nuevaRaiz = pivote.getDerecho();

        //si el balance de nuevaRaiz es del otro signo, hay que hacer doble rotacion
        if (calcularBalance(nuevaRaiz) > 0) {
            rotacionDer(pivote, nuevaRaiz);
        }
        rotacionIzq(padre, pivote);
    }

    private void rotacionIzq(NodoDic padre, NodoDic pivote) {
        //guarda el que va a ser la nueva raiz;
        NodoDic nuevaRaiz = pivote.getDerecho();

        NodoDic aux = nuevaRaiz.getIzquierdo();
        //si padre es nulo es el caso rotacion con raiz;
        if (padre != null) {
            if (pivote == padre.getDerecho()) {
                padre.setDerecho(nuevaRaiz);
            } else {
                padre.setIzquierdo(nuevaRaiz);
            }
        } else {
            raiz = nuevaRaiz;
        }

        pivote.setDerecho(aux);
        nuevaRaiz.setIzquierdo(pivote);
    }

    public boolean eliminar(Comparable aEliminar) {
        boolean exito = false;
        if (raiz != null) {
            if (!raiz.getClave().equals(aEliminar)) {
                exito = eliminarAux(raiz, aEliminar);
            } else {
                eliminarlo(null, raiz);
                rotarRaiz(raiz);
                exito = true;
            }
        }
        return exito;
    }

    private boolean eliminarAux(NodoDic n, Comparable codigo) {
        //n le pregunta a sus hijos si son iguales a x
        boolean exito = false;
        if (n != null) {
            NodoDic hijoI = n.getIzquierdo(), hijoD = n.getDerecho();
            int comparacion = codigo.compareTo(n.getClave());

            if (comparacion > 0) {
                
                if (hijoD != null) {
                    if (hijoD.getClave().equals(codigo)) {
                        eliminarlo(n, hijoD);
                        exito = true;
                    } else {
                        exito = eliminarAux(hijoD, codigo);
                    }
                }
            } else if (comparacion < 0) {
                if (hijoI != null) {
                    if (hijoI.getClave().equals(codigo)) {
                        eliminarlo(n, hijoI);
                        exito = true;
                    } else {
                        exito = eliminarAux(hijoI, codigo);
                    }
                }
            }
            if (exito) {
                n.recalcularAltura();
                rotar(n);
            }
        }
        return exito;
    }

    private void eliminarlo(NodoDic padreN, NodoDic n) {
        NodoDic hijoI = n.getIzquierdo(), hijoD = n.getDerecho(), aux = null;
        //si n tiene dos hijos, hay que buscar el mayor menor y reemplazar n. Si n tiene un hijo, se setea ese hijo como n, sino se pone null donde estaba n
        if (hijoI != null && hijoD != null) {
            //si el hijoIzquierdo tiene hijo derecho, tengo que buscar el mayorMenor, sino se setea el hijo como nueva raiz
            if (hijoI.getDerecho() == null) {
                aux = hijoI;
            } else {
                aux = buscarMayorMenor(hijoI);
                aux.setIzquierdo(n.getIzquierdo());
            }
            aux.setDerecho(n.getDerecho());
            //reviso que este balanceado el subarbol generado
            rotar(aux);

        } else if (hijoI != null) {
            aux = hijoI;
        } else if (hijoD != null) {
            aux = hijoD;
        }

        //si padreN es null es porque es caso eliminar raiz
        if (padreN == null) {
            raiz = aux;
        } else {
            if (n == padreN.getDerecho()) {
                padreN.setDerecho(aux);
            } else {
                padreN.setIzquierdo(aux);
            }
        }

    }

    private NodoDic buscarMayorMenor(NodoDic padre) {
        //le llega el hijo izq del que hay que eliminar, buscar el mayorMenor y se asegura de hacer todas las rotaciones a la vuelta
        NodoDic mayorMenor = padre.getDerecho();
        if (mayorMenor != null) {
            if (mayorMenor.getDerecho() == null) {
                padre.setDerecho(mayorMenor.getIzquierdo());
            } else {
                mayorMenor = buscarMayorMenor(mayorMenor);
            }
            rotar(padre);
        }
        return mayorMenor;
    }

    public Lista listarClaves() {
        Lista lis = new Lista();
        if (raiz != null) {
            auxClaves(raiz, lis);
        }

        return lis;
    }

    private void auxClaves(NodoDic n, Lista lis) {
        if (n != null) {
            auxClaves(n.getDerecho(), lis);
            lis.insertar(n.getClave(), 1);
            auxClaves(n.getIzquierdo(), lis);
        }
    }

    public Lista listarDatos() {
        Lista lis = new Lista();
        if (raiz != null) {
            auxDatos(raiz, lis);
        }

        return lis;
    }

    private void auxDatos(NodoDic n, Lista lis) {
        if (n != null) {
            auxDatos(n.getDerecho(), lis);
            lis.insertar(n.getElem(), 1);
            auxDatos(n.getIzquierdo(), lis);
        }
    }

    public void vaciar() {
        raiz = null;
    }

    public boolean esVacio() {
        return raiz == null;
    }

    @Override
    public Diccionario clone() {
        Diccionario clon = new Diccionario();
        clon.raiz = auxClone(raiz);

        return clon;
    }

    private NodoDic auxClone(NodoDic n) {
        NodoDic clon = null;
        if (n != null) {
            clon = new NodoDic(n.getClave(), n.getElem(), auxClone(n.getIzquierdo()), auxClone(n.getDerecho()));
        }
        return clon;
    }

    @Override
    public String toString() {
        return toStringAux(this.raiz);
    }

    private String toStringAux(NodoDic n) {
        String arbol = "";
        if (n != null) {
            arbol += n.getClave().toString();
            arbol += " alt: " + n.getAltura() + " ";
            if (n.getIzquierdo() != null) {
                arbol = arbol + " HI:" + n.getIzquierdo().getClave();
            } else {
                arbol = arbol + " HI: - ";
            }
            if (n.getDerecho() != null) {
                arbol = arbol + " HD:" + n.getDerecho().getClave() + "\n";
            } else {
                arbol = arbol + " HD: - " + "\n";
            }
            arbol = arbol + toStringAux(n.getIzquierdo());
            arbol = arbol + toStringAux(n.getDerecho());
        }
        return arbol;
    }
    
    public Lista listarEnRango(Comparable inicio, Comparable fin){
        Lista listados = new Lista();
        boolean exito = false;
        if(inicio != null && fin != null && raiz != null && !inicio.equals(fin)){
            listarEnRangoAux(raiz, inicio, fin, listados);
        }
        return listados;
    }
    
    private void listarEnRangoAux(NodoDic n, Comparable inicio, Comparable fin, Lista listados){
        if(n != null){
            Comparable clave = n.getClave();
            
                if(clave.compareTo(inicio) > 0 && clave.compareTo(fin ) > 0){
                    listarEnRangoAux(n.getIzquierdo(), inicio, fin, listados);
                }else if(clave.compareTo(inicio) < 0 && clave.compareTo(fin ) < 0){
                    listarEnRangoAux(n.getDerecho(), inicio, fin, listados);
                }else{
                    listarEnRangoRec(n, inicio, fin, listados);
                }
            
        }
    }
    
    private void listarEnRangoRec(NodoDic n, Comparable inicio, Comparable fin, Lista listados){
        if (n != null) {
            NodoDic der = n.getDerecho(), izq = n.getIzquierdo();
            
            //si es mayor al inicio y menor al fin, hace un posOrden
            if(n.getClave().compareTo(inicio) >= 0 && n.getClave().compareTo(fin) <= 0){
                listarEnRangoRec(der, inicio, fin, listados);
                listados.insertar(n.getClave(), 1);
                listarEnRangoRec(izq,inicio, fin, listados);
                
            //si es mayor al fin, revisa con sus hijos izquierdos
            }else if(n.getClave().compareTo(fin) > 0){
                listarEnRangoRec(izq,inicio, fin, listados);
            //sino es ninguno de los casos anteriores, es porque es menor al inicio, por lo que llama la recursion con su hijo derecho    
            }else{
                listarEnRangoRec(der, inicio, fin, listados);
            }
        }
    }
}
