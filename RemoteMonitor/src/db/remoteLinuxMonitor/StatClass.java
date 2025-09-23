package db.remoteLinuxMonitor;

import java.util.Map;

public class StatClass {
	
	private Map<Long,Long> map = null;
	private Double sum;
	private Double avg;
	
	public StatClass() {}

	public StatClass(Map<Long, Long> map, Double sum, Double avg) {
		this.map = map;
		this.sum = sum;
		this.avg = avg;
	}

	public Map<Long, Long> getMap() {
		return map;
	}

	public void setMap(Map<Long, Long> map) {
		this.map = map;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public Double getAvg() {
		return avg;
	}

	public void setAvg(Double avg) {
		this.avg = avg;
	}
}
