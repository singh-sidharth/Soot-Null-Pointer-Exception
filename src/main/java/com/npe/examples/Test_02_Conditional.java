package com.npe.examples;

import com.npe.Circle;

public class Test_02_Conditional {

    public void test(boolean cond){
        Circle c = null;
        Circle c2 = new Circle(15);
        if(cond)
            c = new Circle(5);
        System.out.println(c);
        System.out.println(c2);
    }
}
