package main;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import main.productions.Production;

public class Executor {
	
	private ExecutorService executorService;
	private CountDownLatch countDownLatch;
	
	public Executor(int pool){
		this.executorService = Executors.newFixedThreadPool(pool);
	}
	
	public void beginStage(int productionCount){
		countDownLatch = new CountDownLatch(productionCount);
	}
	
	public void waitForEnd(){
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void submitProduction(Production production){
		production.setLatch(countDownLatch);
		executorService.submit(production);
	}
	
	public void shutdown(){
		executorService.shutdown();
		try {
			executorService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
