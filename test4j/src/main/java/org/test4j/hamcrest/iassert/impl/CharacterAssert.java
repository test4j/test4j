package org.test4j.hamcrest.iassert.impl;

import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.ICharacterAssert;

public class CharacterAssert extends Assert<Character, ICharacterAssert> implements ICharacterAssert {

    public CharacterAssert() {
        super(Character.class, ICharacterAssert.class);
    }

    public CharacterAssert(Character value) {
        super(value, Character.class, ICharacterAssert.class);
    }

    public ICharacterAssert is(char ch) {
        return this.isEqualTo(ch);
    }
}
