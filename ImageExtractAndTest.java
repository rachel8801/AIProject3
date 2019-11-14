package main;
import java.io.IOException;
import java.util.ArrayList;

import ImageExtraction.ImageExtraction;
import algorithm.NaiveBayes;
import algorithm.Perceptron;
import objects.TestObject;

/*
 * Author : Charmian Goh
 */
public class ImageExtractAndTest {

	public static void main(String[] args) throws IOException{
		//object to extract images from file
		ImageExtraction extract = new ImageExtraction();
		//extract training digits
		ArrayList<TestObject> sample_space = extract.imageExtract(ImageExtraction.Type.FACE_TRAIN);

		//instantiate naive bayes object with training sample
		double[] prior_p = getPrior(extract.getfrequency(), extract.getTotal());
		double[] frequency = extract.getfrequency();

		//params are: #occurence of each digit, prior probability of each digit, training data,
		//			  type of object testing, number of times training data is tested to increased
		//			  likelihood and accuracy.

		//get a subset of the sample space
		ArrayList<TestObject> subset1 = ImageExtraction.getSampleSet(1, sample_space, 10);
		//ArrayList<TestObject> subset2 = getSampleSet(0.5, sample_space);
		NaiveBayes nb = new NaiveBayes(frequency,prior_p, subset1, NaiveBayes.Type.FACE, 1);
		//Perceptron pn = new Perceptron (subset1,Perceptron.Type.DIGIT);
		//print out likelihood array
		//nb.printLikelihood(0);


		//extract image for testing
		ArrayList<TestObject> test = extract.imageExtract(ImageExtraction.Type.FACE_TEST);
		int t=0;
		while(t <=5) {
		//checking accuracy
		int matched = 0;
		for(int i = 0;  i < test.size(); i++){
			boolean match = nb.test(test.get(i), nb.getK());
			if(match){
				matched++;
			}
		}
		System.out.println("accuracy: " + (matched*1.0)/test.size());
		t++;
		}
	}

	private static double[] getPrior(double[] frequency, double total) {
		double[] prior = new double[10];
		for(int i = 0; i < 10 ; i++){
			prior[i] = frequency[i]/total;
		}

		return prior;
	}

}
