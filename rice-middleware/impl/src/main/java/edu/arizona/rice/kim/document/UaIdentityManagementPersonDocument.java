package edu.arizona.rice.kim.document;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Customizer;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;

/**
 * Define UA customized IdentityManagementPersonDocument
 * 
 * @author UAF Technical Team 
 * 
 */
@AttributeOverrides({ @AttributeOverride(name = "documentNumber", column = @Column(name = "FDOC_NBR")) })
@Entity
@Table(name = "KRIM_PERSON_DOCUMENT_T")
@Customizer(UAIdentityManagementPersonDocumentCustomizer.class)
public class UaIdentityManagementPersonDocument extends IdentityManagementPersonDocument {
	protected static final long serialVersionUID = -534993712085516925L;

	@Transient
	protected String employeeId = "";

	public UaIdentityManagementPersonDocument() {
		super();
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
}
