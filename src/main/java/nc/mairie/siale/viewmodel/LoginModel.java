package nc.mairie.siale.viewmodel;

import java.util.List;

import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.technique.Constantes;
import nc.mairie.siale.technique.CurrentUser;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class LoginModel extends SelectorComposer<Component>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6776249026354121693L;
		
	AnnotateDataBinder binder;

	@Wire
	Window login;
	
	public LoginModel(){
		super();
	}
	
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		comp.setAttribute(comp.getId(), this, true);
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
		 
	}
	
	@Listen("onClick = #validerLogin;" +
			"onOK = #login")
	public void onClick$validerLogin() {
		String user = ((Textbox)login.getFellow("usertb")).getValue();
		String pwd = ((Textbox)login.getFellow("pwdtb")).getValue();
		
		
		boolean authOK = nc.mairie.siale.technique.LDAP.controlerHabilitation(user,pwd);
		ControleurSIALE controleurSIALE;
		
		
		//Vérif du mot de passe avec l'AD
		if (!authOK) {
			Messagebox.show("Le user ou mot de passe est invalide!","Erreur",Messagebox.OK,Messagebox.ERROR);
			return;
		}
		
		//Si ADMINWAS
		if (user.toUpperCase().equals("ADMINWAS")) {
			controleurSIALE = new ControleurSIALE();
			controleurSIALE.setId(new Long(99999999));
			controleurSIALE.getDroits().add(Constantes.droitAdmin);
			controleurSIALE.getDroits().add(Constantes.droitControleur);
			controleurSIALE.setUsername(user);
			controleurSIALE.setDisplayname(user);
		} else {
		
			//Recherche du controleur siale
			List <ControleurSIALE> list = ControleurSIALE.findControleurSIALEsByUsernameEquals(user).getResultList();
			//username unique dans l'ad, donc on prend le 1er élément (le seul donc...)
			if (list.size() == 0) {
				Messagebox.show("Utilisateur non habilité. Demander à l'administrateur SIALE de vous rajouter.","Erreur",Messagebox.OK,Messagebox.ERROR);
				return;
			}
			
			controleurSIALE = list.get(0);
		}
		CurrentUser.setCurrentUser(controleurSIALE);

		((Div)login.getFellow("loginDiv")).setVisible(false);
		((Div)login.getFellow("userDiv")).setVisible(true);
		((Label)login.getFellow("userName")).setValue(controleurSIALE.getDisplayname());

		//On redirige sur l'accueil (authentifié ce coup ci
		Executions.sendRedirect("/");
	}

}
