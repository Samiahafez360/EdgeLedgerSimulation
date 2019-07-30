package basic;

public class Helper implements Comparable<Helper> {
	protected double timeInNetwork;
	protected double degreeoftrust;
	protected double finishminingtime;
	protected double nonceProb;
	protected double maxTime;
	protected double intervalTimetrusted;
	protected double intervalTimenottrusted;
	
	public Helper() {
		timeInNetwork = StayTimeRandomGenerator.getIntance().getNextStayTime();
		degreeoftrust = Double.parseDouble(SimulationProperties.getInstance().getParameter("InitialDegreeofTrust"));
		// the degree of trust TO be CHANGED 
		degreeoftrust =LevelofTrustRandomGenerator.getIntance().getNextTrustLevel();
		
		degreeoftrust = 1;//Math.ceil(degreeoftrust*10)/10;
		
		//System.out.println("Degreeof trust: "+degreeoftrust);
	
		intervalTimetrusted = Double.parseDouble(SimulationProperties.getInstance().getParameter("TimeofHash"));
		
		intervalTimenottrusted = Double.parseDouble(SimulationProperties.getInstance().getParameter("TimeofHashwithzkproof"));
			
		
	}

	@Override
	public int compareTo(Helper o) {
		if(this.finishminingtime>= o.finishminingtime)return 0;
		else return 1;
	}

	public double calcMiningTime(double startingtime,long individualRange) {
		// we should count for the network overhead
		
		maxTime= individualRange*degreeoftrust*intervalTimetrusted +
				 individualRange*(1-degreeoftrust)*intervalTimenottrusted;
		
		nonceProb = FindtheNonceGenerator.getIntance(individualRange).isNonceFound();
		//System.out.println(individualRange);
		//System.out.println(nonceProb);
		//System.out.println(intervalTime);
		//System.out.println(maxTime);
		
		finishminingtime = startingtime +FindtheNonceGenerator.getIntance(individualRange).timetoNonce()* 
				(degreeoftrust*intervalTimetrusted + (1-degreeoftrust)*intervalTimenottrusted);
		if (nonceProb >0) {
			while (finishminingtime> maxTime)
			finishminingtime = startingtime +FindtheNonceGenerator.getIntance(individualRange).timetoNonce()* 
					(degreeoftrust*intervalTimetrusted + (1-degreeoftrust)*intervalTimenottrusted);
		}else {
			finishminingtime= startingtime +maxTime;
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
