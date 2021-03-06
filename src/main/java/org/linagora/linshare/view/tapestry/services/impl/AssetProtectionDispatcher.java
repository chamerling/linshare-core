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
package org.linagora.linshare.view.tapestry.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.tapestry5.internal.services.RequestConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ClasspathAssetAliasManager;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.linagora.linshare.core.domain.constants.SecuredShareConstants;
import org.linagora.linshare.core.exception.BusinessErrorCode;


/**
 * Dispatcher that handles whether to allow or deny access to particular 
 * assets. 
 */
public class AssetProtectionDispatcher implements Dispatcher {

	private final ClasspathAssetAliasManager assetAliasManager;
	@Inject
	private final PageRenderLinkSource pageRenderLinkSource;
	private final List<Pattern> patterns;
	
	public AssetProtectionDispatcher(final PageRenderLinkSource pageRenderLinkSource, 
			final ClasspathAssetAliasManager manager, final List<String> regex) {
		this.assetAliasManager = manager;
		this.pageRenderLinkSource = pageRenderLinkSource;
		patterns = new ArrayList<Pattern>();
		for(String r : regex) {
			patterns.add(Pattern.compile(r));
		}
	}

	public boolean dispatch(Request request, Response response)	throws IOException {

		//we only protect assets, and don't examine any other url's.
		String path = request.getPath();

		if (!path.startsWith(RequestConstants.ASSET_PATH_PREFIX)) {
			return false;
		}
		
		String resourcePath = assetAliasManager.toResourcePath(path);
		
		for(Pattern p : patterns) {
			if(p.matcher(resourcePath).matches()) {
				//the resource is authorized
				return false;
			}
		}
		
		//if we get here, no regexp matches the path, so raise an error
		response.sendRedirect(this.pageRenderLinkSource.createPageRenderLinkWithContext(SecuredShareConstants.ERROR_PAGE, BusinessErrorCode.AUTHENTICATION_ERROR));
		return true;
	}

}
