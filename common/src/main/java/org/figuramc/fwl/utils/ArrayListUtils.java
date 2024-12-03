package org.figuramc.fwl.utils;

import java.util.Comparator;
import java.util.List;

public class ArrayListUtils {
    public static <O> int sortedAdd(List<O> list, O object, Comparator<O> comparator) {
        int i = 0;
        int size = list.size();
        for (; i < size; i++) {
            O listObject = list.get(i);
            if (comparator.compare(object, listObject) > 0) {
                break;
            }
        }
        list.add(i, object);
        return i;
    }
}
