package main.grid;

import java.util.Arrays;

import main.tree.DOF;
import main.tree.Element2D;

public class TestGridGenerator {
	
	
	public static void makeTestGrid(){
		DOF[] dofs = new DOF[12];
		for(int i=0;i<12;++i){
			dofs[i] = new DOF(i);
		}
		
		Element2D [] elems = new Element2D[7];
		for(int i=0;i<7;++i){
			elems[i] = new Element2D(i);
		}
		dofsToElems(dofs, elems);
		
		
		elems[0].addFunction(dofs[0], new double[]{0,0,0,1});
		elems[0].addFunction(dofs[1], new double[]{0,0,1,0});
		elems[0].addFunction(dofs[3], new double[]{1,0,0,0});
		elems[0].addFunction(dofs[4], new double[]{0,1,0,0});

		elems[1].addFunction(dofs[1], new double[]{0,0,0,1});
		elems[1].addFunction(dofs[2], new double[]{0,0,1,0});
		elems[1].addFunction(dofs[4], new double[]{1,0,0,0});
		elems[1].addFunction(dofs[5], new double[]{0,1,0,0});

		elems[2].addFunction(dofs[3], new double[]{0,0,0,1});
		elems[2].addFunction(dofs[4], new double[]{0,0,1,0});
		elems[2].addFunction(dofs[8], new double[]{1,0,0,0});
		elems[2].addFunction(dofs[9], new double[]{0,1,0,0});

		elems[3].addFunction(dofs[4], new double[]{0.5,0,0.5,1});
		elems[3].addFunction(dofs[6], new double[]{0,1,0,0});
		
		elems[4].addFunction(dofs[4], new double[]{0,0,0,0.5});
		elems[4].addFunction(dofs[5], new double[]{0,0,1,0});
		elems[4].addFunction(dofs[6], new double[]{1,0,0,0});
		elems[4].addFunction(dofs[7], new double[]{0,1,0,0});
		
		elems[5].addFunction(dofs[4], new double[]{0,0,0,0.5});
		elems[5].addFunction(dofs[6], new double[]{0,0,1,0});
		elems[5].addFunction(dofs[9], new double[]{1,0,0,0});
		elems[5].addFunction(dofs[10], new double[]{0,1,0,0});
		
		elems[6].addFunction(dofs[6], new double[]{0,0,0,1});
		elems[6].addFunction(dofs[7], new double[]{0,0,1,0});
		elems[6].addFunction(dofs[10], new double[]{1,0,0,0});
		elems[6].addFunction(dofs[11], new double[]{0,1,0,0});


	}

//	private static void elemsToDofs(DOF[] dofs, Element2D[] elems) {
//		dofs[0].elements.add(elems[0]);
//		dofs[2].elements.add(elems[1]);
//		dofs[8].elements.add(elems[2]);
//		dofs[11].elements.add(elems[6]);
//		dofs[1].elements.addAll(Arrays.asList(elems[0], elems[1]));
//		dofs[3].elements.addAll(Arrays.asList(elems[0], elems[2]));
//		dofs[5].elements.addAll(Arrays.asList(elems[4], elems[1]));
//		dofs[7].elements.addAll(Arrays.asList(elems[4], elems[6]));
//		dofs[9].elements.addAll(Arrays.asList(elems[2], elems[5]));
//		dofs[10].elements.addAll(Arrays.asList(elems[5], elems[6]));
//		dofs[4].elements.addAll(Arrays.asList(elems[0], elems[1], elems[2], elems[3], elems[4], elems[5]));
//		dofs[6].elements.addAll(Arrays.asList(elems[3], elems[4], elems[5], elems[6]));
//	}

	private static void dofsToElems(DOF[] dofs, Element2D[] elems) {
		elems[0].addDofs(Arrays.asList(dofs[0], dofs[1], dofs[3], dofs[4]));
		elems[1].addDofs(Arrays.asList(dofs[1], dofs[2], dofs[4], dofs[5]));
		elems[2].addDofs(Arrays.asList(dofs[3], dofs[4], dofs[8], dofs[9]));
		elems[3].addDofs(Arrays.asList(dofs[4], dofs[6]));
		elems[4].addDofs(Arrays.asList(dofs[4], dofs[5], dofs[6], dofs[7]));
		elems[5].addDofs(Arrays.asList(dofs[4], dofs[9], dofs[10], dofs[6]));
		elems[6].addDofs(Arrays.asList(dofs[6], dofs[7], dofs[10], dofs[11]));
	}

}
