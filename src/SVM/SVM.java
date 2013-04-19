package SVM;

/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 17 de 2013
 */


import estructuras.VectorFrecuenciasPalabras;
import estructuras.Porcentaje;
import estructuras.ComentarioNormalizado;
import java.util.*;
import libsvm.*;


public class SVM {
    
    private Vector<ComentarioNormalizado> listaComentarios;
    private Vector<VectorFrecuenciasPalabras> listaVectoresFrecuencias; //Lista de todas las caracteristicas.    
    private Vector<String> listaDeEtiquetasTotales;
    private Vector<String> listaEtiquetasUsadasEnEntrenamiento; 
    private Vector<Integer> listaPosicionesEtiquetasEntrenamiento;
    private Hashtable<String,Integer> tablaFrecuenciasEtiquetasTotales;
    private Hashtable<String,Integer> tablaCantMaximaPorEtiqueta;
    private Hashtable<Integer,String> tablaCorrespondenciaIndice_Etiqueta;
    private Hashtable<Integer,VectorFrecuenciasPalabras> tablaCaracteristicasEntrenamiento;
    private Hashtable<Integer,VectorFrecuenciasPalabras> tablaCaracteristicasPrueba;
    private ArrayList<svm_node[]> listaNodosParaProbar;
    private ArrayList<Integer> listaPosicionesEtiquetasPrueba;
    
    private Hashtable<String,Integer> tabla_Etiqueta_AparicionesTotalesEstimadas;
    private Hashtable<String,Integer> tabla_Etiqueta_AparicionesCorrectas;    
    private Hashtable<String,Integer> tabla_Etiqueta_AparicionesTotalesCorrespondientes;
    
    private svm_parameter param;

    public SVM(Vector<ComentarioNormalizado> listaComentarios, Vector<VectorFrecuenciasPalabras> listaVectoresFrecuencias){
        this.listaComentarios = new Vector(listaComentarios);
        this.listaVectoresFrecuencias = new Vector(listaVectoresFrecuencias);
        listaDeEtiquetasTotales = new Vector();
        listaEtiquetasUsadasEnEntrenamiento = new Vector();
        listaPosicionesEtiquetasEntrenamiento = new Vector();
        tablaFrecuenciasEtiquetasTotales = new Hashtable();
        tablaCantMaximaPorEtiqueta = new Hashtable();
        tablaCorrespondenciaIndice_Etiqueta = new Hashtable();
        tablaCaracteristicasEntrenamiento = new Hashtable();
        tablaCaracteristicasPrueba = new Hashtable(); 
        listaNodosParaProbar = new ArrayList<svm_node[]>();
        listaPosicionesEtiquetasPrueba = new ArrayList<Integer>();
        
        tabla_Etiqueta_AparicionesTotalesEstimadas = new Hashtable();
        tabla_Etiqueta_AparicionesCorrectas = new Hashtable();
        tabla_Etiqueta_AparicionesTotalesCorrespondientes = new Hashtable();
        
        mapearTablaIndice_Etiqueta();
        
        param = new svm_parameter();
        definirParametros();
    }
    
    private void definirParametros(){
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.LINEAR; 
        param.gamma = 0.5;
        param.nu = 0.5;
        param.cache_size = 20000;
        param.C = 1;
        param.eps = 0.001;
        param.p = 0.1;  
        param.probability = 1;        
    } 
    
    private void mapearTablaIndice_Etiqueta(){
        for(int i=0; i<listaComentarios.size(); i++){
            String tempEtiqueta = listaComentarios.elementAt(i).obtenerEtiquetas().elementAt(0);
            tablaCorrespondenciaIndice_Etiqueta.put(i, tempEtiqueta);
        }
    }
           
    private void generarTablaCaracteristicasEntrenamiento(int porcentaje){
        int porcentajeSemillas = Porcentaje.calcularNumero(listaComentarios.size(),porcentaje);
        generarFrecuenciasEtiquetasTotales();
        llenarTablaCantMaximaPorEtiqueta(porcentaje);
        
        int aumentoGradualSemillasPorEtiqueta = 0;
        int contador = 0;
        int i = 0;
        while(contador < porcentajeSemillas){
            if(i>=listaComentarios.size()){
                i = 0;
                aumentoGradualSemillasPorEtiqueta++;
                System.out.println("    Aumento gradual entrenamiento: "+aumentoGradualSemillasPorEtiqueta);
            }            
            String etiqueta = listaComentarios.elementAt(i).obtenerEtiquetas().elementAt(0);
            if(contarAparicionEtiqueta(etiqueta) < (tablaCantMaximaPorEtiqueta.get(etiqueta) + aumentoGradualSemillasPorEtiqueta) &&
                    !(listaPosicionesEtiquetasEntrenamiento.contains(i))
                    ){                
                listaEtiquetasUsadasEnEntrenamiento.addElement(etiqueta);
                listaPosicionesEtiquetasEntrenamiento.addElement(i);
                tablaCaracteristicasEntrenamiento.put(i, listaVectoresFrecuencias.elementAt(i));
                contador++;
            }              
            i++;
        }         
    }
    
    private void generarTablaCaracteristicasPrueba(){
        for(int i=0; i<listaComentarios.size(); i++){
            if(!listaPosicionesEtiquetasEntrenamiento.contains(i)){
                tablaCaracteristicasPrueba.put(i, listaVectoresFrecuencias.elementAt(i));
            }
        }        
    }
    
    private int contarAparicionEtiqueta(String eti){
        int apariciones = 0;
        for(int i=0; i<listaEtiquetasUsadasEnEntrenamiento.size(); i++){
            if(eti.equals(listaEtiquetasUsadasEnEntrenamiento.elementAt(i)))
                apariciones++;
        }
        return apariciones;
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
    }
    
    private void llenarTablaCantMaximaPorEtiqueta(int porcentajePorEtiqueta){        
        for(int i=0; i<listaDeEtiquetasTotales.size(); i++){            
            int cantPorEtiqueta = Porcentaje.calcularNumero(tablaFrecuenciasEtiquetasTotales.get(listaDeEtiquetasTotales.get(i)),porcentajePorEtiqueta);            
            tablaCantMaximaPorEtiqueta.put(listaDeEtiquetasTotales.get(i), cantPorEtiqueta);
        }        
    }
    
    private int obtenerCantPoscionesDiferentesCero(VectorFrecuenciasPalabras vectorFC){
        int cantPosiciones = 0;
        for(int i=0; i<vectorFC.tamanio(); i++){
            if(vectorFC.elementoEn(i)!=0){
                cantPosiciones++;                
            }
        }
        return cantPosiciones;
    }
    
    private svm_problem generarProblemaEntrenamientoParaEtiqueta(String etiqueta){        
        int tamListaCaracteristicasEntrenamiento = tablaCaracteristicasEntrenamiento.size();
        svm_problem estructuraEntrenamiento = new svm_problem();
        estructuraEntrenamiento.l = tamListaCaracteristicasEntrenamiento;
        estructuraEntrenamiento.y = new double[estructuraEntrenamiento.l];
        estructuraEntrenamiento.x = new svm_node[estructuraEntrenamiento.l][];
        
        int iterador_i = 0;
        for(int i=0; i<listaComentarios.size(); i++){
            if(tablaCaracteristicasEntrenamiento.containsKey(i)){
                VectorFrecuenciasPalabras tempVectorCaracteristicas = tablaCaracteristicasEntrenamiento.get(i);
                int tempTamVectorCaracteristicas = obtenerCantPoscionesDiferentesCero(tempVectorCaracteristicas);
                estructuraEntrenamiento.x[iterador_i] = new svm_node[tempTamVectorCaracteristicas];
                
                int iterador_j = 0;
                for(int j=0; j<tempVectorCaracteristicas.tamanio(); j++){
                    if(tempVectorCaracteristicas.elementoEn(j) != 0){
                        svm_node nodo = new svm_node();
                        nodo.index = j+1;
                        nodo.value = tempVectorCaracteristicas.elementoEn(j);
                        estructuraEntrenamiento.x[iterador_i][iterador_j] = nodo;                         
                        iterador_j++;                        
                    }
                }
                
                if(tablaCorrespondenciaIndice_Etiqueta.get(i).equals(etiqueta)){
                    estructuraEntrenamiento.y[iterador_i] = 1;                    
                }                
                else{
                    estructuraEntrenamiento.y[iterador_i] = -1;                                        
                }                
                iterador_i++;                 
            }            
        }        
        
        return estructuraEntrenamiento;
    }  
    
    private void generarNodosDePrueba(){                               
        for(int i=0; i<listaComentarios.size(); i++){
            if(tablaCaracteristicasPrueba.containsKey(i)){
                VectorFrecuenciasPalabras tempVectorCaracteristicas = tablaCaracteristicasPrueba.get(i);
                int tempTamVectorCaracteristicas = obtenerCantPoscionesDiferentesCero(tempVectorCaracteristicas);
                svm_node[] listaCaracteristicasPrueba = new svm_node[tempTamVectorCaracteristicas];
                
                int iterador_j = 0;
                for(int j=0; j<tempVectorCaracteristicas.tamanio(); j++){
                    if(tempVectorCaracteristicas.elementoEn(j) != 0){
                        svm_node nodo = new svm_node();
                        nodo.index = j+1;
                        nodo.value = tempVectorCaracteristicas.elementoEn(j);
                        listaCaracteristicasPrueba[iterador_j] = nodo;                        
                        iterador_j++;                        
                    }
                }
                listaNodosParaProbar.add(listaCaracteristicasPrueba);
                listaPosicionesEtiquetasPrueba.add(i);
            }            
        }                        
    }
    
    public void ejecutarSVM(int porcentajeSemillasEntrenamiento){
        generarTablaCaracteristicasEntrenamiento(porcentajeSemillasEntrenamiento);
        generarTablaCaracteristicasPrueba();
                
        generarNodosDePrueba();
        Enumeration<String> listaTodasEtiquetasDiferentes = tablaFrecuenciasEtiquetasTotales.keys();
        while(listaTodasEtiquetasDiferentes.hasMoreElements()){
            String tempEtiqueta = listaTodasEtiquetasDiferentes.nextElement();
            ejecutarSVMparaEtiqueta(tempEtiqueta);
        }
    }
    
    private void ejecutarSVMparaEtiqueta(String etiqueta){
        svm_problem problema = generarProblemaEntrenamientoParaEtiqueta(etiqueta);   
        System.out.println("**********************Eti: "+etiqueta);
        svm_model modelo = svm.svm_train(problema,param);
                        
        for(int i=0; i<listaNodosParaProbar.size(); i++){
            svm_node[] tempListaCaracteristicas = listaNodosParaProbar.get(i);
            double prediccion = svm.svm_predict(modelo, tempListaCaracteristicas);                        
            llenarTablasApariciones(etiqueta, prediccion, i);
        }                
    }
    
    private void llenarTablasApariciones(String etiquetaEvaluada, double prediccion, int posicion){
        int posiconEnListaOriginalComentarios = listaPosicionesEtiquetasPrueba.get(posicion);
        String etiquetaCorrespondiente = tablaCorrespondenciaIndice_Etiqueta.get(posiconEnListaOriginalComentarios); 
        if(etiquetaCorrespondiente.equals(etiquetaEvaluada)){
            aumentarAparicionEnTabla("correspondientes", etiquetaEvaluada);
            if(prediccion == 1.0){
                aumentarAparicionEnTabla("estimadas", etiquetaEvaluada);
                aumentarAparicionEnTabla("correctas", etiquetaEvaluada);
            }            
        }
        else{
            if(prediccion == 1.0){
                aumentarAparicionEnTabla("estimadas", etiquetaEvaluada);
                if(!tabla_Etiqueta_AparicionesCorrectas.containsKey(etiquetaEvaluada)){
                    tabla_Etiqueta_AparicionesCorrectas.put(etiquetaEvaluada,0);
                }
            }
        }
    } 
    
    public void aumentarAparicionEnTabla(String tabla, String etiqueta){
        if(tabla.equals("estimadas")){
            if(tabla_Etiqueta_AparicionesTotalesEstimadas.containsKey(etiqueta)){
                int frecuenciaActual = tabla_Etiqueta_AparicionesTotalesEstimadas.get(etiqueta);
                tabla_Etiqueta_AparicionesTotalesEstimadas.put(etiqueta, ++frecuenciaActual);
            }            
            else{
                tabla_Etiqueta_AparicionesTotalesEstimadas.put(etiqueta, 1);
            }
        }
        else if(tabla.equals("correctas")){
            if(tabla_Etiqueta_AparicionesCorrectas.containsKey(etiqueta)){
                int frecuenciaActual = tabla_Etiqueta_AparicionesCorrectas.get(etiqueta);
                tabla_Etiqueta_AparicionesCorrectas.put(etiqueta, ++frecuenciaActual);
            }            
            else{
                tabla_Etiqueta_AparicionesCorrectas.put(etiqueta, 1);
            }
            
        }
        else{
            if(tabla_Etiqueta_AparicionesTotalesCorrespondientes.containsKey(etiqueta)){
                int frecuenciaActual = tabla_Etiqueta_AparicionesTotalesCorrespondientes.get(etiqueta);
                tabla_Etiqueta_AparicionesTotalesCorrespondientes.put(etiqueta, ++frecuenciaActual);
            }            
            else{
                tabla_Etiqueta_AparicionesTotalesCorrespondientes.put(etiqueta, 1);
            }
            
        }
    }
    
    public Hashtable<String,Integer> getTabla_Etiqueta_AparicionesTotalesEstimadas(){
        return tabla_Etiqueta_AparicionesTotalesEstimadas;        
    }
    
    public Hashtable<String,Integer> getTabla_Etiqueta_AparicionesCorrectas(){
        return tabla_Etiqueta_AparicionesCorrectas;
    }
    
    public Hashtable<String,Integer> getTabla_Etiqueta_AparicionesTotalesCorrespondientes(){
        return tabla_Etiqueta_AparicionesTotalesCorrespondientes;
    }                    
} 