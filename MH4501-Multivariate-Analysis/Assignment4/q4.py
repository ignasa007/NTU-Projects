from scipy.spatial.distance import squareform
from scipy.cluster import hierarchy
import matplotlib.pyplot as plt
from matplotlib.colors import XKCD_COLORS

FONTDICT = {
    'family': 'serif', 
    'color': 'darkred', 
    'weight': 'normal', 
    'size': 12
}
colors = ['b', 'g', 'r', 'c', 'm', 'y', 'k'] 

dissimilarity = squareform([
    [ 0,  9,  3,  6, 11,],
    [ 9,  0,  7,  5, 10,],
    [ 3,  7,  0,  9,  2,],
    [ 6,  5,  9,  0,  8,],
    [11, 10,  2,  8,  0,],
])

for linkage_n in ('complete', 'single'):

    linkage = hierarchy.linkage(dissimilarity, method=linkage_n)

    plt.figure(figsize=(6, 4))
    plt.title(f'{linkage_n} linkage', fontdict=FONTDICT, pad=12)
    plt.xlabel('observation', fontdict=FONTDICT, labelpad=12)
    plt.ylabel('height', fontdict=FONTDICT, labelpad=12)

    ddata = hierarchy.dendrogram(linkage, color_threshold=0)
    for i, d, c in zip(ddata['icoord'], ddata['dcoord'], ddata['color_list']):
        x, y = 0.5*sum(i[1:3]), d[1]
        plt.plot(x, y, 'o', c=c)
        plt.annotate(y, (x, y), xytext=(0, -5), textcoords='offset points', va='top', ha='center')

    plt.grid()
    plt.savefig(f'assets/q4-{linkage_n}.png', bbox_inches='tight')