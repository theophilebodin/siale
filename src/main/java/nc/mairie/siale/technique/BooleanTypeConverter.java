package nc.mairie.siale.technique;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;
import org.zkoss.zul.Listcell;

public class BooleanTypeConverter implements TypeConverter{

	@Override
	public Object coerceToBean(Object arg0, Component arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object coerceToUi(Object arg0, Component comp) {
//		Order order = (Order) val;
//		String info = order.getStatus()+" : "+
//				Consts.YYYY_MM_DD_hh_ss.format(order.getCreateDate());
//		return info;
		if (arg0 instanceof Boolean) {
			if (comp instanceof Listcell) {
				//((Listcell) comp).setImage(((Boolean)arg0).booleanValue() ? "/_images/OUI_24x24.png" : "/_images/NON_24x24.png");
				((Listcell) comp).setImage(((Boolean)arg0).booleanValue() ? "/_images/OUI_24x24.png" : "/_images/tiret_24x24.png");
				return null;
			} else {
				return ((Boolean)arg0).booleanValue() ? "Oui" : "Non";
			} 
		}
		return null;
	}
	
}
