/*
 * @author Jairo Andrés
 */
//Para realizar pruebas
import java.util.*;


public class Main {

    public static void main(String[] args) {
        
        LeerArchivoCSV l = new LeerArchivoCSV();
        l.leerYAlmacenarLineasCSV();        
        System.out.println("Cantidad comentarios: "+l.obtenerListaComentariosLeidos().size());        
}