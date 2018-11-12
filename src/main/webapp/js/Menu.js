/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function menuPrincipal(vista) {
    var _roles = new JSONRoles();
    _roles = JSON.parse(localStorage.getItem('roles'));
    for (var i in _roles) {
        var active = '';
        if(_roles[i].nivelModulo != undefined && _roles[i].nivelModulo == 1){
            if(_roles[i].modulo.page == vista){
                active = ' active';
            }
            $('#menu').append('<li class = text-left '+active+'><a href="'+_roles[i].modulo.page +'">' + _roles[i].modulo.nombremodulo + '</a></li>');
        }
    }
}


