package com.npe.analysis;

import org.glassfish.jaxb.runtime.v2.runtime.output.SAXOutput;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.NullConstant;
import soot.jimple.Ref;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*public class PointerAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Local>> {

    private Map<String, List<String>> reportMap;
    public PointerAnalysis(ExceptionalUnitGraph graph, Map<String, List<String>> map) {
        super(graph);
        reportMap = map;
        var methodName = graph.getBody().getMethod().getName();
        if(!methodName.equals("<init>")) {
            System.out.println("I'm inside");
            reportMap.put(methodName, new ArrayList<>(2));
        }
        System.out.println(this.getClass().getName()+" CONSTRUCTOR\n");
        System.out.println(graph.getBody());
        System.out.println("=============================================");
        doAnalysis();
    }

    @Override
    protected FlowSet<Local> newInitialFlow() {
        // Initialize with the bottom element (not nullable)
        return new ArraySparseSet<>();
    }

    @Override
    protected FlowSet<Local> entryInitialFlow() {
        // Initialize with the bottom element (not nullable)
        return new ArraySparseSet<>();
    }

    @Override
    protected void flowThrough(FlowSet<Local> in, Unit unit, FlowSet<Local> out) {
        in.copy(out);

        if (unit instanceof AssignStmt assignStmt) {
            Local leftOp = (Local) assignStmt.getLeftOp();
            if (assignStmt.getRightOp() instanceof NullConstant) {
                // If the right operand is a null constant, mark the left operand as nullable
                System.out.println(assignStmt);
                System.out.println("Type : " + assignStmt.getRightOp().getClass());
                out.add(leftOp);
            } else if (assignStmt.getRightOp() instanceof Ref) {
                // If the right operand is a reference, check if it's assigned from a nullable source
                System.out.println(assignStmt);
                System.out.println("Type : " + assignStmt.getRightOp().getClass());
                //if (in.contains((Local) assignStmt.getRightOp())) {
                  //  out.add(leftOp);
                //}
            }
        }
    }

    @Override
    protected void merge(FlowSet<Local> in1, FlowSet<Local> in2, FlowSet<Local> out) {
        // Merge operation: logical AND (&&)
        System.out.println("In1: "+in1);
        System.out.println("In2: "+in2);
        in1.intersection(in2, out);
        System.out.println("MERGE: "+ out);
    }

    @Override
    protected void copy(FlowSet<Local> source, FlowSet<Local> dest) {
        source.copy(dest);
    }

    // Method to retrieve analysis results
    public Map<Unit, FlowSet<Local>> getUnitToBeforeFlow() {
        return unitToBeforeFlow;
    }
    public Map<Unit, FlowSet<Local>> getUnitToAfterFlow() {
        return unitToAfterFlow;
    }

    @Override
    public FlowSet<Local> getFlowAfter(Unit s) {
        return super.getFlowAfter(s);
    }
}
*/

public class PointerAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Pair<Local, Boolean>>> {
    private Map<String, List<Integer>> potentialNullDereferences;
    public PointerAnalysis(ExceptionalUnitGraph graph) {
        super(graph);
        potentialNullDereferences = new HashMap<>();
        doAnalysis();
    }

    @Override
    protected FlowSet<Pair<Local, Boolean>> newInitialFlow() {
        // Initialize with the bottom element (not nullable)
        FlowSet<Pair<Local, Boolean>> initFlow = new ArraySparseSet<>();
        return initFlow;
    }

    @Override
    protected FlowSet<Pair<Local, Boolean>> entryInitialFlow() {
        // Initialize with the bottom element (not nullable)
        FlowSet<Pair<Local, Boolean>> entryFlow = new ArraySparseSet<>();
        return entryFlow;
    }

    @Override
    protected void flowThrough(FlowSet<Pair<Local, Boolean>> in, Unit unit, FlowSet<Pair<Local, Boolean>> out) {
        in.copy(out); // Copy elements from the input to the output flow set

        // Retrieve the line number of the current unit
        int lineNumber = ((Stmt) unit).getJavaSourceStartLineNumber();

        if (unit instanceof AssignStmt assignStmt) {
            Local leftOp = (Local) assignStmt.getLeftOp();
            if (assignStmt.getRightOp() instanceof NullConstant) {
                // If the right operand is a null constant, mark the left operand as nullable
                out.add(new Pair<>(leftOp, true));
                // Print line number for potential null assignment
                System.out.println("Potential null assignment at line " + lineNumber);
            }
        }
        else {
            Stmt stmt = (Stmt) unit;// Analyze variable usages
            for (ValueBox valueBox : stmt.getUseBoxes()) {
                if (valueBox.getValue() instanceof Local variable) {
                    // Check if the variable is marked as nullable
                    for (Pair<Local, Boolean> pair : in) {
                        if (pair.getO1().equals(variable) && pair.getO2()) {
                            // Print line number for potential null dereference
                            System.out.println("Potential null dereference at line " + lineNumber);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void merge(FlowSet<Pair<Local, Boolean>> in1, FlowSet<Pair<Local, Boolean>> in2, FlowSet<Pair<Local, Boolean>> out) {
        // Merge operation: logical OR (||)
        in1.union(in2, out);
    }

    @Override
    protected void copy(FlowSet<Pair<Local, Boolean>> source, FlowSet<Pair<Local, Boolean>> dest) {
        source.copy(dest);
    }

    // Method to retrieve analysis results
    public Map<Unit, FlowSet<Pair<Local, Boolean>>> getUnitToBeforeFlow() {
        return unitToBeforeFlow;
    }
}
