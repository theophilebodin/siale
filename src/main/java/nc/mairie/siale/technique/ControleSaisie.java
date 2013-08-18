package nc.mairie.siale.technique;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.impl.InputElement;

public class ControleSaisie {

//	public static boolean controlerSaisie(Component c) {
//	boolean controleOK = true;
//	for (Component component : c.getChildren()) {
//		if (component.isVisible()) {
//			if(component instanceof InputElement) {
//                if (!((InputElement) component).isValid()) {
//                	controleOK = false;
//                    // Force show errorMessage
//                	try {
//                		((InputElement) component).getConstraint().validate(component, ((InputElement) component).getRawValue());
//                	} catch (Exception e) {
//                		((InputElement) component).setErrorMessage(e.getMessage());
//                	}
//                	
//                  }
//			} else {
//				controleOK = controlerSaisie(component) && controleOK;
//			}
//		}
//	}
//	return controleOK;
//}
	
	
	List<WrongValueException> listeErreurs ;
	
	public ControleSaisie() {
		listeErreurs=new ArrayList<WrongValueException>();
	}
	
	public ControleSaisie(Component c) {
		//dès l'instanciation, on controle la saisie
		listeErreurs = controlerSaisie(c);
	}

	public List<WrongValueException> controlerSaisie(Component c) {
		List<WrongValueException> listeErreurs = new ArrayList<WrongValueException>();
		for (Component component : c.getChildren()) {
			if (component.isVisible()) {
				if(component instanceof InputElement) {
	                //if (!((InputElement) component).isValid()) {
					InputElement element = (InputElement) component;
					Constraint constraint = element.getConstraint();
					if (constraint != null) {
	                    // Force show errorMessage
	                	try {
	                		constraint.validate(component, element.getRawValue());
	                	} catch (Exception e) {
	                		listeErreurs.add(new WrongValueException(component, e.getMessage()));
	                	}
					}
	                  //}
				} else {
					listeErreurs.addAll(controlerSaisie(component));
				}
				
			}
		}
		return listeErreurs;
	}

	public void ajouteErreur(Component component, String message) {
		listeErreurs.add(new WrongValueException(component, message));
	}
	
	public void afficheErreursSilYEnA() {
		if(!listeErreurs.isEmpty()) {
			throw new WrongValuesException((WrongValueException[])listeErreurs.toArray(new WrongValueException[listeErreurs.size()]));
		}
	}
}
