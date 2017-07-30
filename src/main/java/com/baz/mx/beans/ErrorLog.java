/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.beans;

/**
 *
 * @author acruzb
 */
public class ErrorLog implements Comparable<ErrorLog>{
    
    private String server;
    private String path;
    private String error;
    private String log;

    public ErrorLog() {}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
    
    @Override
    public int compareTo(ErrorLog o) {
        try {
            return o.getPath().compareTo(this.getPath());
        } catch (Exception ex) {
            System.out.println("Error al comparar los paths.");
        }
        return -1;
    }
    
}
