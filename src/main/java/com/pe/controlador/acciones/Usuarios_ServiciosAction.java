/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pe.controlador.acciones;

import com.pe.controlador.util.CrudInstance;
import com.pe.modelo.dto.ServiciosDTO;
import com.pe.modelo.dto.UsuariosDTO;
import com.pe.modelo.dto.Usuarios_ServiciosDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dbermudez
 */
public class Usuarios_ServiciosAction {

    public List<Usuarios_ServiciosDTO> obtenerTodoUsuarioxServicio() {
        Usuarios_ServiciosDTO objetoDTO = null;
        List<Usuarios_ServiciosDTO> arrayListRetorno = new ArrayList<Usuarios_ServiciosDTO>();
        List<UsuariosDTO> arrayListUsuariosDTO = null;
        List<ServiciosDTO> arrayListServiciosDTO = null;
        CrudInstance crud = null;
        try {
            crud = new CrudInstance();
            arrayListUsuariosDTO = crud.getUsuarios().consultarTodo();
            arrayListServiciosDTO = crud.getServicios().consultarTodo();
            System.out.println(arrayListUsuariosDTO.size() + arrayListServiciosDTO.size());
            for (UsuariosDTO object1 : arrayListUsuariosDTO) {
                for (ServiciosDTO object2 : arrayListServiciosDTO) {
                    objetoDTO = new Usuarios_ServiciosDTO(-1, object2, object1, null, null, 0);
                    arrayListRetorno.add(objetoDTO);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            arrayListRetorno = null;
        } finally {
            return arrayListRetorno;
        }
    }

    public List<Usuarios_ServiciosDTO> obtenerUsuarioxServiciosRestante(List<Usuarios_ServiciosDTO> listaAsignados, List<Usuarios_ServiciosDTO> arrayListRetorno) {
        try {
            for (int i = 0; i < listaAsignados.size(); i++) {
                for (int y = arrayListRetorno.size() - 1; y > -1; y--) {
                    if (arrayListRetorno.get(y).getServicio().getIdservicio() == listaAsignados.get(i).getServicio().getIdservicio()
                            && arrayListRetorno.get(y).getUsuario().getIdusuario() == listaAsignados.get(i).getUsuario().getIdusuario()) {
                        arrayListRetorno.remove(y);
                        y = -1;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            arrayListRetorno = null;
        } finally {
            return arrayListRetorno;
        }
    }
}
