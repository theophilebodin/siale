/**
 * 
 */
package nc.mairie.siale.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import nc.mairie.siale.domain.Etablissement;
import nc.mairie.siale.technique.Action;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;



/**
 * @author boulu72
 *
 */
public class ImportVISHAModel extends SelectorComposer<Component> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6445240917437474565L;
	
	
	AnnotateDataBinder binder;

	@Wire
	Window importVISHA;

	
	Hashtable<String, EtabVISHA> hashEtablissementVISHA;

	List<EtabVISHA> listeEtabVISHA;
	
	Action actionImport= Action.AUCUNE;
	
	public Action getActionImport() {
		return actionImport;
	}

	public void setActionImport(Action actionImport) {
		this.actionImport = actionImport;
	}

	public List<EtabVISHA> getListeEtabVISHA() {
		return listeEtabVISHA;
	}



	public void setListeEtabVISHA(List<EtabVISHA> listeEtabVISHA) {
		this.listeEtabVISHA = listeEtabVISHA;
	}



	public class EtabVISHA {
		public Etablissement etablissement;
		public Action action;
		public Etablissement getEtablissement() {
			return etablissement;
		}
		public void setEtablissement(Etablissement etablissement) {
			this.etablissement = etablissement;
		}
		public Action getAction() {
			return action;
		}
		public void setAction(Action action) {
			this.action = action;
		}
		public EtabVISHA(Etablissement etablissement, Action action) {
			super();
			this.etablissement = etablissement;
			this.action = action;
		}
	};

	
	public void initialiseAllListes() {
	   	//Alimentation des etablissements actuels
		hashEtablissementVISHA = new Hashtable<String, EtabVISHA>();
		for (Etablissement etablissement : Etablissement.findAllEtablissements()) {
			hashEtablissementVISHA.put(etablissement.getCode(), new EtabVISHA(etablissement, Action.AUCUNE));
		}
	 
    	setListeEtabVISHA(new ArrayList<ImportVISHAModel.EtabVISHA>());
    	getListeEtabVISHA().addAll(hashEtablissementVISHA.values());
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
		
		initialiseAllListes();
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
	}
	
	
	public void erreurImport(String message) {
		Messagebox.show(message, "Erreur", 0, Messagebox.ERROR);
		setActionImport(Action.AUCUNE);
	}
	
	@Listen("onUpload=#importVISHA;" +
			"onUpload=#uploadBtn;")
    public void uploadFile(UploadEvent event) {

		setActionImport(Action.AJOUT);
		
	    Media media = event.getMedia();
        if (media == null) {
        	erreurImport("Aucun fichier n'a été sélectionné.");
        	return;
        }
        
        
        String contentType=media.getContentType();

        //Si pas excel, alors ereur
        if (!contentType.equalsIgnoreCase("application/vnd.ms-excel")) {
        	erreurImport("Le fichier n'est pas un fichier Excel.");
        	return;
        }
        
    	Workbook workbook=null;
    	try {
    		workbook = Workbook.getWorkbook(media.getStreamData());
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			alert("BiffException");
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			alert("IOException");
			return;
		}
    	
    	
    	Sheet sheet = workbook.getSheet(0);

    	//On teste les 4 1eres colonnes
    	if (! ((LabelCell)sheet.getCell(0,0)).getString().equals("Etablissement") ||
    		! ((LabelCell)sheet.getCell(1,0)).getString().equals("Contact")	||
    		! ((LabelCell)sheet.getCell(2,0)).getString().equals("Adresse") ||
    		! ((LabelCell)sheet.getCell(3,0)).getString().equals("Immatriculation")) {
    		workbook.close();
    		erreurImport("Verifier le fichier, les titres des 4 premières colonne doivent être :\n" +
    				"Etablissement, Contact, Adresse, Immatriculation");
    		return;
    	}
    	
    	
    	for (int i = 1; i < sheet.getRows(); i++) {
    		
    		
    		String libelle = ((LabelCell)sheet.getCell(0,i)).getString();
    		String contact = ((LabelCell)sheet.getCell(1,i)).getString();
    		String adresse = ((LabelCell)sheet.getCell(2,i)).getString();
    		String code = ((LabelCell)sheet.getCell(3,i)).getString();
    		
    		//l'a t"on déjà ?
    		EtabVISHA etabVISHA = hashEtablissementVISHA.get(code);

    		//Non trouvé, alors on le rajoute
    		if (etabVISHA == null) {
    			Etablissement etablissement = new Etablissement();
    			etablissement.setCode(code);
    			etablissement.setLibelle(libelle);
    			etablissement.setContact(contact);
    			etablissement.setAdresse(adresse);
    			hashEtablissementVISHA.put(code, new EtabVISHA(etablissement, Action.AJOUT));
    		//Sinon, si ce n'est pas un nouveau
    		} else if (! etabVISHA.action.equals("Nouveau")) {
    			Etablissement etablissement =  etabVISHA.etablissement;
    			//Si un des champs a changé
    			if (! (	etablissement.getAdresse().equals(adresse) &&
    					etablissement.getLibelle().equals(libelle)&&
    					etablissement.getContact().equals(contact)) ) {
    				etabVISHA.action = Action.MODIFICATION;
    				etablissement.setLibelle(libelle);
        			etablissement.setContact(contact);
        			etablissement.setAdresse(adresse);
    			}
    		}
    	}
    		
    	setListeEtabVISHA(new ArrayList<ImportVISHAModel.EtabVISHA>());
    	getListeEtabVISHA().addAll(hashEtablissementVISHA.values());
    	
    	workbook.close();
    	
        binder.loadAll();
 
	}
	
//	@Listen("onClick = #uploadBtn")
//	public void onClick$uploadBtn() {
//		
//		progressImportVISHA.setVisible(true);
//		//Fileupload.get();
//		
//		binder.loadComponent(progressImportVISHA);
//		
//	}
	
	@Listen("onClick = #annulerImport;" +
			"onCancel= #importVISHA")
	public void onClick$annulerImport() {
	
		actionImport = Action.AUCUNE;
		initialiseAllListes();
		
		binder.loadAll();
	}
}
