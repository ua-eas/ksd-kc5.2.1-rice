/*
 * Copyright 2005-2012 The Kuali Foundation
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
var bodyHeight;
var profilingOn = false;

/**
 * Takes a name that may have characters incompatible with jQuery selection and escapes them so they can
 * be used in selectors.  This method MUST be called when selecting on a name that can be ANY name on the page
 * to avoid issues with collections(mainly)
 *
 * @returns a string that has been escaped for use in jQuery selectors
 */
function escapeName(name){
    name = name.replace(/\\'/g, "'");
    name = name.replace(/'/g, "\\'");
    name = name.replace(/\\"/g, "\"");
    name = name.replace(/"/g, "\\\"");
    name = name.replace(/\./g, "\\.");
    name = name.replace(/\[/g, "\\[");
    name = name.replace(/\]/g, "\\]");
    return name;
}

function publishHeight(){
    var parentUrl = "";
    if(navigator.cookieEnabled){
        parentUrl = jQuery.cookie('parentUrl');
        var passedUrl = decodeURIComponent( document.location.hash.replace( /^#/, '' ) );
        if(passedUrl && passedUrl.substring(0, 4) === "http"){
            jQuery.cookie('parentUrl', passedUrl, {path: '/'});
            parentUrl = passedUrl;
        }
    }

    if(parentUrl === ""){
        //make the assumption for not cross-domain, will have no effect if cross domain (message wont be
        //received)
        parentUrl = window.location;
        parentUrl = decodeURIComponent(parentUrl);
    }

    var height = jQuery("body").outerHeight();
    jQuery("body").attr("style", "overflow-x: auto; padding-right: 20px;");
    if (parentUrl && !isNaN(height) && height > 0) {
        jQuery.postMessage({ if_height: height}, parentUrl, parent);
        bodyHeight = height;
    }
}

/**
 * Get the current context
 *
 * @returns the jQuery context that can be used to perform actions that must be global to the entire page
 * ie, showing lightBoxes and growls etc
 */
function getContext(){
	var context;
	if(top == self){
		context = jq;
	}
	else{
		context = parent.jQuery;
	}

	return context;
}

/**
 * Sets a configuration parameter that will be accessible with script
 *
 * <p>
 * Configuration parameters are sent from the server and represent non-component
 * state, such as location of images
 * </p>
 *
 * @param paramName - name of the configuration parameter
 * @param paramValue - value for the configuration parameter
 */
function setConfigParam(paramName, paramValue) {
    var configParams = jQuery(document).data("ConfigParameters");
    if (!configParams) {
        configParams = new Object();
        jQuery(document).data("ConfigParameters", configParams);
    }
    configParams[paramName] = paramValue;
}



/**
 * Called when a view is rendered to initialize the state of components
 * that need to be accessed client side
 *
 * @param viewState - map (object) containing the view state
 */
function initializeViewState(viewState) {
    jQuery(document).data(kradVariables.VIEW_STATE, viewState);
}

/**
 * Updates the current view state with the given map of view state
 *
 * <p>
 * The given state will be merged with the current. Matching keys for simple properties will be overridden
 * if contained in the second map, in all cases except when the value is another map, in which case the map
 * value will be merged
 * </p>
 *
 * @param viewState - view state to merge in
 */
function updateViewState(viewState) {
    var currentViewState = jQuery(document).data(kradVariables.VIEW_STATE);
    if (currentViewState) {
        jQuery.extend(currentViewState, viewState);
    }
    else {
        jQuery(document).data(kradVariables.VIEW_STATE, viewState);
    }
}

/**
 * Sets a key/value pair in the view state
 *
 * @param key - name to reference state by
 * @param value - value for the state
 */
function setViewState(key, value) {
    var viewState = jQuery(document).data(kradVariables.VIEW_STATE);
    if (!viewState) {
        viewState = new Object();
        jQuery(document).data(kradVariables.VIEW_STATE, viewState);
    }
    viewState[key] = value;
}

/**
 * Retrieves the current value for a given key in the view state, if
 * not found empty string is returned
 *
 * @param key - name of the variable in view state to return
 */
function getViewState(key) {
    var viewState = jQuery(document).data(kradVariables.VIEW_STATE);
    if (viewState && viewState.hasOwnProperty(key)) {
        return viewState[key];
    }
    return "";
}

/**
 * Adds the given key/value pair to the state maintained for the give component
 *
 * @param componentId - id for the component the state should be associated with
 * @param key - name to reference the state by
 * @param value - value for the state
 */
function setComponentState(componentId, key, value) {
    var componentState = getViewState(componentId);
    if (!componentState) {
        componentState = new Object();
        setViewState(componentId, componentState);
    }
    componentState[key] = value;
}

/**
 * Retrieves the state value for the given key and given component
 *
 * @param componentId - id for the component the key is associated with
 * @param key - name of the state to retrieve
 */
function getComponentState(componentId, key) {
    var componentState = getViewState(componentId);
    if (componentState && componentState.hasOwnProperty(key)) {
        return componentState[key];
    }
    return "";
}

// gets the the label for field with the corresponding id
function getLabel(id){
	var label =  jQuery("#" + id + "_label");
	if(label){
		return label.text();
	}
	else{
		return "";
	}
}
/**
 * runs hidden scripts. The hidden scripts are contained in hidden input elements
 *
 * @param id - the tag id or selector expression to use. If empty, run all hidden scripts
 * @param isSelector - when present and true, the value given for the id used as jquery selector expression
 * @param skipValidationBubbling - set to true to skip processing each field - ONLY is ever true for pages since
 * they handle this during the setupPage call
 */
function runHiddenScripts(id, isSelector, skipValidationBubbling){
	if(id){
        //run dataScript first always
        jQuery("#" + id).find("input[data-role='dataScript']").each(function(){
            evalHiddenScript(jQuery(this));
        });

        var selector = "#" + id;
        if (isSelector && isSelector == true) {
            selector = id;
        }

		jQuery(selector).find("input[name='script']").each(function(){
            evalHiddenScript(jQuery(this));
		});

        runScriptsForId(id);

        //reinit dirty fields
        jQuery('#kualiForm').dirty_form({changedClass:kradVariables.DIRTY_CLASS, includeHidden:true});

        //reinitialize BubblePopup
        initBubblePopups();

        //Interpret new server message state for refreshed InputFields and write them out
        if(!skipValidationBubbling){
            jQuery(selector).find("[data-role='InputField']").andSelf().filter("[data-role='InputField']").each(function(){
                var data = jQuery(this).data(kradVariables.VALIDATION_MESSAGES);
                handleMessagesAtField(jQuery(this).attr('id'));
            });
        }
	}
	else{
        //run dataScript first always
        jQuery("input[data-role='dataScript']").each(function(){
            evalHiddenScript(jQuery(this));
        });

		jQuery("input[name='script']").each(function(){
            evalHiddenScript(jQuery(this));
		});

        //reinitialize BubblePopup
        initBubblePopups();
	}
}

function runHiddenScriptsTemp(id, isSelector){
    runHiddenScripts(id, isSelector);
}

/**
 * runs hidden scripts. The hidden scripts are contained in hidden input elements
 *
 * <p>Finds all hidden inputs with the attribute data-role having a value of 'dataScript' or 'script'
 * then runs the script in the value attribute if the input's data-for attribute value is equal to the id provided</p>
 *
 * @param id - the tag id to use
 */
function runScriptsForId(id) {
    if (id) {
        jQuery("input[data-role='dataScript']").each(function () {
            if (jQuery(this).data("for") === id) {
                evalHiddenScript(jQuery(this));
            }
        });

        jQuery("input[name='script']").each(function () {
            if (jQuery(this).data("for") === id) {
                evalHiddenScript(jQuery(this));
            }
        });
    }
}

/**
 * do the actual work of evaluating a script once it has been located
 *
 * @param jqueryObj - a jquery object representing a hidden input element with a script in its value attribute
 */
function evalHiddenScript(jqueryObj) {
    eval(jqueryObj.val());
    jqueryObj.attr("script", "first_run");
    jqueryObj.removeAttr("name");
}

/**
 * run hidden scripts again
 *
 * <p>This is needed in situations where due to some bugs in page refreshes or progressive rendering,
 * the hidden scripts may have run but not accomplished the desired results</p>
 */
function runHiddenScriptsAgain(){
    jQuery("input[data-role='dataScript']").each(function(){
        eval(jQuery(this).val());
        jQuery(this).removeAttr("script");
    });
    jQuery("input[script='first_run']").each(function(){
        eval(jQuery(this).val());
        jQuery(this).removeAttr("script");
    });
}

/**
 * Writes a hidden for property 'methodToCall' set to the given value. This is
 * useful for submitting forms with JavaScript where the methodToCall needs to
 * be set before the form is submitted.
 *
 * @param methodToCall -
 *          the value that should be set for the methodToCall parameter
 */
function setMethodToCall(methodToCall) {
    jQuery("<input type='hidden' name='methodToCall' value='" + methodToCall + "'/>").appendTo(jQuery("#formComplete"));
}

/**
 * Writes a property name/value pair as a hidden input field on the form. Called
 * to dynamically set request parameters based on a chosen action. Assumes
 * existence of a div named 'formComplete' where the hidden inputs will be
 * inserted
 *
 * @param propertyName -
 *          name for the input field to write
 * @param propertyValue -
 *          value for the input field to write
 */
function writeHiddenToForm(propertyName, propertyValue) {
    //removing because of performFinalize bug
    jQuery('input[name="' + escapeName(propertyName) + '"]').remove();

    if (propertyValue.indexOf("'") != -1) {
        jQuery("<input type='hidden' name='" + propertyName + "'" + ' value="' + propertyValue + '"/>').appendTo(jQuery("#formComplete"));
    } else {
        jQuery("<input type='hidden' name='" + propertyName + "' value='" + propertyValue + "'/>").appendTo(jQuery("#formComplete"));
    }
}

/**
 * Retrieves the actual value from the input widget specified by name
 */
function coerceValue(name){
	var value = "";
	var nameSelect = "[name='" + escapeName(name) + "']";
	if(jQuery(nameSelect + ":checkbox").length){
		value = jQuery(nameSelect + ":checked").val();
	}
	else if(jQuery(nameSelect + ":radio").length){
		value = jQuery(nameSelect + ":checked").val();
	}
	else if(jQuery(nameSelect).length){
		if (jQuery(nameSelect).hasClass("watermark")) {
            jQuery.watermark.hide(nameSelect);
			value = jQuery(nameSelect).val();
            jQuery.watermark.show(nameSelect);
		}
		else{
			value = jQuery(nameSelect).val();
		}
	}

	if(value == null){
		value = "";
	}

	return value;
}

/**
 * Sets a value on the control with the given name attribute
 *
 * @param name - name on control to set value for
 * @param value - value to set
 */
function setValue(name, value) {
    var nameSelect = "[name='" + escapeName(name) + "']";
    jQuery(nameSelect).val(value);
}

function isValueEmpty(value){
	if(value != undefined && value != null && value != ""){
		return false;
	}
	else{
		return true;
	}
}

//returns true if the field with name of name1 occurs before field with name2
function occursBefore(name1, name2){
	var field1 = jQuery("[name='" + escapeName(name1) + "']");
	var field2 = jQuery("[name='" + escapeName(name2) + "']");

	field1.addClass("prereqcheck");
	field2.addClass("prereqcheck");

	var fields = jQuery(".prereqcheck");

	field1.removeClass("prereqcheck");
	field2.removeClass("prereqcheck");

	if(fields.index(field1) < fields.index(field2) ){
		return true;
	}
	else{
		return false;
	}
}

/**
 * Validate dirty fields on the form.
 *
 * <p>Whenever the user clicks on the action field which has action methods set to <code>REFRESH,NAVIGATE,CANCEL,CLOSE</code>,
 * form dirtyness is checked. It checks for any input elements which has "dirty" class. If present, it pops a message to
 * the user to confirm whether they want to stay on the page or want to navigate.
 * </p>
 * @param event
 * @returns true if the form has dirty fields
 */
function checkDirty(event){
	var validateDirty = jQuery("[name='validateDirty']").val()
	var dirty = jQuery(".uif-field").find("input.dirty")

	if (validateDirty == "true" && dirty.length > 0)
	{
		var answer = confirm ("Form has unsaved data. Do you want to leave anyway?")
		if (answer == false){
			event.preventDefault();
			event.stopImmediatePropagation();

			//Change the current nav button class to 'current' if user doesn't wants to leave the page
			var ul = jQuery("#" + event.target.id).closest("ul");
			if (ul.length > 0)
			{
				var pageId = jQuery("[name='pageId']").val();
				if(ul.hasClass(kradVariables.TAB_MENU_CLASS)){
					jQuery("#" + ul.attr("id")).selectTab({selectPage : pageId});
				}
				else{
					jQuery("#" + ul.attr("id")).selectMenuItem({selectPage : pageId});
				}
			}
			return true;
		}
	}
	return false;
}

/**
 * Gets the actual attribute id to use element manipulation related to this attribute.
 *
 * @param elementId
 * @param elementType
 */
function getAttributeId(elementId){
	var id = elementId;
	id = elementId.replace(/_control\S*/, "");
	return id;
}

//performs a 'jump' - a scroll to the necessary html element
//The element that is used is based on the hidden value of jumpToId or jumpToName on the form
//if these hidden attributes do not contain a value it jumps to the top of the page by default
function performJumpTo(){
	var jumpToId = jQuery("[name='jumpToId']").val();
	var jumpToName = jQuery("[name='jumpToName']").val();
	if(jumpToId){
		if(jumpToId.toUpperCase() === "TOP"){
			jumpToTop();
		}
		else if(jumpToId.toUpperCase() === "BOTTOM"){
			jumpToBottom();
		}
		else{
			jumpToElementById(jumpToId);
		}
	}
	else if(jumpToName){
		jumpToElementByName(jumpToName);
	}
	else{
		jumpToTop();
	}
}

//performs a focus on an the element with the id preset
function performFocus(){
	var focusId = jQuery("[name='focusId']").val();
	if(focusId){
		jQuery("#" + focusId).focus();
	}
	else{
		jQuery("[data-role='InputField'] .uif-control:visible:first", "#kualiForm").focus();
	}
}

//performs a focus on an the element with the name specified
function focusOnElementByName(name){
	var theElement =  jQuery("[name='" + escapeName(name) + "']");
	if(theElement.length != 0){
		theElement.focus();
	}
}

//performs a focus on an the element with the id specified
function focusOnElementById(focusId){
	if(focusId){
		jQuery("#" + focusId).focus();
	}
}

//Jump(scroll) to an element by name
function jumpToElementByName(name){
	var theElement =  jQuery("[name='" + escapeName(name) + "']");
	if(theElement.length != 0){
		if(top == self || jQuery(".fancybox-iframe", parent.document).length){
            jQuery.scrollTo(theElement, 0);
		}
		else{
			var headerOffset = top.jQuery("#header").outerHeight(true) + top.jQuery(".header2").outerHeight(true);
			top.jQuery.scrollTo(theElement, 0, {offset: {top:headerOffset}});
		}
	}
}

//Jump(scroll) to an element by Id
function jumpToElementById(id){
	var theElement =  jQuery("#" + id);
	if(theElement.length != 0){
		if(top == self || jQuery(".fancybox-iframe", parent.document).length){
            jQuery.scrollTo(theElement, 0);
		}
		else{
			var headerOffset = top.jQuery("#header").outerHeight(true) + top.jQuery(".header2").outerHeight(true);
			top.jQuery.scrollTo(theElement, 0, {offset: {top:headerOffset}});
		}
	}
}

//Jump(scroll) to the top of the current screen
function jumpToTop(){
	if(top == self || jQuery(".fancybox-iframe", parent.document).length){
        jQuery.scrollTo(jQuery("html"), 0);
	}
	else{
		top.jQuery.scrollTo(top.jQuery("html"), 0);
	}
}

//Jump(scroll) to the bottom of the current screen
function jumpToBottom(){
	if(top == self || jQuery(".fancybox-iframe", parent.document).length){
        jQuery.scrollTo("max", 0);
	}
	else{
		top.jQuery.scrollTo("max", 0);
	}
}

// The following javascript is intended to resize the route log iframe
// to stay at an appropriate height based on the size of the documents
// contents contained in the iframe.
// NOTE: this will only work when the domain serving the content of kuali
// is the same as the domain serving the content of workflow.
var routeLogResizeTimer = "";
var currentHeight = 500;
var safari = navigator.userAgent.toLowerCase().indexOf('safari');

function setRouteLogIframeDimensions() {
    var routeLogFrame = document.getElementById("routeLogIFrame");
    var routeLogFrame = document.getElementById("routeLogIFrame");
    var routeLogFrameWin = window.frames["routeLogIFrame"];
    var frameDocHeight = 0;
    try {
        frameDocHeight = routeLogFrameWin.document.documentElement.scrollHeight;
    } catch (e) {
        // unable to set due to cross-domain scripting
        frameDocHeight = 0;
    }

    if (frameDocHeight > 0) {
        if (routeLogFrame && routeLogFrameWin) {

            if ((Math.abs(frameDocHeight - currentHeight)) > 30) {
                if (safari > -1) {
                    if ((Math.abs(frameDocHeight - currentHeight)) > 59) {
                        routeLogFrame.style.height = (frameDocHeight + 30) + "px";
                        currentHeight = frameDocHeight;
                    }
                } else {
                    routeLogFrame.style.height = (frameDocHeight + 30) + "px";
                    currentHeight = frameDocHeight;
                }
            }
        }
    }

    if (routeLogResizeTimer == "") {
        routeLogResizeTimer = setInterval("resizeTheRouteLogFrame()", 300);
    }
}

function resizeTheRouteLogFrame() {
    setRouteLogIframeDimensions();
}

/**
 * Adds or adds value to the attribute on the element.
 *
 * @param id - element id
 * @param attributeName - name of the attribute to add/add to
 * @param attributeValue - value of the attribute
 * @param concatFlag - indicate if value should be added to current value
 */
function addAttribute(id, attributeName, attributeValue, concatFlag) {
    hasAttribute = jQuery("#" + id).is('[' + attributeName + ']');
    if (concatFlag && hasAttribute) {
        jQuery("#" + id).attr(attributeName, jQuery("#" + id).attr(attributeName) + " " + attributeValue);
    }else{
        jQuery("#" + id).attr(attributeName, attributeValue);
    }
}

/**
 * Open new browser window for the specified help url
 *
 * The help window is positioned in the center of the screen and resized to 1/4th of the screen.
 *
 * Browsers don't allow one to modify windows of other domains.  Thus to ensure that only one help window exist
 * and to guarantee it's placement, size and that the window is in the foreground the following process is performed:
 *   1) open the help window - this ensures that we get a window handle to any existing help window
 *   2) close the help window
 *   3) open a new help window with the correct placement, size and url
 *
 * @param url - url of the help window content
 */
function openHelpWindow(url) {
    var windowWidth =  screen.availWidth/2;
    var windowHeight = screen.availHeight/2;
    var windowPositionY = parseInt((screen.availWidth/2) - (windowWidth/2));
    var windowPositionX = parseInt((screen.availHeight/2) - (windowHeight/2));

    var windowUrl = url;
    var windowName = 'HelpWindow';
    var windowOptions = 'width=' + windowWidth + ',height=' + windowHeight + ',top=' + windowPositionX + ',left=' + windowPositionY + ',scrollbars=yes,resizable=yes';

    var myWindow = window.open('', windowName);
    myWindow.close();
    myWindow = window.open(windowUrl, windowName, windowOptions);
}

/**
 * Uppercases the current value for the control with the given id
 *
 * @param controlId - id for the control whose value should be uppercased
 */
function uppercaseValue(controlId) {
    jQuery("#" + controlId).css('text-transform', 'uppercase');
}

/**
 * Profiling helper method will print out profile info in firefox console
 *
 * @param start true to start profiling, false to stop profiling
 * @param testingText text to be printed with this profile
 */
function profile(start, testingText){
    if(profilingOn){
        if(start){
            console.time(testingText);
            console.profile(testingText);
        }
        else{
            console.profileEnd();
            console.timeEnd(testingText);
        }

    }
}

/**
 * Timing method for profiling use - will print out in console
 *
 * @param start true to start timing, false to stop timing
 * @param testingText text to be printed out with time results
 */
function time(start, testingText){
    if(profilingOn){
        if(start){
            console.time(testingText);
        }
        else{
            console.timeEnd(testingText);
        }
    }
}

/**
 * Adds a class to the collection item related to the delete action
 *
 * @param deleteButton
 * @param highlightItemClass - the class to add to the item that should be highlighted
 */
function deleteLineMouseOver(deleteButton, highlightItemClass) {
    innerLayout = jQuery(deleteButton).parents('.uif-tableCollectionLayout, .uif-stackedCollectionLayout').first().attr('class');
    if (innerLayout == 'uif-tableCollectionLayout') {
        jQuery(deleteButton).closest('tr').addClass(highlightItemClass);
    }else{
        jQuery(deleteButton).closest('.uif-collectionItem').addClass(highlightItemClass);
    }
}

/**
 * Removes a class from the collection item related to the delete action
 *
 * @param deleteButton
 * @param highlightItemClass - the class remove from the collection item
 */
function deleteLineMouseOut(deleteButton, highlightItemClass) {
    innerLayout = jQuery(deleteButton).parents('.uif-tableCollectionLayout, .uif-stackedCollectionLayout').first().attr('class');
    if (innerLayout == 'uif-tableCollectionLayout') {
        jQuery(deleteButton).closest('tr').removeClass(highlightItemClass);
    }else{
        jQuery(deleteButton).closest('.uif-collectionItem').removeClass(highlightItemClass);
    }
}

/**
 * Adds a class to the collection line and enables the save button
 *
 * @param inputField
 * @param highlightItemClass - the class to add to the collection item
 */
function collectionLineChanged(inputField, highlightItemClass) {
    innerLayout = jQuery(inputField).parents('.uif-tableCollectionLayout, .uif-stackedCollectionLayout').first().attr('class');
    if (innerLayout == 'uif-tableCollectionLayout') {
        jQuery(inputField).closest('tr').addClass(highlightItemClass).find(".uif-saveLineAction").removeAttr('disabled');
    }else{
        jQuery(inputField).closest('.uif-collectionItem').addClass(highlightItemClass).find(".uif-saveLineAction").removeAttr('disabled');
    }
}

/**
 * Display the component of the id in a light box
 *
 * <p>
 * jQuery Fancybox is used to create the lightbox.  The specified component is used as the content of the fancy box.
 * The second argument is optional and allows the FancyBox options to be overridden.
 * </p>
 *
 * @param componentId the id of the component that will be used for the lightbox content (usually a group id)
 * @parm overrideOptions the map of option settings (option name/value pairs) for the plugin. This is optional.
 */
function showLightboxComponent(componentId, overrideOptions) {
    var options = {fitToView : true,
                   openEffect : 'fade',
                   closeEffect : 'fade',
                   openSpeed : 200,
                   closeSpeed : 200,
                   helpers : {overlay:{css:{cursor:'arrow'},closeClick:false}},
                   type : 'inline',
                   href : "#" + componentId
                  };

    // override fancybox options
    if (overrideOptions !== undefined) {
        _mergeOptionsMap(options, overrideOptions);
    }

    // Open the light box
    if (top == self) {
        jQuery.fancybox(options);
    } else {
        // for portal usage (the href anchor from the portal page will not work so the content is explicitly passed to fancybox)
        options.content = jQuery('#' + componentId).html();
        parent.jQuery.fancybox(options);
    }
}

/**
 * Internal function for merging fancybox options
 *
 * <p>
 * Existing options will not be deleted but overridden.
 * </p>
 *
 * @param options the existing fancybox options
 * @param overrideOptions the fancybox options that should be added/overwritten
 */
function _mergeOptionsMap(options, overrideOptions) {
    for(var overrideOption in overrideOptions) {
        if (overrideOptions[overrideOption] instanceof Object) {
            _mergeOptionsMap(options[overrideOption], overrideOptions[overrideOption]);
        } else {
            options[overrideOption] = overrideOptions[overrideOption];
        }
    }
}