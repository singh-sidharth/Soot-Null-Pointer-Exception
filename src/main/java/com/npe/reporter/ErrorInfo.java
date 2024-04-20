package com.npe.reporter;

class ErrorInfo{
    public String variable;
    public int line;
    public String methodName;
    public ErrorInfo(String variable, int line, String methodName) {
        this.variable = variable;
        this.line = line;
        this.methodName = methodName;
    }
}