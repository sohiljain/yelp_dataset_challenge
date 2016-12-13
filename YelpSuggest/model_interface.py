import pandas as pd
import numpy as np
import string
import gmplot

from sklearn.metrics import confusion_matrix
from sklearn.metrics import roc_auc_score, roc_curve
from sklearn import metrics
import pickle
TemplatePath = "/home/prerit/Yelp!/YelpRecommendation/templates/"

def predict_best_restraunt(geoloc = 'PA'):
    data = get_pkl("data.pkl")
    classifier = get_pkl("model.pkl")
    cities = get_pkl("cities.pkl")
    business_ids = get_pkl("business_ids.pkl")
    predictions = classifier.predict(data)
    probability = classifier.predict_proba(data)[:,1]
    business_predictions = pd.DataFrame({0:business_ids,"Predictions":predictions,"Proba":probability})
    business_predictions.sort_values(by= "Proba",axis = 0, inplace=True,ascending = False)
    cities_business = cities.merge(business_predictions, on= 0)
    subset_business = cities_business.iloc[np.where((cities_business.iloc[:,3] == geoloc) & (cities_business.iloc[:,-1] == 1))]
    state = {'AZ':'Arizona','PA':'Pittsburg'}
    outputFile = plot_on_map(list(np.array(subset_business.iloc[:150,1])),list(np.array(subset_business.iloc[:200,2])),state[geoloc])
    return outputFile

def plot_on_map(lat,long,geoCode):
    gmap = gmplot.GoogleMapPlotter.from_geocode(geoCode)
    gmap.scatter(lat, long, 'red', size=35, marker=False)
    gmap.draw(TemplatePath+"restraunt_location_%s.html" %geoCode)
    outputFile = "restraunt_location_%s.html" %geoCode
    return outputFile


def get_pkl(filename):
    with open(filename, 'rb') as input:
        return pickle.load(input)




predict_best_restraunt(geoloc = "AZ")
