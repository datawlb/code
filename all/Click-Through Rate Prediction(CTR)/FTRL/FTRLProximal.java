package FTRL;
/**
 *  kaggle forum: https://www.kaggle.com/c/avazu-ctr-prediction/forums/t/10927/beat-the-benchmark-with-less-than-1mb-of-memory
 *  ftrl paper: http://www.eecs.tufts.edu/~dsculley/papers/ad-click-prediction.pdf
 */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

public class FTRLProximal {
	
	private double alpha = 0.1; // learning rate
	private double belta = 1;   // smoothing rate
	private double L1 = 1;      // 
	private double L2 = 1;      // 
	private int D = 1000000;    // number of weights
	//private int epoch = 1;      // repeat train times
	
	private double[] N; // sum of the gradient^2
	private double[] Z; 
	private double[] W;
	
	public FTRLProximal(int D){
		this.D = D;
		this.N = new double[D];
		this.Z = new double[D];
		this.W = new double[D]; 
	}
	/**
	 * 
	 * @param set: hash trick
	 * @param label: click=1,unclick=0
	 */
	public void train(HashSet<Integer> set, int label){
		Double p = 0.0;
		for (Integer i : set) {
			int sign = Z[i] < 0 ? -1 : 1;
			if (Math.abs(Z[i]) <= L1) {
				W[i] = 0.0;
			} else {
				W[i] = (sign * L1 - Z[i]) / ((belta + Math.sqrt(N[i])) / alpha + L2);
			}
			p += W[i];
		}
		
		// predict
		p = 1 / (1 + Math.exp(-p));
		
		// update
		Double g = p - label;
		for (Integer i : set) {
			Double sigma = (Math.sqrt(N[i] + g * g) - Math.sqrt(N[i])) / alpha;
			Z[i] += g - sigma * W[i];
			N[i] += g * g;
		}
		set.clear();
	}
	
	public double predict(HashSet<Integer> set){
		Double p = 0.0;
		for (Integer i : set) {
			p += W[i];
		}
		// predict
		p = 1 / (1 + Math.exp(-p));
		return p;
	}
	
	public double logloss(double p, int label){
		if (label == 1){
			p = -Math.log(p);
		}else{
			p = -Math.log(1.0 - p);
		}
		return p;
	}
	
	public static void main(String args[]){
		Double p = 0.0;
		
		int epoch = 1;      // repeat train times
		
		FTRLProximal ftrl = new FTRLProximal(100000);
		
		String trPath = "D:/work/project/Click-Through Rate Prediction/train.csv";
		String tePath = "D:/work/project/Click-Through Rate Prediction/test.csv";	
		String submissionPath = "D:/work/project/Click-Through Rate Prediction/submission.csv";
		
		BufferedReader br;
		String str = null;
		// train model
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(trPath), "UTF-8"));
			str = br.readLine();
			String name[] = str.split(",");
			String value[] = null;
			HashSet<Integer> set = new HashSet<Integer>();
			for (int epo = 0; epo < epoch; epo++) {				
				while ((str = br.readLine()) != null) {
					value = str.split(",");
					for (int i = 2; i < value.length; i++) {
						Integer hashValue = Math.abs((name[i] + "_" + value[i]).hashCode()) % ftrl.D;
						set.add(hashValue);
					}
					ftrl.train(set, Integer.parseInt(value[1]));
					set.clear();
				}
			}
		}catch(Exception e){
			System.out.println(" error.. ");
		}
		
		// predict result
		BufferedOutputStream bos;
		String string = null;
		byte[] newLine = "\r\n".getBytes();
		int count = 0;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(submissionPath));
			bos.write(("id,click").getBytes());
			bos.write(newLine);
			
			br = new BufferedReader(new InputStreamReader(new FileInputStream(tePath), "UTF-8"));
			string = br.readLine();
			String name[] = string.split(",");
			String value[] = null;
			HashSet<Integer> set = new HashSet<Integer>();
			
			while ((string = br.readLine()) != null) 
			{
				count++;
				value = string.split(",");
				for (int i = 1; i < value.length; i++) 
				{
					Integer hashValue = Math.abs((name[i] + "_" + value[i]).hashCode()) % ftrl.D;
					set.add(hashValue);
				}

				p = ftrl.predict(set);
				String result = value[0] + "," + p;
				bos.write(result.getBytes());
				bos.write(newLine);
				set.clear();
			}
			
			bos.flush();
			bos.close();
			System.out.println(count);

		} catch(Exception e){
			System.out.println(" error.. ");
		}
	}
}
