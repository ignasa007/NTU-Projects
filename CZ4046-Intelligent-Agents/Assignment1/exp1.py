import os
from time import time

import numpy as np

from config import *
from utils import logging
from algorithms import ValueIterator, PolicyIterator


SAVE_DIR = f"{SAVE_DIR}/exp1"
if not os.path.isdir(SAVE_DIR):
    os.makedirs(SAVE_DIR, exist_ok=True)


class ValueIteratorModified(ValueIterator):

    def __init__(self, grid, rewards, actions, pr_intended=0.80, discount_factor=0.99):

        super(ValueIteratorModified, self).__init__(grid, rewards, actions, pr_intended, discount_factor)

    def one_iteration(self):

        utilities_copy = np.copy(self.utilities)
        for (m, n) in self.states:
            best_intended_action, best_utility = self.best_policy((m, n))
            self.utilities[m, n] = self.rewards[self.grid[m, n]] + self.discount_factor*best_utility
            self.policy[m, n] = best_intended_action
        return np.max(np.abs(self.utilities-utilities_copy))

    def value_iteration(self, max_error):

        utilities = list() 
        delta, upper_bound = float("inf"), max_error*(1-self.discount_factor)/self.discount_factor
        while delta >= upper_bound:
            delta = self.one_iteration()
            utilities.append(self.utilities.copy())
        return utilities


mdp = ValueIteratorModified(GRID, REWARDS, ACTIONS, PR_INTENDED, DISCOUNT_FACTOR)
value_iteration_start = time()
utilities = mdp.value_iteration(max_error=MAX_ERROR)
value_iteration_end = time()   

logging.plot(
    states=mdp.states,
    utilities=utilities,
    save_fn=f"{SAVE_DIR}/value-iteration-plot.png"
)

logging.log_results(
    n_iterations=len(utilities), 
    running_time=value_iteration_end-value_iteration_start,
    converged_utility=mdp.utilities, 
    converged_policy=mdp.policy,
    save_fn=f"{SAVE_DIR}/value-iteration-results.txt"
)


class PolicyIteratorModified(PolicyIterator):

    def __init__(self, grid, rewards, actions, pr_intended=0.80, discount_factor=0.99, evaluation_iterations=50):

        super(PolicyIteratorModified, self).__init__(grid, rewards, actions, pr_intended, discount_factor, evaluation_iterations)

    def policy_evaluation(self):

        for _ in range(self.evaluation_iterations):
            for (m, n) in self.states:
                self.utilities[m, n] = self.rewards[self.grid[m, n]] + \
                    self.discount_factor * self.expected_utility((m, n), self.policy[m, n])


mdp = PolicyIteratorModified(GRID, REWARDS, ACTIONS, PR_INTENDED, DISCOUNT_FACTOR, EVALUATION_ITERATIONS)
policy_iteration_start = time()
utilities = mdp.policy_iteration()    
policy_iteration_end = time()

logging.plot(
    states=mdp.states, 
    utilities=utilities,
    save_fn=f"{SAVE_DIR}/policy-iteration-plot.png"
)

logging.log_results(
    n_iterations=len(utilities), 
    running_time=policy_iteration_end-policy_iteration_start,
    converged_utility=mdp.utilities, 
    converged_policy=mdp.policy,
    save_fn=f"{SAVE_DIR}/policy-iteration-results.txt"
)