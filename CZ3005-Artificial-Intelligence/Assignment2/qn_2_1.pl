/* sons of the queen */
son(charles, elizabeth).
son(andrew, elizabeth).
son(edward, elizabeth).
/* if A is a son, then A is male */
male(A) :-
    son(A, _).

/* daughter of the queen */
daughter(ann, elizabeth).
/* if A is a daughter, then A is female */
female(A) :-
    daughter(A, _).

/* if A is a son or a daughter of B, then A is a child of B */
child(A, B) :-
    son(A, B);
    daughter(A, B).

/* age comparison between the children of the queen */
older(charles, ann).
older(charles, andrew).
older(charles, edward).
older(ann, andrew).
older(ann, edward).
older(andrew, edward).

/* if (A is a male and B is a female) or (A and B are the same gender and A is older than B), then A precedes B */
precedes(A, B) :-
    male(A), female(B);
    (male(A), male(B); female(A), female(B)), older(A, B).

/* method for inserting a child in the list of successors based on succession rule */
insert_successor(A, [B|C], [B|D]) :- 
    not(precedes(A, B)),
    !,
    insert_successor(A, C, D).
insert_successor(A, B, [A|B]).

/* method for sorting the list of successors based on succession rule */
sort_successors([A|B], C) :- 
    sort_successors(B, D), 
    insert_successor(A, D, C).
sort_successors([], []).

/* method for getting the list of successors */
successors(A, B):-
	findall(X, child(X, A), Y),
    sort_successors(Y, B).