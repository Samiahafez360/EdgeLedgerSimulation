package mpc;

import java.util.ArrayList;

import basic.LevelofTrustRandomGenerator;

public class Verifier {
	boolean malicious;
	ArrayList<VerificationRequest> verificationRequests;
	
	public Verifier() {
		malicious = LevelofTrustRandomGenerator.getIntance().isMalicioushelper();	
	}
	
	public void assignProvers(ArrayList<VerificationRequest> verificationRequests) {
		this.verificationRequests = verificationRequests;
	}
	
	ArrayList<VerificationResponse> verificationresult;
	
	public ArrayList<VerificationResponse> verify(){
		
		//TODO
		
		return verificationresult;
	}
}
