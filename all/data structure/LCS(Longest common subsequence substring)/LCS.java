package string;
/**最长公共子序列(非连续)，即Longest Common Subsequence，LCS  (最长公共子串是连续的)
 * 和最长递增子序列的区别(峰值队列)
 * @author datawlb
 *
 */
public class LCS {
	public int[][] lcs(String s1, String s2){
		int[][] matrix = new int[s1.length()+1][s2.length()+1];
		// flag=0(默认),-1(左边),1(上边),3(斜上角),flag即是matrix的路径
		int[][] flag = new int[s1.length()+1][s2.length()+1];
		for(int i=1; i<=s1.length(); i++)
			for(int j=1; j<=s2.length(); j++){
				if(s1.charAt(i-1) == s2.charAt(j-1)){
					matrix[i][j] = matrix[i-1][j-1] + 1;
					flag[i][j] = 3;
				}
				else{
					if(matrix[i-1][j-1]<matrix[i][j-1]||matrix[i-1][j-1]<matrix[i-1][j]){
						
					}
					
					if(matrix[i][j-1] > 0 || matrix[i-1][j] > 0){
						if(matrix[i][j-1] > matrix[i-1][j]){
							matrix[i][j] = matrix[i][j-1];
							flag[i][j] = -1;
						}
						else{
							matrix[i][j] = matrix[i-1][j];
							flag[i][j] = 1;
						}
					}

				}
			}
		//return matrix;
		return flag;
	}
	// TODO:
	public void printLCS(int[][] flag, String s1, String s2, int i, int j){
		if(i==0||j==0)
			return;
		if(flag[i][j]==0){
			//System.out.print(s1.charAt(i-1));
			return;
			//printLCS(flag, s1, s2, i-1, j-1);
		}else if(flag[i][j]==3){
			System.out.print(s2.charAt(j-1));
			printLCS(flag, s1, s2, i-1, j-1);
		}else{
			if(flag[i][j]>0){
				//System.out.print(s2.charAt(j-1));
				printLCS(flag, s1, s2, i-1, j);
			}else{
				//System.out.print(s1.charAt(i-1));
				printLCS(flag, s1, s2, i, j-1);
			}
		}
	}
	public static void main(String[] args){
		String s1 = "wlblu";
		String s2 = "lluwlb";
		System.out.println(s1.charAt(1)==s2.charAt(2));
		LCS lcs = new LCS();
		int[][] matrix = lcs.lcs(s1, s2);
		//System.out.println(matrix[s1.length()][s2.length()]);
		int[][] flag = lcs.lcs(s1, s2);
		lcs.printLCS(flag, s1, s2, s1.length(), s2.length());
		System.out.println();
		lcs.printLCS(flag, s1, s2, s1.length(), s2.length()-1);
	}
}
