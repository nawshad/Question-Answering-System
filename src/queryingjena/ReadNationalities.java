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
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author nawshad
 */
public class ReadNationalities {
    public static List<String> getCountryList(String inputFile) throws FileNotFoundException{
        List<String> lstCountries = new ArrayList<String>();
       
        FileReader fileReader = 
            new FileReader(inputFile);
        String line = "";

        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while((line = bufferedReader.readLine()) != null) { 
                //System.out.println(line);
                lstCountries.add(line);
            }     
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                inputFile + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + inputFile + "'");                  
        }

        return lstCountries;
    }

}
