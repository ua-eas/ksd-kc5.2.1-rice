package edu.arizona.rice.kew.docsearch.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.docsearch.service.DocumentSearchService;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.impl.document.search.DocumentSearchGenerator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import edu.arizona.rice.kew.docsearch.dao.DocumentSearchResultsSizeDAO;


public class DocumentSearchResultsSizeDAOJdbcImpl implements DocumentSearchResultsSizeDAO {
    private static final String DOC_SEARCH_GENERATOR_SQL_PREFIX = "Select * ";
    private static final String DOC_SEARCH_GENERATOR_REPLACEMENT_SQL_PREFIX = "Select count(*) ";
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentSearchResultsSizeDAOJdbcImpl.class);

    private DataSource dataSource;
    private DocumentSearchService documentSearchService;


    @Override
    public Long getTotalMatchingDocumentsSize(final DocumentSearchGenerator documentSearchGenerator, final DocumentSearchCriteria criteria, final DocumentType documentType) {
        JdbcTemplate template = new JdbcTemplate(getDataSource());
        return (Long) template.execute(new ConnectionCallback() {
            @Override
            public Object doInConnection(Connection con) throws SQLException,
                    DataAccessException {
                Statement statement = null;
                ResultSet rs = null;
                Long matchingResults = Long.valueOf(0);

                try {
                    // List<RemotableAttributeField> searchFields
                    List<RemotableAttributeField> searchFields = getDocumentSearchService().determineSearchFields(documentType);
                    String sql = documentSearchGenerator.generateSearchSql(criteria, searchFields);
                    // HACK ALERT: assume that the search SQL begins with "Select *"
                    if (!sql.startsWith(DOC_SEARCH_GENERATOR_SQL_PREFIX)) {
                        String logMsg = "Unable to compute number of doc search results, expected prefix " + DOC_SEARCH_GENERATOR_SQL_PREFIX + " in sql string " + sql;
                        LOG.error(logMsg);
                        throw new RuntimeException(logMsg);
                    }

                    statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = statement.executeQuery(StringUtils.replaceOnce(sql, DOC_SEARCH_GENERATOR_SQL_PREFIX, DOC_SEARCH_GENERATOR_REPLACEMENT_SQL_PREFIX));
                    
                    if (rs.next()) {
                        matchingResults = Long.valueOf(rs.getLong(1));
                    }
                } finally {
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                        if (statement != null) {
                            statement.close();
                        }
                    } catch (Exception ex) {
                        //
                    }
                }

                return matchingResults;
            }
        });
    }


    private DataSource getDataSource() {
        return dataSource;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    private DocumentSearchService getDocumentSearchService() {
        return documentSearchService;
    }


    public void setDocumentSearchService(DocumentSearchService documentSearchService) {
        this.documentSearchService = documentSearchService;
    }

}
