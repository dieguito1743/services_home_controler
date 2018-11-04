/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pe.controlador.util;

import com.pe.util.UtilDefines;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dbermudez
 */
public class AditionalFunction {

    public static void changeContext(ClassLoader context) throws UnsupportedEncodingException {
        if (UtilDefines.PATH == null) {
            String path = context.getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");
            fullPath = pathArr[0];
            String reponsePath = "";
            reponsePath = new File(fullPath).getPath();
            UtilDefines.PATH = reponsePath;
        }
    }

    public static void response(int iStatusRet, String contentType, String jsonSalida, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            httpServletResponse.setStatus(iStatusRet);
            httpServletResponse.setContentType(contentType);
            httpServletResponse.getWriter().println(jsonSalida);
        } catch (IOException ex) {
            Logger.getLogger(AditionalFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
