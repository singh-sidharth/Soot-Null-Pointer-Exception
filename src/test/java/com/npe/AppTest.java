package com.npe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soot.*;
import soot.options.Options;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @BeforeEach
    void initializeSoot() {
        soot.G.v();
        G.reset();
        Options.v().set_keep_line_number(true);
        Options.v().setPhaseOption("wjop", "enabled:false"); // Disable Whole-Jimple Optimization Pack
        Options.v().setPhaseOption("cg", "enabled:false");   // Disable Call Graph construction
        Options.v().setPhaseOption("wjop", "enabled:false"); // Disable Whole-Jimple Optimization Pack
        Options.v().setPhaseOption("dce", "enabled:false");  // Disable Dead Code Elimination
        String classpathSeparator = System.getProperty("path.separator");
        Options.v().set_soot_classpath("." + classpathSeparator + "target/classes" + classpathSeparator + "VIRTUAL_FS_FOR_JDK");
        final var checker = new AnalysisTransformer();
        final var transform = new Transform("jtp.NullChecker", checker);
        PackManager.v().getPack("jtp").add(transform);
        Scene.v().addBasicClass("ca.npe.Circle",
                SootClass.SIGNATURES);
    }

    @Test
    @DisplayName("Check the error for Basic Sanity")
    void testSanity() {
        // Load the class containing the method to be analyzed
        final String exampleName = "com.npe.examples.Test_00_Nullness";
        final String[] args = new String[]{exampleName};

        soot.Main.main(args);
    }

    @Test
    @DisplayName("Check the error for basic creation")
    void testBasic() {
        // Load the class containing the method to be analyzed
        final String exampleName = "com.npe.examples.Test_01_Basic";
        final String[] args = new String[]{exampleName};

        soot.Main.main(args);
    }

    @Test
    @DisplayName("Check the error for possible conditional null.")
    void testConditional() {
        // Load the class containing the method to be analyzed
        final String exampleName = "com.npe.examples.Test_02_Conditional";
        final String[] args = new String[]{exampleName};

        soot.Main.main(args);
    }
}

