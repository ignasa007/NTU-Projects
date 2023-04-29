import os
from time import time
from collections import defaultdict

import numpy as np

from config import *
from utils import logging
from algorithms import ValueIterator, PolicyIterator, MDPBase


SAVE_DIR = f"{SAVE_DIR}/exp2"
if not os.path.isdir(SAVE_DIR):
    os.makedirs(SAVE_DIR, exist_ok=True)


class MDPBaseModified(MDPBase):

    def __init__(self, grid, rewards, actions, pr_intended=0.80, discount_factor=0.99):

        super(MDPBaseModified, self).__init__(grid, rewards, actions, pr_intended, discount_factor)
        self.init_policy()

    def init_policy(self):
        
        self.action_space = defaultdict(list); self.policy = np.full(self.grid.shape, " ")
        for (m, n) in self.states:
            for action in self.actions:
                if self.move((m, n), self.actions[action]) != (m, n):
                    self.action_space[(m, n)].append(action)
            if not self.action_space[(m, n)]:
                raise ValueError(f"Invalid grid, cannot find a possible action for state {(m, n)}")
            else:
                self.policy[m, n] = np.random.choice(self.action_space[(m, n)])

    def probable_actions(self, position, intended_action):

        del_x, del_y = intended_action
        right = (del_y, -del_x); left = (-del_y, del_x)
        pr_alt = 1-self.pr_intended
        if self.move(position, right) != position:
            if self.move(position, left) != position:
                return ((intended_action, self.pr_intended), (right, pr_alt/2), (left, pr_alt/2),)
            else:
                return ((intended_action, self.pr_intended), (right, pr_alt),)
        else:
            if self.move(position, left) != position:
                return ((intended_action, self.pr_intended), (left, pr_alt),)
            else:
                return ((intended_action, self.pr_intended+pr_alt),)

    def expected_utility(self, position, intended_action):

        (x, y) = position; expected_utility = 0
        for (del_x, del_y), prob in self.probable_actions(position, self.actions[intended_action]):
            expected_utility += prob * self.utilities[x+del_x, y+del_y]
        return expected_utility
    

class ValueIteratorModified(MDPBaseModified, ValueIterator):

    def __init__(self, grid, rewards, actions, pr_intended=0.80, discount_factor=0.99):

        super(ValueIteratorModified, self).__init__(grid, rewards, actions, pr_intended, discount_factor)


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


class PolicyIteratorModified(MDPBaseModified, PolicyIterator):

    def __init__(self, grid, rewards, actions, pr_intended=0.80, discount_factor=0.99, evaluation_iterations=50):

        super(PolicyIteratorModified, self).__init__(grid, rewards, actions, pr_intended, discount_factor)
        self.evaluation_iterations = evaluation_iterations


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