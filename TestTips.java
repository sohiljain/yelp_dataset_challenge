/**
 * authors: Sohil Jain, Avruti Srivastava
 */
package yelp;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.packed.PackedInts.Reader;
import org.json.JSONException;
import org.apache.lucene.queries.mlt.MoreLikeThis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class TestTips {

    public static void main(String[] args) throws ParseException, IOException, JSONException {

      
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("C:\\Users\\Disha\\Desktop\\yelp_index2")));
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new ClassicSimilarity());

        ArrayList<ArrayList<String>> Vectors = new ArrayList<ArrayList<String>>();
        String line = null;
        //String fileName="td.txt";
        String fileName="C:\\Users\\Disha\\Desktop\\tiptest.txt";
		FileReader fReader = null;
		try {
			fReader = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fReader);
		try { 
		one:	while ((line = bufferedReader.readLine()) != null) {
				
			  ArrayList<String> temp = new ArrayList<String>();
				String[] numbers = line.split("\u0001");
				int sz = numbers.length;
				for (int j = 0; j < sz ; j++)
					if (numbers[j] != null && !numbers[j].isEmpty()) {
					//	numbers[j]=numbers[j].replaceAll("\n",",");
						temp.add((numbers[j]));
				
					}
				Vectors.add(temp);
			
		}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	    double acc=0;//Accuracy
	    //Calculates results for each Business ID
        one: for(int i=0;i<Vectors.size() ;i++)
        {      
      	System.out.println(i);
        String queryString = Vectors.get(i).get(2);  
        String Labels=Vectors.get(i).get(1);
        CustomAnalyzer analyzer = CustomAnalyzer.builder(Paths.get("C:\\Users\\Disha\\Desktop")).withTokenizer("standard").addTokenFilter("standard").addTokenFilter("lowercase").addTokenFilter("stop", "ignoreCase", "false").addTokenFilter("keepWord","words","C:\\Users\\Disha\\Desktop\\keepwords.txt","ignoreCase","false").addTokenFilter("porterstem").addTokenFilter("removeduplicates").build();
        QueryParser parser = new QueryParser("tip", analyzer);
       Query query = parser.parse(QueryParser.escape(queryString));
        BooleanQuery.setMaxClauseCount( Integer.MAX_VALUE );
        TopScoreDocCollector collector = TopScoreDocCollector.create(3);
        searcher.search(query, collector);
        ScoreDoc[] docs = collector.topDocs().scoreDocs;
      //  System.out.println("Doc size: "+docs.length);
        //System.out.println("GIVEN: "+Labels);
        //System.out.println("PREDICTED: ");
        int numoflabels=Labels.split(" ").length;
        //Comparison of predicted and actual Category labels
        two: for (int j = 0; j < docs.length; j++) {
            Document doc = searcher.doc(docs[j].doc);
        //     System.out.print(doc.get("category")+",  ");
            if(Labels.contains(doc.get("category")))
            		{ 
     
      
            	           acc++;
            	           break two;            
            		}
          
        }
   
      
        }
        
        reader.close();
       
        System.out.println("Accuracy: "+acc/Vectors.size());
    }

}