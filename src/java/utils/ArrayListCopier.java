/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 *
 * @author Gabriel Alves
 */
public class ArrayListCopier {

    public static <T> Collector<T, ?, ArrayList<T>> toArrayList() {
        return Collectors.toCollection(ArrayList::new);
    }

    public static ArrayList copyList(java.util.List old) {
        ArrayList ret = new ArrayList();

        for (Object o : old) {
            try {
                Object n = o.getClass().newInstance();
                int id = (int) o.getClass().getMethod("getId", new Class[]{}).invoke(o);
                n.getClass().getMethod("setId", new Class[]{Integer.class}).invoke(n, id);
                ret.add(n);
            } catch (SecurityException | InstantiationException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ArrayListCopier.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

        return ret;
    }
}
