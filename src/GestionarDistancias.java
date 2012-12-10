/*
 * @author Jairo Andrés
 */


import java.util.*;

public class GestionarDistancias {
    
    private Vector<Vector<Double>> listaDeDistanciasEntreComentarios;

    public void calcularDistanciaEuclideanaEntreCadaParDeComentarios(Vector<VectorFrecuenciasPalabras> vectorFrecuenciasComentarios){
        int N = vectorFrecuenciasComentarios.size();
        for(int i=0; i<(N-1); i++){
            for(int j=(i+1); j<N; j++){
                int x = i+1;
                int y = j+1;
                VectorFrecuenciasPalabras vectorA = vectorFrecuenciasComentarios.elementAt(i);
                VectorFrecuenciasPalabras vectorB = vectorFrecuenciasComentarios.elementAt(j);
                double distancia = calcularDistanciaEuclideanaEntreDosComentarios(vectorA, vectorB);
                System.out.println("N"+x+"\t"+"N"+y+"\t"+distancia);
                
            }
        }        
    }    

    public double calcularDistanciaEuclideanaEntreDosComentarios(
            VectorFrecuenciasPalabras vectorFrecuenciasComentarioA, VectorFrecuenciasPalabras vectorFrecuenciasComentarioB){
        
        double distanciaEuclideana = 0;
        for(int i=0; i<vectorFrecuenciasComentarioA.tamanio(); i++){
            int punto_i_ComentarioA = vectorFrecuenciasComentarioA.elementoEn(i);
            int punto_i_ComentarioB = vectorFrecuenciasComentarioB.elementoEn(i);
            if(punto_i_ComentarioA!=0 || punto_i_ComentarioB!=0){
                double restaPuntos = Math.pow( ((punto_i_ComentarioA+0.0) - (punto_i_ComentarioB+0.0)),  2);
                distanciaEuclideana += restaPuntos;                    
            }
        }        
        distanciaEuclideana = Math.sqrt(distanciaEuclideana);        
        return distanciaEuclideana;
    }

    public Vector<Vector<Double>> obtenerListaDeDistanciasEntreComentarios(){
        return listaDeDistanciasEntreComentarios;
    }

}
