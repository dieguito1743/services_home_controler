/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pe.controlador.rest;

import com.pe.controlador.acciones.Usuarios_ServiciosAction;
import com.pe.controlador.util.AditionalFunction;
import com.pe.interfaces.ICrud;
import com.pe.interfaces.IJWT;
import com.pe.interfaces.IJsonTransformer;
import com.pe.modelo.dto.Usuarios_ServiciosDTO;
import com.pe.util.BussinessException;
import com.pe.util.BussinessMessage;
import com.pe.util.UtilDefines;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author dbermudez
 */
@RestController
public class Usuarios_ServiciosController {

    @Autowired
    @Qualifier(UtilDefines.INSTANCE_JSONTRANSFORMER)
    private IJsonTransformer jsonTransformer;
    //
    @Autowired
    @Qualifier(UtilDefines.INSTANCE_USUARIOS_SERVICIOS)
    private ICrud crud;
    //
    @Autowired
    @Qualifier(UtilDefines.INSTANCE_JWT)
    private IJWT jwt;

    //@CrossOrigin
    @RequestMapping(value = "/usuarios_servicios", method = RequestMethod.POST, consumes = UtilDefines.APLJSON, produces = UtilDefines.APLJSON)
    public void registrar(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody String jsonEntrada) {
        String sid = null;
        List<Usuarios_ServiciosDTO> listObjetoDTO = null;
        List<Usuarios_ServiciosDTO> arrayListServicio = null;
        List<BussinessMessage> bussinessMessage = null;
        String jsonSalida = null, contentType = null;
        int iStatusRet = 0;
        boolean tokenValido = false;
        try {
            tokenValido = jwt.validateJWT(httpServletRequest.getHeader(UtilDefines.HEADER_AUTORIZATION));
            if (tokenValido) {
                AditionalFunction.changeContext(this.getClass().getClassLoader());
                listObjetoDTO = jsonTransformer.fromListJSON(jsonEntrada, Usuarios_ServiciosDTO.class);
                arrayListServicio = crud.consultarTodo(listObjetoDTO);
                if (arrayListServicio != null && arrayListServicio.size() > 0) {
                    //limpiar de los que ya estan registrados
                    listObjetoDTO = new Usuarios_ServiciosAction().obtenerUsuarioxServiciosRestante(arrayListServicio, listObjetoDTO);
                }
                if (listObjetoDTO != null && listObjetoDTO.size() > 0) {
                    //registrar solo los que falta
                    for (int i = 0; i < listObjetoDTO.size(); i++) {
                        sid = crud.insertar(listObjetoDTO.get(i));
                        if (sid != null && !sid.equals(UtilDefines.INSERT_ERROR)) {
                            listObjetoDTO.get(i).setIdusuario_servicio(Integer.parseInt(sid));
                        }
                    }
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = jsonTransformer.toJson(listObjetoDTO);
                } else {
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_USER_SERVICE_EXIST);
                }
            } else {
                iStatusRet = HttpServletResponse.SC_UNAUTHORIZED;
                contentType = UtilDefines.APLJSON;
                jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_DESTROY_TOKEN);
            }
        } catch (BussinessException ex) {
            bussinessMessage = ex.getBussinessMessages();
            iStatusRet = HttpServletResponse.SC_BAD_REQUEST;
            contentType = UtilDefines.APLJSON;
            jsonSalida = jsonTransformer.toJson(bussinessMessage);
        } catch (Exception ex) {
            iStatusRet = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            contentType = UtilDefines.CONTENT_TYPE_TEXT;
            jsonSalida = ex.getMessage();
        } finally {
            AditionalFunction.response(iStatusRet, contentType, jsonSalida, httpServletRequest, httpServletResponse);
        }
    }

    //@CrossOrigin
    @RequestMapping(value = "/usuarios_servicios", method = RequestMethod.GET, produces = UtilDefines.APLJSON)
    public void obtenerUsuariosSinServicios(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        List<Usuarios_ServiciosDTO> arrayListServicio = null;
        List<BussinessMessage> bussinessMessage = null;
        String jsonSalida = null, contentType = null;
        int iStatusRet = 0;
        boolean tokenValido = false;
        try {
            tokenValido = jwt.validateJWT(httpServletRequest.getHeader(UtilDefines.HEADER_AUTORIZATION));
            if (tokenValido) {
                AditionalFunction.changeContext(this.getClass().getClassLoader());
                arrayListServicio = crud.consultarTodo();
                if (arrayListServicio != null && arrayListServicio.size() > 0) {
                    //excisten registros de usuarios_servicios
                    //ahora hay que crear un nuevo objeto que tenga a los usuarios y servicios que no esten asignados
                    arrayListServicio = new Usuarios_ServiciosAction().obtenerUsuarioxServiciosRestante(arrayListServicio, new Usuarios_ServiciosAction().obtenerTodoUsuarioxServicio());
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = jsonTransformer.toJson(arrayListServicio);
                } else {
                    //no existe ningùn usuarios_servicios en la bd
                    //ahora solo hay que enviar todas las combinaciones posibles de usuario + servicio
                    arrayListServicio = new Usuarios_ServiciosAction().obtenerTodoUsuarioxServicio();
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = jsonTransformer.toJson(arrayListServicio);
                }
            } else {
                iStatusRet = HttpServletResponse.SC_UNAUTHORIZED;
                contentType = UtilDefines.APLJSON;
                jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_DESTROY_TOKEN);
            }
        } catch (BussinessException ex) {
            bussinessMessage = ex.getBussinessMessages();
            iStatusRet = HttpServletResponse.SC_BAD_REQUEST;
            contentType = UtilDefines.APLJSON;
            jsonSalida = jsonTransformer.toJson(bussinessMessage);
        } catch (Exception ex) {
            iStatusRet = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            contentType = UtilDefines.CONTENT_TYPE_TEXT;
            jsonSalida = ex.getMessage();
        } finally {
            AditionalFunction.response(iStatusRet, contentType, jsonSalida, httpServletRequest, httpServletResponse);
        }
    }
}
