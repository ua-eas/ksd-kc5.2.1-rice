package edu.arizona.rice.kew.actionrequest.service.impl;

import java.util.List;

import org.kuali.rice.kew.api.document.Document;

import edu.arizona.rice.kew.actionrequest.dao.StuckDocumentsDAO;
import edu.arizona.rice.kew.api.document.StuckDocumentService;

/**
 * 
 * UA KFS7 upgrade moved requeue stuck document service to rice
 *
 */
public class StuckDocumentServiceImpl implements StuckDocumentService {
	protected StuckDocumentsDAO stuckDocumentsDAO;
	
	@Override
	public List<Document> getStuckDocuments() {
		return getStuckDocumentsDAO().getStuckDocuments();
	}

	public StuckDocumentsDAO getStuckDocumentsDAO() {
		return this.stuckDocumentsDAO;
	}

	public void setStuckDocumentsDAO(StuckDocumentsDAO stuckDocumentsDAO) {
		this.stuckDocumentsDAO = stuckDocumentsDAO;
	}

	
}
