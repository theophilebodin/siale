package nc.mairie.siale.technique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.mairie.siale.domain.ControleurSIALE;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

public class CurrentUser implements UserDetailsContextMapper,	Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1352657093536096180L;

	private static GrantedAuthority ROLE_USER = new GrantedAuthority() {
		private static final long serialVersionUID = 4356967487267942910L;
		@Override
		public String getAuthority() {
			return "ROLE_USER";
		}
	};
	
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx,
			String username, Collection<? extends GrantedAuthority> authority) {

		List<GrantedAuthority> mappedAuthorities = new ArrayList<GrantedAuthority>();

		// for (GrantedAuthority granted : authority) {
		//
		// if (granted.getAuthority().equalsIgnoreCase("MY USER GROUP")) {
		// mappedAuthorities.add(new GrantedAuthority(){
		// private static final long serialVersionUID = 4356967414267942910L;
		//
		// @Override
		// public String getAuthority() {
		// return "ROLE_USER";
		// }
		//
		// });
		// } else if(granted.getAuthority().equalsIgnoreCase("MY ADMIN GROUP"))
		// {
		// mappedAuthorities.add(new GrantedAuthority() {
		// private static final long serialVersionUID = -5167156646226168080L;
		//
		// @Override
		// public String getAuthority() {
		// return "ROLE_ADMIN";
		// }
		// });
		// }
		// }

		// Si ADMINWAS
		if (username.toUpperCase().equals("ADMINWAS")) {
			
			//On rajoute le ROLE_USER
			mappedAuthorities.add(ROLE_USER);
			
		} else {
			// Recherche du controleur siale
			List<ControleurSIALE> list = ControleurSIALE.findControleurSIALEsByUsernameLikeAndActifNot(username, false).getResultList();
			// username unique dans l'ad, donc on prend le 1er élément (le seul donc...)
			if (list.size() != 0) {
				//On rajoute le ROLE_USER
				mappedAuthorities.add(ROLE_USER);
			}
		}

		return new User(username, "", true, true, true, true, mappedAuthorities);
	}

	@Override
	public void mapUserToContext(UserDetails arg0, DirContextAdapter arg1) {
	}
	
	
	
	public static ControleurSIALE getCurrentUser() {
		
		if ((ControleurSIALE)Sessions.getCurrent().getAttribute("currentUser") == null ) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			String username;
			if (principal instanceof UserDetails) {
			  username = ((UserDetails)principal).getUsername();
//			  System.out.print(username+"/"+((UserDetails)principal).getPassword()+"/");
//			  for (GrantedAuthority grantedAuthority : ((UserDetails)principal).getAuthorities()) {
//				System.out.print(grantedAuthority.getAuthority()+"-");
//			  }
//			  System.out.println("");
			} else {
			  username = principal.toString();
			}
			

			ControleurSIALE controleurSIALE;
			
			//Si ADMINWAS
			if (username.toUpperCase().equals("ADMINWAS")) {
				controleurSIALE = new ControleurSIALE();
				controleurSIALE.setId(new Long(99999999));
				controleurSIALE.getDroits().add(Constantes.droitAdmin);
				controleurSIALE.getDroits().add(Constantes.droitControleur);
				controleurSIALE.setUsername(username);
				controleurSIALE.setDisplayname(username);
			} else {
			
				//Recherche du controleur siale
				List <ControleurSIALE> list = ControleurSIALE.findControleurSIALEsByUsernameLikeAndActifNot(username, false).getResultList();
				//username unique dans l'ad, donc on prend le 1er élément (le seul donc...)
				if (list.size() == 0) {
					Messagebox.show("Utilisateur non habilité. Demander à l'administrateur SIALE de vous rajouter.","Erreur",Messagebox.OK,Messagebox.ERROR);
					return null;
				}
				
				controleurSIALE = list.get(0);
			}
			
			setCurrentUser(controleurSIALE);
			
		}
				
    	return (ControleurSIALE)Sessions.getCurrent().getAttribute("currentUser");
    }
    
    public static void setCurrentUser(ControleurSIALE aControleur) {
    	Sessions.getCurrent().setAttribute("currentUser", aControleur);
    }

}
