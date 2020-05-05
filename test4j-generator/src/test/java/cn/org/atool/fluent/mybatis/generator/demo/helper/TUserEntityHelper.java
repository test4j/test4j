package cn.org.atool.fluent.mybatis.generator.demo.helper;

import cn.org.atool.fluent.mybatis.generator.demo.entity.TUserEntity;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.TUserMP;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 */
public class TUserEntityHelper implements TUserMP{

    public static Map<String, Object> map(TUserEntity entity){
        Map<String, Object> map = new HashMap<>();
        {
            map.put(Property.id, entity.getId());
            map.put(Property.gmtCreated, entity.getGmtCreated());
            map.put(Property.gmtModified, entity.getGmtModified());
            map.put(Property.isDeleted, entity.getIsDeleted());
            map.put(Property.addressId, entity.getAddressId());
            map.put(Property.age, entity.getAge());
            map.put(Property.userName, entity.getUserName());
            map.put(Property.version, entity.getVersion());
        }
        return map;
    }

    public static TUserEntity entity(Map<String, Object> map){
        TUserEntity entity = new TUserEntity();
        {
            entity.setId((Long)map.get(Property.id));
            entity.setGmtCreated((Date)map.get(Property.gmtCreated));
            entity.setGmtModified((Date)map.get(Property.gmtModified));
            entity.setIsDeleted((Integer)map.get(Property.isDeleted));
            entity.setAddressId((Long)map.get(Property.addressId));
            entity.setAge((Integer)map.get(Property.age));
            entity.setUserName((String)map.get(Property.userName));
            entity.setVersion((String)map.get(Property.version));
        }
        return entity;
    }

    public static TUserEntity copy(TUserEntity entity) {
        TUserEntity copy = new TUserEntity();
        {
            copy.setId(entity.getId());
            copy.setGmtCreated(entity.getGmtCreated());
            copy.setGmtModified(entity.getGmtModified());
            copy.setIsDeleted(entity.getIsDeleted());
            copy.setAddressId(entity.getAddressId());
            copy.setAge(entity.getAge());
            copy.setUserName(entity.getUserName());
            copy.setVersion(entity.getVersion());
        }
        return copy;
    }
}