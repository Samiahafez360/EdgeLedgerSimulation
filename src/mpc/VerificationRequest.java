package mpc;

class VerificationRequest {

	MPCHelper helper;
	
	int range;
	int percentage;
	public VerificationRequest(MPCHelper helper, int range, int percentage) {
		super();
		this.helper = helper;
		this.range = range;
		this.percentage = percentage;
	}
	
	
}
