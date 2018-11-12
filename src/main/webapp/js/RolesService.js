/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function getRoles(){
    var _user = JSON.parse(localStorage.getItem('user'));
    var _authorization = localStorage.getItem('authorization');
    AJAX(AJAX_URL_GET_ROLES + _user.idusuario, AJAX_DATATYPE_TEXT, AJAX_CONTENT_TYPE_TEXT, AJAX_TYPE_GET, AJAX_DATATYPE_JSON, AJAX_ASYNC, _authorization, FUNCTION_AJAX_ROLES);
}

var FUNCTION_AJAX_ROLES = {
    f_ok: function (data, status, xhr) {
        //obtener los rolesde usuarios
        //dar permisos solo a los modulos accedidos
        //var autorization = xhr.getResponseHeader('Authorization');
        if(/*autorization == null || autorization == undefined ||*/ data.message != null || data.message != undefined){
            localStorage.setItem('authorization', autorization);
            console.log(data.message);
        }else{
            localStorage.setItem('roles', JSON.stringify(data));
            window.location.href = menuPrincipal;
        }
    },
    f_error: function (data, status, xhr) {
        console.log('ERROR GER ROLES ' + data + ' ' + status + ' ' + xhr);
        console.log(JSON.stringify(data));
    },
    f_complete: function (data, status, xhr) {
        //funcion que se ejecuta de todos modos
        //console.log('EJECUTAR DE TODOS MODOS ' + data + ' ' + status + ' ' + xhr);
    },
    f_test: function () {
        console.log('testing...');
    }
};


