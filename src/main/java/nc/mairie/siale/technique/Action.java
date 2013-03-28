package nc.mairie.siale.technique;

public enum Action {
	AJOUT, MODIFICATION, SUPPRESSION, ANNULATION, VALIDATION, AUCUNE, CONSULTATION;
	
//	@Override
//	public String toString() {
//		switch (this) {
//		case CREATION:
//			return "Creation";
//		case MODIFICATION:
//			return "Modification";
//		case SUPPRESSION:
//			return "Suppression";
//		case ANNULATION:
//			return "Annulation";
//		case VALIDATION:
//			return "Validation";
//		case AUCUNE:
//			return "Aucune";
//
//		default:
//			return "Action non définie";
//		}
//	}
	
	public boolean isAucune() {
		return this == AUCUNE;
	}
	
	public boolean isSuppression() {
		return this == SUPPRESSION;
	}
	public boolean isModification() {
		return this == MODIFICATION;
	}
	
	public boolean isEnCours() {
		return this == AJOUT || this == MODIFICATION || this == SUPPRESSION || this == CONSULTATION; 
	}
}
