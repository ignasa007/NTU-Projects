from itertools import permutations

import pandas as pd


def main(N):

	perms = permutations(range(1, N+1))
	results = []

	for perm in sorted(list(perms)):
		
		lr_mn, indices_lr_mn = perm[0], [0]
		for idx, e in enumerate(perm[1:], 1):
			if e < lr_mn:
				lr_mn = e
				indices_lr_mn.append(idx)
				
		rl_mx, indices_rl_mx = perm[N-1], [0]
		for idx, e in enumerate(perm[N-2::-1], 1):
			if e > rl_mx:
				rl_mx = e
				indices_rl_mx.append(idx)

		results.append({
			'perm': list(perm),
			'indices_lr_mn': indices_lr_mn,
			'indices_rl_mx': indices_rl_mx,
			'len(indices_lr_mn)': len(indices_lr_mn),
			'len(indices_rl_mx)': len(indices_rl_mx),
		})

	results_df, results_cross = [], []

	for result in results:
		if result['indices_lr_mn'] == result['indices_rl_mx']:
			results_df.append(result)
		else:
			results_cross.append(result)

	added = set()
	for i in range(len(results_cross)):
		if i in added:
			continue
		results_i = results_cross[i]
		for j in range(i+1, len(results_cross)):
			results_j = results_cross[j]
			if j not in added and results_i['indices_lr_mn'] == results_j['indices_rl_mx'] and results_j['indices_lr_mn'] == results_i['indices_rl_mx']:
				added.add(j)
				results_df.extend([results_i, results_j])
				break
		else:
			print(f'N = {N}, Rogue: {results_i}')

	print(pd.DataFrame(results_df))


if __name__ == '__main__':

	main(4)