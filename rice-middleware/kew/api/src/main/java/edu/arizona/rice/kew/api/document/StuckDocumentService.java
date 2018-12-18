package edu.arizona.rice.kew.api.document;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.Document;

/**
 * UA KFS7 upgrade moved stuckDocumentService from kfs to rice due to rice schema standalone
 */
@WebService(name = "StuckDocumentService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface StuckDocumentService {

    /**
     *
     * <p>
     *   This method will documents that are stuck in workflow
     * </p>
     *
     * @param documentId the unique id of the document to return
     * @return the document with the passed in id value
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getStuckDocuments")
    @WebResult(name = "documents")
    List<Document> getStuckDocuments() throws RiceIllegalArgumentException;

}
