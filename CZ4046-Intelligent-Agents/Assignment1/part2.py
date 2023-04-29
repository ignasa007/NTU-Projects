'''
execution file for part 2 of the assignment.
usage: python part2.py --nrows 100 --ncols 100
'''


import os
import argparse
import random
import sys
from time import time

import numpy as np
np.set_printoptions(threshold=sys.maxsize); np.set_printoptions(linewidth=np.inf)

from config import *
from utils import logging
from algorithms.value_iteration import ValueIterator
from algorithms.policy_iteration import PolicyIterator


# create a parser for command line arguments which collects the 
# number of rows and columns in the randomly generated grid.
parser = argparse.ArgumentParser()
parser.add_argument("--nrows", default=100, type=int, help="number of rows in the grid")
parser.add_argument("--ncols", default=100, type=int, help="number of cols in the grid")
args = parser.parse_args()


# create the directory to save results of part 2 in.
SAVE_DIR = f"{SAVE_DIR}/part2/{args.nrows}x{args.ncols}-grid"
if not os.path.isdir(SAVE_DIR):
    os.makedirs(SAVE_DIR, exist_ok=True)


# generate a random grid of shape (args.nrows, args.ncols) such that it has
#   - 1/6 green cells,
#   - 1/6 brown cells,
#   - 1/2 white cells,
#   - 1/6  wall cells,
# where the probabilities are estimated from the grid in the question.

states = ("G", "B", " ", "X")
probs = (1/6, 1/6, 1/2, 1/6)
shape = (args.nrows, args.ncols)
GRID = np.random.choice(a=states, size=shape, p=probs)


with open(f"{SAVE_DIR}/random-grid.txt", "w") as f:
    f.write(f"{args.nrows}x{args.ncols} RANDOMLY GENERATED GRID\n")
    strf_array = ["| " + " | ".join(row) + " |" for row in GRID]
    logging.write_strf_array(f, strf_array)


# initialize the value iteration algorithm.
mdp = ValueIterator(GRID, REWARDS, ACTIONS, PR_INTENDED, DISCOUNT_FACTOR)
# collect the sequence of utility estimates calculated until convergence.
value_iteration_start = time()
utilities = mdp.value_iteration(max_error=MAX_ERROR)    
value_iteration_end = time()

# plot the evolution of utility estimates for a maximum of 30 states.
logging.plot(
    states=random.sample(mdp.states, k=min(len(mdp.states), 30)),
    utilities=utilities,
    save_fn=f"{SAVE_DIR}/value-iteration-plot.png"
)

# log the results of the value iteration run.
logging.log_results(
    n_iterations=len(utilities), 
    running_time=value_iteration_end-value_iteration_start,
    converged_utility=mdp.utilities, 
    converged_policy=mdp.policy,
    save_fn=f"{SAVE_DIR}/value-iteration-results.txt"
)


# initialize the policy iteration algorithm.
mdp = PolicyIterator(GRID, REWARDS, ACTIONS, PR_INTENDED, DISCOUNT_FACTOR, EVALUATION_ITERATIONS)
# collect the sequence of utility estimates calculated until convergence.
policy_iteration_start = time()
utilities = mdp.policy_iteration()    
policy_iteration_end = time()

# plot the evolution of utility estimates for a maximum of 30 states.
logging.plot(
    states=random.sample(mdp.states, k=min(len(mdp.states), 30)), 
    utilities=utilities,
    save_fn=f"{SAVE_DIR}/policy-iteration-plot.png"
)

# log the results of the policy iteration run.
logging.log_results(
    n_iterations=len(utilities), 
    running_time=policy_iteration_end-policy_iteration_start,
    converged_utility=mdp.utilities, 
    converged_policy=mdp.policy,
    save_fn=f"{SAVE_DIR}/policy-iteration-results.txt"
)