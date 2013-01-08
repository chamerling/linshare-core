package org.linagora.linshare.core.service;

import java.util.List;

import org.linagora.linshare.core.domain.entities.AbstractDomain;
import org.linagora.linshare.core.domain.entities.Functionality;
import org.linagora.linshare.core.domain.entities.IntegerValueFunctionality;
import org.linagora.linshare.core.domain.entities.StringValueFunctionality;
import org.linagora.linshare.core.domain.objects.SizeUnitValueFunctionality;
import org.linagora.linshare.core.domain.objects.TimeUnitBooleanValueFunctionality;
import org.linagora.linshare.core.domain.objects.TimeUnitValueFunctionality;
import org.linagora.linshare.core.exception.BusinessException;

public interface FunctionalityService {

	/**
	 * This method returns a functionality. 
	 * @param persistence id
	 * @return
	 */
	public Functionality findById(long id);
	
	public void update(AbstractDomain domain, Functionality functionality) throws BusinessException;
	public void update(String domainIdentifier, Functionality functionality) throws BusinessException;
	
	/**
	 * This method is designed to return a list of all existing functionalities from a domain.
	 * @param domain entity
	 * @return
	 */
	public List<Functionality> getAllFunctionalities(AbstractDomain domain);
	
	/**
	 * This method is designed to return a list of all existing functionalities from a domain.
	 * @param domain identifier
	 * @return
	 */
	public List<Functionality> getAllFunctionalities(String domainIdentifier);
	
	/**
	 * This method returns an updatable functionality. Do not try to update a functionality whit other ways. 
	 * @param domain identifier
	 * @param functionality identifier
	 * @return
	 */
	public Functionality getFunctionalityByIdentifiers(String domainIdentifier,String functionalityIdentifier);
	
	/**
	 * This method is designed to return a list of all functionalities.
	 * Only the activation policy of these functionalities can be modified. 
	 * @param domain entity
	 * @return
	 */
	
	public List<Functionality> getAllAvailableFunctionalities(AbstractDomain domain);
	/**
	 * This method is designed to return a list of all functionalities.
	 * Only the activation policy of these functionalities can be modified.  
	 * @param domain identifier
	 * @return
	 */
	public List<Functionality> getAllAvailableFunctionalities(String domainIdentifier);
			
	/**
	 * This method is designed to return a list of all functionalities.
	 * Only the configuration policy of these functionalities can be modified. 
	 * @param domain entity
	 * @return
	 */
	public List<Functionality> getAllAlterableFunctionalities(AbstractDomain domain);
	
	/**
	 * This method is designed to return a list of all functionalities.
	 * Only the configuration policy of these functionalities can be modified.
	 * @param domain identifier
	 * @return
	 */
	public List<Functionality> getAllAlterableFunctionalities(String domainIdentifier);

	/**
	 * This method is designed to return a list of all functionalities.
	 * Only the parameters of these functionalities can be modified. 
	 * @param domain entity
	 * @return
	 */
	public List<Functionality> getAllEditableFunctionalities(AbstractDomain domain);
	
	/**
	 * This method is designed to return a list of all functionalities.
	 * Only the parameters of these functionalities can be modified. 
	 * @param domain identifier
	 * @return
	 */
	public List<Functionality> getAllEditableFunctionalities(String domainIdentifier);

	
	/** 
	 * Shortcuts to functionalities
	 */

	public SizeUnitValueFunctionality getGlobalQuotaFunctionality (AbstractDomain domain);
    public SizeUnitValueFunctionality getUserQuotaFunctionality (AbstractDomain domain);
    public SizeUnitValueFunctionality getUserMaxFileSizeFunctionality (AbstractDomain domain);
	public TimeUnitBooleanValueFunctionality getDefaultShareExpiryTimeFunctionality (AbstractDomain domain);
	public TimeUnitValueFunctionality getDefaultFileExpiryTimeFunctionality (AbstractDomain domain);
	
	public Functionality getGuestFunctionality (AbstractDomain domain);
	public TimeUnitValueFunctionality getGuestAccountExpiryTimeFunctionality (AbstractDomain domain);
	
	
	public StringValueFunctionality getTimeStampingFunctionality(AbstractDomain domain);
	public Functionality getMimeTypeFunctionality(AbstractDomain domain);
	public Functionality getEnciphermentFunctionality(AbstractDomain domain);
	public Functionality getAntivirusFunctionality(AbstractDomain domain);
	public Functionality getAnonymousUrlFunctionality(AbstractDomain domain);
	public Functionality getSecuredAnonymousUrlFunctionality(AbstractDomain domain);
	public Functionality getSignatureFunctionality(AbstractDomain domain);
	public Functionality getUserCanUploadFunctionality(AbstractDomain domain);
	public StringValueFunctionality getCustomLogoFunctionality(AbstractDomain domain);
	public StringValueFunctionality getShareNotificationBeforeExpirationFunctionality(AbstractDomain domain);
	
	public IntegerValueFunctionality getCompletionFunctionality(AbstractDomain domain);
	public Functionality getUserTabFunctionality(AbstractDomain domain);
	public Functionality getAuditTabFunctionality(AbstractDomain domain);
	public Functionality getThreadTabFunctionality(AbstractDomain domain);
	public Functionality getHelpTabFunctionality(AbstractDomain domain);
	
	
	/**
	 * Check if SecuredAnonymousUrl (SAU) is mandatory
	 * @param domain : the current domain identifier
	 * @return 
	 */
	public boolean isSauMadatory(String domainIdentifier);
	
	/**
	 * Check if SecuredAnonymousUrl (SAU) is allowed
	 * @param domain : the current domain identifier
	 * @return 
	 */
	public boolean isSauAllowed(String domainIdentifier);

	/**
	 * return the default value for SecuredAnonymousUrl (SAU)
	 * @param domain : the current domain identifier
	 * @return
	 */
	public boolean getDefaultSauValue(String domainIdentifier);
	
}