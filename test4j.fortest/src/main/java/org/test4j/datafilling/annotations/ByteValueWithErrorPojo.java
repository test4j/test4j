package org.test4j.datafilling.annotations;

import org.test4j.datafilling.annotations.FillByte;

public class ByteValueWithErrorPojo {
	@FillByte(value = "afasdfafa", comment = "This should throw an exception")
	private byte byteAttributeWithErrorAnnotation;

	/**
	 * @return the byteAttributeWithErrorAnnotation
	 */
	public byte getByteAttributeWithErrorAnnotation() {
		return byteAttributeWithErrorAnnotation;
	}

	/**
	 * @param byteAttributeWithErrorAnnotation
	 *            the byteAttributeWithErrorAnnotation to set
	 */
	public void setByteAttributeWithErrorAnnotation(byte byteAttributeWithErrorAnnotation) {
		this.byteAttributeWithErrorAnnotation = byteAttributeWithErrorAnnotation;
	}
}
