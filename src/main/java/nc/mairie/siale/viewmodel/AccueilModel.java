package nc.mairie.siale.viewmodel;

import java.util.ArrayList;
import java.util.List;

import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.technique.CurrentUser;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class AccueilModel extends GenericForwardComposer<Component>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 446527977528558574L;
	
	ListModelList<MenuNode> menuModelParametre = new ListModelList<MenuNode>();
	Listbox menuListboxParametre;
	
	ListModelList<MenuNode> menuModelMission = new ListModelList<MenuNode>();
	Listbox menuListboxMission;
	
	List<Listbox> listBoxes ;
	
	
	MenuNodeSelectListener listener = new MenuNodeSelectListener();
	
	
	MenuNodeItemRenderer renderer = new MenuNodeItemRenderer();
	Div contentDiv;
	
	Label titreLabel;
	Image titreImage;
	
	
	public AccueilModel(){
		
		System.out.println("LUC : A refaire après authentification");
		ControleurSIALE controleurSIALE = ControleurSIALE.findControleurSIALE(new Long(1));
		CurrentUser.setCurrentUser(controleurSIALE);
		
		menuModelMission.add(new MenuNode("Gestion","Gestion des missions","/_gestion_missions/GestionMissions.zul","/_accueil/mission.png"));
		
		
		menuModelParametre.add(new MenuNode("Paramètres","Gestion des paramètres","/_parametres/GestionParametres.zul","/_accueil/parametres.png"));
		if (CurrentUser.isAdmin()) {
			menuModelParametre.add(new MenuNode("**Droits","Gestion des droits","borderlayout_fn2.zul","/_accueil/droits.png"));
		}
		menuModelParametre.add(new MenuNode("**Import VISHA","Import VISHA","borderlayout_fn3.zul","/_accueil/VISHA.png"));
		menuModelParametre.add(new MenuNode("Barême notation","Barême des notations","/_bareme_notation/BaremeNotation.zul","/_accueil/Bareme.png"));
		menuModelParametre.add(new MenuNode("---Gestionexemple","Gestion des interventions","borderlayout_fn1.zul","/_accueil/intervention.png"));
		menuModelParametre.add(new MenuNode("---Paramètres MVVMV","Gestion des paramètres","/problemMVVM/GestionParametresMVVM.zul","/_accueil/parametres.png"));
		
	}
	
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		menuListboxMission.setModel(menuModelMission);
		menuListboxMission.setItemRenderer(renderer);
		menuListboxMission.addEventListener(Events.ON_SELECT,listener);
		
		menuListboxParametre.setModel(menuModelParametre);
		menuListboxParametre.setItemRenderer(renderer);
		menuListboxParametre.addEventListener(Events.ON_SELECT,listener);
		
		//on met toutes les listbox dans une Liste
		listBoxes = new ArrayList<Listbox>();
		listBoxes.add(menuListboxParametre);
		listBoxes.add(menuListboxMission);
		
	}

	class MenuNode {
		String titre;
		String label;
		String link;
		String image;
		public MenuNode(String label, String titre, String link, String image){
			this.label = label;
			this.titre = titre;
			this.link = link;
			this.image = image;
		}
		public String getImage() {
			return image;
		}
		public String getImage_24x24() {
			return image == null ? null : image.replace(".", "_24x24.");
		}
		public void setImage(String image) {
			this.image = image;
		}
		public String getTitre() {
			return titre;
		}
		public void setTitre(String titre) {
			this.titre = titre;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
	}
	
	class MenuNodeItemRenderer implements ListitemRenderer<Object>{

		public void render(Listitem item, Object data, int index)
				throws Exception {
		MenuNode node = (MenuNode)data;
		//item.setImage("/_accueil/icon-24x24.png");
		item.setImage(node.getImage_24x24());
		item.setLabel(node.getLabel());
		item.setValue(node);	
		}
	}
	
	class MenuNodeSelectListener implements EventListener<Event>{
		public void onEvent(Event event) throws Exception {
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
				Executions.createComponents(node.getLink(),contentDiv,null);
			}
		}		
	}

	public void onClick$menuGenerique() {
		Executions.createComponents("/_parametres/GestionParametres.zul",contentDiv,null);
		titreLabel.setValue("Gestion des paramètres");
	}
	public void onAfterRender$menuListboxMission() {
		menuListboxMission.setSelectedIndex(0);
		Events.sendEvent(menuListboxMission, new Event(Events.ON_SELECT,menuListboxMission));
	}



}
