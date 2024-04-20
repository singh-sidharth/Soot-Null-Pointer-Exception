package com.npe;

import soot.*;
import soot.options.Options;

public class App {
    public static void main(String[] args) {
        // Set up Soot options
        soot.G.v();
        G.reset();
        Options.v().set_keep_line_number(true);

        // Setup classpath
        String classpathSeparator = System.getProperty("path.separator");
        Options.v().set_soot_classpath("." + classpathSeparator + "target/classes"
                + classpathSeparator + "VIRTUAL_FS_FOR_JDK");

        // Necessary classes
        final var checker = new AnalysisTransformer();
        final var transform = new Transform("jtp.NullChecker", checker);

        // Add your Soot transformation here
        // For example:
        PackManager.v().getPack("jtp").add(transform);

        // Get Scene
        Scene.v().addBasicClass("com.npe.Circle",
                SootClass.SIGNATURES);

        // Run Soot
        soot.Main.main(args);
    }
}
