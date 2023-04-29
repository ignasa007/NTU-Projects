import os
import argparse
from tqdm import tqdm

import numpy as np
import matplotlib.pyplot as plt

from config import *
from algorithms import PolicyIterator


parser = argparse.ArgumentParser()
parser.add_argument("--nruns", default=10, type=int, help="number of runs to calculate statistics over")
parser.add_argument("--maxsize", default=50, type=int, help="maximum grid size for ")
args = parser.parse_args()


SAVE_DIR = f"{SAVE_DIR}/exp4"
if not os.path.isdir(SAVE_DIR):
    os.makedirs(SAVE_DIR, exist_ok=True)


states = ("G", "B", " ", "X")
probs = (1/6, 1/6, 1/2, 1/6)

FONTDICT = {
    "family": "serif", 
    "color": "darkred", 
    "weight": "normal", 
    "size": 16
}


plt.figure(figsize=(10, 8))
plt.xlabel("Number of Policy Evaluation Rounds", fontdict=FONTDICT, labelpad=12)
plt.ylabel("Number of Policy Improvement Rounds", fontdict=FONTDICT, labelpad=12)

for grid_size in range(10, args.maxsize+1, 10):
    shape = (grid_size, grid_size)
    xs, ys, yerrs = list(), list(), list()
    for EVALUATION_ITERATIONS in tqdm(range(20, 101, 20)):  
        xs.append(EVALUATION_ITERATIONS)
        samples = list()      
        for run in range(1, args.nruns+1):
            GRID = np.random.choice(a=states, size=shape, p=probs)
            mdp = PolicyIterator(GRID, REWARDS, ACTIONS, PR_INTENDED, DISCOUNT_FACTOR, EVALUATION_ITERATIONS)
            n_iters = len(mdp.policy_iteration())
            samples.append(n_iters)
        ys.append(np.mean(samples)); yerrs.append(np.std(samples))
    plt.errorbar(x=xs, y=ys, yerr=yerrs, label=f"Grid Size {grid_size}x{grid_size}", capsize=10)
        
plt.legend(fontsize=12)
plt.grid()
plt.savefig(f"{SAVE_DIR}/effect-of-evaluation-rounds.png")