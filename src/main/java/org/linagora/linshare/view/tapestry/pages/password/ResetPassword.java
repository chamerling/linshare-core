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
package org.linagora.linshare.view.tapestry.pages.password;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.chenillekit.tapestry.core.components.Kaptcha;
import org.linagora.linshare.core.domain.vo.UserVo;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.facade.AbstractDomainFacade;
import org.linagora.linshare.core.facade.UserFacade;
import org.linagora.linshare.view.tapestry.beans.ShareSessionObjects;
import org.linagora.linshare.view.tapestry.enums.BusinessUserMessageType;
import org.linagora.linshare.view.tapestry.objects.BusinessUserMessage;
import org.linagora.linshare.view.tapestry.objects.MessageSeverity;
import org.linagora.linshare.view.tapestry.services.BusinessMessagesManagementService;
import org.slf4j.Logger;

public class ResetPassword {
	@Property
	@Persist
	private String mail;
	
	@Component
	private Kaptcha kaptcha;

	@Property
	private boolean kaptchaValid;

	@Inject
	private Logger logger;

	@SessionState
	private ShareSessionObjects shareSessionObjects;
	
	@Inject
	private UserFacade userFacade;

    @Inject
    private BusinessMessagesManagementService businessMessagesManagementService;

	@Inject
	private Messages messages;
    
	@Inject @Symbol("linshare.domain.visible")
	@Property
	private boolean domainVisible;
	
    @Inject
	private AbstractDomainFacade domainFacade;
	
	@Persist
	@Property
	private List<String> availableDomains;

    @Property
    @Persist
    private String selectedDomainId;
        
    @SetupRender
	public void init() throws BusinessException {
		if (domainVisible) {
			availableDomains = domainFacade.getAllDomainIdentifiers();
		}
	}

	public boolean onValidate() {
		if (mail == null) {
			return false;
		}
		return true;
	}

	public Object onSuccess() throws BusinessException {
		if (!kaptchaValid) {
	    	shareSessionObjects.addError(messages.get("pages.password.error.badcaptcha"));
			return this;
		}
		logger.debug("Capsha is valid, finding user in " + selectedDomainId + " ... ");
		
		UserVo user = userFacade.findUserForResetPassordForm(mail, selectedDomainId);
		logger.debug("user found ... ");
		if (null == user) {
			shareSessionObjects.addError(messages.get("pages.password.error.badmail"));
			return this;
		}

		if (!user.isGuest()) {
			shareSessionObjects.addError(messages.get("pages.password.error.notGuest"));
			return this;
		}

		try {
			userFacade.resetPassword(user);
		} catch (BusinessException e) {
			// should never occur.
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		businessMessagesManagementService.notify(new BusinessUserMessage(BusinessUserMessageType.PASSWORD_RESET_SUCCESS,
                MessageSeverity.INFO));
		mail=null;

		return this;
	}
    
    Object onException(Throwable cause) {
    	shareSessionObjects.addError(messages.get("global.exception.message"));
    	logger.error(cause.getMessage());
    	cause.printStackTrace();
    	return this;
    }

}
