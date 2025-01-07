package com.is.uno.core;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class CircularList<T> extends LinkedList<T> {
    private ListIterator<T> iterator;

    public CircularList() {
        super();
        this.iterator = listIterator();
    }

    public T next() {
        if (!iterator.hasNext()) {
            iterator = listIterator(); // Сброс итератора на начало списка
        }
        return iterator.next();
    }

    public T previous() {
        if (!iterator.hasPrevious()) {
            iterator = listIterator(size()); // Сброс итератора на конец списка
        }
        return iterator.previous();
    }

    public void startFrom(T elem) {
        int pos = 0;
        for (T t : this) {
            if (t.equals(elem)) {
                iterator = listIterator(pos);
                return;
            }
            pos++;
        }
        throw new NoSuchElementException();
    }

}
