/**
 * Copyright 2005-2019 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.data.metadata.impl;

import org.kuali.rice.krad.data.metadata.DataObjectAttribute;

/**
 * Interface implemented by the classes within the provider, but that we don't want to expose to use outside of the
 * krad-data module.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DataObjectAttributeInternal extends DataObjectAttribute, MetadataCommonInternal {
	/**
    * If this data object attribute is wrapping another (generated by a lower metadata provider), return that.
    *
    * @return <b>null</b> if this attribute is not wrapping another.
    */
	DataObjectAttribute getEmbeddedAttribute();

    /**
    * Sets embedded attribute.
    *
    * @param embeddedAttribute embedded attribute.
    */
	void setEmbeddedAttribute(DataObjectAttribute embeddedAttribute);
}
