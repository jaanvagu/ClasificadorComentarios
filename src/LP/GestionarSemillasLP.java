package LP;

/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 16 de 2013
 */


import main.Main;
import entrada_salida.GestionarArchivos;
import estructuras.Porcentaje;
import estructuras.ComentarioNormalizado;
import java.util.Hashtable;
import java.util.Vector;

public class GestionarSemillasLP {
    
    private Vector<ComentarioNormalizado> listaComentariosNormalizados;
    private Vector<String> listaDeEtiquetasTotales;
    private Vector<String> listaEtiquetasUsadasComoSemilla;
    private Vector<Integer> listaPosicionesSemillas;
    private Hashtable<String,Integer> tablaFrecuenciasEtiquetasTotales;
    private Hashtable<String,Integer> tablaCantMaximaPorEtiqueta;
    private GestionarArchivos gestionArchivos;

    public GestionarSemillasLP(Vector<ComentarioNormalizado> listaComentarios) {        
        listaComentariosNormalizados = new Vector(listaComentarios);
        listaDeEtiquetasTotales = new Vector();
        listaEtiquetasUsadasComoSemilla = new Vector();
        listaPosicionesSemillas = new Vector();
        tablaFrecuenciasEtiquetasTotales = new Hashtable();
        tablaCantMaximaPorEtiqueta = new Hashtable();
        gestionArchivos = new GestionarArchivos();      
    }                        

    //Método que genera un archivo semilla, con todas las posibles etiquetas distintas en una lista de comentarios,
    // es decir, si existes E1, E2, E3,... E10 cada uno con una frecuencia de 10, y el porcentaje dado como parámetro es 10 (10%),
    // entonces, el archivo semilla estará contenido por E1, E2, E3,... E10, con una frecuencia de 1
    public void generarArchivoSemillas(int porcentaje){        
        gestionArchivos.crearArchivoTexto("seeds",Main.listaComentariosNormalizados.size());      
        int porcentajeSemillas = Porcentaje.calcularNumero(listaComentariosNormalizados.size(),porcentaje);
        System.out.println("    Porcentaje Semilla: "+porcentajeSemillas);
        
        generarFrecuenciasEtiquetasTotales();
        llenarTablaCantMaximaPorEtiqueta(porcentaje);
        
        int aumentoGradualSemillasPorEtiqueta = 0;
        int contador = 0;
        int i = 0;
        while(contador < porcentajeSemillas){
            if(i>=listaComentariosNormalizados.size()){
                i = 0;
                aumentoGradualSemillasPorEtiqueta++;
                System.out.println("    Aumento gradual semilla: "+aumentoGradualSemillasPorEtiqueta);
            }
            int x = i+1;
            String etiqueta = listaComentariosNormalizados.elementAt(i).obtenerEtiquetas().elementAt(0);
            if(contarAparicionEtiqueta(etiqueta) < (tablaCantMaximaPorEtiqueta.get(etiqueta) + aumentoGradualSemillasPorEtiqueta) &&
                    !(listaPosicionesSemillas.contains(i))
                    ){
                gestionArchivos.escribirLineaEnArchivoTexto("N"+x+"\t"+etiqueta+"\t"+"1.0"); 
                listaEtiquetasUsadasComoSemilla.addElement(etiqueta);
                listaPosicionesSemillas.addElement(i);
                contador++;
            }              
            i++;
        }                
        gestionArchivos.cerrarArchivoTexto();
        //-------------------------------
        System.out.println("--Semillas generadas--");
        System.out.println("    Total semillas: "+listaEtiquetasUsadasComoSemilla.size());  
        //-------------------------------                      
    }

    //Método que genera un archivo con las etiquetas correspondientes a una lista de comentarios.
    // Suponga que una lista tiene 10 comentarios, donde 2 de ellos fueron pasados como semilla, entonces
    // se generará un archivo solución con las etiquetas que le corresponden a los 8 comentarios restantes.
    // Las etiquetas que se establecen como solución, han sido puestas por personal, son verídicas.
    public void generarArchivoSolucion(){
        gestionArchivos.crearArchivoTexto("gold_labels",Main.listaComentariosNormalizados.size());   
        for(int i=0; i<listaComentariosNormalizados.size(); i++){
            String etiqueta = listaComentariosNormalizados.elementAt(i).obtenerEtiquetas().elementAt(0);
            int x = i+1;
            if(!listaPosicionesSemillas.contains(i)){                        
                gestionArchivos.escribirLineaEnArchivoTexto("N"+x+"\t"+etiqueta+"\t"+"1.0");                             
            }
        }                                        
        gestionArchivos.cerrarArchivoTexto();   
        //-------------------------------
        System.out.println("--Gold labels generadas--");                     
        //-------------------------------
    }
    
    public int contarAparicionEtiqueta(String eti){
        int apariciones = 0;
        for(int i=0; i<listaEtiquetasUsadasComoSemilla.size(); i++){
            if(eti.equals(listaEtiquetasUsadasComoSemilla.elementAt(i)))
                apariciones++;
        }
        return apariciones;
    }
    
    private void generarFrecuenciasEtiquetasTotales(){
        for(int i=0; i<listaComentariosNormalizados.size(); i++){           
            ComentarioNormalizado tempComentario = listaComentariosNormalizados.elementAt(i);            
            if(!tempComentario.obtenerEtiquetas().isEmpty()){
                String tempEtiqueta = tempComentario.obtenerEtiquetas().elementAt(0).toLowerCase();
                if(!tablaFrecuenciasEtiquetasTotales.containsKey(tempEtiqueta)){
                    tablaFrecuenciasEtiquetasTotales.put(tempEtiqueta, 1);
                    listaDeEtiquetasTotales.add(tempEtiqueta);
                }
                else{
                    int frecuenciaActual = tablaFrecuenciasEtiquetasTotales.get(tempEtiqueta);
                    tablaFrecuenciasEtiquetasTotales.put(tempEtiqueta, ++frecuenciaActual);                    
                }                                                                                
            }
        }
//        for(int i=0; i<listaDeEtiquetasTotales.size(); i++){            
//            System.out.println(listaDeEtiquetasTotales.get(i)+"\t"+tablaFrecuenciasEtiquetasTotales.get(listaDeEtiquetasTotales.get(i)));
//        }        
//        System.out.println("\n");
    }
    
    private void llenarTablaCantMaximaPorEtiqueta(int porcentajePorEtiqueta){        
        for(int i=0; i<listaDeEtiquetasTotales.size(); i++){            
            int cantPorEtiqueta = Porcentaje.calcularNumero(tablaFrecuenciasEtiquetasTotales.get(listaDeEtiquetasTotales.get(i)),porcentajePorEtiqueta);            
            tablaCantMaximaPorEtiqueta.put(listaDeEtiquetasTotales.get(i), cantPorEtiqueta);
        }
//        for(int i=0; i<listaDeEtiquetasTotales.size(); i++){            
//            System.out.println(listaDeEtiquetasTotales.get(i)+"\t"+tablaCantMaximaPorEtiqueta.get(listaDeEtiquetasTotales.get(i)));
//        }  
    }
}
