package Mudanzas;

import ClasesMudanzas.Ciudad;
import ClasesMudanzas.ClaveSolicitud;
import ClasesMudanzas.Cliente;
import ClasesMudanzas.Solicitud;
import EstructurasMudanzas.Diccionario;
import EstructurasMudanzas.GrafoEtiquetado;
import EstructurasMudanzas.Lista;
import java.util.Scanner;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class MudanzasCompartidas {

    private static Logger crearLogger() {

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {
            // This block configure the logger with handler and formatter  
            fh = new FileHandler("C:/Users/faber/OneDrive/Documents/MudanzasCompartidas/TP/src/main/java/Mudanzas/log.txt");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.setUseParentHandlers(false);

            // the following statement is used to log any messages  
            logger.info("Se abrio el programa");

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

        return logger;

    }

    public static void main(String[] args) {
        BufferedReader lector = null;
        try {
            Scanner sc = new Scanner(System.in);
            //crea el lector de datos
            lector = new BufferedReader(new FileReader("C:/Users/faber/OneDrive/Documents/MudanzasCompartidas/TP/src/main/java/Mudanzas/datos.txt"));
            //declara e inicializar todas las estructuras
            int decision;
            boolean seguir = true;
            Logger logger = crearLogger();
            Diccionario dicCiudad = new Diccionario();
            HashMap mapeo = new HashMap();
            GrafoEtiquetado mapaRutas = new GrafoEtiquetado();
            Map<ClaveSolicitud, Lista> solicitudes = new HashMap();
            while (seguir) {

                mostrarMenuPrincipal();
                decision = sc.nextInt();

                switch (decision) {
                    case 1:
                        cargaInicial(lector, logger, dicCiudad, mapeo, mapaRutas, solicitudes);
                        logger.info("Se realizo la carga inicial");
                        break;
                    case 2:
                        operacionesCiudad(dicCiudad, mapaRutas, logger);
                        break;
                    case 3:
                        operacionesRutas(mapaRutas, logger);
                        break;
                    case 4:
                        operacionesCliente(mapeo, logger);
                        break;
                    case 5:
                        operacionesSolicitud(solicitudes, dicCiudad, mapeo, mapaRutas, logger);
                        break;
                    case 6:
                        operacionesViajes(logger, dicCiudad, mapeo, mapaRutas, solicitudes);
                        break;
                    case 7:
                        mostrarSistema(dicCiudad, mapeo, mapaRutas, solicitudes);
                        break;
                    case 8:
                        System.out.println("Que tenga un buen dia");
                        seguir = false;
                        logger.info("Se cerro el programa");
                        break;
                    default:
                        System.out.println("Numero no aceptado. Intente de nuevo");
                        break;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MudanzasCompartidas.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                lector.close();
            } catch (IOException ex) {
                Logger.getLogger(MudanzasCompartidas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private static void mostrarMenuPrincipal() {
        //muestra el menu principal
        System.out.println("1. Carga Inicial");
        System.out.println("2. Operaciones de Ciudad");
        System.out.println("3. Operaciones mapa de Rutas");
        System.out.println("4. Operaciones de Cliente");
        System.out.println("5. Operaciones de Pedidos");
        System.out.println("6. Operaciones de Viajes");
        System.out.println("7. Mostrar Sistema");
        System.out.println("8. Terminar");
    }

    public static void cargaInicial(BufferedReader lector, Logger logger, Diccionario dicCiudad, HashMap mapeo, GrafoEtiquetado mapaRutas, Map<ClaveSolicitud, Lista> solicitudes) {
        try {
            String[] lineaSeparada;
            String lineaActual = lector.readLine();

            while (lineaActual != null) {
                lineaSeparada = lineaActual.trim().split(";");

                switch (lineaSeparada[0].trim().toUpperCase()) {
                    case "C":
                        // C;cod;nombreCiudad;provincia
                        int cod = Integer.parseInt(lineaSeparada[1]);
                        if (verificarCodigoPostal(cod)) {
                            String nombreCiudad = lineaSeparada[2],
                                    provincia = lineaSeparada[3];

                            Ciudad laCiudad = new Ciudad(cod, nombreCiudad, provincia);
                            if (dicCiudad.insertar(cod, laCiudad)) {
                                mapaRutas.insertarVertice(laCiudad.getCodigoPostal());
                            }
                        }

                        break;

                    case "P":
                        // P;tipo;nroDocu;apellido;nombres;telefono
                        int nroDocu = Integer.parseInt(lineaSeparada[2]);
                        String tipo = lineaSeparada[1].trim().toUpperCase();
                        if ((tipo.equals("DNI") || tipo.equals("PAS")) && verificarDni(nroDocu)) {
                            String apellido = lineaSeparada[3],
                                    nombre = lineaSeparada[4],
                                    telefono = lineaSeparada[5];

                            Cliente elCliente = new Cliente(tipo, nroDocu, nombre, apellido, telefono);

                            mapeo.putIfAbsent(elCliente.getClave(), elCliente);
                        }

                        break;

                    case "R":
                        // R;origenRuta;destinoRuta;distancia
                        int origenRuta = Integer.parseInt(lineaSeparada[1]),
                         destinoRuta = Integer.parseInt(lineaSeparada[2]);

                        Double distancia = Double.parseDouble(lineaSeparada[3]);

                        mapaRutas.insertarArco(origenRuta, destinoRuta, distancia);

                        break;

                    case "S":
                        // S;origen;destino;fecha;tipo;nroDocu;metros;bultos;domRetiro;domEntrega;estaPago
                        String tipoDoc = lineaSeparada[4].trim().toUpperCase();
                        int nroDoc = Integer.parseInt(lineaSeparada[5]);
                        int origen = Integer.parseInt(lineaSeparada[1]),
                         destino = Integer.parseInt(lineaSeparada[2]);
                        //verifica que el documento y las ciudades sean valido
                        if ((tipoDoc.equals("DNI") || tipoDoc.equals("PAS")) && verificarDni(nroDoc) && verificarCodigoPostal(origen) && verificarCodigoPostal(destino)) {
                            String claveCliente = tipoDoc + nroDoc;

                            //tienen que existir las ciudades y el cliente para poder añadir la solicitud
                            if (dicCiudad.existeClave(origen) && dicCiudad.existeClave(destino) && mapeo.containsKey(claveCliente)) {
                                String fecha = lineaSeparada[3],
                                        domRetiro = lineaSeparada[8],
                                        domEntrega = lineaSeparada[9];

                                int metros = Integer.parseInt(lineaSeparada[6]),
                                        bultos = Integer.parseInt(lineaSeparada[7]);

                                boolean estaPago = lineaSeparada[10].trim().equals("T");

                                Solicitud laSolicitud = new Solicitud(origen, destino, metros, bultos, claveCliente, fecha, domRetiro, domEntrega, estaPago);
                                ClaveSolicitud clave = laSolicitud.getClave();

                                Lista lista = (Lista) solicitudes.get(clave);
                                //si no existe la clave, crea la lista mapeada por esa clave.
                                if (lista == null) {
                                    lista = new Lista();
                                    solicitudes.put(clave, lista);
                                }
                                //inserta la solicitud en la primer posicion de la lista mapeada
                                lista.insertar(laSolicitud, 1);

                            }
                        }

                        break;

                    default:
                        break;
                }
                lineaActual = lector.readLine();

            }

        } catch (IOException ex) {
            logger.info("Hubo un error con la carga inicial");
        }
        //logea los datos de la carga inicial
        logger.info(
                "Diccionario de Ciudades: \n" + dicCiudad.toString());
        logger.info(
                "Mapeo de clientes: \n" + mapeo.toString());
        logger.info(
                "Mapa de rutas: \n" + mapaRutas.toString());
        logger.info(
                "Solicitudes: \n" + solicitudes.toString());

    }

    public static void operacionesCiudad(Diccionario dicCiudad, GrafoEtiquetado mapaRutas, Logger logger) {
        Scanner sc = new Scanner(System.in);
        int decision;
        boolean seguir = true;

        while (seguir) {
            System.out.println("1. Agregar Ciudad");
            System.out.println("2. Eliminar Ciudad");
            System.out.println("3. Modificar el nombre de la Ciudad");
            System.out.println("4. Listar Claves");
            System.out.println("5. Listar Datos");
            System.out.println("6. Mostrar datos de una ciudad");
            System.out.println("7. Listar ciudades con prefijo");
            System.out.println("8. Volver al menu principal");
            decision = sc.nextInt();
            switch (decision) {
                case 1:
                    if (agregarCiudad(dicCiudad, mapaRutas)) {
                        logger.info("Se agrego una ciudad");
                    }
                    break;
                case 2:
                    if (eliminarCiudad(dicCiudad, mapaRutas)) {
                        logger.info("Se elimino una ciudad");
                    }
                    break;
                case 3:
                    if (modificarCiudad(dicCiudad)) {
                        logger.info("Se modifico una ciudad");
                    }
                    break;
                case 4:
                    System.out.println(dicCiudad.listarClaves().toString());
                    break;
                case 5:
                    System.out.println(dicCiudad.listarDatos().toString());
                    break;
                case 6:
                    mostrarCiudad(dicCiudad);
                    break;
                case 7:
                    listarCiudadPorPrefijo(dicCiudad);
                    break;
                case 8:
                    System.out.println("Volviendo al menu principal");
                    seguir = false;
                    break;
                default:
                    System.out.println("Numero no aceptado. Intente de nuevo");
                    break;
            }
        }
    }

    private static boolean agregarCiudad(Diccionario dicCiudad, GrafoEtiquetado mapaRutas) {
        //pregunta los datos y agrega la Ciudad al Diccionario si es posible

        Scanner sc = new Scanner(System.in);
        boolean exito = false;

        int cod = preguntarCodigoPostal();

        if (dicCiudad.existeClave(cod)) {
            System.out.println("No es posible agregar la ciudad, debido a que ya existe.");
        } else {
            System.out.println("Introduzca el nombre de la ciudad");
            String nombre = sc.next();

            String provincia = buscarProvincia();
            //crea la ciudad
            Ciudad ciu = new Ciudad(cod, nombre, provincia);
            //la agrega al Diccionario
            exito = dicCiudad.insertar(ciu.getCodigoPostal(), ciu);
            mapaRutas.insertarVertice(cod);

            System.out.println("Se agrego la ciudad.");

        }

        return exito;
    }

    private static void mostrarProvincias() {
        //muestra las provincias por pantalla

        System.out.println("1. Buenos Aires");

        System.out.println("2. Ciudad Autonoma de Buenos Aires");

        System.out.println("3. Catamarca");

        System.out.println("4. Chaco");

        System.out.println("5. Chubut");

        System.out.println("6. Cordoba");

        System.out.println("7. Corrientes");

        System.out.println("8. Entre Rios");

        System.out.println("9. Formosa");

        System.out.println("10. Jujuy");

        System.out.println("11. La Pampa");

        System.out.println("12. La Rioja");

        System.out.println("13. Mendoza");

        System.out.println("14. Misiones");

        System.out.println("15. Neuquen");

        System.out.println("16. Rio Negro");

        System.out.println("17. Salta");

        System.out.println("18. San Juan");

        System.out.println("19. San Luis");

        System.out.println("20. Santa Cruz");

        System.out.println("21. Santa Fe");

        System.out.println("22. Santiago del Estero");

        System.out.println("23. Tierra del Fuego");

        System.out.println("24. Tucuman");
    }

    private static String buscarProvincia() {
        //busca la provincia 
        Scanner sc = new Scanner(System.in);
        boolean valido = false;
        String s = "";
        mostrarProvincias();

        while (!valido) {
            valido = true;
            System.out.println("Introduzca el numero de la provincia de la ciudad");
            int n = sc.nextInt();
            switch (n) {
                case 1:
                    s = "Buenos Aires";
                    break;
                case 2:
                    s = "Ciudad Autonoma de Buenos Aires";
                    break;
                case 3:
                    s = "Catamarca";
                    break;
                case 4:
                    s = "Chaco";
                    break;
                case 5:
                    s = "Chubut";
                    break;
                case 6:
                    s = "Cordoba";
                    break;
                case 7:
                    s = "Corrientes";
                    break;
                case 8:
                    s = "Entre Rios";
                    break;
                case 9:
                    s = "Formosa";
                    break;
                case 10:
                    s = "Jujuy";
                    break;
                case 11:
                    s = "La Pampa";
                    break;
                case 12:
                    s = "La Rioja";
                    break;
                case 13:
                    s = "Mendoza";
                    break;
                case 14:
                    s = "Misiones";
                    break;
                case 15:
                    s = "Neuquen";
                    break;
                case 16:
                    s = "Rio Negro";
                    break;
                case 17:
                    s = "Salta";
                    break;
                case 18:
                    s = "San Juan";
                    break;
                case 19:
                    s = "San Luis";
                    break;
                case 20:
                    s = "Santa Cruz";
                    break;
                case 21:
                    s = "*anta Fe";
                    break;
                case 22:
                    s = "Santiago del Estero";
                    break;
                case 23:
                    s = "Tierra del Fuego";
                    break;
                case 24:
                    s = "Tucuman";
                    break;
                default:
                    valido = false;
                    System.out.println("Numero no correspondiente a ninguna provincia. Intente de nuevo");

            }
        }

        return s;

    }

    private static boolean eliminarCiudad(Diccionario dicCiudad, GrafoEtiquetado mapaRutas) {
        //pregunta la clave y elimina la Ciudad del Diccionario si es posible

        boolean exito = false;

        int cod = preguntarCodigoPostal();

        boolean pudo = dicCiudad.eliminar(cod);
        if (pudo) {
            mapaRutas.eliminarVertice(cod);
            System.out.println("Se elimino la ciudad.");
            exito = true;
        } else {
            System.out.println("No se pudo eliminar la ciudad.");
        }

        return exito;
    }

    private static boolean modificarCiudad(Diccionario dicCiudad) {
        //pregunta la clave y modifica la Ciudad

        Scanner sc = new Scanner(System.in);

        boolean exito = false;

        int cod = preguntarCodigoPostal();
        //los codigos postales argentinos van de 1000 a 9000 y pico

        Ciudad laCiudad = (Ciudad) dicCiudad.obtenerDato(cod);
        if (laCiudad != null) {
            exito = true;
            System.out.println("Cual sera el nuevo nombre de la ciudad");
            laCiudad.setNombre(sc.next());
        } else {
            System.out.println("No existe esa ciudad en el sistema.");
        }

        return exito;
    }

    public static void mostrarCiudad(Diccionario dicCiudad) {
        // manda a buscar la ciudad, la imprime si es que existe, sino avisa que no esta

        Ciudad laCiudad = buscarCiudad(dicCiudad);
        if (laCiudad != null) {
            System.out.println(laCiudad.toString());
        } else {
            System.out.println("La ciudad no esta en la base de datos");
        }

    }

    private static Ciudad buscarCiudad(Diccionario dicCiudad) {
        //pregunta el codigo postal y devuelve la Ciudad si es que existe, sino devuelve nulo

        Ciudad laCiudad;

        int cod = preguntarCodigoPostal();

        laCiudad = (Ciudad) dicCiudad.obtenerDato(cod);

        return laCiudad;
    }

    private static void listarCiudadPorPrefijo(Diccionario dicCiudad) {
        Scanner sc = new Scanner(System.in);
        boolean valido = false;
        int inicio = 0, fin = 0;
        while (!valido) {
            valido = true;
            System.out.println("Cual es el prefijo que quiere listar? Para que sea prefijo debe ser de maximo tres cifras");
            int prefijo = sc.nextInt();
            if (prefijo < 10) {
                inicio = prefijo * 1000;
                fin = (prefijo * 1000) + 999;
            } else if (prefijo < 100) {
                inicio = prefijo * 100;
                fin = (prefijo * 100) + 99;
            } else if (prefijo < 1000) {
                inicio = prefijo * 10;
                fin = (prefijo * 10) + 9;
            } else {
                System.out.println("Prefijo no valido. Intente de nuevo");
                valido = false;
            }
        }
        System.out.println("Ciudades entre " + inicio + " y " + fin + ": \n" + dicCiudad.listarEnRango(inicio, fin));

    }

    public static void operacionesSolicitud(Map<ClaveSolicitud, Lista> solicitudes, Diccionario dicCiudad, HashMap mapeo, GrafoEtiquetado mapaRutas, Logger logger) {
        Scanner sc = new Scanner(System.in);
        int decision;
        boolean seguir = true;

        while (seguir) {
            System.out.println("1. Agregar Solicitud");
            System.out.println("2. Eliminar Solicitud");
            System.out.println("3. Modificar Solicitud");
            System.out.println("4. Volver al menu principal");
            decision = sc.nextInt();
            switch (decision) {
                case 1:
                    if (agregarSolicitud(solicitudes, dicCiudad, mapaRutas, mapeo)) {
                        logger.info("Se agrego una solicitud");
                    }
                    break;
                case 2:
                    if (eliminarSolicitud(solicitudes)) {
                        logger.info("Se elimino una solicitud");
                    }
                    break;
                case 3:
                    if (modificarSolicitud(solicitudes)) {
                        logger.info("Se modifico una solicitud");
                    }
                    break;
                case 4:
                    System.out.println("Volviendo al menu principal");
                    seguir = false;
                    break;
                default:
                    System.out.println("Numero no aceptado. Intente de nuevo");
                    break;
            }
        }
    }

    private static boolean agregarSolicitud(Map<ClaveSolicitud, Lista> solicitudes, Diccionario dicCiudad, GrafoEtiquetado mapaRutas, HashMap mapeo) {
        Scanner sc = new Scanner(System.in);
        boolean exito = false;
        ClaveSolicitud clave = preguntarClaveSolicitud();
        //solo se puede añadir si existen el origen y el destino en el diccionario
        if (dicCiudad.existeClave(clave.getOrigen()) && dicCiudad.existeClave(clave.getDestino())) {

            System.out.println("Cuales son los datos del cliente?");
            String claveCliente = preguntarDocumento();

            if (mapeo.containsKey(claveCliente)) {
                System.out.println("Cual es la cantidad de metros cubicos del camion?");
                int metros = sc.nextInt();
                System.out.println("Cual es la cantidad de bultos del envio?");
                int bultos = sc.nextInt();
                System.out.println("Cual es la fecha de la solicitud? Ingrese con el siguiente formato: dia/mes/año");
                String fecha = sc.nextLine();
                System.out.println("Cual es el domicilio de retiro");
                String domRetiro = sc.nextLine();
                System.out.println("Cual es el domicilio de entrega");
                String domEntrega = sc.nextLine();
                System.out.println("El envio está pago? true/false");
                boolean estaPago = sc.nextBoolean();

                Solicitud laSolicitud = new Solicitud(clave, metros, bultos, claveCliente, fecha, domRetiro, domEntrega, estaPago);

                Lista lista = solicitudes.get(clave);
                if (lista == null) {
                    lista = new Lista();
                    solicitudes.put(clave, lista);
                }
                lista.insertar(laSolicitud, 1);

                exito = true;
            } else {
                System.out.println("El cliente no esta en el sistema");
            }

        } else {
            System.out.println("Una de las ciudades no esta en el sistema");
        }
        return exito;
    }

    private static boolean eliminarSolicitud(Map<ClaveSolicitud, Lista> solicitudes) {
        boolean exito = false;
        ClaveSolicitud clave = preguntarClaveSolicitud();
        System.out.println(clave.toString());
        Lista lista = solicitudes.get(clave);
        //si la lista es nula es porque no hay ninguna solicitud guardada
        if (lista != null) {
            String claveCliente = preguntarDocumento();
            int pos = lista.localizar(new Solicitud(clave, claveCliente));
            //si se pudo localizar la solicitud, se elimina de la lista
            if (pos > 0) {
                exito = lista.eliminar(pos);
                System.out.println("Se elimino la solicitud");
                //si la lista queda vacia, eliminamos la clave
                if (lista.esVacia()) {
                    solicitudes.remove(clave);
                }
            } else {
                System.out.println("No existe una solicitud bajo ese cliente");
            }
        } else {
            System.out.println("No existen solicitudes entre esas ciudades");
        }
        return exito;
    }

    private static boolean modificarSolicitud(Map<ClaveSolicitud, Lista> solicitudes) {
        Scanner sc = new Scanner(System.in);
        boolean exito = false;
        ClaveSolicitud clave = preguntarClaveSolicitud();
        Lista lista = solicitudes.get(clave);
        if (lista != null) {
            String claveCliente = preguntarDocumento();
            int pos = lista.localizar(new Solicitud(clave, claveCliente));
            if (pos > 0) {
                boolean seguir = true;
                Solicitud laSolicitud = (Solicitud) lista.recuperar(pos);
                while (seguir) {
                    System.out.println("Que quiere modificar?");
                    int decision = sc.nextInt();
                    System.out.println("1. Cantidad de metros \n 2. Cantidad de bultos del envio \n 3. Estado del pago \n 4. Fecha del envio \n 5. Terminar la modificación");
                    switch (decision) {
                        case 1:
                            System.out.println("Ingrese la cantidad de metros que quiere agregar");
                            laSolicitud.setCantMetros(sc.nextInt());
                            exito = true;
                            break;
                        case 2:
                            System.out.println("Ingrese la cantidad de bultos que quiere agregar");
                            laSolicitud.setCantBultos(sc.nextInt());
                            exito = true;
                            break;
                        case 3:
                            boolean pago = !laSolicitud.getEstaPago();
                            laSolicitud.setEstaPago(pago);
                            if (pago) {
                                System.out.println("La solicitud ahora esta pagada");
                            } else {
                                System.out.println("La solicitud ahora no esta pagada");
                            }
                            exito = true;
                            break;
                        case 4:
                            System.out.println("Ingrese la nueva fecha del envio. Respete el formato dia/fecha/año");
                            laSolicitud.setFecha(sc.nextLine());
                            exito = true;
                            break;
                        case 5:
                            seguir = false;
                            break;
                        default:
                            System.out.println("Numero ingresado no valido. Intente de nuevo");
                            break;
                    }
                }
            } else {
                System.out.println("No existe una solicitud bajo ese cliente");
            }
        } else {
            System.out.println("No existen solicitudes entre esas ciudades");
        }
        return exito;
    }

    public static void operacionesCliente(HashMap mapeo, Logger logger) {
        Scanner sc = new Scanner(System.in);
        int decision;
        boolean seguir = true;

        while (seguir) {
            System.out.println("1. Agregar Cliente");
            System.out.println("2. Eliminar Cliente");
            System.out.println("3. Modificar Cliente");
            System.out.println("4. Mostrar un Cliente");
            System.out.println("5. Volver al menu principal");
            decision = sc.nextInt();
            switch (decision) {
                case 1:
                    if (agregarCliente(mapeo)) {
                        logger.info("Se agrego un cliente");
                    }
                    break;
                case 2:
                    if (eliminarCliente(mapeo)) {
                        logger.info("Se elimino un cliente");
                    }
                    break;
                case 3:
                    if (modificarCliente(mapeo)) {
                        logger.info("Se modifico un cliente");
                    }
                    break;
                case 4:
                    System.out.println(buscarCliente(mapeo));
                    break;
                case 5:
                    System.out.println("Volviendo al menu principal");
                    seguir = false;
                    break;
                default:
                    System.out.println("Numero no aceptado. Intente de nuevo");
                    break;
            }
        }

    }

    private static boolean agregarCliente(HashMap mapeo) {
        Scanner sc = new Scanner(System.in);
        boolean exito = false;
        String clave = preguntarDocumento();
        if (!mapeo.containsKey(clave)) {
            exito = true;
            System.out.println("Ingrese sus nombres");
            String nombre = sc.nextLine().toUpperCase();
            System.out.println("Ingrese sus apellido");
            String apellido = sc.nextLine().toUpperCase();
            System.out.println("Ingrese su telefono");
            String telefono = sc.nextLine().toUpperCase();
            Cliente elCliente = new Cliente(clave, nombre, apellido, telefono);
            mapeo.put(elCliente.getClave(), elCliente);
        } else {
            System.out.println("El cliente ya existe en el sistema.");
        }

        return exito;
    }

    private static boolean eliminarCliente(HashMap mapeo) {
        String clave = preguntarDocumento();
        boolean eliminado = false;
        if (mapeo.remove(clave) != null) {
            eliminado = true;
        }
        return eliminado;
    }

    private static boolean modificarCliente(HashMap mapeo) {
        Scanner sc = new Scanner(System.in);
        boolean modificado = false;
        System.out.println("A continuacion brinde los datos del cliente que quiere modificar");
        String clave = preguntarDocumento();
        Cliente elCliente = (Cliente) mapeo.get(clave);
        boolean seguir = true;
        if (elCliente != null) {
            while (seguir) {
                System.out.println("Que quiere modificar?");
                System.out.println("1. Nombres del Cliente");
                System.out.println("2. Apellido del Cliente");
                System.out.println("3. Telefono del Cliente");
                System.out.println("4. Terminar");
                int decision = sc.nextInt();
                switch (decision) {
                    case 1:
                        System.out.println("Cual sera el nuevo nombre?");
                        elCliente.setNombre(sc.next().toUpperCase());
                        modificado = true;
                        break;
                    case 2:
                        System.out.println("Cual sera el nuevo apellido?");
                        elCliente.setApellido(sc.next().toUpperCase());
                        modificado = true;
                        break;
                    case 3:
                        System.out.println("Cual sera el nuevo telefono?");
                        elCliente.setTelefono(sc.next());
                        modificado = true;
                        break;
                    case 4:
                        seguir = false;
                        break;
                    default:
                        System.out.println("Numero no valido. Intente de nuevo");
                        break;
                }
            }
        } else {
            System.out.println("No existe el cliente que quiere modificar");
        }
        return modificado;

    }

    private static String buscarCliente(HashMap mapeo) {
        System.out.println("A continuacion brinde los datos del cliente que quiere ver");
        String clave = preguntarDocumento();
        String s;
        Cliente elCliente = (Cliente) mapeo.get(clave);
        if (elCliente != null) {
            s = elCliente.toString();
        } else {
            s = " No se encontro el Cliente con documento " + clave;
        }
        return s;
    }

    public static void operacionesRutas(GrafoEtiquetado mapaRutas, Logger logger) {
        Scanner sc = new Scanner(System.in);
        int decision;
        boolean seguir = true;

        while (seguir) {
            System.out.println("1. Agregar ruta");
            System.out.println("2. Eliminar ruta");
            System.out.println("3. Volver al menu principal");
            decision = sc.nextInt();
            switch (decision) {
                case 1:
                    if (agregarRuta(mapaRutas)) {
                        logger.info("Se agrego una ruta");
                    }
                    break;
                case 2:
                    if (eliminarRuta(mapaRutas)) {
                        logger.info("Se elimino una ruta");
                    }
                    break;

                case 3:
                    System.out.println("Volviendo al menu principal");
                    seguir = false;
                    break;
                default:
                    System.out.println("Numero no aceptado. Intente de nuevo");
                    break;
            }
        }
    }

    private static boolean agregarRuta(GrafoEtiquetado mapaRutas) {
        //pregunta las dos ciudades entre la ruta, y manda a insertar en el arco. Dentro del insertarArco se hace la verificacion de que existan ambas ciudades
        Scanner sc = new Scanner(System.in);
        int origen, destino;
        System.out.println("Primera ciudad: ");
        origen = preguntarCodigoPostal();
        System.out.println("Segunda ciudad: ");
        destino = preguntarCodigoPostal();
        System.out.println("Cuantos km tiene la ruta?");
        int km = sc.nextInt();
        boolean exito = mapaRutas.insertarArco(origen, destino, km);
        if (exito) {
            System.out.println("Se agrego la ruta");
        } else {
            System.out.println("No se pudo agregar la ruta");
        }

        return exito;
    }

    private static boolean eliminarRuta(GrafoEtiquetado mapaRutas) {
        //pregunta las dos ciudades entre la ruta, y manda a eliminar el arco
        int origen, destino;
        System.out.println("Primera ciudad: ");
        origen = preguntarCodigoPostal();
        System.out.println("Segunda ciudad: ");
        destino = preguntarCodigoPostal();
        boolean exito = mapaRutas.eliminarArco(origen, destino);
        if (exito) {
            System.out.println("Se elimino la ruta");
        } else {
            System.out.println("No se pudo eliminar");
        }
        return exito;
    }

    public static void operacionesViajes(Logger logger, Diccionario dicCiudad, HashMap mapeo, GrafoEtiquetado mapaRutas, Map<ClaveSolicitud, Lista> solicitudes) {

        Scanner sc = new Scanner(System.in);
        int decision;
        boolean seguir = true;

        while (seguir) {
            System.out.println("1. Obtener camino que pase por menos ciudades");
            System.out.println("2. Obtener camino mas corto");
            System.out.println("3. Verificar pedidos entre dos ciudades");
            System.out.println("4. Verificar camino perfecto");
            System.out.println("5. Volver al menu principal");
            decision = sc.nextInt();
            switch (decision) {
                case 1:
                    Lista masCorta = caminoMasCorto(mapaRutas);
                    if (!masCorta.esVacia()) {
                        System.out.println(masCorta);
                    } else {
                        System.out.println("No hay camino posible entre esas ciudades");
                    }
                    logger.info("Se pregunto por el camino que pase por menos ciudades");
                    break;
                case 2:
                    Lista menosKm = caminoMenosKm(mapaRutas);
                    if (!menosKm.esVacia()) {
                        System.out.println(menosKm);
                    } else {
                        System.out.println("No hay camino posible entre esas ciudades");
                    }
                    logger.info("Se pregunto por el camino con menos km");
                    break;
                case 3:
                    int cantidad = verificarPedido(solicitudes);
                    if (cantidad > 0) {
                        System.out.println("Se necesita un camion con " + cantidad + " metros cubicos disponibles");
                    }
                    logger.info("Se verificaron los pedidos entre dos ciudades");
                    break;
                case 4:
                    boolean perfecto = caminoPerfecto(solicitudes, mapaRutas, dicCiudad);
                    if (perfecto) {
                        System.out.println("Se encontro un camino perfecto");
                    } else {
                        System.out.println("No se encontro un camino perfecto");
                    }
                    logger.info("Se pregunto por un camino perfecto");
                    break;

                case 5:
                    seguir = false;
                    break;
                default:
                    System.out.println("Numero no aceptado. Intente de nuevo");
                    break;
            }
        }
    }

    private static Lista caminoMasCorto(GrafoEtiquetado mapaRutas) {
        int origen, destino;
        System.out.println("Primera ciudad: ");
        origen = preguntarCodigoPostal();
        System.out.println("Segunda ciudad: ");
        destino = preguntarCodigoPostal();
        return mapaRutas.caminoMasCorto(origen, destino);
    }

    private static Lista caminoMenosKm(GrafoEtiquetado mapaRutas) {
        int origen, destino;
        System.out.println("Primera ciudad: ");
        origen = preguntarCodigoPostal();
        System.out.println("Segunda ciudad: ");
        destino = preguntarCodigoPostal();
        return mapaRutas.caminoMenorEtiqueta(origen, destino);
    }

    private static int verificarPedido(Map<ClaveSolicitud, Lista> solicitudes) {
        int origen, destino;
        int cantidad = 0;
        System.out.println("Primera ciudad: ");
        origen = preguntarCodigoPostal();
        System.out.println("Segunda ciudad: ");
        destino = preguntarCodigoPostal();
        ClaveSolicitud claveSol = new ClaveSolicitud(origen, destino);
        Lista lis = solicitudes.get(claveSol);
        if (lis != null && !lis.esVacia()) {

            int longitud = lis.longitud();
            for (int i = 1; i <= longitud; i++) {
                Solicitud unaSolicitud = (Solicitud) lis.recuperar(i);
                cantidad += unaSolicitud.getCantMetros();
            }

        } else {
            System.out.println("No existen solicitudes entre esas ciudades");
        }
        return cantidad;
    }

    private static boolean caminoPerfecto(Map<ClaveSolicitud, Lista> solicitudes, GrafoEtiquetado mapaRutas, Diccionario dicCiudad) {
        boolean existe = true;
        System.out.println("Primero hay que crear la lista de ciudades");
        Object[] lista = crearListaCiudades(dicCiudad);

        if (mapaRutas.verificarCaminoLista(lista)) {
            int i = 0, longitud = lista.length;

            while (i < longitud - 1 && existe) {
                int j = i + 1;
                existe = false;
                int primero = (int) lista[i];
                while (!existe && j < longitud) {
                    ClaveSolicitud actual = new ClaveSolicitud(primero, (int) lista[j]);
                    //la clave solo esta contenida si hay por lo menos una solicitud
                    existe = solicitudes.containsKey(actual);

                    System.out.println(existe + "" + actual);
                    j++;
                }
                i++;
            }
        }

        return existe;

    }

    private static Object[] crearListaCiudades(Diccionario dicCiudad) {
        Scanner sc = new Scanner(System.in);
        boolean seguir = false;
        int nro = 0, cod;
        int largo = 0;
        while (!seguir) {
            System.out.println("Cuantas ciudades tendra la lista? Minimo dos");
            largo = sc.nextInt();
            if (largo < 3) {
                System.out.println("Numero no valido. Minimo dos");
            } else {
                seguir = true;
            }
        }

        Object[] lista = new Object[largo];
        while (nro < largo) {
            System.out.println("Ingrese los datos de la ciudad N°" + (nro + 1));
            cod = preguntarCodigoPostal();
            if (dicCiudad.existeClave(cod)) {
                lista[nro] = cod;
                nro++;
            } else {
                System.out.println("La ciudad ingresada no esta en el sistema. Pruebe con otra");
            }

        }
        return lista;
    }

    private static String preguntarDocumento() {
        Scanner sc = new Scanner(System.in);
        boolean aceptado = false;
        String tipo = "";
        while (!aceptado) {
            aceptado = true;
            System.out.println("Cual es el tipo de Dni?\n 1. Dni \n 2. Pasaporte");
            int decision = sc.nextInt();
            switch (decision) {
                case 1:
                    tipo = "DNI";
                    break;
                case 2:
                    tipo = "PAS";
                    break;
                default:
                    aceptado = false;
                    System.out.println("Tipo no aceptado");
                    break;
            }
        }
        System.out.println("Cual es el numero de documento?");
        int nro = sc.nextInt();
        while (!verificarDni(nro)) {
            System.out.println("Numero no valido. Intente de nuevo");
            System.out.println("Cual es el numero de documento?");
            nro = sc.nextInt();
        }

        return tipo + nro;
    }

    private static ClaveSolicitud preguntarClaveSolicitud() {
        //crea la ClaveSolicitud

        ClaveSolicitud clave;
        int origen, destino;
        System.out.println("Cual es el origen de la solicitud?");
        origen = preguntarCodigoPostal();
        System.out.println("Cual es el destino de la solicitud?");
        destino = preguntarCodigoPostal();
        clave = new ClaveSolicitud(origen, destino);
        return clave;

    }

    private static int preguntarCodigoPostal() {
        //pregunta y verifica el codigo postal
        Scanner sc = new Scanner(System.in);
        int cod = 0;
        boolean aceptado = false;
        while (!aceptado) {
            System.out.println("Cual es el codigo postal de la ciudad?");
            cod = sc.nextInt();
            if (verificarCodigoPostal(cod)) {
                aceptado = true;
            } else {
                System.out.println("Codigo no Aceptado. Intente de nuevo");
            }
        }
        return cod;
    }

    private static boolean verificarDni(int dni) {
        //verifica que el numero de documento este dentro del rango de numero de documento argentinos
        return dni > 1000000 && dni < 99999999;
    }

    private static boolean verificarCodigoPostal(int cod) {
        //verifica que el codigo postal este dentro del rango de codigos postales argentinos
        return cod >= 1000 && cod < 10000;
    }

    public static void mostrarSistema(Diccionario dicCiudad, HashMap mapeo, GrafoEtiquetado mapaRutas, Map<ClaveSolicitud, Lista> solicitudes) {
        //muestra un menu que pregunta cual de las estructuras quiere ver el usuario
        Scanner sc = new Scanner(System.in);
        int decision;
        boolean seguir = true;
        String resultado;

        while (seguir) {
            System.out.println("1. Mostrar Diccionario Ciudad");
            System.out.println("2. Mostrar Mapeo Clientes");
            System.out.println("3. Mostrar Mapa de Rutas");
            System.out.println("4. Mostrar Solicitudes");
            System.out.println("5. Volver al menu principal");
            decision = sc.nextInt();
            switch (decision) {
                case 1:
                    System.out.println(dicCiudad.toString());
                    break;
                case 2:
                    resultado = "";
                    for (Object clave : mapeo.keySet()) {
                        Cliente cliente = (Cliente) mapeo.get(clave);
                        resultado = resultado + cliente.toString() + "\n";

                    }
                    System.out.println(resultado);
                    break;
                case 3:
                    System.out.println(mapaRutas.toString());
                    break;
                case 4:
                    resultado = "";
                    for (ClaveSolicitud clave : solicitudes.keySet()) {
                        resultado = resultado + clave + ": \n";
                        Lista lista = solicitudes.get(clave);
                        int longitud = lista.longitud();
                        for (int i = 1; i <= longitud; i++) {
                            resultado = resultado + lista.recuperar(i) + "\n";
                        }
                        resultado = resultado + "\n";
                    }
                    System.out.println(resultado);
                    break;

                case 5:
                    System.out.println("Volviendo al menu principal");
                    seguir = false;
                    break;
                default:
                    System.out.println("Numero no aceptado. Intente de nuevo");
                    break;
            }
        }
    }
}
