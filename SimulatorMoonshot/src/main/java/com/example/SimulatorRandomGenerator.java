package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimulatorRandomGenerator {
	static double i=0.0;
	@RequestMapping(value="/moonshotvalues", method={RequestMethod.GET})
	public Object createValue() throws InterruptedException
	{
			i+=0.000001;
		Thread.sleep(100);
		return (i+";"+(Math.random()+100));
	}
	
}

