package mpc;

public class SimulationClock {

	private  double time;
	private static SimulationClock instance;
	private SimulationClock() {
		time = 0;
	}
	public static SimulationClock getInstance () {
		if (instance ==null) {
			instance = new SimulationClock();
		}
		return instance;
		
	}
	public double getTime() {
		return time;
	}
	public void advanceTime(double time) {
		this.time += time;
	}
	
}
