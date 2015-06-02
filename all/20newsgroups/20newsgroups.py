#coding:utf-8
__author__ = 'datawlb'
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_extraction.text import HashingVectorizer
from sklearn.feature_selection import SelectKBest, chi2
from sklearn.neighbors import KNeighborsClassifier
from sklearn import metrics
from sklearn.naive_bayes import BernoulliNB, MultinomialNB
from time import time
import logging, sklearn, sklearn.datasets

import multiNBayes



def get_data():
    dir_path = 'C:\\Users\\Administrator\\Desktop\\used\\project\\python\\Movielens\\20newsgroups-classify-text-master\\dataset'
    files = sklearn.datasets.load_files(dir_path)
    X_train, X_test, y_train, y_test = sklearn.cross_validation.train_test_split(files.data, files.target, test_size=0.3)

    return X_train, X_test, y_train, y_test, files.target_names


if __name__ == '__main__':
    use_hashing = True
    select_chi2 = True
    X_train, X_test, y_train, y_test, target_names = get_data()
    print("%d rows: "  %len(y_train) + "\n")
    print("%d features:" % len(X_train[0] + "\n"))
    if use_hashing:
        vectorizer = HashingVectorizer(stop_words='english', non_negative=True,
                                   n_features=400)
        X_train = vectorizer.transform(X_train)
        X_test = vectorizer.transform(X_test)
    else:
        vectorizer = TfidfVectorizer(sublinear_tf=True, max_df=0.5,
                                 stop_words='english')
        X_train = vectorizer.fit_transform(X_train)
        X_test = vectorizer.fit_transform(X_test)

    # feature select
    if select_chi2:
        ch2 = SelectKBest(chi2, k=30)
        X_train = ch2.fit_transform(X_train, y_train)
        X_test = ch2.transform(X_test)

    # classify, train model
    mnb = MultinomialNB(alpha=1)
    mnb.fit(X_train, y_train)
    mnb_result = mnb.predict(X_test)
    scoremnb = metrics.accuracy_score(y_test, mnb_result)

    nb = multiNBayes.multiNByes(X_train.toarray(), y_train)
    resnb = nb.predict(X_test.toarray())
    scorenb = metrics.accuracy_score(y_test, resnb)
# scoremnb is better than scorenb,is skit-learn's multinominalNB do some other work?

    t0 = time()
    knn1 = KNeighborsClassifier(n_neighbors=10, algorithm="ball_tree")
    knn1.fit(X_train, y_train)
    pre_result1 = knn1.predict(X_test)
    t1 = time()-t0

    t0 = time()
    knn2 = KNeighborsClassifier(n_neighbors=10, algorithm="kd_tree")
    knn2.fit(X_train, y_train)
    pre_result2 = knn1.predict(X_test)
    t2 = time()-t0
    #
    score1 = metrics.accuracy_score(y_test, pre_result1)
    score2 = metrics.accuracy_score(y_test, pre_result2)
    print("%d balltree: "  %score1 + "\n")
    print("%d kdtree: "  %score2 + "\n")
# when dimension is high should use balltree.
    print(" ")