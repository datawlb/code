package logistic;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class LogisticRegression {
	
    // the weight to learn
    private double[] weights;
    // the learning rate
    private double rate = 0.01;
    // the number of iterations
    private int iter = 100;
    // Stochastic gradient descent random
    private int random = 0;
    // predict threshold
    private double labelThreshold = 0.5;
    
    public LogisticRegression(int n) {
        weights = new double[n];
    }
    
    public void setIter(int value){
    	this.iter = value;
    }
    public void setRate(double value){
    	this.rate = value;
    }
    public void setRandom(int value){
    	this.random = value;
    }
    public void setlabelThreshold(double value){
    	this.labelThreshold = value;
    }
    
    private double logisticFuc(double[] x) {
        double vec = 0.0;
        for (int i=0; i<weights.length;i++)  {
            vec += weights[i] * x[i];
        }
        return 1 / (1 + Math.exp(-vec));
    }

    public void train(List<Instance> instances) {
    	double Likelihood = 0.0;
    	if(random==0){
        	// Batch gradient descent
            for (int ite=0; ite<iter; ite++) {
                
                for (int i=0; i<instances.size(); i++) {
                    double[] point = instances.get(i).getX();
                    double predicted = logisticFuc(point);
                    int label = instances.get(i).getLabel();
                    for (int j=0; j<weights.length; j++) {
                        weights[j] = weights[j] + rate * (label - predicted) * point[j];
                    }
                    
                    Likelihood += label * Math.log(logisticFuc(point)) + (1-label) * Math.log(1- logisticFuc(point));
                }
                
                System.out.println("iteration: " + ite + " " + Arrays.toString(weights) + " maxlike: " + Likelihood);
            }
    	}else{
    		// Stochastic gradient descent
    		if (random>=instances.size()){
    			System.out.println("Stochastic gradient descent: random set error!");
    			return;
    		}
    		HashSet<Integer> set = new HashSet<Integer>();  
    		for (int ite=0; ite<iter; ite++){
    			set.clear();
    			randomSet(0, instances.size()-1, random, set);
    		
                for (int i: set) {
                    double[] point = instances.get(i).getX();
                    double predicted = logisticFuc(point);
                    int label = instances.get(i).getLabel();
                    for (int j=0; j<weights.length; j++) {
                        weights[j] = weights[j] + rate * (label - predicted) * point[j];
                    }
                    
                    Likelihood += label * Math.log(logisticFuc(point)) + (1-label) * Math.log(1- logisticFuc(point));
                }
    		}
    	}

    }

    private double predict(double[] point) {
    	//double prob = 
        return logisticFuc(point);
        
    }
    // Generates random number
    public void randomSet(int min, int max, int n, HashSet<Integer> set){  
        if (n > (max - min + 1) || max < min) {  
            return;  
        }  
        for (int i = 0; i < n; i++) {  
            // Math.random()
            int num = (int) (Math.random() * (max - min)) + min;  
            set.add(num);// defferent add to HashSet
        }  
        int setSize = set.size();  
        // 
        if (setSize < n) {  
         randomSet(min, max, n - setSize, set);
        }  
    }
    
    public static void main(String args[]) throws FileNotFoundException {
    	GetData getD = new GetData();
        List<Instance> instances = getD.getData("C:/Users/Administrator/Desktop/iris.data");
        LogisticRegression logistic = new LogisticRegression(instances.get(0).getX().length);
        
        logistic.setRandom(10); // use Stochastic gradient descent
        logistic.train(instances);
        
        System.out.println("Probability: " + logistic.predict(instances.get(0).getX()));
        System.out.println("Probability: " + logistic.predict(instances.get(99).getX()));
   

    }
}
