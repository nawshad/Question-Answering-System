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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import static org.apache.jena.enhanced.BuiltinPersonalities.model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.VCARD;

/**
 *
 * @author nawshad
 */
public class Question3 {
    public static void q3processing(Model model) throws IOException{
        //first need to find out the managers and their teams.
        //then find out teams and players.
        //keep the managers id and players id
        //from the players id find their description
        //see if that player has nationality spanish if it does, save the mangers id
        
        StmtIterator iter = model.listStatements();
        HashMap<Resource,List<Resource>> playersUnderManager = new HashMap<Resource,List<Resource>>();
        HashSet<Resource> managerID = new HashSet<Resource>();
        
        FileWriter fos = new FileWriter("cmput690w16a3_q3_farruque.tsv");
        PrintWriter dos = new PrintWriter(fos);
        
        String queryString = 
                "PREFIX fb: <http://rdf.freebase.com/ns/>\n" +
                "PREFIX fbk: <http://rdf.freebase.com/key/>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX c690: <cmput690>\n" +
                "\n" +
                "SELECT ?manager_id  ?player_id \n" +
                "WHERE\n" +
                "{\n" +
                "   ?team_resource fb:sports.sports_team_roster.player ?player_id .\n" +
                "   ?team_resource fb:sports.sports_team_roster.team ?team_id .\n" +
                "   ?management_resource fb:soccer.football_team_management_tenure.team ?team_id .\n" +
                "   ?management_resource fb:soccer.football_team_management_tenure.manager ?manager_id .\n" +
                "\n" +
                "}\n" +
                "GROUP BY ?manager_id ?player_id\n" +
                "ORDER BY ASC(?manager_id)" ;
        
        org.apache.jena.query.Query query = QueryFactory.create(queryString) ;
        List<Resource> playersID = new ArrayList<Resource>();
        
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
          ResultSet results = qexec.execSelect() ;
            
            String curManagerID = "";
            String prevManagerID = "";
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution();
                curManagerID  = soln.getResource("manager_id").toString();
                if(prevManagerID.equals("")){
                    prevManagerID = curManagerID;
                }
                if(curManagerID.equals(prevManagerID)){
                    playersID.add(soln.getResource("player_id"));
                    playersUnderManager.put(soln.getResource("manager_id"), playersID);
                }
                else{
                   playersID.add(soln.getResource("player_id"));
                   playersUnderManager.put(soln.getResource("manager_id"), playersID);
                   playersID = new ArrayList<Resource>();
                }
                prevManagerID = curManagerID;
            }   
        }
        
        String[] filter = {"Spanish"};
        for (Map.Entry<Resource, List<Resource>> entry : playersUnderManager.entrySet()) 
        {
            //dos.println("Test");
            Resource key = entry.getKey();
            //System.out.println("For Manager:"+key);
            for(Resource playerID : entry.getValue()){  
                //System.out.println("For player:"+playerID);
                String desc = Utils.getDescription(model, "<"+playerID.toString()+">");
                //System.out.println("Desc:"+desc);
                if(CheckTopics.isNationality(desc, filter)){
                    //System.out.println("Manager:"+key);
                    dos.println(key); 
                    break;
                }
            }
        }

         dos.close();
         fos.close();
       }
       
}
