import os
import itertools

import pandas as pd
import numpy as np
from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from chefboost import Chefboost as chef
from sklearn.metrics import confusion_matrix, accuracy_score
import matplotlib.pyplot as plt

from Q4 import select_best


cwd = os.path.dirname(os.path.realpath(__file__))
train_path = os.path.join(cwd, 'output_files', 'Q3_train_cleaned.csv')
test_path = os.path.join(cwd, 'output_files', 'Q3_test_cleaned.csv')

train_X, test_X = pd.read_csv(train_path).rename(columns={'subtype': 'Decision'}), pd.read_csv(test_path).rename(columns={'subtype': 'Decision'})
train_y, test_y = train_X.pop('Decision'), test_X.pop('Decision')

best_genes = select_best(train_X, train_y).index
train_X, test_X = train_X.loc[:, best_genes], test_X.loc[:, best_genes]


def plot_confusion_matrix(cm, classes, normalize=False, title='Confusion matrix', cmap=plt.cm.Oranges):

    if normalize:
        cm = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]
        print("Normalized confusion matrix")
    else:
        print('Confusion matrix, without normalization')

    print(cm, end='\n\n')

    plt.figure(figsize = (8, 8))
    plt.imshow(cm, interpolation='nearest', cmap=cmap)
    plt.title(title, size=16)
    plt.colorbar(aspect=4)
    tick_marks = np.arange(len(classes))
    plt.xticks(tick_marks, classes, rotation=45, size=10)
    plt.yticks(tick_marks, classes, size=10)

    fmt = '.2f' if normalize else 'd'
    thresh = cm.max() / 2.
    
    for i, j in itertools.product(range(cm.shape[0]), range(cm.shape[1])):
        plt.text(j, i, format(cm[i, j], fmt), fontsize = 20,
                 horizontalalignment="center",
                 color="white" if cm[i, j] > thresh else "black")
        
    plt.tight_layout()
    plt.ylabel('True label', size = 18)
    plt.xlabel('Predicted label', size = 18)
    plt.tight_layout()

    plt.show()


clf = make_pipeline(StandardScaler(), SVC(gamma='auto'))
clf.fit(train_X, train_y)

predictions = clf.predict(test_X)
cm = confusion_matrix(test_y, predictions)
plot_confusion_matrix(cm, classes=np.unique(test_y), title='Subtype Classification')


scaled_X = StandardScaler().fit_transform(train_X)
config = {
    'algorithm': 'C4.5'
}
model = chef.fit(pd.concat([train_X, train_y], axis=1), config)

predictions = chef.predict(model, test_X)
cm = confusion_matrix(test_y, predictions)
plot_confusion_matrix(cm, classes=np.unique(test_y), title='Subtype Classification')