package entrada_salida;

/*
 * @author Jairo Andrés
 * Ultima modificacion: Abril 16 de 2013
 */


import entrada_salida.GestionarArchivos;
import estructuras.Comentario;
import com.csvreader.CsvReader;
import java.util.Vector;

public class LeerArchivoCSV {

    private CsvReader lectorArchivosCSV;
    private String rutaArchivo;
    private GestionarArchivos gestionArchivos;
    private Vector<String> encabezadoArchivoCSV;
    private Vector<Comentario> listaComentariosLeidos;

    public LeerArchivoCSV(){
        listaComentariosLeidos = new Vector();
    }

    //Método que utiliza la clase GestionArchivos para obtener el texto contenido en un CSV.
    private boolean cargarCSV(){
        gestionArchivos = new GestionarArchivos();
        rutaArchivo = gestionArchivos.obtenerRutaArchivo("csv");
        if(!rutaArchivo.equals("vacia")){
            //Se convierte a UTF-8 para corregir errores como por ejemplo que la palabra "cómo" se muestra así: "cÃ³mo"
            lectorArchivosCSV = new CsvReader(gestionArchivos.convertirAFormatoUTF8(rutaArchivo));
            return true;
        }
        else{
            return false;            
        }
    }

    //Método que convierte cada fila de un archivo CSV en un objeto de la clase Comentario,
    //cada celda o campo del archivo corresponde a un atributo del objeto.
    public void leerYAlmacenarLineasCSV(){
        if(cargarCSV()){
            try{                
                Vector<Integer> indices = new Vector();
                indices = indicesColumnasALeer(obtenerEncabezadoCSV());
                while (lectorArchivosCSV.readRecord()){                       
                    Comentario comentarioLeido = convertirFilaLeidaAComentario(indices);
                    listaComentariosLeidos.addElement(comentarioLeido);
                    //System.out.println(comentarioLeido.aString());
                }                
            }catch(Exception e){
                System.out.println("Error en leerYAlmacenarLineasCSV: "+e.getMessage());
            }
        }        
    }

    //Método que obtiene la primera fila del archivo CSV, la cual correponde al encabezado
    //del mismo. Usa varios métodos auxiliares descritos posteriormente.
    private Vector<String> obtenerEncabezadoCSV(){
        try{
            Vector<String> encabezadoCSV = new Vector();
            lectorArchivosCSV.readRecord();
            int posColumna = 0;
            while(!lectorArchivosCSV.get(posColumna).isEmpty()){
                String campoLeido = lectorArchivosCSV.get(posColumna);
                encabezadoCSV.addElement(campoLeido);                
                posColumna++;
            }            
            this.encabezadoArchivoCSV = encabezadoCSV;
            return this.encabezadoArchivoCSV;
        }
        catch (Exception e){
            System.out.println("Error en obtenerEncabezadoCSV: "+e.getMessage());
            return null;
        }
    }

    //Método que identifica las posiciones de columnas que se deben leer.
    //Se omiten algunas columnas, para ello esta función.
    private Vector<Integer> indicesColumnasALeer(Vector<String> encabezadoCSV){
        Vector<Integer> indices = new Vector();
        for(int i=0; i<encabezadoCSV.size(); i++){
            if(filtrarColumna(encabezadoCSV.elementAt(i)))
                indices.addElement(i);
        }        
        return indices;
    }

    //Método que filtra las columnas que se desean omitir, por ejemplo
    //si se quiere omitir la columna que títula "Tono", este método devuelve false
    //cuando encuentra dicho título.
    private boolean filtrarColumna(String encabezadoColumna){
        if(encabezadoColumna.equals("Tono") || encabezadoColumna.equals("Tipo"))
            return false;
        else
            return true;
    }

    //Método auxiliar que lee una fila un archivo CSV y obtiene los datos de cada celda
    //relacionándolos con los atributos de la clase Comentario. AL final construye un objeto
    //comentario con los datos de la fila leida.
    private Comentario convertirFilaLeidaAComentario(Vector<Integer> indices){
        String autor = "";
        String mensaje = "";
        String fuente = "";
        Vector<String> etiquetasLeidasPorFila = new Vector();
        Vector<String> todasEtiquetasLeidasPorFila = new Vector();
        Comentario comentarioLeido;
        try{
            for(int i=0; i<indices.size(); i++){
                int posColumna = indices.elementAt(i);
                String campoLeido = lectorArchivosCSV.get(posColumna);                
                if(i==0){
                    autor = campoLeido.trim();
                }
                else if (i==1){                    
                    mensaje = campoLeido.trim();                    
                }
                else if (i==2){
                    fuente = campoLeido.trim();
                }
                else{
                    todasEtiquetasLeidasPorFila.addElement(campoLeido.trim().toLowerCase());
                    if(!campoLeido.trim().isEmpty()){
                        etiquetasLeidasPorFila.addElement(campoLeido.trim().toLowerCase());
                    }
                }
            }            
            etiquetasLeidasPorFila = identificarFormaEtiqueta(etiquetasLeidasPorFila,indices,todasEtiquetasLeidasPorFila);            
            comentarioLeido = new Comentario(autor, mensaje, fuente, etiquetasLeidasPorFila);            
        }
        catch(Exception e){
            System.out.println("Error en convertirFilaLeidaAComentario: "+e.getMessage());
            return null;
        }
        return comentarioLeido;
    }

    //Método que identifica la forma en que están estructuradas las etiquetas correspondientes
    //a los comentarios en un archivo CSV. Existen dos formas:
    //1) Cada celda contigua al mensaje del comentario tiene el nombre de una etiqueta.
    //   Ejemplo
    // Encabezado:  Mensaje | Categoria 1| Categoria 2
    // Comentario1:    X    |  EtiquetaA | EtiquetaB
    // Comentario1:    Y    |  EtiquetaC | EtiquetaD
    //2) Las celdas contiguas al mensaje del comentario, son valores que indican correspondencia,
    //   es decir: las etiquetas se encuentran en el encabezado, y si una etiqueta Y pertenece a un
    //   comentario X, en la fila del comentario X se asigna un valor en la columna que corresponda
    //   a la etiqueta Y, por ejemplo]:
    // Encabezado:  Mensaje | Etiqueta 1 | Etiqueta 2
    // Comentario1:    X    |      a     |
    // Comentario1:    Y    |            |     a
    //Cuando la celda está vacía es porque dicha etiqueta no corresponde al comentario.
    private Vector<String> identificarFormaEtiqueta(Vector<String> etiquetasLeidas, Vector<Integer> indices, Vector<String> todasEtiquetasLeidas){
        int posEtiquetaInicial = indices.elementAt(3);
        String etiquetaInicialEnEncabezadoCSV = encabezadoArchivoCSV.elementAt(posEtiquetaInicial);
        if(etiquetaInicialEnEncabezadoCSV.length()>=6)
            etiquetaInicialEnEncabezadoCSV = etiquetaInicialEnEncabezadoCSV.trim().substring(0, 6);
        //Forma 1
        if(etiquetaInicialEnEncabezadoCSV.equals("Catego")){
            return etiquetasLeidas;
        }
        //Forma 2
        else{            
            Vector<String> etiquetasIdentificadas = new Vector();
            Vector<Integer> posicionesDeEtiquetas = new Vector();
            for(int i=0; i<todasEtiquetasLeidas.size(); i++){
                if(!todasEtiquetasLeidas.elementAt(i).trim().equals("")){
                    posicionesDeEtiquetas.addElement(i);
                }
            }
            if(!posicionesDeEtiquetas.isEmpty()){                
                for(int i=0; i<posicionesDeEtiquetas.size(); i++){
                    int posicionEnIndices = posicionesDeEtiquetas.elementAt(i)+3;
                    int posicionEtiquetaEnEncabezadoCSV = indices.elementAt(posicionEnIndices);
                    String etiquetaIdentificada = encabezadoArchivoCSV.elementAt(posicionEtiquetaEnEncabezadoCSV);
                    etiquetasIdentificadas.addElement(etiquetaIdentificada.toLowerCase());
                }
            }
            return etiquetasIdentificadas;
        }
    }

    public Vector<Comentario> obtenerListaComentariosLeidos(){
        return listaComentariosLeidos;
    }
}


