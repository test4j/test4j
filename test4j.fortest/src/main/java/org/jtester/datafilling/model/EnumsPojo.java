package org.jtester.datafilling.model;

import java.io.Serializable;

import org.jtester.datafilling.utils.ExternalRatePodamEnum;

public class EnumsPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private RatePodamInternal ratePodamInternal;

	private ExternalRatePodamEnum ratePodamExternal;

	public RatePodamInternal getRatePodamInternal() {
		return ratePodamInternal;
	}

	public void setRatePodamInternal(RatePodamInternal ratePodamInternal) {
		this.ratePodamInternal = ratePodamInternal;
	}

	public ExternalRatePodamEnum getRatePodamExternal() {
		return ratePodamExternal;
	}

	public void setRatePodamExternal(ExternalRatePodamEnum ratePodamExternal) {
		this.ratePodamExternal = ratePodamExternal;
	}

	public enum RatePodamInternal {
		COOL, ROCKS, SUPERCOOL
	}
}
