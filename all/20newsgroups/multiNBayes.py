__author__ = 'datawlb'
import numpy as np
class multiNByes:
    '''
    tr_matrix: samples * features
    tr_y: label

    should use log
    '''
    def __init__(self, tr_matrix, tr_y):
        self.tr_matrix = tr_matrix
        self.tr_y = tr_y
        self.labelCount = {}
        self.labelPriproDic = {}
        labelDic = {}
        for idx in range(0, len(tr_y)):
            if tr_y[idx] in labelDic:
                labelDic[tr_y[idx]] = labelDic.get(tr_y[idx]) + sum(self.tr_matrix[idx])
            else:
                labelDic[tr_y[idx]] = sum(self.tr_matrix[idx])
        counts = sum(labelDic.values())
        sorted(labelDic.items(), key=lambda e:e[0], reverse=True)
        self.labelCount = labelDic.copy()
        labelidx = list()
        for key in labelDic:
            labelDic[key] = labelDic[key]/counts
            labelidx.append(key)
        self.labelPriproDic = labelDic.copy()
        self.labelidx = labelidx
        conPro = np.zeros((len(self.labelPriproDic), len(tr_matrix[0])))
        tempDic = {}
        for idf in range(0, len(tr_matrix[0])):
            tempDic = {}
            for idr in range(0, len(tr_y)):
                if tr_y[idr] in tempDic:
                    tempDic[tr_y[idr]] = tempDic[tr_y[idr]] + tr_matrix[idr][idf]
                else:
                    tempDic[tr_y[idr]] = tr_matrix[idr][idf]
            sorted(tempDic.items(), key=lambda e:e[0], reverse=True)
            i = 0
            for key in tempDic:
                conPro[i][idf] = (tempDic[key] + 1) / (self.labelCount[key] + len(self.labelCount))
                i = i + 1
        self.conPro = conPro

    # test: samples * features
    def predict(self, test):
        result = np.zeros(len(test))
        tempDic = {}
        for idx in range(0, len(test)):
            point = test[idx]
            labelidx = 0
            maxPosPro = 0
            curPosPro = 1
            for idc in range(0, len(self.conPro)):
                curPosPro = 1
                for idf in range(len(test[idx])):
                    if test[idx][idf] != 0:
                        temp = self.conPro[idc][idf]**test[idx][idf]
                    else:
                        temp = 1 - self.conPro[idc][idf]
                    curPosPro = curPosPro * temp

                curPosPro = curPosPro * self.labelPriproDic[self.labelidx[idc]]
                if curPosPro > maxPosPro:
                    maxPosPro = curPosPro
                    labelidx = idc
            result[idx] = self.labelidx[labelidx]
            print("%d" % result[idx] + "\n")
        return result