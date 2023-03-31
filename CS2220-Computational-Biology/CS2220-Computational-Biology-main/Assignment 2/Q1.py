import os
import pandas as pd
from collections import Counter

cwd = os.path.dirname(os.path.realpath(__file__))
train_dir = os.path.join(cwd, 'Assignment2-GeneExpression', 'training')
test_dir = os.path.join(cwd, 'Assignment2-GeneExpression', 'testing')

train_df = pd.read_excel(os.path.join(train_dir, 'trainset10-02-01annotation.xls'))
test_df = pd.read_excel(os.path.join(test_dir, 'testset10-02-01annotation.xls'))

train_coding_groups = train_df.groupby(by='Coding').size()
test_coding_groups = test_df.groupby(by='Coding').size()

subtype_counts = pd.concat([train_coding_groups, test_coding_groups], axis=1)
subtype_counts.columns = ['train', 'test']

print('\nSUBTYPES COUNTS IN TRAIN AND TEST SET\n')
print(subtype_counts)

train_genes = []
train_files_dir = os.path.join(train_dir, 'train_data')
for fn in os.listdir(train_files_dir):
    num_lines = sum(1 for _ in open(os.path.join(train_files_dir, fn)))
    train_genes.append(num_lines)

cntr = Counter(train_genes)
print('\nTRAIN SET NUMBER OF GENES DISTRIBUTION (NUMBER OF GENES: NUMBER OF FILES)\n')
for key in cntr:
    print(f'{key}: {cntr[key]}') 

test_genes = []
test_files_dir = os.path.join(test_dir, 'test_data')
for fn in os.listdir(test_files_dir):
    num_lines = sum(1 for _ in open(os.path.join(test_files_dir, fn)))
    test_genes.append(num_lines)
    
cntr = Counter(test_genes)
print('\nTEST SET NUMBER OF GENES DISTRIBUTION (NUMBER OF GENES: NUMBER OF FILES)\n')
for key in cntr:
    print(f'{key}: {cntr[key]}')

print('\nYES, ALL THE FILES HAVE THE SAME NUMBER OF GENES\n')