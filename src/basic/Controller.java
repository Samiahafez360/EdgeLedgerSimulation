package basic;

public class Controller {

	Long range;
	boolean fixed;
	protected int startingNofHelpers;
	public Controller(int startingNofHelpers) {
		int diff = Integer.parseInt(SimulationProperties.getInstance().getParameter("Difficulty"));
		fixed = Boolean.parseBoolean(SimulationProperties.getInstance().getParameter("Fixed"));
		this.startingNofHelpers = startingNofHelpers;
		range = (long) Math.pow(2, diff+8);
	}
	
	public long getIndividualRange() {
		if (fixed) {
			return (long)(Math.ceil(1.0*range/startingNofHelpers/32)*32);
		}else {
			//Estimated number of helpers to arrive during the mining 100
			return (long)(Math.ceil(1.0*range/(100+startingNofHelpers)/32)*32);
		}
	}
}
