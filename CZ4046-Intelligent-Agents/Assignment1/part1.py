'''
execution file for part 1 of the assignment.
usage: python part1.py
'''


import os
from time import time

from config import *
from utils import logging
from algorithms import ValueIterator, PolicyIterator


# create the directory to save results of part 1 in.
SAVE_DIR = f"{SAVE_DIR}/part1"
if not os.path.isdir(SAVE_DIR):
    os.makedirs(SAVE_DIR, exist_ok=True)


# initialize the value iteration algorithm.
mdp = ValueIterator(GRID, REWARDS, ACTIONS, PR_INTENDED, DISCOUNT_FACTOR)
# collect the sequence of utility estimates calculated until convergence.
value_iteration_start = time()
utilities = mdp.value_iteration(max_error=MAX_ERROR)    
value_iteration_end = time()   

# plot the evolution of utility estimates for all states.
logging.plot(
    states=mdp.states,
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

# plot the evolution of utility estimates for all states.
logging.plot(
    states=mdp.states, 
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