/**
 * Copyright 2005-2016 The Kuali Foundation
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
package org.kuali.rice.kns.document.authorization;

import org.kuali.rice.krad.datadictionary.mask.MaskFormatter;

import java.io.Serializable;

/**
 * @deprecated Only used in KNS classes, use KRAD.
 */
@Deprecated
public interface BusinessObjectRestrictions extends Serializable {
    public boolean hasAnyFieldRestrictions();
    
    public boolean hasRestriction(String fieldName);
    
    public void addFullyMaskedField(String fieldName, MaskFormatter maskFormatter);
    
    public void addPartiallyMaskedField(String fieldName, MaskFormatter maskFormatter);

    public FieldRestriction getFieldRestriction(String fieldName);
}
