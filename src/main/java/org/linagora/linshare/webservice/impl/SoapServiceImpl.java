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
package org.linagora.linshare.webservice.impl;

import java.util.List;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.ws.soap.MTOM;

import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.facade.WebServiceDocumentFacade;
import org.linagora.linshare.core.facade.WebServiceShareFacade;
import org.linagora.linshare.core.facade.WebServiceThreadFacade;
import org.linagora.linshare.core.facade.WebServiceUserFacade;
import org.linagora.linshare.webservice.SoapService;
import org.linagora.linshare.webservice.dto.DocumentAttachement;
import org.linagora.linshare.webservice.dto.DocumentDto;
import org.linagora.linshare.webservice.dto.ShareDto;
import org.linagora.linshare.webservice.dto.SimpleLongValue;
import org.linagora.linshare.webservice.dto.ThreadDto;
import org.linagora.linshare.webservice.dto.UserDto;

@WebService(serviceName = "SoapWebService", endpointInterface = "org.linagora.linshare.webservice.SoapService",
	targetNamespace = WebserviceBase.NAME_SPACE_NS, portName = "SoapServicePort")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT,parameterStyle = ParameterStyle.WRAPPED ,use = SOAPBinding.Use.LITERAL)
@MTOM
public class SoapServiceImpl extends WebserviceBase implements
		SoapService {


	private final WebServiceDocumentFacade webServiceDocumentFacade;

	private final WebServiceShareFacade webServiceShareFacade;
	
	private final WebServiceThreadFacade webServiceThreadFacade;
	
	private final WebServiceUserFacade webServiceUserFacade;
	
	public SoapServiceImpl(
			final WebServiceDocumentFacade webServiceDocumentFacade, final WebServiceShareFacade webServiceShareFacade,
			WebServiceThreadFacade webServiceThreadFacade,
			WebServiceUserFacade webServiceUserFacade) {
		this.webServiceDocumentFacade = webServiceDocumentFacade;
		this.webServiceShareFacade = webServiceShareFacade;
		this.webServiceThreadFacade = webServiceThreadFacade;
		this.webServiceUserFacade = webServiceUserFacade;
	}

	
	
	// Documents
	
	/**
	 * get the files of the user
	 * @throws BusinessException 
	 */
	@WebMethod(operationName = "getDocuments")
	// **soap
	@Override
	public List<DocumentDto> getDocuments() throws BusinessException {
		webServiceDocumentFacade.checkAuthentication();
		return webServiceDocumentFacade.getDocuments();
	}

	/**
	 * here we use XOP method for large file upload
	 * 
	 * @param doca
	 * @throws BusinessException 
	 */

	@Oneway
	@WebMethod(operationName = "addDocumentXop")
	// **soap
	@Override
	public DocumentDto addDocumentXop(DocumentAttachement doca) throws BusinessException {
		webServiceDocumentFacade.checkAuthentication();
		return webServiceDocumentFacade.addDocumentXop(doca);
	}

	@WebMethod(operationName = "getUserMaxFileSize")
	// **soap
	@Override
	public SimpleLongValue getUserMaxFileSize() throws BusinessException {
		webServiceDocumentFacade.checkAuthentication();
		return new SimpleLongValue(webServiceDocumentFacade.getUserMaxFileSize());
	}

	@WebMethod(operationName = "getAvailableSize")
	// **soap
	@Override
	public SimpleLongValue getAvailableSize() throws BusinessException {
		webServiceDocumentFacade.checkAuthentication();
		return new SimpleLongValue(webServiceDocumentFacade.getAvailableSize());
	}
	
	
	// Shares
	@Override
	public void sharedocument(String targetMail, String uuid,int securedShare) throws BusinessException {
		webServiceShareFacade.checkAuthentication(); //raise exception
		webServiceShareFacade.sharedocument(targetMail, uuid, securedShare);
	}
	
	@WebMethod(operationName = "getReceivedShares")
	// **soap
	@Override
	public List<ShareDto> getReceivedShares() throws BusinessException {
		webServiceShareFacade.checkAuthentication();
		return webServiceShareFacade.getReceivedShares();
	}


	
	
	
	
	// PluginManagment
	@WebMethod(operationName = "getInformation")
	// **soap
	@Override
	public String getInformation() throws BusinessException {
		return "This API is still in developpement";
	}


	
	
	
	// Threads

	@Override
	public List<ThreadDto> getAllMyThread() throws BusinessException {
		webServiceThreadFacade.checkAuthentication();
		return webServiceThreadFacade.getAllMyThread();
	}



	
	
	
	// Users
	
	@Override
	public List<UserDto> getUsers() throws BusinessException {
		webServiceUserFacade.checkAuthentication();
		return webServiceUserFacade.getUsers();
	}
	
	
}