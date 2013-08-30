package org.test4j.module.dbfit.model;

import org.test4j.module.dbfit.IDbFit;
import org.test4j.module.dbfit.model.SymbolAccessQueryBinding;
import org.test4j.testng.test4j;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class SymbolAccessQueryBindingTest extends test4j implements IDbFit {
    SymbolAccessQueryBinding binding = new SymbolAccessQueryBinding();

    @Test
    public void testSetSymbols() {
        fit.cleanSymbols();

        reflector.invoke(binding, "setSymbols", ">>myid[1]", "id", 1023, 1);
        Integer symbol = fit.getSymbol("myid[1]");
        want.number(symbol).isEqualTo(1023);
    }

    @Test
    public void testSetSymbols2() {
        fit.cleanSymbols();

        reflector.invoke(binding, "setSymbols", ">>myid[asdf]", "id", 1023, 1);
        Integer symbol = fit.getSymbol("myid[asdf]");
        want.number(symbol).isEqualTo(1023);

        Integer symbol2 = fit.getSymbol("myid[1]");
        want.object(symbol2).isNull();
    }

    @Test(expectedExceptions = StringIndexOutOfBoundsException.class)
    public void testSetSymbols3() {
        fit.cleanSymbols();

        reflector.invoke(binding, "setSymbols", ">>myid[", "id", 1023, 1);
        Integer symbol = fit.getSymbol("myid[1]");
        want.number(symbol).isEqualTo(1023);
    }

    @Test
    public void testSetSymbols4() {
        fit.cleanSymbols();

        reflector.invoke(binding, "setSymbols", ">>[1]", "id", 1023, 1);
        Integer symbol = fit.getSymbol("id[1]");
        want.number(symbol).isEqualTo(1023);
    }
}
