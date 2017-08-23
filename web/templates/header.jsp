<%-- 
    Document   : header
    Created on : Aug 18, 2017, 12:45:40 PM
    Author     : mario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>probando bootstrap</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
          <%--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">--%>
          <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css">
          <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-theme.css">
       
          
          <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">--%>
          <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery-3.2.1.js?2"></script>
    </head>
    <body>
        
        <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container-fluid">
          <div class="navbar-header">
            <a class="navbar-brand" href="#">Biblioteca</a>
          </div>
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#">Clientes</a></li>
            <li><a href="#">Libros</a></li>
            <li><a href="#">Préstamo de libros</a></li>
          </ul>
        </div>
      </nav>

