package org.jtester.datafilling.annotations;

import java.io.Serializable;

import org.jtester.datafilling.annotations.FillBoolean;

@SuppressWarnings("serial")
public class BooleanValuePojo implements Serializable {

	/** A boolean field with value forced to true */
	@FillBoolean(value = true)
	private boolean boolDefaultToTrue;

	/** A boolean field with value forced to false */
	@FillBoolean(value = false)
	private boolean boolDefaultToFalse = true;

	/** A boolean object field with value forced to true */
	@FillBoolean(value = true)
	private Boolean boolObjectDefaultToTrue;

	/** A boolean object field with value forced to false */
	@FillBoolean(value = false)
	private Boolean boolObjectDefaultToFalse = true;

	/**
	 * @return the boolDefaultToTrue
	 */
	public boolean isBoolDefaultToTrue() {
		return boolDefaultToTrue;
	}

	/**
	 * @param boolDefaultToTrue
	 *            the boolDefaultToTrue to set
	 */
	public void setBoolDefaultToTrue(boolean boolDefaultToTrue) {
		this.boolDefaultToTrue = boolDefaultToTrue;
	}

	/**
	 * @return the boolDefaultToFalse
	 */
	public boolean isBoolDefaultToFalse() {
		return boolDefaultToFalse;
	}

	/**
	 * @param boolDefaultToFalse
	 *            the boolDefaultToFalse to set
	 */
	public void setBoolDefaultToFalse(boolean boolDefaultToFalse) {
		this.boolDefaultToFalse = boolDefaultToFalse;
	}

	/**
	 * @return the boolObjectDefaultToTrue
	 */
	public Boolean getBoolObjectDefaultToTrue() {
		return boolObjectDefaultToTrue;
	}

	/**
	 * @param boolObjectDefaultToTrue
	 *            the boolObjectDefaultToTrue to set
	 */
	public void setBoolObjectDefaultToTrue(Boolean boolObjectDefaultToTrue) {
		this.boolObjectDefaultToTrue = boolObjectDefaultToTrue;
	}

	/**
	 * @return the boolObjectDefaultToFalse
	 */
	public Boolean getBoolObjectDefaultToFalse() {
		return boolObjectDefaultToFalse;
	}

	/**
	 * @param boolObjectDefaultToFalse
	 *            the boolObjectDefaultToFalse to set
	 */
	public void setBoolObjectDefaultToFalse(Boolean boolObjectDefaultToFalse) {
		this.boolObjectDefaultToFalse = boolObjectDefaultToFalse;
	}
}
