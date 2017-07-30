var myAngularApp = angular.module('myApp', ['ngCookies', 'ui.bootstrap']);//'ngAnimate', 'ngSanitize', 'ui.bootstrap'
myAngularApp.controller('BusquedasController', function ($scope, $http, $window) {
    
    var base = "http://" + $window.location.hostname + ":" + $window.location.port + $window.location.pathname;
    
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
                    escribirDataResult(syntaxHighlight(response.data));
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

//JavaScript functions

var syntaxHighlight = function (json) {
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
}

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

//jQuery init
$(function(){
    
    $('.has-clear input[type="text"]').on('input propertychange', function () {
        var $this = $(this);
        var visible = Boolean($this.val());
        $this.siblings('.form-control-clear').toggleClass('hidden', !visible);
    }).trigger('propertychange');

    $('.form-control-clear').click(function () {
        $(this).siblings('input[type="text"]').val('')
                .trigger('propertychange').focus();
    });
});