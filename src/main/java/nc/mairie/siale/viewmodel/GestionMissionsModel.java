/**
 * 
 */
package nc.mairie.siale.viewmodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nc.mairie.siale.domain.Bareme;
import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Droit;
import nc.mairie.siale.domain.Etablissement;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.MissionAction;
import nc.mairie.siale.domain.MissionActivite;
import nc.mairie.siale.domain.Notation;
import nc.mairie.siale.domain.Param;
import nc.mairie.siale.technique.Action;
import nc.mairie.siale.technique.Constantes;
import nc.mairie.siale.technique.ControleSaisie;
import nc.mairie.siale.technique.CurrentUser;
import nc.mairie.siale.technique.Outlook;
import nc.mairie.siale.technique.TypeEtablissement;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;


/**
 * @author boulu72
 *
 */
public class GestionMissionsModel extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8608406922690877596L;

	AnnotateDataBinder binder;
	
	@Wire
	Window gestionMissions;
	
	List<Mission> listeMission;
	Mission missionCourant;
	
	List<Param> listeAction;
		
	@Wire
	Div zoneSaisie;
	
	ListModelList<TypeEtablissement> listeTypeEtablissement = new ListModelList<TypeEtablissement>(nc.mairie.siale.technique.TypeEtablissement.values());
	
	@Wire
	Combobox etablissementListBox;

	List<Etablissement> listeEtablissement;
	
	Action actionMission = Action.AUCUNE;
	
	MissionActivite missionActiviteCourant;
	MissionActivite missionActiviteCourantSAV;
	
	Action actionActivite = Action.AUCUNE;
	
	@Wire
	Include includeSaisieActivite;
	@Wire("#includeSaisieActivite #zoneSaisieActivite")
	Window zoneSaisieActivite;
	
	List<Param> listeActivite;
	
	Action actionControleur = Action.AUCUNE;
	
	List<Object> listeControleurControleurSIALE;

	TreeSet<Object> listeControleurControleurSIALECourant;
	Object controleurCourant;
	Object controleurCourantSaisi;
	
	@Wire
	Include includeSaisieControleur;
	@Wire("#includeSaisieControleur #zoneSaisieControleur")
	Window zoneSaisieControleur;
	
	@Wire 
	Checkbox envoiOutlookCheckBox;
	
	@Wire
	Listbox missionActivitesListBox;
	@Wire
	Listbox missionControleursListBox;
	
	
	Comparator<Etablissement> comparatorEtablissement = new Comparator<Etablissement>() {

		@Override
		public int compare(Etablissement o1, Etablissement o2) {
			return o1.getNomAffichage().compareTo(o2.getNomAffichage());
		}
	};

	public List<Param> getListeAction() {
		return listeAction;
	}

	public void setListeAction(List<Param> listeAction) {
		this.listeAction = listeAction;
	}

	//@Wire("#zoneSaisieControleur #saisiComboControleurs")
	//Combobox zoneSaisieControleur$saisiComboControleurs;
	
	public Object getControleurCourantSaisi() {
		return controleurCourantSaisi;
	}

	public void setControleurCourantSaisi(Object controleurCourantSaisi) {
		this.controleurCourantSaisi = controleurCourantSaisi;
	}

	public Action getActionControleur() {
		return actionControleur;
	}

	public List<Object> getListeControleurControleurSIALE() {
		if (actionControleur == Action.SUPPRESSION) {
			List<Object> list = new ArrayList<Object>();
			list.add(getControleurCourant());
			return list;
		}
		
		return listeControleurControleurSIALE;
	}


	public void setListeControleurControleurSIALE(
			List<Object> listeControleurControleurSIALE) {
		this.listeControleurControleurSIALE = listeControleurControleurSIALE;
	}


	public TreeSet<Object> getListeControleurControleurSIALECourant() {
		return listeControleurControleurSIALECourant;
	}


	public void setListeControleurControleurSIALECourant(
			TreeSet<Object> listeControleurControleurSIALECourant) {
		this.listeControleurControleurSIALECourant = listeControleurControleurSIALECourant;
	}


	public Object getControleurCourant() {
		return controleurCourant;
	}


	public void setControleurCourant(Object controleurCourant) {
		this.controleurCourant = controleurCourant;
	}


	public void setActionControleur(Action actionControleur) {
		this.actionControleur = actionControleur;
	}


	public Action getActionMission() {
		return actionMission;
	}


	public void setActionMission(Action actionMission) {
		this.actionMission = actionMission;
	}

	public List<Param> getListeActivite() {
		if (actionActivite == Action.SUPPRESSION) {
			List<Param> list = new ArrayList<Param>();
			list.add(getMissionActiviteCourant().getTheActivite());
			return list;
		}
		
		return listeActivite;
	}


	public void setListeActivite(List<Param> listeActivite) {
		this.listeActivite = listeActivite;
	}


	public void setActionActivite(Action actionActivite) {
		this.actionActivite = actionActivite;
	}


	public Action getActionActivite() {
		return actionActivite;
	}


	public MissionActivite getMissionActiviteCourant() {
		return missionActiviteCourant;
	}


	public void setMissionActiviteCourant(MissionActivite missionActiviteCourant) {
		this.missionActiviteCourant = missionActiviteCourant;
	}


	public List<Etablissement> getListeEtablissement() {
		return listeEtablissement;
	}


	public ListModelList<TypeEtablissement> getListeTypeEtablissement() {
		return listeTypeEtablissement;
	}

	public List<Mission> getListeMission() {
		return listeMission;
	}

	public void setListeMission(List<Mission> listeMission) {
		this.listeMission = listeMission;
	}

	public Mission getMissionCourant() {
		return missionCourant;
	}
	
	public void setMissionCourant(Mission missionCourant) {
		this.missionCourant = missionCourant;
		
		if (missionCourant == null) return;
		
		SortedSet<MissionActivite> sortedSet= new TreeSet<MissionActivite>(new Comparator<MissionActivite>() {

			@Override
			public int compare(MissionActivite o1, MissionActivite o2) {
				return o1.getTheActivite().getNom().compareTo(o2.getTheActivite().getNom());
			}
		});
		
		sortedSet.addAll(missionCourant.getMissionActivites());
		
		missionCourant.setMissionActivites(sortedSet);
		
		listeControleurControleurSIALECourant = new TreeSet<Object>(new Comparator<Object>() {
						@Override
						public int compare(Object o1, Object o2) {
							if (o1 instanceof Param) {
								if (o2 instanceof Param) {
									return ((Param)o1).getNomAffichage().compareTo(((Param)o2).getNomAffichage());
								} else {
									return ((Param)o1).getNomAffichage().compareTo(((ControleurSIALE)o2).getNomAffichage());
								}
							} else {
								if (o2 instanceof Param) {
									return ((ControleurSIALE)o1).getNomAffichage().compareTo(((Param)o2).getNomAffichage());
								} else {
									return ((ControleurSIALE)o1).getNomAffichage().compareTo(((ControleurSIALE)o2).getNomAffichage());
								}
							}
						}
					}	
				);

		listeControleurControleurSIALECourant.addAll(missionCourant.getControleurs());
		listeControleurControleurSIALECourant.addAll(missionCourant.getControleursSIALE());
	}

	protected void initialiseAllListes() {
		Comparator<Param> comparator = new Comparator<Param>() {
			@Override
			public int compare(Param o1, Param o2) {
				return o1.getNom().compareTo(o2.getNom());
			}
		};
		
		listeMission=initialiseListeMissions();
		
		listeEtablissement=Etablissement.findAllEtablissements();
		Collections.sort(listeEtablissement, comparatorEtablissement);
		
		listeActivite = Param.findParamsActifsByNomDuTypeParam("ACTIVITE").getResultList();
		Collections.sort(listeActivite, comparator);
		
		listeAction = Param.findParamsActifsByNomDuTypeParam("ACTION").getResultList();
		Collections.sort(listeAction,comparator);
		
		listeControleurControleurSIALE = new ArrayList<Object>();
		listeControleurControleurSIALE.addAll(Param.findParamsActifsByNomDuTypeParam("CONTROLEUR").getResultList());
		//ajout seulment des actifs et contoleurs
		//listeControleurControleurSIALE.addAll(ControleurSIALE.findAllControleurSIALEs());
		Set<Droit> setDroit= new HashSet<Droit>();
		setDroit.add(Constantes.droitControleur);
		listeControleurControleurSIALE.addAll(ControleurSIALE.findControleurSIALEsByActifNotAndDroits(false, setDroit).getResultList());
		Collections.sort(listeControleurControleurSIALE, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof Param) {
					if (o2 instanceof Param) {
						return ((Param)o1).getNom().compareTo(((Param)o2).getNom());
					} else {
						return ((Param)o1).getNom().compareTo(((ControleurSIALE)o2).getNomAffichage());
					}
				} else {
					if (o2 instanceof Param) {
						return ((ControleurSIALE)o1).getNomAffichage().compareTo(((Param)o2).getNom());
					} else {
						return ((ControleurSIALE)o1).getNomAffichage().compareTo(((ControleurSIALE)o2).getNomAffichage());
					}
				}
			}
		});
	}
	/**
	 * Initialise la liste des missions en fonction des droits de l'utilisateur.
	 * @return
	 */
	protected List<Mission> initialiseListeMissions() {
		List<Mission> res;
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -6);
		
		if (CurrentUser.getCurrentUser().isAdmin()) {
			res = Mission.findMissionsByClotureeNotOrDatePrevueGreaterThan(true, c.getTime()).getResultList();
					//findAllMissions();
		} else {
			Set<ControleurSIALE> setControleurs = new HashSet<ControleurSIALE>();
			setControleurs.add(CurrentUser.getCurrentUser());
			
			//res = Mission.findMissionsByControleursSIALE(setControleurs).getResultList();
			res = Mission.findMissionsByControleursSIALEAndClotureeNotOrDatePrevueGreaterThan(setControleurs, true, c.getTime()).getResultList();
			
		}
		//test pour lazy de merde
		for (Mission mission : res) {
			mission.getControleurs();
			mission.getControleursSIALE();
			
		}
		return res;
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
		
		//recup du mission courant s'il y en a 1
		final Execution execution = Executions.getCurrent();
		try {
			Long id = (Long)execution.getArg().get("idMission");
			setMissionCourant(Mission.findMission(id));
		} catch (Exception e) {
			setMissionCourant(null);
		}
		
		//Initialisetion des listes 
		initialiseAllListes();
		
				
		binder = new AnnotateDataBinder(comp);
		binder.setLoadOnSave(false);
		binder.loadAll();

	}

	@Listen("onClick = #annulerMission;" +
			"onCancel= #gestionMissions")
	public void onClick$annulerMission() {
		if (getMissionCourant() != null && getMissionCourant().getId() != null ){
			setMissionCourant(Mission.findMission(getMissionCourant().getId()));
		}
		
		actionMission = Action.AUCUNE;
		initialiseAllListes();
		envoiOutlookCheckBox.setChecked(false);
		
		binder.loadAll();
	}
	
	public boolean isSauverMissionDisabled() {
		//Si le user en cours ne faitpas parti de la liste des controleurs de la missiion, il ne peut enregistrer
		boolean peutSauver = false;
		
		if (getMissionCourant() == null) return peutSauver;
		
		for (ControleurSIALE controleurSIALE : getMissionCourant().getControleursSIALE()) {
			if (CurrentUser.getCurrentUser().getId().equals(controleurSIALE.getId())){
				peutSauver=true;
				break;
			}
		}
		return ! peutSauver;
	}

	@Listen("onClick = #validerMission")
	public void onClick$validerMission() {
	
		//Test si validation possible 
		if (isSauverMissionDisabled()) {
			alert("Vous n'êtes pas habilité à enregistrer la mission");
			return;
		}
		
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(zoneSaisie);
		
		//test de etab bien selectionne
		if (getMissionCourant().getTypeEtablissement() == TypeEtablissement.ETABLISSEMENT) {
			if (getMissionCourant().getEtablissement() == null || getMissionCourant().getEtablissement().getId() == null) {
				controleSaisie.ajouteErreur(etablissementListBox, "Selectionner un etablissement");
			}
		}
		
		//test de activité
		if (getMissionCourant().getMissionActivites().size()==0 || getMissionCourant().getActivitePrincipale() == null) {
			controleSaisie.ajouteErreur(missionActivitesListBox, "L'activité principale est obligatoire");
		}
		//test de activité
		if (getMissionCourant().getControleursSIALE().size()==0) {
			controleSaisie.ajouteErreur(missionControleursListBox, "Un controleur SIALE est obligatoire");
		}
		
		//controle rg_cloturee (7.1.1.5) s'il y a une date d'action
		if (getMissionCourant().getCloturee()) {
			MissionAction mc = getMissionCourant().getMissionAction(); 
			if (mc == null || mc.getDateAction() == null) {
				controleSaisie.ajouteErreur(gestionMissions.getFellow("missionClotureeCheckBox"),
						"Il n'y a pas d'action de saisie, la mission ne peut être cloturée");
			}
		}
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
		
		switch (actionMission) {
		case AJOUT:
			//Controles

			//SGBD
			setMissionCourant(missionCourant.merge());
			
			//VIEW
			
			break;
		case MODIFICATION:
			//Controles
				
			//SGBD
			setMissionCourant(missionCourant.merge());
			
			//VIEW
			
			break;
		case SUPPRESSION:
			//Fait avec la messagebox
			break;

		default:
			break;
		}
		
		//envoi dans outlook
		if (envoiOutlookCheckBox.isChecked()) {
			  try {
				  //dans le zul : application.getInitParameter("HOST_SMTP")
				  
				  Outlook.sendCalendar(getMissionCourant());
				  //on décoche
				  envoiOutlookCheckBox.setChecked(false);
		
			  } catch (Exception e) {
				  alert ("Problème lors de l'envoi dans Outlook :"+e.getMessage());
			}
		}
		
		
		
		initialiseAllListes();
		actionMission = Action.AUCUNE;
		binder.loadAll();
	}

	@Listen("onClick = #notationMission")
	public void onClick$notationMission() {
		
		if (isSauverMissionDisabled()) {
			alert("Seul un contrôleur de la mission est autorisé");
			return;
		}
		
		if (getMissionCourant() == null) {
			alert("Vous devez sélectionner une mission avant de cliquer");
			return;
		}
		
		//Au moins un bareme doit être défini
		if (Bareme.countBaremes() == 0) {
			alert("Vous devez d'abord créer un barème dans \"Paramètres / Barême notation\" ");
			return;
		}	
		
		//Une mission cloturée ne peut être notée
		if (getMissionCourant().getCloturee()) {
			alert("Une mission cloturée ne peut être notée");
			return;
		}
		
		//Une mission non saisie ne peut être notée
		if (getMissionCourant().getSuiteDonnee()==null) {
			alert("Une mission doit être saisie avant de pouvoir être notée");
			return;
		}
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("idMission", getMissionCourant().getId());
		
		Component parent = gestionMissions.getParent();
		parent.getChildren().clear();
		
		//Window window= (Window) Executions.createComponents("/_saisir_mission/SaisirMission.zul", parent , args);
		Executions.createComponents("/_saisir_notation/SaisirNotation.zul", parent , args);
	}

	@Listen("onClick = #saisirMission")
	public void onClick$saisirMission() {
		
		if (isSauverMissionDisabled()) {
			alert("Seul un contrôleur de la mission est autorisé");
			return;
		}

		if (getMissionCourant() == null) {
			alert("Vous devez sélectionner une mission avant de cliquer");
			return;
		}
		
		//Une mission cloturée ne peut être saisie
		if (getMissionCourant().getCloturee()) {
			alert("Une mission cloturée ne peut être saisie");
			return;
		}
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("idMission", getMissionCourant().getId());
		
		Component parent = gestionMissions.getParent();
		parent.getChildren().clear();
		
		//Window window= (Window) Executions.createComponents("/_saisir_mission/SaisirMission.zul", parent , args);
		Executions.createComponents("/_saisir_mission/SaisirMission.zul", parent , args);
		
		//window.doModal();
		
	}

	@Listen("onClick = #supprimerMission")
	public void onClick$supprimerMission() {
		
		//Test si validation possible 
		if (isSauverMissionDisabled()) {
			alert("Vous n'êtes pas habilité à supprimer la mission");
			return;
		}
		
		//Si la mission est cloturée : impossible de supprimer
		if (getMissionCourant().getCloturee()) {
			alert("Suppression impossible, cette mission est clôturée");
			return;
		}
		
		//ne peut être supprimée si des notations la concernent
		if (Notation.existNotationByMission(getMissionCourant())) {
			alert("Suppression impossible, des notations concernent cette mission");
			return;
		}
		
		
		Messagebox.show("Confirmez-vous la suppression ?",
			    "Question", Messagebox.OK | Messagebox.CANCEL,
			    Messagebox.QUESTION,
			        new EventListener<Event>() {
						
						@Override
						public void onEvent(Event e) throws Exception {
			                if(Messagebox.ON_OK.equals(e.getName())){
			                	//OK est sélectionné donc on supprime
			        			
			        			//SGBD
			        			missionCourant.remove();
			        			
			        			//VIEW
			        			initialiseAllListes();
			        			actionMission = Action.AUCUNE;
			        			binder.loadAll();
			                }else if(Messagebox.ON_CANCEL.equals(e.getName())){
			                    //Cancel is clicked
			                	return;
			                }
			            }
			
			        }
			    );
	}

	public void onClick$modifierMissionPossible() {
		
		//Suite au déplacement de Action dans gestion mission, pour remettre carré les missions sans missionAction
		if (getMissionCourant().getMissionAction() == null) {
			MissionAction missionAction = new MissionAction();
			getMissionCourant().setMissionAction(missionAction);
		}
		
		actionMission=Action.MODIFICATION;
		envoiOutlookCheckBox.setDisabled(false);
		binder.loadAll();
	
	}
	
	@Listen("onClick = #modifierMission; onDoubleClick = #missionsListItem")
	public void onClick$modifierMission() {
		
		//Si elle est cloturée, on demande de confirer la déclôture
		if (getMissionCourant().getCloturee()) {
			
			Messagebox.show("La mission est cloturée.\nVoulez-vous d'abord la décloturer ?",
				    "Question", Messagebox.OK | Messagebox.CANCEL,
				    Messagebox.QUESTION,
				        new EventListener<Event>() {
							
							@Override
							public void onEvent(Event e) throws Exception {
				                if(Messagebox.ON_OK.equals(e.getName())){
				                	//OK est sélectionné donc on décloture
				                    getMissionCourant().setCloturee(false);
				                    onClick$modifierMissionPossible();
				                }else if(Messagebox.ON_CANCEL.equals(e.getName())){
				                    //Cancel is clicked
				                	return;
				                }
				            }

					
				        }
				    );
		} else {
			onClick$modifierMissionPossible();
		}
	}

	@Listen("onClick = #ajouterMission")
	public void onClick$ajouterMission() {
		actionMission = Action.AJOUT;
		
		Mission mission = new Mission();
		mission.setEtablissement(null);
		
		//la date pr&évue : demain à 08h:00
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 8, 0, 0);
		c.add(Calendar.DATE, 1);
		mission.setDatePrevue(c.getTime());
		//durée prévue par défaut
		c.set(Calendar.HOUR, 2);
		mission.setDureePrevueRDV(c.getTime());
		mission.setTypeEtablissement(TypeEtablissement.ETABLISSEMENT);
		
		//La missionAction
		mission.setMissionAction(new MissionAction());
		
		//Ajout du user en cours
		mission.getControleursSIALE().add(CurrentUser.getCurrentUser());
		setMissionCourant(mission);
		//on sélectionne Etablissement
		//setTypeEtablissementCourant(TypeEtablissement.ETABLISSEMENT);
		
		envoiOutlookCheckBox.setDisabled(false);
		binder.loadAll();
	}

	public void onChangingDETOURNE$etablissementListBox(String value) {
 		List<Etablissement> newModel = new ArrayList<Etablissement>();
 		
 		for (Etablissement etab : getListeEtablissement()) {
			if (etab.getNomAffichage().toUpperCase().contains(value.toUpperCase())) {
 				newModel.add(etab);
 			}
 		}
 		
 		//tri pas utile car on part d'une liste triée...
 		//Collections.sort(newModel, comparatorEtablissement);
 		
 		//etablissementListBox.setModel(new ListModelList<Etablissement>(newModel));
 		
 		BindingListModelList<Etablissement>  bind= new BindingListModelList<Etablissement>(newModel,false);
 		
 		etablissementListBox.setModel(bind);
 		etablissementListBox.setRenderdefer(5000);
		 		
	}
	
	@Listen("onSelect = #typeEtablissementCourant")
	public void onSelect$typeEtablissementCourant () {
		//Pour le refresh des zones à saisir
		binder.loadComponent(zoneSaisie);
	}
	
	@Listen("onClick = #supprimerMissionActivite")
	public void onClick$supprimerMissionActivite() {
		//test que pas la principale
		if (getMissionActiviteCourant().getPrincipale()) {
			Messagebox.show("Impossible de supprimer l'activite principale");
			return;
		}
		
		setActionActivite(Action.SUPPRESSION);
		binder.loadComponent(zoneSaisieActivite);
	}
	
	@Listen("onClick = #ajouterMissionActivite")
	public void onClick$ajouterMissionActivite() {
		setActionActivite(Action.AJOUT);
		MissionActivite nouv = new MissionActivite();
		nouv.setTheMission(getMissionCourant());
		nouv.setPrincipale(false);
		nouv.setTheActivite(null);
		setMissionActiviteCourant(nouv);
		binder.loadComponent(zoneSaisieActivite);
	}
	
	@Listen("onClick = #modifierMissionActivite; onDoubleClick = #missionActivitesListItem")
	public void onClick$modifierMissionActivite() {
		try {
			missionActiviteCourantSAV = this.missionActiviteCourant.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		setActionActivite(Action.MODIFICATION);
		binder.loadComponent(zoneSaisieActivite);
	}

	@Listen("onClick = #includeSaisieActivite #zoneSaisieActivite #annulerMissionActivite;" +
			"onCancel= #includeSaisieActivite #zoneSaisieActivite")
	public void onClick$annulerMissionActivite() {

		if (actionActivite != Action.AJOUT) {
			getMissionCourant().getMissionActivites().remove(missionActiviteCourant);
			getMissionCourant().getMissionActivites().add(missionActiviteCourantSAV);
			setMissionActiviteCourant(missionActiviteCourantSAV);
		}
		
		
		setActionActivite(Action.AUCUNE);
		
		//Pour forcer à supprimer les messages d'erreur
		zoneSaisieActivite.invalidate();
		
		binder.loadComponent(zoneSaisie);
	}

	
	@Listen("onClick = #includeSaisieActivite #zoneSaisieActivite #validerMissionActivite")
	public void onClick$validerMissionActivite() {
		
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(zoneSaisieActivite);
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
	
		
		switch (actionActivite) {
		case AJOUT:
			
			//verif qu'il n'existe pas déjà
			for (MissionActivite missionActivite : getMissionCourant().getMissionActivites()) {
				
				if (missionActivite.getTheActivite().getId().equals(getMissionActiviteCourant().getTheActivite().getId())) {
					Messagebox.show("Cette activité est déjà affectée");
					return;
				}
			}
			
			//Si l'activite est principale, on vire les autres principales
			if (getMissionActiviteCourant().getPrincipale()) {
				for (MissionActivite missionActivite : getMissionCourant().getMissionActivites()) {
					if (!missionActivite.equals(getMissionActiviteCourant())) {
						missionActivite.setPrincipale(false);
					}
				}
			}

			//on persiste
			//getMissionActiviteCourant().persist();
			
			//mise à jour de la liste
			getMissionCourant().getMissionActivites().add(getMissionActiviteCourant());
			
			break;
		
		case MODIFICATION:
			
			//si pas principale de sélectionnée
			if (!getMissionActiviteCourant().getPrincipale()) {
				//On vérifie qu'il y ait au moins une principale
				boolean existe = false;
				for (MissionActivite missionActivite : getMissionCourant().getMissionActivites()) {
					if (missionActivite.getPrincipale()) {
						existe=true;
						break;
					}
				}	
				if (!existe) {
					Messagebox.show("L'activité principale doit être définie");
					return;
				}
			} else {
				//Sinon l'activite est principale, on vire les autres principales
				for (MissionActivite missionActivite : getMissionCourant().getMissionActivites()) {
					if (!missionActivite.equals(getMissionActiviteCourant()) && missionActivite.getPrincipale()) {
						missionActivite.setPrincipale(false);
						//missionActivite.merge();
					}
				}	
			}
			
			break;
		case SUPPRESSION:
			
			//on persiste
			//getMissionActiviteCourant().remove();
			getMissionCourant().getMissionActivites().remove(getMissionActiviteCourant());
			
			//setMissionActiviteCourant(null);
			
			break;
		default:
			break;
		}
		setActionActivite(Action.AUCUNE);
		binder.loadComponent(zoneSaisie);
	}

	@Listen("onClick = #supprimerControleur")
	public void onClick$supprimerControleur() {
		//ok si pas user courant
		if (getControleurCourant() instanceof ControleurSIALE) {
			if (((ControleurSIALE)getControleurCourant()).getId().equals(CurrentUser.getCurrentUser().getId())) {
				Messagebox.show("Impossible de vous supprimer de la liste des controleurs");
				return;
			}
		}
		
		setControleurCourantSaisi(getControleurCourant());
		setActionControleur(Action.SUPPRESSION);
		binder.loadComponent(zoneSaisieControleur);
	}
	
	@Listen("onClick = #ajouterControleur")
	public void onClick$ajouterControleur() {
		setControleurCourantSaisi(null);
		setActionControleur(Action.AJOUT);
		binder.loadComponent(zoneSaisieControleur);
	}
	
	@Listen("onClick = #modifierControleur; onDoubleClick = #missionControleursListItem")
	public void onClick$modifierControleur() {
		//ok si pas user courant
		if (getControleurCourant() instanceof ControleurSIALE) {
			if (((ControleurSIALE)getControleurCourant()).getId().equals(CurrentUser.getCurrentUser().getId())) {
				Messagebox.show("Impossible de vous modifier de la liste des controleurs");
				return;
			}
		}
		
		setControleurCourantSaisi(getControleurCourant());
		setActionControleur(Action.MODIFICATION);
		binder.loadComponent(zoneSaisieControleur);
	}

	@Listen("onClick = #includeSaisieControleur #zoneSaisieControleur #annulerControleur;" +
			"onCancel= #includeSaisieControleur #zoneSaisieControleur")
	public void onClick$annulerControleur() {
		setActionControleur(Action.AUCUNE);
		
		//Pour forcer à supprimer les messages d'erreur
		zoneSaisieControleur.invalidate();

		binder.loadComponent(zoneSaisie);
	}

	@Listen("onClick = #includeSaisieControleur #zoneSaisieControleur #validerControleur")
	public void onClick$validerControleur() throws Exception{
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(zoneSaisieControleur);
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();

		
		switch (actionControleur) {
		case AJOUT:
			
			//verif qu'il n'existe pas déjà
			if (getControleurCourantSaisi() instanceof ControleurSIALE) {
				ControleurSIALE controleurSaisi = (ControleurSIALE)getControleurCourantSaisi();
				for (ControleurSIALE controleur : getMissionCourant().getControleursSIALE()) {
					if (controleur.getId().equals(controleurSaisi.getId())) {
						Messagebox.show("Ce contrôleur est déjà affectée");
						return;
					}
				}
				getMissionCourant().getControleursSIALE().add(controleurSaisi);
			} else {
				Param controleurSaisi = (Param)getControleurCourantSaisi();
				for (Param controleur : getMissionCourant().getControleurs()) {
					if (controleur.getId().equals(controleurSaisi.getId())) {
						Messagebox.show("Ce contrôleur est déjà affectée");
						return;
					}
				}
				getMissionCourant().getControleurs().add(controleurSaisi);
			}
			
			//ajout du contrôleur
			getListeControleurControleurSIALECourant().add(getControleurCourantSaisi());
			
			break;
			
		case MODIFICATION:
			
			//verif qu'il n'existe pas déjà
			if (getControleurCourantSaisi() instanceof ControleurSIALE) {
				ControleurSIALE controleurSaisi = (ControleurSIALE)getControleurCourantSaisi();
				for (ControleurSIALE controleur : getMissionCourant().getControleursSIALE()) {
					if (controleur.getId().equals(controleurSaisi.getId())) {
						Messagebox.show("Ce contrôleur est déjà affectée");
						return;
					}
				}
				getMissionCourant().getControleursSIALE().remove((ControleurSIALE)getControleurCourant());
				getMissionCourant().getControleursSIALE().add(controleurSaisi);
			} else {
				Param controleurSaisi = (Param)getControleurCourantSaisi();
				for (Param controleur : getMissionCourant().getControleurs()) {
					if (controleur.getId().equals(controleurSaisi.getId())) {
						Messagebox.show("Ce contrôleur est déjà affectée");
						return;
					}
				}
				getMissionCourant().getControleurs().remove((Param)getControleurCourant());
				getMissionCourant().getControleurs().add(controleurSaisi);
			}
			
			//on enlève l'ancien
			getListeControleurControleurSIALECourant().remove(getControleurCourant());
			getListeControleurControleurSIALECourant().add(getControleurCourantSaisi());
			
			break;
		case SUPPRESSION:
			
			//maj de la liste
			getListeControleurControleurSIALECourant().remove(getControleurCourant());
			
			if (getControleurCourant() instanceof ControleurSIALE) {
				getMissionCourant().getControleursSIALE().remove((ControleurSIALE)getControleurCourant());
			} else {
				getMissionCourant().getControleurs().remove((Param)getControleurCourant());
			}

			break;
		default:
			break;
		}
		setActionControleur(Action.AUCUNE);
		binder.loadComponent(zoneSaisie);
	}

	
}
