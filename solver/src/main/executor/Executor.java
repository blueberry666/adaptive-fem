package main.executor;


import main.productions.Production;

public interface Executor {
	
	public void beginStage(int productionCount);
	
	public void waitForEnd();
	
	public void submitProduction(Production production);
	
	public void shutdown();
	
	

}
