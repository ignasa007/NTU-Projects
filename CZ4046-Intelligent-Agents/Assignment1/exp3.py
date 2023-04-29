import os

import numpy as np
from scipy.optimize import curve_fit
import matplotlib.pyplot as plt

from config import *
from algorithms import ValueIterator


SAVE_DIR = f"{SAVE_DIR}/exp3"
if not os.path.isdir(SAVE_DIR):
    os.makedirs(SAVE_DIR, exist_ok=True)


FONTDICT = {
    "family": "serif", 
    "color": "darkred", 
    "weight": "normal", 
    "size": 16
}


class ValueIteratorModified(ValueIterator):

    def __init__(self, grid, rewards, actions, pr_intended=0.80, discount_factor=0.99):

        super(ValueIteratorModified, self).__init__(grid, rewards, actions, pr_intended, discount_factor)

    def value_iteration(self, max_error):

        deltas = list()
        delta, upper_bound = float("inf"), max_error * (1-self.discount_factor) / self.discount_factor
        while delta >= upper_bound:
            U = self.one_iteration()
            delta = np.max(np.abs(U-self.utilities)); deltas.append(delta)
            self.utilities = U
        return deltas


mdp = ValueIteratorModified(GRID, REWARDS, ACTIONS, PR_INTENDED, DISCOUNT_FACTOR)
deltas = mdp.value_iteration(max_error=MAX_ERROR)
iterations = np.linspace(1, len(deltas), len(deltas))

f = lambda x, a, b, c: a + b/(x**1.5+c)
popt, _ = curve_fit(f, iterations, deltas)
xs = np.linspace(1, len(deltas), num=len(deltas)*10)

plt.figure(figsize=(10, 8))
plt.plot(iterations, deltas, color="b", label=r"$\delta$-curve")
plt.plot(xs, f(xs, *popt), color="g", label=r"${0:.2f}+{1:.2f}/\left(x^{{1.5}}+{2:.2f}\right)$".format(*popt))
plt.xlabel("Iteration Number", fontdict=FONTDICT, labelpad=12)
plt.ylabel(r"$\delta$", fontdict=FONTDICT, labelpad=12)
plt.legend(fontsize=12)
plt.grid()
plt.savefig(f"{SAVE_DIR}/delta-decay.png")