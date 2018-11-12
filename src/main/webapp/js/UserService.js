/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function registrar(){
    var _user = new JSONUsuarios();
    _user.accusuario = $('#inputUser').val();
    _user.apellidousuario = $('#inputApellido').val();
    _user.correousuario = $('#inputEmail').val();
    _user.nombreusuario = $('#inputNombre').val();
    _user.passusuario = $('#inputPassword').val();
    _user.telefonousuario = $('#inputTelefono').val();
    _user.tipousuario = $('#inputTipo option:selected').val();
    console.log(_user);
    
}

function consultar(){
    
}

