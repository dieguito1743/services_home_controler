/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function login() {
    act_des_login(1);
    localStorage.setItem('user', null);
    localStorage.setItem('authorization', null);
    var user = $('#inputUser').val();
    var pass = $('#inputPassword').val();
    if (user != undefined && user != null && pass != undefined && pass != null) {
        var jsonUsuarios = getUser(user, pass);
        jsonUsuarios = JSON.stringify(jsonUsuarios);
        AJAX(AJAX_URL_LOGIN, jsonUsuarios, AJAX_CONTENT_TYPE_JSON, AJAX_TYPE_POST, AJAX_DATATYPE_JSON, AJAX_ASYNC, null, FUNCTION_AJAX_LOGIN);
    //realizar peticion AJAX y guardar el token con el usuario
    //redirigir a la vista principal
    }
    act_des_login(-1);
}

function act_des_login(atri) {
    if (atri == 1) {
        $('#buttonSubmit').attr('disabled', 'disabled');
    }
    if (atri == -1) {
        $('#buttonSubmit').removeAttr('disabled');
    }
}

function getUser(user, pass){
    var uRet = new JSONUsuarios();
    uRet.accusuario = user;
    uRet.passusuario = pass;
    return uRet;
}

var FUNCTION_AJAX_LOGIN = {
    f_ok: function (data, status, xhr) {
        //guardar los datos del usuario de sesion en local storage
        //guardar el token de autorización en local storage
        var autorization = xhr.getResponseHeader('Authorization');
        if(autorization == null || autorization == undefined || data.message != null || data.message != undefined){
            console.log(data.message);
        }else{
            localStorage.setItem('user', JSON.stringify(data));
            localStorage.setItem('authorization', autorization);
            getRoles();
        }
    },
    f_error: function (data, status, xhr) {
        console.log('ERROR LOGIN ' + data + ' ' + status + ' ' + xhr);
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