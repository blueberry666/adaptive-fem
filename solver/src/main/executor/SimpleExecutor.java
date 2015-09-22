package main.executor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import main.productions.Production;

public class SimpleExecutor implements Executor{

	private ExecutorService executorService;
	private CountDownLatch countDownLatch;
	
	public SimpleExecutor(int pool){
		this.executorService = Executors.newFixedThreadPool(pool);
	}
	
	public void beginStage(int productionCount){
		countDownLatch = new CountDownLatch(productionCount);
	}
	
	public void waitForEnd(){
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
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
			e.printStackTrace();
		}
	}
}
