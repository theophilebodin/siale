package nc.mairie.siale.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class TypeParam implements Cloneable{

    @NotNull
    @Size(min = 2, max = 20)
    private String nom;
    
    public TypeParam clone() throws CloneNotSupportedException {
    	return (TypeParam) super.clone();
    };
}
