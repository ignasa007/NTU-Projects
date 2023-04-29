/* galacticas3 is a technology */
technology(galacticas3).
/* if A is a technology, then A is a business */
business(A) :-
    technology(A).
    
/* samsum and appy are rivals */
rival(samsum, appy).
/* reflexity of rivalry */
rival(A, B) :-
    rival(B, A).

/* if A is a boss of Z and A steals a business X owned by a rival of Z, then A is unethical */
unethical(A) :-
    steal(A, X),
    business(X),
    own(Y, X),
    boss(Z, A),
    rival(Y, Z).

/* samsum owns galacticas3 */
own(samsum, galacticas3).
/* stevey is the boss of appy */
boss(appy, stevey).
/* stevey steals galacticas3 */
steal(stevey, galacticas3).