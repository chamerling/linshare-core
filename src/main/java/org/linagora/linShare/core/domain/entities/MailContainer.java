/*
 *    This file is part of Linshare.
 *
 *   Linshare is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as
 *   published by the Free Software Foundation, either version 3 of
 *   the License, or (at your option) any later version.
 *
 *   Linshare is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public
 *   License along with Foobar.  If not, see
 *                                    <http://www.gnu.org/licenses/>.
 *
 *   (c) 2008 Groupe Linagora - http://linagora.org
 *
*/
package org.linagora.linShare.core.domain.entities;

import java.util.HashMap;

import org.linagora.linShare.core.domain.constants.Language;

/**
 * Object that contains the informations used to build the email
 * content. Information hiding principle: for example, the view does 
 * not have to know that the services have to build two types of email 
 * contents (txt and html).
 * 
 * @author sduprey
 *
 */
public class MailContainer {
	private String subject;
	private String contentTXT;
	private String contentHTML;
	private String personalMessage;
	private Language language;
	private HashMap<String, String> data;
	

	/**
	 * Copy constructor
	 * 
	 * @param mailContainer
	 */
	public MailContainer(MailContainer mailContainer) {
		this.subject = mailContainer.subject;
		this.contentTXT = mailContainer.contentTXT;
		this.contentHTML = mailContainer.contentHTML;
		this.personalMessage = mailContainer.personalMessage;
		this.language = mailContainer.language;
		this.data = new HashMap<String, String>(mailContainer.data);
	}

	/**
	 * Constructor provided for testing purpose.
	 * 
	 * @param subject
	 * @param contentTxt
	 * @param contentHTML
	 */
	public MailContainer(String subject, String contentTxt, String contentHTML) {
		super();
		this.subject = subject;
		this.contentTXT = contentTxt;
		this.contentHTML = contentHTML;
	}

	/**
	 * Create a mailContainer, used by the MailContainerBuilder
	 * tapestry service.
	 * 
	 * @param contentTXT template holding mail content in text format
	 * @param contentHTML template holding mail content in html format
	 * @param personalMessage personalMessage: not required
	 * @param language language of the email
	 */
	public MailContainer(String contentTXT, String contentHTML,
			String personalMessage, Language language) {
		super();
		this.contentTXT = contentTXT;
		this.contentHTML = contentHTML;
		this.personalMessage = personalMessage;
		this.language = language;
		this.data = new HashMap<String, String>();
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setContentTXT(String contentTXT) {
		this.contentTXT = contentTXT;
	}

	public void setContentHTML(String contentHTML) {
		this.contentHTML = contentHTML;
	}

	public String getSubject() {
		return subject;
	}

	public String getContentTXT() {
		return contentTXT;
	}

	public String getContentHTML() {
		return contentHTML;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Language getLanguage() {
		return language;
	}

	public String getPersonalMessage() {
		return personalMessage;
	}
	
	public void addData(String key, String value) {
		if (this.data == null) {
			this.data = new HashMap<String, String>();
		}
		this.data.put(key, value);
	}
	
	public String getData(String key) {
		if (this.data.containsKey(key)) {
			return this.data.get(key);
		}
		return null;
	}
}
