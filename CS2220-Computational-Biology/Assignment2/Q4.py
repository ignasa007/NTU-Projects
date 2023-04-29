import pandas as pd
from sklearn.feature_selection import SelectKBest
from sklearn.feature_selection import chi2

def select_best(X, y):

    model = SelectKBest(chi2, k=200)
    model.fit(X, y)

    cols = model.get_support(indices=True)
    selected_genes = pd.DataFrame(data=model.scores_[cols], index=X.columns[cols], columns=['chi-square values'])

    return selected_genes


if __name__ == '__main__':

    import os
    cwd = os.path.dirname(os.path.realpath(__file__))
    path = os.path.join(cwd, 'output_files', 'Q3_train_cleaned.csv')

    X = pd.read_csv(path)
    y = X.pop('subtype')
    
    selected_genes = select_best(X, y)
    print('\n', 'CHI-SQUARE VALUES FOR SELECTED GENES', '\n')
    print(selected_genes)