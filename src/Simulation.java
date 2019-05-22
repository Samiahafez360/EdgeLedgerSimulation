import java.util.ArrayList;
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
			h= helpers.poll();
		}
		return 0;
	}
	public void startSimulation_exp2(){
		
		Controller c = new Controller(startingNofHelpers);
		//System.out.println("Controller");
		PriorityQueue<Helper> helpersqueue = new PriorityQueue<Helper>();
		ArrayList<Helper> helperslist = new ArrayList<Helper>();
		int i = startingNofHelpers;
		
		
		//Assigning the helpers 
		while (i>0) {
			Helper h = new Helper();
			//System.out.println("Helpers ... ");
			//Calculate when the helper will finish
			h.calcMiningTime(c.getIndividualRange());
			helpersqueue.add(h);
			helperslist.add(h);
			i--;
		}
		
		long time = 0;
		Helper h;
		while (time <simTime) {
			//mining one block
			i = startingNofHelpers;
			//TODO revisit
			long indvidualrange = c.getIndividualRange();
			while (i>0) {
				h = helperslist.get(i-1);
				//System.out.println("Helpers ... ");
				//Calculate when the helper will finish
				//TODO we have to embed the calculation of semitrusted
				h.calcMiningTime(indvidualrange);
				helpersqueue.add(h);
				i--;
			}
			//start mining
			i = startingNofHelpers;
			h = helpersqueue.poll();
			while (i>0|| h!=null) {
				
				time += (long) Math.ceil(
						h.getMiningTime()/Long.parseLong(
								SimulationProperties.
								getInstance().
								getParameter("OneMinDuration")));
				
				if (h.getNonceFound()) {
					//the nonce is found, we are exiting 
					System.out.println (startingNofHelpers+"\t"+h.getMiningTime());
					// setting i=0 to exit the smaller loop 
					i=0;
				}
				h = helpersqueue.poll();
			}
		}
	}
	public static void main (String[] args) {
		Simulation s = new Simulation();
		while(s.startingNofHelpers<=1000) {
			double avofminingtime= 0;
			for (int i = 0; i<1; i++) {
				//avofminingtime += s.startSimulation();
				s.startSimulation_exp2();
			}
			avofminingtime= avofminingtime/100;
			System.out.println (""+s.startingNofHelpers+"\t"+avofminingtime);
			
			s.startingNofHelpers+=10;
		}
	}
}
