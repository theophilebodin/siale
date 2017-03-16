package nc.mairie.siale.technique;

import java.util.ArrayList;

import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
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
	
	final public static String BO_SERVEUR = litParametre("BO_SERVEUR");
	final public static String BO_SEC = litParametre("BO_SEC");
	final public static String BO_FOLDER_ID = litParametre("BO_FOLDER_ID");
	final public static String BO_PORT = litParametre("BO_PORT");
	final public static String BO_PROTOCOL = litParametre("BO_PROTOCOL");
	final public static String BO_OPENDOCUMENT = litParametre("BO_OPENDOCUMENT");
	final public static String BO_RELEASE_TIMER = litParametre("BO_RELEASE_TIMER");
	
	final public static String URL_OPEN_DOCUMENT = BO_PROTOCOL+"://"+BO_SERVEUR+(BO_PORT == null ? "" : ":"+BO_PORT)+BO_OPENDOCUMENT;

	/**
	 * 
	 * @param param param
	 * @return la valeur du paramètre dans le context.xml
	 */
	public static String litParametre(String param) {
		String result = Executions.getCurrent().getDesktop().getWebApp().getInitParameter(param);
		if (result == null) {
			throw new WrongValueException("Le paramètre "+param+" n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		return result;
	}

	
	public static IEnterpriseSession getEnterpriseSession() {
	
		IEnterpriseSession enterpriseSession;
		ISessionMgr sm;
		try {
			sm = CrystalEnterprise.getSessionMgr();
		} catch (SDKException e) {
			throw new WrongValueException(e.getMessage());
		}
		String user = CurrentUser.getCurrentUser().getUsername();
		String password = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		
		try {
			enterpriseSession = sm.logon(user, password, BO_SERVEUR, BO_SEC);
		} catch (Exception ex) {
			throw new WrongValueException(ex.getMessage());
		} 
		return enterpriseSession;
	}
	
	public static class ObjectBO {

		String cuid;
		String name;
		String displayName;
		String parentId;
		String kind;
		
		public ObjectBO(String cuid, String name, String displayName, String kind, String parentId) {
			super();
			this.cuid = cuid;
			this.name = name;
			this.displayName = displayName;
			this.kind = kind;
			this.parentId = parentId;
			
		}
		
		public String getCUID() {
			return cuid;
		}
		public void setCUID(String cuid) {
			this.cuid = cuid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		public String getParentCUID() {
			return parentId;
		}
		public String getKind() {
			return kind;
		}
		public void setKind(String kind) {
			this.kind = kind;
		}
		
		public String getImage() {
			return "/_images/BO/"+getKind()+".gif"; 
		}
		
		
		
	}
	
	
public static ArrayList<ObjectBO> listeDocumentsWebIduDossier (String idDossier) {
		
		ArrayList<ObjectBO> res = new ArrayList<ObjectBO>();
		
		IEnterpriseSession enterpriseSession =	getEnterpriseSession();
		
		IInfoStore iInf  = null;
		try {
			iInf  = (IInfoStore) enterpriseSession.getService("InfoStore");
			
		
			//liste des documents
			String sQuery = "SELECT SI_CUID, SI_NAME, SI_KIND, SI_PARENT_CUID "
					+ "FROM CI_INFOOBJECTS WHERE ( SI_PARENT_CUID = \'"
					+ idDossier + "\') " 
					+" AND SI_KIND != \'"+ CeKind.FOLDER+"\'"
					+" ORDER BY SI_NAME ASC";
			IInfoObjects webiDocuments = (IInfoObjects) iInf.query(sQuery);
			int	wSize = webiDocuments.size();
			
			for	(int j=0; j<wSize; j++) {
				IInfoObject iObj = (IInfoObject)webiDocuments.get(j);
				res.add( new ObjectBO(String.valueOf(iObj.getCUID()), iObj.getTitle(),iObj.getTitle(), iObj.getKind(), String.valueOf(iObj.getParentCUID())));
			}
		} catch (SDKException e) {
			throw new WrongValueException(e.getMessage());
		} finally {
			enterpriseSession.logoff();
		}
		
		return res;
		
	}
	
	public static ArrayList<ObjectBO> listeDocumentsWebI () {
		
		return listeDocumentsWebIduDossier(BO_FOLDER_ID);
		
	}
	
	public static ObjectBO recupereObjectBODossier(String idDossier) {
		ObjectBO res = null;
		
		IEnterpriseSession enterpriseSession =	getEnterpriseSession();
		
		IInfoStore iInf  = null;
		try {
			iInf  = (IInfoStore) enterpriseSession.getService("InfoStore");
	
			String sQuery = "SELECT SI_CUID, SI_NAME, SI_KIND, SI_PARENT_CUID FROM "
					+ "CI_INFOOBJECTS WHERE ( SI_CUID = \'"
					+ idDossier + "\') AND SI_KIND=\'"
					+ CeKind.FOLDER
					+ "\' ORDER BY SI_NAME ASC ";	
					
					
			IInfoObjects subFolders = (IInfoObjects) iInf.query(sQuery);
			if (subFolders.size() > 0) {
				IFolder	iFld = (IFolder)subFolders.get(0);
				res= new ObjectBO(String.valueOf(iFld.getCUID()), iFld.getTitle(),iFld.getTitle(), iFld.getKind(), String.valueOf(iFld.getParentCUID()));
			}
			

		} catch (SDKException e) {
			throw new WrongValueException(e.getMessage());
		} finally {
			enterpriseSession.logoff();
		}
		
		return res;
		
	}
	
	public static ObjectBO recupereObjectBODossier() {
		
		
		return recupereObjectBODossier(BO_FOLDER_ID);
		
	}
	
	public static ArrayList<ObjectBO> listeFolderBO (String idDossier) {
		
		ArrayList<ObjectBO> res = new ArrayList<ObjectBO>();
		
		IEnterpriseSession enterpriseSession =	getEnterpriseSession();
		
		IInfoStore iInf  = null;
		try {
			iInf  = (IInfoStore) enterpriseSession.getService("InfoStore");
	
			String sQuery = "SELECT SI_CUID, SI_NAME, SI_KIND, SI_PARENT_CUID FROM "
					+ "CI_INFOOBJECTS WHERE ( SI_PARENT_CUID = \'"
					+ idDossier + "\') AND SI_KIND=\'"
					+ CeKind.FOLDER
					+ "\' ORDER BY SI_NAME ASC ";	
					
					
			IInfoObjects subFolders = (IInfoObjects) iInf.query(sQuery);
			int iSize = subFolders.size();
			
			for	(int i=0; i<iSize; i++) {
				IFolder	iFld = (IFolder)subFolders.get(i);
				res.add(new ObjectBO(String.valueOf(iFld.getCUID()), iFld.getTitle(),iFld.getTitle(), iFld.getKind(), String.valueOf(iFld.getParentCUID())));
			}

		} catch (SDKException e) {
			throw new WrongValueException(e.getMessage());
		} finally {
			enterpriseSession.logoff();
		}
		
		return res;
		
	}
	
	
	public static ArrayList<ObjectBO> listeFolderBO () {
		
		return listeFolderBO(BO_FOLDER_ID);
		
	}
	
	public static void testeBO () {
		
		IEnterpriseSession enterpriseSession =	getEnterpriseSession();
		
		IInfoStore iInf  = null;
		try {
			iInf  = (IInfoStore) enterpriseSession.getService("InfoStore");
			
			//ID, CUID : 240965, ASGJr13z7A5Kv7BKtUDXBUU
			
			String sQuery = null;
			
			//test retrouve root
			sQuery = "SELECT SI_CUID, SI_NAME, SI_PARENT_CUID FROM "
					+ "CI_INFOOBJECTS WHERE ( SI_NAME = 'Dossier racine' "
					+") AND SI_KIND=\'"
					+ CeKind.FOLDER
					+ "\' ORDER BY SI_NAME ASC ";	
			
			IInfoObjects subFolders0 = (IInfoObjects) iInf.query(sQuery);
			int iSize0 = subFolders0.size();
			
			System.out.println("dossier racine");
			for	(int i=0; i<iSize0; i++) {
				IFolder	iFld = (IFolder)subFolders0.get(i);
				
				System.out.println("sId="+iFld.getCUID()+" si_name="+iFld.getTitle());
			}
			
			
			String iID = "240965";
			//Liste des dossiers
			sQuery = "SELECT SI_CUID, SI_NAME, SI_PARENT_CUID FROM "
			+ "CI_INFOOBJECTS WHERE ( SI_PARENT_CUID = \'"
			+ iID + "\') AND SI_KIND=\'"
			+ CeKind.FOLDER
			+ "\' ORDER BY SI_NAME ASC ";	
			
			
			IInfoObjects subFolders = (IInfoObjects) iInf.query(sQuery);
			int iSize = subFolders.size();
			
			System.out.println("sous dossier");
			
			for	(int i=0; i<iSize; i++) {
				IFolder	iFld = (IFolder)subFolders.get(i);
				
				System.out.println("sId="+iFld.getCUID()+" si_name="+iFld.getTitle());
			}
			
			//liste des documents
			sQuery = "SELECT SI_CUID, SI_NAME, SI_PARENT_CUID "
					+ "FROM CI_INFOOBJECTS WHERE ( SI_PARENT_CUID = \'"
					+ iID + "\') AND SI_KIND =\'"
					+ CeKind.WEBI
					+ "\' ORDER BY SI_NAME ASC";
			IInfoObjects webiDocuments = (IInfoObjects) iInf.query(sQuery);
			int	wSize = webiDocuments.size();
			
			System.out.println("doc webi");
			
			for	(int j=0; j<wSize; j++) {
				IInfoObject iObj = (IInfoObject)webiDocuments.get(j);
				System.out.println("sId="+iObj.getCUID()+" si_name="+iObj.getTitle());
			}
			
		} catch (SDKException e) {
			e.printStackTrace();
		} finally {
			enterpriseSession.logoff();
		}
		
		
	}
	
	/**
	 * Se connecte à BO et récupère le tokenBO
	 * @return le tokenBO
	 * @throws SDKException SDKException
	 */
	private static String getTokenBO() throws SDKException {
		
		String tokenBO = null;
	
		ISessionMgr sm = CrystalEnterprise.getSessionMgr();
		String user = CurrentUser.getCurrentUser().getUsername();
		String password = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		
		IEnterpriseSession enterpriseSession = null;
		ILogonTokenMgr logonTokenMgr=null;
		try {
			enterpriseSession = sm.logon(user, password, BO_SERVEUR, BO_SEC);
			logonTokenMgr = enterpriseSession.getLogonTokenMgr();

			tokenBO = logonTokenMgr.createWCAToken("", 1, 1);
		} catch (SDKException ex) {
			Messagebox.show(ex.getMessage(),"Erreur Business Object",Messagebox.OK,Messagebox.ERROR);
			try {
				if (enterpriseSession != null) {
					enterpriseSession.logoff();
				}
			} catch (Exception e) {
				// tant pis...
			}
		} finally {
			////Ne pas faire logoff sinon le rapport ne s"'affiche pas
			//enterpriseSession.logoff();

			//On planifie de libérer le token après BO_RELEASE_TIMER secondes
			TimerTaskBO timerTaskBO = new TimerTaskBO(enterpriseSession);
			timerTaskBO.schedule(Integer.valueOf(BO_RELEASE_TIMER));
			
		}
		
        return tokenBO;
		
	}	
	
	
	/**
	 * 
	 * @param iDocID iDocID
	 * @return Url du rapport BO pour la Iframe en récupérant le dossier dans le contexte
	 */
	public static String getURLRapportBO(String iDocID) {
		
		String tokenBO;
		try {
			tokenBO = getTokenBO();
		} catch (SDKException e) {
			throw new WrongValueException(e.getMessage());
		}
		
		return Executions.encodeURL(URL_OPEN_DOCUMENT+"?iDocID="+iDocID+(tokenBO == null ? "" : "&token="+tokenBO) +"&sIDType=CUID");
		
		
	}
}
