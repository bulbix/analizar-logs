<%-- 
    Document   : inicio
    Created on : 25/07/2017, 10:23:01 AM
    Author     : acruzb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en" ng-app="myApp">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>Motor de búsqueda</title>
        <jsp:include page="../fragments/header.jsp" />
        <link href="<c:url value='/css/motor/inicio.css'/>" rel="stylesheet" type="text/css">
        <link href="<c:url value='/css/motor/loading.css'/>" rel="stylesheet" type="text/css">
    </head> 
    <body>
        <div class="container-fluid">
            <!--Loading section-->
            <div class="bg_load"></div>
            <div class="wrapper">
                <div class="inner">
                    <span>C</span>
                    <span>o</span>
                    <span>n</span>
                    <span>s</span>
                    <span>u</span>
                    <span>l</span>
                    <span>t</span>
                    <span>a</span>
                    <span>n</span>
                    <span>d</span>
                    <span>o</span>
                </div>
            </div>
            <!--End loading section-->
            <jsp:include page="parts/head.jsp" />
            <section id="seleccion-informacion-general" class="container" ng-controller="InformacionGeneralController">
                <div class="row" ng-switch on="archivoActual">
                    <h4 ng-switch-when="null">No se ha seleccionado ningún archivo</h4>
                    <h4 ng-switch-default="">Información general del arcivo seleccionado</h4>
                </div>
                <div class="row" ng-show="null !== archivoActual" class="ng-hide" ng-init="fnInitArchivo('${ArchivoFTP.nombre}', '${ArchivoFTP.tamano}', '${ArchivoFTP.fechaCreacion}')">
                    <div class="col-md-12">
                        <div class="row">
                            <p class="col-sm-2">Nombre</p>
                            <div class="col-sm-4">
                                <label>{{archivoActual.nombre}}</label>
                            </div>
                            <p class="col-sm-2 control-label">Tamaño</p>
                            <div class="col-sm-4">
                                <label>{{archivoActual.tamano}}</label>
                            </div>
                        </div>
                        <div class="row">
                            <p class="col-sm-2 control-label">Fecha de creación</p>
                            <div class="col-sm-10">
                                <label>{{archivoActual.fechaCreacion}}</label>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Modal seleccion de archivo -->
                <div class="modal fade" id="modalSeleccionArchivo" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabel">Seleccionar el archivo a procesar</h4>
                            </div>
                            <div class="modal-body" >
                                <table class="table table-bordered">
                                    <thead>
                                        <tr><th>Nombre</th><th>Fecha de creación</th><th>Tamaño</th></tr>
                                    </thead>
                                    <tbody>
                                        <tr ng-repeat="row in archivosHD">
                                            <th scope="row"><span ng-bind="row.nombre"></span></th>
                                            <td><span ng-bind="row.fechaCreacion"></span></td>
                                            <td><span ng-bind="row.tamano"></span></td>
                                            <td><button type="button" class="btn btn-primary btn-sm" ng-click="fnEstablecerArchivo(row)">Seleccionar</button></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section id="seleccion-busqueda" class="container" ng-controller="BusquedasController">
                <div class="panel panel-primary">
                    <div class="panel-heading">Formulario de búsqueda</div>
                    <div  class="panel-body" style="min-height: 250px; max-height: 500px; overflow-y: auto;overflow-x: hidden;">
                        <jsp:include page="parts/partUsuarios.jsp" />   
                        <jsp:include page="parts/partGeneral.jsp" />   
                    </div>
                </div>
            </section>

            <section id="seleccion-resultado" class="container">
                <div class="panel panel-primary">
                    <div class="panel-heading">Resultado de la búsqueda</div>
                    <div  class="panel-body" style="min-height: 300px; max-height: 450px;">
                        <pre id="textareaResultado"></pre>
                    </div>
                </div>
            </section>
            <jsp:include page="../fragments/footer.jsp" />
            <script type="text/javascript" src="<c:url value="/js/motor/inicio.js"/>"></script>
        </div>
    </body>
</html>