package edu.arizona.rice.kew.actionrequest.dao;

import org.kuali.rice.kew.api.document.Document;

import java.util.List;

/**
 * UA KFS7 upgrade moved stuckDocumentService from kfs to rice due to rice schema standalone
 * 
 * UAF-475: MOD-WKFLW-03 Requeue Stuck Documents Job
 *
 * @author Josh Shaloo <shaloo@email.arizona.edu>
 */
public interface StuckDocumentsDAO {
    List<Document> getStuckDocuments();
}
