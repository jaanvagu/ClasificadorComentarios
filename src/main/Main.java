/*
 * @author Jairo Andrés
 * Ultima modificacion: Septiembre 13 de 2013
 */


import java.util.Vector;

public class Main {

    private static LeerArchivoCSV leerArchivoCSV;
    private static Preprocesamiento preprocesamiento;
    private static Lematizar lematizar;
    private static GestionarArchivos gestionArchivos;
    private static GestionarVectorPalabras gestionVectorPalabras;
    private static GestionarDistancias gestionDistancias;
    private static GestionarSemillas gestionSemillas;
    private static LeerArchivoSalidaLP leerArchivoSalidaLP;
    private static GestionarIndicadores gestionIndicadores;

    //Variable que almacena los comentarios de la siguiente manera:
    //  Lista de Comentarios Normalizados = [ComentarioNormalizado(1), ComentarioNormalizado(2), ..., ComentarioNormalizado(N)  ]
    //  Donde ComentarioNormalizado(i):
    //      Lista de palabras del comentario = [palabra1, palabra2, ..., palabraN]
    //      Etiquetas = [etiqueta1, etiqueta2, ..., etiquetaN]
    public static Vector<ComentarioNormalizado> listaComentariosNormalizados;

    //Método que construye la lista de comentarios normalizados, a partir de los comentarios preprocesados y lematizados,
    //y las etiquetas relacionadas a cada comentario. 
    private static void normalizarComentarios(Vector<String> listaComentariosPreprocesadosYLematizados,
                                              Vector<Comentario> listaComentariosOriginal){
        listaComentariosNormalizados = new Vector();
        for(int i=0; i<listaComentariosOriginal.size(); i++){
            ComentarioNormalizado comentarioNormalizadoActual;
            comentarioNormalizadoActual = new ComentarioNormalizado(listaComentariosPreprocesadosYLematizados.elementAt(i),
                                                                    listaComentariosOriginal.elementAt(i).obtenerEtiquetas());
            listaComentariosNormalizados.addElement(comentarioNormalizadoActual);
        }
        gestionArchivos = new GestionarArchivos();
        gestionArchivos.guardarComentariosNormalizados(listaComentariosNormalizados);
    }           

    
    public static void main(String[] args) {                
        /*
        leerArchivoCSV = new LeerArchivoCSV();
        leerArchivoCSV.leerYAlmacenarLineasCSV();
        preprocesamiento = new Preprocesamiento(leerArchivoCSV.obtenerListaComentariosLeidos());
        lematizar = new Lematizar(preprocesamiento.obtenerMensajesProcesados());
        normalizarComentarios(lematizar.obtenerListaMensajesLematizados(), leerArchivoCSV.obtenerListaComentariosLeidos());
        //*/

//        gestionArchivos = new GestionarArchivos();
//        listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados();
//        DistribuirDatos distribuir = new DistribuirDatos(listaComentariosNormalizados);
//        listaComentariosNormalizados = new Vector(distribuir.generarDistribucionNDatos(200));        
//        System.out.println("Tam Com Distribuidos: "+listaComentariosNormalizados.size());
//
//        gestionVectorPalabras = new GestionarVectorPalabras();
//        gestionDistancias = new GestionarDistancias();
//        gestionSemillas = new GestionarSemillas(listaComentariosNormalizados);
//        gestionVectorPalabras.contruirVectorDePalabras();
//        gestionVectorPalabras.generarVectoresDeFrecuenciasDePalabras();
////        gestionDistancias.calcularDistanciaEuclideanaEntreCadaParDeComentarios(gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
////        gestionDistancias.calcularDistanciaEuclideanaInvertidaEntreCadaParDeComentarios(gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
//        gestionDistancias.calcularSimilitudCosenoEntreCadaParDeComentarios(gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
//        gestionSemillas.generarArchivoSemillas(20);
//        gestionSemillas.generarArchivoSolucion();
        leerArchivoSalidaLP = new LeerArchivoSalidaLP();
        gestionIndicadores = new GestionarIndicadores(leerArchivoSalidaLP.obtenerListaEtiquetasCorrespondientes(), leerArchivoSalidaLP.obtenerListaEtiquetasPropagadasLP());
        gestionIndicadores.calcularIndicadorPresicionParaEtiquetas();
        gestionIndicadores.calcularIndicadorRecallParaEtiquetas();
        gestionIndicadores.calcularIndicadorFScoreParaEtiquetas();
        gestionIndicadores.calcularAciertosTotales();
    }
    
}    