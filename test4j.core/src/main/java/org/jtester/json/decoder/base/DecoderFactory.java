package org.jtester.json.decoder.base;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.jtester.json.decoder.ArrayDecoder;
import org.jtester.json.decoder.CollectionDecoder;
import org.jtester.json.decoder.IDecoder;
import org.jtester.json.decoder.MapDecoder;
import org.jtester.json.decoder.PoJoDecoder;
import org.jtester.json.decoder.single.AtomicBooleanDecoder;
import org.jtester.json.decoder.single.AtomicIntegerDecoder;
import org.jtester.json.decoder.single.AtomicLongDecoder;
import org.jtester.json.decoder.single.BigDecimalDecoder;
import org.jtester.json.decoder.single.BigIntegerDecoder;
import org.jtester.json.decoder.single.BooleanDecoder;
import org.jtester.json.decoder.single.ByteDecoder;
import org.jtester.json.decoder.single.CharDecoder;
import org.jtester.json.decoder.single.CharsetDecoder;
import org.jtester.json.decoder.single.ClazzDecoder;
import org.jtester.json.decoder.single.DoubleDecoder;
import org.jtester.json.decoder.single.FileDecoder;
import org.jtester.json.decoder.single.FloatDecoder;
import org.jtester.json.decoder.single.InetAddressDecoder;
import org.jtester.json.decoder.single.IntegerDecoder;
import org.jtester.json.decoder.single.LocaleDecoder;
import org.jtester.json.decoder.single.LongDecoder;
import org.jtester.json.decoder.single.PatternDecoder;
import org.jtester.json.decoder.single.ShortDecoder;
import org.jtester.json.decoder.single.SimpleDateFormatDecoder;
import org.jtester.json.decoder.single.SocketAddressDecoder;
import org.jtester.json.decoder.single.StringDecoder;
import org.jtester.json.decoder.single.TimeZoneDecoder;
import org.jtester.json.decoder.single.URIDecoder;
import org.jtester.json.decoder.single.URLDecoder;
import org.jtester.json.decoder.single.UUIDDecoder;
import org.jtester.json.decoder.single.spec.AppendableDecoder;
import org.jtester.json.decoder.single.spec.DateDecoder;
import org.jtester.json.decoder.single.spec.EnumDecoder;

@SuppressWarnings("serial")
public class DecoderFactory {

	public static List<IDecoder> decoders = new ArrayList<IDecoder>() {
		{
			// 简单对象
			this.add(StringDecoder.toSTRING);
			this.add(BooleanDecoder.toBOOLEAN);
			this.add(ByteDecoder.toBYTE);
			this.add(CharDecoder.toCHARACTER);
			this.add(ClazzDecoder.toCLASS);
			this.add(DoubleDecoder.toDOUBLE);
			this.add(FloatDecoder.toFLOAT);
			this.add(IntegerDecoder.toINTEGER);
			this.add(LongDecoder.toLONG);
			this.add(ShortDecoder.toSHORT);
			this.add(LocaleDecoder.toLOCAL);
			this.add(PatternDecoder.toPATTERN);
			this.add(URLDecoder.toURL);
			this.add(URIDecoder.toURI);
			this.add(UUIDDecoder.toUUID);
			this.add(TimeZoneDecoder.toTimeZone);
			this.add(InetAddressDecoder.toINETADDRESS);
			this.add(BigDecimalDecoder.toBIGDECIMAL);
			this.add(BigIntegerDecoder.toBIGINTEGER);
			this.add(CharsetDecoder.toCHARSET);
			this.add(SocketAddressDecoder.toSOCKETADDRESS);
			this.add(FileDecoder.toFILE);
			this.add(SimpleDateFormatDecoder.toSIMPLEDATEFORMAT);
			this.add(AtomicBooleanDecoder.toATOMICBOOLEAN);
			this.add(AtomicIntegerDecoder.toATOMICINTEGER);
			this.add(AtomicLongDecoder.toATOMICLONG);
			//
			this.add(EnumDecoder.toENUM);
			this.add(DateDecoder.toDATE);
			this.add(AppendableDecoder.toAPPENDABLE);
			// array, collection, map, pojo
			this.add(ArrayDecoder.toARRAY);
			this.add(CollectionDecoder.toCOLLECTION);
			this.add(MapDecoder.toMAP);
			this.add(PoJoDecoder.toPOJO);

		}
	};

	public static IDecoder getDecoder(Type type) {
		for (IDecoder decoder : decoders) {
			if (decoder.accept(type)) {
				return decoder;
			}
		}
		return MapDecoder.toMAP;
	}

}
