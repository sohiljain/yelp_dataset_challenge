import pandas as pd
import numpy as np
from nltk.classify import PositiveNaiveBayesClassifier
import string

# Read all the text files.
ratings = pd.read_csv("../Dataset/rating.txt",header=None,sep=":\|",engine='python')
cities = pd.read_csv("../Dataset/city.txt",header=None,sep=":\|",engine='python')
tips = pd.read_csv("../Dataset/tip.txt",header=None,sep=":\|",engine='python')

categories = pd.read_csv("../Dataset/category.txt",
                         header=None,sep=",",error_bad_lines=False)
categories = categories.drop(1,axis=1) # There are still NaNs


combined_tips_ratings = tips.merge(ratings, on=[0,1], how='inner')
combined_tips_ratings["Response"] = np.where(combined_tips_ratings.iloc[:,-1]>4.0,'pos','unknown')
pos_responses = combined_tips_ratings.iloc[np.where(combined_tips_ratings['Response'] == 'pos')]
unknown_responses = combined_tips_ratings.iloc[np.where(combined_tips_ratings['Response'] == 'unknown')]
train_pos_responses = pos_responses.loc[:,['2_x']]
train_unknown_responses = unknown_responses.loc[:,['2_x']]
positive = train_pos_responses['2_x'].tolist()
unlabelled = train_unknown_responses['2_x'].tolist()

def create_features(text):
    # Remove all the punctuations.
    table = str.maketrans({key: None for key in string.punctuation})
    text = text.translate(table)  
    words = text.lower().split()
    # Create Bag of words.
    dictionary_words = dict(('contains(%s)' %w ,True) for w in words if len(w) > 2)
    return dictionary_words

pos_features = list(map(create_features, positive))
unknown_features = list(map(create_features, unlabelled))

# Learn the model just based on positive Naive Bayes Classifier.
classifier = PositiveNaiveBayesClassifier.train(pos_features,
                                                 unknown_features)
#print(classifier.classify(create_features()))

for tip in tips.iloc[:,2].tolist():
    try:
        tips["class"] = classifier.classify(create_features(tip))
    except AttributeError:
        pass
print(tips)
print(np.unique(tips.loc[:,'class']))

