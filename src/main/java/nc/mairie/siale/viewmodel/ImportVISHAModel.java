/**
 * 
 */
package nc.mairie.siale.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import nc.mairie.siale.domain.Etablissement;
import nc.mairie.siale.technique.Action;

import org.springframework.transaction.annotation.Transactional;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Progressmeter;
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
	
	@Wire("#includeUpload #upload")
	Window upload;

	@Wire("#includeInject #inject")
	Window inject;
	
	@Wire("#includeInject #inject #progressmeter")
	Progressmeter progressmeter;

	public Hashtable<String, EtabVISHA> hashEtablissementVISHA;

	List<EtabVISHA> listeEtabVISHA;
	
	Action actionImport= Action.AUCUNE;
	Action actionUpload= Action.AJOUT;
	Action actionInject= Action.AUCUNE;
	
	int progressMeterValue;
	
	@Wire
	Div resultImport;
	
	String msgImport;
	
	public String getMsgImport() {
		return msgImport;
	}

	public void setMsgImport(String msgImport) {
		this.msgImport = msgImport;
	}

	public int getProgressMeterValue() {
		return progressMeterValue;
	}

	public void setProgressMeterValue(int progressMeterValue) {
		this.progressMeterValue = progressMeterValue;
	}

	public Action getActionInject() {
		return actionInject;
	}

	public void setActionInject(Action actionInject) {
		this.actionInject = actionInject;
	}

	public Action getActionUpload() {
		return actionUpload;
	}

	public void setActionUpload(Action actionUpload) {
		this.actionUpload = actionUpload;
	}

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
	
	@Listen("onClick=#uploadBtn;")
	public void onClick$uploadBtn() {
		setActionUpload(Action.AJOUT);
		binder.loadComponent(upload);
	}

	@Listen("onClick=#includeUpload #upload #annulerUpload;")
	public void onClick$annulerUpload() {
		setActionUpload(Action.AUCUNE);
		setActionImport(Action.AUCUNE);
		binder.loadAll();
	}

	@Listen("onUpload=#includeUpload #upload #uploadBtn;")
	public void onUpliad$uploadBtn(UploadEvent event) {
		Clients.showBusy("Upload en cours...");
		Events.echoEvent("onLater", upload.getFellow("uploadBtn"), event.getMedia());
	}
	
	@Listen("onLater=#includeUpload #upload #uploadBtn;")
    public void uploadFile(Event event) {

	    try {
			Media media = (Media)event.getData();
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
				e.printStackTrace();
				alert(e.getMessage());
				return;
			} catch (IOException e) {
				e.printStackTrace();
				alert(e.getMessage());
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
			
			setActionImport(Action.AJOUT);
			setActionUpload(Action.AUCUNE);
			
			Messagebox.show("Fichier téléchargé avec succès. Vérifier les modification et cliquer sur Valider pour prendre en compte les modifications.", "Télechagement terminé", Messagebox.OK, Messagebox.INFORMATION);
			
			binder.loadAll();
		} finally {
			Clients.clearBusy();
		}
 
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
	


	@Listen("onLater = #includeInject #inject #progressmeter")
	public void onLater$progressmeter(Event event) {
		int value = Integer.parseInt((String)event.getData());
		System.out.println("onLater$progressmeter avec "+value);
		progressmeter.setValue(value);
		binder.loadComponent(progressmeter);
	}

	List<EtabVISHA> tempList;
	
	@Listen("onClick = #validerImport")
	public void onClick$valiserImport() {
		
		//Clients.showBusy("Injection en cours");
		setActionInject(Action.AJOUT);
		binder.loadComponent(inject);
		
		tempList = new ArrayList<ImportVISHAModel.EtabVISHA>(hashEtablissementVISHA.values());
		
		Events.echoEvent("onLater", importVISHA.getFellow("validerImport"), new Integer(0));
	}
	
	
	
	@Listen("onLater = #validerImport")
	@Transactional
	public void onLater$valiserImport(Event event) {
	
		
		int deb = (Integer)event.getData();
		
		int mod = hashEtablissementVISHA.size() / 10;
		
		try {
			for (int i = deb; i < tempList.size(); i++) {
				
				System.out.println(i);
				
				tempList.get(i).getEtablissement().merge();
				
				//si on a atteint mod, on echoEvent pour les prochains (sauf si on est à la fin
				if (i== deb+mod && i!= tempList.size() -1) {
					progressmeter.setValue(i*100/hashEtablissementVISHA.size());
					Events.echoEvent("onLater", importVISHA.getFellow("validerImport"), new Integer(deb+mod+1));
					return;
				}
				
				
				
			}
		} catch (Exception e){
			setActionInject(Action.AUCUNE);
			binder.loadAll();
			return;
		}

		setActionInject(Action.AUCUNE);
		
		Messagebox.show("Les établissements ont été mis à jour.", "Import terminé", Messagebox.OK, Messagebox.INFORMATION);
		
		initialiseAllListes();
		binder.loadAll();
		
		
//		int nbval = hashEtablissementVISHA.size();
//		int cpt=0;
//		int oldvaleur=0;
//		try {
//			for (EtabVISHA etabVISHA : tempCollec) {
//				
//				etabVISHA.getEtablissement().merge();
//				
//				progressMeterValue = (cpt++)*100/nbval;
//				System.out.println("cpt : "+ cpt);
//				
//				if (progressMeterValue != oldvaleur && progressMeterValue % 10 == 0 ) {
//					
//					oldvaleur=progressMeterValue;
//					
//					System.out.println("valeur : "+progressMeterValue);
//					
//					Events.echoEvent("onLater", progressmeter, String.valueOf(progressMeterValue) );
//					
//					//progressmeter.setValue(valeur);
//					//binder.loadComponent(inject);
//					
//				
//				}
//				
//			}
//		} 
//		catch (Exception e) {
//			Messagebox.show("Erreur rencontrée: "+e.getMessage(), "Import annulé", Messagebox.OK, Messagebox.ERROR);
//		}
//		finally {
//			//Clients.clearBusy();
//			setActionInject(Action.AUCUNE);
//			binder.loadComponent(inject);
//		}
//
//		Messagebox.show("Les établissements ont été mis à jour.", "Import terminé", Messagebox.OK, Messagebox.INFORMATION);
//		
//		initialiseAllListes();
//		binder.loadAll();
		
	}
}
