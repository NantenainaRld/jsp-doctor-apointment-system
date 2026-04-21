<html>
<%@ page import="com.doctorapointment.services.PatientService" %>
<body>
	<h2>Hello World!</h2>
	<%
	PatientService patSer = new PatientService();
    	
    	System.out.print(patSer.filterPatient("ss",null, null));
    %>
<p></p>
</body>
</html>
