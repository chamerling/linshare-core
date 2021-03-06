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
package org.linagora.linshare.core.domain.entities;

import java.util.Calendar;

import org.linagora.linshare.core.domain.constants.LogAction;

public class ShareLogEntry extends FileLogEntry {

	private static final long serialVersionUID = -2189443188392440017L;

	private final String targetMail;

	private final String targetFirstname;
	
	private final String targetLastname;
	
	private final String targetDomain;

	private final Calendar expirationDate;
	
	protected ShareLogEntry() {
		super();
		this.targetMail = null;
		this.targetFirstname = null;
		this.targetLastname = null;
		this.targetDomain = null;
		this.expirationDate = null;
	}
	
	
	public ShareLogEntry(Account actor, ShareEntry share,	LogAction logAction, String description) {
		
		super(actor, logAction, description, share.getName(), share.getSize(), share.getType());
		
		Account target = share.getRecipient();
		this.targetDomain = target.getDomainId();
		if(isUser(target)) {
			User user = (User)target;
			this.targetMail = user.getMail();
			this.targetFirstname = user.getFirstName();
			this.targetLastname = user.getLastName();
		} else {
			this.targetMail = target.getLsUuid();
			this.targetFirstname = "";
			this.targetLastname = "";
		}
		this.expirationDate = share.getExpirationDate();
		
	}
	
	public ShareLogEntry(Account actor, AnonymousShareEntry share,	LogAction logAction, String description) {
		
		super(actor, logAction, description, share.getName(), share.getSize(), share.getType());
		
		this.targetDomain = "";
		this.targetMail = share.getAnonymousUrl().getContact().getMail();
		this.targetFirstname = "";
		this.targetLastname = "";
		this.expirationDate = share.getExpirationDate();
	}
	
	public ShareLogEntry(Account actor, DocumentEntry document, LogAction logAction, String description, Calendar expirationDate) {
		
		super(actor, logAction, description, document.getName(), document.getSize(), document.getType());
		
		Account target = document.getEntryOwner();
		this.targetDomain = target.getDomainId();
		if(isUser(target)) {
			User user = (User)target;
			this.targetMail = user.getMail();
			this.targetFirstname = user.getFirstName();
			this.targetLastname = user.getLastName();
		} else {
			this.targetMail = target.getLsUuid();
			this.targetFirstname = "";
			this.targetLastname = "";
		}
		this.expirationDate = expirationDate;
		
	}

	
	
	public ShareLogEntry(Account actor, LogAction logAction, String description, String fileName, Long fileSize, String fileType, Account target, Calendar expirationDate) {
		super(actor, logAction, description, fileName, fileSize, fileType);

		this.targetDomain = target.getDomainId();
		if(isUser(target)) {
			User user = (User)target;
			this.targetMail = user.getMail();
			this.targetFirstname = user.getFirstName();
			this.targetLastname = user.getLastName();
		} else {
			this.targetMail = target.getLsUuid();
			this.targetFirstname = "";
			this.targetLastname = "";
		}
		this.expirationDate = expirationDate;
	}
	
	/*
	 * Wrapper for ease of use
	 * 
	 * This constructor is used for Share Download logging
	 */
	public ShareLogEntry(Account actor, LogAction logAction, String description, ShareEntry shareEntry, Account target) {
		this(actor, logAction, "Download of a sharing", shareEntry.getDocumentEntry().getName(),
				shareEntry.getDocumentEntry().getSize(), shareEntry.getDocumentEntry().getType(),
				shareEntry.getEntryOwner(), shareEntry.getExpirationDate());
	}
	
	public String getTargetMail() {
		return targetMail;
	}

	public String getTargetFirstname() {
		return targetFirstname;
	}

	public String getTargetLastname() {
		return targetLastname;
	}
	public Calendar getExpirationDate() {
		return expirationDate;
	}
	
	public String getTargetDomain() {
		return targetDomain;
	}

}
