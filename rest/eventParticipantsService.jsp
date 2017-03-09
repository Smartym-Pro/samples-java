<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>RepTracker REST service</title>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.7.min.js"></script>
    <script type="text/javascript" src="http://jsonlint.com/c/js/jsl.parser.js"></script>
    <script type="text/javascript" src="http://jsonlint.com/c/js/jsl.format.js"></script>
    <script type="text/javascript" src="http://datejs.googlecode.com/files/date.js"></script>
    <script type="text/javascript" src="http://crypto-js.googlecode.com/files/2.5.3-crypto-sha1.js"></script>
</head>
<body>

<c:import url="include/header.jsp"/>
<c:import url="include/scripts.jsp"/>
<h2 align="center">EventParticipants Service</h2>
<c:import url="include/serviceTableHeader.jsp"/>
<table align="center" border="1px">
    <tr>
        <th width="100">API method</th>
        <th width="300">Parameters</th>
        <th width="600">Response</th>
    </tr>

    <tr style="vertical-align: top; text-align: center;">
        <td>Get EventParticipants<sup>
            <small>(s)</small>
        </sup></td>
        <td>
            <form action="<c:url value='/services/eventparticipants/get'/>" method="GET"
                  target="getEventParticipantsResponse"
                  onsubmit="appendApiSig(this)">
                <input name="signature" type="hidden">
                <input name="auth_id" type="hidden">
                <input name="opaque" type="hidden">
                <table>
                    <tr>
                        <td>User ID *:</td>
                        <td><input name="userId" type="text" value="123"/></td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Go!"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
        <td>
            <iframe name="getEventParticipantsResponse" id="getEventParticipantsResponse" width="100%"
                    height="100%"></iframe>
        </td>
    </tr>

    <tr style="vertical-align: top; text-align: center;">
        <td>Update EventParticipants<sup>
            <small>(s)</small>
        </sup></td>
        <td>
            <form action="<c:url value='/services/eventparticipants/update'/>" method="POST"
                  target="updateEventParticipantResponse"
                  onsubmit="appendApiSig(this)">
                <input name="signature" type="hidden">
                <input name="auth_id" type="hidden">
                <input name="opaque" type="hidden">
                <table>
                    <tr>
                        <td>Tracking Code *:</td>
                        <td><input name="trackingCode" type="text" value="123"/></td>
                    </tr>
                    <tr>
                        <td>ExternalUser Identifier *:</td>
                        <td><input name="externalUserIdentifier" type="text" value="123"/></td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Go!"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
        <td>
            <iframe name="updateEventParticipantResponse" id="updateEventParticipantResponse" width="100%"
                    height="100%"></iframe>
        </td>
    </tr>

</table>

<script type="text/javascript">
    function appendPasswordHash(form) {
        var mail = form.elements['email'].value;
        var pass = form.elements['password'].value;
        form.elements['passwordHash'].value = Sha1.hash(pass + '{' + mail + '}');
    }
</script>

</body>
</html>