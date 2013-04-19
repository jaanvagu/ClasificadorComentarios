package utiles;


import main.Main;
import entrada_salida.GestionarArchivos;

/* 
 * @author Jairo Andrés
 * Ultima modificacion: Abril 16 de 2013
 */


public class ArchivoConfiguracionLP {
    
    private static GestionarArchivos gestionArchivosConfigLP = new GestionarArchivos();
    
    public static void generarArchivo( int cantDatos){
        
        gestionArchivosConfigLP.crearArchivoTexto("configuracion", Main.listaComentariosNormalizados.size());      
        
        String nombreArchivo = GestionarArchivos.nombreArchivoSinExtension;
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("graph_file = Datos_"+nombreArchivo+"/input_graph_"+nombreArchivo+"_"+cantDatos);
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("data_format = edge_factored");
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("seed_file = Datos_"+nombreArchivo+"/seeds_"+nombreArchivo+"_"+cantDatos);
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("gold_labels_file = Datos_"+nombreArchivo+"/gold_labels_"+nombreArchivo+"_"+cantDatos);
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("test_file = Datos_"+nombreArchivo+"/gold_labels_"+nombreArchivo+"_"+cantDatos);
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("iters = 1");
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("verbose = false");
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("prune_threshold = 0");
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("algo = lp_zgl");
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("mu1 = 1");
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("mu2 = 1e-2");
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("mu3 = 1e-2");
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("beta = 2");
        gestionArchivosConfigLP.escribirLineaEnArchivoTexto("output_file = Datos_"+nombreArchivo+"/label_prop_"+nombreArchivo+"_"+cantDatos);
        
        gestionArchivosConfigLP.cerrarArchivoTexto();
        System.out.println("--Archivo configuracion generado--");
    }
            
}
