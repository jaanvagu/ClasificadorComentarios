package entrada_salida;

/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 16 de 2013
 */


import entrada_salida.GestionarArchivos;
import java.io.*;
import java.util.*;

public class LeerArchivoSalidaLP {

    private Vector<String> listaEtiquetasCorrespondientes;
    private Vector<String> listaEtiquetasPropagadasLP;

    public LeerArchivoSalidaLP() {
        listaEtiquetasCorrespondientes = new Vector();
        listaEtiquetasPropagadasLP = new Vector();
        leerLineasArchivo();
    }

    private void leerLineasArchivo(){
        try {
            FileInputStream fis = new FileInputStream(new GestionarArchivos().obtenerRutaArchivo2(0));
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferlector = new BufferedReader(isr);
            String lineaLeida;            
            while ((lineaLeida = bufferlector.readLine()) != null){                                                 
                construirListaEtiquetasCorrespondientes(lineaLeida);
                construirListaEtiquetasPropagadasLP(lineaLeida);                
            }
        }
        catch (Exception e){                            
            System.out.println("Error en Leer archivo salida LP: "+e.getMessage());
        }        
    }

    //Método que se encarga de cosntruir una lista con las etiquetas solución o correspondientes de una lista de comentarios.
    // La lista es leída de un archivo generado por Label Propagation.
    private void construirListaEtiquetasCorrespondientes(String lineaLeida){
        StringTokenizer stLineaLeida = new StringTokenizer(lineaLeida);        
        if(!esEtiquetaSemilla(lineaLeida)){            
            StringBuilder sbEtiquetaLeida = new StringBuilder();
            stLineaLeida.nextToken(); //Salto de un token que corresponde al identificador del nodo (Ni).
            while(true){
                String parte = stLineaLeida.nextToken();
                if(parte.startsWith("1")) {
                    break;
                }
                sbEtiquetaLeida.append(parte).append(" ");
            }
            listaEtiquetasCorrespondientes.addElement(sbEtiquetaLeida.toString().trim());
        }
    }

    //Método que se encarga de cosntruir una lista con las etiquetas que fueran puestas por el Label Propagation,
    // La lista es leída de un archivo generado por Label Propagation.
    private void construirListaEtiquetasPropagadasLP(String lineaLeida){
        StringTokenizer stLineaLeida = new StringTokenizer(lineaLeida);
        if(!esEtiquetaSemilla(lineaLeida)){
            StringBuilder sbEtiquetaLeida = new StringBuilder();
            while(!stLineaLeida.nextToken().startsWith("1")){}
            while(true){
                String parte = stLineaLeida.nextToken();
                if(parte.startsWith("0")) {
                    break;
                }
                sbEtiquetaLeida.append(parte).append(" ");
            }
            listaEtiquetasPropagadasLP.addElement(sbEtiquetaLeida.toString().trim());
        }
    }

    //Método que identifica si la etiqueta leida, corresponde a una de las etiquetas que fue pasada como semilla de entrenamiento a LP.
    private boolean esEtiquetaSemilla(String linea){
        StringTokenizer stLinea = new StringTokenizer(linea);
        int cantTokens = stLinea.countTokens();
        for(int i=0; i<cantTokens-2; i++) {
            stLinea.nextToken();
        }
        String indicador = stLinea.nextToken();        
        if(indicador.equals("false")){
            return true;
        }
        else {
            return false;
        }
    }

    public Vector<String> obtenerListaEtiquetasCorrespondientes(){
        return listaEtiquetasCorrespondientes;
    }

    public Vector<String> obtenerListaEtiquetasPropagadasLP(){
        return listaEtiquetasPropagadasLP;
    }
}