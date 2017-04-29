package edu.arizona.rice.kew.docsearch.dao;

import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.impl.document.search.DocumentSearchGenerator;

public interface DocumentSearchResultsSizeDAO {
    public Long getTotalMatchingDocumentsSize(DocumentSearchGenerator documentSearchGenerator, DocumentSearchCriteria criteria, DocumentType documentType);
}

