package edu.arizona.rice.kew.docsearch.dao;

public interface DocumentSearchResultsSizeDAO {
    public Long getTotalMatchingDocumentsSize(String sql);
}

