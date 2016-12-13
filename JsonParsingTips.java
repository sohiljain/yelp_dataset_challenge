/**
 * authors: Sohil Jain, Avruti Srivastava
 */
package yelp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

//import org.apache.commons.io.FileUtils;

public class JsonParsingTips {
//    public static void main(String myHelpers[]) throws JSONException, IOException {
    public static HashMap<String, String> tips() throws JSONException, IOException {

        BufferedReader read_business = new BufferedReader(new FileReader(new File("C:\\Users\\Disha\\workspace\\yelp\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_business.json")));
        BufferedReader read_tip = new BufferedReader(new FileReader(new File("C:\\Users\\Disha\\workspace\\yelp\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_tip.json")));
        // Categories for each BusinessID
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

            HashMap<String, String> tipMap = new HashMap<String, String>();
            HashMap<String, String> tipMap2 = new HashMap<String, String>();
            //Business-tip Map
            jsonString = "";
            while ((jsonString = read_tip.readLine()) != null) {
            	if(tipMap.size()<19000){
                JSONObject obj = new JSONObject(jsonString);
                String businessId = obj.getString("business_id");
                String tip = obj.getString("text");
                if (tipMap.containsKey(businessId))
                    tipMap.put(businessId, tipMap.get(businessId) + " " + tip);
                else
                    tipMap.put(businessId, tip);
            	}
            	if(tipMap.size()==19000&&tipMap2.size()<6000)
            	{
            		JSONObject obj = new JSONObject(jsonString);
                    String businessId = obj.getString("business_id");
                    String tip = obj.getString("text");
                    tip=tip.replaceAll("\n", " ");
                    if (tipMap2.containsKey(businessId))
                        tipMap2.put(businessId, tipMap2.get(businessId) + " " + tip);
                    else
                        tipMap2.put(businessId, tip);
                	
            	}
            
            }
          
          //Category-Tip map  
            for (String bid : categoryMap.keySet()) {
                if (tipMap.containsKey(bid)) {
                    for (String cat : categoryMap.get(bid)) {
                        if (map.containsKey(cat))
                            map.put(cat, map.get(cat) + "\n" + tipMap.get(bid));
                        else
                            map.put(cat, tipMap.get(bid));
                    }
                }
            }
            File f1 = new File("C:\\Users\\Disha\\Desktop\\tiptest.txt");
            FileOutputStream fs1 = new FileOutputStream(f1);
            PrintWriter w1 = new PrintWriter(fs1);

            one: for (String b_id : categoryMap.keySet()) {
             if (tipMap2.get(b_id)!=null ) {
              w1.println(b_id + "\u0001" + categoryMap.get(b_id) + "\u0001" + tipMap2.get(b_id));
              
             }
            }

            w1.flush();
            w1.close();
            fs1.close();
            System.out.println("Done!");

        } catch (Exception e) {

        }
        return map;
    }

}
