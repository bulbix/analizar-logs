<%-- 
    Document   : head
    Created on : 25/07/2017, 12:49:57 PM
    Author     : acruzb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Static navbar -->
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Motor de búsqueda</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li class="active"><a href="#">Home</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Archivo <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#" data-target="#modalSeleccionArchivo" data-toggle="modal">Archivo a procesar</a></li>
                        <li><a href="#" data-target="#modalArchivosFTP" data-toggle="modal">Gestión de archivos</a></li>
                        <li><a href="#">Descarga de logs</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Herramientas <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li role="separator" class="divider"></li>
                        <li class="dropdown-header">Busquedas</li>
                        <li onclick="mostrarFormBusqueda('divBusquedaUsuarios')"><a href="#">Busqueda de usuarios</a></li>
                        <li onclick="mostrarFormBusqueda('divBusquedaGeneral')"><a href="#">Busqueda de general</a></li>
                        <li role="separator" class="divider"></li>
                        <li class="dropdown-header">Cifrado</li>
                        <li><a href="#">Cifrado</a></li>
                        <li><a href="#">Descifrar properties</a></li>
                        <li role="separator" class="divider"></li>
                        <li class="dropdown-header">Utilerias</li>
                        <li><a href="#">Decodificar imagen</a></li>
                        <li><a href="#">Base 64 a PDF</a></li>
                        <li><a href="#">PDF a Base64</a></li>
                        <li role="separator" class="divider"></li>
                        <li class="dropdown-header">Reportes</li>
                        <li><a href="#">Reporte de migraciones</a></li>
                    </ul>
                </li>
            </ul>
        </div><!--/.nav-collapse -->
    </div><!--/.container-fluid -->
</nav>