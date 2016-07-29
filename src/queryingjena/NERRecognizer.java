/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryingjena;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author nawshad
 */
public class NERRecognizer {
    public static void ner(String inputFile, String outputFile){
        String infile = inputFile;
        String outFile  =  outputFile;
        String line = "";
        
        Properties props = PropertiesUtils.asProperties(
                "annotators", "tokenize, ssplit,  pos, lemma, parse, ner"
        );
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Annotate an example document.  

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(infile);
            
            FileWriter fos = new FileWriter(outFile);
                PrintWriter dos = new PrintWriter(fos);

            try ( // Always wrap FileReader in BufferedReader.
                    BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                int i = 0;
                String fullSentence = "";
                
                while((line = bufferedReader.readLine()) != null) { 
                    line = line.replaceAll("\\\\n", " "); 
                  
                    Annotation doc = new Annotation(line);
                    pipeline.annotate(doc);
                    
                   
                    List<String> lstNames= new ArrayList<String>();
                    
                    for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
                        for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                            String word = token.get(CoreAnnotations.TextAnnotation.class);
                            String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                            if(!ne.equals("O"))dos.println(word+"\t"+ne);
                        }
                    }
                   i++;         
                }
                System.out.println("Full Sentence:"+fullSentence);
                
            }

            dos.close();
            fos.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                infile + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + infile + "'");                  
        }
    }
    
}
