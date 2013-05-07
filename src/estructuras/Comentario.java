/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 18 de 2013
 */

package estructuras;

import java.util.Vector;

public class Comentario {

    private String autor, mensaje, fuente;
    private Vector<String> etiquetas;

    public Comentario(String autor, String mensaje, String fuente, Vector<String> etiquetas){
        this.autor = autor;
        this.mensaje = mensaje;
        this.fuente = fuente;
        this.etiquetas = etiquetas;
    }

    public Comentario(String autor, String mensaje, String fuente){
        this.autor = autor;
        this.mensaje = mensaje;
        this.fuente = fuente;
    }

    public String obtenerAutor(){
        return autor;
    }

    public String obtenerMensaje(){
        return mensaje;
    }

    public String obtenerFuente(){
        return fuente;
    }

    public Vector<String> obtenerEtiquetas(){
        return etiquetas;
    }

    //Método para convertir los atributos de un objeto comentario a String (para imprimirlos).
    public String aString(){
        String comentario = "";
        String lineaInicialYFinal = "------------------\n";
        String listaEtiquetas = "Etiquetas: [ ";
        if(!this.etiquetas.isEmpty()){
            for(int i=0; i<(this.etiquetas.size()-1);i++)
                listaEtiquetas += this.etiquetas.elementAt(i)+", ";
            listaEtiquetas += this.etiquetas.elementAt(this.etiquetas.size()-1)+" ]";
        }
        else{
            listaEtiquetas += "SIN ETIQUETAS ]";            
        }

        comentario += lineaInicialYFinal+this.autor+" | "+this.mensaje+" | "+this.fuente+"\n"+listaEtiquetas+"\n"+lineaInicialYFinal;
        return comentario;
    }
}
