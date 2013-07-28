package arida.ufc.br.moap.clustering.dbscan;

import arida.ufc.br.moap.core.spi.IDataModel;

public interface InputDataModel<T> extends IDataModel<T> {

	public abstract String next();
	
	public abstract void close();

}