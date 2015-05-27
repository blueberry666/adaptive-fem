package main;

import main.generator.BreakType;
import main.generator.Direction;
import main.generator.Generator;
import main.generator.Part;
import main.productions.productionFactory.L2ProjectionFactory;
import main.scheduler.GraphScheduler;
import main.scheduler.Node;
import main.scheduler.NotSoDummyNode;
import main.scheduler.ProductionGraphBuilder;
import main.tree.*;
import main.utils.MatrixUtil;

import java.util.*;
import java.util.Map.Entry;


public class Application {

    public static void main(String[] args) {

        Vertex root = generateMesh();

        Long st = System.currentTimeMillis();

        ProductionGraphBuilder graphBuilder = new ProductionGraphBuilder(new L2ProjectionFactory());

        System.out.println("production graph build time: " + (System.currentTimeMillis() - st));
        st = System.currentTimeMillis();
        GraphScheduler scheduler = new GraphScheduler();
        Set<? extends Node> graph = graphBuilder.makeGraph(root);
        System.out.println("Build graph time: " + (System.currentTimeMillis() - st));
        st = System.currentTimeMillis();
        List<List<Node>> scheduledNodes = scheduler.schedule(graph);

        System.out.println("Schedule graph time:  " + (System.currentTimeMillis() - st));
        st = System.currentTimeMillis();
        execute(scheduledNodes);
        System.out.println("Execute graph time: " + (System.currentTimeMillis() - st));


        Set<Vertex> leaves = new HashSet<>();
        getLeaves(leaves, root);
        Map<DOF, Double> result = gatherResult(leaves);

        System.out.println();
//        for(Entry<DOF, Double> e : result.entrySet()){
//            System.out.println("ID: " + e.getKey().ID + " = " + e.getValue());// + "     " + gaussianElimResult.get(e.getKey()));
//        }


        ResultPrinter.printResult(gatherElements(leaves), result);

    }

    private static void execute(List<List<Node>> scheduledNodes) {
        Executor executor = new Executor();
//        int idx=0;
//        Map<DOF,Double> gaussianElimResult = null;
        for(List<Node> nodes : scheduledNodes){
            executor.beginStage(nodes.size());
            for(Node n : nodes){
                executor.submitProduction(((NotSoDummyNode)n).getProduction());
            }
            executor.waitForEnd();
//            if(idx==1){
//                gaussianElimResult = gatherMatrix(root);
//            }
//            ++idx;
        }

        executor.shutdown();
    }

    private static Map<DOF, Double> gatherResult(Set<Vertex> leaves) {
        Map<DOF, Double> result = new TreeMap<>();
        for(Vertex v : leaves){
            for(int i=0;i<v.rowDofs.size();++i){
                result.put(v.rowDofs.get(i), v.x[i]);
            }
        }
        return result;
    }

    private static Vertex generateMesh(){
        Generator gen = new Generator(0, 1, 0, 1);
        generateMesh1(16, new ArrayList<Integer>(), gen);
        Vertex root = gen.buildEliminationTree();
        TreeInitializer.visit(root);
//        TestTreeBuilder.printTree("", root);
        System.out.println("Generator leaves count: " + gen.getLeaves().size());
        return root;

    }

    //generate mesh 1
	/*
	 * -----------------
	 * |+|+|+|+|
	 * ---------
	 * | | | | |
	 * ---------
	 * |   |   |
	 * ---------
	 *
	 *
	 */
    private static void generateMesh1(int level, List<Integer> path, Generator gen) {
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


    //generate mesh 2
	/*
	 * -----------------
	 * | | | |+|
	 * ---------
	 * |   |   |
	 * ---------
	 *
	 *
	 */
    private static void generateMesh2(int level, List<Integer> path, Generator gen) {
        if(level < 30){
            gen.breakPart(path, BreakType.CROSS);
            path.add(1);
            generateMesh2(level + 1, path, gen);
        }

    }

    private static List<Element2D> gatherElements(Collection<Vertex> leaves){
        List<Element2D> elements = new ArrayList<>();
        for(Vertex v : leaves){
            elements.add((Element2D)v.element);
        }
        return elements;
    }

    public static void print(Part p) {
        System.out.println(p);
        for (Direction d : Direction.values()) {
            System.out.printf("%s -> %s\n", d, p.getEdge(d));
        }
    }

    private static void getLeaves(Set<Vertex> leaves, Vertex root){
        if(root.children.isEmpty()){
            leaves.add(root);
        }else{
            for(Vertex v : root.children){
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
        for(Vertex v : leaves){
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
