/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryingjena;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author nawshad
 */
public class FilterTriples {
    public boolean validTriple(RelationTriple triple){  
        boolean flag = false;
        //System.out.println("Inside validTriple function:"+triple.subjectGloss()+" "+triple.relationGloss()+" "+triple.objectGloss());
        
        if(triple.confidence>0.9){
            if(this.validSubj(triple.subjectGloss().toString())&&this.validPred(triple.relationGloss().toString())&&this.validObj(triple.objectGloss().toString())){
                flag = true;
            }
        }
        
        return flag;
    }
    public boolean validSubj(String text){
        //System.out.println("Inside validSubj Funtion:"+text);
        boolean isPresent = false;
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
          // traversing the words in the current sentence
          // a CoreLabel is a CoreMap with additional token-specific methods
          for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
            // this is the text of the token
            String word = token.get(TextAnnotation.class);
            // this is the POS tag of the token
            String pos = token.get(PartOfSpeechAnnotation.class);
            // this is the NER label of the token
            String ne = token.get(NamedEntityTagAnnotation.class);
            
            //System.out.println("Word"+word+"pos"+pos+"ne"+ne);
            
            if(pos.equals("NNP")
                ||pos.equals("NNPS")
                ||pos.equals("NN")
                ||pos.equals("NNS")
                ||pos.equals("FW")){
                isPresent = true;
            }
           
     
          }
      }

    return isPresent;
    }
    
    public boolean validPred(String text){
        //System.out.println("Inside validPred Funtion:"+text);
        boolean isPresent = false;
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
          // traversing the words in the current sentence
          // a CoreLabel is a CoreMap with additional token-specific methods
          for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
            // this is the text of the token
            String word = token.get(TextAnnotation.class);
            // this is the POS tag of the token
            String pos = token.get(PartOfSpeechAnnotation.class);
            // this is the NER label of the token
            String ne = token.get(NamedEntityTagAnnotation.class); 
            
            if(pos.equals("IN")
                ||pos.equals("VB")
                ||pos.equals("VBD")
                ||pos.equals("VBG")
                ||pos.equals("VBP")
                ||pos.equals("VBZ")){
                isPresent = true;
            }
     
          }
      }

    return isPresent;
    }
    
    public boolean validObj(String text){
        //System.out.println("Inside validObj Funtion:"+text);
        boolean isPresent = false;
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
          // traversing the words in the current sentence
          // a CoreLabel is a CoreMap with additional token-specific methods
          for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
            // this is the text of the token
            String word = token.get(TextAnnotation.class);
            // this is the POS tag of the token
            String pos = token.get(PartOfSpeechAnnotation.class);
            // this is the NER label of the token
            String ne = token.get(NamedEntityTagAnnotation.class); 
            
            //System.out.println("Word:"+word+"POS:"+pos+"NE:"+ne);
            
            if(pos.equals("NNP")
                ||pos.equals("NNPS")
                ||pos.equals("NN")
                ||pos.equals("NNS")
                ||pos.equals("JJ")
                /*||pos.equals("JJR")
                ||pos.equals("JJS")*/){
                isPresent = true;
            }
            
          }
      }
      return isPresent;
    }
    
}
