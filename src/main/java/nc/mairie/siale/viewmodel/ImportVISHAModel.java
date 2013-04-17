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

import org.springframework.transaction.annotation.Transactional;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
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

//	@Listen("onUpload=#importVISHA;" +
//			"onUpload=#uploadBtn;")
	@Listen("onUpload=#includeUpload #upload #uploadBtn;")
    public void uploadFile(UploadEvent event) {

		
		
		
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
	
	class T extends Thread {


		private Progressmeter obj;
		private final Desktop _desktop;



		public T(Progressmeter pt) {
		_desktop = pt.getDesktop();
		obj = pt;
		}

		@Transactional
		public void run()
		{
			try {
				
				int nbval = hashEtablissementVISHA.size();
				int cpt=0;
				
				int oldvaleur=0;
				
				for (EtabVISHA etabVISHA : hashEtablissementVISHA.values()) {
					
					etabVISHA.getEtablissement().merge();
					
					int valeur = (cpt++)*100/nbval;
					System.out.println("cpt : "+ cpt);
					
					if (valeur != oldvaleur && valeur % 10 == 0 ) {
					
						oldvaleur=valeur;
						
						System.out.println("valeur : "+valeur);
						
						Executions.activate(_desktop);
						try {
							
							obj.setValue(valeur);
							
						} finally {
		                     Executions.deactivate(_desktop);
		                     //Thread.sleep(50);
		                }
					}
					}
				
				
				
//				int i=1;
//	            while (i<101) {
//	 
//	                //Step 2. Activate to grant the access of the give desktop
//	                Executions.activate(_desktop);
//	                try {
//	                     //Step 3. Update UI
//	                	obj.setValue(i++); //whatever you like
//	                } finally {
//	                    //Step 4. Deactivate to return the control of UI back
//	                     Executions.deactivate(_desktop);
//	                }
//	            }
	        } catch (Exception ex) {
	        	//setMsgImport(ex.getMessage());
	        	
	        }
			
		}

	} 
	
	
	@Listen("onClick = #validerImport")
	@Transactional
	public void onClick$valiserImport() {
		Clients.showBusy("Injection en cours");
		Events.echoEvent("onLater", importVISHA.getFellow("validerImport"), null);
	}
	
	
	
	@Listen("onLater = #validerImport")
	@Transactional
	public void onLater$valiserImport() {
	
		int cpt=0;
		try {
			for (EtabVISHA etabVISHA : hashEtablissementVISHA.values()) {
				System.out.println(cpt++);
				etabVISHA.getEtablissement().merge();
			}
		} 
//		catch (Exception e) {
//			Messagebox.show("Erreur rencontrée: "+e.getMessage(), "Import annulé", Messagebox.OK, Messagebox.ERROR);
//		}
		finally {
			Clients.clearBusy();
		}

		Messagebox.show("Les établissements ont été mis à jour.", "Import terminé", Messagebox.OK, Messagebox.INFORMATION);
		
		initialiseAllListes();
		binder.loadAll();
		
	}
}
