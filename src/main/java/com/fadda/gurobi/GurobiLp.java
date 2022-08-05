package com.fadda.gurobi;

import gurobi.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class GurobiLp {


    public static void solve(String file) {
        Locale.setDefault(new Locale("en", "US"));
        GurobiSolution solution = GurobiLp.gurobi(file);
        System.out.println("\n\n\n\n");
        System.out.printf("Objetivo : %.2f%n", solution.objVal);
        System.out.println("\n\n");
        System.out.println(solution.values.keySet()
                .stream()
                .filter(e -> solution.values.get(e) > 0.)

                .map(e -> String.format("%s == %.1f", e, solution.values.get(e)))
                .collect(Collectors.joining("\n")));
    }


    public static GurobiSolution solveSolution(String file) {

        Locale.setDefault(new Locale("en", "US"));
        return GurobiLp.gurobi(file);
    }

    public static GurobiSolution gurobi(String file) {

        GRBModel model;
        Double objval = null;
        GRBVar[] vars;
        Map<String, Double> map = null;
        try {
            GRBEnv env = new GRBEnv();
            model = new GRBModel(env, file);

            model.optimize();

            int optimstatus = model.get(GRB.IntAttr.Status);

            if (optimstatus == GRB.Status.INF_OR_UNBD) {
                model.set(GRB.IntParam.Presolve, 0);
                model.optimize();
                optimstatus = model.get(GRB.IntAttr.Status);
            }

            if (optimstatus == GRB.Status.OPTIMAL) {
            } else if (optimstatus == GRB.Status.INFEASIBLE) {
                System.out.println("Model is infeasible");

                model.computeIIS();
                model.write("model.ilp");
            } else if (optimstatus == GRB.Status.UNBOUNDED) {
                System.out.println("Model is unbounded");
            } else {
                System.out.println("Optimization was stopped with status = " + optimstatus);
            }

            objval = model.get(GRB.DoubleAttr.ObjVal);
            vars = model.getVars();
            map = new HashMap<>();
            for (GRBVar v : vars) {
                map.put(v.get(GRB.StringAttr.VarName), v.get(GRB.DoubleAttr.X));
            }

            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
        return GurobiSolution.of(objval, map);
    }
}
