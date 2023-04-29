'''
utility function for logging results.
'''


import sys
from datetime import datetime as dt

import matplotlib.pyplot as plt
import numpy as np
np.set_printoptions(threshold=sys.maxsize); np.set_printoptions(linewidth=np.inf)


def plot(states, utilities, save_fn=None):

    '''
    plots the evolution of utility estimates.  
    '''

    # define font attributes for plots.
    FONTDICT = {
        "family": "serif", 
        "color": "darkred", 
        "weight": "normal", 
        "size": 16
    }

    plt.figure(figsize=(12, 8))

    # for each of the non-wall states, plot the utility estimates against the iteration numbers.
    for (m, n) in sorted(states):
        xs, ys = list(), list()
        for i, u in enumerate(utilities, 1):
            xs.append(i); ys.append(u[m, n])
        plt.plot(xs, ys, label=str((m, n)))

    # set plot title and axes labels. 
    plt.xlabel("Iteration", fontdict=FONTDICT, labelpad=12)
    plt.ylabel("Utility",  fontdict=FONTDICT, labelpad=12)
    plt.title("Evolution of Utility of Different States", fontdict=FONTDICT, pad=12)

    # create a legend identifying different plots by the state they correspond to.
    plt.legend(loc="center left", bbox_to_anchor=(1, 0.5))
    # create a grid.
    plt.grid()

    # save figure.
    if save_fn is not None:
        plt.savefig(save_fn)


def write_strf_array(f, strf_array):

    f.write("-"*len(strf_array[0]) + "\n")
    for strf_row in strf_array:
        f.write(strf_row + "\n")
        f.write("-"*len(strf_row) + "\n")


def log_results(n_iterations, running_time, converged_utility, converged_policy, save_fn):

    '''
    logs the total number of iterations, converged utility values and optimal policy.
    '''

    with open(save_fn, "a", encoding="utf-8") as f:

        time = dt.now().astimezone()
        f.write(time.strftime("%d %B %Y, %H:%M:%S (%Z)\n"))

        f.write(f"\nTOTAL NUMBER OF ITERATIONS = {n_iterations}\n")
        f.write(f"TOTAL RUNNING TIME = {running_time:.3f} seconds\n")

        f.write(f"\nCONVERGED UTILITY FOR EACH STATE\n")
        cell_width = len(str(int(np.max(converged_utility)))) + 4
        strf_array = ["| " + " | ".join([f"{x:.3f}".rjust(cell_width, " ") if x else " "*cell_width for x in row]) + " |" for row in converged_utility]
        write_strf_array(f, strf_array)

        f.write(f"\nOPTIMAL POLICY FOR EACH STATE\n")
        strf_array = ["| " + " | ".join(row) + " |" for row in converged_policy]
        write_strf_array(f, strf_array)

        f.write("\n")