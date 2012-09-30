/* 
 * @author Jairo Andrés
 */


import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GestionarArchivos {

    private JFileChooser selectorArchivo;
    private FileNameExtensionFilter filtroExtensionArchivo;
    private int opcionSeleccionada;
    private String rutaArchivo;
    private final String tipo = "csv";   
    

    //Método que obtiene la ruta de un archivo, a partir de un selector visual de archivos.
    public String obtenerRutaArchivo(){
        selectorArchivo = new JFileChooser("C:/Users/Jairo Andrés/Desktop/Archivos de Pruebas CSV");
        filtroExtensionArchivo = new FileNameExtensionFilter("Archivos de texto (."+tipo+")", tipo);
        selectorArchivo.setFileFilter(filtroExtensionArchivo);
        opcionSeleccionada = selectorArchivo.showOpenDialog(new JTextArea());
        
        if (opcionSeleccionada == JFileChooser.APPROVE_OPTION){
            if (obtenerTipoArchivo(selectorArchivo.getSelectedFile().getName()).equals(tipo))
                rutaArchivo = selectorArchivo.getSelectedFile().getAbsolutePath();
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

    private String obtenerTipoArchivo(String nombreArchivo){
        String extension = "";
        int posPunto = nombreArchivo.lastIndexOf(".");        
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

    //Método para guardar un archivo con los datos ingresados como parámetro.
    public void guardar(String datos){       
        try{
            File f = new File("C:/Users/Jairo Andrés/Desktop/Archivos Guardados/Prueba");
            FileWriter escritor = new FileWriter(f+"."+"txt");
            BufferedWriter bufferescritor = new BufferedWriter(escritor);
   
            bufferescritor.write(datos,0,datos.length());
            bufferescritor.newLine();                      

            bufferescritor.close();
        }
        catch(Exception e){
                System.out.println("Error al guardar Archivo: "+e.getMessage());
                JOptionPane.showMessageDialog(null, "El archivo no se puede guardar","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
