package com.npe.reporter;

public interface Reporter {
    public void reportError(String variableName,
                            int lineNumber,
                            String methodName);
}