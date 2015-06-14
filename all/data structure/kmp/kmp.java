package temp;
// you can see: http://blog.csdn.net/v_july_v/article/details/7041827
public class kmp {
	// build index
	public static int[] getIndex(String str){
		int[] index = new int[str.length()];
		index[0] = -1;
		int j = 0;
		for(int i=1; i<str.length(); i++){
			if(index[i-1]<0)
				j = 0;
			else
				j = index[i-1]+1;
			
			if(str.charAt(i)==str.charAt(j)){
				index[i] = index[i-1] + 1;
			}else{
				index[i] = -1;
			}
		}
		return index;
	}
	
	public static void main(String args[]){
		String bigstr = "BBCABCDABABCDABCDABDEggg";
		String str = "ABCDABD";
		int[] index = getIndex(str);
		int i=0,j=0;
		for(; i<bigstr.length();){
			if(bigstr.charAt(i)==str.charAt(j)){
				i++;
				j++;
				if(j>=str.length()){
					System.out.println(i);
					j=0;
					
				}
					
			}else{
				if(j>0){
					j = index[j-1] + 1;
				}else{
					j=0;
					i++;
				}
			}	
		}
		System.out.println(" ");
	}
}
