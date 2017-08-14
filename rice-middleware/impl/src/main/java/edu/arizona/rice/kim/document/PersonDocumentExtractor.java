package edu.arizona.rice.kim.document;

import org.eclipse.persistence.descriptors.ClassExtractor;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;

/**
 * To decide which class to instantiate when reading from the database.
 * 
 * @author UAF Technical Team 
 *
 */
public class PersonDocumentExtractor extends ClassExtractor {
	
    public Class extractClassFromRow(Record row, Session session) {
        return UaIdentityManagementPersonDocument.class;
    }

}
