/**
 * authors: Disha Wagle, Avruti Srivastava
 */
package search_project;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer.Builder;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.KeepWordFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.BytesRef;
import org.json.JSONException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class generateIndexReviews {

    public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException, JSONException {

       //Set below three variables docDir, indexDir, analyzer according to the analyzer to be used and path settings.
        Path indexDir = Paths.get("C:\\Users\\avruti\\Desktop\\yelp_indexrev");
        CustomAnalyzer analyzer = CustomAnalyzer.builder(Paths.get("C:\\Users\\avruti\\Desktop")).withTokenizer("standard").addTokenFilter("standard").addTokenFilter("lowercase").addTokenFilter("stop", "ignoreCase", "false").addTokenFilter("keepWord","words","C:\\Users\\avruti\\Desktop\\keepwords.txt","ignoreCase","false").addTokenFilter("porterstem").build();
        Directory fsDir = FSDirectory.open(indexDir);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter indexWriter = new IndexWriter(fsDir, iwc);

        HashMap<String, String> map = JsonParsingReviews.reviews();
       
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String category = entry.getKey();
            String reviews = entry.getValue();

            //Create a new document
            Document d = new Document();

            //Write all the fields to the Document
            d.add(new StringField("category", category, Field.Store.YES));
            d.add(new TextField("review", reviews, Field.Store.YES));

            //Write the document into indexWriter
            indexWriter.addDocument(d);

        }

        int numDocs = indexWriter.numDocs();

        indexWriter.forceMerge(1);
        indexWriter.commit();
        indexWriter.close();
        /*
        PrintWriter writer = new PrintWriter("C:\\Users\\avruti\\Desktop\\reviewtermslist.txt", "UTF-8");
        PrintWriter writer2 = new PrintWriter("C:\\Users\\avruti\\Desktop\\cattermslist.txt", "UTF-8");

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(("C:\\Users\\avruti\\Desktop\\yelp_indexrev"))));
		Terms vocabulary = MultiFields.getTerms(reader, "review");
		Terms vocabulary2 = MultiFields.getTerms(reader, "category");
		TermsEnum iterator = vocabulary.iterator();
		TermsEnum iterator2 = vocabulary.iterator();
		BytesRef byteRef = null;
		while((byteRef = iterator.next()) != null) {
		String term = byteRef.utf8ToString();
		writer.println(term+"\t");
		}
		while((byteRef = iterator2.next()) != null) {
			String term = byteRef.utf8ToString();
			writer2.println(term+"\t");
			}
			
		reader.close();
		writer.close();
		writer2.close();
        System.out.println("written");*/
    }
}

