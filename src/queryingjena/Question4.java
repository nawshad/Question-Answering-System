/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryingjena;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

/**
 *
 * @author nawshad
 */
public class Question4 {
    public static void q4processing(Model model) throws IOException{
        //Store the clean list of club.teamNames.   
        StmtIterator iter = model.listStatements();
        HashMap<Resource,String> stadiums = new HashMap<Resource,String>();
        
        FileWriter fos = new FileWriter("cmput690w16a3_q4_farruque.tsv");
        PrintWriter dos = new PrintWriter(fos);
   
        //Find out all the stadium names
        try {
            while (iter.hasNext() ) {
                Statement stmt      = iter.nextStatement();  // get next statement
                Resource  subject   = stmt.getSubject();     // get the subject
                Property  predicate = stmt.getPredicate();   // get the predicate
                RDFNode   object    = stmt.getObject();      // get the object

                if(predicate.getURI().equals("http://rdf.freebase.com/key/wikipedia.en")){
                    if((object.toString().contains("Stadium")||object.toString().toLowerCase().contains("stadio"))){
                        String[] split = object.toString().split("_");               
                        String stadiumName = "";
                        for(String items : split){    
                            stadiumName+=items+" ";
                        }
                        if(stadiumName.matches("^[ A-z]+$")){
                            //System.out.println("Resource:"+subject+" Objects:"+stadiumName);
                            stadiums.put(subject, stadiumName);
                        }
                    }
                }
            }
        } finally {
            if ( iter != null ) iter.close();
        }
        
        HashMap<String,String> stadiumsRenamed = new HashMap<String, String>();
        for (Map.Entry<Resource, String> entry : stadiums.entrySet()) {
            String stadiumResource = entry.getKey().toString();
            String stadiumName = entry.getValue().toString();
            //System.out.println("Stadium ID:"+stadiumResource+" Stadium Name:"+stadiumName);
            String rawDesc = Utils.getDescription(model,"<"+entry.getKey().toString()+">" );   
            String curatedText = Utils.documentCleaning(rawDesc, stadiumName);
            
            String[] filterBy = {"renamed after","named after","named in honor of", "renamed in honor of"}; 
            if(CheckTopics.isFormerPresident(curatedText, filterBy, stadiumName)){
                stadiumsRenamed.put(stadiumName, curatedText);
            }
        }
 
        String queryString = 
                "PREFIX fb: <http://rdf.freebase.com/ns/>\n" +
                "PREFIX fbk: <http://rdf.freebase.com/key/>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX c690: <cmput690>\n" +
                "\n" +
                "SELECT ?team_name \n" +
                "WHERE\n" +
                "{\n" +
                "   ?team_id fbk:wikipedia.en ?team_name .\n" +
                "   ?team_resource fb:sports.sports_team_roster.team ?team_id .\n" +
                "}\n" +
                "GROUP BY ?team_id  ?team_name\n" +
                "ORDER BY ASC(?team_name)" ;
    
        org.apache.jena.query.Query query = QueryFactory.create(queryString) ;
        List<String> teamNameList = new ArrayList<String>();
        
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
          ResultSet results = qexec.execSelect() ;   
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution();
                String[] split = soln.getLiteral("team_name").toString().split("_");               
                String teamName = "";
                for(String items : split){    
                    teamName+=items+" ";
                }
                if(teamName.matches("^[ A-z]+$")){
                    teamNameList.add(teamName); 
                }
            }   
        }
        
        for (Map.Entry<String, String> entry : stadiumsRenamed.entrySet()){
            String[] filters = {"home","owned by", "used by", "is used for", "used mostly for"};
            List<String> listOfFacts = ExtractFromDocument.extractFromDocument(entry.getValue(), filters, entry.getKey());//here we are getting only the objects which may contain club name
            
            String clubNames = Utils.getOrganization(listOfFacts, teamNameList);
            dos.println(entry.getKey()+"\t"+clubNames); 
        }
        dos.close();
        fos.close();
    }
}
