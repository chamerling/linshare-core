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
package org.linagora.linshare.view.tapestry.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.linagora.linshare.core.domain.vo.HelpVO;
import org.linagora.linshare.view.tapestry.enums.HelpType;
import org.linagora.linshare.view.tapestry.objects.HelpsASO;
import org.linagora.linshare.view.tapestry.pages.help.ManualText;
import org.linagora.linshare.view.tapestry.pages.help.ManualVideo;


/**
 * this components launches the help.
 * @author ngapaillard
 *
 */
public class HelpLauncher {

	@SessionState
	private HelpsASO helpASO;

	

	@SuppressWarnings("unused")
	@Property
	private String uuid;
	
	private boolean helpASOExists;
	/**
	 * The id of the section.
	 */
	
	@Parameter(required=true,allowNull=false,defaultPrefix=BindingConstants.LITERAL)
	@Property
	private String idSection;
	
	/**
	 * The css class
	 */
	@SuppressWarnings("unused")
	@Parameter(name="class",required=true,allowNull=false,defaultPrefix=BindingConstants.LITERAL)
	@Property
	private String cssClass;
	
	
	/**
	 * The role associated with the section.
	 */
	@Parameter(required=true,allowNull=false, defaultPrefix=BindingConstants.LITERAL)
	private String role;
	
	
	/**
	 * The number of descriptions by subsections.
	 * For ex.: 3,2,1
	 * 3 descriptions for the first subsection.
	 * 2 descriptions for the second subsection.
	 * 1 description for the third subsection.
	 * 
	 */
	@Parameter(required=false,allowNull=false, defaultPrefix=BindingConstants.LITERAL)
	private String descItems;
	
	/**
	 * The extension of the image.
	 * By default png.
	 */
	@Parameter(required=false,value="png",allowNull=true, defaultPrefix=BindingConstants.LITERAL)
	private String imgExtension;
	
	/**
	 * The extension of the video.
	 * By default swf.
	 */
	@Parameter(required=false,value="swf",allowNull=false, defaultPrefix=BindingConstants.LITERAL)
	private String videoExtension;
	
	
	/**
	 * The title of the help.
	 */
	@SuppressWarnings("unused")
	@Parameter(required=true,allowNull=false, defaultPrefix=BindingConstants.LITERAL)
	@Property
	private String title;
	
	/**
	 * The type of the help.
	 * video or text.
	 * by default text.
	 */
	@SuppressWarnings("unused")
	@Parameter(required=false,allowNull=false, defaultPrefix=BindingConstants.LITERAL)
	@Property
	private boolean video;
	
	@InjectPage
	private ManualText textPage;
	
	@InjectPage
	private ManualVideo videoPage;
	
	@SetupRender
	public void init(){
		if(!helpASOExists){
			helpASO=new HelpsASO();
		}
		if(!video){
			HelpVO helpVo=new HelpVO(idSection,role, descItems,imgExtension,HelpType.TEXT);
			this.uuid=helpVo.getUuid();
			helpASO.add(helpVo);
		}else{
			HelpVO helpVo=new HelpVO(idSection,role,videoExtension);
			this.uuid=helpVo.getUuid();
			helpASO.add(helpVo);
				
		}
		
	}
	
	public Object onActionFromHelp(String uuid){
		if(!video){
			textPage.setUuid(uuid);
			
			return textPage;
		}else{
			videoPage.setUuid(uuid);
			
			return videoPage;
		}
	}

	
	
	 
}
