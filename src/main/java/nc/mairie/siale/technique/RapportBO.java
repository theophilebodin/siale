package nc.mairie.siale.technique;

import java.util.ArrayList;

import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Messagebox;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.occa.security.ILogonTokenMgr;
import com.crystaldecisions.sdk.plugin.CeKind;
import com.crystaldecisions.sdk.plugin.desktop.folder.IFolder;

public class RapportBO {

	
	final public static String regexDossierBO =  "(\\[[^\\[]*\\]){1}(\\p{Blank}*,\\p{Blank}*(\\[[^\\[]*\\]))*";

	
	public static IEnterpriseSession getEnterpriseSession() {
		IEnterpriseSession enterpriseSession =	(IEnterpriseSession) Executions.getCurrent().getSession().getAttribute("enterpriseSession");
		if (enterpriseSession == null) {
		
			ISessionMgr sm;
			try {
				sm = CrystalEnterprise.getSessionMgr();
			} catch (SDKException e) {
				throw new WrongValueException(e.getMessage());
			}
			String user = CurrentUser.getCurrentUser().getUsername();
			String password = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
			String serveur = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_SERVEUR");
			String sec = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_SEC");
			
			try {
				enterpriseSession = sm.logon(user, password, serveur, sec);
			} catch (Exception ex) {
				throw new WrongValueException(ex.getMessage());
			} 
		}
		return enterpriseSession;
	}
	
	public static class ObjectBO {

		String id;
			String name;
		String parentId;
		public ObjectBO(String id, String name, String parentId) {
			super();
			this.id = id;
			this.name = name;
			this.parentId = parentId;
			
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getParentId() {
			return parentId;
		}
		
	}
	
	
public static ArrayList<ObjectBO> listeDocumentsWebIduDossier (String idDossier) {
		
		ArrayList<ObjectBO> res = new ArrayList<>();
		
		IEnterpriseSession enterpriseSession =	getEnterpriseSession();
		
		IInfoStore iInf  = null;
		try {
			iInf  = (IInfoStore) enterpriseSession.getService("InfoStore");
			
		
			//liste des documents
			String sQuery = "SELECT SI_ID, SI_NAME, SI_PARENTID "
					+ "FROM CI_INFOOBJECTS WHERE ( SI_PARENTID = "
					+ idDossier + ") AND SI_KIND =\'"
					+ CeKind.WEBI
					+ "\' ORDER BY SI_NAME ASC";
			IInfoObjects webiDocuments = (IInfoObjects) iInf.query(sQuery);
			int	wSize = webiDocuments.size();
			
			for	(int j=0; j<wSize; j++) {
				IInfoObject iObj = (IInfoObject)webiDocuments.get(j);
				res.add( new ObjectBO(String.valueOf(iObj.getID()), iObj.getTitle(), String.valueOf(iObj.getParentID())));
			}
		} catch (SDKException e) {
			throw new WrongValueException(e.getMessage());
		} finally {
			enterpriseSession.logoff();
		}
		
		return res;
		
	}
	
	public static ArrayList<ObjectBO> listeDocumentsWebI () {
		
		String idDossier = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_FOLDER_ID");
		if (idDossier == null) {
			throw new WrongValueException("Le paramètre BO_SERVEUR_ID n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		
		return listeDocumentsWebIduDossier(idDossier);
		
	}
	
	public static ObjectBO recupereObjectBODossier(String idDossier) {
		ObjectBO res = null;
		
		IEnterpriseSession enterpriseSession =	getEnterpriseSession();
		
		IInfoStore iInf  = null;
		try {
			iInf  = (IInfoStore) enterpriseSession.getService("InfoStore");
	
			String sQuery = "SELECT SI_ID, SI_NAME, SI_PARENTID FROM "
					+ "CI_INFOOBJECTS WHERE ( SI_ID = "
					+ idDossier + ") AND SI_KIND=\'"
					+ CeKind.FOLDER
					+ "\' ORDER BY SI_NAME ASC ";	
					
					
			IInfoObjects subFolders = (IInfoObjects) iInf.query(sQuery);
			if (subFolders.size() > 0) {
				IFolder	iFld = (IFolder)subFolders.get(0);
				res= new ObjectBO(String.valueOf(iFld.getID()), iFld.getTitle(),String.valueOf(iFld.getParentID()));
			}
			

		} catch (SDKException e) {
			throw new WrongValueException(e.getMessage());
		} finally {
			enterpriseSession.logoff();
		}
		
		return res;
		
	}
	
	public static ObjectBO recupereObjectBODossier() {
		
		String idDossier = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_FOLDER_ID");
		if (idDossier == null) {
			throw new WrongValueException("Le paramètre BO_SERVEUR_ID n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		
		return recupereObjectBODossier(idDossier);
		
	}
	
	public static ArrayList<ObjectBO> listeFolderBO (String idDossier) {
		
		ArrayList<ObjectBO> res = new ArrayList<>();
		
		IEnterpriseSession enterpriseSession =	getEnterpriseSession();
		
		IInfoStore iInf  = null;
		try {
			iInf  = (IInfoStore) enterpriseSession.getService("InfoStore");
	
			String sQuery = "SELECT SI_ID, SI_NAME, SI_PARENTID FROM "
					//+ "CI_INFOOBJECTS WHERE ( SI_ID = "
					+ "CI_INFOOBJECTS WHERE ( SI_PARENTID = "
					+ idDossier + ") AND SI_KIND=\'"
					+ CeKind.FOLDER
					+ "\' ORDER BY SI_NAME ASC ";	
					
					
			IInfoObjects subFolders = (IInfoObjects) iInf.query(sQuery);
			int iSize = subFolders.size();
			
			for	(int i=0; i<iSize; i++) {
				IFolder	iFld = (IFolder)subFolders.get(i);
				res.add(new ObjectBO(String.valueOf(iFld.getID()), iFld.getTitle(),String.valueOf(iFld.getParentID())));
			}

		} catch (SDKException e) {
			throw new WrongValueException(e.getMessage());
		} finally {
			enterpriseSession.logoff();
		}
		
		return res;
		
	}
	
	
	public static ArrayList<ObjectBO> listeFolderBO () {
		
		String idDossier = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_FOLDER_ID");
		if (idDossier == null) {
			throw new WrongValueException("Le paramètre BO_SERVEUR_ID n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		
		return listeFolderBO(idDossier);
		
	}
	
	public static void testeBO () {
		
		IEnterpriseSession enterpriseSession =	getEnterpriseSession();
		
		IInfoStore iInf  = null;
		try {
			iInf  = (IInfoStore) enterpriseSession.getService("InfoStore");
			
			//ID, CUID : 240965, ASGJr13z7A5Kv7BKtUDXBUU
			
			String sQuery = null;
			
			//test retrouve root
			sQuery = "SELECT SI_ID, SI_NAME, SI_PARENTID FROM "
					//+ "CI_INFOOBJECTS WHERE ( SI_ID = "
					//+ "CI_INFOOBJECTS WHERE ( SI_PARENTID = "
					+ "CI_INFOOBJECTS WHERE ( SI_NAME = 'Dossier racine' "
					+") AND SI_KIND=\'"
					+ CeKind.FOLDER
					+ "\' ORDER BY SI_NAME ASC ";	
			
			IInfoObjects subFolders0 = (IInfoObjects) iInf.query(sQuery);
			int iSize0 = subFolders0.size();
			
			System.out.println("dossier racine");
			for	(int i=0; i<iSize0; i++) {
				IFolder	iFld = (IFolder)subFolders0.get(i);
				
				System.out.println("sId="+iFld.getID()+" si_name="+iFld.getTitle());
			}
			
			
			String iID = "240965";
			//Liste des dossiers
			sQuery = "SELECT SI_ID, SI_NAME, SI_PARENTID FROM "
			//+ "CI_INFOOBJECTS WHERE ( SI_ID = "
			+ "CI_INFOOBJECTS WHERE ( SI_PARENTID = "
			+ iID + ") AND SI_KIND=\'"
			+ CeKind.FOLDER
			+ "\' ORDER BY SI_NAME ASC ";	
			
			
			IInfoObjects subFolders = (IInfoObjects) iInf.query(sQuery);
			int iSize = subFolders.size();
			
			System.out.println("sous dossier");
			
			for	(int i=0; i<iSize; i++) {
				IFolder	iFld = (IFolder)subFolders.get(i);
				
				System.out.println("sId="+iFld.getID()+" si_name="+iFld.getTitle());
			}
			
			//liste des documents
			sQuery = "SELECT SI_ID, SI_NAME, SI_PARENTID "
					+ "FROM CI_INFOOBJECTS WHERE ( SI_PARENTID = "
					+ iID + ") AND SI_KIND =\'"
					+ CeKind.WEBI
					+ "\' ORDER BY SI_NAME ASC";
			IInfoObjects webiDocuments = (IInfoObjects) iInf.query(sQuery);
			int	wSize = webiDocuments.size();
			
			System.out.println("doc webi");
			
			for	(int j=0; j<wSize; j++) {
				IInfoObject iObj = (IInfoObject)webiDocuments.get(j);
				System.out.println("sId="+iObj.getID()+" si_name="+iObj.getTitle());
			}
			
		} catch (SDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			enterpriseSession.logoff();
		}
		
		
	}
	
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
		String serveur = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_SERVEUR");
		String sec = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_SEC");
		
		IEnterpriseSession enterpriseSession = null;
		ILogonTokenMgr logonTokenMgr=null;
		try {
			enterpriseSession = sm.logon(user, password, serveur, sec);
			logonTokenMgr = enterpriseSession.getLogonTokenMgr();

			tokenBO = logonTokenMgr.createWCAToken("", 1, 1);
			session.setAttribute("enterpriseSession", enterpriseSession);
		} catch (SDKException ex) {
			Messagebox.show(ex.getMessage(),"Erreur Business Object",Messagebox.OK,Messagebox.ERROR);
			try {
				enterpriseSession.logoff();
			} catch (Exception e) {
				// tant pis...
			}
		} finally {
			////Ne pas faire logoff sinon le rapport ne s"'affiche pas
			//enterpriseSession.logoff();
		}
		
        return tokenBO;
		
	}	
	
	
	
	private static String construitURL_OPEN_DOCUMENT () {
		String serveur = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_SERVEUR");
		if (serveur == null) {
			throw new WrongValueException("Le paramètre BO_SERVEUR n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		
		String port = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_PORT");
		if (port == null) {
			throw new WrongValueException("Le paramètre BO_PORT n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		
		String protocol = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_PROTOCOL");
		if (protocol == null) {
			throw new WrongValueException("Le paramètre BO_PROTOCOL n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		
		String urlOpenDocument= Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_OPENDOCUMENT");
		if (urlOpenDocument == null) {
			throw new WrongValueException("Le paramètre BO_OPENDOCUMENT n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		
		String url = protocol+"://"+serveur+(port == null ? "" : ":"+port)+urlOpenDocument;
		
		return url;
	}
	
	public static String URL_OPEN_DOCUMENT = construitURL_OPEN_DOCUMENT();
	
	/**
	 * 
	 * @return Url du rapport BO pour la Iframe
	 */
//	public static String getURLRapportBO(String dossierBO, String rapport) throws WrongValueException {
//		
//		
//		String url = URL_OPEN_DOCUMENT;
//		
//		//Recup du dossier
//		if (dossierBO == null) {
//			throw new WrongValueException ("Le paramètre BO_FOLDER n'est pas défini dans le context.xml. Contacter l'administrateur.");
//		}
//		//format du dossier [dossier] ou [dossier],[sous doss1],[sous dossier2]
//		if (!dossierBO.matches(regexDossierBO)) {
//			throw new WrongValueException ("Le paramètre BO_FOLDER doit être de la forme [dossier] ou [dossier],[sous doss1],[sous dossier2].");
//		}
//		
//		//Recup du rapport
//		if (rapport == null) {
//			throw new WrongValueException ("Le paramètre rapport ne doit pas être null");
//		}
//		
//		String tokenBO;
//		try {
//			tokenBO = getTokenBO();
//		} catch (SDKException e) {
//			Messagebox.show(e.getMessage(),"Erreur Business Object",Messagebox.OK,Messagebox.ERROR);
//			return null;
//		}
//		
//		return Executions.encodeURL(url+"?sPath="+dossierBO+"&sDocName="+rapport+(tokenBO == null ? "" : "&token="+tokenBO));
//	}
	
//	/**
//	 * 
//	 * @return Url du rapport BO pour la Iframe en récupérant le dossier dans le contexte
//	 */
//public static String getURLRapportBO(String rapport) throws WrongValueException {
//		
//		//Recup du dossier
//		String dossierBO = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_FOLDER");
//		if (dossierBO == null) {
//			throw new WrongValueException ("Le paramètre BO_FOLDER n'est pas défini dans le context.xml. Contacter l'administrateur.");
//		}
//
//		//format du dossier [dossier] ou [dossier],[sous doss1],[sous dossier2]
//		if (!dossierBO.matches(regexDossierBO)) {
//			throw new WrongValueException ("Le paramètre BO_FOLDER est mal défini dans le context.xml. Il doit être de la forme [dossier] ou [dossier],[sous doss1],[sous dossier2]. Contacter l'administrateur.");
//		}
//	
//		
//		return getURLRapportBO(dossierBO, rapport);
//}
	
	
	/**
	 * 
	 * @return Url du rapport BO pour la Iframe en récupérant le dossier dans le contexte
	 */
	public static String getURLRapportBO(String iDocID) {
		
		String url = URL_OPEN_DOCUMENT;
		
		String tokenBO;
		try {
			tokenBO = getTokenBO();
		} catch (SDKException e) {
			throw new WrongValueException(e.getMessage());
		}
		
		return Executions.encodeURL(url+"?iDocID="+iDocID+(tokenBO == null ? "" : "&token="+tokenBO));
		
		
	}
}
