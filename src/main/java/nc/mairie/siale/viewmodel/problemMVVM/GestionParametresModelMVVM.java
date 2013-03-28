/**
 * 
 */
package nc.mairie.siale.viewmodel.problemMVVM;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nc.mairie.siale.domain.Param;
import nc.mairie.siale.domain.TypeParam;
import nc.mairie.siale.technique.TypeEtablissement;

import org.hibernate.validator.util.privilegedactions.SetAccessibility;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Messagebox;

/**
 * @author boulu72
 *
 */
public class GestionParametresModelMVVM {

	
	private List<TypeParam> listeTypeParam;
	TypeParam typeParamCourant;
	
	List<TypeEtablissement> listeTypeEtablissement;
	
	List<Param> listeParam;
	Param paramCourant;
	Param nouveauParam;

	public List<TypeEtablissement> getListeTypeEtablissement() {
		return listeTypeEtablissement;
	}

	public void setListeTypeEtablissement(
			List<TypeEtablissement> listeTypeEtablissement) {
		this.listeTypeEtablissement = listeTypeEtablissement;
	}

	public Param getNouveauParam() {
		if (nouveauParam==null) nouveauParam = new Param();
		return nouveauParam;
	}

	public void setNouveauParam(Param nouveauParam) {
		this.nouveauParam = nouveauParam;
	}

	public TypeParam getTypeParamCourant() {
		return typeParamCourant;
	}

	public void setTypeParamCourant(TypeParam typeParamCourant) {
		this.typeParamCourant = typeParamCourant;
	}

	
	public List<TypeParam> getListeTypeParam() {
		return listeTypeParam;
	}

	public void setListeTypeParam(List<TypeParam> listeTypeParam) {
		this.listeTypeParam = listeTypeParam;
	}

	public List<Param> getListeParam() {
		return listeParam;
	}

	public void setListeParam(List<Param> listeParam) {
		this.listeParam = listeParam;
	}

	public Param getParamCourant() {
		if (paramCourant == null) {
			paramCourant = new Param();
		}
		return paramCourant;
	}

	public void setParamCourant(Param paramCourant) {
		this.paramCourant = paramCourant;
	}

	@Command
	@NotifyChange("listeParam")
	public void typeParamSelected() {
		listeParam = Param.findParamsByTypeParam(getTypeParamCourant()).getResultList();
		paramCourant = null;
	}
	
	@Init
	@NotifyChange("listeTypeParam")
	public void init() {
		listeTypeParam = TypeParam.findAllTypeParams();
		
		Collections.sort(listeTypeParam, new Comparator<TypeParam>() {

			@Override
			public int compare(TypeParam o1, TypeParam o2) {
				return o1.getNom().compareTo(o2.getNom());
			}
		});

	}
	
	@Command
	@NotifyChange({"listeParam","paramCourant"})
	public void updatePressed(){
		
		if (paramCourant == null) {
			Messagebox.show("Vous devez d'abotrd sélectionner une ligne");
			return;
		}

		//mise à jour
		int index = listeParam.indexOf(paramCourant);
		paramCourant = paramCourant.merge();
		listeParam.set(index, paramCourant);

	}
	
	@Command
	@NotifyChange({"listeParam","paramCourant"})
	public void addPressed() {
		
		if (getTypeParamCourant() == null) {
			Messagebox.show("Le type de paramètre est obligatoire");
			return;
			//alert("Un type de paramètre doit être sélectionné");
		}
		

		
		if (nouveauParam == null || Strings.isBlank(nouveauParam.getNom())) {
			Messagebox.show("Le nom du paramètre est obligatoire");
			return;
		}
		
		
		//On vérifie qu'il n'existe pas déjà
		List<Param> listeParamByName = Param.findParamsByNomLike(nouveauParam.getNom()).getResultList();
		for (Param param : listeParamByName) {
			//si le même type param alors erreur
			if (param.getNom().equals(nouveauParam.getNom()) && param.getId().equals(getTypeParamCourant().getId())) {
				Messagebox.show("Ce nom existe déjà pour ce type de paramêtre");
				return;
			}
		}
		
		//on checke si actif est sélectionné
		if (nouveauParam.getActif() == null) {
			nouveauParam.setActif(false);
		}
		
		nouveauParam.setTypeParam(getTypeParamCourant());
		
		nouveauParam.persist();
		listeParam.add(nouveauParam);
		paramCourant = nouveauParam;

		
	}

	@Command
	@NotifyChange({"listeParam","paramCourant"})
	public void deletePressed() {

		if (paramCourant == null) {
			Messagebox.show("Aucun élément de la liste n'a été sélectionné");
			return;
		}
	
		
		paramCourant.remove();
		
		listeParam.remove(paramCourant);
		
		paramCourant=null;
		
	}

	
}
