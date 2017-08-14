package edu.arizona.rice.kim.document;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.copying.PersistenceEntityCopyPolicy;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;

/**
 * This is DescriptorCustomizer class to customize UAIdentityManagementPersonDocument query
 * 
 * @author UAF Technical Team 
 *
 */
public class UAIdentityManagementPersonDocumentCustomizer  implements DescriptorCustomizer {
    public void customize(ClassDescriptor descriptor) {
    	ExpressionBuilder person = new ExpressionBuilder();
    	Expression expression = person.getField("PRNCPL_ID").isNull().not();
        descriptor.getInheritancePolicy().setOnlyInstancesExpression(expression);
        // default InstantiationCopyPolicy is used since UAIdentityManagementPersonDocument has no JPA method access or weaving. 
        // InstantiationCopyPolicy will create new instance for InstantiationCopyPolicy which cause documentHeader reset to null. 
        descriptor.setCopyPolicy(new PersistenceEntityCopyPolicy());
    }
}