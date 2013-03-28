package nc.mairie.siale.technique;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

public class BooleanTypeConverter implements TypeConverter{

	@Override
	public Object coerceToBean(Object arg0, Component arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object coerceToUi(Object arg0, Component arg1) {
//		Order order = (Order) val;
//		String info = order.getStatus()+" : "+
//				Consts.YYYY_MM_DD_hh_ss.format(order.getCreateDate());
//		return info;
		if (arg0 instanceof Boolean) {
			return ((Boolean)arg0).booleanValue() ? "Oui" : "Non"; 
		}
		return null;
	}
	
}
