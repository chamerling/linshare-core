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
package org.linagora.linshare.core.domain.transformers.impl;

import java.util.ArrayList;
import java.util.List;

import org.linagora.linshare.core.domain.entities.DocumentEntry;
import org.linagora.linshare.core.domain.transformers.Transformer;
import org.linagora.linshare.core.domain.vo.DocumentVo;
import org.linagora.linshare.core.repository.DocumentEntryRepository;

public class DocumentEntryTransformer implements Transformer<DocumentEntry, DocumentVo> {

	private final DocumentEntryRepository documentEntryRepository;

	public DocumentEntryTransformer(
			DocumentEntryRepository documentEntryRepository) {
		super();
		this.documentEntryRepository = documentEntryRepository;
	}

	@Override
	public DocumentVo disassemble(DocumentEntry entityObject) {
		if (null != entityObject) {
			return new DocumentVo(entityObject);
		}
		return null;
	}

	@Override
	public List<DocumentVo> disassembleList(List<DocumentEntry> entityObjectList) {
		ArrayList<DocumentVo> documents = new ArrayList<DocumentVo>();
		for (DocumentEntry documentEntry : entityObjectList) {
			documents.add(disassemble(documentEntry));
		}
		return documents;
	}

	@Override
	public DocumentEntry assemble(DocumentVo valueObject) {
		if (null != valueObject) {
			return documentEntryRepository
					.findById(valueObject.getIdentifier());
		} else {
			return null;
		}
	}

	@Override
	public List<DocumentEntry> assembleList(List<DocumentVo> valueObjectList) {
		ArrayList<DocumentEntry> documents = new ArrayList<DocumentEntry>();
		for (DocumentVo documentVo : valueObjectList) {
			documents.add(assemble(documentVo));
		}
		return documents;
	}

}
