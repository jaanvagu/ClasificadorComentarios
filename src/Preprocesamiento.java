/*
 * @author Jairo Andrés
 */


import java.util.*;

public class Preprocesamiento {
            
    private Vector<String> listaMensajesProcesados;
    
    private final String[] listaPalabrasVacias = {
        "a","acuerdo","adelante","ademas","adrede","ahi","ahora","al","alli","alrededor", "antano","antaño","ante","antes",
        "apenas","aproximadamente","aquel","aquella", "aquellas","aquello","aquellos","aqui","arriba","abajo","asi",
        "aun","aunque","b","bajo","bastante","bien","breve","c","casi","cerca","claro","como","con", "conmigo","contigo","contra",
        "cual","cuales","cuando","cuanta","cuantas","cuanto","cuantos","d","de","debajo","del","delante","demasiado",
        "dentro","deprisa","desde","despacio","despues","detras","dia","dias", "donde","dos","durante","e","el","ella",
        "ellas","ellos","en","encima","enfrente", "enseguida","entre","es","esa","esas","ese","eso","esos","esta","está",
        "estado","estados","estan","estar","estas","este","esto","estos","ex", "excepto","f","final","fue","fuera","fueron","g",
        "general","gran","h","ha","habia","habla", "hablan","hace","hacia","han","hasta","hay","horas","hoy","i","incluso","informo",
        "j","junto", "k","l","la","lado","las","le","lejos","lo","los","luego","m","mal","mas","mayor","me","medio", "mejor","menos",
        "menudo","mi","mia","mias","mientras","mio","mios","mis", "mismo","mucho","muy","n","nada","nadie","ninguna","no",
        "nos","nosotras","nosotros","nuestra","nuestras", "nuestro","nuestros","nueva","nuevo","nunca","ñ","o","os","otra","otros","p",
        "pais","para","parte", "pasado","peor","pero","poco","por","porque","pronto","proximo","puede","q","qeu","que", "quien",
        "quienes","quiza","quizas","r","raras","repente","s","salvo", "se","segun","ser","sera","si","sido","siempre",
        "sin","sobre","solamente", "solo","son","soyos","su","supuesto","sus","suya","suyas","suyo","t","tal","tambien", "tampoco",
        "tarde","te","temprano","ti","tiene","todavia","todo","todos","tras","tu", "tus","tuya","tuyas","tuyo","tuyos","u","un","una",
        "unas","uno","unos","usted","ustedes","v","veces", "vez","vosotras","vosotros","vuestra","vuestras","vuestro","vuestros","w","x","y",
        "ya","yo","z"
    };

    private final String[] listaCaracteresEspeciales = {
        "¹", "²", "³", "™", "«", "»", "•", "°", "°", "º", "±", "‡", "»", "†", "«", "Ψ", "Ψψ", "Ψ", "Ψ", "φ", "ψ", "ˆ", "ˇ", "ˉ", "˘", "˙",
        "˚", "№", "™", "⅛", "⅜", "⅝", "⅞", "∞", "∫", "≈", "▀▄", "█", "▌", "▐", "▐", "░", "▒", "▓", "■", "■", "□", "▪", "▫", "�", 
        "▫▬", "▲", "►", "▼", "◄", "◊", "◊", "○", "●", "◘", "◘", "◙", "◦", "☺", "☻", "☻", "☼", "♀", "♂", "♠♠", "♣", "♥", "♦", "♫",
        "♬", "♪", "♩", "♭♪", "ﬂ", "©", "★", "☆웃", "❤", "유", "ツ", "ҳ̸Ҳ̸ҳ", "≠", "☠", "☮", "|♥|", "♋", "✿", "ﻬ", "ஐ", "✲❣·•●", "➸", "❝❞"
        ,"﹌", "✎", "✟", "➹", "❀", "☂", "♨☎", "☏", "✖", "♂", "♀", "๑", "۩", "✗☉", "▣", "⊙", "⊕", "♤", "εїз", "☜", "☞", "ⓐ", "ⓑ", "ⓒ",
        "ⓓ", "ⓔ", "ⓕ", "ⓖ", "ⓗ", "ⓘ", "ⓙ", "ⓚ", "ⓛ", "ⓜ", "ⓝ", "ⓞ", "ⓟ", "ⓠ", "ⓡ", "ⓢ", "ⓣ", "ⓤ", "ⓥ", "ⓦ", "ⓧ", "ⓨ", "ⓩ", "="
    };

    private final String[] listaSignosDePuntuacion = {
        "\\.", "\\?", "¿", ",", "¡", "!", "'", "\"", ":", ";", "\\(", "\\)", "\\[", "\\]", "\\{", "\\}", "-", "_", "“", "”"
    };

    private final String[] listaInicialesRuidoTuiter = {"@", "#", "rt", "twitter", "tweet"};

    private final String[] listaInicialesOnomatopeyas = {
        "jaja", "jeje", "jiji", "haha", "hehe", "hihi", "wow", "woow"
    };

    //Constructor que recibe una lista de comentarios, obtiene los mensajes de
    //cada uno de ellos, se los asigna a la variable de clase listaMensajesParaProcesar y finalemente ejecuta
    //algunas funciones de limpieza de datos sobre dicha lista.
    public Preprocesamiento(Vector<Comentario> listaComentarios){        
        listaMensajesProcesados = new Vector();   
        for(int i=0; i<listaComentarios.size(); i++){
            String mensajeDeComentario = listaComentarios.elementAt(i).obtenerMensaje();                                    
            listaMensajesProcesados.addElement(mensajeDeComentario);
        }

        ejecutarPreprocesamientoSecuencial();
    }

    //Método que recorre la lista de mensajes y ejecuta una función según el tipo que le ingrese como parámetro
    //Se apoya en métodos auxiliares definidos posteriormente. 
    private void ejecutarTipoPreProcesamiento(String tipo){
        for(int i=0; i<listaMensajesProcesados.size(); i++){
            StringBuilder mensajePreProcesado = new StringBuilder();
            StringTokenizer tokensMensajeParaProcesar = new StringTokenizer(listaMensajesProcesados.elementAt(i));
            while(tokensMensajeParaProcesar.hasMoreTokens()){
                String palabra = tokensMensajeParaProcesar.nextToken();
                
                if(tipo.equals("eliminarSignosDePuntuacion")){
                    mensajePreProcesado.append(quitarSignosDePuntuacionEnPalabra(palabra)).append(" ");
                }                
                else if (tipo.equals("eliminarRuidoTuiter")){
                    if(!esRuidoTuiter(palabra))
                        mensajePreProcesado.append(palabra).append(" ");
                }
                else if (tipo.equals("eliminarURLs")){
                    if(!esURL(palabra))
                        mensajePreProcesado.append(palabra).append(" ");
                }
                else if (tipo.equals("eliminarOnomatopeyas")){
                    if(!esOnomatopeya(palabra))
                        mensajePreProcesado.append(palabra).append(" ");
                }                
                else if (tipo.equals("eliminarCaracteresEspeciales")) {
                    mensajePreProcesado.append(quitarCaracteresEspecialesEnPalabra(palabra)).append(" ");
                }
                else if (tipo.equals("convertirAMinusculas")) {
                    mensajePreProcesado.append(convertirAMinusculas(palabra)).append(" ");
                }
                else if (tipo.equals("eliminarAcentos")) {
                    mensajePreProcesado.append(quitarAcentos(palabra)).append(" ");
                }
                else if (tipo.equals("eliminarPalabrasVacias")){
                    if(!esPalabraVacia(palabra))
                        mensajePreProcesado.append(palabra).append(" ");
                }
                else if (tipo.equals("eliminarEspaciosEnBlancoAdicionales")){
                    mensajePreProcesado.append(palabra).append(" ");
                }
                else{
                    System.out.println("Tipo mal escrito");
                }
            }
            listaMensajesProcesados.setElementAt(mensajePreProcesado.toString(),i);
        }
    }    

    //Toma un palabra y quita los signos de puntuacion que encuentre. 
    //Finalmente retorna la palabra sin signos de puntuacion.
    private String quitarSignosDePuntuacionEnPalabra(String palabra){               
        for(int i=0; i<listaSignosDePuntuacion.length; i++){            
            palabra = palabra.replaceAll(listaSignosDePuntuacion[i], "");
        }
        String palabraSinSignos = palabra.trim();
        return palabraSinSignos;
    }

    //Identidica si una palabra contiene ruido generado por la red social tuiter.
    private boolean esRuidoTuiter(String palabra){
        if(palabra.startsWith(listaInicialesRuidoTuiter[0]) || palabra.startsWith(listaInicialesRuidoTuiter[1]) ||
                palabra.startsWith(listaInicialesRuidoTuiter[2])){
            return true;
        }
        else
            return false;
    }

    //Identifica si una palabra es una direccion web o URL
    private boolean esURL(String palabra){
        if(palabra.startsWith("http") || palabra.startsWith("www"))
            return true;
        else
            return false;
    }

    //Identifica si una palabra es onomatopeya
    private boolean esOnomatopeya(String palabra){
        for(int i=0; i<listaInicialesOnomatopeyas.length; i++){
            if(palabra.startsWith(listaInicialesOnomatopeyas[i]))
                return true;            
        }
        return false;
    }

    //Toma un palabra y quita los caracteres especiales que encuentre.
    //Finalmente retorna la palabra sin caracteres especiales.
    private String quitarCaracteresEspecialesEnPalabra(String palabra){
        for(int i=0; i<listaCaracteresEspeciales.length; i++){
            palabra = palabra.replaceAll(listaCaracteresEspeciales[i], "");
        }
        String palabraSinCaracteres = palabra.trim();
        return palabraSinCaracteres;
    }

    //Recibe una palabra y la retorna con cada una de sus letras en minuscula.
    private String convertirAMinusculas(String mensaje){
        return mensaje.toLowerCase();
    }
    
    //Elimina los acentos o tildes que encuentre en una palabra.
    private String quitarAcentos(String palabra) {
        palabra = palabra.replaceAll("á","a");
        palabra = palabra.replaceAll("é","e");
        palabra = palabra.replaceAll("í","i");
        palabra = palabra.replaceAll("ó","o");
        palabra = palabra.replaceAll("ú","u");
        palabra = palabra.replaceAll("à","a");
        palabra = palabra.replaceAll("è","e");
        palabra = palabra.replaceAll("ì","i");
        palabra = palabra.replaceAll("ò","o");
        palabra = palabra.replaceAll("ù","u");
        return palabra;
    }

    //Identifica si una palabra es "Palabra Vacia" (Las palabras vacias estan definidas como atributo de clase).
    private boolean esPalabraVacia(String palabra){
        for(int i=0; i<listaPalabrasVacias.length; i++){
            if(palabra.equals(listaPalabrasVacias[i]))
                return true;
        }
        return false;
    }

    private void ejecutarPreprocesamientoSecuencial(){
        ejecutarTipoPreProcesamiento("eliminarSignosDePuntuacion");
        ejecutarTipoPreProcesamiento("eliminarCaracteresEspeciales");
        ejecutarTipoPreProcesamiento("convertirAMinusculas");
        ejecutarTipoPreProcesamiento("eliminarAcentos");
        ejecutarTipoPreProcesamiento("eliminarRuidoTuiter");
        ejecutarTipoPreProcesamiento("eliminarURLs");
        ejecutarTipoPreProcesamiento("eliminarOnomatopeyas");
        ejecutarTipoPreProcesamiento("eliminarPalabrasVacias");
        ejecutarTipoPreProcesamiento("eliminarEspaciosEnBlancoAdicionales");
    }

    public Vector<String> obtenerMensajesProcesados(){
        return listaMensajesProcesados;
    }            
}
