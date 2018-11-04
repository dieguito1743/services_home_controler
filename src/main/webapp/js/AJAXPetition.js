/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*******************Rutas**************************************/
var AJAX_URL_CONTEXT = '/services_home_controlador';
var AJAX_URL_LOGIN = AJAX_URL_CONTEXT + '/usuarios/login';
var AJAX_URL_GET_ROLES = AJAX_URL_CONTEXT + '/roles/';
/*******************Metodos************************************/
var AJAX_TYPE_GET = 'GET';
var AJAX_TYPE_POST = 'POST';
/*******************CONTENTE_TYPE******************************/
var AJAX_CONTENT_TYPE_JSON = 'application/json; charset=utf-8';
var AJAX_CONTENT_TYPE_TEXT = 'text/plain; charset=utf-8';
/*******************DATA_TYPE**********************************/
var AJAX_DATATYPE_JSON = 'json';
var AJAX_DATATYPE_TEXT = 'text';
/**************************************************************/
var AJAX_ASYNC = true;

function AJAX(_url, _data, _contentType, _type, _dataType, _async, _authorization, _f_action) {
    $.ajax({
        // la URL para la petición
        url: _url,
        // la información a enviar
        // (también es posible utilizar una cadena de datos)
        data: _data,
        //
        contentType: _contentType,
        // especifica si será una petición POST o GET
        type: _type,
        // el tipo de información que se espera de respuesta
        dataType: _dataType,
        //
        async: _async,
        //
        headers: {
            "Authorization": _authorization
        },
        // código a ejecutar si la petición es satisfactoria;
        // la respuesta es pasada como argumento a la función
        success: function (data, status, xhr) {
            _f_action.f_ok(data, status, xhr);
        },
        // código a ejecutar si la petición falla;
        // son pasados como argumentos a la función
        // el objeto de la petición en crudo y código de estatus de la petición
        error: function (data, status, xhr) {
            _f_action.f_error(data, status, xhr);
        },
        // código a ejecutar sin importar si la petición falló o no
        complete: function (data, status, xhr) {
            _f_action.f_complete(data, status, xhr);
        }
    });
}
