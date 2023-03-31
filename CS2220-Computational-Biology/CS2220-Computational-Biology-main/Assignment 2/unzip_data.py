import os
import gzip
import shutil

cwd = os.path.dirname(os.path.realpath(__file__))
dir_path = os.path.join(cwd, 'Assignment2-GeneExpression')

train_dir = os.path.join(dir_path, 'training')
train_read_dir = os.path.join(train_dir, 'trainset10-02-01')
train_write_dir = os.path.join(train_dir, 'train_data')
os.makedirs(train_write_dir, exist_ok=True)

for fn in os.listdir(train_read_dir):
    with gzip.open(os.path.join(train_read_dir, fn), 'rb') as f_in, open(os.path.join(train_write_dir, '.'.join(fn.lower().split('.')[:-1])), 'wb') as f_out:
        shutil.copyfileobj(f_in, f_out)

test_dir = os.path.join(dir_path, 'testing')
test_read_dir = os.path.join(test_dir, 'testset10-02-01')
test_write_dir = os.path.join(test_dir, 'test_data')
os.makedirs(test_write_dir, exist_ok=True)

for fn in os.listdir(test_read_dir):
    with gzip.open(os.path.join(test_read_dir, fn), 'rb') as f_in, open(os.path.join(test_write_dir, '.'.join(fn.lower().split('.')[:-1])), 'wb') as f_out:
        shutil.copyfileobj(f_in, f_out)