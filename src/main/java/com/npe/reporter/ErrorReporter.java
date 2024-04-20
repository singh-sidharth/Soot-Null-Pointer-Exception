package com.npe.reporter;

public class ErrorReporter implements Reporter{
    @Override
    public void reportError(String variableName, int lineNumber, String methodName) {
        System.out.printf("In method [%s] at line: %d, error with variable: %s%n", methodName, lineNumber, variableName);
    }
}
