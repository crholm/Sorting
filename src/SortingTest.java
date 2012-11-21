import java.util.Arrays;
import java.util.LinkedList;


public class SortingTest {

	LinkedList<SortingAlgorithm> algs = new LinkedList<SortingAlgorithm>();
	int ref[];
	
	public void addAlgorithem(SortingAlgorithm alg){
		algs.add(alg);
	}
	
	public void runTest(int arraySize, int lowerLimit, int upperLimit){
		int array[] = new int[arraySize];
		
		for(int i = 0; i < arraySize; i++){
			array[i] = (int)(Math.random()*(upperLimit-lowerLimit))+lowerLimit;
		}
		
		
		
		ref = new int[arraySize];
		System.arraycopy(array, 0, ref, 0, arraySize);
		long time = System.currentTimeMillis();
		Arrays.sort(ref);
		time = System.currentTimeMillis() - time;
		System.out.println("Reference java sort: " + time);
		
		
		for (SortingAlgorithm alg : algs) {
			int subject[] = new int[arraySize];
			System.arraycopy(array, 0, subject, 0, arraySize);
			
			time = System.currentTimeMillis();
			subject = alg.sort(subject, lowerLimit, upperLimit);
			time = System.currentTimeMillis() - time;
			
			String sorted = isSorted(subject) ? "is sorted" : "is NOT sorted, diff " + diff;
			System.out.println(alg.getName() + " took " + time + "ms and " + sorted );			
		}
		
		
	
	}
	
	public boolean isSorted(int array[]){
		int last = -1;
		for(int i = 0; i < array.length; i++){
			if(array[i] < last){
				calcDiff(array);
				return false;
			}
				
			last = array[i];
		}
		return true;
	}
	
	double diff;
	private void calcDiff(int[] array) {
		int count = 0;
		for(int i = 0; i< array.length; i++){
			if(array[i] != ref[i])
				count++;
		}
		diff = (double)count/(double)array.length;		
	}

	public static void main(String args[]){
		int size = 1000000;
		int lowerLimit = 1;
		int upperLimit = 10000000;
		
		SortingTest test = new SortingTest();	
		//test.addAlgorithem(new InsertionSort());
		//test.addAlgorithem(new Flashsort());
		//test.addAlgorithem(new FlashsortMultiThreaded());
		test.addAlgorithem(new QuickSort());
		test.addAlgorithem(new QuickSortThreaded());
		
		
		test.runTest(size, lowerLimit, upperLimit);
	}
	 
}
