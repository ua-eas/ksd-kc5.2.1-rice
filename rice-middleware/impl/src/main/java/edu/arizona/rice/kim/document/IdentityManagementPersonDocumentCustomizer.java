package edu.arizona.rice.kim.document;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;

/**
 * This is DescriptorCustomizer class to customize IdentityManagementPersonDocument query since it's inherited by 
 * UAIdentityManagementPersonDocument. Mapping PRNCPL_ID NULL entry to IdentityManagementPersonDocument 
 * as it should be always NOT NULL in KRIM_PERSON_DOCUMENT_T, i.e. all entries should map to UAIdentityManagementPersonDocument instead.
 * 
 * @author UAF Technical Team 
 *
 */
public class IdentityManagementPersonDocumentCustomizer  implements DescriptorCustomizer {
    public void customize(ClassDescriptor descriptor) {
    	ExpressionBuilder person = new ExpressionBuilder();
    	Expression expression = person.getField("PRNCPL_ID").isNull();
        descriptor.getInheritancePolicy().setOnlyInstancesExpression(expression);
    }
}