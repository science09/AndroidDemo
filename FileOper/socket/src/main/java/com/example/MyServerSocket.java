package com.example;

public class MyServerSocket {

    public static void main(String[] args){
        System.out.println("ServerSocket Start...");
        new ServerListener().start();
    }
}



//Error:Gradle: A problem occurred configuring root project 'FileOper'.
//        > Could not resolve all dependencies for configuration ':classpath'.
//        > Could not download ST4.jar (org.antlr:ST4:4.0.8)
//        > Could not get resource 'https://jcenter.bintray.com/org/antlr/ST4/4.0.8/ST4-4.0.8.jar'.
//        > Could not GET 'https://jcenter.bintray.com/org/antlr/ST4/4.0.8/ST4-4.0.8.jar'.
//        > peer not authenticated

