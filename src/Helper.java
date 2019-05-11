
public class Helper implements Comparable<Helper> {
	double timeInNetwork;
	double degreeoftrust;
	double finishminingtime;
	double nonceProb;
	private double maxTime;
	private double intervalTime;
	
	public Helper() {
		timeInNetwork = StayTimeRandomGenerator.getIntance().getNextStayTime();
		degreeoftrust = Double.parseDouble(SimulationProperties.getInstance().getParameter("InitialDegreeofTrust"));
		// the degree of trust TO be CHANGED 
		if (degreeoftrust> 0.5) {
			intervalTime = Double.parseDouble(SimulationProperties.getInstance().getParameter("TimeofHash"));
		}else {
			intervalTime = Double.parseDouble(SimulationProperties.getInstance().getParameter("TimeofHashwithzkproof"));
			
		}
	}

	@Override
	public int compareTo(Helper o) {
		if(this.finishminingtime>= o.finishminingtime)return 0;
		else return 1;
	}

	public double calcMiningTime(long individualRange) {
		maxTime= individualRange*intervalTime;
		nonceProb = FindtheNonceGenerator.getIntance(individualRange).isNonceFound();
		//System.out.println(individualRange);
		//System.out.println(nonceProb);
		//System.out.println(intervalTime);
		//System.out.println(maxTime);
		
		finishminingtime = FindtheNonceGenerator.getIntance(individualRange).timetoNonce();
		if (nonceProb >0) {
			while (finishminingtime> maxTime)
			finishminingtime = FindtheNonceGenerator.getIntance(individualRange).timetoNonce();
		}else {
			finishminingtime= maxTime;
		}
		//System.out.println(finishminingtime);
		return finishminingtime;
	}
	
	public double getMiningTime() {
		return finishminingtime;
		
	}
	public boolean getNonceFound() {
		return nonceProb> 0 ;
		
	}

}
