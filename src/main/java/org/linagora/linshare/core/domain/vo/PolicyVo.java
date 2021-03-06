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
package org.linagora.linshare.core.domain.vo;

import org.linagora.linshare.core.domain.constants.Policies;
import org.linagora.linshare.core.domain.entities.Policy;

/**
 * this class is designed to handle the 'enable/disable' status of a particular functionality.
 */
public class PolicyVo implements Comparable{

	private String domainIdentifier;

	private String functionalityIdentifier;

    private String name;

	/**
	 * enable/disable the policy
	 */
	private boolean status;

	private Policies policy = Policies.ALLOWED;

	private boolean defaultStatus;

	public PolicyVo() {
		super();
	}
	
	public PolicyVo(Policy policy, String funcId, String domainIdentifier) {
		super();
		this.status = policy.getStatus();
		this.policy = policy.getPolicy();
		this.defaultStatus = policy.getDefaultStatus();
		this.functionalityIdentifier = funcId;
		this.domainIdentifier = domainIdentifier;
	}
	
	public PolicyVo(PolicyVo current) {
		super();
		setDefaultStatus(current.getDefaultStatus());
		setStatus(current.getStatus());
		setPolicy(current.getPolicy());
		this.functionalityIdentifier = current.getFunctionalityIdentifier();
		this.domainIdentifier = current.getDomainIdentifier();
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
//		System.out.println("functionalityIdentifier: " + functionalityIdentifier + " :setStatus : " + status);
		this.status = status;
	}

	public Policies getPolicy() {
		return policy;
	}

	public void setPolicy(Policies policy) {
//		System.out.println("functionalityIdentifier: " + functionalityIdentifier + " :setPolicy : " + policy);
		this.policy = policy;
	}

	public boolean getDefaultStatus() {
		return defaultStatus;
	}

	public void setDefaultStatus(boolean default_status) {
		this.defaultStatus = default_status;
	}

	public String toString() {
		return "Policy=" + functionalityIdentifier + ":status=" + status + ":policy=" + policy;
	}

	public String getFunctionalityIdentifier() {
		return functionalityIdentifier;
	}

	public void setFunctionalityIdentifier(String functionalityIdentifier) {
		this.functionalityIdentifier = functionalityIdentifier;
	}

	public String getDomainIdentifier() {
		return domainIdentifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDomainIdentifier(String domainIdentifier) {
		this.domainIdentifier = domainIdentifier;
	}

	@Override
	public int compareTo(Object activationPolicies) {
		PolicyVo obj = (PolicyVo)activationPolicies;
		return this.functionalityIdentifier.compareToIgnoreCase(obj.getFunctionalityIdentifier());
	}
}
