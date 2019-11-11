package mpc;

class VerificationRequest {

	MPCHelper helper;
	
	int range;
	double percentage;
	public VerificationRequest(MPCHelper helper, int range, double percentage) {
		super();
		this.helper = helper;
		this.range = range;
		this.percentage = percentage;
	}
	
	
}
