'''
configurations for value iteration and policy iteraion algorithms.
'''


'''
maze, as in the question:
    - "G": green cells,
    - "B": brown cells,
    - " ": white cells,
    - "X":  wall cells.
'''
GRID = [
    ["G", "X", "G", " ", " ", "G"],
    [" ", "B", " ", "G", "X", "B"],
    [" ", " ", "B", " ", "G", " "],
    [" ", " ", " ", "B", " ", "G"],
    [" ", "X", "X", "X", "B", " "],
    [" ", " ", " ", " ", " ", " "],
]

'''
rewards mapping, as given in the question:
    - "G": green cells,
    - "B": brown cells,
    - " ": white cells.
'''
REWARDS = {
    "G": +1.00,
    "B": -1.00,
    " ": -0.04,
}

'''
set of actions the agent can choose from:
    - "→": move right,
    - "↑": move up,
    - "←": move left,
    - "↓": move down.
'''
ACTIONS = {
    "→": (0, 1), 
    "↑": (-1, 0), 
    "←": (0, -1), 
    "↓": (1, 0),
}

'''
common parameters for value iteration and policy iteration:
    - PR_INTENDED: probability that intended action is executed.
    - DISCOUNT_FACTOR: γ value in Bellman's equation.
'''
PR_INTENDED = 0.80
DISCOUNT_FACTOR = 0.99

'''
maximum error, ε, to stop value iteration.
we stop when δ < ε(1-γ)/γ, where δ is the maximum change in utility values.
'''
MAX_ERROR = 1

'''
number of iterations for policy evaluation in policy iteration algorithm.
perform EVALUATION_ITERATIONS number of simplified value iteration steps to 
get an approximation of the utilities.
'''
EVALUATION_ITERATIONS = 100

'''
directory suffix for saving results
'''
SAVE_DIR = "assets"