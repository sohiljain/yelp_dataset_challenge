"""
Converting json object to a pandas Series object
for faster retrieval. This way we can train the
model example by example.

@Author : Prerit Anwekar

"""

import json
import pandas as pd
import gmplot
                                                                                                                                                                                                                                                                                                
class InnerDataFrame:
    def __init__(self,value):
        self.value = value

def load_json_multiple(segments):
    chunk = ""
    for segment in segments:
        chunk += segment
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                try:
            yield json.loads(chunk)
            chunk = ""
        except ValueError:
            pass

def load_datapoint(fileName):
    data = pd.DataFrame()
    with open('../%s' %fileName) as f:
        i = 0
        for parsed_json in load_json_multiple(f):
            for column in parsed_json.keys():
                if isinstance(parsed_json[column],dict):
                    parsed_json[column] = InnerDataFrame(pd.DataFrame([parsed_json[column]]))
                elif isinstance(parsed_json[column],list):
                    parsed_json[column] = [",".join([str(x) for x in parsed_json[column]])]
            data_point = pd.Series(parsed_json)
            yield data_point

def plot_on_map(lat,long,geoCode):
    gmap = gmplot.GoogleMapPlotter.from_geocode(geoCode)
    gmap.scatter(lat, long, 'red', size=20, marker=False)
    gmap.draw("restraunt_location.html")
    
lats_pa,long_pa = [],[]
business_reviews = []
i = 0
for business in load_datapoint("yelp_academic_dataset_business.json"):
    print(business.head())
    if business['state'] == 'PA':
        for review in load_datapoint("yelp_academic_dataset_review.json"):
            print(review)
            if review['business_id'] == business['business_id']:
                business_reviews.append(review['text'])
            i += 1
        lats_pa.append(business['latitude'])
        long_pa.append(business['longitude'])
plot_on_map(lats_pa,long_pa,'Pittsburg')
