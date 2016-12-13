/**
 * authors: Disha Wagle, Avruti Srivastava
 */
package yelp;

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

//import java.io.IOException;

/**
 * Created by sjain on 9/27/16.
 */
public class generateIndexTip {

    public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException, JSONException {

        //Set below three variables docDir, indexDir, analyzer according to the analyzer to be used and path settings.
        Path indexDir = Paths.get("C:\\Users\\Disha\\Desktop\\yelp_index2");
        CustomAnalyzer analyzer = CustomAnalyzer.builder(Paths.get("C:\\Users\\Disha\\Desktop")).withTokenizer("standard").addTokenFilter("standard").addTokenFilter("lowercase").addTokenFilter("stop", "ignoreCase", "false").addTokenFilter("keepWord","words","C:\\Users\\Disha\\Desktop\\keepwords.txt","ignoreCase","false").addTokenFilter("porterstem").build();
        Directory fsDir = FSDirectory.open(indexDir);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter indexWriter = new IndexWriter(fsDir, iwc);

        HashMap<String, String> map = JsonParsingTips.tips();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String category = entry.getKey();
            String tips = entry.getValue();

            //Create a new document
            Document d = new Document();

            //Write all the fields to the Document
            d.add(new StringField("category", category, Field.Store.YES));
            d.add(new TextField("tip", tips, Field.Store.YES));

            //Write the document into indexWriter
            indexWriter.addDocument(d);

        }

        //Print number of documents in the generated index
        int numDocs = indexWriter.numDocs();
        System.out.println(numDocs);

        indexWriter.forceMerge(1);
        indexWriter.commit();
        indexWriter.close();
        PrintWriter writer = new PrintWriter("C:\\Users\\Disha\\Desktop\\termstips1.txt", "UTF-8");
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(("C:\\Users\\Disha\\Desktop\\yelp_index2"))));
		Terms vocabulary = MultiFields.getTerms(reader, "tip");
		TermsEnum iterator = vocabulary.iterator();
		BytesRef byteRef = null;
		System.out.println("\n*******Vocabulary-Start**********");
		while((byteRef = iterator.next()) != null) {
		String term = byteRef.utf8ToString();
		writer.println(term+"\t");
		}
		reader.close();
		writer.close();
    }
}
