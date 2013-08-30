package org.test4j.datafilling.annotations;

import org.test4j.datafilling.annotations.FillWith;
import org.test4j.datafilling.strategies.WrongTypeStrategy;
public class StringWithWrongStrategyTypePojo {

	@FillWith(value = WrongTypeStrategy.class)
	private String postCodeDestinedToFail;

	/**
	 * @return the postCodeDestinedToFail
	 */
	public String getPostCodeDestinedToFail() {
		return postCodeDestinedToFail;
	}

	/**
	 * @param postCodeDestinedToFail
	 *            the postCodeDestinedToFail to set
	 */
	public void setPostCodeDestinedToFail(String postCodeDestinedToFail) {
		this.postCodeDestinedToFail = postCodeDestinedToFail;
	}
}
