/*
 * @author Jairo Andrés
 * Ultima modificacion: Febrero 13 de 2013
 */


import java.util.Vector;

public class GestionarSemillas {
    
    private Vector<ComentarioNormalizado> listaComentariosNormalizados;
    private Vector<String> listaEtiquetasUsadasComoSemilla;
    private Vector<Integer> listaPosicionesSemillas;
    private GestionarArchivos gestionArchivos;

    public GestionarSemillas(Vector<ComentarioNormalizado> listaComentarios) {        
        listaComentariosNormalizados = new Vector(listaComentarios);
        listaEtiquetasUsadasComoSemilla = new Vector();
        listaPosicionesSemillas = new Vector();
        gestionArchivos = new GestionarArchivos();      
    }                        

    //Método que genera un archivo semilla, con todas las posibles etiquetas distintas en una lista de comentarios,
    // es decir, si existes E1, E2, E3,... E10 cada uno con una frecuencia de 10, y el porcentaje dado como parámetro es 10,
    // entonces, el archivo semilla estará contenido por E1, E2, E3,... E10, con una frecuencia de 1
    public void generarArchivoSemillas(int porcentaje){        
        gestionArchivos.crearArchivoTexto("seeds");      
        int porcentajeSemillas = (listaComentariosNormalizados.size()*porcentaje)/100;
        int maximoSemillasPorEtiqueta = porcentajeSemillas/10;
        int contador = 0;
        int i = 0;
        while(contador<porcentajeSemillas){
            if(i>=listaComentariosNormalizados.size())
                break;
            int x = i+1;
            String etiqueta = listaComentariosNormalizados.elementAt(i).obtenerEtiquetas().elementAt(0);
            if(contarAparicionEtiqueta(etiqueta)<maximoSemillasPorEtiqueta){
                gestionArchivos.escribirLineaEnArchivoTexto("N"+x+"\t"+etiqueta+"\t"+"1.0"); 
                listaEtiquetasUsadasComoSemilla.addElement(etiqueta);
                listaPosicionesSemillas.addElement(i);
                contador++;
            }              
            i++;
        }                
        System.out.println("Total semillas: "+listaEtiquetasUsadasComoSemilla.size());
        gestionArchivos.cerrarArchivoTexto();
    }

    //Método que genera un archivo con las etiquetas correspondientes a una lista de comentarios.
    // Suponga que una lista tiene 10 comentarios, donde 2 de ellos fueron pasados como semilla, entonces
    // se generará un archivo solución con las etiquetas que le corresponden a los 8 comentarios restantes.
    // Las etiquetas que se establecen como solución, han sido puestas por personal, son verídicas.
    public void generarArchivoSolucion(){
        gestionArchivos.crearArchivoTexto("gold_labels");   
        for(int i=0; i<listaComentariosNormalizados.size(); i++){
            String etiqueta = listaComentariosNormalizados.elementAt(i).obtenerEtiquetas().elementAt(0);
            int x = i+1;
            if(!listaPosicionesSemillas.contains(i)){                        
                gestionArchivos.escribirLineaEnArchivoTexto("N"+x+"\t"+etiqueta+"\t"+"1.0");                             
            }
        }                                        
        gestionArchivos.cerrarArchivoTexto();        
    }
    
    public int contarAparicionEtiqueta(String eti){

        int apariciones = 0;
        for(int i=0; i<listaEtiquetasUsadasComoSemilla.size(); i++){
            if(eti.equals(listaEtiquetasUsadasComoSemilla.elementAt(i)))
                apariciones++;
        }
        return apariciones;
    }
    
}
