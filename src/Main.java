/*
 * @author Jairo Andrés
 */


public class Main {

    public static void main(String[] args) {
                        
        LeerArchivoCSV l = new LeerArchivoCSV();
        l.leerYAlmacenarLineasCSV();

        Preprocesamiento p = new Preprocesamiento(l.obtenerListaComentariosLeidos());        
        p.ejecutarPreprocesamientoSecuencial();                                                 
    }
}