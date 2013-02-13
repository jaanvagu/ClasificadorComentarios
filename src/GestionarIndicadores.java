/*
 * @author Jairo Andrés
 * Ultima modificacion: Febrero 13 de 2013
 */


import java.util.*;

public class GestionarIndicadores {

    private Vector<String> listaEtiquetasCorrespondientes;
    private Vector<String> listaEtiquetasPropagadasLP;
    private Hashtable<String,Integer> tabla_Etiqueta_AparicionesCorrectas;
    private Hashtable<String,Integer> tabla_Etiqueta_AparicionesTotalesLP;
    private Hashtable<String,Integer> tabla_Etiqueta_AparicionesTotalesCorrespondientes;
    private Hashtable<String,Double> tabla_Etiqueta_indicadorPresicion;
    private Hashtable<String,Double> tabla_Etiqueta_indicadorRecall;
    private Hashtable<String,Double> tabla_Etiqueta_indicadorFScore;

    public GestionarIndicadores(Vector<String> listaEttiquetasCorrespondientes, Vector<String> listaEtiquetasPropagadasLP){
        this.listaEtiquetasCorrespondientes = listaEttiquetasCorrespondientes;
        this.listaEtiquetasPropagadasLP = listaEtiquetasPropagadasLP;        
        tabla_Etiqueta_AparicionesCorrectas = new Hashtable();
        tabla_Etiqueta_AparicionesTotalesLP = new Hashtable();
        tabla_Etiqueta_AparicionesTotalesCorrespondientes = new Hashtable();
        tabla_Etiqueta_indicadorPresicion = new Hashtable();
        tabla_Etiqueta_indicadorRecall = new Hashtable();
        tabla_Etiqueta_indicadorFScore = new Hashtable();
        construirTablasApariciones();
    }

    //Cálacula el indicador "presicion" para las etiquetas de una lista de comentarios. 
    public void calcularIndicadorPresicionParaEtiquetas(){
        double indicadorPresicionEtiqueta_i = 0;
        for(int i=0; i<listaEtiquetasCorrespondientes.size(); i++){
            String etiqueta = listaEtiquetasCorrespondientes.elementAt(i);
            int aparicionesCorrectas = 0;
            int aparicionesTotales = 0;
            if(!(tabla_Etiqueta_AparicionesCorrectas.get(etiqueta) == null)){
                aparicionesCorrectas = tabla_Etiqueta_AparicionesCorrectas.get(etiqueta);            
                aparicionesTotales = tabla_Etiqueta_AparicionesTotalesLP.get(etiqueta);
                indicadorPresicionEtiqueta_i = (aparicionesCorrectas+0.0)/(aparicionesTotales+0.0);
            }     
            else
                indicadorPresicionEtiqueta_i = 0;
            tabla_Etiqueta_indicadorPresicion.put(etiqueta, indicadorPresicionEtiqueta_i);
        }                
        Enumeration<String> etiDiferentes = tabla_Etiqueta_indicadorPresicion.keys();
        System.out.print("\nEtiquetas: ");
        while(etiDiferentes.hasMoreElements())
            System.out.print(etiDiferentes.nextElement()+", ");
        System.out.println("\nPresicion: "+tabla_Etiqueta_indicadorPresicion.values());
    }

    //Cálacula el indicador "recall" para las etiquetas de una lista de comentarios.
    public void calcularIndicadorRecallParaEtiquetas(){
        double indicadorRecallEtiqueta_i = 0;
        for(int i=0; i<listaEtiquetasCorrespondientes.size(); i++){
            String etiqueta = listaEtiquetasCorrespondientes.elementAt(i);
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
        System.out.println("Recall: "+tabla_Etiqueta_indicadorRecall.values());
    }

    //Cálacula el indicador "FScore" para las etiquetas de una lista de comentarios.
    public void calcularIndicadorFScoreParaEtiquetas(){
        double indicadorFScoreEtiqueta_i = 0.0;
        for(int i=0; i<listaEtiquetasCorrespondientes.size(); i++){
            String etiqueta = listaEtiquetasCorrespondientes.elementAt(i);
            double presicion = 0.0;
            double recall = 0.0;
            presicion = tabla_Etiqueta_indicadorPresicion.get(etiqueta);
            recall = tabla_Etiqueta_indicadorRecall.get(etiqueta);
            if((presicion+recall) != 0.0){
                indicadorFScoreEtiqueta_i = ( 2 * ( (presicion*recall) / (presicion+recall) ) );
            }
            else{
                 indicadorFScoreEtiqueta_i = 0.0;
            }
            tabla_Etiqueta_indicadorFScore.put(etiqueta, indicadorFScoreEtiqueta_i);
        }        
        System.out.println("FScore: "+tabla_Etiqueta_indicadorFScore.values());
    }

    //Método que se encarga de contar las apariciones de cada una de las etiquetas propagadas por LP,
    // y además, de contar cuántas de dichas porpagas, fueron correctas.
    private void construirTablasApariciones(){
        for(int i=0; i<listaEtiquetasPropagadasLP.size(); i++){
            String etiquetaCorrespondiente = listaEtiquetasCorrespondientes.elementAt(i);
            String etiquetaPropagadaLP = listaEtiquetasPropagadasLP.elementAt(i);
            if(tabla_Etiqueta_AparicionesTotalesLP.get(etiquetaPropagadaLP) == null){
                tabla_Etiqueta_AparicionesTotalesLP.put(etiquetaPropagadaLP, 1);
                tabla_Etiqueta_AparicionesCorrectas.put(etiquetaPropagadaLP, 0);
            }
            else{
                int aparicionesTotalesLPActual = tabla_Etiqueta_AparicionesTotalesLP.get(etiquetaPropagadaLP);
                tabla_Etiqueta_AparicionesTotalesLP.put(etiquetaPropagadaLP, ++aparicionesTotalesLPActual);
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
        System.out.println("Total LP: "+tabla_Etiqueta_AparicionesTotalesLP);
        System.out.println("Correctas: "+tabla_Etiqueta_AparicionesCorrectas);
        System.out.println("Correspon: "+tabla_Etiqueta_AparicionesTotalesCorrespondientes);
    }

    public void calcularAciertosTotales(){
        double presicion = 0;
        double correctas = 0;
        for(int i=0; i<listaEtiquetasCorrespondientes.size(); i++){
            String etiquetaCorrespondiente = listaEtiquetasCorrespondientes.elementAt(i);
            String etiquetaPropagadaLP = listaEtiquetasPropagadasLP.elementAt(i);
            if(etiquetaCorrespondiente.equals(etiquetaPropagadaLP))
                correctas++;
        }
        double total = listaEtiquetasCorrespondientes.size();
        presicion = correctas/total;
        System.out.println("\n{"+correctas+" aciertos de "+total+"}");        
    }

}