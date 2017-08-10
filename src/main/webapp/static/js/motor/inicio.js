//Variables globales

var busquedaLinea = null;
var horaInicio = "09:00 AM";
var horaFin = "11:00 AM";
var isShowDownloadModal = false;//para recargar las graficas de descarga de archivos de ftp

//Configuración de la carga de archivos
$("#file-archivo-cifrado").fileinput({
    language: 'es',
    maxFileSize: 1000,
    resizeImage: false,
    uploadLabel: 'Descifrar',
    browseLabel:  'Seleccionar properties',
    previewFileIcon: "<i class='glyphicon glyphicon-qrcode'></i>",
    allowedFileExtensions: ['properties']
});

//SECCION ANGULAR JS
//'ngAnimate', 'ngSanitize', 'ui.bootstrap'
var myAngularApp = angular.module('myApp', ['ngCookies', 'ui.bootstrap']).directive('test', function() {
});


//Información general
myAngularApp.controller('InformacionGeneralController', function ($scope, $http, $window, $timeout) {
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
    $scope.archivosHDCarpeta = true;
    
    $('#toggle-carpeta-log').change(function() {
        $scope.archivosHDCarpeta = $(this).prop('checked');
        $scope.obtenerArchivosServer();
    });
    
    $scope.archivosFTP = [];
    $scope.servidoresFTP = [];
    $scope.servidoresFTPCore = true;
    $scope.servidoresFTPIP = {};
    $scope.servidoresFTPDescargando = [];

    $scope.fnServidoresFTPInit = function(list){
        var data = list.split(",");
        for (var i = 0; i < data.length; i++) {
            $scope.servidoresFTP.push({"ip": data[i]});
        }
        if($scope.servidoresFTP.length > 0){
            $scope.servidoresFTPIP = $scope.servidoresFTP[0].ip;
        }
    };
    
    $('#modalSeleccionArchivo').on('show.bs.modal', function (e) {
        $scope.obtenerArchivosServer();
    });
    
    $scope.obtenerArchivosServer = function(){
        loading(true);
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/listar/archivos',
            data: JSON.stringify({core: $scope.archivosHDCarpeta})
            })
        .then(function (response){
            $scope.archivosHD = response.data;
        }).catch(function(response) {
            console.error('Error occurred:', response.status, response.data);
        }).finally(function() {
            loading(false);
        });
    };
    
    $('#modalArchivosFTPDescarga').on('show.bs.modal', function (e) {
        isShowDownloadModal = true;
        $scope.obtenerPorcentajesFTP();
    });
    $('#modalArchivosFTPDescarga').on('hide.bs.modal', function (e) {
        isShowDownloadModal = false;
    });
    
    $scope.fnEstablecerArchivo = function(data){
        loading(true);
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/establecer/archivo',
            data: data
            })
            .then(function (response){
                if(response.data.success === true){
                    console.log("Se obtienen los archivos correctamente status: " + response.status);
                    $scope.archivoActual = data;
                }
                else{
                    alert("No se pudo establecer el archivo");
                }
                $('#modalSeleccionArchivo').modal('hide');
        }).catch(function(response) {
            console.error('Error occurred:', response.status, response.data);
        }).finally(function() {
            loading(false);
        });
    };
    
    $scope.fnEliminarArchivo = function(data){
        loading(true);
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/eliminar/archivo/servidor',
            data: data
            })
            .then(function (response){
                if(response.data.success === true){
                    console.log("Se elimina correctamente el archivo: " + data.nombre);
                    toastr.info("El archivo "+ data.nombre +" se ha eliminado correctamente.");
                }
                else{
                    alert("No se pudo eliminar el archivo");
                }
                $scope.obtenerArchivosServer();
        }).catch(function(response) {
            console.error('Error occurred:', response.status, response.data);
        }).finally(function() {
            loading(false);
        });
    };
    
    $scope.changeToCore = function(option){
        $scope.servidoresFTPCore = option;
        $scope.obtenerArchivosFTP($scope.servidoresFTPIP);
    };
    
    $scope.obtenerArchivosFTP = function(ip) {
        $scope.servidoresFTPIP = ip;
        console.log('Obteniendo archivos de servidor ftp: ' + $scope.servidoresFTPIP);
        loading(true);
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/obtener/archivos/ftp',
            data: JSON.stringify({ip: $scope.servidoresFTPIP, core: $scope.servidoresFTPCore})
            })
            .then(function (response){
                $scope.archivosFTP = response.data;
                console.log(response.data);
        }).catch(function(response) {
            validarErrorHTTPFTPConnection(response);
        }).finally(function() {
            loading(false);
        });
    };
    
    $scope.actualizarArchivoDesdeFTP = function(row){
        console.log('Actualizando archivo desde servidor ftp: ' + row);
        loading(true);
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/actualizar/archivo/ftp',
            data: JSON.stringify({ip: $scope.servidoresFTPIP, core: $scope.servidoresFTPCore, archivo: row})
            })
        .then(function (response){
            console.log(response.data);
            toastr.info("Ha iniciado la descarga del archivo: " + row.nombre);
        }).catch(function(response) {
            validarErrorHTTPFTPConnection(response);
        }).finally(function() {
            loading(false);
        });
    };
    
    $scope.obtenerPorcentajesFTP = function(){
        console.log('Obteniendo porcentajes');
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/actualizar/archivo/porcentajes'
            })
        .then(function (response){
            console.log(response.data);
            $scope.servidoresFTPDescargando = response.data.archivos;
        }).catch(function(response) {
            console.error('Error occurred:', response.status, response.data);
        });
        if(isShowDownloadModal){
            $timeout(function(){
                $scope.obtenerPorcentajesFTP();
            }, 2000);
        }
    };
    
    //SECCION DE HERRAMIENTAS
    $scope.herrObjCifrar = {
        cadena: "",
        cifrar: true,
        sistema: "Alnova"
    };
    
    $scope.herrArrayCadenasCifrado = [];

    $scope.fnHerrLimpiarTablaCifrado = function(){
        $scope.herrArrayCadenasCifrado = [];
    };
    
    $('#toggle-tipo-cifrado').change(function() {
        $scope.herrObjCifrar.cifrar = $(this).prop('checked');
    });
    
    $scope.fnHerrCifrarSistema = function(sistema){
        $scope.herrObjCifrar.sistema = sistema;
    };
    
    $scope.fnHerrCifrarCadena = function(){
        if($scope.herrObjCifrar.cadena !== ""){
            console.log('Consultando servicio de cifrado: ' + JSON.stringify($scope.herrObjCifrar));
            loading(true);
            $http({withCredentials: true,
                method: 'POST',
                url: base + '/cifrado',
                data: $scope.herrObjCifrar
                })
            .then(function (response){
                if(response.data !== null && response.data !== ""){
                    $scope.herrArrayCadenasCifrado.push(response.data);
                }
                else{
                    alert("No se pudo cifrar/descifrar la cadena.");
                }
            }).catch(function(response) {
                console.error('Error occurred:', response.status, response.data);
            }).finally(function() {
                loading(false);
            });
        }
        else{
            alert("Favor de ingresar una cadena.");
        }
    };
    
    //CIFRADO PROPERTIES
    
    $scope.file = null;

     $scope.uploadFile = function(files) {
        $scope.file = files[0];
        console.log('Se selecciona el archivivo.');
     };

    $scope.uploadFileCifrado = function() {
        console.log('Inicia la subida del archivo');
        emptyResult();
        loading(true);
        var fd = new FormData();
        fd.append('file', $scope.file);
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/subir/archivo/cifrado',
            data: fd,
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .then(function (response){
            console.log(response);
            if(null !== response.data && response.data !== '' && response.data.informacion !== null && response.data.informacion !== ""){
                escribirDataResult(syntaxHighlightText(response.data.informacion));
                $("#modalHerramientaCifradoProperties").modal("hide");
                $(".fileinput-remove").trigger('click');
            }
            else{
                alert('No se pudo descrifrar el archivo.');
            }
        }).catch(function(response) {
            console.error('Error occurred:', response.status, response.data);
        }).finally(function() {
            loading(false);
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
            var partes = horaInicio.split(":");
            horaInicio = (horaInicio.endsWith("PM") ? (parseInt(partes[0]) + 12) :partes[0])  + ":" + partes[1].replace(/ [P|A]M/g, "") + ":00";
            partes = horaFin.split(":");
            horaFin = (horaFin.endsWith("PM") ? (parseInt(partes[0]) + 12) :partes[0])  + ":" + partes[1].replace(/ [P|A]M/g, "") + ":00";
            if(horaInicio <= horaFin){
                $scope.checkboxGeneralModel.rangoTiempo.texto = horaInicio + "-" + horaFin;
            }
            else{
                alert('Por favor verifique el rango de tiempo.');
                return;
            }
        }
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/busqueda/general',
            data: $scope.checkboxGeneralModel
            })
            .then(function (response){
                console.log("Se obtienen la informacion de la búsqueda: ");
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
    
    $scope.submitBusquedaLinea = function(linea){
        console.log('Inicia la busqueda de linea: ' + linea);
        emptyResult();
        loading(true);
        $http({withCredentials: true,
            method: 'POST',
            url: base + '/busqueda/linea',
            data: JSON.stringify({'linea': linea.replace(/\r\n|\r|\n/g, "")})
            })
            .then(function (response){
                console.log("Se obtienen la informacion de la búsqueda: ");
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
    
    busquedaLinea = $scope.submitBusquedaLinea;
    
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
    var regexFecha = /\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{1,3}/g;
    var regexPath = /PathInterceptor:\d+ - \/[\/\w+]+/g;// /api/...
    var regexAlias = /AspectoWalletController:\d+ - Alias: .+\s\s/g;// /api/...
    var regexResponse = /ObjEncriptacionSeguridad:\d+ - doObjToStrJSON:\{\".*codigoOperacion.*\}\s/g;
    var entradaAlnova = "Entrada:";
    var salidaAlnova = "Salida:";
    //Se agrega el estilo de linea
    data = "<span class= 'line-number'>" + data.replace(/\n/g, "</span>\n" + "<span class= 'line-number'>") + "</span>";
    //Se agrega el link para búsqueda de detalle de una línea
    data = data.replace(regexFecha, function(match){
        return "<a class='glyphicon-line-number'>" + match + "</a>";
    });
    //Se agrega la clase de todos los path's
    data = data.replace(regexPath, function(match){
        return "<span class='string-path'>" + match + "</span>";
    });
    //Se agregan las clases de los alias
    data = data.replace(regexAlias, function(match){
        return "<span class='string-alias'>" + match + "</span>";
    });
    //Se agregan las clases de los las respuestas
    data = data.replace(regexResponse, function(match){
        return "<span class='string-respuesta'>" + match + "</span>";
    });
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

var validarErrorHTTPFTPConnection = function(response){
    console.error('Error occurred:', response.status, response.data);
    if(response.status === 500){
        if(response.data.exception.includes('FTPConexionException')){
            console.error('No se pudo conectar con el servidor ftp seleccionado.');
            toastr.error('No se pudo conectar con el servidor ftp seleccionado.');
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
        horaInicio = $(this).find("input").val();
    });
    $(".divHoraFin").on('change.bfhtimepicker', function(){
        console.log("Hora: "+$(this).find("input").val());
        horaFin = $(this).find("input").val();
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
    var menu1 = new BootstrapMenu('.string-respuesta', {
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
            name: 'Visualizar json',
                onClick: function(data) {
                    $('#modalJSONFormato').modal('show');
                }
            }
        ]
    });
    
    var menu2 = new BootstrapMenu('.glyphicon-line-number', {
        menuEvent: 'right-click',
        menuSource: 'element',
        menuPosition: 'aboveRight', // default value, can be omitted
        fetchElementData: function($rowElem) {
            //Texto de la línea completa
            var padreText = $($rowElem).parent().text();
            return padreText;
        },
        actions: [{
            name: 'Ver detalle de línea',
                onClick: function(data) {
                    busquedaLinea(data);
                }
            }
        ]
    });
});