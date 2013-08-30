package org.jtester.spec.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jtester.fortest.beans.User;
import org.jtester.junit.JSpec;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StorySource;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.annotations.When;

@SuppressWarnings({ "serial", "rawtypes" })
@StoryFile(value = "ParameterConvertDemo.xml", type = StoryType.XML, source = StorySource.ClassPath)
public class ParameterConvertXMLDemo extends JSpec {

    @When
    public void convertToIntArray(final @Named("数组") Integer[] intArray// <br>
    ) throws Exception {
        want.array(intArray).sizeEq(3).reflectionEq(new int[] { 1, 2, 3 });
    }

    @When
    public void convertToBoolArray(final @Named("数组") Boolean[][] arrBoolean// <br>
    ) throws Exception {
        want.array(arrBoolean).reflectionEq(new Boolean[][] { { true, false }, { true, true } });
    }

    @When
    public void convertToList(final @Named("列表") ArrayList<Boolean> list// <br>
    ) throws Exception {
        want.list(list).reflectionEq(new boolean[] { true, false });
    }

    @When
    public void convertToListGeneric(final @Named("列表") List<User> users// <br>
    ) throws Exception {
        want.list(users).sizeEq(2).reflectionEqMap(2, new DataMap() {
            {
                this.put("first", "wu", "he");
                this.put("last", "davey", "jobs");
            }
        });
    }

    @When
    public void convertToListHashmap(final @Named("列表") List list// <br>
    ) throws Exception {
        want.list(list).sizeEq(2).reflectionEqMap(2, new DataMap() {
            {
                this.put("first", "wu", "he");
                this.put("last", "davey", "jobs");
            }
        });
    }

    @When
    public void convetToDto(final @Named("用户") User user// <br>
    ) throws Exception {
        want.object(user).reflectionEqMap(new DataMap() {
            {
                this.put("first", "wu");
                this.put("last", "davey");
            }
        });
    }

    @When
    public void convertToPrimitiveType(final @Named("布尔值") Boolean bool, // <br>
                                       final @Named("整型") Integer intValue, // <br>
                                       final @Named("长整型") Long longValue, // <br>
                                       final @Named("短整型") Short shortValue, // <br>
                                       final @Named("浮点数") Float floatValue, // <br>
                                       final @Named("双精度") Double doubleValue, // <br>
                                       final @Named("字符串") String str// <br>
    ) throws Exception {
        want.bool(bool).is(true);
        want.number(intValue).isEqualTo(123);
        want.number(longValue).isEqualTo(234);
        want.number(shortValue).isEqualTo(23);
        want.number(floatValue).isEqualTo(23.12);
        want.number(doubleValue).isEqualTo(234.23);
        want.string(str).isEqualTo("I am a String");
    }

    @When
    public void convertToBigDecimalAndBigInteger(final @Named("大整数") BigInteger bigInteger, // <br>
                                                 final @Named("大数字") BigDecimal bigDecimal// <br>
    ) throws Exception {
        want.number(bigInteger).isEqualTo(new BigInteger("123456789123456789"));
        want.number(bigDecimal).isEqualTo(new BigDecimal("123456789123456789.12345"));
    }

    @When
    public void convertToMap(final @Named("DataMap") DataMap dataMap, // <br>
                             final @Named("HashMap") HashMap hashMap// <br>
    ) throws Exception {
        want.map(dataMap).reflectionEqMap(new DataMap() {
            {
                this.put("first", "wu");
                this.put("last", "davey");
            }
        });
        want.map(hashMap).reflectionEqMap(new DataMap() {
            {
                this.put("first", "wu");
                this.put("last", "davey");
            }
        });
    }

    @When
    public void convertDate(final @Named("日期") Date date, // <br>
                            final @Named("时间") Date time, // <br>
                            final @Named("日期加时间") Date dateTime, // <br>
                            final @Named("java_sql_Date") java.sql.Date java_sql_Date, // <br>
                            final @Named("java_sql_Timestamp") java.sql.Timestamp java_sql_Timestamp// <br>
    ) throws Exception {
        want.date(date).eqByFormat("2010-12-20");
        want.date(time).eqByFormat("12:30:45");
        want.date(dateTime).eqByFormat("2010-04-14 14:23:57");
        want.date(java_sql_Date).eqByFormat("2010-04-14");
        want.date(java_sql_Timestamp).eqByFormat("2010-04-14 14:23:57");
    }
}
