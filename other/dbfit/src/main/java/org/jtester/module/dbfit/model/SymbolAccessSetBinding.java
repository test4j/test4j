package org.jtester.module.dbfit.model;

import org.jtester.module.dbfit.utility.ParseArg;

import fit.Binding;
import fit.Fixture;
import fit.Parse;

public class SymbolAccessSetBinding extends Binding.SetBinding {

	@Override
	public void doCell(Fixture fixture, Parse cell) throws Throwable {
		String text = ParseArg.parseCellValue(cell);
		if ("".equals(text)) {
			fixture.handleBlankCell(cell, adapter);
		}
		Object o = adapter.parse(text);
		adapter.set(o);
	}
}
