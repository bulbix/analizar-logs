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
            <section id="seleccion-informacion-general" class="container-fluid" ng-controller="InformacionGeneralController">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="row" ng-switch on="archivoActual">
                            <div class="col-sm-12">
                                <h3 ng-switch-when="null">No se ha seleccionado ningún archivo</h3>
                            </div>
                        </div>
                        <div class="row" ng-show="null !== archivoActual" class="ng-hide" ng-init="fnInitArchivo('${ArchivoFTP.nombre}', '${ArchivoFTP.tamano}', '${ArchivoFTP.fechaCreacion}')">
                            <div class="col-md-12">
                                <div class="row">
                                    <div class="col-sm-1">Nombre</div>
                                    <div class="col-sm-3">
                                        <label>{{archivoActual.nombre}}</label>
                                    </div>
                                    <div class="col-sm-1 control-label">Tamaño</div>
                                    <div class="col-sm-2">
                                        <label>{{archivoActual.tamano}}</label>
                                    </div>
                                    <div class="col-sm-2 control-label">Fecha de creación</div>
                                    <div class="col-sm-3">
                                        <label>{{archivoActual.fechaCreacion}}</label>
                                    </div>
                                </div>
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
                                <div class="row" style="padding-bottom: 15px;">
                                    <div class="col-md-2"><label>Carpeta</label></div>
                                    <div class="col-md-6">
                                        <input id="toggle-carpeta-log" type="checkbox" ng-checked="archivosHDCarpeta" data-toggle="toggle" data-width="150" data-onstyle="success" data-offstyle="info" data-on="<i class='glyphicon glyphicon-sort-by-alphabet'></i> BAZ" data-off="<i class='glyphicon glyphicon-sort-by-attributes'></i> JVC">
                                    </div>
                                </div>
                                <table class="table table-bordered">
                                    <thead>
                                        <tr><th>Nombre</th><th>Fecha de creación</th><th>Tamaño</th></tr>
                                    </thead>
                                    <tbody ng-init="fnServidoresFTPInit('${FTPServers}')">
                                        <tr ng-show="!archivosHD.length > 0">
                                            <th scope="row"><span>No se encontraton archivos a mostrar</span></th>
                                        </tr>
                                        <tr ng-repeat="row in archivosHD">
                                            <th scope="row"><span ng-bind="row.nombre"></span></th>
                                            <td><span ng-bind="row.fechaCreacion"></span></td>
                                            <td><span ng-bind="row.tamano"></span></td>
                                            <td><button type="button" class="btn btn-primary btn-sm" ng-click="fnEstablecerArchivo(row)">Seleccionar</button></td>
                                            <td><button type="button" class="btn btn-default btn-sm" ng-click="fnEliminarArchivo(row)">Eliminar</button></td>
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
                
                <!-- Modal archivos ftp-->
                <div class="modal fade" id="modalArchivosFTP" tabindex="-1" role="dialog" aria-labelledby="myModalLabelFTP">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabelFTP">Archivos de servidor FTP</h4>
                                <div class="row">
                                    <div class="col-md-5">
                                        <select class="form-control">
                                            <option ng-repeat="item in servidoresFTP" ng-click="obtenerArchivosFTP(item.ip)">{{item.ip}}</option>
                                        </select>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="btn-group" data-toggle="buttons">
                                            <label class="btn btn-default active" ng-click="changeToCore(true)">
                                                <input type="radio" name="options" autocomplete="off"> Core
                                            </label>
                                            <label class="btn btn-default" ng-click="changeToCore(false)">
                                                <input type="radio" name="options" autocomplete="off"> JVC
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-body" >
                                <label ng-show="!archivosFTP.length">No se encontraron archivos a mostrar</label>
                                <table ng-show="archivosFTP.length > 0" class="table table-bordered">
                                    <thead>
                                        <tr><th>Nombre</th><th>Fecha de creación</th><th>Tamaño</th></tr>
                                    </thead>
                                    <tbody>
                                        <tr ng-repeat="row in archivosFTP">
                                            <th scope="row"><span ng-bind="row.nombre"></span></th>
                                            <td><span ng-bind="row.fechaCreacion"></span></td>
                                            <td><span ng-bind="row.tamano"></span></td>
                                            <td><button type="button" class="btn btn-primary btn-sm" data-dismiss="modal" ng-click="actualizarArchivoDesdeFTP(row)">Actualizar log</button></td>
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
                
                <!-- Modal descarga de archivos ftp-->
                <div class="modal fade" id="modalArchivosFTPDescarga" tabindex="-1" role="dialog" aria-labelledby="myModalLabelFTPDescarga">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabelFTPDescarga">Archivos en descarga de servidor FTP</h4>
                            </div>
                            <div class="modal-body" >
                                <div class="row">
                                    <div class="col-md-10 col-md-offset-1">
                                        <label ng-show="!servidoresFTPDescargando.length">No existen archivos en descarga</label>
                                        <div class="progress progress-striped active" ng-repeat="row in servidoresFTPDescargando">
                                            <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="row.porcentaje"
                                                 aria-valuemin="0" aria-valuemax="100" style="width: {{row.porcentaje + '%'}}">
                                                <span class="span-porcentaje"> {{row.ftp + ' -- ' + row.nombre}}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Modal para visualización de json con formato-->
                <div class="modal fade" id="modalJSONFormato" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabel">JSON</h4>
                            </div>
                            <div class="modal-body">
                                <pre id="preJSONResultado"></pre>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default btn-copy-clipboard" data-dismiss="modal" data-clipboard-action="copy" data-clipboard-target="#preJSONResultado">Copiar</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--Se agregan las herramientas-->
                <jsp:include page="parts/partHerramientas.jsp" />
                <!--Fin se seccion de herramientas-->
            </section>
            <section id="seleccion-busqueda" class="container-fluid" ng-controller="BusquedasController">
                <div class="panel panel-primary">
                    <div class="panel-heading">Formulario de búsqueda</div>
                    <div  class="panel-body" style="min-height: 200px; max-height: 500px; overflow-y: auto;overflow-x: hidden;">
                        <jsp:include page="parts/partUsuarios.jsp" />
                        <jsp:include page="parts/partGeneral.jsp" />
                    </div>
                </div>
            </section>

            <section id="seleccion-resultado" class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Resultado de la búsqueda</h3>
                        <ul class="list-inline panel-actions">
                            <li><a href="#" id="panel-fullscreen" role="button" title="Toggle fullscreen"><i class="glyphicon glyphicon-resize-full"></i></a></li>
                        </ul>
                    </div>
                    <div  class="panel-body" style="min-height: 300px;">
                        <pre id="textareaResultado"></pre>
                    </div>
                </div>
            </section>
            <jsp:include page="../fragments/footer.jsp" />
            <script type="text/javascript" src="<c:url value="/js/motor/inicio.js"/>"></script>
        </div>
    </body>
</html>