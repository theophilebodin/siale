package nc.mairie.siale.technique;


import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;

public class BooleanConverterMVVM implements Converter {
    /**
     * Convert Boolean to String.
     * @param val date to be converted
     * @param comp associated component
     * @param ctx bind context for associate Binding and extra parameter (e.g. format)
     * @return the converted String
     */
	@Override
    public Object coerceToUi(Object val, Component comp, BindContext ctx) {
        final Boolean bool = (Boolean) val;
        return bool ? "OUI" :"NON";
    }
	     
    /**
     */
	@Override
    public Object coerceToBean(Object val, Component comp, BindContext ctx) {
		throw new UnsupportedOperationException();
    }
}