import os
from tqdm import tqdm 

def clean_genes(train_dir, test_dir):

    genes_intersection = set()
    for data_dir in [train_dir, test_dir]:
        for fn in tqdm(os.listdir(data_dir)):
            genes = set()
            with open(os.path.join(data_dir, fn), 'r') as f:
                for _ in range(4):
                    f.readline()
                line = f.readline()
                while line:
                    gene, _, _, _, _, absCall, *_ = line.split()
                    if not gene.startswith('AFFX') and not absCall in ('A', 'M'):
                        genes.add(gene)
                    line = f.readline()
            genes_intersection = genes_intersection.intersection(genes) if genes_intersection else genes

    print(f'\nNUMBER OF GENES LEFT AFTER THE CLEANING ABOVE IS PERFORMED = {len(genes_intersection)}\n')

    return genes_intersection


if __name__ == '__main__':

    cwd = os.path.dirname(os.path.realpath(__file__))
    train_dir = os.path.join(cwd, 'Assignment2-GeneExpression', 'training', 'train_data')
    test_dir = os.path.join(cwd, 'Assignment2-GeneExpression', 'testing', 'test_data')

    clean_genes(train_dir, test_dir)