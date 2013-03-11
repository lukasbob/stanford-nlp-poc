package snlparser;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author mja
 */
public class NLPFactory {

    private static HashMap<Integer, NLPFactory> instances = new HashMap<Integer, NLPFactory>();
    private final Properties properties;
    private StanfordCoreNLP parser;

    private NLPFactory(Properties props) {
        this.properties = props;
        this.parser = new StanfordCoreNLP(props);
    }
    
    private static final String FORMATSTRING = "%25s%10s%12s\n";
    
    public Annotation parse(String text) {
        Annotation doc = new Annotation(text);
        parser.annotate(doc);
        return doc;
    }
    
    public static void dump(Annotation document) {
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        
        System.out.printf(FORMATSTRING, "Word", "Pos", "Entity");
        System.out.printf(FORMATSTRING, "------", "------", "------");

        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(TextAnnotation.class);

                // this is the POS tag of the token
                String pos = token.get(PartOfSpeechAnnotation.class);

                // this is the NER label of the token
                String ne = token.get(NamedEntityTagAnnotation.class);
                
                System.out.printf(FORMATSTRING, word, pos, ne);
            }
            
            Tree t = sentence.get(TreeAnnotation.class);
            
            SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
            
            System.out.println("\nInternal dependencies in the sentence:");
            System.out.println(dependencies);
            
            System.out.println("\nTree structure for the sentence:");
            System.out.println(t);
        }
    }
    
    public static NLPFactory Create(Properties props) {
        int hashAddress = props.hashCode();
        NLPFactory instance = instances.get(hashAddress);

        if (instance == null) {
            instance = new NLPFactory(props);
            instances.put(hashAddress, instance);
        }

        return instance;
    }
}
