import numpy as np
from scipy.stats import chi2

mu1 = np.array([-3, 2]).reshape(-1, 1)
mu2 = np.array([0, 1]).reshape(-1, 1)

Sigma11 = np.array([[8, 2], [2, 5]])
Sigma12 = np.array([[3, 1], [-1, 3]])
Sigma21 = Sigma12.T
Sigma22 = np.array([[6, -2], [-2, 7]])

Sigma11_inv = np.linalg.inv(Sigma11)
Lambda, U = np.linalg.eig(Sigma11_inv)
Sigma11_minus_half = U @ np.diag(np.sqrt(Lambda)) @ U.T
print(f'{Sigma11_minus_half = }')

Sigma22_inv = np.linalg.inv(Sigma22)
Lambda, U = np.linalg.eig(Sigma22_inv)
Sigma22_minus_half = U @ np.diag(np.sqrt(Lambda)) @ U.T
print(f'\n{Sigma22_minus_half = }')

weird_U = Sigma11_minus_half @ Sigma12 @ Sigma22_inv @ Sigma21 @ Sigma11_minus_half
rho2, e = np.linalg.eig(weird_U)
order = np.argsort(np.abs(rho2))[::-1]; rho2 = rho2[order]; e = e[:, order]
print(f'\n{weird_U = }')
print(f'{rho2 = }')
print(f'rho = {np.sqrt(rho2)}')
print(f'{e = }')

weird_V = Sigma22_minus_half @ Sigma21 @ Sigma11_inv @ Sigma12 @ Sigma22_minus_half
rho2, f = np.linalg.eig(weird_V)
order = np.argsort(np.abs(rho2))[::-1]; rho2 = rho2[order]; f = f[:, order]

print(f'\n{weird_V = }')
print(f'{rho2 = }')
print(f'{f = }')

a = Sigma11_minus_half @ e
b = Sigma22_minus_half @ f

print(f'\n{a = }')
print(f'{b = }')

TS = -(100-1-(2+2+1)/2)*np.sum(np.log(1-rho2))
k = chi2.ppf(0.95, 4)
print(f'{TS = :.6f}')
print(f'{k = :.6f}')