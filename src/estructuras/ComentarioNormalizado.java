/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 21 de 2013
 */

package estructuras;

import java.io.Serializable;
import java.util.*;

public class ComentarioNormalizado implements Serializable{

    private Vector<String> listaPalabrasEnComentario; //Variable que almacena es un Vector, cada palabra de un comentario (mensaje).
    private Vector<String> etiquetas; // Variable que contiene las Etiquetas de un comentario.

    public ComentarioNormalizado(String mensaje, Vector<String> etiquetas){
        listaPalabrasEnComentario = new Vector();
        listaPalabrasEnComentario = mensajeAListaPalabras(mensaje);
        this.etiquetas = etiquetas;
    }
    
    public ComentarioNormalizado(Vector<String> listaPalabras, Vector<String> etiquetas){
        listaPalabrasEnComentario = new Vector(listaPalabras);
        this.etiquetas = etiquetas;        
    }

    //Método que a partir de el mensaje de un comentario, divide el mismo en palabras, y almacena estas últimas en la variable
    //listaPalabrasEnComentario.
    private Vector<String> mensajeAListaPalabras(String mensaje){
        Vector<String> listaPalabras = new Vector();
        StringTokenizer tokensMensaje = new StringTokenizer(mensaje);
        while(tokensMensaje.hasMoreTokens()){
            listaPalabras.addElement(tokensMensaje.nextToken());
        }
        return listaPalabras;
    }

    public Vector<String> obtenerListaPalabrasEnComentario(){
        return listaPalabrasEnComentario;
    }

    public Vector<String> obtenerEtiquetas(){
        return etiquetas;
    }
}
