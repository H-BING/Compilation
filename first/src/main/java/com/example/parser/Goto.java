package com.example.parser;


public class Goto {
	
	int Cid;
	String B;
	
	public Goto(int id, String string) {
		// TODO Auto-generated constructor stub
		Cid = id;
		B = string;
	}
	
	public Goto() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((B == null) ? 0 : B.hashCode());
		result = prime * result + Cid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goto other = (Goto) obj;
		if (B == null) {
			if (other.B != null)
				return false;
		} else if (!B.equals(other.B))
			return false;
		if (Cid != other.Cid)
			return false;
		return true;
	}

//    @Override
//    public int hashCode()
//    {
//        int result = 17;
//        result = 31 * result + Cid;
//        result = 31 * result + B;
//        return result;
//    }
	
//	public int hashCode() {
//		 return B.hashCode() + Cid;
//	}
	
//	public boolean equals(Object o){
//		  return (o instanceof Goto) && 
//		                (Cid ==((Goto)o).Cid) && 
//		                (B == null && ((Goto)o).B == null || 
//		                    B.equals(((Goto)o).B)
//		                );
//	}
	
}
