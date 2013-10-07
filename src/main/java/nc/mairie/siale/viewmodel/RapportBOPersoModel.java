/**
 * 
 */
package nc.mairie.siale.viewmodel;


import java.util.ArrayList;


import nc.mairie.siale.technique.RapportBO;
import nc.mairie.siale.technique.RapportBO.ObjectBO;

import org.zkoss.zk.ui.Component;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;


/**
 * @author boulu72
 *
 */
public class RapportBOPersoModel extends SelectorComposer<Component> {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2617321906240279396L;
	
	AnnotateDataBinder binder;
	
	@Wire
	Window rapportBOPerso;
	
	@Wire
	Iframe iframeBO;
	
	@Wire
	Groupbox groupeRecherche;
	
	private String rapportBOCourant;
	
	ArrayList<ObjectBO> listFolderBO = null;
	ArrayList<ObjectBO> listDocumentBO = null;
	
	ObjectBO folderCourant;
	
	ObjectBO documentCourant;
	
	
	public ObjectBO getDocumentCourant() {
		return documentCourant;
	}

	public void setDocumentCourant(ObjectBO documentCourant) {
		this.documentCourant = documentCourant;
	}

	public ArrayList<ObjectBO> getListDocumentBO() {
		return listDocumentBO;
	}

	public void setListDocumentBO(ArrayList<ObjectBO> listDocumentBO) {
		this.listDocumentBO = listDocumentBO;
	}

	
	public ObjectBO getFolderCourant() {
		return folderCourant;
	}

	public void setFolderCourant(ObjectBO folderCourant) {
		this.folderCourant = folderCourant;
	}

	
	public ArrayList<ObjectBO> getListFolderBO() {
		if (listFolderBO == null) {
			listFolderBO = initialiseListeDossierBO(null);
		}
		return listFolderBO;
	}

	public void setListFolderBO(ArrayList<ObjectBO> listFolderBO) {
		this.listFolderBO = listFolderBO;
	}

	public String getRapportBOCourant() {
		return rapportBOCourant;
	}

	public void setRapportBOCourant(String rapportBOCourant) {
		this.rapportBOCourant = rapportBOCourant;
	}

	public ArrayList<ObjectBO> initialiseListeDossierBO(String idDossier) {
		ArrayList<ObjectBO> res = idDossier == null ? RapportBO.listeFolderBO() : RapportBO.listeFolderBO(idDossier);
		ObjectBO folder = idDossier == null ? RapportBO.recupereObjectBODossier() : RapportBO.recupereObjectBODossier(idDossier);
		if (folder!=null) {
			folder.setDisplayName("..");
			res.add(0, folder);
		}
		setListDocumentBO(initialiseListeDocumentBO(idDossier));
		
		return res;
	}
	
	public ArrayList<ObjectBO> initialiseListeDocumentBO(String idDossier) {
		ArrayList<ObjectBO> res = idDossier == null ? RapportBO.listeDocumentsWebI() : RapportBO.listeDocumentsWebIduDossier(idDossier);
		return res;
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

	}
	
	@Listen("onClick = #testeBO;")
	public void onClick$testeBO() {

		RapportBO.testeBO();
		
	}

	public String getNomFolderCourant() {
		if (getFolderCourant() == null) {
			setFolderCourant(getListFolderBO().get(0));
		}
		//return "Dossier : "+( getFolderCourant().getParentId().equals("0") ? "/" : getListFolderBO().get(0).getName());
		
		if (getListFolderBO().get(0).getDisplayName().equals("..")) {
			return "Dossier : "+ getFolderCourant().getName();
		} else return "Dossier : /";
		
		//return "Dossier : "+ getFolderCourant().getName();
	}
	
	@Listen("onClick = #folderListItem;")
	public void onDoubleClick$folderListItem() {
		
		String idDossier = getFolderCourant().getDisplayName().equals("..") ? getFolderCourant().getParentId() :getFolderCourant().getId();
		
		setListFolderBO(initialiseListeDossierBO(idDossier));
		setFolderCourant(getListFolderBO().get(0));
		
		binder.loadAll();
		
	}
	
	@Listen("onClick = #documentListItem;")
	public void onDoubleClick$documentListItem() {
		
		iframeBO.setSrc(RapportBO.getURLRapportBO(getDocumentCourant().getId()));
		groupeRecherche.setOpen(false);
		
		binder.loadAll();
		
	}
	
	
}
