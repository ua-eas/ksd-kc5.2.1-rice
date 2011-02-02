/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kns.uif.service;

import java.util.Map;

import org.kuali.rice.kns.uif.container.View;

/**
 * Provides service methods for retrieving and updating <code>View</code>
 * instances. The UIF interacts with this service from the client layer to pull
 * information from the View dictionary and manage the View instance through its
 * lifecycle
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ViewService {

	/**
	 * Returns the <code>View</code> entry identified by the given id
	 * <p>
	 * The id matches the id configured for the View through the dictionary.
	 * Before the View is returned it is initialized with the model data
	 * </p>
	 * 
	 * @param viewId
	 *            - unique id for view configured on its definition
	 * @return View instance associated with the id or Null if id is not found
	 */
	public View getViewById(String viewId);

	/**
	 * Returns the <code>View</code> entry identified by the given id
	 * <p>
	 * The id matches the id configured for the View through the dictionary.
	 * Before the View is returned it is initialized with the model data
	 * </p>
	 * <p>
	 * Any configuration sent through the options Map is used to initialize the
	 * View. This map contains present options the view is aware of and will
	 * typically come from request parameters. e.g. For maintenance Views there
	 * is the maintenance type option (new, edit, copy)
	 * </p>
	 * 
	 * @param viewId
	 *            - unique id for view configured on its definition
	 * @param parameters
	 *            - Map of key values pairs that provide configuration for the
	 *            <code>View</code>, this is generally comes from the request
	 *            and can be the request parameter Map itself. Any parameters
	 *            not valid for the View wil be filtered out
	 * @param model
	 *            - Top level object containing the data (could be the form or a
	 *            top level business object, dto)
	 * @return View instance associated with the id or Null if id is not found
	 */
	public View getView(String viewId, Map<String, String> parameters);
	
	public void updateView(View view, Object model);
	
	public View reconstructView(String viewId, Map<String, String> parameters, Object model);

	/**
	 * Retrieves the unique id for the <code>View</code> instance that is of
	 * Inquiry view type and is configured for the given model class. If more
	 * than one views exists, the name can be given to pick which one should be
	 * returned or if empty the view with name 'default' is returned.
	 * 
	 * @param name
	 *            - name given for the view in its definition that should be
	 *            returned, can be blank or null
	 * @param modelClassName
	 *            - model class the view works with
	 * @return String view id or Null if a view was not found
	 */
	public String getInquiryViewId(String name, String modelClassName);

}
