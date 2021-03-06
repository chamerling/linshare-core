package org.linagora.linshare.core.repository;

import java.util.List;

import org.linagora.linshare.core.domain.entities.MailingList;
import org.linagora.linshare.core.domain.entities.MailingListContact;

public interface MailingListContactRepository extends AbstractRepository<MailingListContact> {

	MailingListContact findById(long id);

	MailingListContact findByUuid(String uuid);

	MailingListContact findByMail(MailingList list, String mail);

	List<String> getAllContactMails(MailingList list);

}
