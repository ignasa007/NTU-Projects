import numpy as np
import matplotlib.pyplot as plt

FONTDICT = {
    'family': 'serif', 
    'color': 'darkred', 
    'weight': 'normal', 
    'size': 12
}

plt.figure(figsize=(6, 4))
plt.xlabel(r'$x$', fontdict=FONTDICT, labelpad=12)
plt.ylabel(r'$f(x)$', fontdict=FONTDICT, labelpad=12)

x = np.linspace(-1, 1, 100)
y = (1-np.abs(x)) / 2
plt.plot(x, y, color='green', label=r'$f_1(x)$')

x = np.linspace(-1.5, 2.5, 200)
y = (2-np.abs(x-0.5))/4
plt.plot(x, y, color='blue', label=r'$f_2(x)$')

plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
plt.grid()
plt.savefig('assets/q1.png', bbox_inches='tight')