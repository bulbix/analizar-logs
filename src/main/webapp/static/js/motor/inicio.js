//Variables globales



var myAngularApp = angular.module('myApp', ['ngCookies', 'ui.bootstrap']);//'ngAnimate', 'ngSanitize', 'ui.bootstrap'

//Información general
myAngularApp.controller('InformacionGeneralController', function ($scope, $http, $window) {
    var base = "http://" + $window.location.hostname + ":" + $window.location.port + $window.location.pathname;
    
    $scope.archivoActual = null;
    $scope.fnInitArchivo = function(nombre, tamano, fechaCreacion){
        if(nombre === ""){
            $scope.archivoActual = null;
            $('#modalSeleccionArchivo').modal('show');
        }
        else{
            $scope.archivoActual = {
                nombre: nombre,
                tamano: tamano,
                fechaCreacion: fechaCreacion
            };
        }
    };
    
    //Descarga de los archivos en disco
    $scope.archivosHD = [];
    
    $('#modalSeleccionArchivo').on('show.bs.modal', function (e) {
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/listar/archivos'})
            .then(function (response){
                $scope.archivosHD = response.data;
        }).catch(function(response) {
            console.error('Error occurred:', response.status, response.data);
        });
    });
    
    $scope.fnEstablecerArchivo = function(data){
        console.log(data);
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/establecer/archivo',
            data: data
            })
            .then(function (response){
                if(response.data.success === true){
                    console.log("Se obtienen los archivos correctamente status: " + response.status);
                    $scope.archivoActual = data;
                    $('#modalSeleccionArchivo').modal('hide');
                }
                else{
                    alert("No se pudo establecer el archivo");
                    $('#modalSeleccionArchivo').modal('hide');
                }
        }).catch(function(response) {
            console.error('Error occurred:', response.status, response.data);
        });
    };
});

//Sección de búsquedas
myAngularApp.controller('BusquedasController', function ($scope, $http, $window) {
    
    var base = "http://" + $window.location.hostname + ":" + $window.location.port + $window.location.pathname;
    
    //Busqueda de usuarios
    $scope.usuario = {
        tipo: 'usuario',
        nombreUsuario: '',
        nombre: '',
        apellido: '',
        icu: ''
    };
    
    $scope.fnFormularioBuscarUsuario = function(type){
        $scope.usuario.tipo = type;
        console.log($scope.usuario.tipo);
    };
    
    $scope.submitBuscarUsuario = function() {
        console.log('Inicia la busqueda del usuario: ' + JSON.stringify($scope.usuario));
        emptyResult();
        loading(true);
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/obtener/informacion/usuario',
            data: $scope.usuario
            })
            .then(function (response){
                $scope.usuario.nombreUsuario = '';
                $scope.usuario.nombre = '';
                $scope.usuario.apellido = '';
                $scope.usuario.icu = '';
                console.log("Se obtienen la informacion del usuario: ");
                console.log(response);
                if(null !== response.data && response.data !== ''){
                    console.log(JSON.stringify(response.data, undefined, 2));
                    escribirDataResult(syntaxHighlightJSON(response.data));
                }
                else{
                    console.log("No se encontraron datos de la búsqueda.");
                    escribirNoResult();
                }
        }).catch(function(response) {
            validarErrorHTTP(response);
        }).finally(function() {
            loading(false);
        });
    };
    
    //Busqueda general
    $scope.checkboxGeneralModel = {
        usuario : {
            visible: false,
            valor: 'usuario',
            texto: ''
        },
        trAlnova : {
            visible: false,
            valor: 'trAlnova',
            texto: ''
        },
        ruta: {
            visible: false,
            valor: 'ruta',
            texto: ''
        },
        textoLibre: {
            visible: false,
            valor: 'textoLibre',
            texto: ''
        },
        rangoTiempo: {
            visible: false,
            valor: 'rangoTiempo',
            texto: ''
        }
    };
    
    $scope.fnValidaCriterio = function(option){
        if(option === $scope.checkboxGeneralModel.usuario.valor){
            $scope.checkboxGeneralModel.ruta.visible = false;
            $scope.checkboxGeneralModel.textoLibre.visible = false;
        }
        else if(option === $scope.checkboxGeneralModel.ruta.valor){
            $scope.checkboxGeneralModel.trAlnova.visible = false;
            $scope.checkboxGeneralModel.textoLibre.visible = false;
        }
        else if(option === $scope.checkboxGeneralModel.textoLibre.valor){
            $scope.checkboxGeneralModel.usuario.visible = false;
            $scope.checkboxGeneralModel.trAlnova.visible = false;
            $scope.checkboxGeneralModel.ruta.visible = false;
        }
        else if(option === $scope.checkboxGeneralModel.trAlnova.valor){
            $scope.checkboxGeneralModel.textoLibre.visible = false;
            $scope.checkboxGeneralModel.ruta.visible = false;
        }
    };
    
    $scope.submitBusquedaGeneral = function(){
        console.log('Inicia la busqueda del usuario: ' + JSON.stringify($scope.checkboxGeneralModel));
        emptyResult();
        loading(true);
        if($scope.checkboxGeneralModel.rangoTiempo.visible){
            
        }
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/busqueda/general',
            data: $scope.checkboxGeneralModel
            })
            .then(function (response){
                console.log("Se obtienen la informacion de la búsqueda: ");
                console.log(response);
                if(null !== response.data && response.data !== '' && response.data.informacion !== null && response.data.informacion !== ""){
                    escribirDataResult(syntaxHighlightText(response.data.informacion));
                }
                else{
                    console.log("No se encontraron datos de la búsqueda.");
                    escribirNoResult();
                }
        }).catch(function(response) {
            validarErrorHTTP(response);
        }).finally(function() {
            loading(false);
        });
    };
    
});

//JavaScript functions

var syntaxHighlightJSON = function (json) {
    if (typeof json !== 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    json = "<span class= 'line-number'>" + json.replace(/\n/g, "</span>\n" + "<span class= 'line-number'>") + "</span>";
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
};

var syntaxHighlightText = function(data){
    //regex de rutas
    var regexPath = /PathInterceptor:\d+ - \/[\/\w+]+/g;// /api/...
    var regexAlias = /AspectoWalletController:\d+ - Alias: .+\s\s/g;// /api/...
    var regexResponse = /ObjEncriptacionSeguridad:\d+ - doObjToStrJSON:\{\".*codigoOperacion.*\}\s/g;
    var entradaAlnova = "Entrada:";
    var salidaAlnova = "Salida:";
    
    data = "<span class= 'line-number'>" + data.replace(/\n/g, "</span>\n" + "<span class= 'line-number'>") + "</span>";
    //Se agrega la clase de todos los path's
    var res = data.match(regexPath);
    if(null !== res){
        for (i = 0; i < res.length; i++) { 
            data = data.replace(res[i], "<span class='string-path'>"+res[i]+"</span>");
        }
    }
    //Se agregan las clases de los alias
    res = data.match(regexAlias);
    if(null !== res){
        for (i = 0; i < res.length; i++) { 
            data = data.replace(res[i], "<span class='string-alias'>"+res[i]+"</span>");
        }
    }
    //Se agregan las clases de los las respuestas
    res = data.match(regexResponse);
    if(null !== res){
        for (i = 0; i < res.length; i++) { 
            data = data.replace(res[i], "<span class='string-respuesta'>"+res[i]+"</span>");
        }
    }
    //Se agregan las clases para las tr
    data = data.split(entradaAlnova).join("<span class='string'>"+ entradaAlnova +"</span>");
    data = data.split(salidaAlnova).join("<span class='string'>"+ salidaAlnova +"</span>");
    
    return data;
};

var emptyResult = function(){
    $("#textareaResultado").empty();
};

var escribirNoResult = function(){
    $("#textareaResultado").append("<h3 style='text-align:center;font-weight: bold;'>No se encontraron resultados de la búsqueda.</h3>");
};

var escribirDataResult = function(data){
    $("#textareaResultado").append(data);
};

var loading = function (mode){
    if(mode === false){
        $(".bg_load").fadeOut("slow");
        $(".wrapper").fadeOut("slow");
    }
    else{
        $(".bg_load").fadeIn("slow");
        $(".wrapper").fadeIn("slow");
    }
};

var validarErrorHTTP = function(response){
    console.error('Error occurred:', response.status, response.data);
    if(response.status === 500){
        if(response.data.exception.includes('ArchivoNoSeleccionadoException')){
            console.error('No se ha seleccionado un archivo para procesar.');
            dialogArchivoNoSeleccionado(response.data.message);
        }
        else{
            console.error('Ha ocurrido un error en el servidor: ' + response.data.exception);
        }
    }
};

//Funcion para mostrar el mensaje de archivo no seleccionado
var dialogArchivoNoSeleccionado = function(message){
    bootbox.dialog({
        title: 'Archivo no seleccionado',
        message: '<p>'+ message +'</p>',
        closeButton: false,
        buttons: {
            ok: {
                label: "Seleccionar",
                className: 'btn-primary',
                callback: function(){
                    $('#modalSeleccionArchivo').modal('show');
                }
            }
        }
    });
};

var mostrarFormBusqueda = function(elem){
    $(".div-busquedas").hide();
    $("."+elem).show();
};

//jQuery init
$(function(){
    
    $("#panel-fullscreen").click(function (e) {
        e.preventDefault();
        
        var $this = $(this);
    
        if ($this.children('i').hasClass('glyphicon-resize-full'))
        {
            $this.children('i').removeClass('glyphicon-resize-full');
            $this.children('i').addClass('glyphicon-resize-small');
        }
        else if ($this.children('i').hasClass('glyphicon-resize-small'))
        {
            $this.children('i').removeClass('glyphicon-resize-small');
            $this.children('i').addClass('glyphicon-resize-full');
        }
        $(this).closest('.panel').toggleClass('panel-fullscreen');
    });
    
    //Div de rango de tiempo
    $(".divHoraInicio").on('change.bfhtimepicker', function(){
        console.log("Hora: "+$(this).find("input").val());
    });
    $(".divHoraFin").on('change.bfhtimepicker', function(){
        console.log("Hora: "+$(this).find("input").val());
    });
    
    $('.has-clear input[type="text"]').on('input propertychange', function () {
        var $this = $(this);
        var visible = Boolean($this.val());
        $this.siblings('.form-control-clear').toggleClass('hidden', !visible);
    }).trigger('propertychange');

    $('.form-control-clear').click(function () {
        $(this).siblings('input[type="text"]').val('')
                .trigger('propertychange').focus();
    });
    
    
    //toastr optiones para mensajes de información en pantalla
    toastr.options = {
        "debug": false,
        "positionClass": "toast-bottom-right",
        "onclick": null,
        "fadeIn": 300,
        "fadeOut": 1000,
        "timeOut": 5000,
        "extendedTimeOut": 500
    };
    
    //clipboard configuración
    var clipboard = new Clipboard('.btn-copy-clipboard');

    clipboard.on('success', function(e) {
        toastr.info("El json se ha copiado al clipboard.");
    });

    clipboard.on('error', function(e) {
        toastr.error("No se pudo copiar al clipboard.");
    });
    
    //Configuración del popup menu
    var menu3 = new BootstrapMenu('.string-respuesta', {
        menuEvent: 'hover',
        menuSource: 'element',
        menuPosition: 'belowRight', // default value, can be omitted
        fetchElementData: function($rowElem) {
            var str = $($rowElem).text();
            var regex = /\{\s*\".*\}/g;
            var res = str.match(regex);
            $("#preJSONResultado").empty();
            if(null !== res){
                var json = JSON.parse(res[0]);
                $("#preJSONResultado").append(syntaxHighlightJSON(json));
                return true;
            }
            return false;
        },
        actions: [{
            name: 'Ver json',
                onClick: function(data) {
                    toastr.info("Se muestra el resultado en formato json");
                    $('#modalJSONFormato').modal('show');
                }
            }
        ]
    });
});