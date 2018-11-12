/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*function JSONRoles(usuario, modulo, nivelModulo){
    this.idrol = -1;
    this.usuario = usuario;
    this.modulo = modulo;
    this.nivelModulo= nivelModulo;
}

function JSONRoles(idrol, usuario, modulo, nivelModulo){
    this.idrol = idrol;
    this.usuario = usuario;
    this.modulo = modulo;
    this.nivelModulo= nivelModulo;
}*/

/*function JSONRoles(usuario, modulo){
    this.idrol = -1;
    this.usuario = usuario;
    this.modulo = modulo;
    this.nivelModulo= -1
}*/

function JSONRoles(){
    this.idrol = -1;
    this.usuario = new JSONUsuarios();
    this.modulo = new JSONModulos();
    this.nivelModulo= -1
}
