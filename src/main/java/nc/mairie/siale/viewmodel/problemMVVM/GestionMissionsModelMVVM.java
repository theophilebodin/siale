/**
 * 
 */
package nc.mairie.siale.viewmodel.problemMVVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Etablissement;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.MissionActivite;
import nc.mairie.siale.domain.Param;
import nc.mairie.siale.technique.Action;
import nc.mairie.siale.technique.CurrentUser;
import nc.mairie.siale.technique.TypeEtablissement;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;



/**
 * @author boulu72
 *
 */
public class GestionMissionsModelMVVM {
	
	List<Mission> listeMission;
	Mission missionCourant;
	
	List<Etablissement> listeEtablissement;
	List<Etablissement> listeEtablissementListBox;
	Etablissement etablissementCourant;
	
	ListModelList<TypeEtablissement> listeTypeEtablissement = new ListModelList<TypeEtablissement>(nc.mairie.siale.technique.TypeEtablissement.values());

	String etablissementSaisi;
	
	MissionActivite missionActiviteCourant;

	List<Param> listeActivite;

	Action actionMission = Action.AUCUNE;
	
	Action actionActivite = Action.AUCUNE;

	public Action getActionMission() {
		return actionMission;
	}

	public void setActionMission(Action actionMission) {
		this.actionMission = actionMission;
	}

	public Action getActionActivite() {
		return actionActivite;
	}

	public List<Param> getListeActivite() {
		return listeActivite;
	}

	public void setListeActivite(List<Param> listeActivite) {
		this.listeActivite = listeActivite;
	}

	public MissionActivite getMissionActiviteCourant() {
		return missionActiviteCourant;
	}

	
	public void setMissionActiviteCourant(MissionActivite missionActiviteCourant) {
		this.missionActiviteCourant = missionActiviteCourant;
	}

	public String getEtablissementSaisi() {
		return etablissementSaisi;
	}

	@NotifyChange("listeEtablissementListBox")
	public void setEtablissementSaisi(String etablissementSaisi) {
		this.etablissementSaisi = etablissementSaisi;
		onChangingEtablissement(etablissementSaisi);
	}

	public ListModelList<TypeEtablissement> getListeTypeEtablissement() {
		return listeTypeEtablissement;
	}

	public void setListeTypeEtablissement(
			ListModelList<TypeEtablissement> listeTypeEtablissement) {
		this.listeTypeEtablissement = listeTypeEtablissement;
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
		
	}

	public List<Etablissement> getListeEtablissementListBox() {
		return listeEtablissementListBox;
	}

	public void setListeEtablissementListBox(
			List<Etablissement> listeEtablissementListBox) {
		this.listeEtablissementListBox = listeEtablissementListBox;
	}

	public Etablissement getEtablissementCourant() {
		return etablissementCourant;
	}

	public void setEtablissementCourant(Etablissement etablissementCourant) {
		this.etablissementCourant = etablissementCourant;
	}

	public List<Mission> getListeMission() {
		return listeMission;
	}

	public void setListeMission(List<Mission> listeMission) {
		this.listeMission = listeMission;
	}
	
		protected void initialiseAllListes() {
		listeMission=initialiseListeMissions();
		listeEtablissement=Etablissement.findAllEtablissements();
		listeEtablissementListBox=listeEtablissement;
		listeActivite = Param.findParamsActifsByNomDuTypeParam("ACTIVITE").getResultList();
		Collections.sort(listeActivite, new Comparator<Param>() {
			@Override
			public int compare(Param o1, Param o2) {
				return o1.getNom().compareTo(o2.getNom());
			}
		});
		
	}

	
	protected List<Mission> initialiseListeMissions() {
		List<Mission> uneListe;
		if (CurrentUser.getCurrentUser().isAdmin()) {
			uneListe = Mission.findAllMissions();
		} else {
			Set<ControleurSIALE> setControleurs = new HashSet<ControleurSIALE>();
			setControleurs.add(CurrentUser.getCurrentUser());
			uneListe = Mission.findMissionsByControleursSIALE(setControleurs).getResultList();
		}
		return uneListe;
	}
	
	@Init
	public void init() {
		initialiseAllListes();
	}

	@NotifyChange("listeEtablissementListBox")
	public void onChangingEtablissement(String value) {
 		List<Etablissement> newModel = new ArrayList<Etablissement>();
 		
 		for (Etablissement etab : listeEtablissement) {
			if (etab.getLibelle().toUpperCase().contains(value.toUpperCase())) {
 				newModel.add(etab);
 			}
 		}
 		
 		listeEtablissementListBox = newModel;
 		 		
	}
	
	@Command
	@NotifyChange("actionMission")
	public void modifierMission() {
		actionMission=Action.MODIFICATION;
	}

	@Command
	@NotifyChange("actionMission")
	public void supprimerMission() {
		actionMission=Action.MODIFICATION;
	}
	
	@Command
	@NotifyChange({"actionMission","missionCourant"})
	public void ajouterMission() {
		actionMission = Action.AJOUT;
		
		Mission mission = new Mission();
		mission.setEtablissement(new Etablissement());
		missionCourant=mission;
		//on sélectionne Etablissement
		//setTypeEtablissementCourant(TypeEtablissement.ETABLISSEMENT);
		
	}
	
	@Command
	@NotifyChange({"listeMission","missionCourant","actionMission"})
	public void annulerMission() {
		missionCourant = Mission.findMission(getMissionCourant().getId());
		
		actionMission = Action.AUCUNE;
		initialiseAllListes();
	}

	@Command
	@NotifyChange({"listeMission","missionCourant","actionMission"})
	public void validerMission() {
		
		switch (actionMission) {
		case AJOUT:
			//Controles
			
			//SGBD
			missionCourant.setDatePrevue(new Date());
			missionCourant.persist();
			
			//VIEW
			
			break;
		case MODIFICATION:
			//Controles
			
			//SGBD
			missionCourant = missionCourant.merge();
			
			//VIEW
			
			break;
		case SUPPRESSION:
			//Controles
			
			//SGBD
			missionCourant.remove();
			
			//VIEW
			missionCourant=null;
			break;

		default:
			break;
		}
		
		
		initialiseAllListes();
		actionMission = Action.AUCUNE;
	}
	
	

	@Command
	@NotifyChange({"missionActiviteCourant","actionActivite"})
	public void removeMissionActivite(@BindingParam("missionActivite") MissionActivite missionActivite) {
		if (missionActivite.getPrincipale()) {
			Messagebox.show("Impossible de supprimer l'activite principale");
			return;
		}
		actionActivite = Action.SUPPRESSION;
		
	}
	
	@Command
	@NotifyChange({"missionActiviteCourant","actionActivite"})
	public void updateMissionActivite(@BindingParam("missionActivite") MissionActivite missionActivite) {
		actionActivite = Action.MODIFICATION;
	}
	
	@Command
	@NotifyChange({"missionActiviteCourant","actionActivite"})
	public void addMissionActivite() {
		actionActivite = Action.AJOUT;
		MissionActivite nouv = new MissionActivite();
		nouv.setTheMission(getMissionCourant());
		setMissionActiviteCourant(nouv);
	}
	
	@Command
//	@NotifyChange({"actionActivite","missionActiviteCourant","missionCourant.missionActivites"})
	@NotifyChange({"actionActivite","missionActiviteCourant","missionCourant"})
	public void saveMissionActivite() {
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
					if (!missionActivite.getId().equals(getMissionActiviteCourant().getId())) {
						missionActivite.setPrincipale(false);
					}
				}
			}

//			//on persiste
//			getMissionActiviteCourant().persist();
			
			//mise à jour de la liste
			getMissionCourant().getMissionActivites().add(getMissionActiviteCourant());
			
			//maj de l'action
			actionActivite= Action.AUCUNE;
			
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
					if (!missionActivite.getId().equals(getMissionActiviteCourant().getId())) {
						missionActivite.setPrincipale(false);
					}
				}	
			}

			//maj de l'action
			actionActivite= Action.AUCUNE;
			
			break;
		case SUPPRESSION:
			
			//test que pas la principale
			if (getMissionActiviteCourant().getPrincipale()) {
				Messagebox.show("Impossible de supprimer l'activite principale");
				return;
			}
			
			//mise à jour de la liste
			if (!getMissionCourant().getMissionActivites().remove(getMissionActiviteCourant())) {
				throw new WrongValueException("problème en supprimant");
			}
			
			//on persiste
			//getMissionActiviteCourant().remove();
			
			setMissionActiviteCourant(null);
			
			//maj de l'action
			actionActivite= Action.AUCUNE;
			
			break;
		default:
			break;
		}

	}
	
	@Command
	@NotifyChange({"actionActivite","missionActiviteCourant"})
	public void cancelMissionActivite() {
		

		
		actionActivite=Action.AUCUNE;
		
	}
	

}
