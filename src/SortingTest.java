import java.util.LinkedList;


public class SortingTest {

	LinkedList<SortingAlgorithm> algs = new LinkedList<SortingAlgorithm>();
	
	public void addAlgorithem(SortingAlgorithm alg){
		algs.add(alg);
	}
	
	public void runTest(int arraySize, int lowerLimit, int upperLimit){
		int array[] = new int[arraySize];
		
		for(int i = 0; i < arraySize; i++){
			array[i] = (int)(Math.random()*(upperLimit-lowerLimit))+lowerLimit;
		}
		
		
		for (SortingAlgorithm alg : algs) {
			int subject[] = new int[arraySize];
			System.arraycopy(array, 0, subject, 0, arraySize);
			
			long time = System.currentTimeMillis();
			subject = alg.sort(subject, lowerLimit, upperLimit);
			time = System.currentTimeMillis() - time;
			String sorted = isSorted(subject) ? "is sorted" : "is NOT sorted";
			System.out.println(alg.getName() + " took " + time + "ms and " + sorted );
			
			
			
			
		}
	}
	
	public boolean isSorted(int array[]){
		int last = -1;
		for(int i = 0; i < array.length; i++){
			if(array[i] < last)
				return false;
			last = array[i];
		}
		return true;
	}
	
	
	public static void main(String args[]){
		int size = 1000000;
		int lowerLimit = 1;
		int upperLimit = 1000000000;
		
		SortingTest test = new SortingTest();	
		//test.addAlgorithem(new InsertionSort());
		test.addAlgorithem(new Flashsort());
		test.addAlgorithem(new FlashsortMultiThreaded());
		
		
		
		test.runTest(size, lowerLimit, upperLimit);
	}
	 
}
