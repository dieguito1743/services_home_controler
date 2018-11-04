/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pe.controlador.rest;

import com.pe.controlador.util.AditionalFunction;
import com.pe.interfaces.ICrud;
import com.pe.interfaces.IJWT;
import com.pe.interfaces.IJsonTransformer;
import com.pe.modelo.dto.ModulosDTO;
import com.pe.util.BussinessException;
import com.pe.util.BussinessMessage;
import com.pe.util.UtilDefines;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dbermudez
 */
@RestController
public class ModulosController {

    @Autowired
    @Qualifier(UtilDefines.INSTANCE_JSONTRANSFORMER)
    private IJsonTransformer jsonTransformer;
    //
    @Autowired
    @Qualifier(UtilDefines.INSTANCE_MODULOS)
    private ICrud crud;
    //
    @Autowired
    @Qualifier(UtilDefines.INSTANCE_JWT)
    private IJWT jwt;

    //@CrossOrigin
    @RequestMapping(value = "/modulos", method = RequestMethod.GET, produces = UtilDefines.APLJSON)
    public void consultarTodo(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        List<ModulosDTO> arrayListDTO = null;
        List<BussinessMessage> bussinessMessage = null;
        String jsonSalida = null, contentType = null;
        int iStatusRet = 0;
        boolean tokenValido = false;
        try {
            tokenValido = jwt.validateJWT(httpServletRequest.getHeader(UtilDefines.HEADER_AUTORIZATION));
            if (tokenValido) {
                AditionalFunction.changeContext(this.getClass().getClassLoader());
                arrayListDTO = crud.consultarTodo();
                if (arrayListDTO != null && arrayListDTO.size() > 0) {
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = jsonTransformer.toJson(arrayListDTO);
                } else {
                    iStatusRet = HttpServletResponse.SC_OK;
                    contentType = UtilDefines.APLJSON;
                    jsonSalida = UtilDefines.MESSAGE.replace(UtilDefines.MESSAGE_COMODIN, UtilDefines.MESSAGE_NOT_FIND_USER);
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
