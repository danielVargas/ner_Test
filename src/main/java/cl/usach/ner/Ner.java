/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.ner;

import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 *
 * @author daniel
 */
@ManagedBean
@ViewScoped
public class Ner implements Serializable  {

    
    private String hello;
    private String tweet;
    private String oracion;
    private ArrayList<ArrayList<String>> nueva;
    
    
    @PostConstruct
    public void init() {
        
        hello = "hola mundooooooooo";
        tweet =  "Perú y Ecuador emiten alerta de tsunami; tras sismo en Chile #Video: The post Perú y Ecuador emiten alerta de por terremoto en #Iquique";
        nueva = new ArrayList<>();
    }
    /**
     * Creates a new instance of Ner
     */
    public Ner() {
    }
    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public String getOracion() {
        return oracion;
    }

    public void setOracion(String oracion) {
        this.oracion = oracion;
    }

    public ArrayList<ArrayList<String>> getNueva() {
        return nueva;
    }

    public void setNueva(ArrayList<ArrayList<String>> nueva) {
        this.nueva = nueva;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
    
    
    public void do_ner(){
        
        nueva = new ArrayList<>();
        StanfordCoreNLP pipeline = new StanfordCoreNLP(
	PropertiesUtils.asProperties(
		"annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref",
		"tokenize.language", "es"));

        // read some text in the text variable
        String text = tweet;
        text = text.replace("#", "");
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        
        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        
            oracion = text;
        for(CoreMap sentence: sentences) {
            System.out.println(sentence);
          // traversing the words in the current sentence
          // a CoreLabel is a CoreMap with additional token-specific methods
          for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
            // this is the text of the token
              System.out.println("Palabra:");
            String word = token.get(TextAnnotation.class);
              System.out.println(word);
              ArrayList<String> temp = new ArrayList<>();
              temp.add(word);
            // this is the POS tag of the token
            String pos = token.get(PartOfSpeechAnnotation.class);
            // this is the NER label of the token
            String ne = token.get(NamedEntityTagAnnotation.class);
              System.out.println(ne);
              if (ne != null) {
                  
                temp.add(ne);
              }
              
              nueva.add(temp);
          }

          // this is the parse tree of the current sentence
          Tree tree = sentence.get(TreeAnnotation.class);

          // this is the Stanford dependency graph of the current sentence
          SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
        }

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
        Map<Integer, edu.stanford.nlp.dcoref.CorefChain> graph = 
          document.get(CorefChainAnnotation.class);

        hello = "terminado";
    }
}
