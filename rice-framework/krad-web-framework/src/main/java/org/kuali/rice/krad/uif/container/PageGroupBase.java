/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krad.uif.container;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.lifecycle.ViewLifecycle;
import org.kuali.rice.krad.uif.util.BreadcrumbItem;
import org.kuali.rice.krad.uif.util.LifecycleElement;
import org.kuali.rice.krad.uif.util.PageBreadcrumbOptions;
import org.kuali.rice.krad.uif.view.FormView;
import org.kuali.rice.krad.uif.view.View;

import java.util.List;

/**
 * A PageGroup represents a page of a View.
 *
 * <p>
 * PageGroups should only be used with a View component.  The contain the main content that will be seen by the
 * user using the View.  Like all other groups, PageGroup can contain items, headers and footers.  Pages also
 * have their own BreadcrumbItem.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "page-bean", parent = "Uif-Page"),
        @BeanTag(name = "disclosure-page-bean", parent = "Uif-Disclosure-Page"),
        @BeanTag(name = "documentPage-bean", parent = "Uif-DocumentPage"),
        @BeanTag(name = "inquiryPage-bean", parent = "Uif-InquiryPage"),
        @BeanTag(name = "lookupPage-bean", parent = "Uif-LookupPage"),
        @BeanTag(name = "maintenancePage-bean", parent = "Uif-MaintenancePage")})
public class PageGroupBase extends GroupBase implements PageGroup {
    private static final long serialVersionUID = 7571981300587270274L;

    private boolean autoFocus = false;

    private PageBreadcrumbOptions breadcrumbOptions;
    private BreadcrumbItem breadcrumbItem;
    private boolean stickyFooter;
    private String formPostUrl;


    /**
     * {@inheritDoc}
     */
    @Override
    public void performInitialization(Object model) {
        super.performInitialization(model);

        //check to see if one of the items is a page, if so throw an exception
        for (Component item : this.getItems()) {
            if (item != null && item instanceof PageGroup) {
                throw new RuntimeException("The page with id='"
                        + this.getId()
                        + "' contains a page with id='"
                        + item.getId()
                        + "'.  Nesting a page within a page is not allowed since only one "
                        + "page's content can be shown on the View "
                        + "at a time.  This may have been caused by possible misuse of the singlePageView flag (when "
                        + "this flag is true, items set on the View become items of the single page.  Instead use "
                        + "the page property on the View to set the page being used).");
            }
        }

        breadcrumbOptions.setupBreadcrumbs(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performFinalize(Object model, LifecycleElement parent) {
        super.performFinalize(model, parent);

        this.addDataAttribute(UifConstants.DataAttributes.TYPE, "Page");

        String prefixScript = "";
        if (this.getOnDocumentReadyScript() != null) {
            prefixScript = this.getOnDocumentReadyScript();
        }

        View view = ViewLifecycle.getView();
        if (view instanceof FormView && ((FormView) view).isValidateClientSide()) {
            this.setOnDocumentReadyScript(prefixScript + "\nsetupPage(true);");
        } else {
            this.setOnDocumentReadyScript(prefixScript + "\nsetupPage(false);");
        }

        breadcrumbOptions.finalizeBreadcrumbs(model, this, breadcrumbItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BreadcrumbItem> getHomewardPathBreadcrumbs() {
        return breadcrumbOptions == null ? null : breadcrumbOptions.getHomewardPathBreadcrumbs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BreadcrumbItem> getPreViewBreadcrumbs() {
        return breadcrumbOptions == null ? null : breadcrumbOptions.getPreViewBreadcrumbs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BreadcrumbItem> getPrePageBreadcrumbs() {
        return breadcrumbOptions == null ? null : breadcrumbOptions.getPrePageBreadcrumbs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BreadcrumbItem> getBreadcrumbOverrides() {
        return breadcrumbOptions == null ? null : breadcrumbOptions.getBreadcrumbOverrides();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @BeanTagAttribute(name = "autoFocus")
    public boolean isAutoFocus() {
        return this.autoFocus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @BeanTagAttribute(name = "breadcrumbOptions", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public PageBreadcrumbOptions getBreadcrumbOptions() {
        return breadcrumbOptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBreadcrumbOptions(PageBreadcrumbOptions breadcrumbOptions) {
        this.breadcrumbOptions = breadcrumbOptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @BeanTagAttribute(name = "breadcrumbItem", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BreadcrumbItem getBreadcrumbItem() {
        return breadcrumbItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBreadcrumbItem(BreadcrumbItem breadcrumbItem) {
        this.breadcrumbItem = breadcrumbItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @BeanTagAttribute(name = "stickyFooter")
    public boolean isStickyFooter() {
        return stickyFooter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStickyFooter(boolean stickyFooter) {
        this.stickyFooter = stickyFooter;

        if (this.getFooter() != null) {
            this.getFooter().addDataAttribute(UifConstants.DataAttributes.STICKY_FOOTER, Boolean.toString(
                    stickyFooter));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);

        PageGroup pageGroupCopy = (PageGroup) component;

        pageGroupCopy.setAutoFocus(this.autoFocus);
        pageGroupCopy.setStickyFooter(this.stickyFooter);
        pageGroupCopy.setFormPostUrl(formPostUrl);

        if (breadcrumbOptions != null) {
            pageGroupCopy.setBreadcrumbOptions((PageBreadcrumbOptions) this.breadcrumbOptions.copy());
        }

        if (breadcrumbItem != null) {
            pageGroupCopy.setBreadcrumbItem((BreadcrumbItem) this.breadcrumbItem.copy());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean(this);

        // Checks that no invalid items are present
        for (int i = 0; i < getItems().size(); i++) {
            if (PageGroup.class.isAssignableFrom(getItems().get(i).getClass())
                    || NavigationGroup.class.isAssignableFrom(getItems().get(i).getClass())) {
                String currentValues[] = {"item(" + i + ").class =" + getItems().get(i).getClass()};
                tracer.createError("Items in PageGroup cannot be PageGroup or NaviagtionGroup", currentValues);
            }
        }

        super.completeValidation(tracer.getCopy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @BeanTagAttribute(name = "formPostUrl")
    public String getFormPostUrl() {
        return formPostUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFormPostUrl(String formPostUrl) {
        this.formPostUrl = formPostUrl;
    }

}
