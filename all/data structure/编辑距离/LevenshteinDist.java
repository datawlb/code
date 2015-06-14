package string;

public class LevenshteinDist {

	
	public static void main(String[] args){
		String s1 = "lluwlb";
		String s2 = "wlbll";
		//String s1 = "osailn";
		//String s2 = "ofailing";
		int stt = 0;
		int temp = 0;
		int[][] mar = new int[s1.length()][s2.length()];
		if(s1.charAt(0)!=s2.charAt(0))
			mar[0][0] = 1;
		for(int i=1; i<s1.length(); i++){
			if(s1.charAt(i)==s2.charAt(0))
				stt = 0;
			else
				stt = 1;
			mar[i][0] = mar[i-1][0] + stt;
		}
		for(int j=1; j<s2.length(); j++){
			if(s2.charAt(j)==s1.charAt(0))
				stt = 0;
			else
				stt = 1;
			mar[0][j] = mar[0][j-1] + stt;
		}
		for(int i=1; i<s1.length(); i++){
			for(int j=1; j<s2.length(); j++){
				if(s1.charAt(i)==s2.charAt(j))
					stt = 0;
				else
					stt = 1;
				temp = Math.min(mar[i-1][j]+1, mar[i][j-1]+1);
				temp = Math.min(temp, mar[i-1][j-1]+stt);
				mar[i][j] = temp;
			}
		}
		System.out.println(mar[s1.length()-1][s2.length()-1]);
	}
}
