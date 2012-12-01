import java.util.Arrays;
import java.util.LinkedList;


public class SortingTest {
	private int[] sortedArray[];
	
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
		
		
		
		
		
		System.out.println(arraySize + " random elements to sort.");
		
		
		
		for (SortingAlgorithm alg : algs) {
			int subject[] = new int[arraySize];
			System.arraycopy(array, 0, subject, 0, arraySize);
			
			long time = System.currentTimeMillis();
			subject = alg.sort(subject, lowerLimit, upperLimit);
			time = System.currentTimeMillis() - time;
			
			String sorted = isSorted(subject) ? "\tOK" : "\tNOT sorted, diff: " + diff;
			System.out.print(alg.getName() + ":");
			int preTabs = (alg.getName().length()+1)/8;
			for(int i = 0; i< 4-preTabs; i++){
				System.out.print("\t");
			}
			System.out.println(time + "ms" + sorted);
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
		ref = new int[array.length];
		System.arraycopy(array, 0, ref, 0, array.length);
		Arrays.sort(ref);
		
		for(int i = 0; i< array.length; i++){
			if(array[i] != ref[i])
				count++;
		}
		diff = (double)count/(double)array.length;		
	}

	public static void main(String args[]){
		int size = 10000000;
		int lowerLimit = 1;
		int upperLimit = 10000000;
		
		SortingTest test = new SortingTest();	
		
//		test.addAlgorithem(new JavaStandardSort());
//		test.addAlgorithem(new InsertionSort());
		test.addAlgorithem(new FlashSort());
//		test.addAlgorithem(new FlashSortOriginal());
		test.addAlgorithem(new FlashSortThreaded());
		test.addAlgorithem(new FlashSortSampled());
//		test.addAlgorithem(new QuickSortHybrid());
//		test.addAlgorithem(new QuickSortHybridThreaded());
//		test.addAlgorithem(new RadixSort());
		
		
		test.runTest(size, lowerLimit, upperLimit);
		System.exit(0);
	}
	 
}
