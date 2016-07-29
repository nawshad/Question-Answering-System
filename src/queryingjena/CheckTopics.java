/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryingjena;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author nawshad
 */
public class CheckTopics {
    public static boolean isNationality(String text, String[] filterBy){
         boolean isPresent = false;
         Properties props = PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,lemma,parse,natlog,openie"
        );
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
 
        List<String> listOfFacts = new ArrayList<String>();   
        Annotation doc = new Annotation(text);
        pipeline.annotate(doc);
        
        //System.out.println("Document:"+text);

        for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)){
           List<String> tripleValues = new ArrayList<String>();
           Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
            //FilterTriples ft = new FilterTriples();
            for (RelationTriple triple : triples) {
                //System.out.println("Triples:"+triples);
                if(Utils.stringContainsItemFromList(triple.objectGloss().toString(), filterBy)){ 
                    //System.out.println("Contains: "+triple.objectGloss().toString());
                    isPresent = true;
                    return isPresent;
                }               
            }
            //if(isPresent)break;
        }
        return isPresent;
    }
    
    public static String returnNationality(String text, List<String> nationalities){
        String nationality = "";
        Properties props = PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,lemma,parse,natlog,openie"
        );
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
 
        List<String> listOfFacts = new ArrayList<String>();   
        Annotation doc = new Annotation(text);
        pipeline.annotate(doc);
        
        //System.out.println("Text:"+text);
        
        //System.out.println("Document:"+text);

        for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)){
           List<String> tripleValues = new ArrayList<String>();
           Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
            FilterTriples ft = new FilterTriples();
            for (RelationTriple triple : triples) { 
                //if(ft.validTriple(triple)&&ft.validObj(triple.objectGloss())){
                    //System.out.println("Outside Object gloss:"+triple.objectGloss());
                    for (String nat : nationalities){
                       if(triple.objectGloss().toString().contains(nat)){
                           /*System.out.println("Inside Object gloss:"+triple.objectGloss());
                           System.out.println(nat);*/
                           nationality = nat;
                           return nationality;
                           //break;
                       }
                   }
                }               
            }  
        return nationality;
    }
    
    
    public static boolean isFormerPresident(String cleanText, String[] filterBy, String stadiumName){
        boolean isPresent = false;
        Properties props = PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,lemma,parse,natlog,openie"
        );
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
 
        List<String> listOfFacts = new ArrayList<String>();   
        Annotation doc = new Annotation(cleanText);
        pipeline.annotate(doc);
        
        //System.out.println("Document:"+cleanText);

        for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)){
           List<String> tripleValues = new ArrayList<String>();
           Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
            //FilterTriples ft = new FilterTriples();
            for (RelationTriple triple : triples) {
                //System.out.println("Triples:"+triples);
                if((Utils.stringContainsItemFromList(triple.objectGloss().toString(), filterBy)
                        ||Utils.stringContainsItemFromList(triple.relationGloss().toString(), filterBy))&&Utils.StringMatch(triple.subjectGloss(), stadiumName)){ 
                    //System.out.println("Contains: "+triple.objectGloss().toString());
                    isPresent = true;
                    return isPresent;
                }               
            }
        }
        return isPresent;
    }
    
}
