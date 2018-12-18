package edu.arizona.rice.kew.api.routeheader;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

public interface DocumentRouteHeaderValueContract extends Identifiable, Versioned {

    String getDocumentId();

    String getDocumentTypeId();

    String getDocRouteStatus();

    java.sql.Timestamp getFinalizedDate();
}
