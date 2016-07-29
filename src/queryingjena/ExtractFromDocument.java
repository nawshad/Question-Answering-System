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
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author nawshad
 */
public class ExtractFromDocument {
    public static List<String> extractFromDocument(String text, String[] filterBy, String topic) throws IOException { 
        Properties props = PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,lemma,parse,natlog,openie"
        );
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
 
        List<String> listOfFacts = new ArrayList<String>();   
        Annotation doc = new Annotation(text);
        pipeline.annotate(doc);

        for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)){
           List<String> tripleValues = new ArrayList<String>();
           Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
            FilterTriples ft = new FilterTriples();
            for (RelationTriple triple : triples) {
                if(Utils.stringContainsItemFromList(triple.relationGloss().toString(), filterBy)||Utils.stringContainsItemFromList(triple.objectGloss().toString(), filterBy)){ 
                    if(ft.validTriple(triple)
                            &&ft.validObj(triple.objectGloss())&&(Utils.StringMatch(triple.subjectGloss().toString(), topic))){           
                        String fact = triple.relationGloss()+"\t"+triple.objectGloss();
                        //System.out.println(fact);
                        listOfFacts.add(fact);
                    }    
                }               
            }
        }
        return listOfFacts;
    }         
}

     
 