package utiles;

/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 16 de 2013
 */


import entrada_salida.GestionarArchivos;
import estructuras.Porcentaje;
import estructuras.ComentarioNormalizado;
import java.util.*;

public class DistribuirDatos {
     
    private Vector<ComentarioNormalizado> listaComentarios;
    private Vector<ComentarioNormalizado> nuevosComentariosDistribuidos;
    private Vector<ComentarioNormalizado> listaComentariosTodosDiferentes;    
    
    private ArrayList<String> listaDeEtiquetasTotales;
    private ArrayList<String> listaDeEtiquetasIncluidasEnNuevosComentarios;
    
    private Hashtable<String,Integer> tablaFrecuenciasEtiquetasTotales;     
    private Hashtable<String,Integer> tablaFrecuenciasEtiquetasUtiles;
    
    private int comentarioUtiles;
        
    public DistribuirDatos(Vector<ComentarioNormalizado> listaComentarios) {
        
        this.listaComentarios = new Vector(listaComentarios);
        nuevosComentariosDistribuidos = new Vector();
        listaComentariosTodosDiferentes = new Vector();
        listaDeEtiquetasTotales = new ArrayList();   
        listaDeEtiquetasIncluidasEnNuevosComentarios = new ArrayList();
        tablaFrecuenciasEtiquetasTotales = new Hashtable<String, Integer>();                
        tablaFrecuenciasEtiquetasUtiles = new Hashtable<String, Integer>();   
    }
        
    public Vector<ComentarioNormalizado> generarDistribucionNDatos(int N){
        Hashtable<String,Integer> tablaCantMaximaPorEtiqueta = new Hashtable();
        generarFrecuenciasEtiquetasTotales();         
        
        int porcentajeDelTotal = Porcentaje.obtenerPorcentaje(N, listaComentarios.size());        
        tablaCantMaximaPorEtiqueta = llenarTablaCantMaximaPorEtiqueta(porcentajeDelTotal);    
        
        int aumentoGradualCantEtiquetas = 0;
        int comentariosIncluidos = 0;
        int i = 0;        
        
        while(comentariosIncluidos<N){            
            if(i>=listaComentarios.size()){                
                i = 0;
                aumentoGradualCantEtiquetas++;
                System.out.println("Aumento gradual: "+aumentoGradualCantEtiquetas);
            }            
            String etiqueta = listaComentarios.elementAt(i).obtenerEtiquetas().elementAt(0).toLowerCase();            
            ComentarioNormalizado tempComentario = listaComentarios.elementAt(i);                                        
            
            if( (contarAparicionEtiqueta(etiqueta) < (tablaCantMaximaPorEtiqueta.get(etiqueta) + aumentoGradualCantEtiquetas)) &&
                    (!existeComentarioEnVector(tempComentario,nuevosComentariosDistribuidos))
                    ){
                nuevosComentariosDistribuidos.addElement(tempComentario);  
                comentariosIncluidos++;
            }                                                                                                                                        
            i++;            
        }        
        guardarListaNuevosComentarios("Distribuidos "+N);
        System.out.println(nuevosComentariosDistribuidos.size());
        generarFrecuenciasEtiquetasUtiles(nuevosComentariosDistribuidos);
        return nuevosComentariosDistribuidos;
    }
            
    
    private Hashtable<String,Integer> llenarTablaCantMaximaPorEtiqueta(int porcentajePorEtiqueta){
        Hashtable<String,Integer> tablaHash = new Hashtable<String,Integer>();
        for(int i=0; i<listaDeEtiquetasTotales.size(); i++){            
            int cantPorEtiqueta = Porcentaje.calcularNumero(tablaFrecuenciasEtiquetasTotales.get(listaDeEtiquetasTotales.get(i)),porcentajePorEtiqueta);            
            tablaHash.put(listaDeEtiquetasTotales.get(i), cantPorEtiqueta);
        }
        return tablaHash;
    }

    private int contarAparicionEtiqueta(String eti){
        int apariciones = 0;
        for(int i=0; i<nuevosComentariosDistribuidos.size(); i++){
            if(eti.equals(nuevosComentariosDistribuidos.elementAt(i).obtenerEtiquetas().elementAt(0).toLowerCase())){
                apariciones++;
            }
        }
        return apariciones;
    }

    private boolean existeComentarioEnVector(ComentarioNormalizado cn, Vector<ComentarioNormalizado> vector){
        for(int i=0; i<vector.size(); i++){
            Vector<String> a = cn.obtenerListaPalabrasEnComentario();
            Vector<String> b = vector.elementAt(i).obtenerListaPalabrasEnComentario();            
            if(a.equals(b)){                
                return true;
            }
        }
        return false;
    }
    
    private void guardarListaNuevosComentarios(String tipo){
        GestionarArchivos ga = new GestionarArchivos();
        ga.guardarComentariosNormalizados(nuevosComentariosDistribuidos, tipo);        
    }
    
    private void generarFrecuenciasEtiquetasTotales(){
        for(int i=0; i<listaComentarios.size(); i++){           
            ComentarioNormalizado tempComentario = listaComentarios.elementAt(i);            
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
    
    private void generarFrecuenciasEtiquetasUtiles(Vector<ComentarioNormalizado> listaOrigen){
        listaComentariosTodosDiferentes = new Vector();
        listaDeEtiquetasIncluidasEnNuevosComentarios = new ArrayList<String>();
        tablaFrecuenciasEtiquetasUtiles = new Hashtable<String, Integer>();
        
        for(int i=0; i<listaOrigen.size(); i++){           
            ComentarioNormalizado tempComentario = listaOrigen.elementAt(i);             
            if( !(tempComentario.obtenerEtiquetas().isEmpty()) &&
                    !(tempComentario.obtenerListaPalabrasEnComentario().isEmpty()) &&                    
                    !(existeComentarioEnVector(tempComentario,listaComentariosTodosDiferentes))                    
                    ){
                String tempEtiqueta = tempComentario.obtenerEtiquetas().elementAt(0).toLowerCase();                
                if(!(tablaFrecuenciasEtiquetasUtiles.containsKey(tempEtiqueta))){
                    tablaFrecuenciasEtiquetasUtiles.put(tempEtiqueta, 1);
                    listaDeEtiquetasIncluidasEnNuevosComentarios.add(tempEtiqueta);
                    listaComentariosTodosDiferentes.add(tempComentario);
                }
                else{
                    int frecuenciaActual = tablaFrecuenciasEtiquetasUtiles.get(tempEtiqueta);
                    tablaFrecuenciasEtiquetasUtiles.put(tempEtiqueta, ++frecuenciaActual); 
                    listaComentariosTodosDiferentes.add(tempComentario);
                }                                
            }                      
        }        
        for(int i=0; i<listaDeEtiquetasIncluidasEnNuevosComentarios.size(); i++){            
            System.out.println(listaDeEtiquetasIncluidasEnNuevosComentarios.get(i)+"\t"+tablaFrecuenciasEtiquetasUtiles.get(listaDeEtiquetasIncluidasEnNuevosComentarios.get(i)));            
        }        
    }        
    
    public void generarListaSoloComentarioUtiles(){                               
        generarFrecuenciasEtiquetasUtiles(listaComentarios);        
        
        for(int i=0; i<listaComentarios.size(); i++){           
            ComentarioNormalizado tempComentario = listaComentarios.elementAt(i);            
            if( !(tempComentario.obtenerEtiquetas().isEmpty()) &&
                    !(tempComentario.obtenerListaPalabrasEnComentario().isEmpty()) &&                    
                    !(existeComentarioEnVector(tempComentario,nuevosComentariosDistribuidos))                    
                    ){
                String tempEtiqueta = tempComentario.obtenerEtiquetas().elementAt(0).toLowerCase();
                if((tablaFrecuenciasEtiquetasUtiles.get(tempEtiqueta)) >= 35){                    
                    nuevosComentariosDistribuidos.addElement(tempComentario);
                    if(!listaDeEtiquetasIncluidasEnNuevosComentarios.contains(tempEtiqueta)){
                        listaDeEtiquetasIncluidasEnNuevosComentarios.add(tempEtiqueta);                        
                    }
                }                
            }                                     
        }
        comentarioUtiles = nuevosComentariosDistribuidos.size();
        guardarListaNuevosComentarios("Utiles Normalizados");
        //-------------------------------
        System.out.println("\nTamaño= "+listaComentarios.size());
        System.out.println("Utiles= "+comentarioUtiles+"\n");  
        generarFrecuenciasEtiquetasUtiles(nuevosComentariosDistribuidos);
        //-------------------------------
    }        
}
