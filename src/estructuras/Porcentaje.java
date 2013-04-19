package estructuras;

/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 16 de 2013
 */

public class Porcentaje {
    
    public static int calcularNumero(int numero, int porcentaje){
         int numeroCaculado = 0;
         
         double dNumero = numero+0.0;
         double dPorcentaje = porcentaje+0.0;
         double dPorcentajeCalculado = dNumero*(dPorcentaje/100);
         numeroCaculado = aproximar(dPorcentajeCalculado);         
         return numeroCaculado;
    }
    
    public static int obtenerPorcentaje(int cantidad, int numeroTotal){
         int porcentajeCalculado = 0;
         
         double dNumeroTotal = numeroTotal+0.0;
         double dCantidad = cantidad+0.0;
         double dPorcentajeCalculado = (dCantidad/dNumeroTotal)*100;
         porcentajeCalculado = aproximar(dPorcentajeCalculado);         
         return porcentajeCalculado;      
    }
    
    private static int aproximar(double numero){
        numero = Math.rint(numero); 
        int iNumero = (int) numero;        
        return iNumero;
    }
}
