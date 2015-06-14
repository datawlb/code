package logistic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**

 */
public class GetData {

    public List<Instance>  getData(String filePath) throws FileNotFoundException {
    	// default is iris.data
    	// Iris-setosa:0, Iris-versicolor:1,  Iris-virginica:2
        List<Instance> data = new ArrayList<Instance>();
        Scanner scanner = new Scanner(new File(filePath));
        int label = 2;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] strArr = line.split(",");
            double[] tempData = new double[strArr.length-1];
            for(int i=0; i<strArr.length-1; i++){
            	tempData[i] = Double.valueOf(strArr[i]);
            }
            if(strArr[strArr.length-1].equals("Iris-setosa"))
            	label = 0;
            else if(strArr[strArr.length-1].equals("Iris-versicolor"))
            	label = 1;
            else
            	label = 2;
   
            if(label!=2){
            	Instance instance = new Instance(tempData, label);
                data.add(instance);
            }
            
        }
        scanner.close();
        return data;
    }
}
