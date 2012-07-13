package org.linagora.linshare.core.service.impl;

import org.linagora.linshare.core.business.service.AnonymousUrlBusinessService;
import org.linagora.linshare.core.domain.entities.AnonymousUrl;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.service.AnonymousUrlService;

public class AnonymousUrlServiceImpl implements AnonymousUrlService {

	private final AnonymousUrlBusinessService anonymousUrlBusinessService;
	
	
	public AnonymousUrlServiceImpl(AnonymousUrlBusinessService anonymousUrlBusinessService) {
		super();
		this.anonymousUrlBusinessService = anonymousUrlBusinessService;
	}

}