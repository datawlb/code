package kmeans;

public class Kmeans {
	private int k; 
	private int iterNB; 
	private double[][] center; 
	private double err; 
	private double[][] data; 
	private int[] result; 
	private int dim; 
	private int pointNB;

	private Double[] distance = new Double[k];

	public Kmeans(int k, int iterNB, double[][] data) {
		this.k = k;
		this.iterNB = iterNB;
		this.data = data;
		this.dim = data[0].length;
		this.pointNB = data.length;
		
		InitialSet();
	}

	private void InitialSet() {
		if (k <= 0)
			return;
		if (iterNB <= 0)
			return;
		center = new double[k][dim];
		// initial cluster center
		int temp;
		for (int i = 0; i < k; i++) {
			temp = (int) (Math.random() * pointNB);
			center[i] = data[temp];
		}
		result = new int[data.length];
	}
	public int[] train(){
		double dist = 0.0;
		double minDist = Double.MAX_VALUE;
		int minIndex = 0;
		int[] centerCount = new int[center.length];
		for(int itr=0; itr<iterNB; itr++){
			// each point get self's class
			for (int i = 0; i < pointNB; i++) {
				for (int j = 0; j < k; j++) {
					dist = distanceToCenter(data[i], center[j]);
					if(minDist>dist){
						minDist = dist;
						minIndex = j;
					}
					
				}
				result[i] = minIndex;
				minDist = Double.MAX_VALUE;
			}
			
			// update centers
			//1.centers initial, set 0
			for(int i=0; i<k; i++){
				for(int j=0; j<data[0].length; j++){
					center[i][j] = 0;
				}
				centerCount[i] = 0;
			}
			//2.update:sum center
			for(int i=0; i<pointNB; i++){
				for(int j=0; j<data[0].length; j++){
					
					center[result[i]][j] = center[result[i]][j] + data[i][j]; 
				}
				centerCount[result[i]] = centerCount[result[i]] + 1;
			}
			//3.update:averge center
			for(int i=0; i<k; i++){
				for(int j=0; j<data[0].length; j++){
					center[i][j] = center[i][j]/centerCount[i]; 
				}
			}
			
			// TODO:Threshold judge
		}
		return result;
	}

	private double distanceToCenter(double[] point, double[] cent) {
		double dis = 0;
		for (int i = 0; i < dim; i++) {
			dis = dis + Math.pow(cent[i] - point[i], 2);
		}
		return Math.sqrt(dis);
	}



}
