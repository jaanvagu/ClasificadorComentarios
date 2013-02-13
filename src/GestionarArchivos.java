/* 
 * @author Jairo Andrés
 * Ultima modificacion: Febrero 13 de 2013
 */


import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GestionarArchivos {

    private JFileChooser selectorArchivo;
    private FileNameExtensionFilter filtroExtensionArchivo;
    private File archivo;
    private FileWriter escritor;
    private BufferedWriter bufferEscritor;
    private int opcionSeleccionada;
    private String rutaArchivo;    

    public static String nombreArchivoSinExtension;
    

    //Método que obtiene la ruta de un archivo, a partir de un selector visual de archivos.
    public String obtenerRutaArchivo(String tipo){
        selectorArchivo = new JFileChooser("C:/Users/Jairo Andrés/Desktop/Archivos de Prueba CSV");        
        filtroExtensionArchivo = new FileNameExtensionFilter("Archivos de texto (."+tipo+")", tipo);
        selectorArchivo.setFileFilter(filtroExtensionArchivo);
        opcionSeleccionada = selectorArchivo.showOpenDialog(new JTextArea());
        
        if (opcionSeleccionada == JFileChooser.APPROVE_OPTION){
            if (obtenerTipoArchivo(selectorArchivo.getSelectedFile().getName()).equals(tipo)){
                rutaArchivo = selectorArchivo.getSelectedFile().getAbsolutePath();
                System.out.println("Procesando: "+selectorArchivo.getSelectedFile().getName());
            }
            else{
                rutaArchivo = "vacia";
                System.out.println("El archivo cargado no es de tipo "+tipo);
                JOptionPane.showMessageDialog(null,"El archivo cargado no es de tipo "+tipo,"Información",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else{
            rutaArchivo = "vacia";
        }        
        
        return rutaArchivo;
    }

    //Retorna la extensión del archivo (txt, csv, pdf, etc.)
    private String obtenerTipoArchivo(String nombreArchivo){
        String extension = "";
        int posPunto = nombreArchivo.lastIndexOf(".");
        nombreArchivoSinExtension = nombreArchivo.substring(0,posPunto);
        extension = nombreArchivo.substring(posPunto+1,nombreArchivo.length());        
        return extension;
    }

    //Método que convierte el contenido de un archivo a formato UTF-8
    public InputStreamReader convertirAFormatoUTF8(String rutaArchivo){
        try{
            FileInputStream fis = new FileInputStream(rutaArchivo);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            return isr;
        }
        catch (Exception e){
            System.out.println("Error al convertir formato de archivo a UTF8: "+e.getMessage());
            return null;
        }
    }   

    //******* TEMPORAL ***********
    //Método que almacena en un directorio local los comentarios despues de procesados (normalizados), se guardan los
    //objetos, no el texto literal. Se usa la Serializacion para guardar directamente los objetos.
    public void guardarComentariosNormalizados(Vector<ComentarioNormalizado> listaAGuardarComentariosNormalizados){
        try{
            String temprutaArchivo = "/home/jairo/Documentos/Archivos Guardados/Consolidado "+nombreArchivoSinExtension+" - Distribuido";
            
            FileOutputStream archivoDeSalida = new FileOutputStream(temprutaArchivo);
            ObjectOutputStream objetoSalida = new ObjectOutputStream(archivoDeSalida);
            
            objetoSalida.writeObject(listaAGuardarComentariosNormalizados);
           
            objetoSalida.close();
        }catch(Exception e){
            System.out.println("Error al guardar lista de comentarios normalizados\n"+e.getMessage());
        }
    }

    //******* TEMPORAL ***********
    public Vector<ComentarioNormalizado> cargarComentariosNormalizados(){
        String temprutaArchivo = obtenerRutaArchivo2();
        Vector<ComentarioNormalizado> listaComentariosLeidos = new Vector();
        try{
            FileInputStream input = new FileInputStream(temprutaArchivo);
            ObjectInputStream objectInput = new ObjectInputStream(input);
            Object objetoLeido = objectInput.readObject();
            listaComentariosLeidos = (Vector<ComentarioNormalizado>) objetoLeido;
        }
        catch(Exception e){
            System.out.println("Error cargar archivo Comentario Normalizado: "+e.getMessage());
        }
        return listaComentariosLeidos;
    }

    //Método que crea un archivo para escribir en el texto plano, y lo almacena en un directorio local.
    public void crearArchivoTexto(String tipo){
        try{
            archivo = new File("/home/jairo/Escritorio/Datos_Prueba/Datos_"+nombreArchivoSinExtension+"/"+tipo+"_"+nombreArchivoSinExtension);
            escritor = new FileWriter(archivo);
            bufferEscritor = new BufferedWriter(escritor);                                               
        }
        catch(Exception e){
                System.out.println("Error al guardar Archivo texto: "+e.getMessage());
        }
    }

    //Método que cierra un archivo después de que se termino de escribir en él.
    public void cerrarArchivoTexto(){
        try{
            bufferEscritor.close();
        }
        catch(Exception e){
                System.out.println("Error al cerrar Archivo texto: "+e.getMessage());
        }
    }

    //Método que escribe linea de texto plano en un archivo almacenado en directorio local.
    public void escribirLineaEnArchivoTexto(String linea){
        try{                        
            bufferEscritor.write(linea);
            bufferEscritor.newLine();            
        }
        catch(Exception e){
                System.out.println("Error al escribir linea Archivo texto: "+e.getMessage());
        }
    }

    //*********** TEMPORAL ***********
    public String obtenerRutaArchivo2(){
        selectorArchivo = new JFileChooser("C:/Users/Jairo Andrés/Desktop/Datos_Prueba");
        opcionSeleccionada = selectorArchivo.showOpenDialog(new JTextArea());

        if (opcionSeleccionada == JFileChooser.APPROVE_OPTION){
            
                rutaArchivo = selectorArchivo.getSelectedFile().getAbsolutePath();
                //nombreArchivoSinExtension = recortarNombre(selectorArchivo.getSelectedFile().getName());
                System.out.println("Procesando: "+selectorArchivo.getSelectedFile().getName());            
        }
        else{
            rutaArchivo = "vacia";
        }

        return rutaArchivo;
    }
    
    public String recortarNombre(String nombreCompletoArchivo){
        StringTokenizer stNombreCompletoArchivo = new StringTokenizer(nombreCompletoArchivo);
        stNombreCompletoArchivo.nextToken();
        String nombreArchivoRecortado = stNombreCompletoArchivo.nextToken().toLowerCase().trim();        
        return nombreArchivoRecortado;
    }
}


