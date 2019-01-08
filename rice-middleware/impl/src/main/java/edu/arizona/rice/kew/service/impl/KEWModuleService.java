package edu.arizona.rice.kew.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.rice.krad.bo.ExternalizableBusinessObject;

import edu.arizona.rice.kew.routeheader.DocumentRouteHeaderValueEboImpl;

public class KEWModuleService extends org.kuali.rice.kew.service.impl.KEWModuleService {

    @SuppressWarnings({"deprecation", "unchecked"})//EBO, cast of return type
    @Override
    public <T extends ExternalizableBusinessObject> List<T> getExternalizableBusinessObjectsList(Class<T> businessObjectClass, Map<String, Object> fieldValues) {
        if (businessObjectClass.isAssignableFrom(DocumentRouteHeaderValueEboImpl.class)) {
            Map<String, String> castValueMap = new HashMap<>();
            for (String key : fieldValues.keySet()) {
                castValueMap.put(key, (String)fieldValues.get(key));
            }

            return (List<T>) getLookupService().findCollectionBySearch(DocumentRouteHeaderValueEboImpl.class, castValueMap);
        } else {
            // Default to super when not our special case
            return super.getExternalizableBusinessObjectsList(businessObjectClass, fieldValues);
        }
    }

}
