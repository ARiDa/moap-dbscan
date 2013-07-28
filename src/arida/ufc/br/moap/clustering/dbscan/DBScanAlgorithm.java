package arida.ufc.br.moap.clustering.dbscan;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arida.ufc.br.moap.clustering.api.ICluster;
import arida.ufc.br.moap.clustering.api.IClusteringAlgorithm;
import arida.ufc.br.moap.core.imp.Parameters;
import arida.ufc.br.moap.core.spi.IDataModel;

public class DBScanAlgorithm extends IClusteringAlgorithm<Point> {

	private final static List<Integer> defaultColumnsToProcess;

	static {
		// By default process columns 0 and 1 in this order from each line of the file.
		defaultColumnsToProcess = new ArrayList<Integer>();
		defaultColumnsToProcess.add(0);
		defaultColumnsToProcess.add(1);
	}
	
	public DBScanAlgorithm() {
		this.distanceFunction = new DBScanDistanceFunction();
	}
	
	@Override
	public IDataModel<? extends ICluster<Point>> execute(IDataModel<Point> data, Parameters params) {
		FileDataModel<Point> fdm = (FileDataModel<Point>)data;

		// Get parameters
		List<Integer> columnsToProcess = (List<Integer>)params.getParamValue("columnsToProcess");
		if (columnsToProcess == null) {
			columnsToProcess = defaultColumnsToProcess;
		}
		
		Boolean useDecimalDegrees = (Boolean)params.getParamValue("useDecimalDegrees");
		if (useDecimalDegrees == null) {
			useDecimalDegrees = false;
		}
		
		String key = (String)params.getParamValue("key");
		if (key == null) {
			key = "";
		}
		
		// Required DBScan parameters
		Double eps = (Double)params.getParamValue("eps");
		Integer minPoints = (Integer)params.getParamValue("minPoints");
		
		if (eps == null || minPoints == null) {
			throw new DBScanException("Missing DBScan required parameter(s): 'eps' and/or 'minPoints'");
		}
		
		String l;

		String[] line;
		Double pX, pY;
		int pos, cluster = 0;
		Point p;
		// os pontos a serem clusterizados
		List<Point> points = new ArrayList<Point>();
		// conjuntos de vizinhos
		Set<Integer> neighborPts;

		
		while ((l = fdm.next()) != null) {
			line = l.split(",");
			pX = Double.parseDouble(line[columnsToProcess.get(0)]);
			pY = Double.parseDouble(line[columnsToProcess.get(1)]);
			if (useDecimalDegrees) {
				pX = GeoUtils.long2XSpherical(pX);
				pY = GeoUtils.lat2YSpherical(pY);
			}
			
			if (pY.isNaN() || pX.isNaN()) {
				throw new IllegalArgumentException("NaN");
			}
			
			//if (!pY.isNaN() && !pX.isNaN()) {
			p = new Point(pY, pX);
			points.add(p);
			//}
		}
		
		while ((pos = getUnvisitedPoint(points)) != -1) {
			points.get(pos).setIsvisited(true);
			neighborPts = regionQuery(pos, points, eps);
			if (neighborPts.size() < minPoints) {
				points.get(pos).setNoise(true);
			} else {
				cluster++;
				String clusterName = key + cluster;
				points.get(pos).setCluster(clusterName);
				points.get(pos).setIscore(true);
				expandCluster(points, pos, neighborPts, cluster, eps, minPoints, key);
			}
		}
		System.out.println(points);
		return convertToMoapDataModel(points);
	}

	private IDataModel<PointCluster> convertToMoapDataModel(List<Point> points) {
		DBScanPointDataModel dm = new DBScanPointDataModel();

		int clusterId = 1;
		PointCluster pc = null;
		for (Point p : points) {
			pc = dm.getInstance(p.getCluster());
			if (pc == null) {
				pc = new PointCluster(clusterId++);
				dm.addInstance(p.getCluster(), pc);
			} 
			pc.getObjects().add(p);
		}
		return dm;
	}
	
	private void expandCluster(List<Point> points, int pos,
			Set<Integer> neighborPts, int cluster, double eps, int minPoints,
			String key) {
		Set<Integer> neighborPts2;
		Point p;
		int index = 0;

		ArrayList<Integer> listNeigh = new ArrayList<Integer>();
		listNeigh.addAll(neighborPts);

		for (int j = 0; j < listNeigh.size(); j++) {
			index = listNeigh.get(j);
			p = points.get(index);
			if (!p.isVisited()) {
				points.get(index).setIsvisited(true);
				neighborPts2 = regionQuery(index, points, eps);
				if (neighborPts2.size() >= minPoints) {
					points.get(index).setIscore(true);
					listNeigh.addAll(neighborPts2);
					// neighborPts.addAll(neighborPts2);
				}
			}
			if (points.get(index).getCluster().equals(""))
				points.get(index).setCluster(key + cluster);
		}
	}

	private Set<Integer> regionQuery(int pos, List<Point> points,
			double eps) {
		Point p = points.get(pos);
		Set<Integer> neighborPts = new HashSet<Integer>();
		for (int i = 0; i < points.size(); i++) {
			if (distanceFunction.evaluate(p, points.get(i)) <= eps) {
				neighborPts.add(i);
			}
		}
		// numero de vizinhos do ponto que esta na posicao pos
		points.get(pos).setNeigh(neighborPts.size());
		return neighborPts;
	}

	private int getUnvisitedPoint(List<Point> points) {
		for (int i = 0; i < points.size(); i++) {
			if (!points.get(i).isVisited()) {
				return i;
			}
		}
		return -1;
	}

	public boolean mergeDbscan(List<Point> points1, List<Point> points2, String id_cluster1, String id_cluster2,
			double eps, int minPoints) {
		boolean canMerge = false;
		
		Point p1, p2;
		// matriz para identificar quando dois pontos de clusters distintos
		// sao vizinhos
		boolean[][] adj = new boolean[points1.size()][points2.size()];

		// verifica qual a distancia de dois pontos de clusters distintos,
		// caso sejam vizinhos, incrementa o neigh dos pontos
		for (int i = 0; i < points1.size(); i++) {
			p1 = points1.get(i);
			for (int j = 0; j < points2.size(); j++) {
				p2 = points2.get(j);
				if (distanceFunction.evaluate(p1, p2) <= eps) {
					points1.get(i).increaseNeigh();
					points2.get(j).increaseNeigh();
					adj[i][j] = true;
				}
			}
			// novos vizinhos permitiram que virasse core point no cluster 1
			if (p1.getNeigh() >= minPoints)
				points1.get(i).setIscore(true);
		}

		// novos vizinhos permitiu que ponto virasse core point no cluster 2
		for (int j = 0; j < points2.size(); j++) {
			p2 = points2.get(j);
			if (p2.getNeigh() >= minPoints)
				points2.get(j).setIscore(true);
		}

		// verifica se os clusters podem sofrer merge, ou seja, se existem
		// dois pontos que sao cores que sao vizinhos
		for (int i = 0; i < points1.size(); i++) {
			p1 = points1.get(i);
			for (int j = 0; j < points2.size(); j++) {
				p2 = points2.get(j);
				if (adj[i][j] && p2.isCore() && p1.isCore()) {
					canMerge = true;
					// se p2 ou p1 forem noise, com o merge eles deixam de
					// ser
					if (p1.isNoise())
						points1.get(i).setNoise(false);
					if (p2.isNoise())
						points2.get(j).setNoise(false);
				} else if (adj[i][j] && p2.isCore() && p1.isNoise()) {
					System.out.println("era noise");
					points1.get(i).setNoise(false);
				} else if (adj[i][j] && p1.isCore() && p2.isNoise()) {
					System.out.println("era noise");
					points2.get(j).setNoise(false);
				}
			}
		}

		if (canMerge) {
			// Can Merge
			// devolve os valores atualizados pro banco ou pra aplicacao que
			// chamou esse merge
			points1.addAll(points2);
		}

		return canMerge;
	}
	
	
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	public static void main(String[] args) {
		DBScanAlgorithm ds = new DBScanAlgorithm();
		File f = new File(DBScanAlgorithm.class.getResource("/data/AntonioSales-sample.txt").getFile());
		Parameters p = new Parameters();
		p.addParam("eps", 5.0);
		p.addParam("minPoints", 1);
		List<Integer> l = new ArrayList<Integer>();
		l.add(7); l.add(8);
		p.addParam("columnsToProcess", l);
		DBScanPointDataModel dm = (DBScanPointDataModel)ds.execute(new FileDataModel(f), p);
		System.out.println(dm);
	}
	
}
