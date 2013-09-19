package nc.mairie.siale.technique;

import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Messagebox;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.security.ILogonTokenMgr;

public class RapportBO {

	
	final public static String regexDossierBO =  "(\\[[^\\[]*\\]){1}(\\p{Blank}*,\\p{Blank}*(\\[[^\\[]*\\]))*";

	/**
	 * Release le tokenBO après avoir récupéré l'enterprise session (créé par getTokenBO) dans la session;
	 */
	public static void releaseTokenBO() {
		IEnterpriseSession enterpriseSession =	(IEnterpriseSession) Executions.getCurrent().getSession().getAttribute("enterpriseSession");
		if (enterpriseSession != null) {
			
			try {
				enterpriseSession.logoff();
			} catch (Exception e) {
				//rien à faire
			} finally {
				Executions.getCurrent().getSession().removeAttribute("enterpriseSession");
			}
		
		}
	}
	
	/**
	 * Se connecte à BO et récupère le tokenBO
	 * @return le tokenBO
	 * @throws SDKException 
	 */
	private static String getTokenBO() throws SDKException {

		Session session = Executions.getCurrent().getSession();
		String tokenBO = null;
	
		ISessionMgr sm = CrystalEnterprise.getSessionMgr();
		String user = CurrentUser.getCurrentUser().getUsername();
		String password = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		IEnterpriseSession enterpriseSession = sm.logon(user, password, "svpsacube", "secLDAP");
		
		// Store the logon token for later use.
		ILogonTokenMgr iLogonTokenMgr= enterpriseSession.getLogonTokenMgr();
		tokenBO = iLogonTokenMgr.getDefaultToken();
		//tokenBO = iLogonTokenMgr.createLogonToken("", 1, 1); 
		
		session.setAttribute("enterpriseSession", enterpriseSession);
		//enterpriseSession.logoff();
				
		
        return tokenBO;
		
	}	
	
	
	/**
	 * 
	 * @return Url du rapport BO pour la Iframe
	 */
	public static String getURLRapportBO(String dossierBO, String rapport) throws WrongValueException {
		
		String urlOpenDocument = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_OPENDOCUMENT");
		if (urlOpenDocument == null) {
			throw new WrongValueException("Le paramètre BO_OPENDOCUMENT n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		
		//Recup du dossier
		if (dossierBO == null) {
			throw new WrongValueException ("Le paramètre BO_FOLDER n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		//format du dossier [dossier] ou [dossier],[sous doss1],[sous dossier2]
		if (!dossierBO.matches(regexDossierBO)) {
			throw new WrongValueException ("Le paramètre BO_FOLDER doit être de la forme [dossier] ou [dossier],[sous doss1],[sous dossier2].");
		}
		
		//Recup du rapport
		if (rapport == null) {
			throw new WrongValueException ("Le paramètre rapport ne doit pas être null");
		}
		
		String tokenBO;
		try {
			tokenBO = getTokenBO();
		} catch (SDKException e) {
			Messagebox.show(e.getMessage(),"Erreur Business Object",Messagebox.OK,Messagebox.ERROR);
			return null;
		}
		
		return Executions.encodeURL(urlOpenDocument+"?sPath="+dossierBO+"&sDocName="+rapport+(tokenBO == null ? "" : "&token="+tokenBO));
	}
	
	/**
	 * 
	 * @return Url du rapport BO pour la Iframe en récupérant le dossier dans le contexte
	 */
	public static String getURLRapportBO(String rapport) throws WrongValueException {
		
		//Recup du dossier
		String dossierBO = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_FOLDER");
		if (dossierBO == null) {
			throw new WrongValueException ("Le paramètre BO_FOLDER n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}

		//format du dossier [dossier] ou [dossier],[sous doss1],[sous dossier2]
		if (!dossierBO.matches(regexDossierBO)) {
			throw new WrongValueException ("Le paramètre BO_FOLDER est mal défini dans le context.xml. Il doit être de la forme [dossier] ou [dossier],[sous doss1],[sous dossier2]. Contacter l'administrateur.");
		}
	
		
		return getURLRapportBO(dossierBO, rapport);
	}
	
}
