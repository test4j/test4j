package org.jtester.json.encoder;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.regex.Pattern;

import org.jtester.json.encoder.single.FixedTypeEncoder;
import org.jtester.json.encoder.single.SpecTypeEncoder;
import org.jtester.json.encoder.single.fixed.BigDecimalEncoder;
import org.jtester.json.encoder.single.fixed.BigIntegerEncoder;
import org.jtester.json.encoder.single.fixed.BooleanEncoder;
import org.jtester.json.encoder.single.fixed.ByteEncoder;
import org.jtester.json.encoder.single.fixed.CharEncoder;
import org.jtester.json.encoder.single.fixed.CharsetEncoder;
import org.jtester.json.encoder.single.fixed.ClazzEncoder;
import org.jtester.json.encoder.single.fixed.DoubleEncoder;
import org.jtester.json.encoder.single.fixed.EnumEncoder;
import org.jtester.json.encoder.single.fixed.FloatEncoder;
import org.jtester.json.encoder.single.fixed.InetAddressEncoder;
import org.jtester.json.encoder.single.fixed.IntegerEncoder;
import org.jtester.json.encoder.single.fixed.LocaleEncoder;
import org.jtester.json.encoder.single.fixed.LongEncoder;
import org.jtester.json.encoder.single.fixed.PatternEncoder;
import org.jtester.json.encoder.single.fixed.ShortEncoder;
import org.jtester.json.encoder.single.fixed.SocketAddressEncoder;
import org.jtester.json.encoder.single.fixed.StringEncoder;
import org.jtester.json.encoder.single.fixed.TimeZoneEncoder;
import org.jtester.json.encoder.single.fixed.URIEncoder;
import org.jtester.json.encoder.single.fixed.URLEncoder;
import org.jtester.json.encoder.single.fixed.UUIDEncoder;
import org.jtester.json.encoder.single.spec.AppendableEncoder;
import org.jtester.json.encoder.single.spec.AtomicBooleanEncoder;
import org.jtester.json.encoder.single.spec.AtomicIntegerArrayEncoder;
import org.jtester.json.encoder.single.spec.AtomicIntegerEncoder;
import org.jtester.json.encoder.single.spec.AtomicLongArrayEncoder;
import org.jtester.json.encoder.single.spec.AtomicLongEncoder;
import org.jtester.json.encoder.single.spec.AtomicReferenceArrayEncoder;
import org.jtester.json.encoder.single.spec.AtomicReferenceEncoder;
import org.jtester.json.encoder.single.spec.DateEncoder;
import org.jtester.json.encoder.single.spec.FileEncoder;
import org.jtester.json.encoder.single.spec.SimpleDateFormatEncoder;

@SuppressWarnings("rawtypes")
public abstract class SingleEncoder<T> extends JSONEncoder<T> {

	protected SingleEncoder(Class clazz) {
		super(clazz);
	}

	public static FixedTypeEncoder isFinalTypeEncoder(Class type) {
		if (type == String.class) {
			return StringEncoder.instance;
		}
		if (type.isEnum()) {
			return EnumEncoder.instance;
		}

		if (type == boolean.class || type == Boolean.class) {
			return BooleanEncoder.instance;
		}
		if (type == byte.class || type == Byte.class) {
			return ByteEncoder.instance;
		}
		if (type == char.class || type == Character.class) {
			return CharEncoder.instance;
		}
		if (type == Class.class) {
			return ClazzEncoder.instance;
		}

		// ====number decoder
		if (type == double.class || type == Double.class) {
			return DoubleEncoder.instance;
		}
		if (type == float.class || type == Float.class) {
			return FloatEncoder.instance;
		}
		if (type == int.class || type == Integer.class) {
			return IntegerEncoder.instance;
		}
		if (type == long.class || type == Long.class) {
			return LongEncoder.instance;
		}
		if (type == short.class || type == Short.class) {
			return ShortEncoder.instance;
		}

		if (type == Locale.class) {
			return LocaleEncoder.instance;
		}
		if (type == Pattern.class) {
			return PatternEncoder.instance;
		}
		if (type == URI.class) {
			return URIEncoder.instance;
		}
		if (type == URL.class) {
			return URLEncoder.instance;
		}
		if (type == UUID.class) {
			return UUIDEncoder.instance;
		}

		// not final type
		if (TimeZone.class.isAssignableFrom(type)) {
			return TimeZoneEncoder.instance;
		}
		if (InetAddress.class.isAssignableFrom(type)) {
			return InetAddressEncoder.instance;
		}
		if (BigDecimal.class.isAssignableFrom(type)) {
			return BigDecimalEncoder.instance;
		}
		if (BigInteger.class.isAssignableFrom(type)) {
			return BigIntegerEncoder.instance;
		}
		if (Charset.class.isAssignableFrom(type)) {
			return CharsetEncoder.instance;
		}
		if (SocketAddress.class.isAssignableFrom(type)) {
			return SocketAddressEncoder.instance;
		}
		return null;
	}

	public static SpecTypeEncoder isSpecTypeEncoder(Class type) {
		if (Appendable.class.isAssignableFrom(type)) {
			return AppendableEncoder.instance;
		}
		if (Date.class.isAssignableFrom(type)) {
			return DateEncoder.instance;
		}
		if (File.class.isAssignableFrom(type)) {
			return FileEncoder.instance;
		}
		if (AtomicBoolean.class.isAssignableFrom(type)) {
			return AtomicBooleanEncoder.instance;
		}
		if (AtomicInteger.class.isAssignableFrom(type)) {
			return AtomicIntegerEncoder.instance;
		}
		if (AtomicIntegerArray.class.isAssignableFrom(type)) {
			return AtomicIntegerArrayEncoder.instance;
		}
		if (AtomicLong.class.isAssignableFrom(type)) {
			return AtomicLongEncoder.instance;
		}
		if (AtomicLongArray.class.isAssignableFrom(type)) {
			return AtomicLongArrayEncoder.instance;
		}
		if (AtomicReference.class.isAssignableFrom(type)) {
			return AtomicReferenceEncoder.instance;
		}
		if (AtomicReferenceArray.class.isAssignableFrom(type)) {
			return AtomicReferenceArrayEncoder.instance;
		}
		if (SimpleDateFormat.class.isAssignableFrom(type)) {
			return SimpleDateFormatEncoder.instance;
		}
		return null;
	}
}
