<%-- 
    Document   : partGeneral
    Created on : 30/07/2017, 01:32:23 PM
    Author     : acruzb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--Formulario de busqueda general-->
<div class="row div-busquedas divBusquedaGeneral" style="display: block;">
    <div class="col-md-12">
        <div class="center">
            <div class="row">
                <label class="col-sm-12 control-label" style="text-align: center;">Búsqueda de información general</label>
            </div>
            <div class="row">
                <form class="form-horizontal" role="form" ng-submit="submitBusquedaGeneral()">
                    <div class="form-group">
                        <div class="col-sm-10 col-sm-offset-1">
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <label class="checkbox-inline">
                                                <input type="checkbox" ng-model="checkboxGeneralModel.usuario.visible" ng-click="fnValidaCriterio(checkboxGeneralModel.usuario.valor)">Usuario
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group has-clear">
                                                <input type="text" class="form-control" placeholder="Ingrese el nombre de usuario" ng-model="checkboxGeneralModel.usuario.texto" ng-readonly="!checkboxGeneralModel.usuario.visible"/>
                                                <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <label class="checkbox-inline">
                                                <input type="checkbox" ng-model="checkboxGeneralModel.trAlnova.visible" ng-click="fnValidaCriterio(checkboxGeneralModel.trAlnova.valor)">TR Alnova
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group has-clear">
                                                <input type="text" class="form-control" placeholder="Ingrese la TR" ng-model="checkboxGeneralModel.trAlnova.texto" ng-readonly="!checkboxGeneralModel.trAlnova.visible"/>
                                                <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-5">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <label class="checkbox-inline">
                                                <input type="checkbox" ng-model="checkboxGeneralModel.ruta.visible" ng-click="fnValidaCriterio(checkboxGeneralModel.ruta.valor)">Ruta (Servicio)
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group has-clear">
                                                <input type="text" class="form-control" placeholder="Ingrese la ruta del servicio" ng-model="checkboxGeneralModel.ruta.texto" ng-readonly="!checkboxGeneralModel.ruta.visible"/>
                                                <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <label class="checkbox-inline">
                                                <input type="checkbox" ng-model="checkboxGeneralModel.textoLibre.visible" ng-click="fnValidaCriterio(checkboxGeneralModel.textoLibre.valor)">Texto libre
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group has-clear">
                                                <input type="text" class="form-control" placeholder="Ingrese texto libre" ng-model="checkboxGeneralModel.textoLibre.texto" ng-readonly="!checkboxGeneralModel.textoLibre.visible"/>
                                                <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-8">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <label class="checkbox-inline">
                                                <input type="checkbox" ng-model="checkboxGeneralModel.rangoTiempo.visible" ng-click="fnValidaCriterio(checkboxGeneralModel.rangoTiempo.valor)">Rango de tiempo
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row" ng-show="checkboxGeneralModel.rangoTiempo.visible">
                                        <p class="col-sm-1 control-label">Entre</p>
                                        <div class="col-md-3">
                                            <div class="bfh-timepicker divHoraInicio" data-time="09:00 AM" data-align="right" data-mode="12h"></div>
                                        </div>
                                        <p class="col-sm-1 control-label">y</p>
                                        <div class="col-md-3">
                                            <div class="bfh-timepicker divHoraFin" data-time="11:00 AM" data-align="right" data-mode="12h"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-10 col-sm-offset-1">
                            <input type="submit" value="Buscar" class="btn btn-primary" /> 
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!--End formulario de busqueda general-->