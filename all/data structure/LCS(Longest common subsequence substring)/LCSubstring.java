package string;

public class LCSubstring {
	int maxRow,maxCol;
	public int[][] lcs(String s1, String s2){
		int maxValue = 0;
		int[][] matrix = new int[s1.length()+1][s2.length()+1];

		for(int i=1; i<=s1.length(); i++)
			for(int j=1; j<=s2.length(); j++){
				if(s1.charAt(i-1) == s2.charAt(j-1)){
					matrix[i][j] = matrix[i-1][j-1] + 1;
					if (maxValue < matrix[i][j])
					maxValue = matrix[i][j]; 
					this.maxRow = i;
					this.maxCol = j;
				}
				
			}
		return matrix;

	}
	// TODO:
	public void printLCS(int[][] matrix, String s2, int i, int j){
		if(matrix[i][j]==0)
			return;
		else{
			System.out.print(s2.charAt(j-1));
			printLCS(matrix, s2, i-1, j-1);
		}
	}
	public static void main(String[] args){
		String s1 = "wlblu";
		String s2 = "lluwlb";
		System.out.println(s1.charAt(1)==s2.charAt(2));
		LCSubstring lcs = new LCSubstring();
		int[][] matrix = lcs.lcs(s1, s2);

		lcs.printLCS(matrix, s2, lcs.maxRow, lcs.maxCol);
		
	}
}
