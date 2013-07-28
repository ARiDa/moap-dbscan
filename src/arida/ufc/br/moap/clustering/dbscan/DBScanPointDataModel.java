package arida.ufc.br.moap.clustering.dbscan;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import arida.ufc.br.moap.core.spi.IDataModel;

public class DBScanPointDataModel implements IDataModel<PointCluster> {

	private Map<String, PointCluster> pointClusters = new HashMap<String, PointCluster>();
	
	public DBScanPointDataModel() {
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public Collection<PointCluster> getInstances() {
		throw new DBScanException("Not supported operation.");
	}

	public PointCluster getInstance(String clusterName) {
		return pointClusters.get(clusterName);
	}
	

	@Override
	public void addInstance(PointCluster instance) {
		throw new DBScanException("Not supported operation.");
	}

	public void addInstance(String clusterName, PointCluster instance) {
		this.pointClusters.put(clusterName, instance);
	}

	
	@Override
	public String toString() {
		return pointClusters.toString();
	}
	
}
