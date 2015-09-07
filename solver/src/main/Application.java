package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import main.generator.BreakType;
import main.generator.Direction;
import main.generator.Generator;
import main.generator.Part;
import main.productions.productionFactory.L2ProjectionFactory;
import main.scheduler.GraphScheduler;
import main.scheduler.Node;
import main.scheduler.NotSoDummyNode;
import main.scheduler.ProductionGraphBuilder;
import main.tree.DOF;
import main.tree.Element2D;
import main.tree.TreeInitializer;
import main.tree.Vertex;
import main.utils.MatrixUtil;

public class Application {

	//input: adaptation_type {1,2} level_count threads_count
	public static void main(String[] args) {

		int adaptationType = new Integer(args[0]);
		int levels = new Integer(args[1]);
		int threads = new Integer(args[2]);
		Long scheduleTime = 0l;
		Long executionTime = 0l;
		int iterations = 5;
		for (int i = 0; i < iterations; ++i) {
			Vertex root = generateMesh(adaptationType, levels);

			Long st = System.currentTimeMillis();

			ProductionGraphBuilder graphBuilder = new ProductionGraphBuilder(
					new L2ProjectionFactory());

			st = System.currentTimeMillis();
			GraphScheduler scheduler = new GraphScheduler();
			Set<? extends Node> graph = graphBuilder.makeGraph(root);

			st = System.currentTimeMillis();
			List<List<Node>> scheduledNodes = scheduler.schedule(graph);
			Long tmp = (System.currentTimeMillis() - st);
			scheduleTime += tmp;

			st = System.currentTimeMillis();
			execute(scheduledNodes, root, threads);
			tmp = (System.currentTimeMillis() - st);
			executionTime += tmp;

			Set<Vertex> leaves = new HashSet<>();
			getLeaves(leaves, root);
			Map<DOF, Double> result = gatherResult(leaves);

		}

		// System.out.print("Schedule graph time:  " + scheduleTime / 10);
		System.out.println(adaptationType + ":" + threads + ":" + levels + ":" + executionTime / iterations);

	}

	private static void execute(List<List<Node>> scheduledNodes, Vertex root,
			int pool) {
		Executor executor = new Executor(pool);
		int idx = 0;
		Map<DOF, Double> gaussianElimResult = null;

		for (List<Node> nodes : scheduledNodes) {
			executor.beginStage(nodes.size());
			for (Node n : nodes) {
				executor.submitProduction(((NotSoDummyNode) n).getProduction());
			}
			executor.waitForEnd();
			// if(idx==1){
			// Long st = System.currentTimeMillis();
			// gaussianElimResult = gatherMatrix(root);
			// System.out.println("Gaussian time!: " +
			// (System.currentTimeMillis()-st));
			// }
			// ++idx;
		}

		executor.shutdown();
	}

	private static Map<DOF, Double> gatherResult(Set<Vertex> leaves) {
		Map<DOF, Double> result = new TreeMap<>();
		for (Vertex v : leaves) {
			for (int i = 0; i < v.rowDofs.size(); ++i) {
				result.put(v.rowDofs.get(i), v.x[i]);
			}
		}
		return result;
	}

	private static Vertex generateMesh(int meshType, int levelsCount) {
		Generator gen = new Generator(0, 1, 0, 1);
		switch(meshType){
		case 1:
			generateEdgeMesh(levelsCount, new ArrayList<Integer>(), gen);
			break;
		case 2:
			generateCornerMesh(levelsCount, new ArrayList<Integer>(), gen);
			break;
		}
		Vertex root = gen.buildEliminationTree();
		TreeInitializer.visit(root);
		// TestTreeBuilder.printTree("", root);
//		System.out.println("Generator leaves count: " + gen.getLeaves().size());
		return root;

	}

	// generate mesh 1
	/*
	 * ----------------- |+|+|+|+| --------- | | | | | --------- | | | ---------
	 */
	private static void generateEdgeMesh(int level, List<Integer> path,
			Generator gen) {
		Queue<List<Integer>> q = new LinkedList<List<Integer>>();
		q.add(path);
		for (int i = 0; i < level; ++i) {
			List<List<Integer>> tmp = new ArrayList<>(q);
			q.clear();
			for (List<Integer> l : tmp) {
				gen.breakPart(l, BreakType.CROSS);
				l.add(0);
				q.add(new ArrayList<>(l));
				l.remove(l.size() - 1);
				l.add(1);
				q.add(new ArrayList<>(l));

			}
		}

	}

	// generate mesh 2
	/*
	 * ----------------- | | | |+| --------- | | | ---------
	 */
	private static void generateCornerMesh(int level, List<Integer> path,
			Generator gen) {
		if (level > 0) {
			gen.breakPart(path, BreakType.CROSS);
			path.add(1);
			generateCornerMesh(level - 1, path, gen);
		}

	}

	private static List<Element2D> gatherElements(Collection<Vertex> leaves) {
		List<Element2D> elements = new ArrayList<>();
		for (Vertex v : leaves) {
			elements.add((Element2D) v.element);
		}
		return elements;
	}

	public static void print(Part p) {
		System.out.println(p);
		for (Direction d : Direction.values()) {
			System.out.printf("%s -> %s\n", d, p.getEdge(d));
		}
	}

	private static void getLeaves(Set<Vertex> leaves, Vertex root) {
		if (root.children.isEmpty()) {
			leaves.add(root);
		} else {
			for (Vertex v : root.children) {
				getLeaves(leaves, v);
			}

		}
	}

	private static Map<DOF, Double> gatherMatrix(Vertex root) {
		Map<DOF, Double> result = new TreeMap<>();
		Set<Vertex> leaves = new HashSet<>();
		getLeaves(leaves, root);
		Set<DOF> dofs = new HashSet<>();
		for (Vertex v : leaves) {
			dofs.addAll(v.rowDofs);
		}
		List<DOF> dofList = new ArrayList<>(dofs);
		double[][] matrixA = new double[dofList.size()][dofList.size()];
		double[] matrixB = new double[dofList.size()];
		combineMatrices(leaves, dofList, matrixA, matrixB);
		double[] solution = MatrixUtil.gaussianElimination(matrixA, matrixB);
		for (int i = 0; i < dofList.size(); ++i) {
			result.put(dofList.get(i), solution[i]);
		}
		return result;
	}

	private static void combineMatrices(Set<Vertex> leaves, List<DOF> dofList,
			double[][] matrixA, double[] matrixB) {
		for (Vertex v : leaves) {
			for (DOF d : v.rowDofs) {
				int parentI = dofList.indexOf(d);
				int childI = v.rowDofs.indexOf(d);
				for (DOF d2 : v.rowDofs) {

					int parentJ = dofList.indexOf(d2);

					int childJ = v.rowDofs.indexOf(d2);
					matrixA[parentI][parentJ] += v.A[childI][childJ];

				}
				matrixB[parentI] += v.b[childI];
			}
		}
	}

}
