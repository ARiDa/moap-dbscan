package arida.ufc.br.moap.clustering.dbscan;

import java.util.ArrayList;
import java.util.List;


import arida.ufc.br.moap.clustering.api.ICluster;

public class PointCluster implements ICluster<Point> {

	private int id;
	private List<Point> points;
	
	public PointCluster(int id) {
		this.id = id;
		this.points = new ArrayList<Point>();
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public List<Point> getObjects() {
		return this.points;
	}

	@Override
	public String toString() {
		return points.toString();
	}
}
