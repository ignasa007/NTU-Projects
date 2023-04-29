import numpy as np
from algorithms.mdp_base import MDPBase


'''
Value iteration algorithm.
'''
class ValueIterator(MDPBase):


    def __init__(self, grid, rewards, actions, pr_intended=0.80, discount_factor=0.99):

        '''
        initialize the ValueIterator object.
        '''

        # initialize the MDPBase parent class.
        super(ValueIterator, self).__init__(grid, rewards, actions, pr_intended, discount_factor)


    def one_iteration(self):

        '''
        one iteration of value iteration.
        returns: 
            - U: utility estimates after one round of value iteration.
        '''

        U = np.zeros_like(self.utilities, dtype=float)
        # loop over all non-wall states.
        for (m, n) in self.states:
            # calculate the best intended action, and its utility, for the given state.
            best_intended_action, best_utility = self.best_policy((m, n))
            # set the best utility value for the given state.
            U[m, n] = self.rewards[self.grid[m, n]] + self.discount_factor * best_utility
            # set the best intended action for the given state.
            self.policy[m, n] = best_intended_action
                
        return U


    def value_iteration(self, max_error):

        # initialize the sequence collecting utility approximation from each iteration.
        utilities = list()
        
        # initialize the maximum change in utility values, delta.
        # initialize the upper bound for delta to call convergence. 
        delta, upper_bound = float("inf"), max_error * (1-self.discount_factor) / self.discount_factor

        # collect the utility estimates in each iteration until the estimates converge.
        while delta >= upper_bound:
            U = self.one_iteration()
            delta = np.max(np.abs(U-self.utilities))
            self.utilities = U; utilities.append(self.utilities.copy())

        return utilities