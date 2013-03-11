package snlparser;

import edu.stanford.nlp.pipeline.Annotation;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author mja
 */
public class SNLParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        NLPFactory snlpHelper = NLPFactory.Create(props);
        
        Scanner s = new Scanner(System.in);
        String input = "";
        
        while (!input.equals("q")) {
            System.out.println("Enter a sentence to be parsed, or \"q\" to exit:");
            input = s.nextLine();
            Annotation doc = snlpHelper.parse(input);
            NLPFactory.dump(doc);
        }
    }
}
