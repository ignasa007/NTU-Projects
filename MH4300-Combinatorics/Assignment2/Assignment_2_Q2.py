a = 0.5
C = [1.]
A = [0.]

for n in range(1, 11):

    print(f'\nn = {n}')

    C_n = 0.
    for i in range(n):
        C_n += C[i]*C[n-i-1]
    C.append(C_n)

    A_n = 0.
    for i in range(n):
        print(f'i = {i}, n-i-1 = {n-i-1}')
        print(f'C[i] = {C[i]}, A[n-i-1] = {A[n-i-1]}, C[n-i-1] = {C[n-i-1]}, A[i] = {A[i]}')
        print(f'C[i]*A[n-i-1] = {C[i]*A[n-i-1]}, C[n-i-1]*A[i] = {C[n-i-1]*A[i]}, C[i]*C[n-i-1] = {C[i]*C[n-i-1]}, (i+1)*a + (n-i)*(1-a) = {(i+1)*a + (n-i)*(1-a)}')
        A_n += C[i]*A[n-i-1] + C[n-i-1]*A[i] + C[i]*C[n-i-1]*((i+1)*a + (n-i)*(1-a))
        print(f'A_{n} = {A_n}')
    A.append(A_n)

print(f'\nA = {A}')