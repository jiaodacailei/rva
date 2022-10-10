package com.ruoyi.rva.framework.domain;

import java.util.ArrayList;
import java.util.Collection;

public class RvaList<E> extends ArrayList<E> {

    public RvaList() {
        super();
    }

    public RvaList(E... es) {
        super();
        for (E e : es) {
            add(e);
        }
    }

    public RvaList(Collection<E> es) {
        super();
        addAll(es);
    }

    public RvaList<E> rvaAdd (E e) {
        add(e);
        return this;
    }

    public RvaList<E> rvaAddAll (Collection<E> es) {
        addAll(es);
        return this;
    }
}
