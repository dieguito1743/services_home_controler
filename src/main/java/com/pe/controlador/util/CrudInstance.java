/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pe.controlador.util;

import com.pe.interfaces.ICrud;
import com.pe.modelo.dao.ModulosCrud;
import com.pe.modelo.dao.RecibosCrud;
import com.pe.modelo.dao.RolesCrud;
import com.pe.modelo.dao.ServiciosCrud;
import com.pe.modelo.dao.UsuariosCrud;
import com.pe.modelo.dao.Usuarios_ServiciosCrud;

/**
 *
 * @author dbermudez
 */
public class CrudInstance {

    private ICrud servicios;
    private ICrud usuarios;
    private ICrud usuarios_servicios;
    private ICrud recibos;
    private ICrud modulos;
    private ICrud roles;

    public ICrud getModulos() {
        if (modulos == null) {
            modulos = new ModulosCrud();
        }
        return modulos;
    }

    public ICrud getRecibos() {
        if (recibos == null) {
            recibos = new RecibosCrud();
        }
        return recibos;
    }

    public ICrud getRoles() {
        if (roles == null) {
            roles = new RolesCrud();
        }
        return roles;
    }

    public ICrud getServicios() {
        if (servicios == null) {
            servicios = new ServiciosCrud();
        }
        return servicios;
    }

    public ICrud getUsuarios() {
        if (usuarios == null) {
            usuarios = new UsuariosCrud();
        }
        return usuarios;
    }

    public ICrud getUsuarios_servicios() {
        if (usuarios_servicios == null) {
            usuarios_servicios = new Usuarios_ServiciosCrud();
        }
        return usuarios_servicios;
    }
}
