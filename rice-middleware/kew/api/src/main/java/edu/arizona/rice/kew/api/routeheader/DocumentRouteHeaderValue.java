package edu.arizona.rice.kew.api.routeheader;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kew.api.KewApiConstants;



@XmlRootElement(name = DocumentRouteHeaderValue.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentRouteHeaderValue.Constants.TYPE_NAME, propOrder = {
        DocumentRouteHeaderValue.Elements.ID,
        CoreConstants.CommonElements.VERSION_NUMBER,
        DocumentRouteHeaderValue.Elements.DOCUMENT_ID,
        DocumentRouteHeaderValue.Elements.DOCUMENT_TYPE_ID,
        DocumentRouteHeaderValue.Elements.DOC_ROUTE_STATUS,
        DocumentRouteHeaderValue.Elements.FINALIZED_DATE,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class DocumentRouteHeaderValue extends AbstractDataTransferObject implements DocumentRouteHeaderValueContract {
    private static final long serialVersionUID = -1234724890181802996L;

    @XmlElement(name = DocumentRouteHeaderValue.Elements.ID)
    private String id;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER)
    private Long versionNumber;
    @XmlElement(name = Elements.DOCUMENT_ID, required = true)
    private String documentId;
    @XmlElement(name = Elements.DOCUMENT_TYPE_ID, required = true)
    private String documentTypeId;
    @XmlElement(name = Elements.DOCUMENT_TYPE_ID, required = true)
    private String docRouteStatus;
    @XmlElement(name = Elements.FINALIZED_DATE)
    private Timestamp finalizedDate;


    /**
     * Private constructor used only by JAXB.
     */
    private DocumentRouteHeaderValue() {
        this.id = null;
        this.versionNumber = null;
        this.documentId = null;
        this.documentTypeId = null;
        this.docRouteStatus = null;
        this.finalizedDate = null;
    }


    private DocumentRouteHeaderValue(Builder builder) {
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.documentId = builder.getDocumentId();
        this.documentTypeId = builder.getDocumentTypeId();
        this.docRouteStatus = builder.getDocRouteStatus();
        this.finalizedDate = builder.getFinalizedDate();
    }


    @Override
    public String getId() {
        return id;
    }


    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }


    @Override
    public String getDocumentId() {
        return documentId;
    }


    @Override
    public String getDocumentTypeId() {
        return documentTypeId;
    }


    @Override
    public String getDocRouteStatus() {
        return docRouteStatus;
    }


    @Override
    public Timestamp getFinalizedDate() {
        return finalizedDate;
    }


    public static class Cache {
        public static final String NAME = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }


    public final static class Builder implements Serializable, ModelBuilder, DocumentRouteHeaderValueContract {
        private static final long serialVersionUID = 6621024337713773145L;

        private String id;
        private Long versionNumber;
        private String documentId;
        private String documentTypeId;
        private String docRouteStatus;
        private Timestamp finalizedDate;

        private Builder(String documentId) {
            setDocumentId(documentId);
        }


        @Override
        public DocumentRouteHeaderValue build() {
            return new DocumentRouteHeaderValue(this);
        }


        public static Builder create(DocumentRouteHeaderValueContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("Passed in contract cannot be null!");
            }

            Builder builder = new Builder(contract.getDocumentId());
            builder.setDocumentTypeId(contract.getDocumentTypeId());
            builder.setDocRouteStatus(contract.getDocRouteStatus());
            builder.setFinalizedDate(contract.getFinalizedDate());

            return builder;
        }


        @Override
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
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

    }


    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentRouteHeaderValue";
        // This has to be different than the org version
        final static String TYPE_NAME = "DocumentRouteHeaderValueEboImpl";
    }


    static class Elements {
        final static String ID = "id";
        final static String DOCUMENT_ID = "documentId";
        final static String DOCUMENT_TYPE_ID = "documentTypeId";
        final static String DOC_ROUTE_STATUS = "docRouteStatus";
        final static String FINALIZED_DATE = "finalizedDate";
    }

}
