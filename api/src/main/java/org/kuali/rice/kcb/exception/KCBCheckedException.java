/*
 * Copyright 2007-2008 The Kuali Foundation
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
package org.kuali.rice.kcb.exception;

/**
 * Base class for KCB checked exceptions 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KCBCheckedException extends Exception {
    /**
     * @see Exception() 
     */
    public KCBCheckedException() {
        super();
    }

    /**
     * @see Exception(String, Throwable)
     */
    public KCBCheckedException(String message, Throwable cause) {
        // TODO arh14 - THIS CONSTRUCTOR NEEDS A JAVADOC
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Exception(String)
     */
    public KCBCheckedException(String message) {
        super(message);
    }

    /**
     * @see Exception(Throwable)
     */
    public KCBCheckedException(Throwable cause) {
        super(cause);
    }
}