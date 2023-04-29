import os
from tqdm import tqdm
import warnings
warnings.filterwarnings('ignore')

import pandas as pd
from Q2 import clean_genes

cwd = os.path.dirname(os.path.realpath(__file__))
train_dir = os.path.join(cwd, 'Assignment2-GeneExpression', 'training', 'train_data')
test_dir = os.path.join(cwd, 'Assignment2-GeneExpression', 'testing', 'test_data')
output_dir = os.path.join(cwd, 'output_files')
os.makedirs(output_dir, exist_ok=True)

genes = list(clean_genes(train_dir, test_dir))

train_df = pd.DataFrame(columns=genes+['subtype'])
train_labels = pd.read_excel(os.path.join(train_dir, os.pardir, 'trainset10-02-01annotation.xls'), index_col='Chip')
train_labels.index = train_labels.index.map(lambda x: x.lower())

for i, fn in tqdm(enumerate(os.listdir(train_dir))):
    if not fn.endswith('.txt'):
        continue
    with open(os.path.join(train_dir, fn), 'r') as f:
        for line in f.readlines()[4:]:
            arr = line.split()
            gene = arr[0]
            if gene in genes:
                train_df.loc[i, gene] = arr[4]  
                train_df.loc[i, 'subtype'] = train_labels.loc[fn[:-4].lower(), 'Coding']

print('\n', train_df.head(), '\n')
train_df.to_csv(os.path.join(cwd, 'output_files', 'Q3_train_cleaned.csv'), index=False)

test_df = pd.DataFrame(columns=genes+['subtype'])
test_labels = pd.read_excel(os.path.join(test_dir, os.pardir, 'testset10-02-01annotation.xls'), index_col='Chip')
test_labels.index = test_labels.index.map(lambda x: x.lower())

for i, fn in tqdm(enumerate(os.listdir(test_dir))):
    if not fn.endswith('.txt'):
        continue
    with open(os.path.join(test_dir, fn), 'r') as f:
        for line in f.readlines()[4:]:
            arr = line.split()
            gene = arr[0]
            if gene in genes:
                test_df.loc[i, gene] = arr[4]  
                test_df.loc[i, 'subtype'] = test_labels.loc[fn[:-4].lower(), 'Coding']

print('\n', test_df.head(), '\n')
test_df.to_csv(os.path.join(cwd, 'output_files', 'Q3_test_cleaned.csv'), index=False)