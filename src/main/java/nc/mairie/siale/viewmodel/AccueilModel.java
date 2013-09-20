package nc.mairie.siale.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.technique.CurrentUser;
import nc.mairie.siale.technique.LDAP;
import nc.mairie.siale.technique.RapportBO;
import nc.mairie.siale.technique.RapportBO.ObjectBO;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

public class AccueilModel extends SelectorComposer<Component>{
	
	/**
	 *
	 */
	private static final long serialVersionUID = 446527977528558574L;
	
	AnnotateDataBinder binder;

	@Wire
	Window accueil;
	
	ListModelList<MenuNode> menuModelParametre = new ListModelList<MenuNode>();
	@Wire
	Listbox menuListboxParametre;
	
	ListModelList<MenuNode> menuModelRapportBO = new ListModelList<MenuNode>();
	@Wire
	Listbox menuListboxRapportBO;
	
	ListModelList<MenuNode> menuModelMission = new ListModelList<MenuNode>();
	@Wire
	Listbox menuListboxMission;
	
	List<Listbox> listBoxes ;
	
	
	transient MenuNodeSelectListener listener = new MenuNodeSelectListener();
	
	
	MenuNodeItemRenderer renderer = new MenuNodeItemRenderer();
	@Wire
	Div contentDiv;
	
	@Wire
	Label titreLabel;
	@Wire
	Image titreImage;
	
	//DEB A VIRER: SIMULATION CHANGEMENT AUTHENTIFICATION	
			@Wire
			Listbox controleurSIALEListBox;
			
			ControleurSIALE currentUser;
			
			List<ControleurSIALE> listeControleurSIALE;
			
			
			public List<ControleurSIALE> getListeControleurSIALE() {
				return listeControleurSIALE;
			}
		
		
			public void setListeControleurSIALE(List<ControleurSIALE> listeControleurSIALE) {
				this.listeControleurSIALE = listeControleurSIALE;
			}
		
		
			public ControleurSIALE getCurrentUser() {
				return currentUser;
			}
		
		
			public void setCurrentUser(ControleurSIALE currentUser) {
				
				if (currentUser.getId()!= null) {
				
					this.currentUser = currentUser;
					CurrentUser.setCurrentUser(currentUser);
					
					if (menuListboxMission.getSelectedIndex()!=-1) {
						Events.sendEvent(menuListboxMission, new Event(Events.ON_SELECT,menuListboxMission));
					} else if (menuListboxRapportBO.getSelectedIndex()!=-1) {
						Events.sendEvent(menuListboxRapportBO, new Event(Events.ON_SELECT,menuListboxRapportBO));
					} else if (menuListboxParametre.getSelectedIndex()!=-1) {
						Events.sendEvent(menuListboxParametre, new Event(Events.ON_SELECT,menuListboxParametre));
					}
				} else {
					alert(currentUser.getDisplayname());
				}
				
				//menuListboxMission.setSelectedIndex(0);
				//Events.sendEvent(menuListboxMission, new Event(Events.ON_SELECT,menuListboxMission));
				
			}
// FIN A VIRER: SIMULATION CHANGEMENT AUTHENTIFICATION

	public AccueilModel(){

		super();
		
	}
	
		
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		menuModelMission.add(new MenuNode("Gestion","Gestion des missions","/_missions/GestionMissions.zul","/_accueil/mission.png"));
		
		//Recup des infos BO
		ArrayList<ObjectBO> arrDocWebi = RapportBO.listeDocumentsWebI();
		for (ObjectBO docWebi : arrDocWebi) {
			Map<String,Object> args=new HashMap<String, Object>();
			args.put("iDocID", docWebi.getId());
			menuModelRapportBO.add(new MenuNode(docWebi.getName(),"Rapport BO - "+docWebi.getName(),"/_rapport_BO/RapportBO.zul","/_accueil/BO.png", args));
		}
		
		//Rajout rapport perso
		menuModelRapportBO.add(new MenuNode("Rapport personnel","Rapport BO personnel","/_rapport_BO/RapportBOPerso.zul","/_accueil/BO.png", null));
			
		
		menuModelParametre.add(new MenuNode("Paramètres","Gestion des paramètres","/_parametres/GestionParametres.zul","/_accueil/parametres.png"));
		if (CurrentUser.getCurrentUser().isAdmin()) {
			menuModelParametre.add(new MenuNode("Droits","Gestion des droits","/_droits/GestionDroits.zul","/_accueil/droits.png"));
		}
		menuModelParametre.add(new MenuNode("Import VISHA","Import VISHA","/_VISHA/ImportVISHA.zul","/_accueil/VISHA.png"));
		menuModelParametre.add(new MenuNode("Barême notation","Barême des notations","/_bareme_notation/BaremeNotation.zul","/_accueil/Bareme.png"));
		menuModelParametre.add(new MenuNode("Mes paramètres","Mes paramètres","/_mes_parametres/MesParametres.zul","/_accueil/parametres.png"));
//		menuModelParametre.add(new MenuNode("---Gestionexemple","Gestion des missions","borderlayout_fn1.zul","/_accueil/mission.png"));
//		menuModelParametre.add(new MenuNode("---Paramètres MVVMV","Gestion des paramètres","/problemMVVM/GestionParametresMVVM.zul","/_accueil/parametres.png"));

		comp.setAttribute(comp.getId(), this, true);
		
		menuListboxMission.setModel(menuModelMission);
		menuListboxMission.setItemRenderer(renderer);
		menuListboxMission.addEventListener(Events.ON_SELECT,listener);
		
		menuListboxRapportBO.setModel(menuModelRapportBO);
		menuListboxRapportBO.setItemRenderer(renderer);
		menuListboxRapportBO.addEventListener(Events.ON_SELECT,listener);
		
		menuListboxParametre.setModel(menuModelParametre);
		menuListboxParametre.setItemRenderer(renderer);
		menuListboxParametre.addEventListener(Events.ON_SELECT,listener);
		
		//on met toutes les listbox dans une Liste
		listBoxes = new ArrayList<Listbox>();
		listBoxes.add(menuListboxParametre);
		listBoxes.add(menuListboxRapportBO);
		listBoxes.add(menuListboxMission);
		
		//DEB A VIRER: SIMULATION CHANGEMENT AUTHENTIFICATION		
			currentUser = CurrentUser.getCurrentUser();
			listeControleurSIALE = ControleurSIALE.findAllControleurSIALEs();
			Enumeration<String> e = LDAP.getHashParametres().keys();
			while (e.hasMoreElements()) {
				String cle = e.nextElement();
				ControleurSIALE cs = new ControleurSIALE();
				cs.setDisplayname(cle + "="+LDAP.getHashParametres().get(cle));
				listeControleurSIALE.add(cs);
			}
			
		//FIN A VIRER: SIMULATION CHANGEMENT AUTHENTIFICATION
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
		 
	}

	private static class MenuNode implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8364735892583540164L;
		String titre;
		String label;
		String link;
		String image;
		Map<String,Object> args; 
		
		public MenuNode(String label, String titre, String link, String image){
			this(label, titre, link, image, null);
		}
		public MenuNode(String label, String titre, String link, String image, Map<String,Object> args){
			this.label = label;
			this.titre = titre;
			this.link = link;
			this.image = image;
			this.args = args;
		}
		public String getImage() {
			return image;
		}
		public String getImage_24x24() {
			return image == null ? null : image.replace(".", "_24x24.");
		}
		public String getTitre() {
			return titre;
		}
		public String getLabel() {
			return label;
		}
		public String getLink() {
			return link;
		}
		public Map<String,Object> getArgs() {
			return args;
		}
	}
	
	private static class MenuNodeItemRenderer implements ListitemRenderer<Object>, Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -5660746701096064628L;

		public void render(Listitem item, Object data, int index)
				throws Exception {
		MenuNode node = (MenuNode)data;
		item.setImage(node.getImage_24x24());
		item.setLabel(node.getLabel());
		item.setValue(node);	
		}
	}
	
	private class MenuNodeSelectListener implements EventListener<Event>{
		public void onEvent(Event event) throws Exception {
			
			//on vire l'éventielle connexion à BO
			RapportBO.releaseTokenBO();
			
			Component comp = event.getTarget();
			
			//on déselectionne toutes les autres listbox du menu
			for (Listbox listbox : listBoxes) {
				if (listbox != comp)
					listbox.setSelectedIndex(-1);
			}
			
			Listitem item = ((Listbox)comp).getSelectedItem();
			
			contentDiv.getChildren().clear();
			if(item!=null){
				MenuNode node = (MenuNode)item.getValue();
				titreLabel.setValue(node.getTitre()); 
				titreImage.setSrc(node.getImage());
				Executions.createComponents(node.getLink(),contentDiv,node.getArgs());
			}
		}		
	}

	public void onClick$menuGenerique() {
		Executions.createComponents("/_parametres/GestionParametres.zul",contentDiv,null);
		titreLabel.setValue("Gestion des paramètres");
	}
	
	@Listen("onAfterRender = #menuListboxMission")
	public void onAfterRender$menuListboxMission() {
		menuListboxMission.setSelectedIndex(0);
		Events.sendEvent(menuListboxMission, new Event(Events.ON_SELECT,menuListboxMission));
	}



	public boolean isListeControleursSIALEVisible () {
		return CurrentUser.getCurrentUser().getUsername().toUpperCase().equals("ADMINWAS");
	}

	@Listen("onClick = #logout")
	public void onClick$logout() throws Exception {
		
		//logoff de l'éventuel BO
		RapportBO.releaseTokenBO();
		
		Executions.getCurrent().getSession().setAttribute("logout", "true");
		Executions.getCurrent().sendRedirect("/");
		
	}
	
}
