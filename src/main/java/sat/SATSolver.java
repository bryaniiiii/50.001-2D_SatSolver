package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     *
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // TODO: implement this.

        return solve(formula.getClauses(), new Environment());

    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // TODO: implement this.
        //Case 1: If clause is empty
        if (clauses.isEmpty())
        {return env;}
        Clause smallestClause= clauses.first();
        int size = smallestClause.size();
        for (Clause c : clauses) {
            if (c.isEmpty()) {
                return null;
            } else if (c.size() == 1) {
                smallestClause = c;
                break;
            } else if (c.size() < size) {
                size = c.size();
                smallestClause = c;
            }
        }

        Literal l = smallestClause.chooseLiteral();
        if (smallestClause.size() > 1) {
            Environment positiveEnv = env.putTrue(l.getVariable()); // trying to be positive
            Literal positiveL = PosLiteral.make(l.getVariable());
            ImList bePositive = substitute(clauses, positiveL);
            Environment testEnv1 = solve(bePositive, positiveEnv);
            if (testEnv1 != null) {
                return testEnv1;
            } else {
                Environment negativeEnv = env.putFalse(l.getVariable()); // trying to be positive
                Literal negativeL = PosLiteral.make(l.getVariable());
                ImList beNegative = substitute(clauses, negativeL);
                Environment testEnv2 = solve(beNegative, negativeEnv);
                return testEnv2;
            }
        }
        else{
            if (l instanceof PosLiteral) {
                env = env.putTrue(l.getVariable());
                ImList<Clause> testClausepositive = substitute(clauses, l);
                return solve(testClausepositive, env);
            } else {
                env = env.putFalse(l.getVariable());
                ImList<Clause> testClausenegative = substitute(clauses, l);
                return solve(testClausenegative, env);
            }

        }




    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
                                             Literal l) {
        // TODO: implement this.
        ImList<Clause> newClauses = new EmptyImList<Clause>();
        for (Clause c : clauses) {
            Clause newClause = c.reduce(l);
            if (newClause != null) {
                newClauses = newClauses.add(newClause);
            }
        }
        return newClauses;

    }

}
