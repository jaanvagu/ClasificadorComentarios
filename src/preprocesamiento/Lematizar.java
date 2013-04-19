package preprocesamiento;

/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 16 de 2013
 */


import java.util.*;
import org.annolab.tt4j.*;

public class Lematizar {

    private Vector<String> listaMensajesLematizados;
    private TreeTaggerWrapper treeTagger; //Variable (objeto) de tipo TreeTagger, la cual se encarga de encontrar el lema de una palabra
    private StringBuilder mensajeLematizado;

    //Constructor que recibe una lista de mensajes, y obtiene el lema de cada una de las palabras contenidas en los mensajes.
    //Almacena en una nueva lista de mensajes, los cuales contienen los lemas de las palabras originales.
    public Lematizar(Vector<String> listaMensajes){
        
        treeTagger = new TreeTaggerWrapper<String>();
        listaMensajesLematizados = new Vector();       
        if(!listaMensajes.isEmpty()){
            ejecutarLematizacion(listaMensajes);
            System.out.println("--Lematizacion Realizada--");
        }
    }
    
    private void ejecutarLematizacion(Vector<String> listaMensajes){        
        System.setProperty("treetagger.home", "/TreeTagger");
        try{
            for(int i=0; i<listaMensajes.size(); i++){
                mensajeLematizado = new StringBuilder();
                treeTagger.setModel("/treetagger/lib/spanish.par");                
                treeTagger.setHandler(new TokenHandler<String>() {                        
                        public void token(String token, String pos, String lemma) {
                            if(!lemma.isEmpty()){
                                mensajeLematizado.append(lemma).append(" ");
                                System.out.println(token+": "+lemma+" - "+pos);
                            }
                        }
                });                
                Vector palabrasMensaje = mensajeAVectorPalabras(listaMensajes.elementAt(i));
                treeTagger.process(palabrasMensaje);
                quitarEspacioEnBlancoAlFinalDeMensaje();
                listaMensajesLematizados.addElement(mensajeLematizado.toString());                
            }            
        }
        catch(Exception e){
            System.out.println("Error en lematizar: "+e.getMessage());
        }
        finally {
                treeTagger.destroy();
        }
    }

    private Vector<String> mensajeAVectorPalabras(String mensaje){
        StringTokenizer tokensMensaje = new StringTokenizer(mensaje);
        Vector<String> palabrasMensaje = new Vector();
        while(tokensMensaje.hasMoreTokens()){
            String tempPalabraMensaje = tokensMensaje.nextToken().trim();
            if(!tempPalabraMensaje.isEmpty()){
                palabrasMensaje.addElement(tempPalabraMensaje);
            }
        }
        return palabrasMensaje;
    }

    private void quitarEspacioEnBlancoAlFinalDeMensaje(){
        StringTokenizer tokensMensaje = new StringTokenizer(mensajeLematizado.toString());
        StringBuilder tempMensaje = new StringBuilder();
        while(tokensMensaje.hasMoreTokens()){
            if(tokensMensaje.countTokens()!=1){
                tempMensaje.append(tokensMensaje.nextToken().trim()).append(" ");
            }
            else{
                tempMensaje.append(tokensMensaje.nextToken().trim());                
            }
        }
        mensajeLematizado = tempMensaje;
    }

    public Vector<String> obtenerListaMensajesLematizados(){
        return listaMensajesLematizados;
    }
    
    public static void main(String[] args) { 
        Vector<String> v = new Vector<String>();
        v.addElement("yo estoy cansado de esta mierda jajajaja");
        Lematizar l = new Lematizar(v);
    }
}