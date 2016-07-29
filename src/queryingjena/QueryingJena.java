/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryingjena;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.management.Query;
import static org.apache.jena.assembler.JA.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author nawshad
 */
public class QueryingJena {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        
        List<String> nationalities = ReadNationalities.getCountryList("nationalities_in_file.txt");
        
        if(args.length==2){
            Model model = RDFDataMgr.loadModel(args[0]);
            if(args[1].equals("1")){
                Question1.q1processing(model);
            }else if(args[1].equals("3")){
                Question3.q3processing(model);
            }else if(args[1].equals("4")){
                Question4.q4processing(model);
            }else if(args[1].equals("5")){
               Question5.q5processing(model, nationalities);
            }   
        }
    }
}
