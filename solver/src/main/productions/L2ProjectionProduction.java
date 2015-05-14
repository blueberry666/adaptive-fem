package main.productions;

import main.geometry.Basis2D;
import main.integrals.GaussQuad;
import main.tree.Element2D;
import main.tree.Vertex;
import main.utils.Rectangle;

public class L2ProjectionProduction extends Production {
	
	private static int degree=2; 

	public L2ProjectionProduction(Vertex vert) {
		super(vert);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void apply() {

		double[] points = GaussQuad.points(degree);
		double[] weights = GaussQuad.weights(degree);
		Element2D element = ((Element2D) vertex.element);
		Rectangle r = element.rectangle;
		for (int i = 0; i < points.length; ++i) {
			for (int j = 0; j < points.length; ++j) {
				double p0 = points[i];
				double p1 = points[j];
				double xt = Basis2D.normalize(-1, 1, p0);
				double yt = Basis2D.normalize(-1, 1, p1);
				double x = xt * (r.x1 - r.x0) + r.x0;
				double y = yt * (r.y1 - r.y0) + r.y0;
				double w = weights[i] * weights[j];
				double wartosc = chujoza(x, y);
				for (int dofIdx = 0; dofIdx < vertex.rowDofs.size(); ++dofIdx) {
					double chujnia = Basis2D.evaluateDOF(xt, yt,
							element.localBasisFunctions.get(vertex.rowDofs
									.get(dofIdx)));
					vertex.b[dofIdx] += w/4*wartosc*chujnia;
					for (int dofIdx2 = 0; dofIdx2 < vertex.rowDofs.size(); ++dofIdx2) {
						double chujnia2 = Basis2D.evaluateDOF(xt, yt,
								element.localBasisFunctions.get(vertex.rowDofs
										.get(dofIdx2)));
						vertex.A[dofIdx][dofIdx2] += w / 4 * chujnia * chujnia2;
					}

				}

			}
		}
		
				
	}
	
	private double chujoza(double x, double y) {
		double a = Math.max(Math.abs(x - 0.5), Math.abs(y - 0.5));
		return 1 - 4 * a * a;
	}
	


}
