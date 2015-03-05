package main;


public class Stuff {

	
	public static int p = 2;
	public static int treeHeight = 10;
	public static int leafCount = (int) Math.pow(2.0, treeHeight - 2) * (p + 1);
	public static int knotSize = leafCount + 2 * p + 1;
	public static int parametersCount = knotSize - p - 1;
	public static double [] knotVector = generateKnotVector();
	public static double dt=0.000001;
	

	public static double f(double x){
		return Math.tan(Math.PI/2*x);
//		return x*(1-x);
	}
	public static double [] generateKnotVector(){
		   double [] v = new double[Stuff.knotSize];
		   for(int i=0;i<Stuff.p;++i){
			   v[i]=0;
		   }
		   for(int i=0;i<=Stuff.leafCount;++i){
			   v[i+Stuff.p]=(double)i/Stuff.leafCount;
		   }
		   
		   for(int i=0;i<Stuff.p;++i){
			   v[i+Stuff.p+Stuff.leafCount+1]=1;
		   }
		   
		   return v;
	   }
	
	
}
