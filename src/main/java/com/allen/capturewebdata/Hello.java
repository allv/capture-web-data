package com.allen.capturewebdata;

public class Hello extends Thread{

	public Hello() {
		 
    }
 
    public Hello(String name) {
        this.name = name;
    }
 
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(name + "运行     " + i);
        }
    }
 
    public static void main(String[] args) {
    	Hello h1=new Hello("A");
    	Hello h2=new Hello("B");
        h1.start();
        h2.start();
    }
 
    private String name;
}
