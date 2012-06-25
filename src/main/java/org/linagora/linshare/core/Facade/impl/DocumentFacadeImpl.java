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
package org.linagora.linshare.core.Facade.impl;

import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.linagora.linshare.core.Facade.DocumentFacade;
import org.linagora.linshare.core.domain.constants.Reason;
import org.linagora.linshare.core.domain.constants.UserType;
import org.linagora.linshare.core.domain.entities.Document;
import org.linagora.linshare.core.domain.entities.MailContainer;
import org.linagora.linshare.core.domain.entities.Share;
import org.linagora.linshare.core.domain.entities.Signature;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.domain.transformers.impl.DocumentTransformer;
import org.linagora.linshare.core.domain.transformers.impl.SignatureTransformer;
import org.linagora.linshare.core.domain.vo.DisplayableAccountOccupationEntryVo;
import org.linagora.linshare.core.domain.vo.DocumentVo;
import org.linagora.linshare.core.domain.vo.ShareDocumentVo;
import org.linagora.linshare.core.domain.vo.SignatureVo;
import org.linagora.linshare.core.domain.vo.UserVo;
import org.linagora.linshare.core.exception.BusinessErrorCode;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.repository.UserRepository;
import org.linagora.linshare.core.service.DocumentService;
import org.linagora.linshare.core.service.EnciphermentService;
import org.linagora.linshare.core.service.ShareService;
import org.linagora.linshare.view.tapestry.beans.AccountOccupationCriteriaBean;


public class DocumentFacadeImpl implements DocumentFacade {

	private final DocumentService documentService;
	
	private final UserRepository<User> userRepository;
	
	private final DocumentTransformer documentTransformer;
	private final SignatureTransformer signatureTransformer;
	
	private final EnciphermentService enciphermentService;
	
	private final ShareService shareService;
	
	public DocumentFacadeImpl(DocumentService documentService,
			UserRepository<User> userRepository,
			final DocumentTransformer documentTransformer,
			final ShareService shareService,final SignatureTransformer signatureTransformer,EnciphermentService enciphermentService) {
		super();
		this.documentService = documentService;
		this.userRepository = userRepository;
		this.documentTransformer = documentTransformer;
		this.shareService = shareService;
		this.signatureTransformer=signatureTransformer;
		this.enciphermentService = enciphermentService;
	}

	public String getMimeType(InputStream theFileStream, String theFilePath)
			throws BusinessException {
		return documentService.getMimeType(theFileStream, theFilePath);
	}

	public DocumentVo insertFile(InputStream file, long size, String fileName,
			String mimeType, UserVo owner) throws BusinessException {
		User docOwner =  userRepository.findByLsUid(owner.getLogin());
		return documentTransformer.disassemble(documentService.insertFile(docOwner.getLogin(), file, size, fileName, mimeType, docOwner));
	}


	
	public void removeDocument(UserVo actor, DocumentVo document, MailContainer mailContainer)
		throws BusinessException {
		if (document instanceof ShareDocumentVo) {
			// if this document is a sharedocumentVo, it means we received the document
			// and we delete the sharing (for us)

			User receiver = userRepository.findByLsUid(actor.getLogin());
			
			if (receiver == null) {
				throw new BusinessException(BusinessErrorCode.USER_NOT_FOUND, "The user couldn't be found");
			}
			
			// must find the share
			Set<Share> listShare = receiver.getReceivedShares();
			Share shareToRemove = null;
			
			for (Share share : listShare) {
				if (share.getDocument().getIdentifier().equals(document.getIdentifier())) {
					shareToRemove = share;
					break;
				}
			}
			
			if (shareToRemove!=null) {
				shareService.removeReceivedShareForUser(shareToRemove, receiver, receiver);				
			} else {
				throw new BusinessException(BusinessErrorCode.SHARED_DOCUMENT_NOT_FOUND, "The sharing couldn't be found");
			}
		} else {
			documentService.deleteFileWithNotification(actor.getLogin(),document.getIdentifier(), Reason.NONE, mailContainer);
		}
	}

	public DocumentVo getDocument(String login, String uuid) {
		return documentTransformer.disassemble(documentService.getDocument(uuid));
	}
	
	public InputStream downloadSharedDocument(ShareDocumentVo doc, UserVo actor) throws BusinessException {
		return documentService.downloadSharedDocument(doc, actor);
	}
	
	public InputStream retrieveFileStream(DocumentVo doc, UserVo actor) throws BusinessException {
		return documentService.retrieveFileStream(doc, actor);
	}
	
	
	

	public InputStream retrieveFileStream(DocumentVo doc, String actor)
			throws BusinessException {
		return documentService.retrieveFileStream(doc, actor);
	}

	public void insertSignatureFile(InputStream file, long size,
			String fileName, String mimeType, UserVo owner, DocumentVo document, X509Certificate signerCertificate)
			throws BusinessException {
		User docOwner =  userRepository.findByLsUid(owner.getLogin());
		Document doc = documentService.getDocument(document.getIdentifier());
		
		documentService.insertSignatureFile(file, size, fileName, mimeType, docOwner, doc, signerCertificate);
	}
	
	public boolean isSignedDocumentByCurrentUser(UserVo currentSigner, DocumentVo document){
		
		User currentSignerDoc =  userRepository.findByLsUid(currentSigner.getLogin());
		
		Document doc = documentService.getDocument(document.getIdentifier());
		List<Signature> signatures = doc.getSignatures();
		
		boolean res = false;
		
		for (Signature signature : signatures) {
			if (signature.getSigner().equals(currentSignerDoc)) {
				res = true;
				break;
			}
		}
		
		return res;
	}
	
	public boolean isSignedDocument(DocumentVo document){
		Document doc = documentService.getDocument(document.getIdentifier());
		List<Signature> signatures = doc.getSignatures();
		boolean res = false;
		if(signatures!=null && signatures.size()>0) res = true;
		return res;
	}
	
	public List<SignatureVo> getAllSignatures(DocumentVo document){
		Document doc = documentService.getDocument(document.getIdentifier());
		return signatureTransformer.disassembleList(doc.getSignatures());
	}

	public SignatureVo getSignature(UserVo currentSigner,DocumentVo document) {
		User currentSignerDoc =  userRepository.findByLsUid(currentSigner.getLogin());
		
		Document doc = documentService.getDocument(document.getIdentifier());
		List<Signature> signatures = doc.getSignatures();
		
		SignatureVo res = null;
		
		for (Signature signature : signatures) {
			if (signature.getSigner().equals(currentSignerDoc)) {
				res = signatureTransformer.disassemble(signature);
				break;
			}
		}
		return res;
	}
	
	public InputStream retrieveSignatureFileStream(SignatureVo signaturedoc){
		return documentService.retrieveSignatureFileStream(signaturedoc);
	}
	

	@Override
	public Long getUserAvailableQuota(UserVo user) throws BusinessException {
		User currentUser =  userRepository.findByLsUid(user.getLogin());
		return documentService.getAvailableSize(currentUser);
	}
	
	@Override
	public Long getUserMaxFileSize(UserVo user) throws BusinessException {
		User currentUser =  userRepository.findByLsUid(user.getLogin());
		return documentService.getUserMaxFileSize(currentUser);
	}
	
	public List<DisplayableAccountOccupationEntryVo> getAccountOccupationStat(AccountOccupationCriteriaBean criteria) throws BusinessException {
		List<DisplayableAccountOccupationEntryVo> result = new ArrayList<DisplayableAccountOccupationEntryVo>();
		
		List<User> users = userRepository.findByCriteria(criteria);
		for (User user : users) {
			if (user.getDomain() !=null) {
				//user needs to be in a domain to compute quotas
				DisplayableAccountOccupationEntryVo accountOccupation = getAccountStats(user);
				result.add(accountOccupation);
			}
		}
		
		return result;
	}

	private DisplayableAccountOccupationEntryVo getAccountStats(User user) throws BusinessException {
		Long userAvailableQuota = documentService.getAvailableSize(user);
		Long userTotalQuota = documentService.getTotalSize(user);
		Long userUsedSize = 0L;
		for (Document doc : user.getDocuments()) {
			userUsedSize += doc.getSize();
		}
		DisplayableAccountOccupationEntryVo accountOccupation = new DisplayableAccountOccupationEntryVo(user.getFirstName(), 
				user.getLastName(), user.getMail(), user.getAccountType(), 
				userAvailableQuota, userTotalQuota, userUsedSize);
		return accountOccupation;
	}

	public DocumentVo encryptDocument(DocumentVo doc,UserVo user, String password) throws BusinessException{
		Document docenc = enciphermentService.encryptDocument(doc, user, password);
		return documentTransformer.disassemble(documentService.getDocument(docenc.getIdentifier()));
	}

	public DocumentVo decryptDocument(DocumentVo doc, UserVo user,String password) throws BusinessException {
		Document docenc = enciphermentService.decryptDocument(doc, user, password);
		return documentTransformer.disassemble(documentService.getDocument(docenc.getIdentifier()));
	}
	
	public boolean isDocumentEncrypted(DocumentVo doc) {
		return enciphermentService.isDocumentEncrypted(doc);
	}

	public Long getUserTotalQuota(UserVo user) throws BusinessException {
		User currentUser =  userRepository.findByLsUid(user.getLogin());
		return documentService.getTotalSize(currentUser);
	}

	public DocumentVo updateDocumentContent(String currentFileUUID,
			InputStream file, long size, String fileName, String mimeType,
			UserVo owner) throws BusinessException {
		User currentUser =  userRepository.findByLsUid(owner.getLogin());
		return documentTransformer.disassemble(documentService.updateDocumentContent(currentFileUUID, file, size, fileName, mimeType, currentUser)); 
	}

    public void renameFile(String uuid, String newName) {
        documentService.renameFile(uuid, newName);
    }
    public void  updateFileProperties(String uuid, String newName, String comment){
        documentService.updateFileProperties(uuid, newName,comment);
    }
    
    public InputStream getDocumentThumbnail(String uuid) {
    	return documentService.getDocumentThumbnail(uuid);
    }

    public boolean documentHasThumbnail(String uuid) {
		return documentService.documentHasThumbnail(uuid);
    }

	@Override
	public boolean isSignatureActive(UserVo user) {
		User currentUser =  userRepository.findByLsUid(user.getLogin());
		return documentService.isSignatureActive(currentUser);
	}
	
	@Override
	public boolean isEnciphermentActive(UserVo user) {
		User currentUser =  userRepository.findByLsUid(user.getLogin());
		return documentService.isEnciphermentActive(currentUser);
	}

	@Override
	public boolean isGlobalQuotaActive(UserVo user) throws BusinessException {
		User currentUser =  userRepository.findByLsUid(user.getLogin());
		return documentService.isGlobalQuotaActive(currentUser);
	}

	@Override
	public boolean isUserQuotaActive(UserVo user) throws BusinessException {
		User currentUser =  userRepository.findByLsUid(user.getLogin());
		return documentService.isUserQuotaActive(currentUser);
	}

	@Override
	public Long getGlobalQuota(UserVo user) throws BusinessException {
		User currentUser =  userRepository.findByLsUid(user.getLogin());
		return documentService.getGlobalQuota(currentUser);
	}
	
}