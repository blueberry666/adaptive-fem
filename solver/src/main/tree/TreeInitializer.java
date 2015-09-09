package main.tree;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


public class TreeInitializer {
	enum Op {
		FIND_ELIMINATED_DOFS,
		FIND_BOUNDARY_ELEMS
	}

	private static final Map<Op, Long> times = new EnumMap<>(Op.class);

	static {
		for (Op op: Op.values()) {
			times.put(op, 0L);
		}
	}

	private static void addTime(Op op, long dt) {
		long value = times.get(op);
		times.put(op, value + dt);
	}
	
	public static void visit(Vertex vertex) {
		long st = System.currentTimeMillis();
		Queue<Vertex> q = new ArrayDeque<>();
		Deque<Vertex> dq = new ArrayDeque<>();
		q.add(vertex);

		while (!q.isEmpty()) {
			vertex = q.poll();
			dq.addFirst(vertex);
			for (Vertex v : vertex.children) {
				q.add(v);

			}

		}
		System.out.println("add to queue "+ (System.currentTimeMillis()-st));
		st = System.currentTimeMillis();
		while (!dq.isEmpty()) {
			vertex = dq.pollFirst();
			doStuff(vertex);
		}
		System.out.println("do stuff "+(System.currentTimeMillis()-st));
		for (Op op: Op.values()) {
			System.out.printf("%s: %f\n", op, times.get(op) / 1e6);
		}

	}
	
	private static void doStuff(Vertex vertex){
		if(vertex.element != null){
			vertex.rowDofs.addAll(vertex.element.dofs);
			vertex.boundaryElements.add(vertex.element);
		}else{
			Set<DOF> dofs = new HashSet<>();
			Set<Element> chlidrenBoundaryElements = new HashSet<>();
			for(Vertex v : vertex.children){
				dofs.addAll(v.getNotEliminatedDOFS());
				chlidrenBoundaryElements.addAll(v.boundaryElements);
			}
			long t1 = System.nanoTime();
			findEliminatedDofs(vertex, dofs, chlidrenBoundaryElements);
			long t2 = System.nanoTime();
			addTime(Op.FIND_ELIMINATED_DOFS, t2 - t1);
			vertex.rowDofs.addAll(dofs);
			findBoundaryElements(vertex);
			long t3 = System.nanoTime();
			addTime(Op.FIND_BOUNDARY_ELEMS, t3 - t2);
			
		}
		initializeMatrices(vertex);
	}

	private static void findEliminatedDofs(Vertex vertex, Set<DOF> dofs,
			Set<Element> chlidrenBoundaryElements) {
		Iterator<DOF> it = dofs.iterator();
		while(it.hasNext()){
			DOF d = it.next();
			boolean isInBoundaryElements = true;
			for(Element e : d.elements){
				if(!chlidrenBoundaryElements.contains(e)){
					isInBoundaryElements = false;
					break;
				}
			}
			if(isInBoundaryElements){
				vertex.rowDofs.add(d);
				++vertex.eliminatedDofs;
				it.remove();
			}
		}
	}

	private static void findBoundaryElements(Vertex vertex) {
		Set<Element> boundaryElements = new HashSet<>();
		for (Vertex v : vertex.children) {
			boundaryElements.addAll(v.boundaryElements);
		}

		for (Vertex v : vertex.children) {
			Set<DOF> notEliminated = new HashSet<>(v.getNotEliminatedDOFS());
			for (Element e : v.boundaryElements) {
				boolean isBoundary = false;

				outerLoop: 
				for (DOF d : e.dofs) {
					if (notEliminated.contains(d)) {
						for (Element el : d.elements) {
							if (!boundaryElements.contains(el)) {
								isBoundary = true;
								break outerLoop;
							}
						}
					}
				}
				if (isBoundary) {
					vertex.boundaryElements.add(e);
				}
			}
		}

	}

	private static void initializeMatrices(Vertex vertex) {
		vertex.A = new double[vertex.rowDofs.size()][vertex.rowDofs.size()];
		vertex.b = new double[vertex.rowDofs.size()];
	}
}
