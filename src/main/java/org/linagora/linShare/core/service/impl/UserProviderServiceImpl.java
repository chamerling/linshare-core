package org.linagora.linShare.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.linagora.linShare.core.domain.entities.DomainPattern;
import org.linagora.linShare.core.domain.entities.LDAPConnection;
import org.linagora.linShare.core.domain.entities.LdapUserProvider;
import org.linagora.linShare.core.domain.entities.User;
import org.linagora.linShare.core.exception.BusinessException;
import org.linagora.linShare.core.repository.DomainPatternRepository;
import org.linagora.linShare.core.repository.LDAPConnectionRepository;
import org.linagora.linShare.core.repository.UserProviderRepository;
import org.linagora.linShare.core.service.LDAPQueryService;
import org.linagora.linShare.core.service.UserProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserProviderServiceImpl implements UserProviderService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserProviderServiceImpl.class);
	
	private final LDAPConnectionRepository ldapConnectionRepository;
	private final DomainPatternRepository domainPatternRepository;
	private final LDAPQueryService ldapQueryService;
	private final UserProviderRepository userProviderRepository;
	

	public UserProviderServiceImpl(LDAPConnectionRepository ldapConnectionRepository,
			DomainPatternRepository domainPatternRepository,
			LDAPQueryService ldapQueryService,
			UserProviderRepository userProviderRepository) {
		this.ldapConnectionRepository = ldapConnectionRepository;
		this.domainPatternRepository = domainPatternRepository;
		this.ldapQueryService = ldapQueryService;
		this.userProviderRepository = userProviderRepository;
	}

	@Override
	public DomainPattern createDomainPattern(DomainPattern domainPattern) throws BusinessException {
		DomainPattern createdDomain = domainPatternRepository.create(domainPattern);
		return createdDomain;
	}

	@Override
	public LDAPConnection createLDAPConnection(LDAPConnection ldapConnection) throws BusinessException {
		LDAPConnection createdLDAPConnection = ldapConnectionRepository.create(ldapConnection);
		return createdLDAPConnection;
	}
	
	@Override
	public LDAPConnection retrieveLDAPConnection(String identifier) throws BusinessException {
		return ldapConnectionRepository.findById(identifier);
	}
	
	@Override
	public DomainPattern retrieveDomainPattern(String identifier) throws BusinessException {
		return domainPatternRepository.findById(identifier);
	}
	
	@Override
	public void deleteConnection(String connectionToDelete) throws BusinessException {
		if (!connectionIsDeletable(connectionToDelete)) {
			throw new BusinessException("Cannot delete connection because still used by domains");
		}
		
		LDAPConnection conn = retrieveLDAPConnection(connectionToDelete);
		if(conn == null) {
			logger.error("Ldap connexion not found: " + connectionToDelete);
		} else {
			logger.debug("delete ldap connexion : " + connectionToDelete);
			ldapConnectionRepository.delete(conn);
		}
	}
	
	@Override
	public boolean connectionIsDeletable(String connectionToDelete) {
		List<LdapUserProvider> list = userProviderRepository.findAll();
		boolean used = false;
		for (LdapUserProvider ldapUserProvider : list) {
			if (ldapUserProvider.getLdapconnexion().getIdentifier().equals(connectionToDelete)) {
				used = true;
				break;
			}
		}
		return (!used);
	}
	
	@Override
	public void deletePattern(String patternToDelete) throws BusinessException {
		
		if (!patternIsDeletable(patternToDelete)) {
			throw new BusinessException("Cannot delete pattern because still used by domains");
		}
		
		DomainPattern pattern = retrieveDomainPattern(patternToDelete);
		domainPatternRepository.delete(pattern);
	}
	
	@Override
	public boolean patternIsDeletable(String patternToDelete) {
		List<LdapUserProvider> list = userProviderRepository.findAll();
		boolean used = false;
		for (LdapUserProvider ldapUserProvider : list) {
			if (ldapUserProvider.getPattern().getIdentifier().equals(patternToDelete)) {
				used = true;
				break;
			}
		}
		return (!used);
	}
	
	
	@Override
	public List<DomainPattern> findAllDomainPatterns() throws BusinessException {
		return domainPatternRepository.findAll();
	}
	
	@Override
	public List<LDAPConnection> findAllLDAPConnections() throws BusinessException {
		return ldapConnectionRepository.findAll();
	}
	
	@Override
	public void updateLDAPConnection(LDAPConnection ldapConnection) throws BusinessException {
		LDAPConnection ldapConn = ldapConnectionRepository.findById(ldapConnection.getIdentifier());
		ldapConn.setProviderUrl(ldapConnection.getProviderUrl());
		ldapConn.setSecurityAuth(ldapConnection.getSecurityAuth());
		ldapConn.setSecurityCredentials(ldapConnection.getSecurityCredentials());
		ldapConn.setSecurityPrincipal(ldapConnection.getSecurityPrincipal());
		ldapConnectionRepository.update(ldapConn);
	}
	
	@Override
	public void updateDomainPattern(DomainPattern domainPattern) throws BusinessException {
		DomainPattern pattern = domainPatternRepository.findById(domainPattern.getIdentifier());
		pattern.setDescription(domainPattern.getDescription());
		pattern.setGetUserCommand(domainPattern.getGetUserCommand());
		pattern.setGetAllDomainUsersCommand(domainPattern.getGetAllDomainUsersCommand());
		pattern.setAuthCommand(domainPattern.getAuthCommand());
		pattern.setSearchUserCommand(domainPattern.getSearchUserCommand());
		pattern.setUserFirstname(domainPattern.getUserFirstName());
		pattern.setUserLastName(domainPattern.getUserLastName());
		pattern.setUserMail(domainPattern.getUserMail());
		domainPatternRepository.update(pattern);
	}

	@Override
	public void create(LdapUserProvider userProvider) throws BusinessException {
		userProviderRepository.create(userProvider);
	}
	
	@Override
	public void delete(LdapUserProvider userProvider) throws BusinessException {
		userProviderRepository.delete(userProvider);
	}
	
	@Override
	public void update(LdapUserProvider userProvider) throws BusinessException {
		userProviderRepository.update(userProvider);
	}

	@Override
	public List<User> searchUser(LdapUserProvider userProvider, String mail) throws BusinessException, NamingException, IOException {
		return searchUser(userProvider, mail, "", "");
	}

	@Override
	public List<User> searchUser(LdapUserProvider userProvider, String mail, String firstName, String lastName) throws BusinessException, NamingException, IOException {
		return ldapQueryService.searchUser(userProvider.getLdapconnexion(), userProvider.getBaseDn(), userProvider.getPattern(), mail, firstName, lastName);
	}

	@Override
	public User getUser(LdapUserProvider userProvider, String mail) throws BusinessException, NamingException, IOException {
		LdapUserProvider p = (LdapUserProvider)userProvider;
		return ldapQueryService.getUser(p.getLdapconnexion(), p.getBaseDn(), p.getPattern(), mail);
	}

	@Override
	public User auth(LdapUserProvider userProvider, String mail, String userPasswd)	throws BusinessException, NamingException, IOException {
		LdapUserProvider p = (LdapUserProvider)userProvider;
		if(p == null) {
			return null;
		}
		return ldapQueryService.auth(p.getLdapconnexion(), p.getBaseDn(), p.getPattern(), mail,userPasswd);
	}

	@Override
	public List<String> findAllDomainPatternIdentifiers() {
		List<String> list = new ArrayList<String>();
		for (DomainPattern d : domainPatternRepository.findAll()) {
			list.add(d.getIdentifier());
		}
		return list;
	}

	@Override
	public List<String> findAllLDAPConnectionIdentifiers() {
		List<String> list = new ArrayList<String>();
		for (LDAPConnection c : ldapConnectionRepository.findAll()) {
			list.add(c.getIdentifier());
		}
		return list;
	}
	
}

