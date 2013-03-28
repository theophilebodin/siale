package nc.mairie.siale.technique;

import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Droit;

import org.zkoss.zk.ui.Sessions;

public class CurrentUser {

	final static Droit droitAdmin=Droit.findDroit(new Long(1));
		
	public static ControleurSIALE getCurrentUser() {
    	return (ControleurSIALE)Sessions.getCurrent().getAttribute("currentUser");
    }
    
    public static void setCurrentUser(ControleurSIALE aControleur) {
    	
    	Sessions.getCurrent().setAttribute("currentUser", aControleur);
    }

    public static boolean isAdmin() {
    	for (Droit droit : getCurrentUser().getDroits()) {
			if (droit.getId().equals(droitAdmin.getId())) return true;
		}
    	return false;
    }
	
}
