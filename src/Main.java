/*
 * @author Jairo Andrés
 */


public class Main {

    private static LeerArchivoCSV leerArchivoCSV;
    private static Preprocesamiento preprocesamiento;
    private static Lematizar lematizar;

    public static void main(String[] args) {
        
        leerArchivoCSV = new LeerArchivoCSV();
        leerArchivoCSV.leerYAlmacenarLineasCSV();
        preprocesamiento = new Preprocesamiento(leerArchivoCSV.obtenerListaComentariosLeidos());
        lematizar = new Lematizar(preprocesamiento.obtenerMensajesProcesados());        
    }
}