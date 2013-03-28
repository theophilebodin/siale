/**
 * 
 */
package nc.mairie.siale.viewmodel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nc.mairie.siale.domain.Param;
import nc.mairie.siale.domain.TypeParam;

import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;



/**
 * @author boulu72
 *
 */
public class GestionParametresModel extends GenericForwardComposer<Component> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6002244966600701488L;
	
	Listbox paramlb;
	Textbox paramtb;
	Checkbox paramActicCheckBox;
	
	AnnotateDataBinder binder;
	
	List<TypeParam> listeTypeParam;
	TypeParam typeParamCourant;
	
	List<Param> listeParam;
	Param paramCourant;
	
	public Param getParamCourant() {
		return paramCourant;
	}


	public void setParamCourant(Param paramCourant) {
		this.paramCourant = paramCourant;
	}


	public List<Param> getListeParam() {
		if (getTypeParamCourant() != null) {
			listeParam = Param.findParamsByTypeParam(getTypeParamCourant()).getResultList();
		} else listeParam = null;
		return listeParam;
	}


	public void setListeParam(List<Param> listeParam) {
		this.listeParam = listeParam;
	}


	public TypeParam getTypeParamCourant() {
		return typeParamCourant;
	}

	public void setTypeParamCourant(TypeParam typeParamCourant) {
		this.typeParamCourant = typeParamCourant;
//		List<Param> list = Param.findParamsByTypeParam(getTypeParamCourant()).getResultList();
//		setListeParam(list);
		setParamCourant(null);
		binder.loadAll();

	}


	public List<TypeParam> getListeTypeParam() {
		return listeTypeParam;
	}

	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId() + "Ctrl", this, true);
		
		listeTypeParam = TypeParam.findAllTypeParams();
		Collections.sort(listeTypeParam, new Comparator<TypeParam>() {

			@Override
			public int compare(TypeParam o1, TypeParam o2) {
				return o1.getNom().compareTo(o2.getNom());
			}
		});

		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

	}

	
	public void onClick$update(){
		
		
		if (paramCourant == null) {
			alert("Vous devez d'abotrd sélectionner une ligne");
			return;
		}
		
		paramCourant.setNom(getParamCourantNom());
		paramCourant.setActif(getParamCourantActif());
		
		
		//mise à jour
		int index = listeParam.indexOf(paramCourant);
		paramCourant = paramCourant.merge();
		listeParam.set(index, paramCourant);


		binder.loadAll();
				
	}
	
	public void onClick$add() {
		
		if (getTypeParamCourant() == null) {
			alert("Un type de paramètre doit être sélectionné");
			return;
		}
		
		//On vérifie qu'il n'existe pas déjà
		List<Param> listeParamByName = Param.findParamsByNomLike(getParamCourantNom()).getResultList();
		for (Param param : listeParamByName) {
			//si le même type param alors erreur
			if (param.getTypeParam().getId().equals(getTypeParamCourant().getId())) {
				alert("Ce nom existe déjà pour ce type de paramêtre");
				return;
			}
		}
		
		
		Param param = new Param();
		param.setNom(getParamCourantNom());
		param.setTypeParam(getTypeParamCourant());
		param.setActif(getParamCourantActif());
		
		param.persist();
		
		listeParam.add(param);
		paramCourant = param;
		
		binder.loadAll();
		
	}
	
	public void onClick$delete() {
		
	
		if (paramCourant == null) {
			alert("Rien de sélectionné");
			return;
		}
		
		paramCourant.remove();
		
		listeParam.remove(paramCourant);
		paramCourant = null;

		binder.loadAll();
	}

	private String getParamCourantNom() throws WrongValueException {
		String nom = paramtb.getValue();
		if (Strings.isBlank(nom)) {
			throw new WrongValueException(paramtb, "Ne doit pas être vide!");
		}
		return nom;
	}
	
	private boolean getParamCourantActif() throws WrongValueException {
		return paramActicCheckBox.isChecked();
	}
	
}
