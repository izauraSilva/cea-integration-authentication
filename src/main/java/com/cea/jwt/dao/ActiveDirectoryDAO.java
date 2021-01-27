package com.cea.jwt.dao;

import com.cea.jwt.dto.JwtRequest;
import com.cea.jwt.exception.UnauthorizedAuthenticationException;
import com.cea.jwt.exception.UserNotFoundException;
import com.cea.jwt.model.User;
import com.cea.jwt.repository.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Configuration
@PropertySource("classpath:/ldap.properties")
@Component
public class ActiveDirectoryDAO {

	private static final Logger logger = LogManager.getLogger(ActiveDirectoryDAO.class);

	@Value("${ldap.url}")
	private String ldapUrl;

	@Value("${ldap.base}")
	private String ldapBase;

	@Value("${ldap.userDn}")
	private String ldapUserDn;

	@Value("${ldap.password}")
	private String ldapPassword;

	@Value("${ldap.amb}")
	private String ldapAmb;

	@Autowired
	private UserRepo userRepo;

	public ActiveDirectoryDAO() {
	}

	public String getMembersOf(JwtRequest jwtRequest, HttpServletRequest request ) {

		LdapContextSource sourceLdapCtx = new LdapContextSource();

		sourceLdapCtx.setUrl(ldapUrl);
		sourceLdapCtx.setUserDn(ldapUserDn);
		sourceLdapCtx.setBase(ldapBase);
		sourceLdapCtx.setPassword(ldapPassword);
		sourceLdapCtx.setDirObjectFactory(DefaultDirObjectFactory.class);
		sourceLdapCtx.afterPropertiesSet();

		LdapTemplate sourceLdapTemplate = new LdapTemplate(sourceLdapCtx);

		Filter filter = new EqualsFilter("sAMAccountName", jwtRequest.getUsername());

		if(!sourceLdapTemplate.authenticate("", filter.encode() , jwtRequest.getPassword())){
			this.setRedis(jwtRequest.getUsername(), null, request, null, "Unauthorized authentication",null);
			throw new UnauthorizedAuthenticationException("Erro na autenticação do usuário: " + jwtRequest.getUsername());
		}

		return this.getMembersOf(jwtRequest.getUsername(), sourceLdapTemplate, request);
	}

	private String getMembersOf(String user, LdapTemplate sourceLdapTemplate, HttpServletRequest request) {

		boolean isAllowedGroup = false;

		String nameUser = this.getPerson(user, sourceLdapTemplate, request);

		ArrayList<?> membersOf = sourceLdapTemplate.search(
				query().where("userPrincipalName").is(user.concat(ldapAmb)),
				(AttributesMapper<ArrayList<?>>) attrs -> Collections.list(attrs.get("memberOf").getAll())
		).get(0);

		for (Object group : membersOf) {
			if(group.toString().contains("API_RH")){
				if(group.toString().indexOf("CN=API_RH,") == 0) isAllowedGroup = true;
			}
		}

		if(isAllowedGroup){
			return  membersOf.toString().concat(";").concat(nameUser);
		}

		this.setRedis(user, null, request, null, "Unauthorized authentication",null);
		throw new UserNotFoundException("Usuário não autenticado: " + user);
	}

	private String getPerson(String user, LdapTemplate sourceLdapTemplate, HttpServletRequest request) {

		AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter("userPrincipalName", user.concat(ldapAmb)));

		List<String> cn = sourceLdapTemplate.
				search("", andFilter.toString(), (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());

		if(cn.size()>0) return cn.get(0);

		this.setRedis(user, null, request, null, "Unauthorized authentication",null);
		throw new UserNotFoundException("Usuário não autenticado: " + user);
	}

	public void setRedis(String username, String perfil , HttpServletRequest request, String token, String observation, String membersOf) {

		User user = new User(username,
				perfil,
				request.getRemoteAddr(),
				token,
				observation,
				membersOf,
				new Date());

		userRepo.save(user);
	}

}
