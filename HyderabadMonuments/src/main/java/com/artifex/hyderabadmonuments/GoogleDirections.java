
package com.artifex.hyderabadmonuments;

import java.util.List;

public class GoogleDirections{
   	private List routes;
   	private String status;

 	public List getRoutes(){
		return this.routes;
	}
	public void setRoutes(List routes){
		this.routes = routes;
	}
 	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	}
}
