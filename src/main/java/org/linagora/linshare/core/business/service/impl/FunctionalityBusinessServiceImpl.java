package org.linagora.linshare.core.business.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.linagora.linshare.core.business.service.FunctionalityBusinessService;
import org.linagora.linshare.core.domain.entities.AbstractDomain;
import org.linagora.linshare.core.domain.entities.Functionality;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.repository.AbstractDomainRepository;
import org.linagora.linshare.core.repository.FunctionalityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class FunctionalityBusinessServiceImpl implements FunctionalityBusinessService {

	private final static Logger logger = LoggerFactory.getLogger(FunctionalityBusinessServiceImpl.class);
	
	private FunctionalityRepository functionalityRepository;

	private AbstractDomainRepository abstractDomainRepository;

	public FunctionalityBusinessServiceImpl(FunctionalityRepository functionalityRepository,
			AbstractDomainRepository abstractDomainRepository) {
		super();
		this.functionalityRepository = functionalityRepository;
		this.abstractDomainRepository = abstractDomainRepository;
	}

	private class InnerFunctionality {

		private final Functionality functionality;

		private final String identifier;

		public InnerFunctionality(Functionality functionality) {
			super();
			this.functionality = functionality;
			this.identifier = functionality.getIdentifier();
		}

		public Functionality getFunctionality() {
			return functionality;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			InnerFunctionality other = (InnerFunctionality) obj;
			if (identifier == null) {
				if (other.identifier != null)
					return false;
			} else if (!identifier.equals(other.identifier))
				return false;
			return true;
		}
	}

	/**
	 * Helper : convert Functionality list to InnerFunctionality list.
	 * 
	 * @param functionalities
	 * @return innerFunctionalities
	 */
	private Set<InnerFunctionality> convertToInnerFunctionality(Set<Functionality> functionalities) {
		Set<InnerFunctionality> res = new HashSet<InnerFunctionality>();
		for (Functionality f : functionalities) {
			res.add(new InnerFunctionality(f));
		}
		return res;
	}

	/**
	 * Helper : convert InnerFunctionality list to Functionality list.
	 * 
	 * @param functionalities
	 * @return innerFunctionalities
	 */
	private Set<Functionality> convertToFunctionality(Set<InnerFunctionality> functionalities) {
		Set<Functionality> res = new HashSet<Functionality>();
		for (InnerFunctionality f : functionalities) {
			res.add(f.getFunctionality());
		}
		return res;
	}

	/**
	 * get all functionalities from current domain, then ask parent domains to
	 * get missing ones, recursively until all root domain is reached. At this
	 * point all LinShare functionalities are collected in the result list.
	 * 
	 * @param domain
	 * @return result contains unique list of InnerFunctionality.
	 *         InnerFunctionality is an association of functionality identifier
	 *         and a functionality.
	 */
	private Set<InnerFunctionality> getAllInnerFunctionalities(AbstractDomain domain) {
		Assert.notNull(domain);

		Set<InnerFunctionality> res = new HashSet<InnerFunctionality>();

		// Copy all functionalities from this domain to result list
		res.addAll(convertToInnerFunctionality(domain.getFunctionalities()));

		if (domain.getParentDomain() != null) {
			// get parent functionalities using recursive call
			for (InnerFunctionality innerFunctionality : this.getAllInnerFunctionalities(domain.getParentDomain())) {
				// only functionality identifier is compared.
				if (!res.contains(innerFunctionality)) {
					res.add(innerFunctionality);
				}
			}
		}
		return res;
	}

	private Functionality getParentFunctionality(AbstractDomain domain, String functionalityIdentifier) {
		Assert.notNull(domain);
		Assert.notNull(functionalityIdentifier);

		Functionality res = null;
		AbstractDomain parentDomain = domain.getParentDomain();
		if (parentDomain != null) {
			for (Functionality f : parentDomain.getFunctionalities()) {
				// only functionality identifier is compared.
				if (f.getIdentifier().equals(functionalityIdentifier)) {
					res = f;
					break;
				}
			}
			// no functionality was found in the current parentDomain. Trying parentDomain of parentDomain, using recursive call.
			if (res == null) {
				res = getParentFunctionality(parentDomain, functionalityIdentifier);
			}
		}
		return res;
	}

	@Override
	public Set<Functionality> getAllFunctionalities(AbstractDomain domain) {
		// Check if the current argument is a real domain.
		AbstractDomain abstractDomain = abstractDomainRepository.findById(domain.getIdentifier());
		if (abstractDomain != null) {
			return convertToFunctionality(this.getAllInnerFunctionalities(abstractDomain));
		}
		return null;
	}

	@Override
	public Set<Functionality> getAllFunctionalities(String domain) {
		AbstractDomain abstractDomain = abstractDomainRepository.findById(domain);
		return getAllFunctionalities(abstractDomain);
	}

	@Override
	public boolean activationPolicyIsMutable(Functionality functionality, String domain) {
		Assert.notNull(functionality);
		Assert.notNull(domain);

		// Check if the current functionality belong to the current domain.
		if (!functionality.getDomain().getIdentifier().equals(domain)) {
			// The current functionality belong to a parent domain.
			AbstractDomain abstractDomain = abstractDomainRepository.findById(domain);
			Functionality ancestorFunc = getParentFunctionality(abstractDomain, functionality.getIdentifier());
			// We check if the parent domain allow the current domain to
			// modify/override activation policy configuration.
			if (ancestorFunc == null || ancestorFunc.getActivationPolicy().isMutable()) {
				return true;
			}
		} else {
			// The current functionality belong to the current domain.
			if (!functionality.getActivationPolicy().isSystem()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean configurationPolicyIsMutable(Functionality functionality, String domain) {
		Assert.notNull(functionality);
		Assert.notNull(domain);
		
		// Check if the current functionality belong to the current domain.
		if (!functionality.getDomain().getIdentifier().equals(domain)) {
			// The current functionality belong to a parent domain.
			AbstractDomain abstractDomain = abstractDomainRepository.findById(domain);
			Functionality ancestorFunc = getParentFunctionality(abstractDomain, functionality.getIdentifier());
			// We check if the parent domain allow the current domain to
			// modify/override activation policy configuration.
			if (ancestorFunc == null) {
				return true;
			}
			if (ancestorFunc.getActivationPolicy().getStatus()) {
				if (ancestorFunc.getConfigurationPolicy().isMutable()) {
					return true;
				}
			}
		} else {
			// I have to check if I have the permission to modify the configuration status of this functionality
			if (functionality.getActivationPolicy().getStatus()) {
				if (!functionality.getConfigurationPolicy().isSystem()) {
					return true;
				}
			}
		}
		return false;
	}

	
	private  Functionality getFunctionalityEntityByIdentifiers(AbstractDomain domain, String functionalityId) {
		Assert.notNull(domain);
		Assert.notNull(functionalityId);
		Functionality fonc = functionalityRepository.findById(domain, functionalityId);
		if (fonc == null && domain.getParentDomain() != null) {
			fonc = getFunctionalityEntityByIdentifiers(domain.getParentDomain(), functionalityId);
		}
		return fonc;
	}
	
	@Override
	public Functionality getFunctionality(String domainId, String functionalityId) {
		Assert.notNull(domainId);
		Assert.notNull(functionalityId);
		
		AbstractDomain domain = abstractDomainRepository.findById(domainId);
		Functionality functionality = getFunctionalityEntityByIdentifiers(domain, functionalityId);
		// Never returns the entity when we try to modify the functionality.
		// The current functionality returned could belong to a parent domain. 
		// In this case, the functionality will be clone, linked to the current domain and the updated by the FonctionalityBusiness update method.
		return (Functionality)functionality.clone();
	}

	@Override
	public void delete(String domainId, String functionalityId) throws IllegalArgumentException, BusinessException {
		Assert.notNull(domainId);
		Assert.notNull(functionalityId);
		
		Functionality f = getFunctionality(domainId, functionalityId);

		// The functionality belong to the current domain. We can delete it.
		if (f.getDomain().getIdentifier().equals(domainId)){
			logger.debug("suppression of the functionality : " + domainId + " : " + functionalityId);
			AbstractDomain domain = abstractDomainRepository.findById(domainId);
			Functionality rawFunc = functionalityRepository.findById(domain, functionalityId);
			functionalityRepository.delete(rawFunc);
			domain.getFunctionalities().remove(rawFunc);
			abstractDomainRepository.update(domain);
		} else {
			logger.warn("You are try to delete the functionality "  + domainId + " : " + functionalityId + " which does not belong to the current domain : " + f.getDomain().getIdentifier());
		}	
	}
}
