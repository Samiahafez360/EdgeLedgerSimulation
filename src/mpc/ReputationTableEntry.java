package mpc;

public class ReputationTableEntry implements Comparable<ReputationTableEntry>{
	int helperid ;
	double reputation;
	public int getHelperid() {
		return helperid;
	}
	public ReputationTableEntry(int helperid, int reputation) {
		super();
		this.helperid = helperid;
		this.reputation = reputation;
	}
	public void setHelperid(int helperid) {
		this.helperid = helperid;
	}
	public double getReputation() {
		return reputation;
	}
	public void setReputation(int reputation) {
		this.reputation = reputation;
	}
	@Override
	public int compareTo(ReputationTableEntry o) {
		if (this.helperid> o.helperid) return 1;
		if (this.helperid< o.helperid) return -1;
		else return 0;
	}

}
