package yelp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JP {

	public static void main(String[] args) throws IOException, JSONException {
		// TODO Auto-generated method stub
    	BufferedReader read_business = new BufferedReader(new FileReader(new File("C:\\Users\\Disha\\workspace\\yelp\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_business.json")));
        BufferedReader read_review = new BufferedReader(new FileReader(new File("C:\\Users\\Disha\\workspace\\yelp\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_review.json")));
        BufferedReader read_tip = new BufferedReader(new FileReader(new File("C:\\Users\\Disha\\workspace\\yelp\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_tip.json")));
        BufferedReader read_user=new BufferedReader(new FileReader(new File("C:\\Users\\Disha\\workspace\\yelp\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_user.json"))); 

     //   BufferedWriter bw = new BufferedWriter(new FileWriter(new File("C:\\Users\\Disha\\Desktop\\yelp_index\\temp.tsv")));
        

            HashMap<String, ArrayList<String>> categoryMap = new HashMap<String, ArrayList<String>>();
            String jsonString = "";
            while ((jsonString = read_business.readLine()) != null) {
                JSONObject obj = new JSONObject(jsonString);
                JSONArray catArray = obj.getJSONArray("categories");
                int flag=0;
                for(int i=0;i<catArray.length();i++)
                {
                	if(catArray.getString(i).equals("Restaurants"))
                	{
                		flag=1;
                		break;
                	}
                }
                HashMap<String,ArrayList<Integer>> AttributeMap=new HashMap<String,ArrayList<Integer>>();
                String attributes[]={"Accepts Credit Cards","Alcohol","Delivery","Drive-Thru","Good for Groups","Good for Kids","Noise Level","Outdoor Seating","Price Range","Waiter Service","Takes Reservations","Take-out"};
                if(obj.getString("state").equals("PA")&& flag==1)
                {
                	String businessId = obj.getString("business_id");
                	 ArrayList<String> categories = new ArrayList<>();
                     for (int i = 0; i < catArray.length(); i++) {
                         categories.add(catArray.getString(i));
                     }
                	categoryMap.put(businessId, categories);
                	ArrayList<Integer> atttemp = null;
                	if(obj.get("attributes")!=null)
                	{
                		JSONObject o=(JSONObject) obj.get("attributes");
                		atttemp=new ArrayList<Integer>();
                		for(int i=0;i<attributes.length;i++)
                		{
                			String a=attributes[i];
                			switch(i)
                			{
                			case 0:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals(true))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 1:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals("full_bar"))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 2:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals(true))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 3:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals(true))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 4:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals(true))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 5:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals(true))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 6:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals("very_loud"))
                						atttemp.add(3);
                					else if(o.get(a).equals("loud"))
                						atttemp.add(2);
                					else if(o.get(a).equals("average"))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 7:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals(true))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 8:
                			{
                				if(o.has(a))
                				{
                					
                					if(!o.get(a).equals(0))
                						atttemp.add((Integer)o.get(a));
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 9:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals(true))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 10:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals(true))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			case 11:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals(true))
                						atttemp.add(1);
                					else
                						atttemp.add(0);
                				}
                				else
                				atttemp.add(0);
                				break;
                			}
                			default:
                				break;

                			}
                		}
                		
                		
                	}
                	AttributeMap.put(businessId, atttemp);
                	
                	
                }
               
            
            }
            String jsonString1 = "";
            HashMap<String,Integer> ratingMap=new HashMap<String,Integer>(); 
            while ((jsonString1 = read_review.readLine()) != null) {
                JSONObject obj = new JSONObject(jsonString1);
                String businessId = obj.getString("business_id");
                if(categoryMap.containsKey(businessId))
                {
                	String key=businessId+":"+obj.getString("user_id");
                	ratingMap.put(key,obj.getInt("stars"));
                }
            }
            String jsonString2 = "";
            HashMap<String,String> tipMap=new HashMap<String,String>(); 
            while ((jsonString2 = read_tip.readLine()) != null) {
                JSONObject obj = new JSONObject(jsonString2);
                String businessId = obj.getString("business_id");
                if(categoryMap.containsKey(businessId))
                {
                	String key=businessId+":"+obj.getString("user_id");
                	tipMap.put(key,obj.getString("text"));
                }
            }

	}

}
