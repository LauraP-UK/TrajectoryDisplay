package com.laura.playground.utils;

import java.util.ArrayList;
import java.util.Collection;

public class UniqueList<T> extends ArrayList<T> {

    public boolean add(T o) {
        return !super.contains(o) && super.add(o);
    }

    public boolean addAll(Collection<? extends T> c) {
        boolean addedAll = true;
        for (T o : c) {
            if (!this.add(o)) {
                addedAll = false;
            }
        }

        return addedAll;
    }

    public void add(int index, T o) {
        if (!super.contains(o)) {
            super.add(index, o);
        }
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        boolean canAddAll = true;

        for (T o : c) {
            if (super.contains(o)) {
                canAddAll = false;
                break;
            }
        }

        if (canAddAll) {
            super.addAll(index, c);
        }

        return canAddAll;
    }

    public ArrayList<T> asArrayList() {
        return new ArrayList<>(this);
    }
}
