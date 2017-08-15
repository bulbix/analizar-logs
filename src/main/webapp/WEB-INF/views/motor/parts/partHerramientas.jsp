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
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- Modal herramienta de conversion de formatos de archivos-->
<div class="modal fade" id="modalHerramientaConversionFormatos" tabindex="-1" role="dialog" aria-labelledby="myModalCifradoFormatos">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalCifradoFormatos">Conversión de formato de archivos</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <h4>Convertir archivo pdf a Base64</h4>
                        <form class="form-horizontal" ng-submit="uploadArchivosFormatoPDF()" role="form">
                            <div class="form-group">
                                <input id="file-archivo-pdf" class="file" type="file" onchange="angular.element(this).scope().uploadFilePDF(this.files)">
                            </div>
                        </form>
                    </div>
                    <div class="col-md-10 col-md-offset-1" ng-show="filePDFBase64">
                        <label>Respuesta</label>
                        <div class="form-group has-clear">
                            <input type="text" class="form-control" ng-model="filePDFBase64"/>
                            <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <h4>Conversión de formatos</h4>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="form-horizontal" ng-submit="uploadArchivosFormato()" role="form">
                            <div class="row" style="padding-bottom: 15px;">
                                <div class="btn-group" data-toggle="buttons">
                                    <label class="btn btn-primary active" ng-click="fnCambiarFormatoConversion(ArchivosFormatoConversion.formato.jpg)">
                                        <input type="radio" name="options" autocomplete="off"> JPG a TIFF
                                    </label>
                                    <label class="btn btn-primary" ng-click="fnCambiarFormatoConversion(ArchivosFormatoConversion.formato.tiff)">
                                        <input type="radio" name="options" autocomplete="off"> TIFF a JPG
                                    </label>
                                    <label class="btn btn-primary" ng-click="fnCambiarFormatoConversion(ArchivosFormatoConversion.formato.base64pdf)">
                                        <input type="radio" name="options" autocomplete="off"> Base64 a PDF
                                    </label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group has-clear">
                                        <input type="text" class="form-control" placeholder="Base 64" ng-model="ArchivosFormatoConversion.base"/>
                                        <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-12" ng-show="imagenBase64">
                                <label>Respuesta</label>
                                <div class="form-group has-clear">
                                    <input type="text" class="form-control" ng-model="imagenBase64"/>
                                    <span class="form-control-clear glyphicon glyphicon-remove form-control-feedback hidden"></span>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12" style="padding: 0px;">
                                    <input type="submit" value="Convertir" class="btn btn-default" /> 
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
