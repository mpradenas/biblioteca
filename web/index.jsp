<%-- 
    Document   : index
    Created on : Aug 17, 2017, 3:27:55 PM
    Author     : mario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

 <%@include file="templates/header.jsp"%>
 
       <div class="container" style="margin-top:50px">
            <h2>Cliente</h2>
            <form>
              <div class="form-group">
                  <label for="nombre" class="label">Nombre</label>
                <input type="text" class="form-control" id="nombre" placeholder="nombre">
              </div>
              <button type="button" id="guarda"class="btn btn-primary">Guardar</button>
              <button type="button" class="btn btn-default" onclick="cancelar();" style="display:none" >Cancelar</button>
           
            </form>
            
            
       </div>
 
 <script type="text/javascript" src="javascript/cliente.js?2"></script>    
 <%@include file="templates/footer.jsp"%>
