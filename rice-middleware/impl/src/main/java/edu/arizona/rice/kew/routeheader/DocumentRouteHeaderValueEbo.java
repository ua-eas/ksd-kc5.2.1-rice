package edu.arizona.rice.kew.routeheader;

import org.kuali.rice.krad.bo.ExternalizableBusinessObject;

public interface DocumentRouteHeaderValueEbo extends ExternalizableBusinessObject {

    String getDocumentId();

    String getDocumentTypeId();

    String getDocRouteStatus();

    java.sql.Timestamp getFinalizedDate();
}
