package edu.arizona.rice.kew.routeheader;

import javax.persistence.Entity;

import edu.arizona.rice.kew.api.routeheader.DocumentRouteHeaderValueContract;

/**
 * This class is overriden in order to expose DocumentRouteHeaderValue as an
 * Externalizable Business Object (EBO). This then makes the underlying BO
 * avaiable to the KSB, thus available to KFS while Rice is in standalone
 * mode.
 */
@Entity
public class DocumentRouteHeaderValueEboImpl extends org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue implements DocumentRouteHeaderValueEbo, DocumentRouteHeaderValueContract {
    private static final long serialVersionUID = 4527977459578527106L;

    public static edu.arizona.rice.kew.api.routeheader.DocumentRouteHeaderValue to(DocumentRouteHeaderValueEboImpl documentRouteHeaderValue) {
        if (documentRouteHeaderValue == null) {
            return null;
        }
        edu.arizona.rice.kew.api.routeheader.DocumentRouteHeaderValue.Builder builder = edu.arizona.rice.kew.api.routeheader.DocumentRouteHeaderValue.Builder.create(documentRouteHeaderValue);

        return builder.build();
    }


    public static DocumentRouteHeaderValueEboImpl from(DocumentRouteHeaderValueContract contract) {
        if (contract == null) {
            return null;
        }

        DocumentRouteHeaderValueEboImpl docRouteHeader = new DocumentRouteHeaderValueEboImpl();
        docRouteHeader.setDocumentId(contract.getDocumentId());
        docRouteHeader.setDocumentTypeId(contract.getDocumentTypeId());
        docRouteHeader.setDocRouteStatus(contract.getDocRouteStatus());
        docRouteHeader.setFinalizedDate(contract.getFinalizedDate());

        return docRouteHeader;
    }


    @Override
    public String getId() {
        return getDocumentId();
    }

}
