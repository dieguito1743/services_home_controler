/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var LS_AUTORIZATION = 'authorization';
var LS_USER = 'user';
var LS_ROLES = 'roles';
var LS_TYPE_JSON = 'json';
var LS_TYPE_TEXT = 'text';

function setItemLS(item, value, type) {
    if (type == LS_TYPE_JSON) {
        localStorage.setItem(item, JSON.stringify(value));
    } else {
        localStorage.setItem(item, value);
    }
}

function getItemLS(item, type) {
    var sRet = '';
    if (type == LS_TYPE_JSON) {
        sRet = localStorage.getItem(JSON.stringify(item));
    } else {
        sRet = localStorage.getItem(item);
    }
    return sRet;
}


