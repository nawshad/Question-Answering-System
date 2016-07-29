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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author nawshad
 */
public class Utils {
    public static boolean stringContainsItemFromList(String inputString, String[] items)
    {
        //System.out.println("Inside String Contains Function: "+inputString);
        for(int i =0; i < items.length; i++)
        {
            if(inputString.contains(items[i]))
            {
                //System.out.println("Contains: "+inputString);
                return true;
            }
        }
        return false;
    }
    
    public static String documentCleaning(String text, String topic){
        text = text.replaceAll("\\\\n", " ");
        text = text.replaceAll(" and the ", " "+topic+" ");
        text = text.replaceAll(" which ", " "+topic+" ");
        text = text.replaceAll(" It ", " "+topic+" ");
        text = text.replace("The stadium", " "+topic+" ");
        text = text.replaceAll(" it ", " "+topic+" ");
        
        return text;   
    }
    
    public static String getDescription(Model model, String resourceID){
        String Description="";
        String queryDescriptions = 
                "PREFIX fb: <http://rdf.freebase.com/ns/>\n" +
                "PREFIX fbk: <http://rdf.freebase.com/key/>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX c690: <cmput690>\n" +
                "\n" +
                "SELECT ?player_id ?description\n" +
                "WHERE\n" +
                "{\n" +
                 "   "+resourceID+" c690:hasDocument ?description . \n" +   
                " \n" +
                "}"
                + "GROUP BY ?player_id ?description" ;
        
        
        org.apache.jena.query.Query queryDesc = QueryFactory.create(queryDescriptions) ;
        try (QueryExecution qexec = QueryExecutionFactory.create(queryDesc, model)) {
          ResultSet results = qexec.execSelect() ;
          for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution();
                Description = soln.getLiteral("description").toString();
                
            } 
        }
        
        return Description;
    }
    
    public static String getOrganization(List<String> objects, List<String> clubNames){
        String returnOrg = "";
        for(String object : objects){
            //System.out.println("Objects:"+object);
            String[] objectSplits = object.split("\\s+");
            for(String clubName : clubNames ){
                //System.out.println("Clubs:"+clubName);
                String[] splits = clubName.split("\\s+");
                for(String split: splits){
                    for(String objectSplit : objectSplits){
                        if(objectSplit.equals("of")
                            ||objectSplit.toLowerCase().equals("the")    
                            ||objectSplit.toLowerCase().equals("club")
                            ||objectSplit.toLowerCase().equals("futbol")
                            ||objectSplit.toLowerCase().equals("calcio")
                            ||objectSplit.toLowerCase().equals("fc")  
                            ||objectSplit.toLowerCase().equals("ac")
                            ||objectSplit.toLowerCase().equals("athletic")
                            ||objectSplit.toLowerCase().equals("athletico")
                            ||objectSplit.toLowerCase().equals("atletico")
                            ||objectSplit.toLowerCase().equals("ca")
                            ||objectSplit.toLowerCase().equals("rcd")
                            ||objectSplit.toLowerCase().equals("real")
                            ||objectSplit.toLowerCase().equals("us")
                            ||objectSplit.toLowerCase().equals("as")
                            ||objectSplit.toLowerCase().equals("ud")
                            ||objectSplit.toLowerCase().equals("football")
                            ||objectSplit.toLowerCase().equals("cf")
                            ||objectSplit.toLowerCase().equals("de")    
                            ||objectSplit.toLowerCase().equals("ss")){
                                continue;
                        }
                        if(split.equals(objectSplit)){
                            return clubName;
                        }
                    }
                }
            }
        }
        
        return returnOrg;
    }
    
    public static boolean StringMatch(String subject, String topic){
        //System.out.println("Subject: "+subject+"Topic: "+topic);
        if(subject.equals(topic)){
            return true;
        }     
      return true;
    }
    
}
