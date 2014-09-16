<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link href="<c:url value="/main.css"/>" rel="stylesheet" type="text/css" />
    <title>Faculty Information System Web Service Control Panel</title>
</head>
<body>
<div id="container">

    <ul id="mainlinks">
        <li><a href="<c:url value="/index.jsp"/>" class="selected">Home</a></li>
    </ul>

    <div id="content">
        <h1>Faculty Information System ORCID Web Service Control Panel</h1>

        <div>
            <ul>
                <li><a href="<c:url value="/index.jsp"/>">Home</a></li>
                <li><a href="<c:url value="/orcid/register"/>" class="selected">Register faculty for ORCIDs</a></li>
                <li><a href="<c:url value="/orcid/poll"/>" class="selected">Poll ORCID registry</a></li>
            </ul>
        </div>

        <p class="footer">&copy;2014 Regents of the University of Colorado</p>
    </div>
</div>
</body>
</html>