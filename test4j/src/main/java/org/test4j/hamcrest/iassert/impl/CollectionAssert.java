package org.test4j.hamcrest.iassert.impl;

import java.util.Collection;

import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.ICollectionAssert;

@SuppressWarnings("rawtypes")
public class CollectionAssert extends Assert<Collection, ICollectionAssert> implements ICollectionAssert {

    public CollectionAssert() {
        super(Collection.class, ICollectionAssert.class);
    }

    public CollectionAssert(Collection value) {
        super(value, Collection.class, ICollectionAssert.class);
    }
}
