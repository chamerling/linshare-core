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

import org.linagora.linshare.core.domain.constants.ThreadRoles;
import org.linagora.linshare.core.domain.entities.ThreadMember;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class ThreadMemberVo implements Comparable<ThreadMemberVo> {

	private UserVo user;

	private boolean canUpload;

	private boolean admin;

	public ThreadMemberVo(ThreadMember threadMember) {
		super();
		this.user = new UserVo(threadMember.getUser());
		this.admin = threadMember.getAdmin();
		this.canUpload = this.admin || threadMember.getCanUpload();
	}

	public ThreadMemberVo(UserVo user, boolean canUpload, boolean admin) {
		super();
		this.user = user;
		this.canUpload = canUpload;
		this.admin = admin;
	}

	public ThreadMemberVo() {
	}

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}

	public boolean isCanUpload() {
		return canUpload;
	}

	public void setCanUpload(boolean canUpload) {
		this.canUpload = canUpload;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getMail() {
		return user.getMail();
	}

	public String getFullName() {
		return user.getFullName();
	}

	public String getLsUuid() {
		return user.getLsUuid();
	}

	@Override
	public int compareTo(ThreadMemberVo o) {
		if (this.admin) {
			return o.admin ? this.user.compareTo(o.getUser()) : -1;
		}
		if (this.canUpload)
			return o.admin ? 1 : o.canUpload ? this.user.compareTo(o.getUser())
					: -1;
		return o.admin || o.canUpload ? 1 : this.user.compareTo(o.getUser());
	}

	/*
	 * Transformers
	 */
	public static Function<ThreadMember, ThreadMemberVo> toVo() {
		return new Function<ThreadMember, ThreadMemberVo>() {
			@Override
			public ThreadMemberVo apply(ThreadMember arg0) {
				return new ThreadMemberVo(arg0);
			}
		};
	}

	/*
	 * Filters
	 */
	public static Predicate<ThreadMemberVo> hasRole(final ThreadRoles role) {
		if (role == null)
			return Predicates.alwaysTrue();
		return new Predicate<ThreadMemberVo>() {
			@Override
			public boolean apply(ThreadMemberVo input) {
				switch (role) {
				case ADMIN:
					return input.admin;
				case NORMAL:
					return input.canUpload && !input.admin;
				case RESTRICTED:
					return !(input.canUpload || input.admin);
				default: /* NOT REACHED */
					return true;
				}
			}
		};
	}
	
	public static Predicate<ThreadMemberVo> equalTo(final String identifier) {
		return new Predicate<ThreadMemberVo>() {
			@Override
			public boolean apply(ThreadMemberVo input) {
				return input.getLsUuid().equals(identifier);
			}
		};
	}
}
