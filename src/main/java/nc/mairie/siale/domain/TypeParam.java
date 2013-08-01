package nc.mairie.siale.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "typeparam_sequence")
public class TypeParam implements Cloneable, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5461220011752406279L;
	@NotNull
    @Size(min = 2, max = 20)
    private String nom;
    
    public TypeParam clone() throws CloneNotSupportedException {
    	return (TypeParam) super.clone();
    };
}
