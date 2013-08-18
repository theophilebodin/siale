package nc.mairie.siale.technique;

public class Nombre {
	static public double arrondir(double value, int nbDecimal) {
		double r = (Math.round(value * Math.pow(10, nbDecimal))) / (Math.pow(10, nbDecimal));
		return r;
	} 
}
