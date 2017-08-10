<%-- 
    Document   : partHerramientas
    Created on : 9/08/2017, 10:39:45 AM
    Author     : acruzb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!-- Modal herramienta de cifrado-->
<div class="modal fade" id="modalHerramientaCifrado" tabindex="-1" role="dialog" aria-labelledby="myModalCifrado">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalCifrado">Herramienta de cifrado</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" ng-submit="fnHerrCifrarCadena()" role="form">
                    <div class="row">
                        <div class="col-md-2"><label>Modo</label></div>
                        <div class="col-md-6">
                            <input id="toggle-tipo-cifrado" type="checkbox" ng-checked="herrObjCifrar.cifrar" data-toggle="toggle" data-width="150" data-onstyle="success" data-offstyle="info" data-on="<i class='glyphicon glyphicon-eye-close'></i> Cifrar" data-off="<i class='glyphicon glyphicon-eye-open'></i> Descifrar">
                        </div>
                    </div>
                    <div class="row" style="margin-top: 10px;margin-bottom: 10px;">
                        <div class="col-md-2"><label>Cadena</label></div>
                        <div class="col-md-6">
                            <div class="form-group has-clear">
                                <input type="text" class="form-control" placeholder="Texto" aria-describedby="sizing-addon2" ng-model="herrObjCifrar.cadena">
                                <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden" style="right: 15px;"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2"><label>Sistema</label></div>
                        <div class="col-md-6">
                            <div class="btn-group" data-toggle="buttons">
                                <label class="btn btn-default active" checked ng-click="fnHerrCifrarSistema('Alnova')">
                                    <input type="radio" name="options" autocomplete="off"> Alnova
                                </label>
                                <label class="btn btn-default" ng-click="fnHerrCifrarSistema('Aclaraciones')">
                                    <input type="radio" name="options" autocomplete="off"> Aclaraciones
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <hr/>
                    </div>
                    <div class="row">
                        <div class="col-md-1">
                            <button type="submit" class="btn btn-primary">Agregar</button>
                        </div>
                        <div class="col-md-1 col-md-offset-9">
                            <button type="button" class="btn btn-default" ng-click="fnHerrLimpiarTablaCifrado()">Limpiar tabla</button>
                        </div>
                    </div>
                    <div class="row">
                        <hr/>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Cadena original</th>
                                        <th>Cadena procesada</th>
                                        <th>Tipo</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="row in herrArrayCadenasCifrado">
                                        <th scope="row">{{row.original}}</th>
                                        <td>{{row.procesada}}</td>
                                        <td>{{row.tipo}}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>
<!-- Fin modal herramienta de cifrado-->


<!-- Modal herramienta de descifrar properties-->
<div class="modal fade" id="modalHerramientaCifradoProperties" tabindex="-1" role="dialog" aria-labelledby="myModalCifradoProperties">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalCifradoProperties">Descifrar archivos properties</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="form-horizontal" ng-submit="uploadFileCifrado()" role="form">
                            <div class="form-group">
                                <input id="file-archivo-cifrado" class="file" type="file" onchange="angular.element(this).scope().uploadFile(this.files)">
                            </div>
                            <!--<input type="file" name="file" onchange="angular.element(this).scope().uploadFile(this.files)" />-->
                            <!--<input type="submit" value="Enviar" data-dismiss="modal" ng-click="uploadFilesss()"/>-->
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

