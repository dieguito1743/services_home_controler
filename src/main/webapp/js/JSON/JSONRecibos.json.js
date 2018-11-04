/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function JSONRecibos(usuario_servicio, servicio, periodorecibo, medicioninicial, medicionfinal, fechami, fechamf, montorecibo, consumorecibo, preciounidad, fechavencimiento, estadorecibo){
    this.idrecibo = -1;
    this.usuario_servicio = usuario_servicio;
    this.servicio = servicio;
    this.periodorecibo = periodorecibo;
    this.medicioninicial = medicioninicial;
    this.medicionfinal = medicionfinal;
    this.fechami = fechami;
    this.fechamf = fechamf;
    this.montorecibo = montorecibo;
    this.consumorecibo = consumorecibo;
    this.preciounidad = preciounidad;
    this.fechavencimiento = fechavencimiento;
    this.estadorecibo = estadorecibo;
}

function JSONRecibos(idrecibo, usuario_servicio, servicio, periodorecibo, medicioninicial, medicionfinal, fechami, fechamf, montorecibo, consumorecibo, preciounidad, fechavencimiento, estadorecibo){
    this.idrecibo = idrecibo;
    this.usuario_servicio = usuario_servicio;
    this.servicio = servicio;
    this.periodorecibo = periodorecibo;
    this.medicioninicial = medicioninicial;
    this.medicionfinal = medicionfinal;
    this.fechami = fechami;
    this.fechamf = fechamf;
    this.montorecibo = montorecibo;
    this.consumorecibo = consumorecibo;
    this.preciounidad = preciounidad;
    this.fechavencimiento = fechavencimiento;
    this.estadorecibo = estadorecibo;
}

function JSONRecibos(usuario_servicio, servicio){
    this.idrecibo = -1;
    this.usuario_servicio = usuario_servicio;
    this.servicio = servicio;
    this.periodorecibo = -1;
    this.medicioninicial = -1;
    this.medicionfinal = -1;
    this.fechami = "";
    this.fechamf = "";
    this.montorecibo = -1;
    this.consumorecibo = -1;
    this.preciounidad = -1;
    this.fechavencimiento = "";
    this.estadorecibo = -1;
}
