package mpc;

import basic.Helper;
import basic.LevelofTrustRandomGenerator;
import basic.SimulationProperties;
import basic.StayTimeRandomGenerator;

public class MPCHelper extends Helper {

	double SHA_ops[];
	boolean malicious;
	public MPCHelper() {
		
		timeInNetwork = StayTimeRandomGenerator.getIntance().getNextStayTime();
		
		//TODO
		//degreeoftrust = Double.parseDouble(SimulationProperties.getInstance().getParameter("InitialDegreeofTrust"));
		// the degree of trust TO be CHANGED 
		//degreeoftrust =LevelofTrustRandomGenerator.getIntance().getNextTrustLevel();
		
		degreeoftrust = 1;//Math.ceil(degreeoftrust*10)/10;
		SHA_ops[0] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op0"));
		SHA_ops[1] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op1"));
		SHA_ops[2] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op2"));
		SHA_ops[3] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op3"));
		SHA_ops[4] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op4"));
		SHA_ops[5] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op5"));
		malicious = LevelofTrustRandomGenerator.getIntance().isMalicioushelper();
		
	}
	double getCollectiveSHA() {
		double sum = 0;
		for (int i =0; i <SHA_ops.length; i++) {
			sum += SHA_ops[i];
			
		}
		return sum;
	}
	
	@Override
	public double calcMiningTime(double startingtime, long individualRange) {
		// TODO Auto-generated method stub
		
	}
	
}
