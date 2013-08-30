package org.jtester.datafilling.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BusinessObjectWithErrors<ID extends Serializable> extends BusinessObject<ID> {

	private List<String> errorCodeList = new ArrayList<String>();

	public List<String> getErrorCodeList() {
		return errorCodeList;
	}

	public void setErrorCodeList(List<String> allErrors) {
		this.errorCodeList = allErrors;
	}

}
