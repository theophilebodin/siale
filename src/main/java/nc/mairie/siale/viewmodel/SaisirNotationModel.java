/**
 * 
 */
package nc.mairie.siale.viewmodel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nc.mairie.siale.domain.Bareme;
import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.Notation;
import nc.mairie.siale.domain.NoteCritere;
import nc.mairie.siale.domain.NoteGroupe;
import nc.mairie.siale.technique.ControleSaisie;
import nc.mairie.siale.technique.CurrentUser;
import nc.mairie.siale.technique.Nombre;
import nc.mairie.siale.technique.RisqueEtablissement;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;


/**
 * @author boulu72
 *
 */
@SuppressWarnings("deprecation")
public class SaisirNotationModel extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7035070390921105088L;

	AnnotateDataBinder binder;
	
	@Wire
	Window saisirNotation;
	
	Mission missionCourant;

	List<Bareme> listeBareme;
	
	Bareme baremeCourant;
	
	List<NoteGroupeNotation> listeNoteGroupeNotation;
	
	@Wire
	Combobox baremeListBox;
	
	private double noteGlobaleCalculee=0;
	private RisqueEtablissement niveauEtablissementCalcule;
			
		
		
	public static class NoteGroupeNotation implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8688470716186658955L;
		private NoteGroupe noteGroupe;
		private List<Notation> notations;
		
		public NoteGroupe getNoteGroupe() {
			return noteGroupe;
		}
		public void setNoteGroupe(NoteGroupe noteGroupe) {
			this.noteGroupe = noteGroupe;
		}
		public List<Notation> getNotations() {
			return notations;
		}
		public void setNotations(List<Notation> notations) {
			this.notations = notations;
		}
		
		//Calcule la moyenne pondérés des notes saisies pour le groupe
		public double getCalculeNoteGroupe() {
			double total = 0;
			double diviseur = 0;
			for (Notation notation : notations) {
				double note = notation.getNote();
				if (note >0 ) {
					total+=note*notation.getNoteCritere().getPonderation();
					diviseur+=notation.getNoteCritere().getPonderation();
				}
			}
			return diviseur == 0 ? 0 : Nombre.arrondir(total / diviseur,2);
		}
		
		public NoteGroupeNotation(NoteGroupe noteGroupe, List<Notation> notations) {
			super();
			this.noteGroupe = noteGroupe;
			this.notations = notations;
			
			Collections.sort(this.notations, new Comparator<Notation>() {

				@Override
				public int compare(Notation o1, Notation o2) {
					return o1.getNoteCritere().getNom().compareTo(o2.getNoteCritere().getNom());
				}
				
			});
			
			
		}
	}
	public List<NoteGroupeNotation> getListeNoteGroupeNotation() {
		return listeNoteGroupeNotation;
	}

	public void setListeNoteGroupeNotation(
			List<NoteGroupeNotation> listeNoteGroupeNotation) {
		this.listeNoteGroupeNotation = listeNoteGroupeNotation;
	}
	public Bareme getBaremeCourant() {
		return baremeCourant;
	}

	public void setBaremeCourant(Bareme baremeCourant) {
		this.baremeCourant = baremeCourant;
	}

	public Mission getMissionCourant() {
		return missionCourant;
	}
	
	public void setMissionCourant(Mission missionCourant) {
		this.missionCourant = missionCourant;
	}

	public List<Bareme> getListeBareme() {
		return listeBareme;
	}

	public void setListeBareme(List<Bareme> listeBareme) {
		this.listeBareme = listeBareme;
	}
	
	
	protected void initialiseAllListes() {
		
		listeBareme = Bareme.findAllBaremes();
		
		Collections.sort(listeBareme, new Comparator<Bareme>() {

			@Override
			public int compare(Bareme o1, Bareme o2) {
				return -1 * o1.getDateCreation().compareTo(o2.getDateCreation());
			}
		});
		
	}
	
	

	public void initialiseNotations() {
		
		//si le bareme change, RAZ des notations
		if (((long)baremeCourant.getId())!=missionCourant.getBareme().getId()) {
			missionCourant.getNotations().clear();
			missionCourant.setBareme(baremeCourant);
		}
		
		//si pas de notations, on les génère
		if (missionCourant.getNotations().size() ==0) {
			for (NoteGroupe noteGroupe : baremeCourant.getNoteGroupes()) {
				for (NoteCritere noteCritere : noteGroupe.getNoteCriteres()) {
					Notation notation = new Notation();
					notation.setMission(missionCourant);
					notation.setNoteCritere(noteCritere);
					missionCourant.getNotations().add(notation);
				}
			}
		}
		
		//on prépare la hashTable : en clé les note groupe, en valeur, la liste des notations
		Hashtable<NoteGroupe, List<Notation>> hash = new Hashtable<NoteGroupe, List<Notation>>();
		for (Notation notation : missionCourant.getNotations()) {
			NoteGroupe noteGroupe = notation.getNoteCritere().getNoteGroupe();
			List<Notation> liste = hash.get(noteGroupe);
			if (liste == null) {
				liste = new ArrayList<Notation>();
				hash.put(noteGroupe, liste);
			}
			liste.add(notation);
		}
		
		//parcours de la hashtable pour créer notre listeNoteGrouypeNotation
		listeNoteGroupeNotation = new ArrayList<SaisirNotationModel.NoteGroupeNotation>();
		Enumeration<NoteGroupe> enume = hash.keys();
		while (enume.hasMoreElements()) {
			NoteGroupe noteGroupe = enume.nextElement();
			NoteGroupeNotation ngn = new NoteGroupeNotation(noteGroupe, hash.get(noteGroupe));
			listeNoteGroupeNotation.add(ngn);
		}
		Collections.sort(listeNoteGroupeNotation, new Comparator<NoteGroupeNotation>(
				) {

					@Override
					public int compare(NoteGroupeNotation o1,
							NoteGroupeNotation o2) {
						return o1.getNoteGroupe().getNom().compareTo(o2.getNoteGroupe().getNom());
					}
		});
	}
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
	
		final Execution execution = Executions.getCurrent();
		Long id = (Long)execution.getArg().get("idMission");
		
		setMissionCourant(Mission.findMission(id));

		//Initialisetion des listes 
		initialiseAllListes();
		
		//Si la date de notation n'est pas renseignée, on le fait
		if (missionCourant.getDateNotation() == null) {
			missionCourant.setDateNotation(new Date());
		}
	
		
		//Si le bareme n'est pas défini, on prend le plus récent
		if (missionCourant.getBareme() == null) {
			missionCourant.setBareme(listeBareme.get(0));
		}
		
		setBaremeCourant(missionCourant.getBareme());
		
		initialiseNotations();

		
//		//test
//		for (NoteGroupeNotation noteGroupeNotation : listeNoteGroupeNotation) {
//			System.out.println(noteGroupeNotation.getNoteGroupe().getNom());
//			for (Notation notation : noteGroupeNotation.getNotations()) {
//				System.out.println("   "+notation.getNoteCritere().getNom());
//			}
//			
//		}
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

	}
	
	@Listen("onSelect = #baremeListBox")
	public void onSelect$baremeListBox() {
		initialiseNotations();
		binder.loadAll();
	}

	public void rappelGestionMission() {
		Component parent = saisirNotation.getParent();
		parent.getChildren().clear();
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("idMission", getMissionCourant().getId());
		
		//Window window= (Window) Executions.createComponents("/_missions/GestionMissions.zul", parent , args);
		Executions.createComponents("/_missions/GestionMissions.zul", parent , args);
	}
	
	
	@Listen("onClick = #annulerNotation;" +
			"onCancel= #saisirNotation")
	public void onClick$annulerNotation() {
	
//		//test
//		for (NoteGroupeNotation noteGroupeNotation : listeNoteGroupeNotation) {
//			System.out.println(noteGroupeNotation.getNoteGroupe().getNom());
//			for (Notation notation : noteGroupeNotation.getNotations()) {
//				System.out.println("   "+notation.getNoteCritere().getNom()+" : "+notation.getNote());
//			}
//			
//		}
		
		rappelGestionMission();
	
	}
	
	@Listen("onClick = #validerNotation")
	public void onClick$validerNotation() {
	
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(saisirNotation);
	
		if (getMissionCourant().getCloturee()) {
			getMissionCourant().controleCloturable(controleSaisie, saisirNotation.getFellow("missionClotureeCheckBox"));
		}
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
		
		missionCourant.setNoteEtablissement(noteGlobaleCalculee);
		missionCourant.setRisqueEtablissement(niveauEtablissementCalcule);
		
		setMissionCourant(missionCourant.merge());
		
		rappelGestionMission();
			
	}

	
	public double getCalculNoteGlobale () {
		double total = 0;
		double diviseur = 0;
		for (NoteGroupeNotation noteGroupeNotation : listeNoteGroupeNotation) {
			double note = noteGroupeNotation.getCalculeNoteGroupe();
			if (note >0 ) {
				total+=note*noteGroupeNotation.getNoteGroupe().getPonderation();
				diviseur+=noteGroupeNotation.getNoteGroupe().getPonderation();
			}
		}
		noteGlobaleCalculee = diviseur == 0 ? 0 : Nombre.arrondir(total / diviseur,2);
		return noteGlobaleCalculee;
	}
		
	
	public RisqueEtablissement calculNiveauEtablissement() {
		if (getMissionCourant().getSuiteDonnee().getNom().toUpperCase().startsWith("FERM")) {
			niveauEtablissementCalcule = RisqueEtablissement.MAJEUR;
		}
		else if (noteGlobaleCalculee < getBaremeCourant().getSeuilFaible()) niveauEtablissementCalcule = RisqueEtablissement.FAIBLE;
		else if (noteGlobaleCalculee < getBaremeCourant().getSeuilModere()) niveauEtablissementCalcule = RisqueEtablissement.MODERE;
		else if (noteGlobaleCalculee < getBaremeCourant().getSeuilEleve()) niveauEtablissementCalcule = RisqueEtablissement.ELEVE;
		else niveauEtablissementCalcule = RisqueEtablissement.TRES_ELEVE;
		
		return niveauEtablissementCalcule;
		  
	}
	
	public boolean isValiderDisabled() {
		//Si le user en cours ne faitpas parti de la liste des controleurs de la missiion, il ne peut enregistrer
		boolean peutSauver = false;
		
		if (getMissionCourant().getCloturee()) return true;
		
		for (ControleurSIALE controleurSIALE : getMissionCourant().getControleursSIALE()) {
			if (CurrentUser.getCurrentUser().getId().equals(controleurSIALE.getId())){
				peutSauver=true;
				break;
			}
		}
		return ! peutSauver;
	}
}
