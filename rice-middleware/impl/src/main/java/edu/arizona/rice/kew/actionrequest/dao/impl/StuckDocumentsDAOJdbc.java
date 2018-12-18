package edu.arizona.rice.kew.actionrequest.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import edu.arizona.rice.kew.actionrequest.dao.StuckDocumentsDAO;

/**
 * UA KFS7 upgrade moved stuckDocumentService from kfs to rice due to rice schema standalone
 * 
 * UAF-475: MOD-WKFLW-03 Requeue Stuck Documents Job
 *
 * @author Josh Shaloo <shaloo@email.arizona.edu>
 */
public class StuckDocumentsDAOJdbc implements StuckDocumentsDAO {

    public static final String DOCUMENT_HEADER_ID_COLUMN_LABEL = "DOCUMENT HEADER_ID";
    public static final String MODIFICATION_DATE_COLUMN_LABEL = "MODIFICATION DATE";

    protected WorkflowDocumentService workflowDocumentService;
    protected DataSource dataSource;


    private static final String STUCK_DOCUMENTS_SQL =
            "select  dh.doc_hdr_id   as \"" + DOCUMENT_HEADER_ID_COLUMN_LABEL + "\", " +
                    "dh.stat_mdfn_dt as \"" + MODIFICATION_DATE_COLUMN_LABEL  + "\" " +
            "from krew_doc_hdr_t dh " +
            "left join krew_actn_rqst_t ar " +
            "  on dh.doc_hdr_id = ar.doc_hdr_id " +
            "  and (ar.stat_cd = 'I' or (ar.stat_cd = 'A' and (ar.actn_rqst_cd != 'F' or ar.actn_rqst_cd = 'A'))) " +
            "inner join krew_doc_typ_t dt on dh.doc_typ_id = dt.doc_typ_id " +
            "where dh.doc_hdr_stat_cd = 'R' " +
            "and ar.doc_hdr_id is null " +
            "and dt.actv_ind = 1 " +
            "order by dh.stat_mdfn_dt";

    @Override
    public List<Document> getStuckDocuments() {
    	JdbcTemplate template = new JdbcTemplate(dataSource);
    	
        List<Document> stuckDocuments = template.query(STUCK_DOCUMENTS_SQL, new RowMapper<Document>() {
            @Override
            public Document mapRow(ResultSet resultSet, int i) throws SQLException {

                Document stuckDocument = getWorkflowDocumentService().getDocument(resultSet.getNString(DOCUMENT_HEADER_ID_COLUMN_LABEL));
                return stuckDocument;
            }
        });
        
        return stuckDocuments;
    }

    protected WorkflowDocumentService getWorkflowDocumentService() {
        return this.workflowDocumentService;
    }

    public void setWorkflowDocumentService( WorkflowDocumentService workflowDocumentService ) {
        this.workflowDocumentService = workflowDocumentService;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
    }
}