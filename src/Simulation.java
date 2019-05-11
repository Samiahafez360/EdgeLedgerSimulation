import java.util.PriorityQueue;

public class Simulation {

	// parameters for the Simulation loaded from the configuration file
	int startingNofHelpers;
	int difficulty;
	long simTime;
	
	// in hours
	double malRatio;
	
	public Simulation(){
 		startingNofHelpers = Integer.parseInt(SimulationProperties.getInstance().getParameter("StartingHelpers"));
		difficulty = Integer.parseInt(SimulationProperties.getInstance().getParameter("Difficulty"));
		//System.out.println(startingNofHelpers+"####"+difficulty);
		simTime = Long.parseLong(SimulationProperties.getInstance().getParameter("SimTime"));
	}
	public double startSimulation(){
		Controller c = new Controller(startingNofHelpers);
		//System.out.println("Controller");
		PriorityQueue<Helper> helpers = new PriorityQueue<Helper>();
		int i = startingNofHelpers;
		//setting up calc times
		while (i>0) {
			Helper h = new Helper();
			//System.out.println("Helpers ... ");
			//Calculate when the helper will finish
			h.calcMiningTime(c.getIndividualRange());
			helpers.add(h);
			i--;
		}
		//start mining
		Helper h = helpers.poll();
		while(h!=null) {
			try {
				Thread.sleep((long) Math.ceil(
						h.getMiningTime()/Long.parseLong(
								SimulationProperties.
								getInstance().
								getParameter("OneMinDuration"))));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (h.getNonceFound()) {
				//the nonce is found, we are exiting 
				//System.out.println (startingNofHelpers+"\t"+h.getMiningTime());
				return h.getMiningTime();
			}
		}
		return 0;
	}
	public static void main (String[] args) {
		Simulation s = new Simulation();
		while(s.startingNofHelpers<=1000) {
			double avofminingtime= 0;
			for (int i = 0; i<1000; i++) {
				avofminingtime += s.startSimulation();
				
			}
			avofminingtime= avofminingtime/1000;
			System.out.println ("HOHOHO"+s.startingNofHelpers+"\t"+avofminingtime);
			
			s.startingNofHelpers+=10;
		}
	}
}
