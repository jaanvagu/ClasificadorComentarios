/*
 * @author Jairo Andrés
 * Ultima modificacion: Febrero 13 de 2013
 */

import java.util.*;

// ********* TEMPORAL *********
public class DistribuirDatos {

    private Vector<String> listaDeEtiquetas;   
    private Vector<ComentarioNormalizado> listaComentarios;
    private Vector<ComentarioNormalizado> nuevosComentariosDistribuidos;

    public DistribuirDatos(Vector<ComentarioNormalizado> listaComentarios) {

        listaDeEtiquetas = new Vector();
        this.listaComentarios = new Vector(listaComentarios);
        nuevosComentariosDistribuidos = new Vector();
    }
    
    public Vector<ComentarioNormalizado> generarDistribucionNDatos(int N){
        int maximoCantPorEtiqueta = (N/10);
        int comentariosIncluidos = 0;
        int i = 0;
        while(comentariosIncluidos<N){
            
            if(i>=listaComentarios.size()){
                System.out.println("Salio en BREAK");
                break;
            }

            if((!listaComentarios.elementAt(i).obtenerEtiquetas().isEmpty()) && (!listaComentarios.elementAt(i).obtenerListaPalabrasEnComentario().isEmpty())){
                String etiqueta = listaComentarios.elementAt(i).obtenerEtiquetas().elementAt(0);
                //System.out.println("Original: "+etiqueta+" -tApariciones: "+contarAparicionEtiqueta(etiqueta));
                ComentarioNormalizado cn = listaComentarios.elementAt(i);                
                if( (!existeComentarioEnVectorDistribuido(cn)) ){//&& (!etiqueta.toLowerCase().equals("otros")) ){
                    if( (listaDeEtiquetas.size()<10) && (contarAparicionEtiqueta(etiqueta)<maximoCantPorEtiqueta) ){                          
                        if(!listaDeEtiquetas.contains(etiqueta))  {                          
                            listaDeEtiquetas.addElement(etiqueta);
                            //System.out.println("**********Nueva****************");
                        }
                        nuevosComentariosDistribuidos.addElement(cn);
                        comentariosIncluidos++;                                                    
                        //System.out.println("If: "+etiqueta+" -\tEti: "+listaDeEtiquetas.size()+" -\tTotal: "+nuevosComentariosDistribuidos.size());
                                                                      
                    }
                    else{                        
                        if( (listaDeEtiquetas.contains(etiqueta)) && (contarAparicionEtiqueta(etiqueta)<maximoCantPorEtiqueta) ){                               
                            nuevosComentariosDistribuidos.addElement(cn);
                            comentariosIncluidos++;                                                              
                            //System.out.println("Else: "+etiqueta+" -\tEti: "+listaDeEtiquetas.size()+" -\tTotal: "+nuevosComentariosDistribuidos.size());
                        }
                    }                                                                                             
                }
                else{
                    //System.out.println("___Vacio o Repetido___");
                }
                    
                //System.out.println("----------------------------------------------------------------------------\n");
            }
            i++;
        }
        System.out.println("Total Eti: "+listaDeEtiquetas.size());        
        GestionarArchivos ga = new GestionarArchivos();
        ga.guardarComentariosNormalizados(nuevosComentariosDistribuidos);
        return nuevosComentariosDistribuidos;
    }

    public Vector<ComentarioNormalizado> generarDistribucion100Datos(){
        int comentariosIncluidos = 0;
        int i = 0;
        while(comentariosIncluidos<100){
            
            if(i>=listaComentarios.size()){
                System.out.println("Salio en BREAK");
                break;
            }

            if((!listaComentarios.elementAt(i).obtenerEtiquetas().isEmpty()) && (!listaComentarios.elementAt(i).obtenerListaPalabrasEnComentario().isEmpty())){
                String etiqueta = listaComentarios.elementAt(i).obtenerEtiquetas().elementAt(0);
                //System.out.println("Original: "+etiqueta+" -tApariciones: "+contarAparicionEtiqueta(etiqueta));
                ComentarioNormalizado cn = listaComentarios.elementAt(i);                
                if( (!existeComentarioEnVectorDistribuido(cn)) && (!etiqueta.toLowerCase().equals("otros")) ){//&& (!etiqueta.toLowerCase().equals("escenarios de uso")) ){                      
                    if( (listaDeEtiquetas.size()<10) && (contarAparicionEtiqueta(etiqueta)<10) ){                          
                        if(!listaDeEtiquetas.contains(etiqueta))  {                          
                            listaDeEtiquetas.addElement(etiqueta);
                            //System.out.println("**********Nueva****************");
                        }
                        nuevosComentariosDistribuidos.addElement(cn);
                        comentariosIncluidos++;                                                    
                        //System.out.println("If: "+etiqueta+" -\tEti: "+listaDeEtiquetas.size()+" -\tTotal: "+nuevosComentariosDistribuidos.size());
                                                                      
                    }
                    else{                        
                        if( (listaDeEtiquetas.contains(etiqueta)) && (contarAparicionEtiqueta(etiqueta)<10) ){                               
                            nuevosComentariosDistribuidos.addElement(cn);
                            comentariosIncluidos++;                                                              
                            //System.out.println("Else: "+etiqueta+" -\tEti: "+listaDeEtiquetas.size()+" -\tTotal: "+nuevosComentariosDistribuidos.size());
                        }
                    }                                                                                             
                }
                else{
                    //System.out.println("___Vacio o Repetido___");
                }
                    
                //System.out.println("----------------------------------------------------------------------------\n");
            }
            i++;
        }
        System.out.println("Total Eti: "+listaDeEtiquetas.size()+"\n");
        for(int ii=0; ii<listaDeEtiquetas.size(); ii++)
            System.out.println(listaDeEtiquetas.elementAt(ii)+": "+contarAparicionEtiqueta(listaDeEtiquetas.elementAt(ii)));
        GestionarArchivos ga = new GestionarArchivos();
        ga.guardarComentariosNormalizados(nuevosComentariosDistribuidos);
        return nuevosComentariosDistribuidos;
    }

    public int contarAparicionEtiqueta(String eti){

        int apariciones = 0;
        for(int i=0; i<nuevosComentariosDistribuidos.size(); i++){
            if(eti.equals(nuevosComentariosDistribuidos.elementAt(i).obtenerEtiquetas().elementAt(0)))
                apariciones++;
        }
        return apariciones;
    }

    public void obtenerCaracteristasDeListaComentarios(){
        int contadorIguales = 0;
        int vacios = 0;

        for(int i=0; i<listaComentarios.size(); i++){
            if(!listaComentarios.elementAt(i).obtenerEtiquetas().isEmpty()){
                if(!existeEtiquetaEnVector(listaComentarios.elementAt(i).obtenerEtiquetas().elementAt(0)))
                    listaDeEtiquetas.addElement(listaComentarios.elementAt(i).obtenerEtiquetas().elementAt(0));
            }

            if(listaComentarios.elementAt(i).obtenerListaPalabrasEnComentario().isEmpty())
                vacios++;

            if(!existeComentarioEnVectorDistribuido(listaComentarios.elementAt(i))){
                nuevosComentariosDistribuidos.addElement(listaComentarios.elementAt(i));
            }
            else
                contadorIguales++;
        }
        System.out.println(listaDeEtiquetas);
        System.out.println("Tamaño= "+listaComentarios.size());
        System.out.println("eti distintas= "+listaDeEtiquetas.size());
        System.out.println("Iguales coments= "+contadorIguales);
        System.out.println("Vacios= "+vacios);
    }

    private boolean existeEtiquetaEnVector(String eti){
        if(listaDeEtiquetas.contains(eti))
            return true;
        else
            return false;
    }

    private boolean existeComentarioEnVectorDistribuido(ComentarioNormalizado cn){
        for(int i=0; i<nuevosComentariosDistribuidos.size(); i++){
            Vector<String> a = cn.obtenerListaPalabrasEnComentario();
            Vector<String> b = nuevosComentariosDistribuidos.elementAt(i).obtenerListaPalabrasEnComentario();
            if(a.equals(b))                
                return true;            
        }
        return false;
    }

}
