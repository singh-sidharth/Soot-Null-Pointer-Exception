package com.npe;

public class Circle {
    private float radius;
    public Circle(float radius){
        this.radius = radius;
    }

    public float getRadius(){
        return this.radius;
    }

    public boolean setRadius(float r){
        this.radius = r;
        return true;
    }

    public double getArea(){
        return Math.PI * Math.pow(this.radius, 2);
    }

    public double getCircumference(){
        return 2*Math.PI*this.radius;
    }

    @Override
    public String toString() {
        return String.format("You have created a circle with radius: %.6f", radius);
    }
}
