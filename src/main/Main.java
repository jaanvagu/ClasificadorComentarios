/*
 * @author Jairo Andrés
 * Ultima modificacion: Mayo 7 de 2013
 */

package main;

import LP.GestionarLabelPropagation;
import LP.GestionarSemillasLP;
import entrada_salida.GestionarArchivos;
import entrada_salida.LeerArchivoCSV;
import entrada_salida.LeerArchivoSalidaLP;
import estructuras.Comentario;
import estructuras.ComentarioNormalizado;
import evaluacion.GestionarIndicadores;
import extraccion_caracteristicas.GestionarDistancias;
import extraccion_caracteristicas.GestionarVectorPalabras;
import java.util.Vector;
import org.apache.log4j.Logger;
import preprocesamiento.Lematizar;
import preprocesamiento.Preprocesamiento;
import svm.SVM;
import utiles.ArchivoConfiguracionLP;
import utiles.DistribuirDatos;
import utiles.ExtenderComentarios;
import utiles.SonidoBuffer;

public class Main {
    static SonidoBuffer sonido = new SonidoBuffer(2);
    
    private final static Logger LOG = Logger.getLogger(Main.class);

    private static GestionarArchivos gestionArchivos;
    private static LeerArchivoCSV leerArchivoCSV;
    private static Preprocesamiento preprocesamiento;
    private static Lematizar lematizar;
    private static ExtenderComentarios extenderComentarios;
    private static DistribuirDatos distribuir;
    private static GestionarVectorPalabras gestionVectorPalabras;
    private static GestionarDistancias gestionDistancias;
    private static GestionarLabelPropagation gestionLabelPropagation;
    private static GestionarSemillasLP gestionSemillasLP;
    private static LeerArchivoSalidaLP leerArchivoSalidaLP;
    private static GestionarIndicadores gestionIndicadores;
    private static SVM SVM;

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
        gestionArchivos.guardarComentariosNormalizados(listaComentariosNormalizados,"Normalizado");
        LOG.info("Normalizacion terminada");
    }

    private static void distribuir_EjecucionSecuencial(){
        String[] nombresArchivos = {"Balance","Coca Cola","Dog Chow","Don Julio","Kotex","Nike","Sony","Top Terra"};
        int[] cantDatos =          {   1000,      1000,       1000,      600,      1000,  1000,  1000,    1000};
//        int[] cantDatos =          {   400,      400,       400,       400,        400,   400,   400,     400};
        for(int i=0; i<nombresArchivos.length; i++){
            gestionArchivos = new GestionarArchivos();
            listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados("Utiles Normalizados/",
                    "Consolidado "+nombresArchivos[i] + " - Utiles Normalizados", false);
            distribuir = new DistribuirDatos(listaComentariosNormalizados);
//            distribuir.generarListaSoloComentarioUtiles();
            listaComentariosNormalizados = distribuir.generarDistribucionUniformeNDatos(cantDatos[i]);
//            listaComentariosNormalizados = distribuir.generarDistribucionUniformeNDatos(cantDatos[i]);
//            listaComentariosNormalizados = distribuir.eliminarComentariosConPalabrasRepetidasEnExceso();
//            distribuir.eliminarComentariosConEtiquetasMalas();
        }
    }
    
    private static void extender_EjecucionSecuencial(){
        String[] nombresArchivos = {"Balance","Coca Cola","Dog Chow","Don Julio","Kotex","Nike","Sony","Top Terra"};
        String[] cantDatos = {"400","400","400","400","400","400","400","400"};
        for(int i=0; i<nombresArchivos.length; i++){
            gestionArchivos = new GestionarArchivos();
            listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados("Distribuidos Uniformemente Todas_Etiquetas/",
                    "Consolidado "+nombresArchivos[i] + " - DistribuidosUniforme "+cantDatos[i], false);
            
            extenderComentarios = new ExtenderComentarios(listaComentariosNormalizados);
            extenderComentarios.extender(false, true);
        }
    }
    
    private static void configParaLP_EjecucionSecuencial(){
        String[] nombresArchivos = {"Balance","Coca Cola","Dog Chow","Don Julio","Kotex","Nike","Sony","Top Terra"};
        String[] cantDatos = {      "1000",      "1000",   "1000",      "600",   "1000", "1000","1000",   "1000"};
//        String[] cantDatos = {"400","400","400","400","400","400","400","400"};
        for(int i=0; i<nombresArchivos.length; i++){
            gestionArchivos = new GestionarArchivos();
            listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados("",
                    "Consolidado "+nombresArchivos[i] + " - DistribuidosUniforme "+cantDatos[i], false);
            
            gestionVectorPalabras = new GestionarVectorPalabras(listaComentariosNormalizados);
            gestionVectorPalabras.contruirVectorDePalabras();
            gestionVectorPalabras.generarVectoresDeFrecuenciasDePalabras();
            
            gestionDistancias = new GestionarDistancias();
            gestionDistancias.calcularSimilitudCosenoEntreCadaParDeComentarios(gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
            gestionSemillasLP = new GestionarSemillasLP(listaComentariosNormalizados);
            gestionSemillasLP.generarArchivoSemillas(20);
            gestionSemillasLP.generarArchivoSolucion();
            ArchivoConfiguracionLP.generarArchivo(listaComentariosNormalizados.size());
        }
    }
    
    private static void indicadoresLP_EjecucionSecuencial(){
        String[] nombresArchivos = {"Balance","Coca Cola","Dog Chow","Don Julio","Kotex","Nike","Sony","Top Terra"};
        String[] cantDatos = {       "1000",  "1000",       "1000",    "600",     "1000","1000","1000", "1000"};
//        String[] cantDatos =       {"400","400","400","400","400","400","400","400"};
        for(int i=0; i<nombresArchivos.length; i++){
            gestionArchivos = new GestionarArchivos();
            listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados("",
                    "Consolidado "+nombresArchivos[i] + " - DistribuidosUniforme "+cantDatos[i], false);
            
            leerArchivoSalidaLP = new LeerArchivoSalidaLP(false);
            gestionIndicadores = new GestionarIndicadores(leerArchivoSalidaLP.obtenerListaEtiquetasCorrespondientes(), 
                    leerArchivoSalidaLP.obtenerListaEtiquetasPropagadasLP());
            gestionIndicadores.calcularIndicadores("LP");
//            gestionIndicadores.imprimirIndicadoresParaGrafica();
        }
    }
    
    private static void SVM_EjecucionSecuencial(){
        String[] nombresArchivos = {"Balance","Coca Cola","Dog Chow","Don Julio","Kotex","Nike","Sony","Top Terra"};
//        String[] cantDatos = {       "1000",  "1000",       "1000",    "600",     "1000","1000","1000", "1000"};
        String[] cantDatos =       {"400","400","400","400","400","400","400","400"};
        for(int i=0; i<nombresArchivos.length; i++){
            gestionArchivos = new GestionarArchivos();
            listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados("",
                    "Consolidado "+nombresArchivos[i] + " - DistribuidosUniformeCanDefi "+cantDatos[i], false);
            
            gestionVectorPalabras = new GestionarVectorPalabras(listaComentariosNormalizados);
            gestionVectorPalabras.contruirVectorDePalabras();
            gestionVectorPalabras.generarVectoresDeFrecuenciasDePalabras();
            
            SVM = new SVM(listaComentariosNormalizados, gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
            SVM.ejecutarSVM(20);
            
            gestionIndicadores = new GestionarIndicadores(SVM.getTabla_Etiqueta_AparicionesTotalesEstimadas(),SVM.getTabla_Etiqueta_AparicionesCorrectas(), SVM.getTabla_Etiqueta_AparicionesTotalesCorrespondientes());
            gestionIndicadores.calcularIndicadores("SVM");
            gestionIndicadores.imprimirIndicadoresParaGrafica();
        }
    }
    
    public static void main(String[] args) {
        
        // ********* LECTURA Y PREPROCESAMIENTO *********
//        leerArchivoCSV = new LeerArchivoCSV();
//        leerArchivoCSV.leerYAlmacenarLineasCSV();
//        preprocesamiento = new Preprocesamiento(leerArchivoCSV.obtenerListaComentariosLeidos());
//        lematizar = new Lematizar(preprocesamiento.obtenerMensajesProcesados());
//        normalizarComentarios(lematizar.obtenerListaMensajesLematizados(), leerArchivoCSV.obtenerListaComentariosLeidos());
        
        // ********* CARGAR COMENTARIOS NORMALIZADOS *********
//        gestionArchivos = new GestionarArchivos();
//        gestionArchivos.limpiaCarpetaArchivosPrueba(0);
//        listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados("Extendidos WN Distribuidos Uniforme 10_Etiquetas", "", true);
//        listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados("Distribuidos Uniformemente 10_Etiquetas SinMalas", "", true);
//        listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados("Utiles Normalizados", "", true);
//        listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados("","", true);
        
        // ********* EXTENSIÓN DE COMENTARIOS *********
//        extenderComentarios = new ExtenderComentarios(listaComentariosNormalizados);
//        listaComentariosNormalizados = new Vector(extenderComentarios.extender(false, true));

        // ********* DISTRIBUCIÓN DE DATOS *********
//        distribuir = new DistribuirDatos(listaComentariosNormalizados);
//        distribuir.eliminarComentariosConEtiquetasMalas();
//        listaComentariosNormalizados = distribuir.generarDistribucionUniformeNDatos(400,10);
//        listaComentariosNormalizados = distribuir.generarDistribucionUniformeNDatos(1000);
//        distribuir.generarDistribucionProporcionalNDatos(100);
//        listaComentariosNormalizados = new Vector(distribuir.eliminarComentariosConPalabrasRepetidasEnExceso());
//        distribuir.generarListaSoloComentarioUtiles();
        
        // ********* IMPRIMIR TEXTO DE LOS COMENTARIOS *********
//        for(int i=0; i<listaComentariosNormalizados.size(); i++){
//            System.out.println(listaComentariosNormalizados.elementAt(i).obtenerListaPalabrasEnComentario());
//        }
        
        // ********* EXTRACCIÓN DE CARACTERÍSTICAS *********
//        gestionVectorPalabras = new GestionarVectorPalabras(listaComentariosNormalizados);
//        gestionVectorPalabras.contruirVectorDePalabras();
//        gestionVectorPalabras.generarVectoresDeFrecuenciasDePalabras();
//        gestionVectorPalabras.obtenerListaVectoresDeFrecuencias();
        
        // ********* SVM EJECUCIÓN *********
//        SVM = new SVM(listaComentariosNormalizados, gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
//        SVM.ejecutarSVM(20);
        
        // ********* INDICADORES SVM *********
//        gestionIndicadores = new GestionarIndicadores(SVM.getTabla_Etiqueta_AparicionesTotalesEstimadas(),SVM.getTabla_Etiqueta_AparicionesCorrectas(), SVM.getTabla_Etiqueta_AparicionesTotalesCorrespondientes());
//        gestionIndicadores.calcularIndicadores("SVM");
//        gestionIndicadores.imprimirIndicadoresParaGrafica();
        
        // ********* GRAFO, SEMILLAS Y CONFIGURACIÓN LABEL PROPAGATION *********
//        gestionDistancias = new GestionarDistancias();
//        gestionDistancias.calcularSimilitudCosenoEntreCadaParDeComentarios(gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
//        gestionSemillasLP = new GestionarSemillasLP(listaComentariosNormalizados);
//        gestionSemillasLP.generarArchivoSemillas(20);
//        gestionSemillasLP.generarArchivoSolucion();
//        ArchivoConfiguracionLP.generarArchivo(listaComentariosNormalizados.size());
        
//        // ********* LABEL PROPAGATION EJECUCIÓN *********
//        gestionLabelPropagation = new GestionarLabelPropagation();
//        gestionLabelPropagation.ejecutarLabelPropagation();
//        
//        // ********* INDICADORES PARA LABEL PROPAGATION *********
//        leerArchivoSalidaLP = new LeerArchivoSalidaLP(true);
//        gestionIndicadores = new GestionarIndicadores(leerArchivoSalidaLP.obtenerListaEtiquetasCorrespondientes(), leerArchivoSalidaLP.obtenerListaEtiquetasPropagadasLP());
//        gestionIndicadores.calcularIndicadores("LP");
//        gestionIndicadores.imprimirIndicadoresParaGrafica();
        
        // ********* EJECUCIONES SECUENCIALES *********
//        extender_EjecucionSecuencial();
//        distribuir_EjecucionSecuencial();
//        configParaLP_EjecucionSecuencial();
//        indicadoresLP_EjecucionSecuencial();
        SVM_EjecucionSecuencial();
        
        // ********* FINALIZACIONES *********
//        sonido.start();
//        System.setErr(null);
//        System.setOut(null);
        System.exit(0);
    }
}