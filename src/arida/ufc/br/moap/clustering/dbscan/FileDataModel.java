package arida.ufc.br.moap.clustering.dbscan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class FileDataModel<T> implements InputDataModel<T> {
	private BufferedReader br;
	
	public FileDataModel() {
	}
	
	public FileDataModel(File f) {
		setFile(f);
	}
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public Collection<T> getInstances() {
		throw new RuntimeException("Not supported operation.");
	}

	@Override
	public void addInstance(T instance) {
		throw new RuntimeException("Not supported operation.");
	}
	
	@Override
	public String next() {
		try {
			return br.readLine();
		} catch (IOException e) {
			throw new RuntimeException("Could not read line from file.");
		}
	}

	private void setFile(File file) {
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			new IllegalArgumentException("File not found: " + e.getMessage());
		}
	}

	@Override
	public void close() {
		try {
			br.close();
		} catch (IOException e) {
			throw new RuntimeException("Could not close file.");
		}
	}
}
