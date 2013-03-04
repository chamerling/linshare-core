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
package org.linagora.linshare.core.facade.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.linagora.linshare.core.domain.entities.DocumentEntry;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.exception.BusinessErrorCode;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.facade.WebServiceDocumentFacade;
import org.linagora.linshare.core.service.AccountService;
import org.linagora.linshare.core.service.DocumentEntryService;
import org.linagora.linshare.webservice.dto.DocumentAttachement;
import org.linagora.linshare.webservice.dto.DocumentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebServiceDocumentFacadeImpl implements WebServiceDocumentFacade {
    private static final Logger logger = LoggerFactory.getLogger(WebServiceDocumentFacade.class);
    
	private final DocumentEntryService documentEntryService;
	private final AccountService accountService;

	public WebServiceDocumentFacadeImpl(final DocumentEntryService documentEntryService, final AccountService accountService) {
		this.documentEntryService = documentEntryService;
		this.accountService = accountService;
	}
	
	
	@Override
	public List<DocumentDto> getDocuments() throws BusinessException {
		User actor = getAuthentication();
		 
		List<DocumentEntry> docs;
		try {
			docs = documentEntryService.findAllMyDocumentEntries(actor, actor);
		} catch (BusinessException e) {
			throw e;
		}
 
		if (docs == null) {
			throw new BusinessException(BusinessErrorCode.WEBSERVICE_NOT_FOUND, "No such document");
		}
		
		return convertDocumentEntryList (docs);
	}

	
	@Override
	public DocumentDto uploadfile(InputStream fi, String filename, String description) throws BusinessException{
		DocumentEntry res;
		
		try {
			User actor = getAuthentication();

			res =  documentEntryService.createDocumentEntry(actor, fi, filename);
			documentEntryService.updateFileProperties(actor, res.getUuid(), res.getName(), description);
		} catch (BusinessException e) {
			throw e;
		}
		
		return new DocumentDto(res);
	}
	

	
	@Override
	public User checkAuthentication() throws BusinessException {
		
		User actor = getAuthentication();
		
		if (actor== null) {
			throw new BusinessException(BusinessErrorCode.WEBSERVICE_UNAUTHORIZED, "You are not authorized to use this service");
		}
		
		return actor;
	}
	
	

	@Override
	public DocumentDto addDocumentXop(DocumentAttachement doca)  throws BusinessException {
    	DocumentEntry res;
    	
    	try {
    		User actor = getAuthentication();
    		DataHandler dh = doca.getDocument();
    		InputStream in = dh.getInputStream();
			
			res =  documentEntryService.createDocumentEntry(actor, in, doca.getFilename());
			
			//mandatory ?
		 	String comment = (doca.getComment() == null)? "" : doca.getComment();
			
			documentEntryService.updateFileProperties(actor, res.getUuid(), res.getName(), comment);
		} catch (IOException e) {
			throw  new BusinessException(BusinessErrorCode.WEBSERVICE_FAULT, "unable to upload",e);
		} catch (BusinessException e) {
			throw e;
		}
		
		return new DocumentDto(res);
	}
	
	@Override
	public Long getUserMaxFileSize() throws BusinessException {
		
		Long res;
		
		try {
			User actor = getAuthentication();
			res = documentEntryService.getUserMaxFileSize(actor);
		} catch (BusinessException e) {
			throw e;
		}
		return res;
	}
	
	@Override
	public Long getAvailableSize() throws BusinessException {
		Long res;
		
		try {
			User actor = getAuthentication();
			res = documentEntryService.getAvailableSize(actor);
		} catch (BusinessException e) {
			throw e;
		}
		return res;
	}
	
	
	
	//#############  utility methods
	
	private User getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
	     String name =  (auth != null) ? auth.getName() : null; //get logged in username
	     logger.debug("Authentication auth : " + name);
	     if (name == null) {
	    	 return null;
	     }
	     User user = (User) accountService.findByLsUid(name);
	     return user;
	}

	
	private static List<DocumentDto> convertDocumentEntryList(List<DocumentEntry> input) {

		if (input == null)
			return null;

		List<DocumentDto> output = new ArrayList<DocumentDto>();
		for (DocumentEntry var : input) {
			output.add(new DocumentDto(var));
		}
		return output;
	}

}
