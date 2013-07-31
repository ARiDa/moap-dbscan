package arida.ufc.br.moap.clustering.dbscan;

import java.util.ArrayList;
import java.util.Collection;

import arida.ufc.br.moap.core.spi.IDataModel;

public class DBScanPointDataModel implements IDataModel<PointCluster> {

	private Collection<PointCluster> pointClusters = new ArrayList<PointCluster>();
	
	public DBScanPointDataModel() {
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public Collection<PointCluster> getInstances() {
		return this.pointClusters;
	}

	@Override
	public void addInstance(PointCluster instance) {
		this.pointClusters.add(instance);
	}

	@Override
	public String toString() {
		return pointClusters.toString();
	}
	
}
