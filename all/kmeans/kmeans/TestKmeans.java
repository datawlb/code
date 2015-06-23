package kmeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class TestKmeans {
	public static void main(String[] args) throws FileNotFoundException{
		List<double[]> data = new ArrayList<double[]>();
		List<Integer> label = new ArrayList<Integer>();
		int label0 = 2;
		String filePath = "C:/Users/Administrator/Desktop/iris.data";
        Scanner scanner = new Scanner(new File(filePath));
        
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] strArr = line.split(",");
            double[] tempData = new double[strArr.length-1];
            for(int i=0; i<strArr.length-1; i++){
            	tempData[i] = Double.valueOf(strArr[i]);
            }
            if(strArr[strArr.length-1].equals("Iris-setosa"))
            	label0 = 0;
            else if(strArr[strArr.length-1].equals("Iris-versicolor"))
            	label0 = 1;
            else
            	label0 = 2;
            
            label.add(label0);
            data.add(tempData);
        }
        scanner.close();
     
        Kmeans km = new Kmeans(2, 100, (double[][])data.toArray(new double[0][0]));
        int[] result = km.train();
        
		System.out.println("");
	}
}
