/*
 * @author Jairo Andrés
 */


import java.util.*;

public class Preprocesamiento {
    
    
    private Vector<String> listaMensajesParaProcesar; //Variable temporal para realiazar pruebas.
    private Vector<String> listaMensajesProcesados;
    
    private final String[] listaPalabrasVacias = {
        "a","acuerdo","adelante","ademas","además","adrede","ahi","ahí","ahora","al","alli","allí","alrededor",
        "antano","antaño","ante","antes","apenas","aproximadamente","aquel","aquél","aquella","aquélla",
        "aquellas","aquéllas","aquello","aquellos","aquéllos","aqui","aquí","arriba","abajo","asi","así","aun",
        "aún","aunque","b","bajo","bastante","bien","breve","c","casi","cerca","claro","como","cómo","con",
        "conmigo","contigo","contra","cual","cuál","cuales","cuáles","cuando","cuándo","cuanta","cuánta",
        "cuantas","cuántas","cuanto","cuánto","cuantos","cuántos","d","de","debajo","del","delante","demasiado",
        "dentro","deprisa","desde","despacio","despues","después","detras","detrás","dia","día","dias","días",
        "donde","dónde","dos","durante","e","el","él","ella","ellas","ellos","en","encima","enfrente",
        "enseguida","entre","es","esa","ésa","esas","ésas","ese","ése","eso","esos","ésos","esta","está","ésta",
        "estado","estados","estan","están","estar","estas","éstas","este","éste","esto","estos","éstos","ex",
        "excepto","f","final","fue","fuera","fueron","g","general","gran","h","ha","habia","había","habla",
        "hablan","hace","hacia","han","hasta","hay","horas","hoy","i","incluso","informo","informó","j","junto",
        "k","l","la","lado","las","le","lejos","lo","los","luego","m","mal","mas","más","mayor","me","medio",
        "mejor","menos","menudo","mi","mí","mia","mía","mias","mías","mientras","mio","mío","mios","míos","mis",
        "mismo","mucho","muy","n","nada","nadie","ninguna","no","nos","nosotras","nosotros","nuestra","nuestras",
        "nuestro","nuestros","nueva","nuevo","nunca","ñ","o","os","otra","otros","p","pais","paìs","para","parte",
        "pasado","peor","pero","poco","por","porque","pronto","proximo","próximo","puede","q","qeu","que","qué",
        "quien","quién","quienes","quiénes","quiza","quizá","quizas","quizás","r","raras","repente","s","salvo",
        "se","sé","segun","según","ser","sera","será","si","sí","sido","siempre","sin","sobre","solamente",
        "solo","sólo","son","soyos","su","supuesto","sus","suya","suyas","suyo","t","tal","tambien","también",
        "tampoco","tarde","te","temprano","ti","tiene","todavia","todavía","todo","todos","tras","tu","tú",
        "tus","tuya","tuyas","tuyo","tuyos","u","un","una","unas","uno","unos","usted","ustedes","v","veces",
        "vez","vosotras","vosotros","vuestra","vuestras","vuestro","vuestros","w","x","y","ya","yo","z"        
    };

    private final String[] listaCaracteresEspeciales = {
        "¹", "²", "³", "™", "«", "»", "•", "°", "°", "º", "±", "‡", "»", "†", "«", "Ψ", "Ψψ", "Ψ", "Ψ", "φ", "ψ", "ˆ", "ˇ", "ˉ", "˘", "˙",
        "˚", "№", "™", "⅛", "⅜", "⅝", "⅞", "∞", "∫", "≈", "▀▄", "█", "▌", "▐", "▐", "░", "▒", "▓", "■", "■", "□", "▪", "▫", "▫▬",
        "▲", "►", "▼", "◄", "◊", "◊", "○", "●", "◘", "◘", "◙", "◦", "☺", "☻", "☻", "☼", "♀", "♂", "♠♠", "♣", "♥", "♦", "♫", "♬",
        "♪", "♩", "♭♪", "ﬂ", "©", "★", "☆웃", "❤", "유", "ツ", "ҳ̸Ҳ̸ҳ", "≠", "☠", "☮", "|♥|", "♋", "✿", "ﻬ", "ஐ", "✲❣·•●", "➸", "❝❞",
        "﹌", "✎", "✟", "➹", "❀", "☂", "♨☎", "☏", "✖", "♂", "♀", "๑", "۩", "✗☉", "▣", "⊙", "⊕", "♤", "εїз", "☜", "☞", "ⓐ", "ⓑ", "ⓒ",
        "ⓓ", "ⓔ", "ⓕ", "ⓖ", "ⓗ", "ⓘ", "ⓙ", "ⓚ", "ⓛ", "ⓜ", "ⓝ", "ⓞ", "ⓟ", "ⓠ", "ⓡ", "ⓢ", "ⓣ", "ⓤ", "ⓥ", "ⓦ", "ⓧ", "ⓨ", "ⓩ", "="
    };

    private final String[] listaSignosDePuntuacion = {
        "\\.", "\\?", "¿", ",", "¡", "!", "'", "\"", ":", ";", "\\(", "\\)", "\\[", "\\]", "\\{", "\\}", "-", "_", "“", "”"
    };

    private final String[] listaInicialesRuidoTuiter = {"@", "#", "rt"};

    private final String[] listaInicialesOnomatopeyas = {
        "jaja", "jeje", "jiji", "haha", "hehe", "hihi", "wow", "woow"
    };

    //Constructor que recibe una lista de comentarios, obtiene los mensajes de
    //cada uno de ellos y se los asigna a la variable de clase listaMensajesParaProcesar
    public Preprocesamiento(Vector<Comentario> listaComentarios){
        listaMensajesParaProcesar = new Vector();
        listaMensajesProcesados = new Vector();   
        for(int i=0; i<listaComentarios.size(); i++){
            String mensajeDeComentario = listaComentarios.elementAt(i).obtenerMensaje();                        
            listaMensajesParaProcesar.addElement(mensajeDeComentario);
            listaMensajesProcesados.addElement(mensajeDeComentario);
        }
    }

    //Método que recorre la lista de mensajes y ejecuta una función según el tipo que le ingrese como parámetro
    //Se apoya en métodos auxiliares definidos posteriormente. 
    public void ejecutarTipoPreProcesamiento(String tipo){
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

    public void ejecutarPreprocesamientoSecuencial(){
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
    

    //Método temporal para imprimir en consola los mensajes contenidos en el vector listaMensajesParaProcesar.
    public void imprimirMensajesParaProcesar(){
        if(!this.listaMensajesParaProcesar.isEmpty()){
            for(int i=0; i<listaMensajesParaProcesar.size(); i++){
                System.out.println(listaMensajesParaProcesar.elementAt(i));
            }
        }
    }

    //Método temporal para imprimir en consola los mensajes contenidos en el vector listaMensajesProcesados.
    public void imprimirMensajesProcesados(){
        if(!this.listaMensajesProcesados.isEmpty()){
            for(int i=0; i<listaMensajesProcesados.size(); i++){
                System.out.println(listaMensajesProcesados.elementAt(i));
            }
        }
    }
}
