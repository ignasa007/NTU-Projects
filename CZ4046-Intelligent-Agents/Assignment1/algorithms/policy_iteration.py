import numpy as np
from algorithms.mdp_base import MDPBase


'''
Policy iteration algorithm.
'''
class PolicyIterator(MDPBase):

    
    def __init__(self, grid, rewards, actions, pr_intended=0.80, discount_factor=0.99, evaluation_iterations=100):

        '''
        initialize the PolicyIterator object.
        '''

        # initialize the MDPBase parent class.
        super(PolicyIterator, self).__init__(grid, rewards, actions, pr_intended, discount_factor)
        
        # number of simplified value iteration steps to get an approximation of the utilities.
        self.evaluation_iterations = evaluation_iterations
    

    def policy_evaluation(self):

        '''
        estimate the utility values for all states iteratively.
        '''

        # do a fixed number of iterations of estimating the utilities with a fixed policy.
        for _ in range(self.evaluation_iterations):
            U_pi = np.zeros_like(self.utilities, dtype=float)
            # loop over all non-wall states.
            for (m, n) in self.states:
                # set the expected utility for the given state using the current policy. 
                U_pi[m, n] = self.rewards[self.grid[m, n]] + \
                    self.discount_factor * self.expected_utility((m, n), self.policy[m, n])
            # set utility estimates from current round of policy evaluation.
            self.utilities = U_pi


    def one_iteration(self):

        '''
        one iteration of policy iteration.
        returns: 
            - converged: boolean value indicating if there has been a change in policy.
        '''

        # estimate the utility values.
        self.policy_evaluation()
    
        # initialize converged = True. 
        converged = True
        # loop over all non-wall states.
        for (m, n) in self.states:
            # calculate the best intended action, and its utility, for the given state.
            best_intended_action, best_utility = self.best_policy((m, n))
            # if the best action has better expected utility than the current policy, 
            # set it as the new policy and set converged = False.
            if best_utility > self.expected_utility((m, n), self.policy[m, n]):
                self.policy[m, n] = best_intended_action
                converged = False

        return converged


    def policy_iteration(self):

        '''
        perform policy iteration until policy converges.
        returns:
            - utilities: sequence of utility estimates calculated until convergence.
        '''

        # initialize the sequence collecting utility estimates from each iteration.
        # initialize converged = False.
        utilities, converged = list(), False

        # collect the utility estimates in each iteration until the policy converges. 
        while not converged:
            converged = self.one_iteration()
            utilities.append(self.utilities.copy())

        return utilities