package arida.ufc.br.moap.clustering.dbscan;

import arida.ufc.br.moap.function.api.IDistanceFunction;

public class DBScanDistanceFunction implements IDistanceFunction<Point> {

	public DBScanDistanceFunction() {
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public Double evaluate(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.getLon() - p2.getLon(), 2) + Math.pow(p1.getLat() - p2.getLat(), 2));
	}

}
