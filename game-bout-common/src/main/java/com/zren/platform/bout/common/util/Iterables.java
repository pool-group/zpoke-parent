package com.zren.platform.bout.common.util;


import java.util.function.BiConsumer;

/**
 *@author gavin
 *@since 2018年12月4日 下午4:50:54
 *@迭代器
*/
public class Iterables {

    public static <E> void forEach(
            Iterable<? extends E> elements, BiConsumer<Integer, ? super E> action) {
        java.util.Objects.requireNonNull(elements);
        java.util.Objects.requireNonNull(action);

        int index = 0;
        for (E element : elements) {
            action.accept(index++, element);
        }
    }
}
