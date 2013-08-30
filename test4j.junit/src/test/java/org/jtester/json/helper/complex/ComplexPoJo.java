package org.jtester.json.helper.complex;

import java.io.Serializable;
import java.util.Date;

public class ComplexPoJo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Date gmtCreate;
	private String creator;
	private Date gmtModified;
	private String modifier;
	private String isDeleted;
	private Integer version;
	private String keyName;
	private Long procinst;
	private Long actinst;
	private Date dateValue;
	private Long longValue;
	private String stringValue;
	private String textValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Long getProcinst() {
		return procinst;
	}

	public void setProcinst(Long procinst) {
		this.procinst = procinst;
	}

	public Long getActinst() {
		return actinst;
	}

	public void setActinst(Long actinst) {
		this.actinst = actinst;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
}
