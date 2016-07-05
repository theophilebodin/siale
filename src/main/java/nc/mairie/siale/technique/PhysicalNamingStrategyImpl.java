package nc.mairie.siale.technique;

import java.io.Serializable;
import java.util.Locale;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PhysicalNamingStrategyImpl  extends PhysicalNamingStrategyStandardImpl implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 8709553485374978784L;
public static final PhysicalNamingStrategyImpl  INSTANCE = new PhysicalNamingStrategyImpl();

@Override
public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
    return new Identifier(addUnderscores(name.getText()), name.isQuoted());
}

@Override
public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
	int pos_id = name.getText().lastIndexOf("_id");
	String columnName = pos_id == -1 ? name.getText() : name.getText().substring(0, pos_id);
	return new Identifier(addUnderscores(columnName), name.isQuoted());
}


protected static String addUnderscores(String name) {
    final StringBuilder buf = new StringBuilder( name.replace('.', '_') );
    for (int i=1; i<buf.length()-1; i++) {
        if (
            Character.isLowerCase( buf.charAt(i-1) ) &&
            Character.isUpperCase( buf.charAt(i) ) &&
            Character.isLowerCase( buf.charAt(i+1) )
        ) {
            buf.insert(i++, '_');
        }
    }
    return buf.toString().toLowerCase(Locale.ROOT);
}
}