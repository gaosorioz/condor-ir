package condorIR;

import clasesIR.Paper;
import clasesIR.PaperJDBC;
import clasesIR.Query;
import clasesIR.QueryJDBC;
import clasesIR.QueryPaper;
import clasesIR.QueryPaperJDBC;

import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.exception.ZeroByteFileException;
import org.apache.tika.langdetect.OptimaizeLangDetector;

import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.joda.time.LocalTime;

import org.tartarus.snowball.ext.EnglishStemmer;
import org.tartarus.snowball.ext.SpanishStemmer;
import org.tartarus.snowball.ext.PortugueseStemmer;

import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.spanishStemmer;
import org.tartarus.snowball.ext.portugueseStemmer;


/**
 * En esta clase de definen los diferentes métodos que permiten manipular
 * los metadatos y contenidos de un  repositorio
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/08
  */


public class Condor {

    public static final String ARCH_COD_LATEX = "/home/gaoz/MEGAsync/dataset/config-CondorIR/codLaTeX.txt";    
    public static final String DIR_CONFIG = "/home/gaoz/MEGAsync/dataset/config-CondorIR/";    
    public static final String ARCH_NO_ENC = "/home/gaoz/MEGAsync/dataset/config-CondorIR/noEnc.txt";    
/**
 * Devuelve el vector con el path de cada uno de los archivos de una carpeta
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/08
 * @param dir Carpeta desde se extraen todos los nombres de archivo
  * @return Devuelve un vector de cadenas con el path de cada uno de archivos de
 * la carpeta

*/    
 
public static String [] vecNomArch(String dir) {
    File carpeta = new File(dir);
    String [] listado = carpeta.list();
    String [] VecNomArch = new String[listado.length];
    if (listado.length == 0) {
        System.err.println("No hay elementos dentro de la carpeta" + dir);
        return null;
    }
    else {
        int i = 0;
        for (String nomArch : listado) {
            VecNomArch[i] = dir + nomArch;
            i++;
            }
        System.out.println("\n\nNúmero de archivos en la carpeta " + dir + " es --> " +i);
        return VecNomArch;
         }    

}

/**
 * Devuelve el vector con el (solo) nombre de cada uno de los archivos de una carpeta
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/08
 * @param dir Carpeta desde se extraen todos los nombres de archivo
 * @return Devuelve un vector de cadenas con los nombres de archivos de
 * la carpeta 
 */    

public static String [] vecNomArchSinDir(String dir) {
    File carpeta = new File(dir);
    String [] VecNomArch = carpeta.list();
    if (VecNomArch.length == 0) {
        System.err.println("No hay elementos dentro de la carpeta " + dir);
        return null;
    }
    else {
        return VecNomArch;
         }  
}

/**
 * Devuelve la similitud de dos cadenas (rango 0-1), basada en la Distancia de 
 * Levenshtein
 * @version: 2020/08/13
 * @param s1 Cadena inicial
 * @param s2 Cadena con la que se compara
 * @return Devuelve un valor entre 0 y 1 que corresponde a la similitud entre
 * ambas cadenas
 */
// http://rosettacode.org/wiki/Levenshtein_distance#Java
  public static double simDosCad(String s1, String s2) {
    String longer = s1, shorter = s2;
    if (s1.length() < s2.length()) { // longer should always have greater length
      longer = s2; shorter = s1;
    }
    int longerLength = longer.length();
    if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
    /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
    LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
    return (longerLength - distLevenshtein(longer, shorter)) / (double) longerLength;

  }

 /**
 * Distancia de edición de Levenshtein
 * @version: 2020/08/31
 * @param s1 Cadena inicial
 * @param s2 Cadena con la que se compara
 * @return Devuelve la distancia de Levenshtein entre dos cadenas
 */
 
  // http://rosettacode.org/wiki/Levenshtein_distance#Java
  public static int distLevenshtein(String s1, String s2) {
    s1 = s1.toLowerCase();
    s2 = s2.toLowerCase();

    int[] costs = new int[s2.length() + 1];
    for (int i = 0; i <= s1.length(); i++) {
      int lastValue = i;
      for (int j = 0; j <= s2.length(); j++) {
        if (i == 0)
          costs[j] = j;
        else {
          if (j > 0) {
            int newValue = costs[j - 1];
            if (s1.charAt(i - 1) != s2.charAt(j - 1))
              newValue = Math.min(Math.min(newValue, lastValue),
                  costs[j]) + 1;
            costs[j - 1] = lastValue;
            lastValue = newValue;
          }
        }
      }
      if (i > 0)
        costs[s2.length()] = lastValue;
    }
    return costs[s2.length()];
  }

/**
 * Escribe la similitud entre dos cadenas
 * @version: 2020/08/31
 * @param s Cadena inicial
 * @param t Cadena con la que se compara
  */

  
public static void escSimDosCad(String s, String t) {
    System.out.println(String.format("%.3f es la similitud entre \"%s\" and \"%s\"", simDosCad(s, t), s, t));
  }

/**
 * Devuelve el contenido de un metadato
 * @version: 2020/09/15
 * @param content Cadena desde donde se va a extraer el metadato
 * @param cadBusIni Cadena que contiene delimitador inicial y nombre del metadato
 * @param cadBusFin Cadena con el delimitador final del metadato
 * @param posMetIni Posición inicial dentro de la cadena desde dónde buscar. Se 
 * coloca 0 para buscar desde el principio
 * @param incluye La longitud del texto del metadato, si éste no se va a incluir
 * en la devolución. Se coloca 0 para incluirlo
 * @return Devuelve la cadena con el contenido del metadato
  */

public static String devMeta(String content, String cadBusIni, String cadBusFin,
                            int posMetIni, int incluye){
    String cad;
    int posMetaFin;
    posMetIni = content.indexOf(cadBusIni, posMetIni);    
    if (posMetIni == -1) 
    {
        return "";
    }
    posMetaFin = content.indexOf(cadBusFin, posMetIni + incluye + 1);
    if (posMetaFin == -1) {
//        System.out.println("devMeta 1 " + posMetIni + " | " + content.length());
        if (posMetIni<content.length()) {
//            System.out.println("devMeta 2 " +posMetIni + " | " + content.length());
            cad = content.substring(posMetIni + incluye, content.length());
            return cad;
        }
       return "";
    }
//    System.out.println("devMeta 3 " + posMetIni + " | " + content.length());
    cad = content.substring(posMetIni + incluye, posMetaFin);
    return cad;
}

/**
 * Crea un archivo de texto plano con extensión txt, a partir de una cadena con
 * el contenido
 * @version: 2020/09/15
 
 * @param nDir Cadena con el nombre de la carpeta que contendrá el archivo
 * @param nArch Nombre del archivo a crar
  * @param contArch Cadena que contiene el texto del archivo a crear
  */

@SuppressWarnings("ConvertToTryWithResources")
    public static void creaArchTxt(String nDir, String nArch, String contArch){
        String pathArch;
        pathArch = nDir + nArch;
        try {
            FileWriter arch = new FileWriter(pathArch);
            arch.write(contArch);
            arch.close();
        } catch (IOException e) {
            System.err.println("Falló la escritura del archivo" + e);
        }
    }
/**
 * Crea una carpeta con todos archivos del repositorio, pero, en formato de texto 
 * plano
 * @throws java.io.IOException Excepción para error de lectura
 * @throws org.apache.tika.exception.TikaException Excepción para archivo cuando 
 * lo procesa Tika
 * @version: 2020/09/15
 * @param tipoEjec Tipo de ejecución: "test"; "producción"
  */

@SuppressWarnings("ConvertToTryWithResources")
    public static void creaDirArchTxt(String tipoEjec) throws IOException, TikaException {    
        long inicio = System.currentTimeMillis();
        Boolean pare = false;
        int numArch = 0;
        String textLog, dirLog, dirRepPdf, dirArchTex, archConfig;
        String texto;
        textLog= "ARCHIVOS QUE FALLARON\n\n";
        String sistOperativo = System.getProperty("os.name");
        textLog += "Sistema operativo -> " + sistOperativo;
//        if (sistOperativo.equalsIgnoreCase("linus")) {
//           dir = "/home/gaoz/MEGAsync/w/tests/";
//             dir = "/home/gaoz/Documents/MendeleyOK/";
//        String dirConfig;
//        dirConfig = DIR_CONFIG;
        archConfig = Condor.archCfg(tipoEjec);
/*        if (tipoEjec.equalsIgnoreCase("test")) 
            archConfig= dirConfig + "config-CondorIR-test.cfg";
        else
            archConfig= dirConfig + "config-CondorIR.cfg"; */
        texto = loadArchTxt(archConfig);

        dirRepPdf = Condor.devMeta(texto, "&dirDataPdf  =", "&", 0, 14);
        dirLog = Condor.devMeta(texto, "&dirLog      =", "&", 0, 14);
        dirArchTex = Condor.devMeta(texto, "&dirDataTxt  =", "&", 0, 14);
        if (pare) 
            return;
// ________________________________________________________        
        File fDir = new File(dirRepPdf);
// Crear una instancia de un objeto Date invocando su constructor 
        Date fecha = new Date(); 
        String fechaStr = fecha.toString();
        textLog += "\n\n" + fechaStr + "\n\n";
     //Instantiating tika facade class 
        Tika tika = new Tika();
      String nomArch = "";
      String linea = new String(new char[60]).replace("\0", "_");
      int i = 0;
      try(DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(dirRepPdf))) {
          for(Path ruta : ds ) {
            nomArch = ruta.getFileName().toString();
            int posPunto = nomArch.indexOf(".", nomArch.length()-5);
              if (posPunto == -1) {
                  System.err.println("Error en archivo ->" + nomArch);
                  textLog = textLog + "posPunto Error por nombre de archivo ---> " + nomArch +"\n";
                  continue;
              }
            String nomArchTxt = nomArch.substring(0, posPunto);
            String conteArch;
              try {
                File archivo = new File(fDir, nomArch);
                conteArch = tika.parseToString(archivo);
                String filetype = tika.detect(archivo);
                i++;
/*                if (i < 10) {
                    System.out.println(filetype);      
                    System.out.println("Tamaño " + nomArch + " = " + conteArch.length());
                  }
*/                
                File f = new File(dirArchTex, nomArchTxt); 
                Boolean existe = f.exists();
                
                if  (!existe) {
                    Condor.creaArchTxt(dirArchTex, nomArchTxt, conteArch);
                    }
                else
                      System.err.println("creaDirArchTxt: Archivo existe");
                numArch++;
              } catch (ZeroByteFileException e) {
                  System.err.println("Tamaño de archivo en cero");
              }
              catch (TikaException e) {
                  System.err.println("Error en archivo ->" + nomArchTxt);
                  textLog = textLog + "Error por tipo de archivo ---> " + nomArch +"\n";
                  System.err.println("Error -> " + e.getMessage());
              }
          }
      }
      catch(IOException e) {
          System.err.println("Error -> " + e.getMessage());
          
      }
      catch (StringIndexOutOfBoundsException e) {
          System.out.println("Error en archivo ->" + nomArch);
          textLog = textLog + "Error por nombre de archivo ---> " + nomArch +"\n";
          System.err.println("Error -> " + e.getMessage());
      }

        long fin = System.currentTimeMillis();
        double tiempo = (double) ((fin - inicio)/1000);
        int min = (int) tiempo / 60;
        double seg = tiempo % 60;
        System.out.println(linea);
        System.out.println("TIEMPO EJECUCIÓN: " + min + " minutos " + seg + " segundos");
        System.out.println(linea);
        System.out.println("Número de archivos procesados = " + numArch);
        textLog = textLog + "\nTIEMPO EJECUCIÓN: " + min + " minutos " + seg + " segundos";
        textLog = textLog + "\nNúmero de archivos procesados = " + numArch;
        Condor.creaArchTxt(dirLog, fechaStr+".log", textLog);

    }    


/**
 * Devuelve el contenido de un archivo de texto plano en una cadena
 * @version: 2020/09/15
 * @param nomArch Nombre del archivo (con path)
 * @return Devuelve la cadena con el contenido del archivo
  */
    
    public static String loadArchTxt(String nomArch){
        String texto = "";
        try {
                FileReader fr = new FileReader(nomArch);
                BufferedReader entrada = new BufferedReader(fr);
                String s;
                while((s = entrada.readLine()) != null)
                     texto += s + "\n";
                entrada.close();
                return texto;
                }
                catch(java.io.FileNotFoundException fnfex) {
                    System.err.println("contArchTxt: Archivo no encontrado: " + fnfex);}
        catch(java.io.IOException ioex) {
                    System.err.println("No leyó archivo " + nomArch + ioex);
            }
        return "";
    }

/**
 * Devuelve el valor de un metadato de un archivo de texto
 * @version: 2020/09/15
 * @param nomArch Nombre del archivo (con path)
 * @param cadBusIni Cadena que contiene delimitador inicial y nombre del metadato
 * @param cadBusFin Cadena con el delimitador final del metadato
 * @param posMetIni Posición inicial dentro de la cadena desde dónde buscar. Se 
 * coloca 0 para buscar desde el principio
 * @param incluye La longitud del texto del metadato, si éste no se va a incluir
 * en la devolución. Se coloca 0 para incluirlo
* @return Devuelve valor del metadato
  */
    
    

public static String extrUnMetaArch( String nomArch,String cadBusIni,
                                    String cadBusFin, int posMetIni, int incluye)
{
    String texto,contMet;
    texto = Condor.loadArchTxt(nomArch);
    contMet = Condor.devMeta(texto, cadBusIni, cadBusFin, posMetIni, incluye);
    return contMet;
}

/**
 * Extrae las palabras de cada una de las consultas generadas a partir de 
 * colecciones en Mendeley, que corresponden al nombre de archivo bibtex de cada 
 * colección
 * @version: 2020/09/15
 * @param tipoEjec Tipo de ejecución: "test" "producción" 
 * @return Devuelve todas las consultas, separadas por comas
  */



public static String extrPalQryDirBib(String tipoEjec){
        String dir, nomArch;
        String archConf, consultas = "";
        Boolean pare = false;
        archConf = Condor.archCfg(tipoEjec);
        dir = Condor.extrUnMetaArch(archConf, "&dirBibliog  =", "&", 0, 14);
        if (pare) {
//            System.out.println("Dir --> " + dir);
        }
        int numQryProc = 0;
        try(DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(dir))) {
          for(Path ruta : ds ) {
            nomArch = ruta.getFileName().toString();
            int posPunto, posAmper = nomArch.indexOf("&");
              if (posAmper == -1) 
                  continue;

            posAmper++;
            posPunto = nomArch.indexOf(".", nomArch.length()-5);
            String investigador = devMeta(nomArch," - ","-&",0,3);
            String wordsQuery = nomArch.substring(posAmper, posPunto);
            String tema = devMeta(nomArch,"$"," -",0,1);
            consultas = consultas + investigador + " - " + tema + " - " + wordsQuery + "\n";
            numQryProc++;
              }
          }
      
        catch(IOException e) {
          System.err.println("Error -> " + e.getMessage());
          
      }
        System.out.println("\n\nConsultas procesadas = " + numQryProc + "\n\n");
        return consultas;        
    
}
/**
 * Extrae las palabras de cada una de las consultas generadas a partir de 
 * colecciones en Mendeley, que corresponden al nombre de archivo bibtex de cada 
 * colección
 * @version: 2020/10/10
 * @param tipoEjec Tipo de ejecución: "test" "producción" 
 * @return Genera la tabla Query en la BD dataset
  */



public static int genTabQueryDataset(String tipoEjec){
        String linea = new String(new char[60]).replace("\0", "_");
        QueryJDBC queryJDBC = new QueryJDBC();
        Query query = new Query();
        Date fecha = new Date();
        
        String dirBib, nomArch;
        String archConf, consultas = "";
        
        Boolean pare = false;
        archConf = Condor.archCfg(tipoEjec);
        dirBib = Condor.extrUnMetaArch(archConf, "&dirBibliog  =", "&", 0, 14);
        if (pare) {
            System.out.println("Lo paré");
            return -1;
        }
        int numQryProc = 0;
        try(DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(dirBib))) {
          for(Path ruta : ds ) {
            nomArch = ruta.getFileName().toString();
            int posPunto, posAmper = nomArch.indexOf("&");
              if (posAmper == -1) 
                  continue;

            posAmper++;
            posPunto = nomArch.indexOf(".", nomArch.length()-5);
            String investigador = devMeta(nomArch," - ","-&",0,3);
            String wordsQuery = nomArch.substring(posAmper, posPunto);
            wordsQuery = wordsQuery.toLowerCase();
            String topic = devMeta(nomArch,"$"," -",0,1);
            query.setCodQuery(wordsQuery.hashCode());
            query.setLengQuery("en");
            query.setCadQuery(wordsQuery);
            query.setInvestigador(investigador);
            query.setTopicQuery(topic);
            query.setFecCrea(fecha.toString());
            query.setFecModi("");
            int nroIns = queryJDBC.insert(query);
            System.out.println("registros insertados --> " + nroIns);
// ___________________ Agrega los queryPapers en la tabla ______________________
            String contArchBib, codPaper;
            Tika tika = new Tika();
            tika.setMaxStringLength(1000000);
            File fDir = new File(dirBib);
            File archBib = new File(fDir, nomArch);
              try {
                  contArchBib = tika.parseToString(archBib);
              } catch (TikaException ex) {
                  ex.printStackTrace(System.out);
                  continue;
              }
            int posMetaIni, posMetaFin = 0, numPapers = 0;
            String contBibPaper;
//            Paper regPaper;
            contBibPaper = Condor.devMeta(contArchBib, "\n@", "}\n@", 0, 0);
//              System.out.println("p a p e r  B i b");
            posMetaIni = contArchBib.indexOf("\n@");
            
// procesa los metadatos de cada uno de los artículos señalados en un archivo bibtex
            for (; posMetaFin != -1;) {
                codPaper = Condor.devMeta(contBibPaper,"{",",\n", 0, 1);
//                paperJDBC.insert(regPaper);
//                Condor.escMetaPaper(RegPaper);
//                System.out.println("FIN DEL ARTÍCULO" + linea);   
                posMetaFin = contArchBib.indexOf("\n@", posMetaIni + 1);
                posMetaIni = posMetaFin;
                numPapers++;
//______________________________________________________________________________                
                QueryPaperJDBC queryPaperJDBC = new QueryPaperJDBC();
                QueryPaper queryPaper =  new QueryPaper();

                queryPaper.setCodQuery(wordsQuery.hashCode());
                queryPaper.setCodPaper(codPaper);
                queryPaper.setFecCrea(fecha.toString());
        //        queryPaper.setFecModi("");

                int nroReg = queryPaperJDBC.insert(queryPaper);
                System.out.println("registros insertados --> " + nroReg);                
                
//______________________________________________________________________________                                
                if (posMetaFin != -1) {
                    contBibPaper = Condor.devMeta(contArchBib, "\n@", "}\n@", posMetaIni, 0);        
                }
            }
            System.out.println(linea);
            System.out.printf("Número de papers pertinentes para consulta %s es %d\n\n\n",
                                            wordsQuery, numPapers);
            System.out.println(linea);            
//______________________________________________________________________________
//            consultas = consultas + investigador + " - " + topic + " - " + wordsQuery + "\n";
            numQryProc++;
              }
          }
      
        catch(IOException e) {
          System.err.println("Error -> " + e.getMessage());
          
      }
        System.out.println("\n\nConsultas procesadas = " + numQryProc + "\n\n");
        return numQryProc;        
    
}

/**
 * Reemplaza algunos caracteres especiales LaTeX por el caracter tipográfico 
 * correspondiente
 * @version: 2020/09/16
 * @param kad Cadena original que contiene caracteres especiales LaTeX
 * @return Devuelve la cadena con los caracteres LaTeX reemplazados.
  */

/*
public static String reemCarLaTeX(String kad){
        String cadena=kad, cad1 = "", cad2 = "", subS1, subS2;
        String [] car = {"{\'a}", "{\'e}", "{\'i}", "{\'o}", "{\'u}", "{\'A}",
            "{\'E}", "{\'I}", "{\'O}", "{\'U}",  "{\"u}", "{\"U}", "{\textless}",
            "{\textgreater}", "{\'{a}}", "{\'{e}}","{\'{i}}", "{\'{o}}", "{\'{ú}}",
            "{\'{o}", "{\"{o}", "{\b{o}", "{\r{a}" };
        String [] carRee = {"á", "é", "í", "ó", "ú", "Á", "É", "Í", "Ó", "Ú",
            "ü", "Ü", "<", ">", "á", "é", "í", "ó", "ú", "ó", "ö", "o", "å"};
//        String cad = "{\"{u}}";
        try {
            for (int i = 0; i < car.length; i++) {
                cad1 = car[i];
                cad2 = carRee[i];
                cadena =  cadena.replace(cad1, cad2);
            }
              }
        catch (Exception e) {
                System.out.println("Error en --> " + cad1 + " <-> " + cad2);
              }
        return cadena;
}
*/

/**
 * Escribe los valores de los metadatos de un artículo que está en formato
 * bibtex
 * @version: 2020/09/16
 * @param paper Cadena con los metadatos en formato bibtex de un artículo
   */

/* public static void escMetaPaper(String paper) {
    String tipPaper,codPaper, titPaper, yearPaper, pathPaper;
    String pathFilePaper, abstPaper, keywPaper;
//    String [] vecNomArch, vecNomArchSinDir;
//    vecNomArch = Condor.vecNomArch(paper);
    System.out.println("\n\n__________________\nARTICULO\n__________________\n\n" + paper);
    tipPaper = Condor.devMeta(paper,"@", "{", 0, 1);
    System.out.println("Tipo de paper = " + tipPaper);
    codPaper = Condor.devMeta(paper,"{",",", 0, 1);
    System.out.println("Código Paper = " + codPaper);
    titPaper = Condor.devMeta(paper,"title = {{","}}", 0, 10);
    System.out.println(" Título Paper\n" + titPaper);
    System.out.println("*Título Paper\n" + Condor.reemCarLaTeX(titPaper));
    yearPaper = Condor.devMeta(paper,"year = {","}", 0, 8);
    System.out.println("Año del Paper= " + yearPaper);
    String autorPaper = Condor.reemCarLaTeX(Condor.devMeta(paper,"author = {","},", 0, 10));
    System.out.println(" Autores del Paper= " + autorPaper);
    String kad = Condor.reemCarLaTeX(autorPaper);
    String kadena = kad;
//    System.out.println("*Autores del Paper= " + kadena);
        String cadena = "Holgu{\'{i}}n-Veras, Jos{\'{e}} and Jaller, Miguel and Wachtendorf, Tricia";
        System.out.println(Condor.reemCarLaTeX(cadena));
    pathFilePaper = Condor.devMeta(paper,"file = {:",":pdf},", 0, 9);
    System.out.println(" Path del archivo del Paper= " + pathFilePaper);
    System.out.println("*Path del archivo del Paper= " + Condor.reemCarLaTeX(pathFilePaper));
    
    abstPaper = Condor.devMeta(paper,"abstract = {","},", 0, 12);
    System.out.println(" Abstract\n" + abstPaper);
    System.out.println("*Abstract\n" + Condor.reemCarLaTeX(abstPaper));
    keywPaper = Condor.devMeta(paper,"keywords = {","},", 0, 12);
    System.out.println("Keywords = " + keywPaper);
    System.out.println("*Keywords = " + Condor.reemCarLaTeX(keywPaper));
    pathPaper = "/" + Condor.devMeta(paper,"file = {:",":pdf", 0, 9);
//    System.out.println("Path paper = " + pathPaper);

    
} */

/**
 * Escribe los valores de los metadatos de cada uno de artículos (en formato
 * bibtex), extraídos de un archivo de un tema-investigador
 * @throws java.io.IOException Error de escritura del paper
 * @version: 2020/09/16
 * @param content Cadena que contiene los metadatos de todos artículos de un
 * tema-investigador
 * @param tipoEjec Tipo de ejecución: "test" "producción"
 * @param creeArch Opción de crear o no, los archivos
   */


public static void escMetaArchBib(String content, String tipoEjec, Boolean creeArch)
                                throws IOException{
    int posMetIni, posMetFin = 0, numPapers = 0;
    String contPaperBib;
    Paper RegPaper;
    contPaperBib = Condor.devMeta(content, "\n@", "}\n@", 0, 0);
    posMetIni = content.indexOf("\n@");
    
    for (; posMetFin != -1;) {
//        System.out.println("______________________________________________________");
//        System.out.println(contPaperBib);
//        System.out.println("______________________________________________________");
        RegPaper = Condor.setMetaPaper(contPaperBib, tipoEjec,creeArch);
        Condor.escMetaPaper(RegPaper);
//        Condor.escMetPaper(contPaperBib);
        posMetFin = content.indexOf("\n@", posMetIni + 1);
        posMetIni = posMetFin;
        numPapers++;
        if (posMetFin != -1) {
            contPaperBib = Condor.devMeta(content, "\n@", "}\n@", posMetIni, 0);        
        }
    }
    System.out.println("Número de artículos procesados en este tema --->  " + numPapers);
 
               
}

/**
 * Devuelve un vector de con los años de cada paper, partiendo del nombre del
 * archivo del paper que tiene el formaoto Autor - Año - Título
 * @version: 2020/09/17
 * @param nomArchPaper Vector con todos los nombres de archivo del repositorio
 * @return Devuelve el vector de años de los papers
   */ 


public static int [] vecYearPaper(String [] nomArchPaper) {
    int [] YearPaper;
    int posYear, year, i = 0;
    String syear;
    YearPaper = new int[nomArchPaper.length];
    for(String nomArch : nomArchPaper) {
        posYear = nomArch.indexOf("20");
        if (posYear == -1) {
            posYear = nomArch.indexOf("19");
            if (posYear == -1) 
                syear = "0";
            else
                syear = nomArch.substring(posYear, posYear + 4);
            }    
        else
            syear = nomArch.substring(posYear, posYear + 4);

        year = Integer.valueOf(syear);
        YearPaper[i] = year;
        i++;
    }
    return YearPaper;
}

/**
 * Devuelve una subcadena, extraída a partir de la última aparición del caracter
 * señalado. Si no encuentra el caracter, devuelve toda la cadena original
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/17
 * @param cad Cadena original desde donde se extrae la subcadena
 * @param car caracter que determina la extracción
 * @return Subcadena extraida desde la última aparición del caracter
   */ 

public static String extrText(String cad, String car) {
    int posIni, posAnt = 0;
    String kad;
    posIni = cad.indexOf(car);
    for ( ; posIni != -1; ) {
        posAnt = posIni;
        posIni = cad.indexOf(car, posAnt + 1);
    }
    if (posAnt == 0)
        posAnt = -1;
    kad = cad.substring(posAnt + 1, cad.length());
    return kad;
}

/**
 * Devuelve el path del archivo de configuración del paquete del dataset, 
 * dependiendo si se ejecuta en modalidad de test o producción
 * señalado.
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/21
 * @param tipoEjec Tipo de ejecución: "test" o "producción"
  * @return Path del archivo de configuración
   */ 

public static String archCfg(String tipoEjec) {
    String archConfig;
    String dirConfig;
    dirConfig = DIR_CONFIG;
    if (tipoEjec.equalsIgnoreCase("test")) 
        archConfig= dirConfig + "config-CondorIR-test.cfg";
    else
        archConfig= dirConfig + "config-CondorIR.cfg";
    return archConfig;
    
}

/**
 * Setea un registro de la clase Paper, a partir de los metadatos extraidos de
 * una texto bibtex
     
 * @throws java.io.IOException Error de escritura del paper
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/21
 * @param contBibPaper Contenido del texto bibtex con los metadatos del paper
 * @param tipoEjec Tipo de ejecución: "test" "producción"
 * @param creeArch Opción de crear o no archivos
 * @return Un registro de la clase Paper
 */ 


public static Paper setMetaPaper(String contBibPaper, String tipoEjec, Boolean creeArch)
                                throws IOException {

    PaperJDBC paperJDBC = new PaperJDBC();

    String tipoPaper, codPaper, titPaper, yearPaper, autorPaper,  pathPaper;
    String abstPaper, keywPaper, fecCreaPaper, fecModPaper;
    Paper regPaper = new Paper();
    Date datePaper = new Date(); 
    String nomArchRep;
//    System.out.println("C O N T E N I D O  T E X  P A P E R\n" + contBibPaper);
    int year, archTxtNoCrea;
    archTxtNoCrea = 0;
    codPaper = Condor.devMeta(contBibPaper,"{",",\n", 0, 1);
    regPaper.setCodPaper(codPaper);

    tipoPaper = Condor.devMeta(contBibPaper,"@", "{", 0, 1);
    regPaper.setTipoPaper(tipoPaper);

    titPaper = Condor.devMeta(contBibPaper,"title = {{","}},\n", 0, 10);
    titPaper = Condor.cadSinCarTex(titPaper);
    titPaper = Condor.ultCleanCarTex(titPaper);

    regPaper.setTitPaper(titPaper);

    yearPaper = Condor.devMeta(contBibPaper,"year = {","}\n", 0, 8);
    if ("".equalsIgnoreCase(yearPaper) ) {
        yearPaper = "0";
    }
    regPaper.setYearPaper(Integer.parseInt(yearPaper));
    if (contBibPaper.contains("author = {{") ) 
        autorPaper = Condor.devMeta(contBibPaper,"author = {{","},\n", 0, 11);
    else
        autorPaper = Condor.devMeta(contBibPaper,"author = {","},\n", 0, 10);
    
//    System.out.println("autorPaper ---> " + autorPaper);
    autorPaper = Condor.cadSinCarTex(autorPaper);
    autorPaper = Condor.ultCleanCarTex(autorPaper);
    regPaper.setAutorPaper(autorPaper);

    abstPaper = Condor.devMeta(contBibPaper,"abstract = {","},\n", 0, 12);
    abstPaper = Condor.cadSinCarTex(abstPaper);
    abstPaper = Condor.ultCleanCarTex(abstPaper);    
    regPaper.setAbstPaper(abstPaper);
  
    keywPaper = Condor.devMeta(contBibPaper,"keywords = {","},\n", 0, 12);
    keywPaper = Condor.cadSinCarTex(keywPaper);
    keywPaper = Condor.ultCleanCarTex(keywPaper);
    keywPaper = Condor.limpKeyw(keywPaper);
    regPaper.setKeywPaper(keywPaper);
    
    pathPaper = Condor.devMeta(contBibPaper,"file = {:",":", 0, 9);
    pathPaper = Condor.cadSinCarTex(pathPaper);
    pathPaper = Condor.ultCleanCarTex(pathPaper);    
    regPaper.setPathPaper(pathPaper);

    fecCreaPaper = datePaper.toString();
    regPaper.setFecCrea(fecCreaPaper);

    fecModPaper = "";
    regPaper.setFecModi(fecModPaper);
//______________________________________________________________________________
    Tika tika = new Tika();
    String texto;
    String linea = new String(new char[60]).replace("\0", "_") + "\n";
    String [] vNomArch;
    String nomArchTxt = "";
    int [] vYearPaper;
    int pos, fall = 0;
    String dirPdf, dirTxt, nomArch, artic, contArchBib, contWords="";
    String archConfig, leng, contPdf, subs, dirDataPdf;
    archConfig = Condor.archCfg(tipoEjec);
    texto = Condor.loadArchTxt(archConfig);
    dirPdf = Condor.devMeta(texto, "&dirReposPdf =", "&", 0, 14);
    dirDataPdf = Condor.devMeta(texto, "&dirDataPdf  =", "&", 0, 14);
    dirTxt = Condor.devMeta(texto, "&dirDataTxt  =", "&", 0, 14);
    vNomArch = Condor.vecNomArchSinDir(dirPdf);
    vYearPaper = Condor.vecYearPaper(vNomArch);   
//______________________________________________________________________________
    nomArchRep = regPaper.getPathPaper();
//    System.out.println("Nombre archivo antes --> " + nomArchRep );
    nomArchRep = Condor.extrText(nomArchRep, "/");
    if (nomArchRep.length() == 0) {
//            System.out.println("Texto bib " + texto);
            System.err.println("Pasó por aquí 1 --> " + codPaper);
    }

    year = regPaper.getYearPaper();
// Devuelve la posición del nombre del archivo (en el repositorio) con mayor
//          similitud al del metadato
    pos = posNomArch(vYearPaper, vNomArch, year, nomArchRep);
    if (pos == -1) {
        System.out.println("setMetaPaper: Archivo no encontrado " + nomArchRep);
        fall++;
    }
    else {
// Copio el archivo PDF en la carpeta final del repositorio del dataset
        String nomArchPdf;
        nomArchPdf = vNomArch[pos];
        regPaper.setPathPaper(dirDataPdf + nomArchPdf);
        File archPdf = new File(dirPdf+nomArchPdf);

        try 
        {
            contPdf = tika.parseToString(archPdf);
            if (contPdf.length() > 7000) 
                 {
                 subs = contPdf.substring(0, 7000);
                 leng = Condor.detecLeng(subs);
                 }
            else {
                 leng = Condor.detecLeng(contPdf);
                 }
            if ("".equals(leng)) 
                {
                    leng = Condor.detecLeng(regPaper.getTitPaper());            
                    System.out.println(leng + " <--- Lenguaje desde el titulo -->" + regPaper.getTitPaper());
                }
            regPaper.setLengPaper(leng);
// en este punto se tiene todo el registro _____________________________________
// sigue la creación de archivos________________________________________________
            
            int nReg = paperJDBC.insert(regPaper);
            if (nReg == -1) {
                System.out.printf("Metadato de %s no grabado en la BD ni se generaron los archivos txt y pdf", regPaper.getPathPaper());
            }
            else {
// ______________________ Genera el archivo de texto ___________________________            
                    int posPunto = nomArchPdf.indexOf(".", nomArchPdf.length()-5);
                    if (posPunto == -1) {
                        System.err.println("Nombre de archivo sin extensión ->" + nomArchPdf);
                      }
                    else {
                        nomArchTxt = nomArchPdf.substring(0, posPunto) + ".txt";
                    }
                    File f = new File(dirTxt, nomArchTxt); 
                    Boolean existe = f.exists();
                    if (creeArch) 
                        if  (!existe) {
                            Condor.creaArchTxt(dirTxt, nomArchTxt, contPdf);
                            }
                        else
                            System.err.println("setMetaPaper: Archivo existe " + dirTxt + nomArchTxt);
// ______________________ copia el archivo pdf 
                    if (creeArch) 
                        {
                            if (!Condor.copyFile(archPdf, dirDataPdf + nomArchPdf)) 
                                {
                                System.out.println("setMetaPaper: Arhivo no copiado " + nomArchPdf);
                                }
                        }        

            
            }
        }           // Fin del try _____________________________________________    
        catch (TikaException ex) {
            System.out.println("Tika: Error de entrada-salida en archivo --> " + archPdf);
            System.out.println("Archivo de texto no generado");
            archTxtNoCrea++;
            Logger.getLogger(Condor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            System.out.println("Error de entrada-salida en archivo --> " + archPdf);
        }

        } 

    return regPaper;
    
}

/**
 * Escribe un registro de la clase Paper
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/21
 * @param RegPaper Registro de la clase Paper
   */ 

public static void escMetaPaper(Paper RegPaper) {
    

    System.out.println("\n\n________________\nARTICULO\n________________\n\n");
   
    System.out.println("Tipo de paper = " + RegPaper.getTipoPaper());
    System.out.println("Código Paper = " + RegPaper.getCodPaper());
    System.out.println(" Título Paper\n" + RegPaper.getTitPaper());
    System.out.println("Año del Paper= " + RegPaper.getYearPaper());
    System.out.println(" Autores del Paper= " + RegPaper.getAutorPaper());
    System.out.println("Lenguaje del Paper --> " + RegPaper.getLengPaper());
    System.out.println(" Path del archivo del Paper= " + RegPaper.getPathPaper());
    System.out.println(" Abstract\n" + RegPaper.getAbstPaper());
    System.out.println("Keywords = " + RegPaper.getKeywPaper());
    System.out.println("Fecha de creación = " + RegPaper.getFecCrea());
    System.out.println("Fecha de modificación = " + RegPaper.getFecModi());
    

}

/**
 * Detecta el idioma del texto
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/21
 * @param texto Contenido a detectar el idioma
 * @return El código del idioma correspondiente (por ejemplo, es, en, pt, ...)
 */ 

public static String detecLeng(String texto) {
        LanguageDetector detector = new OptimaizeLangDetector().loadModels();
        LanguageResult result = detector.detect(texto);
        String leng;
        leng = result.getLanguage();
        if (leng == null) 
            return "";
        else
            return leng;
  }

/**
 * Reemplaza los caracteres especiales LaTeX de un texto por el correspondiente
 * caracter
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/22
 * @param cad Cadena original
 * @param vCodHash Vector de códigos Hash de códigos LaTeX de caracteres
 * especiales
 * @param vCarEq Vector de caracteres especiales para cada código Hash
  * @return La cadena con los caracteres especiales LaTeX reemplazados
 */ 


public static String cadSinCarTex(String cad, int [] vCodHash,String [] vCarEq)   {
    String subs, carEq, kad;
    int codHash;
    int posMetFin, posMetIni;
    int k = 0;
    kad = cad;
    while (true) {
        posMetIni = cad.indexOf("{");
        if (posMetIni == -1) 
        {
            return cad;
        }
        posMetFin = cad.indexOf("}}");
        if (posMetFin == -1) {
            posMetFin = cad.indexOf("}");
                if (posMetFin == -1) 
                   return cad;
                else
                   subs = cad.substring(posMetIni + 1, posMetFin); 
            }
        else   
        {
            subs = cad.substring(posMetIni + 1, posMetFin + 1);
        }

        codHash = subs.hashCode();
        Boolean encontro=false;
        carEq = "";
        for (int i = 0; i < vCodHash.length; i++) {
            if (codHash == vCodHash[i]) {
                carEq= vCarEq[i];
                encontro=true;
                break;
            }
        }

        if (encontro) {
            kad = cad.substring(0,posMetIni) + carEq + cad.substring(posMetIni + subs.length() + 2);
            cad = kad;
        }
        k++;
        if (!cad.contains("{") || k > 5)
            break;
        }
    return kad;
}

/**
 * Reemplaza los caracteres especiales LaTeX de un texto por el correspondiente
 * caracter
 * @throws java.io.FileNotFoundException Error de lectura del archivo
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/22
 * @param cad Cadena original
  * @return La cadena con los caracteres especiales LaTeX reemplazados 
 */ 


public static String cadSinCarTex(String cad) throws FileNotFoundException, IOException   {
    //__________________Carga del archivo con los caracteres especiales de LaTeX
        String nomArchNoEnc;
        nomArchNoEnc = ARCH_NO_ENC;

        BufferedWriter bw = new BufferedWriter(new FileWriter(nomArchNoEnc, true));
        PrintWriter salida;
        salida = new PrintWriter(bw);     
// ______________________________    
        String [] vCad,vCadTex;
        String cadTex="", cadCarEq = "";        
        String [] vCarEq;
        int [] vCodHash;
        String archCodTex = ARCH_COD_LATEX;
        FileReader archCarTex = new FileReader(archCodTex);
        BufferedReader entrada;
        entrada = new BufferedReader(archCarTex);
        String s;
        while((s = entrada.readLine()) != null)
        {
            try {
//                System.out.println(s);
                if (!"".equals(s)) {
                vCad = s.split(";");
//                System.out.println("cadSinCarTex: 4");
                cadTex += vCad[0] + ";";
//                System.out.println("cadSinCarTex: 5");                
                cadCarEq += vCad[1] + ";";
//                System.out.println("cadSinCarTex: 6");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("cad --> " + s + " Error de límites del vector en cadena --> " + e);
            }
            
        }
//        System.out.println("cadSinCarTex: 7");
        entrada.close();
        vCarEq = cadCarEq.split(";");
        vCadTex = cadTex.split(";");
        vCodHash = new int[vCadTex.length];
        for (int i = 0; i < vCadTex.length; i++) {
            vCodHash[i] = vCadTex[i].hashCode();
        }    
    
    //_______________________________________________________________________
    
    String subs, carEq, kad;
    Boolean encontro2;
    int codHash;
    int posMetFin, posMetIni;
    int k = 0;
    kad = cad;
    while (true) {
        k++;
        posMetIni = cad.indexOf("{");
        if (posMetIni == -1) 
        {
//            System.out.println("1 -> " + cad);
            return cad;
        }
        posMetFin = cad.indexOf("}}");
        if (posMetFin == -1) {
            posMetFin = cad.indexOf("}");
                if (posMetFin == -1) {
//                    System.out.println("2 -> " + cad);
                    return cad;
                }
                   
                else {

//                    System.out.println("cadSinCarTex 3 -> " + posMetIni + " y "
//                                    + posMetFin + " " + k + " veces");
                    if (posMetIni + 2 > posMetFin) {
                        subs = cad;
                    }
                    else
                    subs = cad.substring(posMetIni + 2, posMetFin);                     
                }
                    
                   
            }
        else   
        {
//            System.out.println("Pasó por aquí 13 --->" + posMetIni + " y " + posMetFin 
//                               + " " + k + "ciclos");
            subs = cad.substring(posMetIni + 2, posMetFin + 1);
//            System.out.println(cad);
//            System.out.println("4 -> " + subs);
        }

        codHash = subs.hashCode();
        Boolean encontro=false;
        carEq = "";
        for (int i = 0; i < vCodHash.length; i++) {
            if (codHash == vCodHash[i]) {
                carEq= vCarEq[i];
                encontro=true;
                break;
            }
        }

        if (encontro) {
            kad = cad.substring(0,posMetIni) + carEq + cad.substring(posMetIni + subs.length() + 3);
            // System.out.println("5 -> " + kad);
            cad = kad;

        }
        else
            salida.println("No  encontró ----> |" + subs + "|");
//        k++;
//        if (!cad.contains("{") || k > 50) {
        if (!cad.contains("{") || !encontro) {
//            System.out.println("k = " + k);
            break;
        } 
/*        else if (!encontro && k < 10 )
                {
                    k++;
                    posMetFin = cad.lastIndexOf("}}", cad.length());
                    if (posMetFin == -1) 
                        posMetFin = cad.lastIndexOf("}", cad.length());
                        if (posMetFin == -1) 
                           break;    
                        else
                            posMetIni = cad.lastIndexOf("{", posMetFin);
                            if (-1 == posMetIni) 
                                break;
                            else
                                encontro2 = false;
                                for (int i = 0; i < vCodHash.length; i++) 
                                {
                                    if (codHash == vCodHash[i]) {
                                        carEq= vCarEq[i];
                                        encontro2=true;
                                        break;
                                    }
                                }
                                if (encontro2) 
                                {
                                  kad = cad.substring(0,posMetIni) + carEq 
                                        + cad.substring(posMetIni + subs.length() + 3);
                                   // System.out.println("5 -> " + kad);
                                  cad = kad; 
                                }
                    }
                else
                    break; */
        } // fin del while
    salida.close();
    return kad;
} 

/**
 * Elimina de las keywords la palabras clave de longitud 1
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/23
 * @param keyw Palabras clave separadas por comas
  * @return Keywords sin palabras de longitud 1.
 */ 



public static String limpKeyw(String keyw){
    String [] vKeyw;
    String newKeyw="";
    vKeyw = keyw.split(",");
    int i=0;
    for(String palabra : vKeyw){
        if (!(palabra.length()<=1)) {
            if (i==0) {
                newKeyw += palabra;
            }
            else
                newKeyw += "," + palabra;
            i++;
        }
    }
    return newKeyw;
}

/**
 * Devuelve la posición del nombre del archivo en la carpeta que tiene mayor
 * similitud con el nombre de archivo extraido del metadato
  * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/23
 * @param vYear Vector con los años extraidos de los nombre de los archivos en
 * la carpeta
 * @param vNomArch Vector con los nombres de los archivos en la carpeta
 * @param yearPaper Año del paper en el metadato
 * @param nomArchBib Nombre del archivo en el metadato
  
 * @return Nombre del archivo de la carpeta con mayor similitud al del metadato
   */ 


public static int posNomArch(int [] vYear, String [] vNomArch, int yearPaper, String nomArchBib) {
    int posNom = 0;
    double simNom, simMay=0;
    for (int i = 0; i < vYear.length; i++) {
        if (yearPaper ==  vYear[i]) {
            simNom = Condor.simDosCad(nomArchBib, vNomArch[i]);
            if (simNom > simMay) {
                simMay = simNom;
                posNom = i;
            }
        }
    }
    if (simMay < 0.5) {
        System.err.println("Similitud ---> " + simMay);
        return -1;
    }
    else 
        return posNom;
}

/**
 * Hace copia de un archivo
   * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/24
 * @param origen Path del archivo fuente
 * @param destino Path del archivo destino
 * @return Retorna si la copia fue exitosa o no
   */ 

public static boolean copyFile(String origen, String destino) {
        File origin = new File(origen);
        File destination = new File(destino);
            try {
//                System.out.println("1. Pasó por aquí");
                InputStream in = new FileInputStream(origin);
//                System.out.println("2. Pasó por aquí");                
                OutputStream out = new FileOutputStream(destination);
//                System.out.println("3. Pasó por aquí");                
                // We use a buffer for the copy (Usamos un buffer para la copia).
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                return true;
            } catch (IOException ioe) {
                System.out.println("copyFile 1: Archivo no copiado --> " + origen);
                return false;
            }
}

/**
 * Hace copia de un archivo
   * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/24
 * @param origen Archivo fuente (tipo File)
 * @param destino Path del archivo destino
 * @return Retorna si la copia fue exitosa o no
   */ 

public static boolean copyFile(File origen, String destino) {
        File destination = new File(destino);
            try {
//                System.out.println("1. Pasó por aquí");
                InputStream in = new FileInputStream(origen);
//                System.out.println("2. Pasó por aquí");                
                OutputStream out = new FileOutputStream(destination);
//                System.out.println("3. Pasó por aquí");                
                // We use a buffer for the copy (Usamos un buffer para la copia).
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                return true;
            } catch (IOException ioe) {
                System.err.println("copyFile 2: Archivo no copiado a --> " + destino);
                return false;
            }
}

/**
 * Limpia de códigos de caracter LaTeX a una cadena
   * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/09/30
 * @param cad Cadena a limpiar códigos de caracteres LaTeX
 * @return Retorna la cadena sin códigos de caracter LaTeX
   */ 

public static String ultCleanCarTex(String cad) {
    cad = cad.replace("{\\'{a}", "á");
    cad = cad.replace("{\\'{e}", "é");
    cad = cad.replace("{\\'{i}", "í");
    cad = cad.replace("{\\'{o}", "ó");
    cad = cad.replace("{\\'{ú}", "ú");
    cad = cad.replace("{\\~{a}", "ã");
    cad = cad.replace("{\\~{n}", "ñ");    
    cad = cad.replace("{\\c{c}", "ç");    
    cad = cad.replace("\\v{z}", "ž");    
    cad = cad.replace("\\v{Z}", "Ž");
    cad = cad.replace("\\'{A}", "Á");
    cad = cad.replace("\\'A", "Á");
    cad = cad.replace("\\n","\n");
    cad = cad.replace("\\v{c}","č");

    cad = cad.replace("\\\"{a}","ä");
    cad = cad.replace("\\\"{e}","ë");
    cad = cad.replace("\\\"{i}","ï");
    cad = cad.replace("\\\"{o}","ö");
    cad = cad.replace("\\\"{u}","ü");

    cad = cad.replace("\\^{e}","ê");
    cad = cad.replace("\\\"{O}","Ö");    
   
    cad = cad.replace("\\v{z}","ž");
    cad = cad.replace("\\ae","æ");
    cad = cad.replace("\\aa","å");
    cad = cad.replace("\\o","ø");
    cad = cad.replace("\\O","Ø");
    cad = cad.replace("\\ss","ß");
    cad = cad.replace("\\l","ł");
    cad = cad.replace("$\\epsilon$","ϵ");
    cad = cad.replace("$\\sigma$","σ");
    
    cad = cad.replace("$\\mu$","μ");
    cad = cad.replace("$\\sim$","~");
    cad = cad.replace("$\\theta$","θ");
    cad = cad.replace("$\\alpha$","α");

    cad = cad.replace("$\\beta$","β");
    cad = cad.replace("$\\omega$","ω");
    cad = cad.replace("$\\Omega$","Ω");
    cad = cad.replace("{", "");
    cad = cad.replace("}", "");
    return cad;            
}

/**
 * Escribe el tiempo de ejecución de un proceso
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/23
 * @param inicio Tiempo de inicio del proceso calculado con System.currentTimeMillis();
    */ 

public static void tiempoEjec(long inicio){
    long fin = System.currentTimeMillis();
    long segundos =(fin - inicio)/1000;
    long mili = (fin - inicio) - segundos * 1000;
    long min = (segundos / 60);
    long seg; 
    seg = (segundos - min*60);
    System.out.println("\n\nTiempo transcurrido: " + tiempoTransc(inicio));

}

/**
 * Lapso transcurrido entre dos momentos
 * @return Tiempo de ejecución entre un momento dado y el actual
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/31
 * @param inicio Tiempo de inicial de referencia System.currentTimeMillis();
    */ 

public static String tiempoTransc(long inicio){
    String cad;
    long fin = System.currentTimeMillis();
    long segundos =(fin - inicio)/1000;
    long mili = (fin - inicio) - segundos * 1000;
    long min = (segundos / 60);
    long seg; 
    seg = (segundos - min*60);
    cad = min + " minutos " + seg + " segundos " + mili + " milisegundos";
    return cad;
}

/**
 * Escribe en un log en disco  el tiempo de ejecución de un proceso y otra información
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/26
 * @param inicio Tiempo de inicio del proceso calculado con System.currentTimeMillis();
 * @param proceso Nombre del proceso
 * @param resultado Comentarios sobre el proceso
 * @param dir Path de la carpeta del log
    */ 

public static void tiempoEjec(long inicio, String proceso, String resultado, String dir){
    String fechaEd = LocalDate.now().toString();
    String pathArch = dir + "Log de " + fechaEd + ".log";
    String hora = LocalTime.now().toString();
    String linea = new String(new char[90]).replace("\0", "_") + "\n";        

        try {
            FileWriter fw;            
            fw = new FileWriter(pathArch, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(bw);

            salida.println(linea);
            salida.println(proceso);
            salida.println(linea);
            salida.println("Hora de finalización del proceso: " + hora);
            salida.println(resultado + "\n");
            salida.println("\nTIEMPO DE EJECUCIÓN: " + tiempoTransc(inicio));
            salida.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
    }

/**
 * Escribe una matriz en disco como flujo de bits
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/26
 * @param pathArch Path del archivo donde se escribe la matriz
 * @param M Matriz
    */ 
public static void genArchDesdeMatriz(String pathArch, double [][] M){
    try {
        FileOutputStream fileOut=new FileOutputStream(pathArch);
        DataOutputStream salida = new DataOutputStream(fileOut);
        int nFil = M.length;
        int nCol = M[0].length;
        salida.writeInt(nFil);
        salida.writeInt(nCol);
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                salida.writeDouble(M[i][j]);
                }
            }
        salida.close();
        }
    catch (java.io.IOException ex) {
        ex.printStackTrace();
    }
}        
        

/**
 * Agrega palabras estimizadas únicas al diccionario desde una cadena de caracteres,
 * en un idioma específico eliminando las palabras vacías
 * @author: Germán A. Osorio-Zuluaga
 * @version: 2020/10/23
 * @param wordBag Diccionario de palabras
 * @param contArch la cadena de donde se extraen las palabras
 * @param stopwords lista de palabras vacías en el idioma correspondiente
 * @param leng Idioma de la cadena de caracteres
 * @param numDoc Número del paper que se está procesando
 * @param MatrizTermDoc Matriz de frecuencias término-documento
 * 
 */ 

public static void genWordBagMatriz(HashMap<String, Integer> wordBag,
                                            String contArch, List<String> stopwords, 
                                            String leng,
                                            int numDoc,
                                            double [][] MatrizTermDoc
                                            )
{
    int indPal;
    StringReader reader = new StringReader(contArch);
    StandardAnalyzer stdAnz = new StandardAnalyzer();
    org.apache.lucene.analysis.TokenStream ts = null;
// Stemmer de tres idiomas            
    EnglishStemmer english = new EnglishStemmer();
    SpanishStemmer spanish = new SpanishStemmer();
    PortugueseStemmer portuguese = new PortugueseStemmer();

    try {
        ts = stdAnz.tokenStream("fulltext", reader);
        CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        while (ts.incrementToken()) {
            String token = termAtt.toString();
            String pal;
            if (Condor.esAlfab(token)) {
                if(!stopwords.contains(token)){
                    indPal = wordBag.size();
                 // ____________________________________________________
                 // Stemming en el idioma correspondiente
                    switch (leng) {
                        case "pt":
                            portuguese.setCurrent(token);
                            portuguese.stem();
                            pal = portuguese.getCurrent();
                            break;

                        case "en":
                            english.setCurrent(token);
                            english.stem();
                            pal = english.getCurrent();
                            break;

                        case "es":
                            spanish.setCurrent(token);
                            spanish.stem();
                            pal = spanish.getCurrent();
                            break;
                        default:
                            pal = "";
                            System.out.println("Error en lenguaje");
                            continue;
                    }
                    if (!wordBag.containsKey(pal)) {
                        wordBag.put(pal, indPal);
                        }
                    else
                        indPal = wordBag.get(pal);
                    try {
                        MatrizTermDoc[indPal][numDoc]+=1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Error tamaño matriz");
                        System.out.println("Filas: " + indPal);
                        System.out.println("Columnas: " + numDoc);
                        e.printStackTrace();
                    }
                    

                 // ____________________________________________________                            
                    }
            }

        }
    ts.end(); // token stream
    } catch (IOException e) {
    } finally {
        try {
            ts.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    stdAnz.close();  // cierra analyzer. En este caso, el standard
    }    
}

/**
* Carga los stopwords del idioma correspondiente
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/23
* @param leng Idioma del que se cargan las stopwords
* @return Lista de stopwords del idioma dado
*/ 

public static List<String> loadStopwords(String leng)  {
    List<String> stopwords = null;
    String arch;
    arch = "/home/gaoz/pcloudSync/dev/corpus/" + leng.toUpperCase() +"stopWordsNLTK.txt";
    try {
        stopwords = Files.readAllLines(Paths.get(arch));                    
    } catch (IOException ex) {
        ex.printStackTrace(System.err);
    }
    return stopwords;
    }

/**
* Crea archivo de texto con base a un diccionario HashMap
*   @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/24
* @param codNom Diccionario de código entero y cadena de caracteres
* @param pathArch Path del archivo a crear
*/ 

public static void genArchCodNom(HashMap<String, Integer> codNom,String pathArch){
    try {
        FileWriter salida = new FileWriter(pathArch);

        int tam = codNom.size();
        codNom.forEach((k,v) -> {
            try {
                salida.write(v + "," + k + "\n");
            } catch (IOException ex) {
            }
        });
        salida.close();
        }
    catch (IOException ex) {
    }    
    
}

/**
* Lee lista hashMap desde archivo de texto (cod, nom), separados por comas
* @return Colección hashMap de código - nombre
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/26
* @param pathArch Path del archivo desde donde se lee la lista
* 
*/ 

    public static HashMap<Integer, String> loadCodNomDesdeArch(String pathArch) {
        HashMap<Integer, String> lista = new HashMap<>();
        try {
            FileReader fileIn = new FileReader(pathArch);
            BufferedReader entrada = new BufferedReader(fileIn);
            String linea = entrada.readLine();
            while (linea != null) {
                String [] datos = linea.split(",");
                int cod = Integer.parseInt(datos[0]);
                String nom = datos[1];
                lista.put(cod, nom);
                linea = entrada.readLine();
            }
            entrada.close();
            }
        catch (IOException ex) {
            System.out.println(pathArch);
            System.out.println("Falló lectura de diccionario");
            ex.printStackTrace();
        }
        return lista;
    }
/**
* Lee lista hashMap desde archivo de texto (nom, cod), separados por comas.
* El archivo debe estar con el formato (cod, nom) separado por coma.
*  @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/26
* @param pathArch Path del archivo desde donde se lee la lista
* @return Lista con nombre (key) y valor numérico asociado
* 
*/ 

    public static HashMap<String, Integer> loadNomCodDesdeArch(String pathArch) {
        HashMap<String, Integer> lista = new HashMap<>();
        try {
            FileReader fileIn = new FileReader(pathArch);
            BufferedReader entrada = new BufferedReader(fileIn);
            String linea = entrada.readLine();
            while (linea != null) {
                String [] datos = linea.split(",");
                int cod = Integer.parseInt(datos[0]);
                String nom = datos[1];
                lista.put(nom, cod);
                linea = entrada.readLine();
            }
            entrada.close();
            }
        catch (IOException ex) {
            System.out.println("Falló lectura de archivo");
        }
        return lista;
    }

/**
* Escribe lista (código, nombre)
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/27
* @param lista Lista a escribir
* 
*/    
    
public static void escListaCodNom(HashMap<Integer, String> lista) {
    lista.forEach((k,n) -> System.out.println(k + " " + n));
}

/**
* Lee matriz desde archivo. Primero el tamaño con dos enteros (int)
* @throws java.io.IOException Error de lectura de archivo
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/27
* @param path Archivo desde donde se lee la matriz
* @return la matriz obtenida
*/    

public static double [][] loadMatrizDesdeArch(String path) throws IOException{
    
    double [][] Matriz=null;

    try {
        FileInputStream fileIn=new FileInputStream(path);
        DataInputStream entrada = new DataInputStream(fileIn);

        int nFil = entrada.readInt();
        int nCol = entrada.readInt();
        int factor = nFil / 10;
        if (factor == 0) {
            factor = 1;
        }
        Matriz = new double[nFil][nCol];
        for (int i = 0; i < nFil; i++) {
            if ((nFil % factor) == 0) {
  
                for (int j = 0; j < 3; j++) {
                    System.out.print("||||||||||-");
                }
            }
            for (int j = 0; j < nCol; j++) {
                Matriz[i][j] = entrada.readDouble();
                }
            }
        entrada.close();
        }
    catch (java.io.IOException ex) {
        System.out.println("No leyó archivo " + path);
    }
    return Matriz;
}

/**
* Genera matriz de frecuencia ponderada TF-IDF
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/28
* @param M Matriz término-documento de frecuencias
* @return la matriz término-documento ponderada TF-IDF
*/

public static double [][] genMatrizTFIDF(double [][] M) {
    int nPal = M.length;
    int nDoc = M[0].length;
    int factor = nPal / 10;
    final double LOG2 = Math.log(2);
    double tf, idf;
    double [][] mat = new double[nPal][nDoc];
    
    double [] nDocTienenPal = new double[nPal];
    int tp;
    for (int pal = 0; pal < nPal; pal++) {
        tp = 0;
        for (int doc = 0; doc < nDoc; doc++) {
            if (M[pal][doc] > 0) {
                tp++;
            }
        nDocTienenPal[pal] = tp;
        }
    }
    System.out.println("");
    for (int pal = 0; pal < nPal; pal++) {
        int mod = (pal + 1) % factor;
        if (mod == 0) {
            System.out.print("|||||||||-");
        }
        for (int doc = 0; doc < nDoc; doc++) {
            double v = M[pal][doc];
            if (v > 0) {
                tf = 1 + Math.log(v)/LOG2;
                idf = Math.log(nDoc/nDocTienenPal[pal])/LOG2;
                mat[pal][doc] = tf * idf;
            }
        }
    }
    return mat;
    }

/**
* Determina si una cadena es alfabética (acepta punto). 
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/30
* @param cad Cadena a determinar
* @return Si es alfabética o no
*/

public static Boolean esAlfab(String cad) {
    Boolean alfab = true;
    int i = 0;
    int l = cad.length();
    alfab = l > 2;
    while (i < l && alfab) {
        if (Character.isLetter(cad.charAt(i)) || cad.charAt(i)=='\''){
        } else {
            alfab =false;
        }
        i++;
    }
    return alfab;
    
}

/**
* Calcula el vector fila de las normas de las colummnas de una matriz
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/30
* @param matriz Matriz a calcular las normas de las columnas
* @return Vector fila de las normas de la columnas
*/

public static double [] normaMatrizTD(double [][] matriz) {
    int nPal = matriz.length;
    int nDoc = matriz[0].length;
    double sum, val;
    double [] norma = new double[nDoc];
    for (int doc = 0; doc < nDoc; doc++) {
        sum = 0;
        for (int pal = 0; pal < nPal; pal++) {
           val = matriz[pal][doc];
           sum =  sum + val*val;
        }
        norma[doc] = Math.sqrt(sum);
    }
    return norma;
}

/**
* Escribe vector por columna
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/30
* @param vec Vector a escribir
*/

public static void escVecCol(double [] vec) {
    System.out.println("VECTOR\n");
    double min, max;
    min =vec[0];
    max =vec[0];
    double sum=0;
    int i=0, px =0, pm = 0;
    for (double v : vec) {
        if (v < min) {
            min = v;
            pm = i;            
        }
        if (v > max) {
            max = v;
            px = i;
        }
        sum = sum + v;
        System.out.println(v);
        i++;
    }
    System.out.println("\n\nPromedio: " + sum/vec.length);    
    System.out.println("Doc " + px + " Máximo: " + max);    
    System.out.println("Doc " + pm + " Mínimo: " + min);
    System.out.println("Número de elementos: " + vec.length);    
    }

/**
* Crea un archivo de flujo de bits desde un vector
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/30
* @param pathArch Path del archivo donde se escribe el vector
* @param V Vector
*/

public static void genArchDesdeVector(String pathArch, double [] V){
    try {
        FileOutputStream fileOut=new FileOutputStream(pathArch);
        DataOutputStream salida = new DataOutputStream(fileOut);
        int tamV = V.length;

        salida.writeInt(tamV);

        for (int i = 0; i < V.length; i++) {
                salida.writeDouble(V[i]);
            }
        salida.close();
        }
    catch (java.io.IOException ex) {
        ex.printStackTrace();
    }
}        

/**
* Carga un vector desde archivo
* @throws java.io.IOException Error de lectura del archivo
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/10/30
* @param path Path del archivo desde donde se lee el vector
* @return Vector leído desde archivo
*/

public static double [] loadVectorDesdeArch(String path) throws IOException{
    
    double [] V = null;

    try {
        FileInputStream fileIn=new FileInputStream(path);
        DataInputStream entrada = new DataInputStream(fileIn);

        int tamV = entrada.readInt();
        V  = new double[tamV];
        for (int i = 0; i < tamV; i++) {
                V[i] = entrada.readDouble();
            }
        entrada.close();
        }
    catch (java.io.IOException ex) {
    }
    return V ;
}

/**
* Dado el código de la consulta y el código del paper determina si está en la
* tabla de documentos pertinentes
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/06
* @param codQuery Código de la consulta
* @param codPaper Código del paper
* @return Existe el registro o no
*/

public static Boolean existeQueryPaper(int codQuery, String codPaper) {
    List<QueryPaper> queryPapers;        
    QueryPaperJDBC queryPaperJDBC = new QueryPaperJDBC();
    queryPapers = queryPaperJDBC.select(codQuery, codPaper);
    if (queryPapers.size()==0) {
//        System.out.println("Registros no existe con esos datos -> " + codQuery + " " + codPaper);
        return false;
        }
    else
//        for(QueryPaper qp : queryPapers)
//            System.out.println(qp.toString());
    return true;
}

/**
* Hace stemmer a palabra dada 
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/06
* @param pal Palabra
* @param leng Idioma en que se realiza
* @param stopwords lista de palabras vacías
* @return La palabra estimizada o vacía, si es stopword
*/

public static String stemPal(String pal, String leng, List<String> stopwords) {
    String palStem ="";
// Steemer de tres idiomas            
    englishStemmer english = new englishStemmer();
    spanishStemmer spanish = new spanishStemmer();
    portugueseStemmer portuguese = new portugueseStemmer();

    if(stopwords.contains(pal)){
       return palStem;
    }
        switch (leng) {
            case "pt":
                portuguese.setCurrent(pal);
                portuguese.stem();
                palStem = portuguese.getCurrent();
                break;

            case "en":
                english.setCurrent(pal);
                english.stem();
                palStem = english.getCurrent();
                break;

            case "es":
                spanish.setCurrent(pal);
                spanish.stem();
                pal = spanish.getCurrent();
                break;
            default:
                palStem = "";
                System.out.println("Error en lenguaje");
        }
                
    return palStem;
}

/**
* Calcula y escribe los estadísticos máximo, mínimo, promedio, varianza y desviación
* estándar de un vector
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/06
* @param V vector
*/

public static void estadVec(double [] V) {
    double max, min, prom, suma = 0, var = 0, desv;
    int posMax = 0, posMin = 0;
    max = V[0];
    min = V[0];
    System.out.println("Tamaño del vector de resultados -> " + V.length);
    for (int i = 0; i < V.length; i++) {
        if (V[i] > 0) {
            suma += V[i];
        }


        if (V[i] > max) {
            max = V[i];
            posMax = i;
        }

        if (V[i] < min) {
            min = V[i];
            posMin = i;            
        }
    }
    System.out.println("Suma: " + suma + " tamaño del vector aquí -> " + V.length);
    prom = suma/V.length;
    
    double dif=0;
    for (int i = 0; i < V.length; i++) {
        if (V[i] > 0) {
            dif = V[i] - prom;
        }
        else
            dif = -prom;

        var = var + dif*dif;
    }  
    var = var / V.length;
    desv = Math.sqrt(var);
    System.out.printf("\nNúmero de elementos: %7d\n", V.length);
    System.out.printf("Mínimo:                %.3f  posición: %d\n", min, posMin);
    System.out.printf("Máximo:                %.3f  posición: %d\n", max, posMax);
    System.out.printf("Promedio:              %.3f\n", prom);
    System.out.printf("Varianza:              %.3f\n", var);
    System.out.printf("Desviación estándar:   %.3f\n", desv);
 
}

/**
* Escribe en pantalla, la lista de registros de la tabla query del dataset
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/07
* @param queries Lista de registros query
*/

public static void listaRegQueries(List<Query> queries) {
        for(Query  query : queries)
            System.out.println("Query --> " + query);
        System.out.println();
    
}

/**
* Ordena un vector a la par con las posiciones de sus elementos
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/07
* @param ranking vector a ordenar
* @param pos  de posiciones
*/


public static void ordeneResultados(double [] ranking, int [] pos) {
    for (int i = 0; i < pos.length; i++) {
        pos[i] = i;
    }
    double val;
    int posic;
    for (int i = 1; i < ranking.length; i++) {
        val = ranking[i];
        posic = i;
        int k = i - 1;
        while (k>=0 && val > ranking[k]) {
            ranking[k+1] = ranking[k];
            pos[k+1] = pos[k];
            k--;
        }
        ranking[k+1] = val;
        pos[k+1] = posic;        
    }
    
}

/**
* Suma todos los elementos de una fila
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/11
* @param matriz Matriz de entrada
* @return Vector columna con la suma de elementos de cada fila
*/

public static double [] sumFilaVec(double [][] matriz) {
    int nPal = matriz.length;
    int nDoc = matriz[0].length;
    double sum, val;
    double [] frecFila = new double[nPal];
    
    for (int pal = 0; pal < nPal; pal++) {    
        sum = 0;
        for (int doc = 0; doc < nDoc; doc++) {
            val = matriz[pal][doc];
            if (val>0) {
                sum =  sum + val;
            }
        }
        frecFila[pal]= sum;
    }
    return frecFila;
}

/**
* Crea archivo de texto con los resultados de precisión y recall de las consultas
*   @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/11
* @param codQuery Código de la consulta
* @param consulta Palabras de la consulta
* @param numPalQuery Número de palabras de la consulta
* @param M Matriz de resultados con las columnas de precisión, recall y pertinencia
* o no para ese resultado en este punto (1, 0)
* @param codPapers Vector con el código de los papers recuperados en la consulta
* @param pathArch Path del archivo a crear
*/ 

public static void genArchResulQuery(int codQuery, String consulta, int numPalQuery,
                                   double [][] M, String [] codPapers,
                                   long codTopicQuery, String pathArch) {
    try {
        FileWriter fw;            
        fw = new FileWriter(pathArch, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter salida = new PrintWriter(bw);
        for (int i = 0; i < M.length; i++) {
            salida.printf("%s,%s,%d,%d,%f,%f,%f,%f,%f,%f,%f,%d,%f\n", consulta, 
                    codPapers[i], numPalQuery,
                    codQuery, M[i][0], M[i][1], M[i][2], M[i][3], M[i][4],
                    M[i][5], M[i][6], codTopicQuery, M[i][7]);
        }
        salida.close();  

    } catch (IOException ex) {
            }

    }

/**
* Crea archivo de texto con la precisión media de cada consulta

* @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/16
* @param codQuery Código de la consulta
* @param consulta Palabras de la consulta
* @param numPalQuery Número de palabras de la consulta
* @param avPrec Precisión media para esta consultad
* @param nueCodQry Nuevo código de la consulta de 3 dígitos
* @param pathArch Path del archivo a crear
*/ 

public static void genArchAvPrec(int codQuery, String consulta, int numPalQuery,
                                   double avPrec, double nueCodQry, String pathArch) {
    try {
        FileWriter fw;            
        fw = new FileWriter(pathArch, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter salida = new PrintWriter(bw);
        salida.printf("%d,%s,%d,%f,%f\n", codQuery,consulta, numPalQuery, avPrec, nueCodQry);
        salida.close();  
        
    } catch (IOException ex) {
        System.out.println("Error escribiendo archivo de precisión media");
        ex.printStackTrace();
    }

    }

/**
* Crea matriz desde un archivo CSV
* @return Matriz
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/25
* @param pathArch Path del archivo 
*/

public static double [][] loadMatrizDesdeCSV(String pathArch) {
    double [][] M = null;
    String [] elemento;
    int nCol, nFil = 0;
        try {
            // Lee inicialmente el archivo para determinar su tamaño
            FileReader fr = new FileReader(pathArch);
            BufferedReader entrada = new BufferedReader(fr);
            String s;
            s = entrada.readLine();
            elemento = s.split(",");
            nCol = elemento.length;
            while(s != null) {
                nFil++;
                s = entrada.readLine();
            }
            System.out.println("Tamaño de la matriz: " + nFil + " x " + nCol);
            entrada.close();
            // con el tamaño, crea la matriz correspondiente
            M = new double[nFil][nCol];
            fr = new FileReader(pathArch);
            entrada = new BufferedReader(fr);
            int f = 0;
            while((s = entrada.readLine()) != null) {
                elemento = s.split(",");
                for (int c = 0; c < nCol; c++) {
                    M[f][c] = Double.parseDouble(elemento[c]);
                }
                f++;
                nFil++;
            }            
        }
        catch(java.io.FileNotFoundException fnfex) {
                System.err.println("contArchTxt: Archivo no encontrado: " + fnfex);}
        catch(java.io.IOException ioex) {
                    System.err.println("No leyó archivo " + pathArch + ioex);
            }
        return M;
}

/**
* Escribe matriz en pantalla
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/25
* @param M Matriz
*/

public static void escMat(double [][] M) {
    int nF, nC;
    nF = M.length;
    nC = M[0].length;
    for (int i = 0; i < nF; i++) {
        System.out.println("");
        for (int j = 0; j < nC; j++) {
            System.out.print(M[i][j] +"\t");
        }
    }
    System.out.println("");
}

/**
* Normaliza una matriz a través de las normas sus vectores columna
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/11/30
* @param M Matriz
*/

    public static void normalizarMat(double [][] M){
        int nFil = M.length;
        int nCol = M[0].length;
        double sum, val;
        double [] norma = new double[nCol];
        for (int doc = 0; doc < nCol; doc++) {
            sum = 0;
            for (int pal = 0; pal < nFil; pal++) {
               val = M[pal][doc];
               sum =  sum + val*val;
            }
            norma[doc] = Math.sqrt(sum);
        }        
        for (int f = 0; f < nFil; f++) {
            for (int c = 0; c < nCol; c++) {
                M[f][c] = M[f][c] / norma[c];
            }
        }
    }

/**
* Expansión automática de una consulta
* @author: Germán A. Osorio-Zuluaga
* @version: 2020/12/01
* @return Consulta ampliada con su vector normalizado
* @param leng Lenguaje de la consulta
* @param consulta Palabras de la consulta
* @param stopwords Palabras vacías del lenguaje dado
* @param wordBag Diccionario de palabras. La clave es la palabra
* @param wordBagCod Diccionario de palabras. La clave es código de la palabra
* @param tesauro Matriz con el tesauro
*/
    
public static TreeMap <Double, Integer> rankAQE(String leng, String consulta,
                    List<String> stopwords, HashMap<String, Integer> wordBag,   
                    HashMap<Integer, String> wordBagCod, double [][] tesauro) {
 
        StringTokenizer tokens = new StringTokenizer(consulta.toLowerCase());
        String word;
        System.out.println("_____________________________________________");
        Set <Integer> codQueryWords = new HashSet<>();
                        // Se estimizan las palabras de la consulta
        while (tokens.hasMoreTokens()) {
            String palabra = tokens.nextToken();
            System.out.println(palabra);
            word = Condor.stemPal(palabra, leng, stopwords);
            if (!word.equals("")) {
                System.out.println("Palabra estimizada: " + word);
                if (wordBag.containsKey(word)) {
                    System.out.println(word 
                            + " está en el diccionario con código "
                            + wordBag.get(word));
                    codQueryWords.add(wordBag.get(word));
                }
            }
        }
// Se calcula el ranking para cada documento según la consulta ________________
        double sum;
        double normaQuery = Math.sqrt(codQueryWords.size());
        double [] ranking = new double[wordBag.size()];

        for (int pal = 0; pal < wordBag.size(); pal++) {
            ranking[pal] = 0;
            sum = 0;
        try {            
            for (int indPal : codQueryWords) {
//                System.out.println("indPal= " +indPal +" pal= "+pal);                
                sum = sum + tesauro[indPal][pal];

            }
        }                
        catch (java.lang.NullPointerException ex) {
            System.out.println(ex + "Error pal= " + pal);
        }

            ranking[pal] = sum / normaQuery;

        }
// Se ordenan los resultados de mayor a menor usando TreeMap____________________
        TreeMap <Double, Integer> ranks = new TreeMap<>();
        TreeMap <Double, Integer> ranksAQE = new TreeMap<>();

        double dec = -0.0000001;
        int z = 0;
        double v;

        for (double value : ranking) {
//            System.out.println(z + "Value= " + value);
            if (value > 0.2) {
               v = -value;
        // TreeMap no permite valores duplicados, se agrega un valor que 
        // afecte, de manera insignificante, los resultados si se da _______                 
               while (ranks.containsKey(v)) {
                    v -= dec;
                    dec -= 0.0000001;
                }
                ranks.put(v, z);
//                System.out.println("Pasó 2 " + z);                           
            }
            z++;

        }
// Se pasa a una colección de 50 o menos elementos______________________________
        Set <Double> claves = ranks.keySet();
        double val, sumQuery = 0;
        int nVal = 0;
        Iterator <Double> iterador = claves.iterator();
        while (iterador.hasNext()) {
            val = iterador.next();
            int indPal = ranks.get(val);
            ranksAQE.put(val, indPal);
            nVal++;
            if (nVal >= 50) {
                break;
            }
        }
        return ranksAQE;
}    
    
    
}
