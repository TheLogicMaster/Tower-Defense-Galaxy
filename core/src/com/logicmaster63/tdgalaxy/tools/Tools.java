package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tools {

    public static <T> T[] concatenate(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getImplements(List list, Class<T> type) {
        List<T> newList = new ArrayList<T>();
        for(Object o: list)
            if(type.isInstance(o))
                newList.add((T) o);
        return newList;
    }

    public static int[] doubleParseInt(String string) {
        char c = 'a';
        int increment = 0;
        while(c != ' ') {
            c = string.charAt(increment);
            increment++;
        }
        return new int[]{Integer.parseInt(string.substring(0, increment - 1)), Integer.parseInt(string.substring(increment, string.length()))};
    }

    public static int[] trippleParseInt(String string) {
        char c = '?';
        int increment0 = 0;
        while (c != ' ') {
            c = string.charAt(increment0);
            increment0++;
        }
        c = '?';
        int increment1 = increment0 + 1;
        while (c != ' ') {
            c = string.charAt(increment1);
            increment1++;
        }
        return new int[]{Integer.parseInt(string.substring(0, increment0 - 1)), Integer.parseInt(string.substring(increment0, increment1 - 1)), Integer.parseInt(string.substring(increment1, string.length()))};
    }

    public static Vector3 calculateBezierPoint(float t, Vector3 p0, Vector3 p1, Vector3 p2) {
        float u = 1 - t;
        float tt = t*t;
        float uu = u*u;
        float uuu = uu * u;
        Vector3 p = p0.scl(uuu); //first term
        p = p.add(p1.scl(3 * uu * t)); //second term
        p = p.add(p2.scl(3 * u * tt)); //third term
        //p = p.add(p3.scl(ttt)); //fourth term           Can one just remove this to create a quadratic curve?
        return p;
    }
}
