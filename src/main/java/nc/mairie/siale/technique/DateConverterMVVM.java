package nc.mairie.siale.technique;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;

@SuppressWarnings("rawtypes")
public class DateConverterMVVM implements Converter {
	    /**
	     * Convert Date to String.
	     * @param val date to be converted
	     * @param comp associated component
	     * @param ctx bind context for associate Binding and extra parameter (e.g. format)
	     * @return the converted String
	     */
	@Override
	    public Object coerceToUi(Object val, Component comp, BindContext ctx) {
	        //user sets format in annotation of binding or args when calling binder.addPropertyBinding() 
	        final String format = (String) ctx.getConverterArg("format");
	        if(format==null) throw new NullPointerException("format attribute not found");
	        final Date date = (Date) val;
	        return date == null ? null : new SimpleDateFormat(format).format(date);
	    }
	     
	    /**
	     * Convert String to Date.
	     * @param val date in string form
	     * @param comp associated component
	     * @param ctx bind context for associate Binding and extra parameter (e.g. format)
	     * @return the converted Date
	     */
	@Override
	    public Object coerceToBean(Object val, Component comp, BindContext ctx) {
	        final String format = (String) ctx.getConverterArg("format");
	        if(format==null) throw new NullPointerException("format attribute not found");
	        final String date = (String) val;
	        try {
	            return date == null ? null : new SimpleDateFormat(format).parse(date);
	        } catch (ParseException e) {
	            throw UiException.Aide.wrap(e);
	        }
	    }
	}