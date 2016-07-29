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
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;

/**
 *
 * @author nawshad
 * Returns the team id and count of nationalities in those teams
*/
public class Question5 {
    public static void q5processing(Model model, List<String> nationalities) throws IOException{
        StmtIterator iter = model.listStatements();
        HashMap<Resource,List<Resource>> playersUnderTeam = new HashMap<Resource,List<Resource>>();
        HashSet<String> nationality = new HashSet<String>();
        HashMap<String, HashSet<String>> teamsNationalities = new HashMap<String, HashSet<String>>();
        HashSet<String> national = new HashSet<String>();
        
        FileWriter fos = new FileWriter("cmput690w16a3_q5_farruque.tsv");
        PrintWriter dos = new PrintWriter(fos);
        
        String queryString = 
                "PREFIX fb: <http://rdf.freebase.com/ns/>\n" +
                "PREFIX fbk: <http://rdf.freebase.com/key/>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX c690: <cmput690>\n" +
                "\n" +
                "SELECT ?team_id  ?player_id \n" +
                "WHERE\n" +
                "{\n" +
                "   ?team_resource fb:sports.sports_team_roster.player ?player_id .\n" +
                "   ?team_resource fb:sports.sports_team_roster.team ?team_id .\n" +
                "}\n" +
                "GROUP BY ?team_id  ?player_id\n" +
                "ORDER BY ASC(?team_id)" ;
        
        org.apache.jena.query.Query query = QueryFactory.create(queryString) ;
        List<Resource> playersID = new ArrayList<Resource>();
        
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
          ResultSet results = qexec.execSelect() ;
            
            String curTeamID = "";
            String prevTeamID = "";
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution();
                curTeamID  = soln.getResource("team_id").toString();
                if(prevTeamID.equals("")){
                    prevTeamID = curTeamID;
                }
                if(curTeamID.equals(prevTeamID)){
                    playersID.add(soln.getResource("player_id"));
                    playersUnderTeam.put(soln.getResource("team_id"), playersID);
                }
                else{
                   playersID.add(soln.getResource("player_id"));
                   playersUnderTeam.put(soln.getResource("team_id"), playersID);
                   playersID = new ArrayList<Resource>();
                }
                prevTeamID = curTeamID;
            }   
        }
        
        for (Map.Entry<Resource, List<Resource>> entry : playersUnderTeam.entrySet()) 
        {
            Resource key = entry.getKey();
            //System.out.println("For Team:"+key);
            for(Resource playerID : entry.getValue()){  
                //System.out.println("For player:"+playerID);
                String desc = Utils.getDescription(model, "<"+playerID.toString()+">");
                //System.out.println("Desc:"+desc);
                String nat = CheckTopics.returnNationality(desc, nationalities);
                national.add(nat);
               
            }
            teamsNationalities.put(key.toString(), national);
            national = new HashSet<String>();
        }
        
        
        for (Map.Entry<String, HashSet<String>> entry : teamsNationalities.entrySet()) {
            String key = entry.getKey();
            dos.println(key+"\t"+entry.getValue().size());
        }
        
        dos.close();
        fos.close();
       }
        
 }
    

