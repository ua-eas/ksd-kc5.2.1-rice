package edu.arizona.rice.kew.routeheader;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kuali.rice.krad.bo.DataObjectBase;
import org.kuali.rice.krad.data.jpa.PortableSequenceGenerator;

import edu.arizona.rice.kew.api.routeheader.DocumentRouteHeaderValueContract;

/**
 * This class exposes several fields of DocumentRouteHeaderValue as an
 * Externalizable Business Object (EBO). This is then made avaiable
 * on the KSB, thus available to KFS while Rice is in standalone mode.
 */
@Entity
@Table(name="KREW_DOC_HDR_T")
public class DocumentRouteHeaderValueEboImpl extends DataObjectBase implements DocumentRouteHeaderValueEbo, DocumentRouteHeaderValueContract {
    private static final long serialVersionUID = 4527977459578527106L;

    @Id
    @GeneratedValue(generator = "KREW_DOC_HDR_S")
    @PortableSequenceGenerator(name = "KREW_DOC_HDR_S")
    @Column(name = "DOC_HDR_ID", nullable = false)
    private String documentId;

    @Column(name = "DOC_TYP_ID")
    private String documentTypeId;

    @Column(name = "DOC_HDR_STAT_CD", nullable = false)
    private String docRouteStatus;

    @Column(name = "FNL_DT")
    private Timestamp finalizedDate;


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
    public String getDocumentId() {
        return documentId;
    }


    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    @Override
    public String getDocumentTypeId() {
        return documentTypeId;
    }


    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }


    @Override
    public String getDocRouteStatus() {
        return docRouteStatus;
    }


    public void setDocRouteStatus(String docRouteStatus) {
        this.docRouteStatus = docRouteStatus;
    }


    @Override
    public Timestamp getFinalizedDate() {
        return finalizedDate;
    }


    public void setFinalizedDate(Timestamp finalizedDate) {
        this.finalizedDate = finalizedDate;
    }


    @Override
    public String getId() {
        return getDocumentId();
    }


    @SuppressWarnings("unused")
    public void setId(String id) {
        // Present due to interface, but we don't use it
    }


    @Override
    public void refresh() {
        // DocumentRouteHeaderValue has an empty impl, and so will we
    }

}
