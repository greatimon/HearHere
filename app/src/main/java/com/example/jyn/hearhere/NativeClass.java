package com.example.jyn.hearhere;

/**
 * Created by JYN on 2017-08-11.
 */

public class NativeClass {
    public native static String getMessage();
    public native static long LandmarkDetection(long addrInput, long addrOutput);
}