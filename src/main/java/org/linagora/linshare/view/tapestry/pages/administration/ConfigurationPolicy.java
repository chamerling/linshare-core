/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2013 LINAGORA
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version, provided you comply with the Additional Terms applicable for
 * LinShare software by Linagora pursuant to Section 7 of the GNU Affero General
 * Public License, subsections (b), (c), and (e), pursuant to which you must
 * notably (i) retain the display of the “LinShare™” trademark/logo at the top
 * of the interface window, the display of the “You are using the Open Source
 * and free version of LinShare™, powered by Linagora © 2009–2013. Contribute to
 * Linshare R&D by subscribing to an Enterprise offer!” infobox and in the
 * e-mails sent with the Program, (ii) retain all hypertext links between
 * LinShare and linshare.org, between linagora.com and Linagora, and (iii)
 * refrain from infringing Linagora intellectual property rights over its
 * trademarks and commercial brands. Other Additional Terms apply, see
 * <http://www.linagora.com/licenses/> for more details.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License and
 * its applicable Additional Terms for LinShare along with this program. If not,
 * see <http://www.gnu.org/licenses/> for the GNU Affero General Public License
 * version 3 and <http://www.linagora.com/licenses/> for the Additional Terms
 * applicable to LinShare software.
 */
package org.linagora.linshare.view.tapestry.pages.administration;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.linagora.linshare.core.domain.vo.PolicyVo;
import org.linagora.linshare.core.domain.vo.UserVo;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.facade.AbstractDomainFacade;
import org.linagora.linshare.core.facade.FunctionalityFacade;
import org.linagora.linshare.core.facade.UserFacade;
import org.linagora.linshare.view.tapestry.beans.ShareSessionObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigurationPolicy {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigurationPolicy.class);

    @SessionState
    @Property
    private ShareSessionObjects shareSessionObjects;

    /* ***********************************************************
     *                      Injected services
     ************************************************************ */

	@Inject
	private Messages messages;
    @Inject
    private AbstractDomainFacade abstractDomainFacade;
    
    @Inject
    private FunctionalityFacade functionalityFacade;
    
    @Inject
    private UserFacade userFacade;
    
    @SessionState
    private UserVo loginUser;
    
    @Persist
	@Property
	private String selectedDomain;
	
	@Persist
	@Property
	private List<String> domains;
	
	@Property
	@Persist
	private boolean superadmin;
	
	@Property
	@Persist
	private boolean admin;

	@Property
	private boolean noDomain;
	
	
	@InjectComponent
	private Form policyForm;
	
	@Property
	@Persist
	private List<PolicyVo> configurationPolicies;
	

    /* ***********************************************************
     *                   Event handlers&processing
     ************************************************************ */
    @SetupRender
    public void init() throws BusinessException {
    	superadmin = loginUser.isSuperAdmin();
    	admin = loginUser.isAdministrator();
    }   
    

	public Object onActivate(String identifier) throws BusinessException {
		logger.debug("domainIdentifier:" + identifier);
		selectedDomain = identifier;

		domains = abstractDomainFacade.getAllDomainIdentifiers(loginUser);
		if(!domains.contains(selectedDomain)) {
			shareSessionObjects.addError(messages.get("pages.error.badAuth.message"));
			return org.linagora.linshare.view.tapestry.pages.administration.Index.class;
    	}
		
		configurationPolicies = functionalityFacade.getAllConfigurationPolicy(identifier);
		Collections.sort(configurationPolicies);
		return null;
	}
    
	public Object onSuccessFromPolicyForm() throws BusinessException {
		logger.debug("onSuccessFromPolicyForm");
		functionalityFacade.updateConfigurationPolicies(loginUser, configurationPolicies);
		return org.linagora.linshare.view.tapestry.pages.administration.Index.class;
	}
	
    Object onException(Throwable cause) {
    	shareSessionObjects.addError(messages.get("global.exception.message"));
    	logger.error(cause.getMessage());
    	cause.printStackTrace();
    	return this;
    }
    
    public Object onActionFromCancel() {
		configurationPolicies = null;
		return org.linagora.linshare.view.tapestry.pages.administration.Index.class;
	}
}
