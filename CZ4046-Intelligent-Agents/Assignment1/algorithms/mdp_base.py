from collections import defaultdict

import numpy as np


'''
Parent class for value iteration and policy iteration algorithms.
'''
class MDPBase:
    

    def __init__(self, grid, rewards, actions, pr_intended=0.80, discount_factor=0.99):

        '''
        initialize the object.
        params:
            - grid: a 2D array with cell states in {"G", "B", " ", "X"}, where
                    - "G" refers to a green cell,
                    - "B" refers to a brown cell,
                    - " " refers to a white cell,
                    - "X" refers to a  wall cell.
            - rewards: map from states to their (floating point) rewards.
            - actions: map between string representation of action and the corresponding 
                       x and y displacements.
            - pr_intended: probability of the intended outcome being executed. 
                           alternate actions are executed with equal probabilities.
            - discount_factor: γ value in Bellman's equation.
        '''
        
        self.grid = np.array(grid)
        self.rewards = rewards
        
        # check that 0 <= pr_intended <= 1. initialize as 0.80 otherwise.
        if pr_intended < 0 or 1 < pr_intended:
            print(f"expected 'pr_intended' to be between 0 and 1, instead received {pr_intended}")
            print("defaulting to pr_intended = 0.80")
            self.pr_intended = 0.80
        else:
            self.pr_intended = pr_intended

        # check that 0 <= discount_factor <= 1. initialize as 0.99 otherwise.
        if discount_factor < 0 or 1 < discount_factor:
            print(f"expected 'discount_factor' to be between 0 and 1, instead received {discount_factor}")
            print("defaulting to discount_factor = 0.99")
            self.discount_factor = 0.99
        else:
            self.discount_factor = discount_factor

        # (row index, column index) tuples where the cell is not a wall.
        self.states = set(zip(*np.where(self.grid != "X")))
        # actions that the agent can choose from.
        self.actions = actions

        # initialize utilities and initialize policy.
        self.init_utilities(); self.init_policy()


    def init_utilities(self):

        '''
        method defining how the utilities will be initialized.
        '''

        self.utilities = np.zeros_like(self.grid, dtype=float)

    
    def init_policy(self):

        '''
        method defining how the policy will be initialized.
        '''

        self.action_space = defaultdict(list); self.policy = np.full(self.grid.shape, " ")
        for (m, n) in self.states:
            for action in self.actions:
                self.action_space[(m, n)].append(action)
            self.policy[m, n] = np.random.choice(self.action_space[(m, n)])
    

    def move(self, curr_position, action):

        '''
        calculate the final position of the agent upon executing an action in a given position.
        params:
            - curr_position: where the agent is currently at.
            - action: 2-tuple with step sizes along x and y axes.
        returns:
            - new_position: position agent will end up at if it executes the given action.
        '''

        # unpack the position and action 2-tuples.
        x, y = curr_position; del_x, del_y = action
        # calculate the new (unrestricted) position values.
        new_x = x+del_x; new_y = y+del_y

        # if new position is valid, return it, else return current position.
        if (new_x, new_y) in self.states:
            return (new_x, new_y)
        else:
            return (x, y)
        

    def probable_actions(self, intended_action):

        '''
        returns a sequence of (action, pr) pairs consisting of actions that may be
        executed given the intended action and their corresponding probabilities.
        params:
            - intended_action: tuple indicating direction along which the agent intends to move.
        returns:
            - probable_actions: a sequence of action-probability pairs.
        '''
        
        del_x, del_y = intended_action
        probs = (self.pr_intended,) + (((1-self.pr_intended)/2),) * 2
        right = (del_y, -del_x); left = (-del_y, del_x)
        return zip((intended_action, right, left), probs)
        

    def expected_utility(self, position, intended_action):

        '''
        calculate the expected utility with an intended action in a given position.
        params:
            - position: where the agent is currently at.
            - intended_action: action agent intends to perform.
        returns:
            - expected_utility: expected utility of the intended action. 
        '''
            
        # calculated the expected utility as Σ P(s'|s,a)U(s').
        expected_utility = 0
        for action, prob in self.probable_actions(self.actions[intended_action]):
            new_x, new_y = self.move(position, action)
            expected_utility += prob * self.utilities[new_x, new_y]

        return expected_utility
    
    
    def best_policy(self, position):

        '''
        calculate the best action in a given position.
        params:
            - position: where the agent is currently at.
        returns:
            - best_intended_action: action yielding the highest expected utility.
            - best_utility: utility of the best_intended_action
        '''

        # intialize best_intended_action and best_utility.
        best_intended_action, best_utility = None, -float("inf")
        # loop over the possible actions to find the action with the highest expected utility.
        for intended_action in self.action_space[position]:
            action_utility = self.expected_utility(position, intended_action)
            if action_utility > best_utility:
                best_intended_action, best_utility = intended_action, action_utility

        return best_intended_action, best_utility