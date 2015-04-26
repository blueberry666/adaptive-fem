package main.geometry;

public enum ShapeFunction {
	
	LB{
		@Override
		public double eval(double x, double y){
			return (1-x)*(1-y);
		}
	},RB{
		@Override
		public double eval(double x, double y){
			return x*(1-y);
		}
	},RT{
		@Override
		public double eval(double x, double y){
			return x*y;
		}
	},LT{
		@Override
		public double eval(double x, double y){
			return (1-x)*y;
		}
	};
	public abstract double eval(double x, double y);

}
