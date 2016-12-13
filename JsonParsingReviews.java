/**
 * authors: Sohil Jain, Avruti Srivastava
 */
package search_project;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

//import org.apache.commons.io.FileUtils;

public class JsonParsingReviews {
	//returns map of reviews for each category 
    public static HashMap<String, String> reviews() throws JSONException, IOException {

        BufferedReader read_business = new BufferedReader(new FileReader(new File("C:\\Users\\avruti\\Desktop\\Sem3\\Search\\project\\data\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_business.json")));
        BufferedReader read_review = new BufferedReader(new FileReader(new File("C:\\Users\\avruti\\Desktop\\Sem3\\Search\\project\\data\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_review.json")));

        HashMap<String, String> map = null;
        try {

            HashMap<String, ArrayList<String>> categoryMap = new HashMap<String, ArrayList<String>>();
            String jsonString = "";
            while ((jsonString = read_business.readLine()) != null) {
                JSONObject obj = new JSONObject(jsonString);
                String businessId = obj.getString("business_id");
                JSONArray catArray = obj.getJSONArray("categories");
                ArrayList<String> categories = new ArrayList<>();
                for (int i = 0; i < catArray.length(); i++) {
                    categories.add(catArray.getString(i));
                }
                categoryMap.put(businessId, categories);
            }

            Set<String> distinctCategories = categoryMap.values().stream().flatMap(x -> x.stream()).distinct().collect(Collectors.toSet());

            map = new HashMap<>();

            for (String distinctCategory : distinctCategories) {
                map.put(distinctCategory, "");
            }

            HashMap<String, String> reviewMap = new HashMap<String, String>();  //data used to create index
            HashMap<String, String> reviewMap2 = new HashMap<String, String>(); //data to make predictions

            jsonString = "";
            while ((jsonString = read_review.readLine()) != null) {
            	if(reviewMap.size()<15000){
                JSONObject obj = new JSONObject(jsonString);
                String businessId = obj.getString("business_id");
                String review = obj.getString("text");
                if (reviewMap.containsKey(businessId))
                    reviewMap.put(businessId, reviewMap.get(businessId) + " " + review.replaceAll("//s"," "));
                else
                    reviewMap.put(businessId, review.replaceAll("//s"," "));
            	}
            	if(reviewMap.size()==15000&&reviewMap2.size()<5000)
            	{
            		JSONObject obj = new JSONObject(jsonString);
                    String businessId = obj.getString("business_id");
                    String review = obj.getString("text");
                    review=review.replaceAll("\n", " ");
                    if (reviewMap2.containsKey(businessId))
                        reviewMap2.put(businessId, reviewMap2.get(businessId) + " " + review.replaceAll("//s"," "));
                    else
                        reviewMap2.put(businessId, review.replaceAll("//s"," "));
                	
            	}
            
            }
            for (String bid : categoryMap.keySet()) {
                if (reviewMap.containsKey(bid)) {
                    for (String cat : categoryMap.get(bid)) {
                        if (map.containsKey(cat))
                            map.put(cat, map.get(cat) + "\n" + reviewMap.get(bid));
                        else
                            map.put(cat, reviewMap.get(bid));
                    }
                }
            }
             
            File f1 = new File("C:\\Users\\avruti\\Desktop\\reviewtest.txt");
            FileOutputStream fs1 = new FileOutputStream(f1);
            PrintWriter w1 = new PrintWriter(fs1);

            one: for (String b_id : categoryMap.keySet()) {
             if (reviewMap2.get(b_id)!=null ) {
              w1.println(b_id + "\u0001" + categoryMap.get(b_id) + "\u0001" + reviewMap2.get(b_id));
              
             }
            }

            w1.flush();
            w1.close();
            fs1.close();
            System.out.println("File Write Done!");

        } catch (Exception e) {

        }
        return map;
    }

}

