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
package org.linagora.linshare.core.repository.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.linagora.linshare.core.domain.entities.Account;
import org.linagora.linshare.core.domain.entities.Tag;
import org.linagora.linshare.core.repository.TagRepository;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class TagRepositoryImpl extends AbstractRepositoryImpl<Tag>  implements TagRepository {

	public TagRepositoryImpl(HibernateTemplate hibernateTemplate) {
		super(hibernateTemplate);
	}


	@Override
	public Tag findById(Long id) {
		List<Tag> tags = findByCriteria(Restrictions.eq("id", id).ignoreCase());
        if (tags == null || tags.isEmpty()) {
            return null;
        } else if (tags.size() == 1) {
            return tags.get(0);
        } else {
            throw new IllegalStateException("Mail must be unique");
        }
	}
	

	@Override
	public Tag findByOwnerAndName(Account owner, String name) {
		
		if(owner == null) throw new IllegalStateException("Tag owner must be set");
		if(name == null) throw new IllegalStateException("Tag name must be set");
		
		List<Tag> results = findByCriteria(Restrictions.eq("owner", owner), Restrictions.eq("name", name));
		if (results == null || results.isEmpty()) {
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            throw new IllegalStateException("Tag must be unique");
        }
	}

	
	@Override
	protected DetachedCriteria getNaturalKeyCriteria(Tag entity) {
		DetachedCriteria det = DetachedCriteria.forClass(Tag.class).add(Restrictions.eq("name", entity.getName())).add(Restrictions.eq("owner", entity.getOwner()));
		return det;
	}

}
