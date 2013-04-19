package evaluacion;

/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 16 de 2013
 */


import estructuras.Porcentaje;
import java.util.*;

public class GestionarIndicadores {

    private Vector<String> listaEtiquetasCorrespondientes;
    private Vector<String> listaEtiquetasEstimadas;
    private Hashtable<String,Integer> tabla_Etiqueta_AparicionesTotalesEstimadas;
    private Hashtable<String,Integer> tabla_Etiqueta_AparicionesCorrectas;    
    private Hashtable<String,Integer> tabla_Etiqueta_AparicionesTotalesCorrespondientes;
    private Hashtable<String,Double> tabla_Etiqueta_indicadorPrecision;
    private Hashtable<String,Double> tabla_Etiqueta_indicadorRecall;
    private Hashtable<String,Double> tabla_Etiqueta_indicadorFScore;
    
    private Enumeration<String> etiDiferentes;

    public GestionarIndicadores(Vector<String> listaEtiquetasCorrespondientes, Vector<String> listaEtiquetasEstimadas){
        this.listaEtiquetasCorrespondientes = listaEtiquetasCorrespondientes;
        this.listaEtiquetasEstimadas = listaEtiquetasEstimadas;        
        tabla_Etiqueta_AparicionesCorrectas = new Hashtable();
        tabla_Etiqueta_AparicionesTotalesEstimadas = new Hashtable();
        tabla_Etiqueta_AparicionesTotalesCorrespondientes = new Hashtable();
        tabla_Etiqueta_indicadorPrecision = new Hashtable();
        tabla_Etiqueta_indicadorRecall = new Hashtable();
        tabla_Etiqueta_indicadorFScore = new Hashtable();
        construirTablasApariciones();
    }
    
    public GestionarIndicadores(Hashtable<String,Integer> tablaEstimadas, Hashtable<String,Integer> tablaCorrectas, 
            Hashtable<String,Integer> tablaCorrespondientes){        
        tabla_Etiqueta_AparicionesTotalesEstimadas = tablaEstimadas;
        tabla_Etiqueta_AparicionesCorrectas = tablaCorrectas;
        tabla_Etiqueta_AparicionesTotalesCorrespondientes = tablaCorrespondientes; 
        tabla_Etiqueta_indicadorPrecision = new Hashtable();
        tabla_Etiqueta_indicadorRecall = new Hashtable();
        tabla_Etiqueta_indicadorFScore = new Hashtable();
    }
    
    public void calcularIndicadores(String algoritmo){
        if(algoritmo.equals("LP")){
            calcularIndicadorPrecisionParaEtiquetas();
            calcularIndicadorRecallParaEtiquetas();
            calcularIndicadorFScoreParaEtiquetas();
            calcularAciertosTotales();
            imprimirIndicadores(algoritmo);                       
        }
        else if(algoritmo.equals("SVM")){
            calcularIndicadorPrecisionParaEtiquetas();
            calcularIndicadorRecallParaEtiquetas();
            calcularIndicadorFScoreParaEtiquetas();
            imprimirIndicadores(algoritmo); 
        }        
    }

    //Cálacula el indicador "precision" para las etiquetas de una lista de comentarios. 
    public void calcularIndicadorPrecisionParaEtiquetas(){
        double indicadorPrecisionEtiqueta_i = 0;
        etiDiferentes = tabla_Etiqueta_AparicionesTotalesCorrespondientes.keys();
        while(etiDiferentes.hasMoreElements()){
            String etiqueta = etiDiferentes.nextElement();
            int aparicionesCorrectas = 0;
            int aparicionesTotales = 0;
            if(!(tabla_Etiqueta_AparicionesCorrectas.get(etiqueta) == null)){
                aparicionesCorrectas = tabla_Etiqueta_AparicionesCorrectas.get(etiqueta);            
                aparicionesTotales = tabla_Etiqueta_AparicionesTotalesEstimadas.get(etiqueta);
                indicadorPrecisionEtiqueta_i = (aparicionesCorrectas+0.0)/(aparicionesTotales+0.0);
            }     
            else{
                indicadorPrecisionEtiqueta_i = 0;
            }
            tabla_Etiqueta_indicadorPrecision.put(etiqueta, indicadorPrecisionEtiqueta_i);
        }                                
    }

    //Cálacula el indicador "recall" para las etiquetas de una lista de comentarios.
    public void calcularIndicadorRecallParaEtiquetas(){
        double indicadorRecallEtiqueta_i = 0;
        etiDiferentes = tabla_Etiqueta_AparicionesTotalesCorrespondientes.keys();
        while(etiDiferentes.hasMoreElements()){
            String etiqueta = etiDiferentes.nextElement();
            int aparicionesCorrectas = 0;
            int aparicionesTotalesCorrespondientes = 0;
            if(!(tabla_Etiqueta_AparicionesCorrectas.get(etiqueta) == null)){
                aparicionesCorrectas = tabla_Etiqueta_AparicionesCorrectas.get(etiqueta);
                aparicionesTotalesCorrespondientes = tabla_Etiqueta_AparicionesTotalesCorrespondientes.get(etiqueta);
                indicadorRecallEtiqueta_i = (aparicionesCorrectas+0.0)/(aparicionesTotalesCorrespondientes+0.0);
            }
            else
                indicadorRecallEtiqueta_i = 0;
            tabla_Etiqueta_indicadorRecall.put(etiqueta, indicadorRecallEtiqueta_i);
        }        
    }

    //Cálacula el indicador "FScore" para las etiquetas de una lista de comentarios.
    public void calcularIndicadorFScoreParaEtiquetas(){
        double indicadorFScoreEtiqueta_i = 0.0;
        etiDiferentes = tabla_Etiqueta_AparicionesTotalesCorrespondientes.keys();
        while(etiDiferentes.hasMoreElements()){
            String etiqueta = etiDiferentes.nextElement();
            double precision = 0.0;
            double recall = 0.0;
            precision = tabla_Etiqueta_indicadorPrecision.get(etiqueta);
            recall = tabla_Etiqueta_indicadorRecall.get(etiqueta);
            if((precision+recall) != 0.0){
                indicadorFScoreEtiqueta_i = ( 2 * ( (precision*recall) / (precision+recall) ) );
            }
            else{
                 indicadorFScoreEtiqueta_i = 0.0;
            }
            tabla_Etiqueta_indicadorFScore.put(etiqueta, indicadorFScoreEtiqueta_i);
        }                
    }

    //Método que se encarga de contar las apariciones de cada una de las etiquetas propagadas por LP,
    // y además, de contar cuántas de dichas propagadas, fueron correctas.
    private void construirTablasApariciones(){
        for(int i=0; i<listaEtiquetasEstimadas.size(); i++){
            String etiquetaCorrespondiente = listaEtiquetasCorrespondientes.elementAt(i);
            String etiquetaPropagadaLP = listaEtiquetasEstimadas.elementAt(i);
            if(tabla_Etiqueta_AparicionesTotalesEstimadas.get(etiquetaPropagadaLP) == null){
                tabla_Etiqueta_AparicionesTotalesEstimadas.put(etiquetaPropagadaLP, 1);
                tabla_Etiqueta_AparicionesCorrectas.put(etiquetaPropagadaLP, 0);
            }
            else{
                int aparicionesTotalesLPActual = tabla_Etiqueta_AparicionesTotalesEstimadas.get(etiquetaPropagadaLP);
                tabla_Etiqueta_AparicionesTotalesEstimadas.put(etiquetaPropagadaLP, ++aparicionesTotalesLPActual);
            }
            
            if(etiquetaCorrespondiente.equals(etiquetaPropagadaLP)){                
                int aparicionesCorrectasActual = tabla_Etiqueta_AparicionesCorrectas.get(etiquetaPropagadaLP);
                tabla_Etiqueta_AparicionesCorrectas.put(etiquetaPropagadaLP, ++aparicionesCorrectasActual);                
            }
            
            if(tabla_Etiqueta_AparicionesTotalesCorrespondientes.get(etiquetaCorrespondiente) == null)
                tabla_Etiqueta_AparicionesTotalesCorrespondientes.put(etiquetaCorrespondiente, 1);
            else{
                int aparicionesTotalesCorrespondientesActual = tabla_Etiqueta_AparicionesTotalesCorrespondientes.get(etiquetaCorrespondiente);
                tabla_Etiqueta_AparicionesTotalesCorrespondientes.put(etiquetaCorrespondiente, ++aparicionesTotalesCorrespondientesActual);
            }
        }        
    }

    public void calcularAciertosTotales(){        
        double correctas = 0;
        for(int i=0; i<listaEtiquetasCorrespondientes.size(); i++){
            String etiquetaCorrespondiente = listaEtiquetasCorrespondientes.elementAt(i);
            String etiquetaPropagadaLP = listaEtiquetasEstimadas.elementAt(i);
            if(etiquetaCorrespondiente.equals(etiquetaPropagadaLP))
                correctas++;
        }
        double total = listaEtiquetasCorrespondientes.size();        
        System.out.println("\n{"+correctas+" aciertos de "+total+"} | "+Porcentaje.obtenerPorcentaje((int)correctas,(int)total)+"% de efectividad");        
    }
    
    public void imprimirIndicadores(String algoritmo){ 
        etiDiferentes = tabla_Etiqueta_AparicionesTotalesCorrespondientes.keys();
        System.out.println("\nEtiqueta\tEstimadas por "+algoritmo+"\tCorrectas\tCorrespondientes\tPrecision\tRecall\tFScore");        
        while(etiDiferentes.hasMoreElements()){
            String tempEtiqueta = etiDiferentes.nextElement();
            int estimada = 0, correctas = 0, correspondientes = 0;
            double precision, recall, fscore;                  
            
            if(tabla_Etiqueta_AparicionesTotalesEstimadas.containsKey(tempEtiqueta)){
                estimada = tabla_Etiqueta_AparicionesTotalesEstimadas.get(tempEtiqueta);
            }
            if(tabla_Etiqueta_AparicionesCorrectas.containsKey(tempEtiqueta)){
                correctas = tabla_Etiqueta_AparicionesCorrectas.get(tempEtiqueta);
            }
            correspondientes = tabla_Etiqueta_AparicionesTotalesCorrespondientes.get(tempEtiqueta);
            precision = tabla_Etiqueta_indicadorPrecision.get(tempEtiqueta);
            recall = tabla_Etiqueta_indicadorRecall.get(tempEtiqueta);
            fscore = tabla_Etiqueta_indicadorFScore.get(tempEtiqueta);
            
            System.out.println(tempEtiqueta+"\t"+estimada+"\t"+correctas+"\t"+correspondientes+"\t"+precision+"\t"+recall+"\t"+fscore);
        }
    }

}