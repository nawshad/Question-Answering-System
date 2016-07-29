/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryingjena;

/*import java.sql.ResultSet;*/
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.Query;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import static org.apache.jena.vocabulary.DCTerms.subject;
import static org.apache.jena.vocabulary.RDF.Nodes.object;
import static org.apache.jena.vocabulary.RDF.Nodes.predicate;

/**
 *
 * @author nawshad
 */
public class Question1 {
    public static void q1processing(Model model) throws IOException{ 
        //Model model = RDFDataMgr.loadModel(ttlFile);
        StmtIterator iter = model.listStatements();
        HashMap<Resource,String> stadiums = new HashMap<Resource,String>();
        
        FileWriter fos = new FileWriter("cmput690w16a3_q1_farruque.tsv");
        PrintWriter dos = new PrintWriter(fos);
        
        try {
            while ( iter.hasNext() ) {
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
        
        //get the team names;
        String queryStringForTeam = 
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
    
        org.apache.jena.query.Query queryForTeam = QueryFactory.create(queryStringForTeam) ;
        List<String> teamNameList = new ArrayList<String>();
        
        try (QueryExecution qexec = QueryExecutionFactory.create(queryForTeam, model)) {
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
        

        //System.out.println("Stadiums:"+stadiums);
        List<String> listOfFacts = new ArrayList<String>();
        
        String queryString = 
                "PREFIX fb: <http://rdf.freebase.com/ns/>\n" +
                "PREFIX fbk: <http://rdf.freebase.com/key/>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX c690: <cmput690>\n" +
                "\n" +
                "SELECT ?resource ?description \n" +
                "WHERE\n" +
                "{?resource c690:hasDocument ?description .\n" +

                "}" ;
        
        org.apache.jena.query.Query query = QueryFactory.create(queryString) ;
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
          ResultSet results = qexec.execSelect() ;
          for ( ; results.hasNext() ; )
          {
                QuerySolution soln = results.nextSolution() ;    
                for (Map.Entry<Resource, String> entry : stadiums.entrySet()) {
                    Resource key = entry.getKey();
                    String value = entry.getValue();
                    //System.out.println("Key:"+key+"Value:"+value);
                    if(soln.getResource("resource").equals(key)){
                        String curatedText = Utils.documentCleaning(soln.getLiteral("description").toString(),value);
                        String[] filters = {"home","owned by", "used by", "is used for", "used mostly for"};
                        listOfFacts = ExtractFromDocument.extractFromDocument(curatedText, filters, value);//here we are getting only the objects which may contain club name
                        
                        String clubNames = Utils.getOrganization(listOfFacts, teamNameList);
                        dos.println(value+"\t"+clubNames);
                        
                    }
                   
                }
            }
        }
        dos.close();
        fos.close();
    }
}
