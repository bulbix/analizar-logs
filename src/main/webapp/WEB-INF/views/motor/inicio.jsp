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
                    <div  class="panel-body" style="min-height: 250px; max-height: 500px;">
                        <!--Formulario busqueda usuario-->
                        <div id="meet-the-team" class="row">
                            <div class="col-md-12">
                                <div class="center">
                                    <div class="row">
                                        <label class="col-sm-12 control-label" style="text-align: center;">Búsqueda de información de usuario</label>
                                    </div>
                                    <div class="row">
                                        <form class="form-horizontal" ng-submit="submitBuscarUsuario()" role="form">
                                            <div class="form-group">
                                                <div class="col-sm-6 col-sm-offset-3" style="text-align: center;">
                                                    <div class="btn-group" data-toggle="buttons">
                                                        <label class="btn btn-primary active" ng-click="fnFormularioBuscarUsuario('usuario')">
                                                            <input type="radio" name="options" autocomplete="off"> Usuario
                                                        </label>
                                                        <label class="btn btn-primary" ng-click="fnFormularioBuscarUsuario('nombre')">
                                                            <input type="radio" name="options" autocomplete="off"> Nombre
                                                        </label>
                                                        <label class="btn btn-primary" ng-click="fnFormularioBuscarUsuario('icu')">
                                                            <input type="radio" name="options" autocomplete="off"> ICU
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-sm-8 col-sm-offset-2">
                                                    <!--Busqueda por nombre de usuario-->
                                                    <div class="col-sm-6" ng-show="usuario.tipo === 'usuario'">
                                                        <div class="form-group has-clear">
                                                            <input type="text" class="form-control" ng-model="usuario.nombreUsuario" placeholder="Ingrese nombre de usuario"/>
                                                            <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                                                        </div>                 
                                                    </div>
                                                </div>
                                                <div class="col-sm-8 col-sm-offset-2">
                                                    <!--Busqueda por nombre completo-->
                                                    <div class="col-sm-6" ng-show="usuario.tipo === 'nombre'">
                                                        <div class="form-group has-clear">
                                                            <input type="text" class="form-control" ng-model="usuario.nombre" placeholder="Ingrese nombre"/>
                                                            <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-6" ng-show="usuario.tipo === 'nombre'">
                                                        <div class="form-group has-clear">
                                                            <input type="text" class="form-control" ng-model="usuario.apellido" placeholder="Ingrese apellido"/>
                                                            <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-8 col-sm-offset-2">
                                                    <!--Busqueda por ICU-->
                                                    <div class="col-sm-6" ng-show="usuario.tipo === 'icu'">
                                                        <div class="form-group has-clear">
                                                            <input type="text" class="form-control" ng-model="usuario.icu" placeholder="Ingrese el icu"/>
                                                            <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-8 col-sm-offset-2">
                                                    <input type="submit" value="Buscar" class="btn btn-primary" /> 
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div> 
                        
                        
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