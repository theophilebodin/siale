<%@page import="java.util.List"%>
<%@page import="nc.mairie.siale.domain.Param"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.zkoss.zk.ui.Sessions"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%


List<Param> listParam = (List<Param>)request.getSession().getAttribute("popol");
if (listParam == null ) {
	listParam = new ArrayList<Param>();
	request.getSession().setAttribute("popol", listParam);
}

nc.mairie.siale.domain.Param p1 = nc.mairie.siale.domain.Param.findParam(new Long(1));
nc.mairie.siale.domain.Param p2 = nc.mairie.siale.domain.Param.findParam(new Long(1));



listParam.add(p1);
listParam.add(p2);

System.out.println("-----------------------");
for (Param param : listParam) {
	System.out.println(param.toString()+ 
			"-------------PARAM hashcode :" +String.valueOf(param.hashCode())+
			"-------------TYPE_PARAM hashcode : "+param.getTypeParam().hashCode() + param.equals(listParam.get(0)));
}

%>

<select id="popol">

</select>

</body>

</html>