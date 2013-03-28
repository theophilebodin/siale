package nc.mairie.siale.technique;

public enum TypeEtablissement {
	ETABLISSEMENT, NONDECLARE, PROJET;
	
	@Override
	public String toString() {
		switch (this) {
		case ETABLISSEMENT:
			return "Etablissement";
		case NONDECLARE:
			return "Non déclaré";
		case PROJET:
			return "Projet";
		
		default:
			return null;
		}
	}
	
}
