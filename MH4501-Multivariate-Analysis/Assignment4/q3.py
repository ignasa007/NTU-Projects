import numpy as np

x1 = np.array([0.9, 0.9]).reshape(-1, 1)
x2 = np.array([1.5, 2.0]).reshape(-1, 1)
x3 = np.array([3.0, 4.0]).reshape(-1, 1)
x4 = np.array([5.0, 7.0]).reshape(-1, 1)
x5 = np.array([3.5, 5.0]).reshape(-1, 1)
x6 = np.array([4.5, 5.0]).reshape(-1, 1)
x7 = np.array([3.5, 4.5]).reshape(-1, 1)
K = 2

points = [x1, x2, x3, x4, x5, x6, x7]

clusters = [0, 0, 0, 1, 1, 1, 1]
means = [np.mean([point for point, cluster in zip(points, clusters) if cluster == k], axis=0) for k in range(K)]
print(*means, sep='\n')
print(clusters)

for _ in range(2):

    clusters = [np.argmin([np.linalg.norm(point-mean) for mean in means]) for point in points]
    means = [np.mean([point for point, cluster in zip(points, clusters) if cluster == k], axis=0) for k in range(K)]
    print(*means, sep='\n')
    print(clusters)