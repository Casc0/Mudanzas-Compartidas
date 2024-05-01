package EstructurasMudanzas;

public class GrafoEtiquetado {
    //este GrafoEtiquetado simple y no-dirigido

    private NodoVert inicio;

    public GrafoEtiquetado() {
        inicio = null;
    }

    public void vaciar() {
        inicio = null;
    }

    public boolean esVacio() {
        return inicio == null;
    }

    private NodoVert buscarVertice(NodoVert n, Object elem) {
        NodoVert elVertice = n;
        while (elVertice != null && !elVertice.getElem().equals(elem)) {
            elVertice = elVertice.getSigVertice();
        }
        return elVertice;
    }

    public boolean insertarVertice(Object aInsertar) {
        boolean exito = false;
        if (aInsertar != null) {
            if (inicio == null) {
                inicio = new NodoVert(aInsertar, null, null);
            } else if (!inicio.getElem().equals(aInsertar)) {
                exito = insertarVertAux(inicio, aInsertar);
            }
        }

        return exito;
    }

    private boolean insertarVertAux(NodoVert n, Object aInsertar) {
        //busca el ultimo nodo de la lista de Vertices e inserta ahi. En el proceso de busqueda, se va asegurando que no se exista ya
        boolean exito = false;
        NodoVert sigVertice = n.getSigVertice();
        if (sigVertice == null) {
            n.setSigVertice(new NodoVert(aInsertar, null, null));
            exito = true;
        } else if (!sigVertice.getElem().equals(aInsertar)) {
            exito = insertarVertAux(sigVertice, aInsertar);
        }

        return exito;
    }

    public boolean eliminarVertice(Object aEliminar) {
        boolean exito = false;
        if (aEliminar != null) {
            if (inicio != null) {
                if (inicio.getElem().equals(aEliminar)) {
                    eliminarAllArcos(inicio);
                    inicio = inicio.getSigVertice();
                    exito = true;
                } else {
                    exito = eliminarVertAux(inicio, aEliminar);
                }
            }
        }

        return exito;
    }

    private boolean eliminarVertAux(NodoVert n, Object aEliminar) {
        //pregunta si el siguiente a n es el aEliminar, si lo es, lo elimina, sino hace recursion.
        boolean exito = false;
        NodoVert sigVertice = n.getSigVertice();
        if (sigVertice != null) {
            if (sigVertice.getElem().equals(aEliminar)) {
                eliminarAllArcos(sigVertice);
                n.setSigVertice(sigVertice.getSigVertice());
                exito = true;
            } else {
                exito = eliminarVertAux(sigVertice, aEliminar);
            }
        }
        return exito;
    }

    private void eliminarAllArcos(NodoVert nodoOrigen) {
        NodoAdy actual = nodoOrigen.getPrimerAdy();
        Object elemOrigen = nodoOrigen.getElem();

        while (actual != null) {
            NodoVert nodoAEliminar = actual.getVertice();
            //buscar en la lista de Adyacentes al arco y lo elimina
            NodoAdy actualDestino = nodoAEliminar.getPrimerAdy();
            if (actualDestino.getVertice().getElem().equals(elemOrigen)) {
                nodoAEliminar.setPrimerAdy(actualDestino.getSigAdyacente());
            } else {
                NodoAdy siguienteDestino = actualDestino.getSigAdyacente();
                //como el grafo es no dirigido, el arco existe en la lista de nodoDestino
                while (!siguienteDestino.getVertice().equals(elemOrigen)) {
                    actualDestino = siguienteDestino;
                    siguienteDestino = actualDestino.getSigAdyacente();
                    //completar eliminacion
                    nodoAEliminar.setPrimerAdy(actualDestino.getSigAdyacente());
                }
                //paso iterativo       
                actual = actual.getSigAdyacente();
            }
        }

    }

    public boolean existeVertice(Object aBuscar) {
        boolean exito = false;
        if (aBuscar != null) {
            if (inicio != null) {
                if (inicio.getElem().equals(aBuscar)) {
                    exito = true;
                } else {
                    exito = existeVertAux(inicio, aBuscar);
                }
            }
        }

        return exito;
    }

    private boolean existeVertAux(NodoVert n, Object aBuscar) {
        //pregunta al siguiente a n si es igual a aBuscar
        boolean exito = false;
        NodoVert sigVertice = n.getSigVertice();
        if (sigVertice != null) {
            if (sigVertice.getElem().equals(aBuscar)) {
                exito = true;
            } else {
                exito = eliminarVertAux(sigVertice, aBuscar);
            }
        }
        return exito;
    }

    public boolean insertarArco(Object primerElem, Object segundoElem, double etiqueta) {
        boolean exito = false;
        //como el grafo es simple, los dos elementos deben ser diferentes
        if (primerElem != null && segundoElem != null && !primerElem.equals(segundoElem) && inicio != null) {

            if (inicio.getElem().equals(primerElem)) {
                exito = insertarArcoAux(inicio, segundoElem, etiqueta);

            } else if (inicio.getElem().equals(segundoElem)) {
                exito = insertarArcoAux(inicio, primerElem, etiqueta);

            } else {
                //busca el Nodo del que aparece primero y lo manda al auxiliar con el otro elemento
                NodoVert primero = primeroQueAparece(inicio, primerElem, segundoElem);

                if (primero != null) {
                    if (primero.getElem().equals(primerElem)) {
                        exito = insertarArcoAux(primero, segundoElem, etiqueta);

                    } else {
                        exito = insertarArcoAux(primero, primerElem, etiqueta);
                    }

                }

            }

        }
        return exito;
    }

    private boolean insertarArcoAux(NodoVert primerVert, Object elem, double etiqueta) {
        //busca el segundo vertice, si lo encuentra manda a insertar el arco
        boolean exito = false;
        //busca si es que existe un nodo para el otro elemento
        NodoVert segundoVert = buscarVertice(primerVert.getSigVertice(), elem);
        if (segundoVert != null) {
            exito = insertarAdy(primerVert, segundoVert, etiqueta);
        }
        return exito;
    }

    private boolean insertarAdy(NodoVert nodoOrigen, NodoVert nodoDestino, double etiqueta) {
        //inserta un arco en ambos nodos mientras ya no este en la lista de adyacentes
        boolean exito = false;
        NodoAdy actual = nodoOrigen.getPrimerAdy();
        Object elemDestino = nodoDestino.getElem();
        if (actual == null) {
            nodoOrigen.setPrimerAdy(new NodoAdy(null, nodoDestino, etiqueta));

            exito = true;
        } else {
            //este while se corta si es que el adyacente ya existe
            while (actual != null && !actual.getVertice().getElem().equals(elemDestino)) {
                NodoAdy siguiente = actual.getSigAdyacente();
                if (siguiente == null) {
                    actual.setSigAdyacente(new NodoAdy(null, nodoDestino, etiqueta));

                    exito = true;

                }
                actual = siguiente;
            }
        }
        //como ya sabe que no habia un arco pre-existente, inserta el arco en el nodoDestino
        if (exito) {
            nodoDestino.setPrimerAdy(new NodoAdy(nodoDestino.getPrimerAdy(), nodoOrigen, etiqueta));
        }
        return exito;
    }

    private NodoVert primeroQueAparece(NodoVert n, Object elem, Object elem2) {
        //devuelve el nodo del elemento que aparece primero en la lista de vertices
        NodoVert elVertice = n;
        boolean seguir = true;
        while (elVertice != null && seguir) {
            if (elVertice.getElem().equals(elem) || elVertice.getElem().equals(elem2)) {
                seguir = false;
            } else {
                elVertice = elVertice.getSigVertice();
            }

        }
        return elVertice;
    }

    public boolean eliminarArco(Object primerElem, Object segundoElem) {
        boolean exito = false;
        //como el grafo es simple, los dos elementos deben ser diferentes
        if (primerElem != null && segundoElem != null && !primerElem.equals(segundoElem) && inicio != null) {

            if (inicio.getElem().equals(primerElem)) {
                exito = eliminarAdy(inicio, segundoElem);

            } else if (inicio.getElem().equals(segundoElem)) {
                exito = eliminarAdy(inicio, primerElem);

            } else {
                //busca el Nodo del que aparece primero y lo manda al auxiliar con el otro elemento
                NodoVert primero = primeroQueAparece(inicio, primerElem, segundoElem);
                if (primero != null) {

                    if (primero.getElem().equals(primerElem)) {
                        exito = eliminarAdy(primero, segundoElem);

                    } else {
                        exito = eliminarAdy(primero, primerElem);
                    }

                }

            }

        }
        return exito;
    }

    private boolean eliminarAdy(NodoVert nodoOrigen, Object destino) {
        //borra el arco en la lista de arcos de nodoOrigen y de nodoDestino
        boolean exito = false;
        NodoAdy actual = nodoOrigen.getPrimerAdy();
        while (actual != null && !exito) {
            NodoAdy siguiente = actual.getSigAdyacente();
            if (siguiente != null && siguiente.getVertice().getElem().equals(destino)) {
                NodoVert nodoDestino = siguiente.getVertice();
                //esta seccion borra el arco en la lista de adyacentes de nodoDestino
                NodoAdy actualDestino = nodoDestino.getPrimerAdy();
                Object elemOrigen = nodoOrigen.getElem();
                //un if que verifica si es el primer ady o otro mas adelante
                if (actualDestino.getVertice().getElem().equals(elemOrigen)) {
                    nodoDestino.setPrimerAdy(actualDestino.getSigAdyacente());
                } else {
                    NodoAdy siguienteDestino = actualDestino.getSigAdyacente();
                    //como el grafo es no dirigido, el arco existe en la lista de nodoDestino
                    while (!siguienteDestino.getVertice().getElem().equals(elemOrigen)) {
                        actualDestino = siguienteDestino;
                        siguienteDestino = actualDestino.getSigAdyacente();
                    }
                    //borra el arco en la lista de nodoDestino
                    actualDestino.setSigAdyacente(siguiente.getSigAdyacente());
                }

                //borra el arco en la lista de adyacentes de nodoOrigen
                actual.setSigAdyacente(siguiente.getSigAdyacente());
                exito = true;
            } else {
                actual = siguiente;
            }

        }
        return exito;
    }

    public boolean existeArco(Object primerElem, Object segundoElem) {
        boolean exito = false;
        //como el grafo es simple, los dos elementos deben ser diferentes
        if (primerElem != null && segundoElem != null && !primerElem.equals(segundoElem) && inicio != null) {

            if (inicio.getElem().equals(primerElem)) {
                exito = existeArcoAux(inicio, segundoElem);

            } else if (inicio.getElem().equals(segundoElem)) {
                exito = existeArcoAux(inicio, primerElem);

            } else {
                //busca el Nodo del que aparece primero y lo manda al auxiliar con el otro elemento
                NodoVert primero = primeroQueAparece(inicio, primerElem, segundoElem);
                if (primero != null) {

                    if (primero.getElem().equals(primerElem)) {
                        exito = existeArcoAux(primero, segundoElem);

                    } else {
                        exito = existeArcoAux(primero, primerElem);
                    }

                }

            }

        }
        return exito;
    }

    private boolean existeArcoAux(NodoVert n, Object elem) {
        //si el elemento esta presente en la la lista de adyacente de n, existe un arco
        boolean existe = false;
        if (buscarAdy(n, elem) != null) {
            existe = true;
        }
        return existe;
    }

    private NodoAdy buscarAdy(NodoVert n, Object elem) {
        //busca un adyacente, retorna nulo si no lo encuentra
        NodoAdy buscado = n.getPrimerAdy();
        while (buscado != null && !buscado.getVertice().getElem().equals(elem)) {
            buscado = buscado.getSigAdyacente();
        }
        return buscado;
    }

    public Lista listarEnProfundidad() {
        Lista visitados = new Lista();
        // define un vertice donde comenzar a recorrer
        NodoVert aux = inicio;
        while (aux != null) {
            if (visitados.localizar(aux.getElem()) < 0) {
                // si el vertice no fue visitado aun, avanza en profundidad
                listarEnProfundidadAux(aux, visitados);
            }
            aux = aux.getSigVertice();
        }
        return visitados;
    }

    private void listarEnProfundidadAux(NodoVert n, Lista vis) {
        if (n != null) {
            // marca al vertice n como visitado
            vis.insertar(n.getElem(), vis.longitud() + 1);
            NodoAdy ady = n.getPrimerAdy();
            while (ady != null) {
                // visita en profundidad los adyacentes de n aun no visitados
                if (vis.localizar(ady.getVertice().getElem()) < 0) {
                    listarEnProfundidadAux(ady.getVertice(), vis);
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    public boolean existeCamino(Object origen, Object destino) {
        boolean exito = false;
        // verifica si ambos vertices existen
        NodoVert auxO = null, auxD = null, aux = inicio;

        while ((auxO == null || auxD == null) && aux != null) {
            if (aux.getElem().equals(origen)) {
                auxO = aux;
            }
            if (aux.getElem().equals(destino)) {
                auxD = aux;
            }
            aux = aux.getSigVertice();
        }

        if (auxO != null && auxD != null) {
            // si ambos vertices existe busca si existe camino entre ambos
            Lista visitados = new Lista();
            exito = existeCaminoAux(auxO, destino, visitados);
        }

        return exito;
    }

    private boolean existeCaminoAux(NodoVert n, Object destino, Lista vis) {
        boolean exito = false;
        if (n != null) {
            // si vertice n es el destino: HAY CAMINO!
            if (n.getElem().equals(destino)) {
                exito = true;
            } else {
                // si no es el destino verifica si hay camino entre n y destino
                vis.insertar(n.getElem(), vis.longitud() + 1);
                NodoAdy ady = n.getPrimerAdy();
                while (!exito && ady != null) {
                    if (vis.localizar(ady.getVertice().getElem()) < 0) {
                        exito = existeCaminoAux(ady.getVertice(), destino, vis);
                    }
                    ady = ady.getSigAdyacente();
                }
            }
        }
        return exito;
    }

    public Lista caminoMasCorto(Object origen, Object destino) {
        Lista caminoCorto = new Lista();
        NodoVert auxO = null, auxD = null, aux = inicio;

        while ((auxO == null || auxD == null) && aux != null) {
            if (aux.getElem().equals(origen)) {
                auxO = aux;
            }
            if (aux.getElem().equals(destino)) {
                auxD = aux;
            }
            aux = aux.getSigVertice();
        }

        if (auxO != null && auxD != null) {
            // si ambos vertices existe busca si existe camino entre ambos
            Lista visitados = new Lista();
            visitados.insertar(origen, 1);
            caminoCorto = caminoCortoAux(auxO, destino, caminoCorto, visitados);

        }
        return caminoCorto;
    }

    private Lista caminoCortoAux(NodoVert n, Object destino, Lista caminoCorto, Lista vis) {
        if (n != null) {
            //si n es el destino, ya se encontro un camino
            if (n.getElem().equals(destino)) {
                caminoCorto = vis.clone();
            } else {
                NodoAdy actual = n.getPrimerAdy();
                while (actual != null) {

                    //pregunta si el adyacente no esta ya en la lista de visitados
                    if (vis.localizar(actual.getVertice().getElem()) < 0) {
                        //busca un primer camino, una vez que lo encuentra, manda al else. 
                        //manda la recursion mientras la longitud de visitados es menor a la de caminoCorto -1. El menos 1 es restandole el destino que se le agrego
                        if (caminoCorto.esVacia() || vis.longitud() < caminoCorto.longitud() - 1) {
                            vis.insertar(actual.getVertice().getElem(), vis.longitud() + 1);
                            caminoCorto = caminoCortoAux(actual.getVertice(), destino, caminoCorto, vis);
                            vis.eliminar(vis.longitud());
                        }
                    }
                    actual = actual.getSigAdyacente();
                }
            }
        }
        return caminoCorto;
    }

    public Lista caminoMenorEtiqueta(Object origen, Object destino) {
        Lista caminoCorto = new Lista();
        NodoVert auxO = null, auxD = null, aux = inicio;

        while ((auxO == null || auxD == null) && aux != null) {
            if (aux.getElem().equals(origen)) {
                auxO = aux;
            }
            if (aux.getElem().equals(destino)) {
                auxD = aux;
            }
            aux = aux.getSigVertice();
        }

        if (auxO != null && auxD != null) {
            // si ambos vertices existe busca si existe camino entre ambos
            Lista visitados = new Lista();
            visitados.insertar(origen, 1);
            double[] largo = new double[2];
            caminoCorto = caminoEtiquetaAux(auxO, destino, caminoCorto, visitados, largo);

        }
        return caminoCorto;
    }

    private Lista caminoEtiquetaAux(NodoVert n, Object destino, Lista caminoCorto, Lista vis, double[] largo) {
        //largo[0] es el km mas corto, largo[1] es el km actual
        if (n != null) {
            //si n es el destino, ya se encontro un camino
            if (n.getElem().equals(destino)) {

                caminoCorto = vis.clone();
                largo[0] = largo[1];

            } else {
                NodoAdy actual = n.getPrimerAdy();
                while (actual != null) {

                    //pregunta si el adyacente no esta ya en la lista de visitados
                    if (vis.localizar(actual.getVertice().getElem()) < 0) {
                        //busca un primer camino, una vez que lo encuentra, manda al else. 
                        //manda la recursion mientras la longitud de visitados es menor a la de caminoCorto -1. El menos 1 es restandole el destino que se le agrego
                        if (caminoCorto.esVacia()) {

                            vis.insertar(actual.getVertice().getElem(), vis.longitud() + 1);
                            largo[1] += (double) actual.getEtiqueta();
                            caminoCorto = caminoEtiquetaAux(actual.getVertice(), destino, caminoCorto, vis, largo);
                            vis.eliminar(vis.longitud());
                            largo[1] -= (double) actual.getEtiqueta();

                        } else {
                            largo[1] += (double) actual.getEtiqueta();
                            if (largo[1] < largo[0]) {
                                vis.insertar(actual.getVertice().getElem(), vis.longitud() + 1);
                                
                                caminoCorto = caminoEtiquetaAux(actual.getVertice(), destino, caminoCorto, vis, largo);
                                vis.eliminar(vis.longitud());
                                
                            }
                            largo[1] -= (double) actual.getEtiqueta();
                        }
                    }
                    actual = actual.getSigAdyacente();
                }
            }
        }
        return caminoCorto;
    }

    public boolean verificarCaminoLista(Object[] lista) {
        boolean exito = false;
        int i = 0, longitud = lista.length;
        NodoVert vertActual = null, aux = inicio;
        NodoAdy adyActual;
        //se asegura que la lista tenga mas de un elemento
        if (longitud > 1) {
            //busca el primer vertice
            while (vertActual == null && aux != null) {
                if (aux.getElem().equals(lista[i])) {
                    vertActual = aux;
                }
                aux = aux.getSigVertice();
            }

            if (vertActual != null) {
                adyActual = vertActual.getPrimerAdy();
                i++;
                //se fija si el vertice tiene al siguiente de la lista entre sus adyacentes
                while (adyActual != null && !exito) {
                    if (adyActual.getVertice().getElem().equals(lista[i])) {
                        i++;
                        //cuando i es igual a longitud es que ya se verificaron todos
                        if (i == longitud) {
                            exito = true;
                        } else {
                            vertActual = adyActual.getVertice();
                            adyActual = vertActual.getPrimerAdy();
                        }

                    } else {
                        adyActual = adyActual.getSigAdyacente();
                    }
                }
            }
        }
        return exito;
    }

    @Override
    public String toString() {
        return toStringAux(this.inicio);
    }

    private String toStringAux(NodoVert n) {
        String arbol = "";
        if (n != null) {
            arbol += "Vertice " + n.getElem().toString() + " con arcos: \n" + toStringAdy(n.getPrimerAdy()) + "\n";
            arbol = arbol + toStringAux(n.getSigVertice());
        }
        return arbol;
    }

    private String toStringAdy(NodoAdy n) {
        String arbol = "";
        if (n != null) {
            arbol += n.getEtiqueta() + " a " + n.getVertice().getElem().toString() + "\n";

            arbol = arbol + toStringAdy(n.getSigAdyacente());
        }
        return arbol;
    }

}
