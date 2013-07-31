package arida.ufc.br.moap.clustering.dbscan;

public class Point {
	
	private double lat;
	private double lon;
	private boolean iscore;
	private int neigh;
	private boolean isvisited;
	private int cluster;
	private boolean isNoise;


	public Point(double lat, double lon) {
		setIscore(false);
		setLat(lat);
		setLon(lon);
		setNeigh(0);
		setIsvisited(false);
		setNoise(false);
	}
	
	public Point(double lat, double lon, boolean iscore, int neigh, int cluster, boolean isNoise) {
		setIscore(iscore);
		setLat(lat);
		setLon(lon);
		setNeigh(neigh);
		setNoise(isNoise);
		setCluster(cluster);
	}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public boolean isCore() {
		return iscore;
	}
	public void setIscore(boolean iscore) {
		this.iscore = iscore;
	}
	public int getNeigh() {
		return neigh;
	}
	public void setNeigh(int neigh) {
		this.neigh = neigh;
	}

	public boolean isVisited() {
		return isvisited;
	}

	public void setIsvisited(boolean isvisited) {
		this.isvisited = isvisited;
	}
	
	public int getCluster() {
		return cluster;
	}

	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	
	public boolean isNoise() {
		return isNoise;
	}

	public void setNoise(boolean isNoise) {
		this.isNoise = isNoise;
	}
	
	public void increaseNeigh(){
		this.neigh++;
	}

	@Override
	public String toString() {
		return String.valueOf(cluster);
		//return "Point [lat=" + lat + ", lon=" + lon + ", iscore=" + iscore
		//		+ ", neigh=" + neigh + ", isvisited=" + isvisited
		//		+ ", cluster=" + cluster + ", isNoise=" + isNoise + "]";
	}

}




