import pandas as pd
import numpy as np
import string
import gmplot

from sklearn.metrics import confusion_matrix
from sklearn.metrics import roc_auc_score, roc_curve
from sklearn import metrics
from sklearn.metrics import precision_recall_fscore_support
import pickle


# Read all the text files.
ratings = pd.read_csv("../Dataset/rating.csv",header=None,sep=";;;",engine='python')

ratings = ratings.iloc[np.where((ratings.iloc[:,2]!=3))]
ratings = ratings.iloc[np.where((ratings.iloc[:,2]!=4))]
cities = pd.read_csv("../Dataset/city.csv",header=None,sep=";;;",engine='python')
tips = pd.read_csv("../Dataset/tip.csv",header=None,sep= ";;;",engine='python')

categories = pd.read_csv("../Dataset/category.csv",
                         header=None,sep=",",error_bad_lines=False)
categories = categories.drop(1,axis=1)
combined_tips_ratings = tips.merge(ratings, on=[0,1], how='inner')                               
pos = combined_tips_ratings.iloc[np.where(combined_tips_ratings.iloc[:,-1]==5.0)]
neg = combined_tips_ratings.iloc[np.where(combined_tips_ratings.iloc[:,-1]<=2.0)]
pos["Response"] = 1
neg["Response"] = 0
pos = pos[:6000]
neg = neg[:6000]
business_ids = combined_tips_ratings.iloc[:,0]
answer = []
for df in [pd.concat([pos,neg])]:
    ## 3. Tokenization ##

    tokenized_reviews = []

    for value in df.loc[:,['2_x']]['2_x'].tolist():
        tokenized_reviews.append(value.split(" "))


    ## 4. Preprocessing ##

    punctuation = [p for p in string.punctuation]
    clean_tokenized = []
    for item in tokenized_reviews:
        tokens = []
        for token in item:
            token = token.lower()
            for punc in punctuation:
                token = token.replace(punc, "")
            tokens.append(token)
        clean_tokenized.append(tokens)

    ## 5. Assembling a matrix ##

    unique_tokens = []
    single_tokens = []

    for tokens in clean_tokenized:
        for token in tokens:
            if token not in single_tokens:
                single_tokens.append(token)
            elif token not in unique_tokens:
                unique_tokens.append(token)

    counts = pd.DataFrame(0, index = np.arange(len(clean_tokenized)), columns = unique_tokens)
    ## 6. Counting tokens ##

    # clean_tokenized and counts have been loaded in.
    for i, item in enumerate(clean_tokenized):
        for token in item:
            if token in unique_tokens:
                counts.iloc[i][token] += 1
    
    ## 7. Removing extraneous columns ##

    # clean_tokenized and counts have been loaded in.# clean_tokenized and counts have been loaded in.
    word_counts = counts.sum(axis=0)
    counts = counts.loc[:,(word_counts >= 5) & (word_counts <= 450)]
    answer.append(counts)

train = answer[0]
train.Response = pd.concat([pos,neg]).Response

train.iloc[np.random.permutation(len(train))]
X,y = train,train.Response

shuffler = np.arange(X.shape[0])
np.random.shuffle(shuffler)
X = X.iloc[shuffler,:]
y = y.iloc[shuffler]
business_ids = business_ids.iloc[shuffler]

X_train,y_train = X.iloc[:7000],y.iloc[:7000]
X_test, y_test = X.iloc[7000:],y.iloc[7000:]
model_scores = []
scores_f1_pre_re = []
## 9. Making predictions ##


from sklearn.linear_model import LogisticRegression

from sklearn.tree import DecisionTreeClassifier
dt = DecisionTreeClassifier(criterion = 'gini', max_depth = 70, random_state = 1)
dt.fit(X_train,y_train)
y_pred = dt.predict(X_test)
score = roc_auc_score(y_test, y_pred)
print("DT: Area under ROC {0}".format(score))
model_scores.append(score)
s = precision_recall_fscore_support(y_test, y_pred)
scores_f1_pre_re.append(s)

from sklearn.ensemble import AdaBoostClassifier
ada = AdaBoostClassifier(base_estimator = dt, n_estimators = 70, learning_rate = 0.25, random_state = 1)
ada.fit(X_train,y_train)
y_pred = ada.predict(X_test)
score = roc_auc_score(y_test, y_pred)
print("Ada: Area under ROC {0}".format(score))
model_scores.append(score)
s = precision_recall_fscore_support(y_test, y_pred)
scores_f1_pre_re.append(s)

"""
"""
from sklearn.ensemble import BaggingClassifier
bag = BaggingClassifier(base_estimator = dt, n_estimators = 75, max_samples = 1.0, max_features = 1.0,
                       bootstrap = True, bootstrap_features = False, n_jobs = 1, random_state = 1)
bag.fit(X_train,y_train)
y_pred = bag.predict(X_test)
score = roc_auc_score(y_test, y_pred)
print("Bag: Area under ROC {0}".format(score))
model_scores.append(score)
s = precision_recall_fscore_support(y_test, y_pred)
scores_f1_pre_re.append(s)

from sklearn.ensemble import ExtraTreesClassifier
etc = ExtraTreesClassifier(n_estimators = 150, random_state = 20, n_jobs = -1)
etc.fit(X_train,y_train)
y_pred = etc.predict(X_test)
score = roc_auc_score(y_test, y_pred)
print("ET: Area under ROC {0}".format(score))
model_scores.append(score)
s = precision_recall_fscore_support(y_test, y_pred)
scores_f1_pre_re.append(s)

import xgboost as xgb
gbm = xgb.XGBClassifier(max_depth=500, n_estimators=150, learning_rate=0.15, colsample_bytree = 0.35)
gbm.fit(X_train, y_train)

y_pred = gbm.predict(X_test)
score = roc_auc_score(y_test, y_pred)
model_scores.append(score)
print("XGB: Area under ROC {0}".format(score))
s = precision_recall_fscore_support(y_test, y_pred)
scores_f1_pre_re.append(s)

lr = LogisticRegression(penalty='l1',C=0.1,fit_intercept = True)
lr.fit(X_train, y_train)
y_pred = lr.predict(X_test)
score = roc_auc_score(y_test, y_pred)
model_scores.append(score)
print("LR: Area under ROC {0}".format(score))
s = precision_recall_fscore_support(y_test, y_pred)
scores_f1_pre_re.append(s)

with open("model.pkl",'wb') as output:
    pickle.dump(lr,output,pickle.HIGHEST_PROTOCOL)

with open('data.pkl','wb') as output:
    pickle.dump(X,output,pickle.HIGHEST_PROTOCOL)

with open('business_ids.pkl','wb') as output:
    pickle.dump(business_ids,output,pickle.HIGHEST_PROTOCOL)

with open('cities.pkl','wb') as output:
    pickle.dump(cities,output,pickle.HIGHEST_PROTOCOL)

## This function is the interface to other parts of data. One can choose which
## classifier to use. ## Only of Developer's use.

def predict_best_restraunt(data,classifier = lr,geoloc = 'PA'):
    predictions = classifier.predict(data)
    probability = classifier.predict_proba(data)[:,1]
    business_predictions = pd.DataFrame({0:business_ids,"Predictions":predictions,"Proba":probability})
    business_predictions.sort_values(by= "Proba",axis = 0, inplace=True,ascending = False)
    cities_business = cities.merge(business_predictions, on= 0)
    subset_business = cities_business.iloc[np.where((cities_business.iloc[:,3] == geoloc) & (cities_business.iloc[:,-1] == 1))]
    state = {'AZ':'Arizona','PA':'Pittsburg'}
    plot_on_map(list(np.array(subset_business.iloc[:200,1])),list(np.array(subset_business.iloc[:200,2])),state[geoloc])

def plot_on_map(lat,long,geoCode):
    gmap = gmplot.GoogleMapPlotter.from_geocode(geoCode)
    gmap.scatter(lat, long, 'red', size=20, marker=False)
    gmap.draw("restraunt_location_%s.html" %geoCode)

predict_best_restraunt(X)
