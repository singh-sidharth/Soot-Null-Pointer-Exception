package com.npe;

import com.npe.analysis.PointerAnalysis;

import soot.*;
import soot.jimple.DefinitionStmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.Pair;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AnalysisTransformer extends BodyTransformer {

    private HashMap<String, List<String>> analysisDetails;

    AnalysisTransformer(){
        analysisDetails = new HashMap<>();
    }
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        // Create an ExceptionalUnitGraph for the body
        System.out.println("*****************************");
        System.out.printf("Method: "+body.getMethod().getName()+"\n");
        ExceptionalUnitGraph graph = new ExceptionalUnitGraph(body);

        var pointerAnalysis = new PointerAnalysis(graph);

        // Retrieve analysis results
        Map<Unit, FlowSet<Pair<Local, Boolean>>> results = pointerAnalysis.getUnitToBeforeFlow();

        // Iterate over analysis results
        for (Map.Entry<Unit, FlowSet<Pair<Local, Boolean>>> entry : results.entrySet()) {
            Unit unit = entry.getKey();
            FlowSet<Pair<Local, Boolean>> flowSet = entry.getValue();
            int lineNumber = unit.getJavaSourceStartLineNumber(); // Get the line number of the unit
            System.out.println("Line Number: " + lineNumber);
            System.out.println("Unit: " + unit);
            System.out.println("Variable States:");
            for (Pair<Local, Boolean> pair : flowSet) {
                Local variable = pair.getO1();
                Boolean isNullable = pair.getO2();
                System.out.println("Variable: " + variable + ", Nullable: " + isNullable);
            }
            System.out.println("-----------------------------------------");
        }

        System.out.println("<!*****************************!>");
    }

}
