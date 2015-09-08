package main.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import main.productions.Production;

public class GroupingExecutor implements Executor{

	private ExecutorService executorService;
	private CountDownLatch countDownLatch;
	private List<List<Production>> productionGroups = new ArrayList<>();
	int listSize;
	int pool;
	
	public GroupingExecutor(int pool){
		this.executorService = Executors.newFixedThreadPool(pool);
		productionGroups.add(new ArrayList<>());
		this.pool = pool;
		
	}
	
	public void beginStage(int productionCount){
		listSize = productionCount/(5*pool);
		productionGroups.clear();
		productionGroups.add(new ArrayList<>());
		
	}
	
	public void waitForEnd(){
		try {
			countDownLatch = new CountDownLatch(productionGroups.size());
			for(List<Production> list : productionGroups){
				executorService.submit(new Runnable() {
					
					@Override
					public void run() {
						for(Production p : list){
							p.apply();
						}
						countDownLatch.countDown();
						
					}
				});
			}
			
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void submitProduction(Production production) {
		List<Production> last = productionGroups.get(productionGroups.size() - 1);
		if (last.size() < listSize) {
			last.add(production);
		} else {
			productionGroups.add(new ArrayList<Production>(Arrays.asList(production)));
		}
		production.setLatch(countDownLatch);
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

