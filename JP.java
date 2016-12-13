/**
 * Author: Disha Wagle
 */
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
	
	//parses data from json files used in Task 2

	public static void main(String[] args) throws IOException, JSONException {
		// TODO Auto-generated method stub
    	BufferedReader read_business = new BufferedReader(new FileReader(new File("C:\\Users\\Disha\\workspace\\yelp\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_business.json")));
        BufferedReader read_review = new BufferedReader(new FileReader(new File("C:\\Users\\Disha\\workspace\\yelp\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_review.json")));
        BufferedReader read_tip = new BufferedReader(new FileReader(new File("C:\\Users\\Disha\\workspace\\yelp\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_tip.json")));
        BufferedReader read_user=new BufferedReader(new FileReader(new File("C:\\Users\\Disha\\workspace\\yelp\\yelp_dataset_challenge_academic_dataset\\yelp_academic_dataset_user.json"))); 
        final String COMMA_DELIMITER = ",";
        final String NEW_LINE_SEPARATOR = "\n";
        FileWriter cb = new FileWriter("C:\\Users\\Disha\\Desktop\\Matrices\\businessId_categories.csv");
        FileWriter ba = new FileWriter("C:\\Users\\Disha\\Desktop\\Matrices\\businessId_attributes.csv");
        FileWriter ubr = new FileWriter("C:\\Users\\Disha\\Desktop\\Matrices\\userId_businessId_ratings.csv");
        FileWriter bw = new FileWriter("C:\\Users\\Disha\\Desktop\\Matrices\\businessId_words.csv");
        FileWriter uw = new FileWriter("C:\\Users\\Disha\\Desktop\\Matrices\\userId_words.csv");

     //   BufferedWriter bw = new BufferedWriter(new FileWriter(new File("C:\\Users\\Disha\\Desktop\\yelp_index\\temp.tsv")));
        
        String att_names="Business ID";
        ArrayList<String> bidseq=new ArrayList<String>();
        String attributes[]={"Accepts Credit Cards","Alcohol","Delivery","Drive-Thru","Good for Groups","Good for Kids","Noise Level","Outdoor Seating","Price Range","Waiter Service","Takes Reservations","Take-out"};
        for(int i=0;i<attributes.length;i++)
        {
        	if(i!=attributes.length-1)
        	att_names+=attributes[i]+",";
        	else
        		att_names+=attributes[i];
        }
        ba.append(att_names.toString());
        ba.append(NEW_LINE_SEPARATOR);
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
              //  String attributes[]={"Accepts Credit Cards","Alcohol","Delivery","Drive-Thru","Good for Groups","Good for Kids","Noise Level","Outdoor Seating","Price Range","Waiter Service","Takes Reservations","Take-out"};
                if(obj.getString("state").equals("PA")&& flag==1)
                {
                	
                	String businessId = obj.getString("business_id");
                	bidseq.add(businessId);
                	String category=businessId+",";
                	 ArrayList<String> categories = new ArrayList<>();
                     for (int i = 0; i < catArray.length(); i++) {
                    	 if(i!=catArray.length()-1)
                         	category+=catArray.getString(i)+",";
                         	else
                         		category+=catArray.getString(i);
                         categories.add(catArray.getString(i));
                     }
                     cb.append(category.toString());
                     cb.append(NEW_LINE_SEPARATOR);
                	categoryMap.put(businessId, categories);
                	ArrayList<Integer> atttemp = null;
                	if(obj.get("attributes")!=null)
                	{
                		
                		JSONObject o=(JSONObject) obj.get("attributes");
                		String att=businessId;
                		atttemp=new ArrayList<Integer>();
                		for(int i=0;i<attributes.length;i++)
                		{
                			String a=attributes[i];
                			switch(i)
                			{
                			case 0:
                			case 2:
                			case 3:
                			case 4:
                			case 5:
                			case 7:
                			case 9:
                			case 10:
                			case 11:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals(true))
                						att+="1";
                						//atttemp.add(1);
                					else
                						att+="0";
                						//atttemp.add(0);
                				}
                				else
                					att+="0";
                					//atttemp.add(0);
                				break;
                			}
                			case 1:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals("full_bar"))
                						att+="1";
                						//atttemp.add(1);
                					else
                						att+="0";
                						//.add(0);
                				}
                				else
                					att+="0";
                					//atttemp.add(0);
                				break;
                			}
                			case 6:
                			{
                				if(o.has(a))
                				{
                					if(o.get(a).equals("very_loud"))
                						att+="3";
                						//atttemp.add(3);
                					else if(o.get(a).equals("loud"))
                						att+="2";
                						//atttemp.add(2);
                					else if(o.get(a).equals("average"))
                						att+="1";
                						//atttemp.add(1);
                					else
                						att+="0";
                						//atttemp.add(0);
                				}
                				else
                					att+="0";
                					//atttemp.add(0);
                				break;
                			}
                			case 8:
                			{
                				if(o.has(a))
                				{
                					
                					if(!o.get(a).equals(0))
                						att+=String.valueOf(o.get(a));
                						//atttemp.add((Integer)o.get(a));
                					else
                						att+="0";
                						//atttemp.add(0);
                				}
                				else
                					att+="0";
                					//atttemp.add(0);
                				break;
                			}
                			default:
                				break;

                			}
                			if(i!=attributes.length-1)
                				att+=',';
                		}
                		ba.append(att.toString());
                		ba.append(NEW_LINE_SEPARATOR);
                	}
                	//AttributeMap.put(businessId, atttemp);
                	
                	
                }
               
            
            }
            String jsonString1 = "";
            String bid="";
            HashMap<String,ArrayList<Integer>> ratingMap=new HashMap<String,ArrayList<Integer>>(); 
            HashMap<String,ArrayList<String>> ubmap=new HashMap<String,ArrayList<String>>();
            while ((jsonString1 = read_review.readLine()) != null) {
                JSONObject obj = new JSONObject(jsonString1);
                String businessId = obj.getString("business_id");
                ArrayList<String> btemp=null;
                ArrayList<Integer> utemp=null;
                if(categoryMap.containsKey(businessId)&&obj.getInt("stars")>=0&&obj.getInt("stars")<=5)
                {
                	bid+=businessId+",";
                	if(!ubmap.containsKey(obj.getString("user_id")))
                	{
                		btemp=new ArrayList<String>();
                		btemp.add(businessId);
                		ubmap.put(obj.getString("user_id"), btemp);
                	}
                	else
                	{
                		btemp=ubmap.get(obj.getString("user_id"));
                		btemp.add(businessId);
                		ubmap.put(obj.getString("user_id"), btemp);
                	}
                	if(!ratingMap.containsKey(obj.getString("user_id")))
                	{
                		utemp=new ArrayList<Integer>();
                		utemp.add(obj.getInt("stars"));
                		ratingMap.put(obj.getString("user_id"), utemp);
                	}
                	else
                	{
                		utemp=ratingMap.get(obj.getString("user_id"));
                		utemp.add(obj.getInt("stars"));
                		ratingMap.put(obj.getString("user_id"), utemp);
                	}
                	
                	//ratingMap.put(key,obj.getInt("stars"));
                }
            }
            for(String i:bidseq)
            {
            	bid+=i+",";
            }
            bid=bid.substring(0,bid.length()-1);
            ubr.append(bid.toString());
            ubr.append(NEW_LINE_SEPARATOR);
            for(String i:ratingMap.keySet())
            {
            	String urate=i+",";
            	ArrayList<String> b=ubmap.get(i);
            	ArrayList<Integer> r=ratingMap.get(i);
            	for(int j=0;j<bidseq.size();j++)
            	{
            		if(b.contains(bidseq.get(j)))
            		{
            			int index=b.indexOf(bidseq.get(j));
            			urate+=r.get(index)+",";
            		}
            		else
            		{
            			urate+="0"+",";
            		}
            	}
            	urate=urate.substring(0, urate.length()-1);
            	ubr.append(urate.toString());
                ubr.append(NEW_LINE_SEPARATOR);

            }
            String jsonString2 = "";
          
            HashMap<String,ArrayList<String>> usertipMap=new HashMap<String,ArrayList<String>>();
            HashMap<String,ArrayList<String>> businesstipMap=new HashMap<String,ArrayList<String>>();
            while ((jsonString2 = read_tip.readLine()) != null) {
                JSONObject obj = new JSONObject(jsonString2);
                String businessId = obj.getString("business_id");
                String userId=obj.getString("user_id");
                ArrayList<String> utemp=null;
                ArrayList<String> btemp=null;
                if(categoryMap.containsKey(businessId))
                {
                	if(!usertipMap.containsKey(userId))
                	{
                		utemp=new ArrayList<String>();
                		utemp.add(obj.getString("text"));
                		usertipMap.put(userId, utemp);
                	}
                	else
                	{
                		utemp=usertipMap.get(userId);
                		utemp.add(obj.getString("text"));
                		usertipMap.put(userId, utemp);
                	}
                	if(!businesstipMap.containsKey(businessId))
                	{
                		btemp=new ArrayList<String>();
                		btemp.add(obj.getString("text"));
                		businesstipMap.put(businessId, btemp);
                	}
                	else
                	{
                		btemp=businesstipMap.get(businessId);
                		btemp.add(obj.getString("text"));
                		businesstipMap.put(businessId, btemp);
                	}
                	
                }
            }
            for(String i:bidseq)
            {
            	if(businesstipMap.containsKey(i))
            	{
            	String bt=i+",";
            	for(String s:businesstipMap.get(i))
            		bt+=s+" ";
            	bw.append(bt.toString());
            	bw.append(NEW_LINE_SEPARATOR);
            	}
            	else
            	{
            		bw.append((i+","+" ").toString());
            		bw.append(NEW_LINE_SEPARATOR);
            	}
            }
            for(String i:usertipMap.keySet())
            {
            	if(usertipMap.containsKey(i))
            	{
            	String bt=i+",";
            	for(String s:usertipMap.get(i))
            		bt+=s+" ";
            	uw.append(bt);
            	uw.append(NEW_LINE_SEPARATOR);
            	}
            	else
            	{
            		bw.append((i+","+" ").toString());
            		bw.append(NEW_LINE_SEPARATOR);
            	}
            }

            bw.flush();
            uw.flush();
            cb.flush();
            ba.flush();
            ubr.flush();
            cb.close();
            uw.close();
            bw.close();
            ba.close();
            ubr.close();

	}

}
