package nc.mairie.siale.technique;

import java.util.List;

import nc.mairie.siale.domain.ControleurSIALE;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

public class CurrentUser {

	public static ControleurSIALE getCurrentUser() {
		
		if ((ControleurSIALE)Sessions.getCurrent().getAttribute("currentUser") == null ) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			String username;
			if (principal instanceof UserDetails) {
			  username = ((UserDetails)principal).getUsername();
			  System.out.print(username+"/"+((UserDetails)principal).getPassword()+"/");
			  for (GrantedAuthority grantedAuthority : ((UserDetails)principal).getAuthorities()) {
				System.out.print(grantedAuthority.getAuthority()+"-");
			  }
			  System.out.println("");
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
				List <ControleurSIALE> list = ControleurSIALE.findControleurSIALEsByUsernameEquals(username).getResultList();
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
