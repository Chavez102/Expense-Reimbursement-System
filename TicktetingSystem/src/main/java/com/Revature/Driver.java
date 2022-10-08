package com.Revature;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Javalin app = Javalin.create().start(8000); 
		System.out.println("Start");
		
		app.get("/hello", (Context ctx) ->{
			ctx.result("interresting1");
			ctx.result("interresting2");
		});
		
		
		
		
	}

}
