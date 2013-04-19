package main;

/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 16 de 2013
 */

import LP.GestionarSemillasLP;
import SVM.SVM;
import entrada_salida.GestionarArchivos;
import entrada_salida.LeerArchivoCSV;
import entrada_salida.LeerArchivoSalidaLP;
import estructuras.Comentario;
import estructuras.ComentarioNormalizado;
import evaluacion.GestionarIndicadores;
import extraccion_caracteristicas.GestionarDistancias;
import extraccion_caracteristicas.GestionarVectorPalabras;
import java.util.Vector;
import preprocesamiento.Lematizar;
import preprocesamiento.Preprocesamiento;

public class Main {

    private static LeerArchivoCSV leerArchivoCSV;
    private static Preprocesamiento preprocesamiento;
    private static Lematizar lematizar;
    private static GestionarArchivos gestionArchivos;
    private static GestionarVectorPalabras gestionVectorPalabras;
    private static GestionarDistancias gestionDistancias;
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
    }           

    
    public static void main(String[] args) {                
        
        // ********* LECTURA Y PREPROCESAMIENTO *********        
//        leerArchivoCSV = new LeerArchivoCSV();
//        leerArchivoCSV.leerYAlmacenarLineasCSV();
//        preprocesamiento = new Preprocesamiento(leerArchivoCSV.obtenerListaComentariosLeidos());
//        lematizar = new Lematizar(preprocesamiento.obtenerMensajesProcesados());
//        normalizarComentarios(lematizar.obtenerListaMensajesLematizados(), leerArchivoCSV.obtenerListaComentariosLeidos());                

//        gestionArchivos = new GestionarArchivos();
//        listaComentariosNormalizados = gestionArchivos.cargarComentariosNormalizados(2);

        // ********* DISTRIBUCIÓN DE DATOS *********
//        DistribuirDatos distribuir = new DistribuirDatos(listaComentariosNormalizados);  
////        distribuir.generarListaSoloComentarioUtiles(); 
//        listaComentariosNormalizados = new Vector(distribuir.generarDistribucionNDatos(400));        
        
        // ********* IMPRIMIR TEXTO DE LOS COMENTARIOS *********
//        for(int i=0; i<listaComentariosNormalizados.size(); i++){
//            System.out.println(listaComentariosNormalizados.elementAt(i).obtenerListaPalabrasEnComentario());
//        }
        
        // ********* EXTRACCIÓN DE CARACTERÍSTICAS *********
//        gestionVectorPalabras = new GestionarVectorPalabras();        
//        gestionVectorPalabras.contruirVectorDePalabras();
//        gestionVectorPalabras.generarVectoresDeFrecuenciasDePalabras();
        
        // ********* SVM *********
//        SVM = new SVM(listaComentariosNormalizados, gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
//        SVM.ejecutarSVM(20);     
        
        // ********* INDICADORES PARA SVM *********
//        gestionIndicadores = new GestionarIndicadores(SVM.getTabla_Etiqueta_AparicionesTotalesEstimadas(),SVM.getTabla_Etiqueta_AparicionesCorrectas(), SVM.getTabla_Etiqueta_AparicionesTotalesCorrespondientes());
//        gestionIndicadores.calcularIndicadores("SVM");
        
        // ********* LABEL PROPAGATION *********        
//        gestionDistancias = new GestionarDistancias();        
////        gestionDistancias.calcularDistanciaEuclideanaEntreCadaParDeComentarios(gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
////        gestionDistancias.calcularDistanciaEuclideanaInvertidaEntreCadaParDeComentarios(gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
//        gestionDistancias.calcularSimilitudCosenoEntreCadaParDeComentarios(gestionVectorPalabras.obtenerListaVectoresDeFrecuencias());
//        gestionSemillasLP = new GestionarSemillasLP(listaComentariosNormalizados);
//        gestionSemillasLP.generarArchivoSemillas(20);
//        gestionSemillasLP.generarArchivoSolucion();
//        ArchivoConfiguracionLP.generarArchivo(listaComentariosNormalizados.size());
        
        // ********* INDICADORES PARA LABEL PROPAGATION *********        
        leerArchivoSalidaLP = new LeerArchivoSalidaLP();
        gestionIndicadores = new GestionarIndicadores(leerArchivoSalidaLP.obtenerListaEtiquetasCorrespondientes(), leerArchivoSalidaLP.obtenerListaEtiquetasPropagadasLP());
        gestionIndicadores.calcularIndicadores("LP");
                
        System.setErr(null);
        System.setOut(null);
        System.exit(0);
    }
    
}    