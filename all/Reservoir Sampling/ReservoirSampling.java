package temp;

import java.util.Random;

/**
 * reference：http://www.cnblogs.com/HappyAngel/archive/2011/02/07/1949762.html
 * @author datawlb
 *
Init : a reservoir with the size： k
for i= k+1 to N
    M=random(1, i);
    if( M < k)
     SWAP the Mth value and ith value
end for 
 */
public class ReservoirSampling {

	public static int[] reservoirSample(int[] data, int k){
		if((data == null)||data.length <= k)
			return new int[0];
		int[] sampleResult = new int[k];
		for(int i=0; i<k; i++)
			sampleResult[i] = data[i];
		for(int i=k; i<data.length; i++){
			int random = new Random().nextInt(i+1);
			if(random < k)
				sampleResult[random] = data[i];
		}
		return sampleResult;
	}
	public static void main(String[] args){
		int k = 50;
		int n = 1000;
		int[] data = new int[n];
		for(int i=0; i<n; i++)
			data[i] = i;
		int[] result = reservoirSample(data, k);
		System.out.println(result.length);
	}
}
