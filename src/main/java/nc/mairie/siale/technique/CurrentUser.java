package nc.mairie.siale.technique;

import nc.mairie.siale.domain.ControleurSIALE;

import org.zkoss.zk.ui.Sessions;

public class CurrentUser {

	public static ControleurSIALE getCurrentUser() {
    	return (ControleurSIALE)Sessions.getCurrent().getAttribute("currentUser");
    }
    
    public static void setCurrentUser(ControleurSIALE aControleur) {
    	
    	Sessions.getCurrent().setAttribute("currentUser", aControleur);
    }

}
