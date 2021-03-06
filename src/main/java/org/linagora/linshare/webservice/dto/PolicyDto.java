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
package org.linagora.linshare.webservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.linagora.linshare.core.domain.entities.Policy;

@JsonIgnoreProperties({"previousPolicy"})
@XmlRootElement(name = "Policy")
public class PolicyDto {
	
	protected String policy;
	
	// Current status of the policy
	protected boolean status;

	// Default value for the field status when the policy is reset (ex changing policy from FORBIDEN to ALLOWED.
	protected boolean defaultStatus;
	
	// This field is designed to indicate if the parent functionality allow you to update the policy.
	protected boolean parentAllowUpdate;

	
	public PolicyDto() {
		super();
	}

	public PolicyDto(String policy, boolean status, boolean defaultStatus) {
		super();
		this.policy = policy;
		this.status = status;
		this.defaultStatus = defaultStatus;
		this.parentAllowUpdate = false;
	}
	
	public PolicyDto(Policy policy) {
		super();
		this.policy = policy.getPolicy().toString();
		this.status = policy.getStatus();
		this.defaultStatus = policy.getDefaultStatus();
		this.parentAllowUpdate = false;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getDefaultStatus() {
		return defaultStatus;
	}

	public void setDefaultStatus(boolean defaultStatus) {
		this.defaultStatus = defaultStatus;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public boolean getParentAllowUpdate() {
		return parentAllowUpdate;
	}

	public void setParentAllowUpdate(boolean parentAllowUpdate) {
		this.parentAllowUpdate = parentAllowUpdate;
	}
	
}
