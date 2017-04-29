package edu.arizona.rice.kew.impl.document.search;

import java.util.Collection;

import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.impl.document.search.DocumentSearchGenerator;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.kns.web.ui.ResultRow;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.lookup.CollectionIncomplete;

import edu.arizona.rice.kew.docsearch.dao.DocumentSearchResultsSizeDAO;


public class DocumentSearchCriteriaBoLookupableHelperService extends org.kuali.rice.kew.impl.document.search.DocumentSearchCriteriaBoLookupableHelperService {
    private DocumentSearchResultsSizeDAO enDocumentSearchResultsSizeDAO;


    /*
     * Overriding to wrap in total-count collection when result size is over congid'ed threshold.
     */
    @Override
    public Collection<? extends BusinessObject> performLookup(LookupForm lookupForm, @SuppressWarnings("deprecation") Collection<ResultRow> resultTable, boolean bounded) {
        Collection<? extends BusinessObject> lookupResult = super.performLookup(lookupForm, resultTable, bounded);

        // This if block is the entire reason for this class: if we are over the threshold for returned results,
        // wrap the result set with type that knows how many total results were found.
        if (this.searchResults.isOverThreshold()) {
            Long actualResultsSize = getTotalMatchingDocumentsSize();
            lookupResult = new CollectionIncomplete(lookupResult, actualResultsSize);
        }

        return lookupResult;
    }


    private Long getTotalMatchingDocumentsSize() {
        DocumentType documentType = getValidDocumentType(criteria.getDocumentTypeName());
        DocumentSearchGenerator docSearchGenerator = getDocumentSearchService().getStandardDocumentSearchGenerator();

        return getEnDocumentSearchResultsSizeDAO().getTotalMatchingDocumentsSize(docSearchGenerator, criteria, documentType);
    }


    private DocumentSearchResultsSizeDAO getEnDocumentSearchResultsSizeDAO() {
        return enDocumentSearchResultsSizeDAO;
    }


    public void setEnDocumentSearchResultsSizeDAO(DocumentSearchResultsSizeDAO enDocumentSearchResultsSizeDAO) {
        this.enDocumentSearchResultsSizeDAO = enDocumentSearchResultsSizeDAO;
    }

}
