# absorbing-markov-chains

With at least one possible terminal state, defined as a state which contains no transition probabilities, this program 
calculates the fractional probability of ending in one of potentially many terminal states from some initial state which
contains state transition probabilities.

The program makes use of matrix canonization, Gaussian elimination techniques, and the formula A = F^-1 * S
to find S, the solution matrix.

The Stern-Brocot algorithm is implemented to obtain fractional probabilities.
