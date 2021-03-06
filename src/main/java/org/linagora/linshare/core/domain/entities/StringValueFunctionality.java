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
package org.linagora.linshare.core.domain.entities;

import java.util.ArrayList;
import java.util.List;

import org.linagora.linshare.core.domain.constants.FunctionalityType;
import org.linagora.linshare.core.domain.vo.FunctionalityVo;
import org.linagora.linshare.core.domain.vo.StringValueFunctionalityVo;
import org.linagora.linshare.webservice.dto.FunctionalityDto;
import org.linagora.linshare.webservice.dto.ParameterDto;

public class StringValueFunctionality extends OneValueFunctionality<String> {

	public StringValueFunctionality() {
		super();
	}

	public StringValueFunctionality(String identifier, boolean system, Policy activationPolicy,
			Policy configurationPolicy, AbstractDomain domain, String value) {
		super(identifier, system, activationPolicy, configurationPolicy, domain, value);
	}

	@Override
	public FunctionalityType getType() {
		return FunctionalityType.STRING;
	}

	@Override
	public boolean businessEquals(Functionality obj, boolean checkPolicies) {
		if (super.businessEquals(obj, checkPolicies)) {
			StringValueFunctionality o = (StringValueFunctionality) obj;
			if (value.equals(o.getValue())) {
				logger.debug("StringValueFunctionality : " + this.toString() + " is equal to StringValueFunctionality "
						+ obj.toString());
				return true;
			}
		}
		logger.debug("StringValueFunctionality : " + this.toString() + " is not equal to StringValueFunctionality "
				+ obj.toString());
		return false;
	}

	@Override
	public void updateFunctionalityFrom(Functionality functionality) {
		super.updateFunctionalityFrom(functionality);
		this.updateFunctionalityValuesOnlyFrom(functionality);
	}

	@Override
	public void updateFunctionalityValuesOnlyFrom(Functionality functionality) {
		StringValueFunctionality f = (StringValueFunctionality) functionality;
		this.value = f.getValue();
	}

	@Override
	public void updateFunctionalityValuesOnlyFromVo(FunctionalityVo functionality) {
		StringValueFunctionalityVo f = (StringValueFunctionalityVo) functionality;
		this.value = f.getValue();
	}

	@Override
	public void updateFunctionalityValuesOnlyFromDto(FunctionalityDto functionality) {
		List<ParameterDto> parameters = functionality.getParameters();
		if (parameters != null && !parameters.isEmpty()) {
			ParameterDto parameterDto = parameters.get(0);
			this.value = parameterDto.getString();
		}
	}

	@Override
	public List<ParameterDto> getParameters() {
		List<ParameterDto> res = new ArrayList<ParameterDto>();
		res.add(new ParameterDto(this.getValue()));
		return res;
	}
}
