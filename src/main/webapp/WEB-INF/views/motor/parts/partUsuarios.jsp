<%-- 
    Document   : partUsuarios
    Created on : 30/07/2017, 01:31:19 PM
    Author     : acruzb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--Formulario busqueda usuario-->
<div class="row div-busquedas divBusquedaUsuarios" style="display: none;">
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
<!--End formulario busqueda usuario-->